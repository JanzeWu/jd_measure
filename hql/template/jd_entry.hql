set hive.execution.engine=mr;
set hive.auto.convert.join=false;


create table if not exists <jd-db-name>.jd_entry(
	jd_id               	string        comment  	'jd的id，“jd_src_id+content_sign”的MD5值',
	entity_id				string        comment   'JD实体id，逻辑上唯一标识渠道内的一个JD。',
	jd_src_id           	string        comment  	'JD来源id，和原始jd的URL相关',
	content_sign        	string        comment  	'验签值，标识JD内容是否变化',
	jd_from_id				int			  comment   'JD来源渠道id',	
	jd_content_pub_timestamp	bigint    comment   'JD版本表动的更新/发布日期，Unix Timestamp格式',
	jd_content_pub_date 	string        comment  	'JD版本表动的更新/发布日期，日期格式',
	jd_src_url          	string        comment  	'JD来源URL',
	inc_seg_id          	string        comment  	'裸公司id，为裸公司名+ipin.com的MD5值',
	status              	int           comment  	'状态：0-生效中,1-下架/过期,2-历史版本',
	src                 	string        comment  	'系统来源',
	rownum					bigint		  comment   '行号',
	random_int				int			  comment   '随机整数',
	etl_date            	string        comment  	'ETL日期');

drop table if exists <jd-db-name>.jd_entry__b;
create external table if not exists <jd-db-name>.jd_entry__b(
	jd_id               	string        comment  	'jd的id，“jd_src_id+content_sign”的MD5值',
	entity_id				string        comment   'JD实体id，逻辑上唯一标识渠道内的一个JD。',
	jd_src_id           	string        comment  	'JD来源id，和原始jd的URL相关',
	content_sign        	string        comment  	'验签值，标识JD内容是否变化',
	jd_from_id				int			  comment   'JD来源渠道id',	
	jd_content_pub_timestamp	bigint    comment   'JD版本表动的更新/发布日期，Unix Timestamp格式',
	jd_content_pub_date 	string        comment  	'JD版本表动的更新/发布日期，日期格式',
	jd_src_url          	string        comment  	'JD来源URL',
	inc_seg_id          	string        comment  	'裸公司id，为裸公司名+ipin.com的MD5值',
	status              	int           comment  	'状态：0-生效中,1-下架/过期,2-历史版本',
	src                 	string        comment  	'系统来源',
	rownum					bigint		  comment   '行号',
	random_int				int			  comment   '随机整数',
	etl_date            	string        comment  	'ETL日期')
location '<jd-measure-output>/jd_entry';


insert into table <jd-db-name>.jd_entry
	select
		jd_id               	
        ,entity_id				
        ,jd_src_id           	
        ,content_sign        	
        ,jd_from_id				
        ,jd_content_pub_timestamp
        ,jd_content_pub_date 	
        ,jd_src_url          	
        ,inc_seg_id          	
        ,status              	
        ,min(src)
        ,min(rownum)
        ,min(random_int)
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
	    select distinct
            b.jd_id               	
            ,b.entity_id				
            ,b.jd_src_id           	
            ,b.content_sign        	
            ,b.jd_from_id				
            ,b.jd_content_pub_timestamp
            ,b.jd_content_pub_date 	
            ,b.jd_src_url          	
            ,b.inc_seg_id          	
            ,b.status              	
            ,b.src                 	
            ,b.rownum					
            ,b.random_int				
            ,b.etl_date            	
	    from
	    	<jd-db-name>.jd_entry__b b
	    left join
	    	<jd-db-name>.jd_entry a 
	    on
           	b.jd_id                        = a.jd_id                    
            and b.entity_id                = a.entity_id                
            and b.jd_src_id                = a.jd_src_id                
            and b.content_sign             = a.content_sign             
            and b.inc_seg_id               = a.inc_seg_id               
	    where
	    	a.jd_id is null
            or b.jd_from_id               <>  a.jd_from_id                and not (b.jd_from_id               is null and a.jd_from_id               is null)
            or b.jd_content_pub_timestamp <>  a.jd_content_pub_timestamp  and not (b.jd_content_pub_timestamp is null and a.jd_content_pub_timestamp is null)
            or b.jd_content_pub_date      <>  a.jd_content_pub_date       and not (b.jd_content_pub_date      is null and a.jd_content_pub_date      is null)
            or b.jd_src_url               <>  a.jd_src_url                and not (b.jd_src_url               is null and a.jd_src_url               is null)
            or b.status      	          <>  a.status      	          and not (b.status      	          is null and a.status      	         is null)
	) t
	group by
		jd_id               	
        ,entity_id				
        ,jd_src_id           	
        ,content_sign        	
        ,jd_from_id				
        ,jd_content_pub_timestamp
        ,jd_content_pub_date 	
        ,jd_src_url          	
        ,inc_seg_id          	
        ,status;

drop table if exists <jd-db-name>.jd_entry__b;
