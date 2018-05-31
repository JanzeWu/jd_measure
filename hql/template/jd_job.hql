set hive.execution.engine=mr;

set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_job(
    jd_id               string          comment    'jd的id，“jd_src_id+content_sign”的MD5值',
    job_position        string          comment    '职位名称解析值',
    job_cate            string          comment    '工作职能/类别解析值',
    job_salary          string          comment    '薪酬解析值',
    job_salary_min      DECIMAL(24,3)   comment    '薪酬区间下限',
    job_salary_max      DECIMAL(24,3)   comment    '薪酬区间上限',
    job_work_age        string          comment    '工作经验要求解析值',
    job_work_age_min    int             comment    '工作经验要求区间下限，单位是天数，不限工作经验则用0表示',
    job_work_age_max    int             comment    '工作经验要求区间上限，单位是天数，不限工作经验则用0表示',
    job_diploma         string          comment    '工作学历要求解析值',
    job_diploma_id      int             comment    '工作学历要求量化值。',
    job_position_count  string          comment    '职位招聘人数解析值',
    job_position_min    int             comment    '职位招聘人数量化值下限',
    job_position_max    int             comment    '职位招聘人数量化值上限',
    job_welfare         array<string>   comment    '职位的福利解析值',
    job_age             string          comment    '年龄要求解析值',
    job_age_num_min     int             comment    '年龄要求下限量化值，单位为岁，0表示不限年龄',
    job_age_num_max     int             comment    '年龄要求上限量化值，单位为岁，0表示不限年龄',
    job_gender_raw      string          comment    '职位性别要求解析值',
    job_gender          string          comment    '职位性别要求，M表示男，F表示女，‘-’表示不限',
    job_endtime         string          comment    '截止日期解析值',
    job_endtime_date    string          comment    '截止日期，日期格式',
    skill_list          array<string>   comment    '技能要求解析值列表',
    cert_list           array<string>   comment    '证书要求解析值列表',
    job_language        array<string>   comment    '语言要求',
    require_oversea_exp string          comment    '是否要求海外工作经历，Y/N',
    is_urgent           string          comment    '是否急招，Y/N',
    cv_recv_raw         string          comment    '接受简历方式解析值',
    cv_recv_email       string          comment    '接受简历email地址',
    job_department      string          comment    '工作所在部门解析值',
    job_label           string          comment    '职位标签',
    job_position_desc   string          comment    '职位描述文本',
    work_demand         string          comment    '工作要求',
    work_duty           string          comment    '工作职责',
    rownum				bigint  		comment	   '行号',
    random_int	        int				comment	   '随机整数',
    etl_date            string          comment    'ETL日期');


drop table if exists <jd-db-name>.jd_job__b;
create external table if not exists <jd-db-name>.jd_job__b(
    jd_id               string          comment    'jd的id，“jd_src_id+content_sign”的MD5值',
    job_position        string          comment    '职位名称解析值',
    job_cate            string          comment    '工作职能/类别解析值',
    job_salary          string          comment    '薪酬解析值',
    job_salary_min      DECIMAL(24,3)   comment    '薪酬区间下限',
    job_salary_max      DECIMAL(24,3)   comment    '薪酬区间上限',
    job_work_age        string          comment    '工作经验要求解析值',
    job_work_age_min    int             comment    '工作经验要求区间下限，单位是天数，不限工作经验则用0表示',
    job_work_age_max    int             comment    '工作经验要求区间上限，单位是天数，不限工作经验则用0表示',
    job_diploma         string          comment    '工作学历要求解析值',
    job_diploma_id      int             comment    '工作学历要求量化值。',
    job_position_count  string          comment    '职位招聘人数解析值',
    job_position_min    int             comment    '职位招聘人数量化值下限',
    job_position_max    int             comment    '职位招聘人数量化值上限',
    job_welfare         array<string>   comment    '职位的福利解析值',
    job_age             string          comment    '年龄要求解析值',
    job_age_num_min     int             comment    '年龄要求下限量化值，单位为岁，0表示不限年龄',
    job_age_num_max     int             comment    '年龄要求上限量化值，单位为岁，0表示不限年龄',
    job_gender_raw      string          comment    '职位性别要求解析值',
    job_gender          string          comment    '职位性别要求，M表示男，F表示女，‘-’表示不限',
    job_endtime         string          comment    '截止日期解析值',
    job_endtime_date    string          comment    '截止日期，日期格式',
    skill_list          array<string>   comment    '技能要求解析值列表',
    cert_list           array<string>   comment    '证书要求解析值列表',
    job_language        array<string>   comment    '语言要求',
    require_oversea_exp string          comment    '是否要求海外工作经历，Y/N',
    is_urgent           string          comment    '是否急招，Y/N',
    cv_recv_raw         string          comment    '接受简历方式解析值',
    cv_recv_email       string          comment    '接受简历email地址',
    job_department      string          comment    '工作所在部门解析值',
    job_label           string          comment    '职位标签',
    job_position_desc   string          comment    '职位描述文本',
    work_demand         string          comment    '工作要求',
    work_duty           string          comment    '工作职责',
    rownum				bigint  		comment	   '行号',
    random_int	        int				comment	   '随机整数',
    etl_date            string          comment    'ETL日期')
