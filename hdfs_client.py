#!/usr/bin/env python
# coding=utf-8

import json
import os, sys
import requests
import subprocess
import shlex
from pyhive import hive
from util import log, hql, dev_email
import time
from hdfs import InsecureClient,HdfsError
from configuration import Configuration

root_dir = os.path.dirname(os.path.abspath(__file__))
sys.path.append(root_dir)

logger = log.getLogger("jd_measure")

HQL_RUNNING_DIR = os.path.join(root_dir, 'hql/running')

def main():
    conf = Configuration()
    conf_hdfs = conf.get_section_props('hdfs')
    hdfs_url = conf_hdfs.get('hdfs_url')
    user = conf_hdfs.get('user')
    password = conf_hdfs.get('password')
    jd_db_path = conf_hdfs.get('jd-db-path')
    jar_path = os.path.join(root_dir, conf_hdfs.get('jar_path'))
    main_jd_filter = conf_hdfs.get('main_jd_filter')
    main_jd_measure = conf_hdfs.get('main_jd_measure')
    main_timeline_sort = conf_hdfs.get('main_timeline_sort')
    main_archive = conf_hdfs.get('main_archive')
    input_path = conf_hdfs.get('input_path')
    jd_crawler_output = conf_hdfs.get('jd-crawler-output')
    jd_measure_output = conf_hdfs.get('jd-measure-output')

    hql.generate_hql_running()
    client = InsecureClient(hdfs_url, user=user)
    sub_paths = client.walk(input_path) # /Data/crawler_jd
    for sub_path in sub_paths:
        # /Data/crawler_jd/tmp 目录为文件上传路径，上传完毕后需改名 
        # /Data/crawler_jd/xxx 目录为工作目录，解析JD被量化后，文件压缩归档并删除此目录
        if sub_path[0] != input_path and sub_path[0] != os.path.join(input_path, 'tmp') and len(sub_path[1]) == 0 and len(sub_path[2]) > 0:
            jd_raw_input_path = sub_path[0]

            # Return names of files contained in /Data/crawler_jd/xxx
            jd_raws = client.list(jd_raw_input_path)
            if not jd_raws:
                # 删除空目录，并退出
                client.delete(jd_raw_input_path, recursive=False)
                exit(0)  

            # entity_id, content_sign和整个解析JD组成一行 
            while True:
                logger.info('run jd filter mapper')
                client.delete(jd_crawler_output, recursive=True)
                filter_res = run_task(jar_path, main_jd_filter, jd_raw_input_path, jd_crawler_output)
                if filter_res:
                    break

            # 过滤库中已经存在的JD
            while True:
                filter_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'jd_filter.hql')) 
                if filter_res:
                    break

            if not jd_crawler_filter():
                return
            # 量化JD
            while True:
                logger.info('run jd measure mapreduce.')
                # clear jd measure output path
                client.delete(jd_measure_output, recursive=True)
                # run mapreduce 
                mes_res = run_task(jar_path, main_jd_measure, os.path.join(jd_db_path, 'jd_crawler_filter'), jd_measure_output)
                if mes_res:
                    break

            # 将新旧jd_timeline 导入edw_cv.jd_timeline__c 表
            while True:
                logger.info('jd_timeline sort , set is_first and is_last.')
                hql_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'jd_timeline.hql'))
                if hql_res:
                    break

            input_timeline =  os.path.join(jd_measure_output, 'jd_timeline')
            output_timeline = os.path.join(jd_db_path, 'jd_timeline__b')

            # 排序jd_timeline  设置is_first/is_last
            while True:
                # clear jd_timeline sort output path
                client.delete(output_timeline, recursive=True)
                # run mapreduce
                sort_res = run_task(jar_path, main_timeline_sort, input_timeline , output_timeline)
                if sort_res:
                    logger.info('jd_timeline table rename')
                    ind_rename_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'jd_timeline_rename.hql'))
                    if not ind_rename_res:
                        dev_email.send_email(subject='[fail] jd量化入库 jd_timeline_rename.hql', message='jd_timeline_rename.hql 执行失败')
                    break

            # job_zhineng
            while True: 
                logger.info('job_zhineng set endtime.')
                zhineng_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'job_zhineng.hql'))
                if zhineng_res:
                    logger.info('job_zhineng table rename.')
                    zn_rename_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'job_zhineng_rename.hql'))
                    if not zn_rename_res:
                        dev_email.send_email(subject='[fail] jd量化入库 job_zhineng_rename.hql', message='job_zhineng_rename.hql 执行失败')
                    break

            # measure_inc_industry
            while True:
                logger.info('measure_inc_industry set endtime.')
                industry_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'measure_inc_industry.hql'))
                if industry_res:
                    logger.info('measure_inc_industry table rename')
                    ind_rename_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, 'measure_inc_industry_rename.hql'))
                    if not ind_rename_res:
                        dev_email.send_email(subject='[fail] jd量化入库 measure_inc_industry_rename.hql', message='measure_inc_industry_rename.hql 执行失败')
                    break

            # load data into table 
            load_data( 'jd_entry.hql',
                    'jd_inc.hql',
                    'jd_job.hql',
                    'jd_job_major.hql',
                    'jd_job_type.hql',
                    'jd_job_work_city.hql',
                    'jd_job_work_location.hql',
                    'jd_others.hql',
                    'jd_page.hql',
                    'load_max_rownum.hql')

            logger.info('archive crawler_jd.')
            archive_res = run_task(jar_path, main_archive, jd_raw_input_path, None)
            if archive_res:
                client.delete(jd_raw_input_path, recursive=True)
            else:
                dev_email.send_email(subject='[fail] jd量化入库 archive crawler_jd', message='JD 解析数据归档失败 执行失败') 

# 是否有解析JD需要量化
def jd_crawler_filter():
    conf = Configuration()
    config = conf.get_section_props('hive')
    conn_hive = hive.connect(host=config.get('host'), 
            port=config.get('port'),
            username=config.get('username'),
            auth=config.get('auth'),
            password=config.get('password'),
            configuration={'hive.execution.engine':'mr'})
    cursor = conn_hive.cursor() 
    cursor.execute("select * from {}.{} limit 1".format(conf.get_prop('db', 'jd-db-name'), 'jd_crawler_filter'))
    res = cursor.fetchone()
    cursor.close()
    conn_hive.close()
    if res is None or len(res) < 1:
        return False
    return True

def load_data(*hqls):
    for h in hqls:
        while True:
            load_res = hql.run_tasks(os.path.join(HQL_RUNNING_DIR, h))
            if load_res:
                break

def run_task(jar, main, input_p, output):
    command = '''
        hadoop jar %s %s %s %s
    ''' % (jar, main, input_p, output)
    process = subprocess.Popen(shlex.split(command))
    while True:
        return_code = process.poll()
        if return_code is None:
            time.sleep(5)
            continue
        if return_code != 0:
            logger.warn('fail task << %s >>...' % (command))
            return False
        else:
            logger.info('success task << %s >>.' % (command))
            return True
    
if __name__ == '__main__':
    main()

