set hive.execution.engine=mr;

set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_inc(
    inc_seg_id              string    comment    '裸公司id，为裸公司名+ipin.com的MD5值',
    inc_seg_name            string    comment    '裸公司名称，为页面解析值',
    inc_seg_alias_name      string    comment    '公司别名/发布名称/简称',
    inc_seg_city_id         string    comment    '公司所在的城市id，为12个字符的国家行政区域编码',
    inc_seg_city            string    comment    '公司所在城市',
    inc_seg_location        string    comment    '公司的地址解析值',
    inc_seg_employee        string    comment    '公司人数规模解析值',
    inc_seg_employee_min    int       comment    '公司人数规模下限',
    inc_seg_employee_max    int       comment    '公司人数规模上限',
    inc_seg_type_id         int       comment    '公司类型量化值',
    inc_seg_type            string    comment    '公司类型解析值',
    inc_seg_zipcode_raw     string    comment    '公司邮政编码解析值',
    inc_seg_zipcode         string    comment    '公司邮政编码量化值',
    inc_seg_contact_name    string    comment    '公司联系人解析值',
    inc_seg_contact_info    string    comment    '公司联系方式解析值',
    inc_seg_vest_inst       string    comment    '投资公司的机构名称解析值',
    inc_seg_stage           string    comment    '公司发展阶段解析值，如B轮融资等',
    inc_seg_url             string    comment    '公司官网网址',
    inc_seg_desc            string    comment    '公司介绍文字解析值',
    rownum                  bigint    comment    '行号',
    random_int              int       comment    '随机整数',
    etl_date                string    comment    'ETL日期');


drop table if exists <jd-db-name>.jd_inc__b;
create external table if not exists <jd-db-name>.jd_inc__b(
    inc_seg_id              string    comment    '裸公司id，为裸公司名+ipin.com的MD5值',
    inc_seg_name            string    comment    '裸公司名称，为页面解析值',
    inc_seg_alias_name      string    comment    '公司别名/发布名称/简称',
    inc_seg_city_id         string    comment    '公司所在的城市id，为12个字符的国家行政区域编码',
    inc_seg_city            string    comment    '公司所在城市',
    inc_seg_location        string    comment    '公司的地址解析值',
    inc_seg_employee        string    comment    '公司人数规模解析值',
    inc_seg_employee_min    int       comment    '公司人数规模下限',
    inc_seg_employee_max    int       comment    '公司人数规模上限',
    inc_seg_type_id         int       comment    '公司类型量化值',
    inc_seg_type            string    comment    '公司类型解析值',
    inc_seg_zipcode_raw     string    comment    '公司邮政编码解析值',
    inc_seg_zipcode         string    comment    '公司邮政编码量化值',
    inc_seg_contact_name    string    comment    '公司联系人解析值',
    inc_seg_contact_info    string    comment    '公司联系方式解析值',
    inc_seg_vest_inst       string    comment    '投资公司的机构名称解析值',
    inc_seg_stage           string    comment    '公司发展阶段解析值，如B轮融资等',
    inc_seg_url             string    comment    '公司官网网址',
    inc_seg_desc            string    comment    '公司介绍文字解析值',
    rownum                  bigint    comment    '行号',
    random_int              int       comment    '随机整数',
    etl_date                string    comment    'ETL日期')
