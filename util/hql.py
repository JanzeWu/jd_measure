#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, sys
import time
import shlex
import subprocess
import collections
import log
import dev_email

root_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(root_dir)
from configuration import Configuration

logger = log.getLogger("jd_measure")

HQL_TEMPLATE_DIR = os.path.join(root_dir, 'hql/template')
HQL_RUNNING_DIR = os.path.join(root_dir, 'hql/running')

def run_tasks(hql):

    process = subprocess.Popen( shlex.split('''
        hive -f %s
    ''' % (hql))) 
    while True:
        return_code = process.poll()
        if return_code is None:
            time.sleep(5)
            continue

        if return_code != 0:
            msg = 'hql:%s run fail'%( hql)
            logger.error(msg)
            return False
        else:
            logger.info('hql:%s done.' % ( 
                hql
            ))
            return True



# 获取目录下的所有文件
def get_all_file_from_dir(dir_path):
    file_path_list = []
    files = os.listdir(dir_path)
    for file in files:
        file_path = os.path.join(dir_path, file)
        if os.path.isdir(file_path):
            for path in get_all_file_from_dir(file_path):
                file_path_list.append(path)
            continue
        else:
            file_path_list.append(file_path)

    return file_path_list

def generate_hql_running():
    '''
    根据hql_template生成可执行hql
    替换模板中的变量
    '''
    configuration = Configuration()
    conf_argvs = configuration.get_props()
    for script_hql_path in get_all_file_from_dir(HQL_TEMPLATE_DIR ):
        with open(script_hql_path, 'rb') as hql_src:
            dest_path = os.path.join(HQL_RUNNING_DIR, os.path.basename(script_hql_path))
            with open(dest_path, 'wb') as hql_dest:
                lines = ''
                for line in hql_src:
                    lines = lines + line
                for sec in conf_argvs:
                    for key in conf_argvs.get(sec):
                        lines = lines.replace('<' + key + '>', conf_argvs.get(sec).get(key))
                hql_dest.write(lines)

if __name__ == '__main__':
    generate_hql_running()
