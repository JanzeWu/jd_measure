-- Description: 记录各表最大行数
-- Date: 2018-010-09

set hive.execution.engine=mr;

drop table if exists <jd-db-name>.load_max_rownum;

create table <jd-db-name>.load_max_rownum(
	table_name	    string  comment '表名', 
	load_max_rownum	int     comment '该表rownum最大值',
	etl_date        string  comment 'ETL日期');


insert into <jd-db-name>.load_max_rownum
	select
		'jd_entry' ,
		max(rownum) ,
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd') 
	from 
		<jd-db-name>.jd_entry
	union all
	select
		'jd_inc',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from 
		<jd-db-name>.jd_inc
	union all
	select
		'jd_job',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from 
		<jd-db-name>.jd_job
	union all
	select
		'jd_others',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.jd_others
	union all
	select
		'job_zhineng',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.job_zhineng
	union all
	select
		'measure_inc_industry',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.measure_inc_industry
	union all
	select
		'jd_job_major',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.jd_job_major
	union all
	select
		'jd_job_type',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.jd_job_type
	union all
	select
		'jd_job_work_city',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.jd_job_work_city
	union all
	select
		'jd_job_work_location',
		max(rownum),
		from_unixtime(unix_timestamp(), 'yyyy-MM-dd')
	from
		<jd-db-name>.jd_job_work_location;

