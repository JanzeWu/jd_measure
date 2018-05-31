set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_page(
    jd_id                string  comment  'jd的id，“jd_src_id+content_sign”的MD5值',
    jd_src_id            string  comment  'JD来源id，和原始jd的URL相关',
    content_sign         string  comment  '验签值，标识JD内容是否变化',
    page_content_path    string  comment  '页面内容路径',
    crawler_update_time  bigint  comment  '爬取日期，UnixTimestamp格式',
    crawler_update_date  string  comment  '爬取日期，日期格式',
    etl_date             string  comment  'ETL日期');


drop table if exists <jd-db-name>.jd_page__b;
create external table if not exists <jd-db-name>.jd_page__b(
    jd_id                string  comment  'jd的id，“jd_src_id+content_sign”的MD5值',
    jd_src_id            string  comment  'JD来源id，和原始jd的URL相关',
    content_sign         string  comment  '验签值，标识JD内容是否变化',
    page_content_path    string  comment  '页面内容路径',
    crawler_update_time  bigint  comment  '爬取日期，UnixTimestamp格式',
    crawler_update_date  string  comment  '爬取日期，日期格式',
    etl_date             string  comment  'ETL日期')
Location '<jd-measure-output>/jd_page';


insert into table <jd-db-name>.jd_page
    select
        jd_id              
        ,jd_src_id          
        ,content_sign       
        ,page_content_path  
        ,crawler_update_time
        ,crawler_update_date
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
    	select
    	    b.jd_id              
            ,b.jd_src_id          
            ,b.content_sign       
            ,b.page_content_path  
            ,b.crawler_update_time
            ,b.crawler_update_date
            ,b.etl_date           
    	from
    		<jd-db-name>.jd_page__b b
    	left join
    		<jd-db-name>.jd_page a
    	on
    		    b.jd_id               = a.jd_id                
            and b.content_sign        = a.content_sign         

    	where
    		a.jd_id is null
            or b.jd_src_id           <> a.jd_src_id            and not  (b.jd_src_id           is null and  a.jd_src_id           is null) 
            or b.page_content_path   <> a.page_content_path    and not  (b.page_content_path   is null and  a.page_content_path   is null) 
            or b.crawler_update_time <> a.crawler_update_time  and not  (b.crawler_update_time is null and  a.crawler_update_time is null)
            or b.crawler_update_date <> a.crawler_update_date  and not  (b.crawler_update_date is null and  a.crawler_update_date is null)
	) t
	group by
		jd_id              
        ,jd_src_id          
        ,content_sign       
        ,page_content_path  
        ,crawler_update_time
        ,crawler_update_date ;

drop table if exists <jd-db-name>.jd_page__b;
