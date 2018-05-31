set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_job_work_city(
    jd_id              string   comment    'jd的id，“entity_id+content_sign”的MD5值',
    job_work_city      string   comment    '职位发布城市/工作城市解析值',
    job_work_city_id   string   comment    '职位发布城市/工作城市ID，12个字符的国家行政区域码',
    rownum             bigint   comment    '行号',
    random_int         int      comment    '随机整数',
    etl_date           string   comment    'ETL日期');


drop table if exists <jd-db-name>.jd_job_work_city__b;

create external table if not exists <jd-db-name>.jd_job_work_city__b(
    jd_id              string   comment    'jd的id，“entity_id+content_sign”的MD5值',
    job_work_city      string   comment    '职位发布城市/工作城市解析值',
    job_work_city_id   string   comment    '职位发布城市/工作城市ID，12个字符的国家行政区域码',
    rownum             bigint   comment    '行号',
    random_int         int      comment    '随机整数',
    etl_date           string   comment    'ETL日期')
location '<jd-measure-output>/jd_job_work_city'; 


insert into table <jd-db-name>.jd_job_work_city
	select
        jd_id           
        ,job_work_city   
        ,job_work_city_id		
        ,min(rownum)
        ,min(random_int)
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
    	select distinct
    		b.jd_id           
            ,b.job_work_city   
            ,b.job_work_city_id
            ,b.rownum          
            ,b.random_int      
            ,b.etl_date        
    	from 
    		<jd-db-name>.jd_job_work_city__b b
    	left join
    		<jd-db-name>.jd_job_work_city a 
    	on 
                b.jd_id            = a.jd_id             
            and b.job_work_city    = a.job_work_city     
    	where 
    		a.jd_id is null
            or b.job_work_city_id <> a.job_work_city_id and not (b.job_work_city_id is null and  a.job_work_city_id is null) 
	) t
	group by 
		jd_id           
        ,job_work_city   
        ,job_work_city_id;

drop table if exists <jd-db-name>.jd_job_work_city__b;
