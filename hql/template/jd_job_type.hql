set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_job_type(
    jd_id          string   comment  'jd的id，“entity_id+content_sign”的MD5值',
    job_type       string   comment  '工作类型解析值',
    job_type_id    int      comment  '工作类型id:工作类型id:1-全职,2-实习,3-兼职,8-临时',
    rownum         bigint   comment  '行号',
    random_int     int      comment  '随机整数',
    etl_date       string   comment  'ETL日期');



drop table if exists <jd-db-name>.jd_job_type__b;

create external table if not exists <jd-db-name>.jd_job_type__b(
    jd_id          string   comment  'jd的id，“entity_id+content_sign”的MD5值',
    job_type       string   comment  '工作类型解析值',
    job_type_id    int      comment  '工作类型id:工作类型id:1-全职,2-实习,3-兼职,8-临时',
    rownum         bigint   comment  '行号',
    random_int     int      comment  '随机整数',
    etl_date       string   comment  'ETL日期')
location '<jd-measure-output>/jd_job_type';

insert into <jd-db-name>.jd_job_type
	select
		jd_id      
        ,job_type   
        ,job_type_id
        ,min(rownum)				
        ,min(random_int)
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
        select distinct
            b.jd_id      
            ,b.job_type   
            ,b.job_type_id
            ,b.rownum     
            ,b.random_int 
            ,b.etl_date   
        from
        	<jd-db-name>.jd_job_type__b b
        left join
        	<jd-db-name>.jd_job_type a
        on
        	    b.jd_id       = a.jd_id       
            and b.job_type    = a.job_type    
        where 
        	a.jd_id is null
			or b.job_type_id <> a.job_type_id and not (b.job_type_id is null and a.job_type_id  is null)

	) t
    group by
    	jd_id      
        ,job_type   
        ,job_type_id;

drop table if exists <jd-db-name>.jd_job_type__b;
