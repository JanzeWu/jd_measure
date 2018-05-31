set hive.execution.engine=mr;

set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.measure_inc_industry(
    jd_id                string   comment  'jd的id，“entity_id+content_sign”的MD5值',
    inc_seg_industry     string   comment  '公司的行业解析值',
    inc_seg_industry_id  string   comment  '公司的行业量化id',
    src                  string   comment  '系统来源',
    rownum               bigint   comment  '行号',
    random_int           int      comment  '随机整数',
    start_date           string   comment  '量化日期',
    end_date             string   comment  '失效日期');

create table if not exists <jd-db-name>.measure_inc_industry__b(
    jd_id                string   comment  'jd的id，“entity_id+content_sign”的MD5值',
    inc_seg_industry     string   comment  '公司的行业解析值',
    inc_seg_industry_id  string   comment  '公司的行业量化id',
    src                  string   comment  '系统来源',
    rownum               bigint   comment  '行号',
    random_int           int      comment  '随机整数',
    start_date           string   comment  '量化日期',
    end_date             string   comment  '失效日期');


drop table if exists <jd-db-name>.measure_inc_industry__c;

create external table if not exists <jd-db-name>.measure_inc_industry__c(
    jd_id                string   comment  'jd的id，“entity_id+content_sign”的MD5值',
    inc_seg_industry     string   comment  '公司的行业解析值',
    inc_seg_industry_id  string   comment  '公司的行业量化id',
    src                  string   comment  '系统来源',
    rownum               bigint   comment  '行号',
    random_int           int      comment  '随机整数',
    start_date           string   comment  '量化日期',
    end_date             string   comment  '失效日期')
location '<jd-measure-output>/measure_inc_industry' ;


-- 相同JD，相同行业，只保留新量化的行业
insert overwrite table <jd-db-name>.measure_inc_industry__b
	select
	    jd_id              	
        ,inc_seg_industry   
        ,inc_seg_industry_id
        ,min(src)
			as src
        ,min(rownum)
			as rownum
        ,min(random_int)
			as random_int
        ,min(start_date)
			as start_date
        ,min(end_date)
			as end_date
	from
		<jd-db-name>.measure_inc_industry__c
	group by
	    jd_id              	
	    ,inc_seg_industry   
	    ,inc_seg_industry_id
	union all
	-- 不同JD
	select
		a.jd_id              
        ,a.inc_seg_industry   
        ,a.inc_seg_industry_id
        ,a.src                
        ,a.rownum             
        ,a.random_int         
        ,a.start_date         
        ,a.end_date           
	from
		<jd-db-name>.measure_inc_industry a	
	left join (
		select
			jd_id,
			inc_seg_industry
		from 
			<jd-db-name>.measure_inc_industry__c
		group by 
			jd_id, inc_seg_industry
	) c
	on
		a.jd_id = c.jd_id                           
	where 
		c.jd_id is null
	union all
	-- 相同JD，不同行业
	select
		a.jd_id              
        ,a.inc_seg_industry   
        ,a.inc_seg_industry_id
        ,a.src                
        ,a.rownum             
        ,a.random_int         
        ,a.start_date         
        ,case 
        	when datediff(c.start_date, a.start_date) > 0 then date_sub(c.start_date, 1)
        	else c.start_date
        end as end_date
	from
		<jd-db-name>.measure_inc_industry a	
	left join (
		select 
			jd_id,
			inc_seg_industry,
			from_unixtime(min(unix_timestamp(start_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
				as start_date
		from 
			<jd-db-name>.measure_inc_industry__c
		group by 
			jd_id, inc_seg_industry
	) c
	on
		a.jd_id = c.jd_id                       
	where 
		c.jd_id is not null
		and a.inc_seg_industry is not null
		and c.inc_seg_industry is not null
		and a.inc_seg_industry <> c.inc_seg_industry ;
	
drop table if exists <jd-db-name>.measure_inc_industry__c;
