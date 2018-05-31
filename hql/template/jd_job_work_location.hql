set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_job_work_location(
    jd_id                         string   comment   'jd的id，“entity_id+content_sign”的MD5值',
    job_work_location             string   comment   '工作地点详细地址解析值',
    job_location_province_id      string   comment   '工作地点量化省份id',
    job_location_city_id          string   comment   '工作地点量化城市id',
    job_location_region_id        string   comment   '工作地点量化区域id',
    rownum                        bigint   comment   '行号',
    random_int                    int      comment   '随机整数',
    etl_date                      string   comment   'ETL日期');


drop table if exists <jd-db-name>.jd_job_work_location__b;
create external table if not exists <jd-db-name>.jd_job_work_location__b(
    jd_id                         string   comment   'jd的id，“entity_id+content_sign”的MD5值',
    job_work_location             string   comment   '工作地点详细地址解析值',
    job_location_province_id      string   comment   '工作地点量化省份id',
    job_location_city_id          string   comment   '工作地点量化城市id',
    job_location_region_id        string   comment   '工作地点量化区域id',
    rownum                        bigint   comment   '行号',
    random_int                    int      comment   '随机整数',
    etl_date                      string   comment   'ETL日期')
location '<jd-measure-output>/jd_job_work_location' ;


insert into table <jd-db-name>.jd_job_work_location
    select
    	jd_id                    
        ,job_work_location        
        ,job_location_province_id 
        ,job_location_city_id     
        ,job_location_region_id   
	    ,min(rownum)
        ,min(random_int)
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
	    select distinct
	    	b.jd_id                    
            ,b.job_work_location        
            ,b.job_location_province_id 
            ,b.job_location_city_id     
            ,b.job_location_region_id   
            ,b.rownum                   
            ,b.random_int               
            ,b.etl_date                 
	    from
	    	<jd-db-name>.jd_job_work_location__b b
	    left join
	    	<jd-db-name>.jd_job_work_location a
	    on
	    	    b.jd_id                      = a.jd_id                     
            and b.job_work_location          = a.job_work_location         
	    where
	    	a.jd_id is null
            or b.job_location_province_id   <> a.job_location_province_id and not ( b.job_location_province_id   is null and  a.job_location_province_id  is null) 
            or b.job_location_city_id       <> a.job_location_city_id     and not ( b.job_location_city_id       is null and  a.job_location_city_id      is null)
            or b.job_location_region_id     <> a.job_location_region_id   and not ( b.job_location_region_id     is null and  a.job_location_region_id    is null)
	) t
	group by
		jd_id                    
        ,job_work_location        
        ,job_location_province_id 
        ,job_location_city_id     
        ,job_location_region_id  ;

drop table if exists <jd-db-name>.jd_job_work_location__b;
