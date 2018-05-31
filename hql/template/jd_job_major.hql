-- Description: 将量化jd_job_major 导入 jd_job_major表

set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_job_major(
    jd_id           string   comment    'jd的id，“entity_id+content_sign”的MD5值',
    job_major       string   comment    '工作要求的专业解析值',
    job_major_id    string   comment    '工作要求的专业id',
    job_diploma     string   comment    '工作学历要求解析值',
    job_diploma_id  int      comment    '工作学历要求量化值。',
    rownum          bigint   comment    '行号',
    random_int      int      comment    '随机整数',
    etl_date        string   comment    'ETL日期');


drop table if exists <jd-db-name>.jd_job_major__b;

create external table if not exists <jd-db-name>.jd_job_major__b(
    jd_id           string   comment    'jd的id，“entity_id+content_sign”的MD5值',
    job_major       string   comment    '工作要求的专业解析值',
    job_major_id    string   comment    '工作要求的专业id',
    job_diploma     string   comment    '工作学历要求解析值',
    job_diploma_id  int      comment    '工作学历要求量化值。',
    rownum          bigint   comment    '行号',
    random_int      int      comment    '随机整数',
    etl_date        string   comment    'ETL日期')
Location '<jd-measure-output>/jd_job_major';

		
insert into table <jd-db-name>.jd_job_major
	select 
		jd_id         
        ,job_major     
        ,job_major_id  
        ,job_diploma   
        ,job_diploma_id
        ,min(rownum)       
        ,min(random_int)    
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
        select distinct
            b.jd_id         
            ,b.job_major     
            ,b.job_major_id  
            ,b.job_diploma   
            ,b.job_diploma_id
            ,b.rownum        
            ,b.random_int    
            ,b.etl_date      
        from 
        	<jd-db-name>.jd_job_major__b b
        left join
        	<jd-db-name>.jd_job_major a
        on
               b.jd_id           = a.jd_id          
           and b.job_major       = a.job_major      
        where
        	a.jd_id is null
		    or b.job_major_id    <>  a.job_major_id   and not (b.job_major_id    is null and  a.job_major_id   is null) 
            or b.job_diploma     <>  a.job_diploma    and not (b.job_diploma     is null and  a.job_diploma    is null) 
            or b.job_diploma_id  <>  a.job_diploma_id and not (b.job_diploma_id  is null and  a.job_diploma_id is null) 
	
	) t
    group by
    	jd_id         
        , job_major     
        , job_major_id  
        , job_diploma   
        , job_diploma_id;

drop table if exists <jd-db-name>.jd_job_major__b;
