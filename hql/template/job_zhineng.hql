set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.job_zhineng(
    jd_id                  string   comment   'jd的id，“entity_id+content_sign”的MD5值',
    job_position           string   comment   '职位名称解析值',
    zhineng_id             int      comment   '量化职能ID',
    zhineng_name           string   comment   '量化职能名称',
    zhineng_version        string   comment   '量化职能算法版本',
    src                    string   comment   '系统来源',
    rownum                 bigint   comment   '行号',
    random_int             int      comment   '随机整数',
    start_date             string   comment   '量化日期',
    end_date               string   comment   '失效日期');

create table if not exists <jd-db-name>.job_zhineng__b(
    jd_id                  string   comment   'jd的id，“entity_id+content_sign”的MD5值',
    job_position           string   comment   '职位名称解析值',
    zhineng_id             int      comment   '量化职能ID',
    zhineng_name           string   comment   '量化职能名称',
    zhineng_version        string   comment   '量化职能算法版本',
    src                    string   comment   '系统来源',
    rownum                 bigint   comment   '行号',
    random_int             int      comment   '随机整数',
    start_date             string   comment   '量化日期',
    end_date               string   comment   '失效日期');


drop table if exists <jd-db-name>.job_zhineng__c;

create EXTERNAL table if not exists <jd-db-name>.job_zhineng__c(
    jd_id                  string   comment   'jd的id，“entity_id+content_sign”的MD5值',
    job_position           string   comment   '职位名称解析值',
    zhineng_id             int      comment   '量化职能ID',
    zhineng_name           string   comment   '量化职能名称',
    zhineng_version        string   comment   '量化职能算法版本',
    src                    string   comment   '系统来源',
    rownum                 bigint   comment   '行号',
    random_int             int      comment   '随机整数',
    start_date             string   comment   '量化日期',
    end_date               string   comment   '失效日期')
location '<jd-measure-output>/job_zhineng'; 

-- 相同JD 相同职能 只保留最新量化的职能
insert OVERWRITE TABLE <jd-db-name>.job_zhineng__b
	select 
	    jd_id          	
        ,job_position   
        ,zhineng_id     
        ,zhineng_name   
        ,zhineng_version
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
		<jd-db-name>.job_zhineng__c
	group by
		jd_id          	
        ,job_position   
        ,zhineng_id     
        ,zhineng_name   
        ,zhineng_version
	union all
	-- 不同JD, copy 到B表
	select
		a.jd_id          
        ,a.job_position   
        ,a.zhineng_id     
        ,a.zhineng_name   
        ,a.zhineng_version
        ,a.src            
        ,a.rownum         
        ,a.random_int     
        ,a.start_date     
        ,a.end_date       
	from
		<jd-db-name>.job_zhineng a
	left join (
		select
			jd_id,
			job_position,
			zhineng_id
		from 	
			<jd-db-name>.job_zhineng__c
		group by 
			jd_id, job_position, zhineng_id
	) c
	on
		    a.jd_id = c.jd_id                
	where
		c.jd_id is null
	union all
	-- 相同JD不同职能, 设置end_date 非空
	select
		a.jd_id          
        ,a.job_position   
        ,a.zhineng_id     
        ,a.zhineng_name   
        ,a.zhineng_version
        ,a.src            
        ,a.rownum         
        ,a.random_int     
        ,a.start_date     
        ,case 
			when datediff(c.start_date, a.start_date) > 0 then date_sub(c.start_date, 1)
			else c.start_date
		end as end_date
	from
		<jd-db-name>.job_zhineng a
	left join (
		select
			jd_id,
			job_position,
			zhineng_id,
			from_unixtime(min(unix_timestamp(start_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
				as start_date
		from 
			<jd-db-name>.job_zhineng__c
		group by 
			jd_id, job_position, zhineng_id
	) c	
	on
		a.jd_id = c.jd_id
	where
		c.jd_id is not null
		and a.zhineng_id is not null 
		and c.zhineng_id is not null
		and a.zhineng_id <> c.zhineng_id;




drop table if exists <jd-db-name>.job_zhineng__c;