location '<jd-measure-output>/jd_inc';

	
insert into table <jd-db-name>.jd_inc
	select 
        inc_seg_id          
        ,inc_seg_name        
        ,inc_seg_alias_name  
        ,inc_seg_city_id     
        ,inc_seg_city        
        ,inc_seg_location    
        ,inc_seg_employee    
        ,inc_seg_employee_min
        ,inc_seg_employee_max
        ,inc_seg_type_id     
        ,inc_seg_type        
        ,inc_seg_zipcode_raw 
        ,inc_seg_zipcode     
        ,inc_seg_contact_name
        ,inc_seg_contact_info
        ,inc_seg_vest_inst   
        ,inc_seg_stage       
        ,inc_seg_url         
        ,inc_seg_desc        
        ,min(rownum)
        ,min(random_int)        
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
        select distinct
            b.inc_seg_id              
            ,b.inc_seg_name            
            ,b.inc_seg_alias_name      
            ,b.inc_seg_city_id         
            ,b.inc_seg_city            
            ,b.inc_seg_location        
            ,b.inc_seg_employee        
            ,b.inc_seg_employee_min    
            ,b.inc_seg_employee_max    
            ,b.inc_seg_type_id         
            ,b.inc_seg_type            
            ,b.inc_seg_zipcode_raw     
            ,b.inc_seg_zipcode         
            ,b.inc_seg_contact_name    
            ,b.inc_seg_contact_info    
            ,b.inc_seg_vest_inst       
            ,b.inc_seg_stage           
            ,b.inc_seg_url             
            ,b.inc_seg_desc            
            ,b.rownum                  
            ,b.random_int              
            ,b.etl_date                
        from
        	<jd-db-name>.jd_inc__b b
        left join
        	<jd-db-name>.jd_inc a
        on
        	    b.inc_seg_id             =  a.inc_seg_id             
            and b.inc_seg_name           =  a.inc_seg_name           
        where 
        	a.inc_seg_id is null
            or b.inc_seg_alias_name     <>  a.inc_seg_alias_name     and not (b.inc_seg_alias_name    is null and a.inc_seg_alias_name    is null)
            or b.inc_seg_city_id        <>  a.inc_seg_city_id        and not (b.inc_seg_city_id       is null and a.inc_seg_city_id       is null)
            or b.inc_seg_city           <>  a.inc_seg_city           and not (b.inc_seg_city          is null and a.inc_seg_city          is null)
            or b.inc_seg_location       <>  a.inc_seg_location       and not (b.inc_seg_location      is null and a.inc_seg_location      is null)
            or b.inc_seg_employee       <>  a.inc_seg_employee       and not (b.inc_seg_employee      is null and a.inc_seg_employee      is null)
            or b.inc_seg_employee_min   <>  a.inc_seg_employee_min   and not (b.inc_seg_employee_min  is null and a.inc_seg_employee_min  is null)
            or b.inc_seg_employee_max   <>  a.inc_seg_employee_max   and not (b.inc_seg_employee_max  is null and a.inc_seg_employee_max  is null)
            or b.inc_seg_type_id        <>  a.inc_seg_type_id        and not (b.inc_seg_type_id       is null and a.inc_seg_type_id       is null)
            or b.inc_seg_type           <>  a.inc_seg_type           and not (b.inc_seg_type          is null and a.inc_seg_type          is null)
            or b.inc_seg_zipcode_raw    <>  a.inc_seg_zipcode_raw    and not (b.inc_seg_zipcode_raw   is null and a.inc_seg_zipcode_raw   is null)
            or b.inc_seg_zipcode        <>  a.inc_seg_zipcode        and not (b.inc_seg_zipcode       is null and a.inc_seg_zipcode       is null)
            or b.inc_seg_contact_name   <>  a.inc_seg_contact_name   and not (b.inc_seg_contact_name  is null and a.inc_seg_contact_name  is null)
            or b.inc_seg_contact_info   <>  a.inc_seg_contact_info   and not (b.inc_seg_contact_info  is null and a.inc_seg_contact_info  is null)
            or b.inc_seg_vest_inst      <>  a.inc_seg_vest_inst      and not (b.inc_seg_vest_inst     is null and a.inc_seg_vest_inst     is null)
            or b.inc_seg_stage          <>  a.inc_seg_stage          and not (b.inc_seg_stage         is null and a.inc_seg_stage         is null)
            or b.inc_seg_url            <>  a.inc_seg_url            and not (b.inc_seg_url           is null and a.inc_seg_url           is null)
            or b.inc_seg_desc           <>  a.inc_seg_desc           and not (b.inc_seg_desc          is null and a.inc_seg_desc          is null)
	) t
	group by
		inc_seg_id          
	    ,inc_seg_name        
        ,inc_seg_alias_name  
        ,inc_seg_city_id     
        ,inc_seg_city        
        ,inc_seg_location    
        ,inc_seg_employee    
        ,inc_seg_employee_min
        ,inc_seg_employee_max
        ,inc_seg_type_id     
        ,inc_seg_type        
        ,inc_seg_zipcode_raw 
        ,inc_seg_zipcode     
        ,inc_seg_contact_name
        ,inc_seg_contact_info
        ,inc_seg_vest_inst   
        ,inc_seg_stage       
        ,inc_seg_url         
        ,inc_seg_desc;

drop table if exists <jd-db-name>.jd_inc__b;
