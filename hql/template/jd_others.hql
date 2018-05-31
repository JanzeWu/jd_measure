set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_others(
    jd_id              string           comment   'jd的id，“jd_src_id+content_sign”的MD5值',
    key_words          array<string>    comment   '关键字列表',
    pos_type           string           comment   '企业职位/猎头职位',
    is_fulltime        string           comment   '是否全日制统招',
    salary_combine     string           comment   '薪资构成',
    holiday_welfare    string           comment   '年假福利',
    traffic_welfare    string           comment   '通讯交通福利',
    job_sub_size       string           comment   '下属人数',
    social_welfare     string           comment   '社保福利',
    jd_remedy          string           comment   '',
    living_welfare     string           comment   '居住福利',
    job_report         string           comment   '汇报对象',
    job_report_detail  string           comment   '',
    rownum             bigint           comment   '行号',
    random_int         int              comment   '随机整数',
    etl_date           string           comment   'ETL日期');


drop table if exists <jd-db-name>.jd_others__b;
create external table  if not exists <jd-db-name>.jd_others__b(
    jd_id              string           comment   'jd的id，“jd_src_id+content_sign”的MD5值',
    key_words          array<string>    comment   '关键字列表',
    pos_type           string           comment   '企业职位/猎头职位',
    is_fulltime        string           comment   '是否全日制统招',
    salary_combine     string           comment   '薪资构成',
    holiday_welfare    string           comment   '年假福利',
    traffic_welfare    string           comment   '通讯交通福利',
    job_sub_size       string           comment   '下属人数',
    social_welfare     string           comment   '社保福利',
    jd_remedy          string           comment   '',
    living_welfare     string           comment   '居住福利',
    job_report         string           comment   '汇报对象',
    job_report_detail  string           comment   '',
    rownum             bigint           comment   '行号',
    random_int         int              comment   '随机整数',
    etl_date           string           comment   'ETL日期')
location '<jd-measure-output>/jd_others';


insert into table <jd-db-name>.jd_others
	select
		jd_id            
        ,key_words        
        ,pos_type         
        ,is_fulltime      
        ,salary_combine   
        ,holiday_welfare  
        ,traffic_welfare  
        ,job_sub_size     
        ,social_welfare   
        ,jd_remedy        
        ,living_welfare   
        ,job_report       
        ,job_report_detail
        ,min(rownum)
        ,min(random_int)
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
	from (
	    select  distinct
	    	b.jd_id            
            ,b.key_words        
            ,b.pos_type         
            ,b.is_fulltime      
            ,b.salary_combine   
            ,b.holiday_welfare  
            ,b.traffic_welfare  
            ,b.job_sub_size     
            ,b.social_welfare   
            ,b.jd_remedy        
            ,b.living_welfare   
            ,b.job_report       
            ,b.job_report_detail
            ,b.rownum           
            ,b.random_int       
            ,b.etl_date         
	    from
	    	<jd-db-name>.jd_others__b b
	    left join
	    	<jd-db-name>.jd_others a
	    on
	    	b.jd_id             = a.jd_id             
	    where
	    	a.jd_id is null
            or concat_ws('|', b.key_words)   <>  concat_ws('|', a.key_words)         and not ( b.key_words         is null and   a.key_words         is null) 
            or b.pos_type          <>  a.pos_type          and not ( b.pos_type          is null and   a.pos_type          is null) 
            or b.is_fulltime       <>  a.is_fulltime       and not ( b.is_fulltime       is null and   a.is_fulltime       is null) 
            or b.salary_combine    <>  a.salary_combine    and not ( b.salary_combine    is null and   a.salary_combine    is null) 
            or b.holiday_welfare   <>  a.holiday_welfare   and not ( b.holiday_welfare   is null and   a.holiday_welfare   is null) 
            or b.traffic_welfare   <>  a.traffic_welfare   and not ( b.traffic_welfare   is null and   a.traffic_welfare   is null) 
            or b.job_sub_size      <>  a.job_sub_size      and not ( b.job_sub_size      is null and   a.job_sub_size      is null) 
            or b.social_welfare    <>  a.social_welfare    and not ( b.social_welfare    is null and   a.social_welfare    is null) 
            or b.jd_remedy         <>  a.jd_remedy         and not ( b.jd_remedy         is null and   a.jd_remedy         is null) 
            or b.living_welfare    <>  a.living_welfare    and not ( b.living_welfare    is null and   a.living_welfare    is null) 
            or b.job_report        <>  a.job_report        and not ( b.job_report        is null and   a.job_report        is null) 
            or b.job_report_detail <>  a.job_report_detail and not ( b.job_report_detail is null and   a.job_report_detail is null) 

	) t
	group by
		jd_id            
        ,key_words        
        ,pos_type         
        ,is_fulltime      
        ,salary_combine   
        ,holiday_welfare  
        ,traffic_welfare  
        ,job_sub_size     
        ,social_welfare   
        ,jd_remedy        
        ,living_welfare   
        ,job_report       
        ,job_report_detail;

drop table if exists <jd-db-name>.jd_others__b;
