package com.ipin.jd.consts;

public class MeasureUrl {

	public static final String JD_MEASURE_HOST = "http://measure.ipin.com/";
	public static final String INC_ID 		= JD_MEASURE_HOST + "measure/inc";
	public static final String INC_REGION 	= JD_MEASURE_HOST + "measure/inc_region";
	public static final String INC_INDUSTRY = JD_MEASURE_HOST + "measure/industry";
	public static final String INC_TYPE 	= JD_MEASURE_HOST + "measure/inc_type";//公司类型
	public static final String INC_SCALE 	= JD_MEASURE_HOST + "measure/com_size";//返回值中，最大值或者最小值没有时，相应返回值为null
	public static final String JOB_SALARY 	= JD_MEASURE_HOST + "measure/salary";//返回值中，最大值或者最小值没有时，相应返回值为null
	public static final String JOB_LOCID 	= JD_MEASURE_HOST + "measure/region";
	public static final String JOB_DIPLOMA	= JD_MEASURE_HOST + "measure/diploma";
	public static final String JOB_WORKAGE 	= JD_MEASURE_HOST + "measure/work_age";//按区间返回：[-1,0] 代表 在校学生；[-1,0] 代表应届生
	public static final String JOB_ZHIJI 	= JD_MEASURE_HOST + "measure/zhiji";
	public static final String JOB_GENDER	= JD_MEASURE_HOST + "measure/gender";//返回可能值:""
	public static final String DATE 		= JD_MEASURE_HOST + "measure/date";	
	public static final String MAJOR		= JD_MEASURE_HOST + "measure/major_v2";
	public static final String SCHOOL		= JD_MEASURE_HOST + "measure/school_v2";
	public static final String JOB_TYPE     = JD_MEASURE_HOST + "measure/job_type";//工作类型量化
	public static final String JOB_TYPE_V2  = JD_MEASURE_HOST + "measure/job_type_v2";//工作类型批量量化
	public static final String LOC_BATCH    = JD_MEASURE_HOST + "measure/region_detail_v2";//地址批量量化省市区
	public static final String REGION_BATCH = JD_MEASURE_HOST + "measure/inc_region_v2"; //公司地址批量量化
	
	// 职能量化接口 zhineng_version = v3
	public static final String ZHINENG_MEASURE_HOST_IP = "192.168.1.100";
	public static final int ZHINENG_MEASURE_HOST_PORT = 1234;
	public static final String ZHINENG_URL = "http://192.168.1.100:1234/batch";
	
//	//公司合并接口，薛龙版
//	public static final String INC_MERGE_URL_91 = "http://192.168.1.91:9002/stand_name_info";
//	public static final String INC_MERGE_URL_92 = "http://192.168.1.92:9002/stand_name_info";
}
