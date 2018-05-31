set hive.execution.engine=mr;
set hive.auto.convert.join=false;

drop table if exists <jd-db-name>.jd_timeline__c;
create external table if not exists <jd-db-name>.jd_timeline__c(
    jd_src_id           	string   comment  'JD来源id，和原始jd的URL相关',
    jd_update_timestamp 	bigint   comment  'JD更新/发布时间，Unix Timestamp格式',
    jd_update_date      	string   comment  'JD的更新/发布日期，日期格式',
    is_first            	string   comment  '是否第一次发布:Y/N',
    is_last             	string   comment  '是否最后一次发布:Y/N',
    etl_date            	string   comment  'ETL日期')
Location'<jd-measure-output>/jd_timeline';

create table if not exists <jd-db-name>.jd_timeline(
	jd_src_id           	string   comment  'JD来源id，和原始jd的URL相关',
	jd_update_timestamp 	bigint   comment  'JD更新/发布时间，Unix Timestamp格式',
	jd_update_date      	string   comment  'JD的更新/发布日期，日期格式',
	is_first            	string   comment  '是否第一次发布:Y/N',
	is_last             	string   comment  '是否最后一次发布:Y/N',
	etl_date            	string   comment  'ETL日期');

create table if not exists <jd-db-name>.jd_timeline__b(
    jd_src_id           	string   comment  'JD来源id，和原始jd的URL相关',
    jd_update_timestamp 	bigint   comment  'JD更新/发布时间，Unix Timestamp格式',
    jd_update_date      	string   comment  'JD的更新/发布日期，日期格式',
    is_first            	string   comment  '是否第一次发布:Y/N',
    is_last             	string   comment  '是否最后一次发布:Y/N',
    etl_date            	string   comment  'ETL日期');

insert into table <jd-db-name>.jd_timeline__c
	select
	    jd_src_id               
        , jd_update_timestamp     
        , jd_update_date          
        , is_first                
        , is_last                 
        , etl_date                
	from
		<jd-db-name>.jd_timeline;

drop table if exists <jd-db-name>.jd_timeline__c;