Location '<jd-measure-output>/jd_job';
    
    

insert into table <jd-db-name>.jd_job
    select 
        jd_id              
        ,job_position       
        ,job_cate           
        ,job_salary         
        ,job_salary_min     
        ,job_salary_max     
        ,job_work_age       
        ,job_work_age_min   
        ,job_work_age_max   
        ,job_diploma        
        ,job_diploma_id     
        ,job_position_count 
        ,job_position_min   
        ,job_position_max   
        ,job_welfare        
        ,job_age            
        ,job_age_num_min    
        ,job_age_num_max    
        ,job_gender_raw     
        ,job_gender         
        ,job_endtime        
        ,job_endtime_date   
        ,skill_list         
        ,cert_list          
        ,job_language       
        ,require_oversea_exp
        ,is_urgent          
        ,cv_recv_raw        
        ,cv_recv_email      
        ,job_department     
        ,job_label          
        ,job_position_desc  
        ,work_demand        
        ,work_duty          
        ,min(rownum)			   
        ,min(random_int)	       
        ,from_unixtime(min(unix_timestamp(etl_date, 'yyyy-MM-dd')), 'yyyy-MM-dd')
    from (
        select distinct
        	b.jd_id              
            ,b.job_position       
            ,b.job_cate           
            ,b.job_salary         
            ,b.job_salary_min     
            ,b.job_salary_max     
            ,b.job_work_age       
            ,b.job_work_age_min   
            ,b.job_work_age_max   
            ,b.job_diploma        
            ,b.job_diploma_id     
            ,b.job_position_count 
            ,b.job_position_min   
            ,b.job_position_max   
            ,b.job_welfare        
            ,b.job_age            
            ,b.job_age_num_min    
            ,b.job_age_num_max    
            ,b.job_gender_raw     
            ,b.job_gender         
            ,b.job_endtime        
            ,b.job_endtime_date   
            ,b.skill_list         
            ,b.cert_list          
            ,b.job_language       
            ,b.require_oversea_exp
            ,b.is_urgent          
            ,b.cv_recv_raw        
            ,b.cv_recv_email      
            ,b.job_department     
            ,b.job_label          
            ,b.job_position_desc  
            ,b.work_demand        
            ,b.work_duty          
            ,b.rownum
            ,b.random_int
            ,b.etl_date
        from
        	<jd-db-name>.jd_job__b b 
        left join
            <jd-db-name>.jd_job a
        on 
                b.jd_id               = a.jd_id                
            and b.job_position        = a.job_position         
        where
            a.jd_id is null
            or b.job_cate           <>  a.job_cate            and not ( b.job_cate            is null and  a.job_cate            is null) 
            or b.job_salary         <>  a.job_salary          and not ( b.job_salary          is null and  a.job_salary          is null) 
            or b.job_salary_min     <>  a.job_salary_min      and not ( b.job_salary_min      is null and  a.job_salary_min      is null) 
            or b.job_salary_max     <>  a.job_salary_max      and not ( b.job_salary_max      is null and  a.job_salary_max      is null) 
            or b.job_work_age       <>  a.job_work_age        and not ( b.job_work_age        is null and  a.job_work_age        is null) 
            or b.job_work_age_min   <>  a.job_work_age_min    and not ( b.job_work_age_min    is null and  a.job_work_age_min    is null) 
            or b.job_work_age_max   <>  a.job_work_age_max    and not ( b.job_work_age_max    is null and  a.job_work_age_max    is null) 
            or b.job_diploma        <>  a.job_diploma         and not ( b.job_diploma         is null and  a.job_diploma         is null) 
            or b.job_diploma_id     <>  a.job_diploma_id      and not ( b.job_diploma_id      is null and  a.job_diploma_id      is null) 
            or b.job_position_count <>  a.job_position_count  and not ( b.job_position_count  is null and  a.job_position_count  is null) 
            or b.job_position_min   <>  a.job_position_min    and not ( b.job_position_min    is null and  a.job_position_min    is null) 
            or b.job_position_max   <>  a.job_position_max    and not ( b.job_position_max    is null and  a.job_position_max    is null) 
            or concat_ws('|', b.job_welfare)  <>  concat_ws('|', a.job_welfare)         and not ( b.job_welfare         is null and  a.job_welfare         is null) 
            or b.job_age            <>  a.job_age             and not ( b.job_age             is null and  a.job_age             is null) 
            or b.job_age_num_min    <>  a.job_age_num_min     and not ( b.job_age_num_min     is null and  a.job_age_num_min     is null) 
            or b.job_age_num_max    <>  a.job_age_num_max     and not ( b.job_age_num_max     is null and  a.job_age_num_max     is null) 
            or b.job_gender_raw     <>  a.job_gender_raw      and not ( b.job_gender_raw      is null and  a.job_gender_raw      is null) 
            or b.job_gender         <>  a.job_gender          and not ( b.job_gender          is null and  a.job_gender          is null) 
            or b.job_endtime        <>  a.job_endtime         and not ( b.job_endtime         is null and  a.job_endtime         is null) 
            or b.job_endtime_date   <>  a.job_endtime_date    and not ( b.job_endtime_date    is null and  a.job_endtime_date    is null) 
            or concat_ws('|', b.skill_list)   <>  concat_ws('|', a.skill_list)          and not ( b.skill_list          is null and  a.skill_list          is null) 
            or concat_ws('|', b.cert_list)    <>  concat_ws('|', a.cert_list)           and not ( b.cert_list           is null and  a.cert_list           is null) 
            or concat_ws('|', b.job_language) <>  concat_ws('|', a.job_language)        and not ( b.job_language        is null and  a.job_language        is null) 
            or b.require_oversea_exp <>  a.require_oversea_exp and not ( b.require_oversea_exp is null and  a.require_oversea_exp is null) 
            or b.is_urgent          <>  a.is_urgent           and not ( b.is_urgent           is null and  a.is_urgent           is null) 
            or b.cv_recv_raw        <>  a.cv_recv_raw         and not ( b.cv_recv_raw         is null and  a.cv_recv_raw         is null) 
            or b.cv_recv_email      <>  a.cv_recv_email       and not ( b.cv_recv_email       is null and  a.cv_recv_email       is null) 
            or b.job_department     <>  a.job_department      and not ( b.job_department      is null and  a.job_department      is null) 
            or b.job_label          <>  a.job_label           and not ( b.job_label           is null and  a.job_label           is null) 
            or b.job_position_desc  <>  a.job_position_desc   and not ( b.job_position_desc   is null and  a.job_position_desc   is null) 
            or b.work_demand        <>  a.work_demand         and not ( b.work_demand         is null and  a.work_demand         is null) 
            or b.work_duty          <>  a.work_duty           and not ( b.work_duty           is null and  a.work_duty           is null) 

	) t
    group by
        jd_id              
        ,job_position       
        ,job_cate           
        ,job_salary         
        ,job_salary_min     
        ,job_salary_max     
        ,job_work_age       
        ,job_work_age_min   
        ,job_work_age_max   
        ,job_diploma        
        ,job_diploma_id     
        ,job_position_count 
        ,job_position_min   
        ,job_position_max   
        ,job_welfare        
        ,job_age            
        ,job_age_num_min    
        ,job_age_num_max    
        ,job_gender_raw     
        ,job_gender         
        ,job_endtime        
        ,job_endtime_date   
        ,skill_list         
        ,cert_list          
        ,job_language       
        ,require_oversea_exp
        ,is_urgent          
        ,cv_recv_raw        
        ,cv_recv_email      
        ,job_department     
        ,job_label          
        ,job_position_desc  
        ,work_demand        
        ,work_duty ;

drop table if exists <jd-db-name>.jd_job__b;
