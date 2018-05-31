package com.ipin.jd.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ipin.jd.bean.db.*;
import com.ipin.jd.bean.mes.RegionDetail;
import com.ipin.jd.bean.mes.RegionDetails;
import com.ipin.jd.bean.mes.ZhinengMes;
import com.ipin.jd.consts.MeasureUrl;
import com.ipin.jd.consts.Regulation;
import com.ipin.jd.helper.HttpHelper;
import com.ipin.jd.task.CityTask;
import com.ipin.jd.task.LocationTask;
import com.ipin.jd.task.MajorTask;
import com.ipin.jd.task.ZhinengTask;
import com.ipin.mes.beans.DateMes;
import com.ipin.mes.beans.Major;
import com.ipin.mes.beans.result.MeasureResult;
import com.ipin.mes.constants.IndustryStd;
import com.ipin.mes.service.MeasureService;
import com.ipin.mes.util.ContentCheckUtil;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.swing.plaf.synth.Region;
import java.sql.Array;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.*;

import static com.ipin.jd.consts.Regulation.RANDOM_INT_MAX;

/**
 * Created by janze on 12/21/17.
 */
public class JdConversion {

    private static Random random = new Random();


    private ExecutorService cachedThreadPool;

    private String jdRaw;
    private DBObject jdObj;
    private String jdId;

    private JdEntry jdEntry;
    private JdInc jdInc;
    private JdJob jdJob;
    private List<JdJobType> jobTypeList = new ArrayList<>();
    private List<JdJobWorkCity> workCityList = new ArrayList<>();
    private List<JdJobMajor> jobMajorList = new ArrayList<>();
    private List<JdJobWorkLocation> workLocList = new ArrayList<>();

    private JdOthers jdOthers;
    private JdPage jdPage;
    private JdTimeline jdTimeline;
    private JobZhineng jobZhineng;
    private List<MeasureIncIndustry> measureIncIndustryList = new ArrayList<>();

    public JdConversion(String jdRaw, ExecutorService cachedThreadPool) {
        this.jdRaw = jdRaw;
        this.jdObj = (DBObject)JSON.parse(jdRaw);
        this.cachedThreadPool = cachedThreadPool;
    }


    public void convertToTableEntity(){

        try {
            DBObject indexInfo = (DBObject) this.jdObj.get("indexInfo");
            // 如果entityId为空，取值indexUrl
            if(StringUtils.isBlank((String)indexInfo.get("entityId"))){
                indexInfo.put("entityId", (String)indexInfo.get("indexUrl"));
            }
            String entityId = (String) indexInfo.get("entityId");
            String contentSign =  (String) indexInfo.get("contentSign");
            // 计算jdId
            if(StringUtils.isBlank(entityId) && StringUtils.isBlank(contentSign)){
                return;
            }
            this.jdId = Base.generateJdId(entityId, contentSign);
            // 清洗特殊字符
            String dataType = (String) this.jdObj.get("dataType");
            if(Regulation.DATA_TYPE_JD.equals(dataType)){
                // 展示字符处理，将'\n' 转 '<BR>'
                DBObject inc = (DBObject) this.jdObj.get("jdInc");
                ContentCheckUtil.cleanShowDBObject(inc, "incIntro");

                DBObject job = (DBObject)this.jdObj.get("jdJob");
                ContentCheckUtil.cleanShowDBObject(job, "jobDesc");

                DBObject others = (DBObject)this.jdObj.get("others");
                ContentCheckUtil.cleanShowDBObject(others, "holidayWelfare", "livingWelfare",
                        "salaryCombine", "socialWelfare", "trafficWelfare", "jobReportDetail");

                // 非展示字段字符处理，删除特殊字符
                ContentCheckUtil.cleanDBObject(this.jdObj);

                indexInfo.put("isUpdated", "Y");
                this.getJdEntryFromJdRaw();

                this.getJdIncFromJdRaw();
                this.getJdJobFromJdRaw();
                this.getJdJobTypeListFromJdRaw();
                this.getJdOthersFromJdRaw();
                this.getMeasureIncIndustryListFromJdRaw();

                MajorTask majorTask = new MajorTask(this);
                Future<List<JdJobMajor>> majorFuture = this.cachedThreadPool.submit(majorTask);

                CityTask cityTask = new CityTask(this);
                Future<List<JdJobWorkCity>> cityFuture = this.cachedThreadPool.submit(cityTask);

                LocationTask locationTask = new LocationTask(this);
                Future<List<JdJobWorkLocation>> locationFuture = this.cachedThreadPool.submit(locationTask);

                ZhinengTask zhinengTask = new ZhinengTask(this);
                Future<JobZhineng> zhinengFuture = this.cachedThreadPool.submit(zhinengTask);

                majorFuture.get();
                cityFuture.get();
                locationFuture.get();
                zhinengFuture.get();
            }
            this.getJdPageFromJdRaw();
            this.getJdTimelineFromJdRaw();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getJdEntryFromJdRaw(){
        JdEntry jdEntry = new JdEntry();
        DBObject jd = this.jdObj;
        DBObject indexInfo = (DBObject) jd.get("indexInfo");

        jdEntry.setJdId(this.jdId);
        jdEntry.setEntityId((String)indexInfo.get("entityId"));
        jdEntry.setJdSrcId((String) indexInfo.get("indexUrl"));
        jdEntry.setContentSign((String)indexInfo.get("contentSign"));
        jdEntry.setJdFromId(MeasureService.measureChannel((String) jd.get("jdFrom")));

        DateMes dateMes = MeasureService.matchDate((String)jd.get("pubTime"));
        if(dateMes.getDate_long() != -1){
            jdEntry.setJdContentPubDate( new Date(dateMes.getDate_long()) );
            jdEntry.setJdContentPubTimestamp(dateMes.getDate_long());
        }

        jdEntry.setJdSrcUrl((String)indexInfo.get("url"));
        DBObject inc = (DBObject) jd.get("jdInc");
        jdEntry.setIncSegId(jdEntry.generateIncSegId((String) inc.get("incName")));
        jdEntry.setStatus((Integer) indexInfo.get("status"));
        jdEntry.setSrc((String) indexInfo.get("binPath"));
        jdEntry.setRandomInt(getRandomInt());
        jdEntry.setEtlDate(new Date(new java.util.Date().getTime()));
        if(jdEntry.legalEntry()){
            this.jdEntry = jdEntry;
        }
    }

    private void getJdIncFromJdRaw(){
        JdInc jdInc = new JdInc();
        DBObject jd = this.jdObj;
        DBObject inc = (DBObject) jd.get("jdInc");
        jdInc.setIncSegName((String) inc.get("incName"));
        jdInc.setIncSegId(jdInc.generateIncSegId(jdInc.getIncSegName()));
        jdInc.setIncSegAliasName((String) inc.get("incAliasName"));
        jdInc.setIncSegCity((String) inc.get("incCity"));
        //inc_city_id
        if(StringUtils.isNotBlank(jdInc.getIncSegCity())){
            List<RegionDetail> rds = measureRegion(jdInc.getIncSegCity());
            if(rds != null && !rds.isEmpty()){
                jdInc.setIncSegCityId(rds.get(0) != null ? rds.get(0).getCity_id() : null);
            }
        }
        jdInc.setIncSegLocation((String) inc.get("incLocation"));
        jdInc.setIncSegEmployee((String) inc.get("incScale"));
        if(StringUtils.isNotBlank(jdInc.getIncSegEmployee())){
            List<Integer> list = MeasureService.measureComSize(jdInc.getIncSegEmployee());
            if(list != null && !list.isEmpty()){
                jdInc.setIncSegEmployeeMin(list.get(0));
                jdInc.setIncSegEmployeeMax(list.size() > 1 ? list.get(1) : null);
            }
        }
        jdInc.setIncSegType((String) inc.get("incType"));
        if(StringUtils.isNotBlank(jdInc.getIncSegType())){
            jdInc.setIncSegTypeId(MeasureService.measureIncType(jdInc.getIncSegType()));
        }
        jdInc.setIncSegZipCodeRaw((String) inc.get("incZipCode"));
        // inc_seg_zipcode
        jdInc.setIncSegContactName((String) inc.get("incContactName"));
        jdInc.setIncSegContactInfo((String) inc.get("incContactInfo"));
        jdInc.setIncSegVestInst((String) inc.get("investIns"));
        jdInc.setIncSegStage((String) inc.get("incStage"));
        jdInc.setIncSegUrl((String) inc.get("incUrl"));
        jdInc.setIncSegDesc(ContentCheckUtil.cleanShowString((String) inc.get("incIntro")));
        jdInc.setRandomInt(this.getRandomInt());
        jdInc.setEtlDate(new Date(new java.util.Date().getTime()));
        if(jdInc.legalInc()){
            this.jdInc = jdInc;
        }

    }


    private List<RegionDetail> measureRegion(String... locRaws){
        Gson gson = new Gson();
        Map<String, List> params = new HashMap<>();
        List list = new ArrayList();
        for(String item : locRaws){
            list.add(item);
        }
        params.put("location_strs", list);
        String res = HttpHelper.postByJson(MeasureUrl.LOC_BATCH, gson.toJson(params), false);
        BasicDBList resList = (BasicDBList) JSON.parse(res);
        List<RegionDetail> rds = new ArrayList<>();
        if(resList != null && !resList.isEmpty()){
            DBObject obj = (DBObject)resList.get(0);
            RegionDetail rd = new RegionDetail();
            rd.setCity_id((String) obj.get("city_id"));
            rd.setCity_name((String) obj.get("city_name"));
            rd.setProvince_id((String) obj.get("province_id"));
            rd.setProvince_name((String) obj.get("province_name"));
            rd.setRegion_id((String) obj.get("region_id"));
            rd.setRegion_name((String) obj.get("region_name"));
            rds.add(rd);
        }
        return rds;
    }
    private void getJdJobFromJdRaw(){
        JdJob jdJob = new JdJob();
        DBObject jd = this.jdObj;
        DBObject job = (DBObject) jd.get("jdJob");
        DBObject others = (DBObject) jd.get("others");

        jdJob.setJdId(this.jdId);
        jdJob.setJobPosition((String) job.get("jobPosition"));
        jdJob.setJobCate((String)job.get("jobCate"));
        jdJob.setJobSalary((String)job.get("jobSalary"));
        if(StringUtils.isNotBlank(jdJob.getJobSalary())){
            List<Integer> list = MeasureService.measureSalary(jdJob.getJobSalary());
            if(list != null && !list.isEmpty()){
                jdJob.setJobSalaryMin(list.get(0) != null ? list.get(0).doubleValue() : null);
                if(list.size() > 1){
                    jdJob.setJobSalaryMax(list.get(1) != null ? list.get(1).doubleValue() : null);
                }
            }

        }

        jdJob.setJobWorkAge((String)job.get("jobWorkAge"));
        if(StringUtils.isNotBlank(jdJob.getJobWorkAge())){
            List<Integer> list = MeasureService.measureWorkAge(jdJob.getJobWorkAge(), (String) this.jdObj.get("jdFrom"));
            if(list != null && !list.isEmpty()){
                jdJob.setJobWorkAgeMin(list.get(0) != null ? list.get(0) * 365 : list.get(0));
                if(list.size() > 1){
                    jdJob.setJobWorkAgeMax(list.get(1) != null ? list.get(1) * 365 : list.get(1));
                }
            }

        }
        jdJob.setJobDiploma((String) job.get("jobDiploma"));
        if(StringUtils.isNotBlank(jdJob.getJobDiploma())){
            MeasureResult res =  MeasureService.measureDiploma(jdJob.getJobDiploma());
            if(res != null){
                jdJob.setJobDiplomaId(res.getCode());
            }
        }
        jdJob.setJobPositionCount((String) job.get("jobNum"));
        if(StringUtils.isNotBlank(jdJob.getJobPositionCount())){
            List<Integer> list = MeasureService.measureComSize(jdJob.getJobPositionCount());
            if(null != list && !list.isEmpty()){
                jdJob.setJobPositionMin(list.get(0));
                if(list.size() > 1){
                    jdJob.setJobPositionMax(list.get(1));
                }
            }
        }

        BasicDBList welfareList = (BasicDBList) job.get("jobWelfareList");
        jdJob.setJobWelfare(this.getStringListFromBasicDBList(welfareList));
        jdJob.setJobAge((String) job.get("age"));
        if(StringUtils.isNotBlank(jdJob.getJobAge())){
            List<Integer> list = MeasureService.measureJobAge(jdJob.getJobAge());
            if(list != null && !list.isEmpty()){
                jdJob.setJobAgeNumMin(list.get(0));
                jdJob.setJobAgeNumMax(list.size() > 1 ? list.get(1) : null);
            }

        }

        jdJob.setJobGenderRaw((String) job.get("gender"));
        if(StringUtils.isNotBlank(jdJob.getJobGenderRaw())){
            Integer genderNum = MeasureService.measureGender(jdJob.getJobGenderRaw());
            if(genderNum != null){
                switch (genderNum){
                    /**
                     * "女": 0
                     * "男": 1
                     * "不限": 2
                     * "保密": 9
                     */
                    case 0 : jdJob.setJobGender("F");break;
                    case 1 : jdJob.setJobGender("M"); break;
                    case 2 : jdJob.setJobGender("-");break;
                    case 9 : jdJob.setJobGender("-");break;
                    default: jdJob.setJobGender(null);
                }
            }
        }

        jdJob.setJobEndTime((String) job.get("jobEndTime"));
        if(StringUtils.isNotBlank(jdJob.getJobEndTime())){
            DateMes mes = MeasureService.matchDate(jdJob.getJobEndTime());
            if(mes.getDate_long() != -1L){
                jdJob.setJobEndTimeDate(new Date(mes.getDate_long()));
            }
        }

        BasicDBList skillList =  (BasicDBList) job.get("skillList");
        jdJob.setSkillList( getStringListFromBasicDBList(skillList));

        BasicDBList certList = (BasicDBList) job.get("certList");
        jdJob.setCertList(getStringListFromBasicDBList(certList));

        jdJob.setJobLanguage(getStringListFromBasicDBList((BasicDBList) others.get("languageList")));

        String overSea = (String) others.get("overSea");
        jdJob.setRequireOverseaExp(StringUtils.isNotBlank(overSea) && overSea.toLowerCase().startsWith("y") ? "Y" : "N");

        String urgent = (String) others.get("urgent");
        jdJob.setIsUrgent(StringUtils.isNotBlank(urgent) && urgent.toLowerCase().startsWith("y") ? "Y" : "N");

        jdJob.setCvRecvRaw((String) job.get("cvRecviveRaw"));
        jdJob.setCvRecvEmail((String) job.get("email"));

        jdJob.setJobDepartment((String) others.get("jobDepartment"));
        BasicDBList labelList = (BasicDBList) job.get("jobLabelList");
        if(labelList != null && !labelList.isEmpty()){
            String[] labels = new String[labelList.size()];
            labelList.toArray(labels);
            jdJob.setJobLabel(StringUtils.join((Arrays.asList(labels)), ","));
        }
        jdJob.setJobPositionDesc((String) job.get("jobDesc"));
        jdJob.setWorkDemand((String) job.get("workDemand"));
        jdJob.setWorkDuty((String) job.get("workDuty"));
        jdJob.setRandomInt(getRandomInt());
        jdJob.setEtlDate(new Date(new java.util.Date().getTime()));

        if(jdJob.legalJob()){
            this.jdJob = jdJob;
        }

    }

    public void getJdJobMajorListFromJdRaw(){
        DBObject job = (DBObject) this.jdObj.get("jdJob");
        //jobMajors
        BasicDBList jobMajors = (BasicDBList) job.get("jobMajorList");
        if(jobMajors != null && !jobMajors.isEmpty()){
            for(Object item : jobMajors){
                String major = (String) item;
                if(StringUtils.isNotBlank(major)){
                    JdJobMajor jobMajor = new JdJobMajor();
                    jobMajor.setJdId(this.jdId);
                    jobMajor.setJobMajor(major);
                    if(jdJob.getJobDiplomaId() != null){
                        Major res = MeasureService.measureMajor(major, jdJob.getJobDiplomaId());
                        if(res != null){
                            jobMajor.setJobMajorId(res.getMajorId());
                        }
                    }
                    jobMajor.setDiploma(jdJob.getJobDiploma());
                    jobMajor.setDiplomaId(jdJob.getJobDiplomaId());
                    jobMajor.setRandomInt(this.getRandomInt());
                    jobMajor.setEtlDate(new Date(new java.util.Date().getTime()));
                    this.jobMajorList.add(jobMajor);
                }
            }
        }
    }

    private void getJdJobTypeListFromJdRaw(){
        DBObject job = (DBObject) this.jdObj.get("jdJob");
        //jobTypes
        BasicDBList jobTypes = (BasicDBList) job.get("jobTypeList");
        if(jobTypes != null && !jobTypes.isEmpty()){
            for(Object item : jobTypes){
                String type = (String) item;
                if(StringUtils.isNotBlank(type)){
                    JdJobType jobType = new JdJobType();
                    jobType.setJdId(this.jdId);
                    jobType.setJobType(type);
                    jobType.setJobTypeId(MeasureService.measureJobType(type));
                    jobType.setRandomInt(this.getRandomInt());
                    jobType.setEtlDate(new Date(new java.util.Date().getTime()));
                    this.jobTypeList.add(jobType);
                }
            }
        }
    }

    public void getJdJobWorkCityListFromJdRaw(){
        DBObject job = (DBObject) this.jdObj.get("jdJob");
        // workCity
        BasicDBList workCityList = (BasicDBList) job.get("jobWorkCityList");
        if(workCityList != null && !workCityList.isEmpty()){
            for(Object item : workCityList){
                String city = (String) item;
                if(StringUtils.isNotBlank(city)){
                    JdJobWorkCity workCity = new JdJobWorkCity();
                    workCity.setJdId(this.jdId);
                    workCity.setJobWorkCity(city);
                    List<RegionDetail> rds = measureRegion(city);
                    if(!rds.isEmpty()){
                        workCity.setJobWorkCityId(rds.get(0).getCity_id());
                    }
                    workCity.setRandomInt(this.getRandomInt());
                    workCity.setEtlDate(new Date(new java.util.Date().getTime()));
                    this.workCityList.add(workCity);
                }
            }
        }
    }

    public void getJdJobWorkLocListFromJdRaw(){
        DBObject job = (DBObject) this.jdObj.get("jdJob");
        // workLoc
        BasicDBList workLocList = (BasicDBList) job.get("jobWorkLocList");
        if(workLocList != null && !workLocList.isEmpty()){
            for(Object item : workLocList){
                String loc = (String) item;
                if(StringUtils.isNotBlank(loc)){
                    JdJobWorkLocation workLoc = new JdJobWorkLocation();
                    workLoc.setJdId(this.jdId);
                    workLoc.setJobWorkLocation(loc);
                    List<RegionDetail> rds = measureRegion(loc);
                    if(!rds.isEmpty()){
                        RegionDetail rd = rds.get(0);
                        if(rd != null){
                            workLoc.setJobLocationProvinceId(rd.getProvince_id());
                            workLoc.setJobLocationCityId(rd.getCity_id());
                            workLoc.setJobLocationRegionId(rd.getRegion_id());
                        }

                    }
                    workLoc.setRandomInt(this.getRandomInt());
                    workLoc.setEtlDate(new Date(new java.util.Date().getTime()));
                    this.workLocList.add(workLoc);
                }
            }
        }
    }
    /**
     */
    private void getJdOthersFromJdRaw(){
        DBObject others = (DBObject) this.jdObj.get("others");
        JdOthers jdOthers = new JdOthers();
        jdOthers.setJdId(this.jdId);
        jdOthers.setKeyWords(this.getStringListFromBasicDBList((BasicDBList) others.get("keyWordList")));
        jdOthers.setPosType((String) others.get("posType"));
        jdOthers.setIsFulltime((String) others.get("isFullTime"));
        jdOthers.setSalaryCombine((String) others.get("salaryCombine"));
        jdOthers.setHolidayWelfare((String) others.get("holidayWelfare"));
        jdOthers.setTrafficWelfare((String) others.get("trafficWelfare"));
        jdOthers.setJobSubSize((String) others.get("jobSubSize"));
        jdOthers.setSocialWelfare((String) others.get("socialWelfare"));
        jdOthers.setJdRemedy((String) others.get("jdRemedy"));
        jdOthers.setLivingWelfare((String) others.get("livingWelfare"));
        jdOthers.setJobReport((String) others.get("jobReport"));
        jdOthers.setJobReportDetail((String) others.get("jobReportDetail"));
        jdOthers.setRandomInt(this.getRandomInt());
        jdOthers.setEtlDate(new Date(new java.util.Date().getTime()));
        if(jdOthers.isLegal())
            this.jdOthers = jdOthers;
    }

    private void getJdPageFromJdRaw(){
        DBObject indexInfo = (DBObject)this.jdObj.get("indexInfo");
        JdPage page = new JdPage();
        page.setJdId(this.jdId);
        page.setJdSrcId((String) indexInfo.get("indexUrl"));
        page.setContentSign((String) indexInfo.get("contentSign"));
        page.setPageContentPath((String) indexInfo.get("binPath"));
        page.setCrawlerUpdateTime((Long) indexInfo.get("crawlerUpdateTime"));
        page.setCrawlerUpdateDate(new Date(page.getCrawlerUpdateTime()));
        page.setEtlDate(new Date(new java.util.Date().getTime()));
        if(page.isLegal())
            this.jdPage = page;
    }

    private void getJdTimelineFromJdRaw(){
        DBObject indexInfo =(DBObject) this.jdObj.get("indexInfo");
        Long updateTime = (Long) indexInfo.get("updateTime");
        String isUpdated = (String) indexInfo.get("isUpdated");
        if(updateTime != null && isUpdated.toLowerCase().equals("y")){
                JdTimeline timeline = new JdTimeline();
                timeline.setJdSrcId((String) indexInfo.get("indexUrl"));
                timeline.setJdUpdateTimestamp(updateTime);
                timeline.setJdUpdateDate(new Date(updateTime));
                // isFirst islast by map reduce
                timeline.setEtlDate(new Date(new java.util.Date().getTime()));
                this.jdTimeline = timeline;
        }
    }

    public void getJobZhinengFromJdRaw(){
        DBObject job = (DBObject) this.jdObj.get("jdJob");
        String jobPos = (String) job.get("jobPosition");
        String jobDesc = (String) job.get("jobDesc");
        if(! (StringUtils.isBlank(jobPos) && StringUtils.isBlank(jobDesc))){
            JobZhineng jobZhineng = new JobZhineng();
            jobZhineng.setJdId(this.jdId);
            jobZhineng.setJobPosition(StringUtils.isNotBlank(jobPos) ? jobPos : "-");
            ZhinengMes zhinengMes = this.measureZhineng(jobPos, jobDesc);
            if(StringUtils.isNotBlank(zhinengMes.getZhinengName())){
                jobZhineng.setZhinengId(zhinengMes.getZhinengId());
                jobZhineng.setZhinengName(zhinengMes.getZhinengName());
                jobZhineng.setZhinengVersion("v3");
            }
            jobZhineng.setSrc("-");
            jobZhineng.setRandomInt(this.getRandomInt());
            jobZhineng.setStartDate(new Date(new java.util.Date().getTime()));
            this.jobZhineng = jobZhineng;
        }
    }

    public List<ZhinengMes> measureZhinengs(List<Map<String, String>> params){
        List<ZhinengMes> list = new ArrayList<>();
        Gson gson = new Gson();
        DBObject resEntity = (DBObject)JSON.parse(HttpHelper.postByJson(MeasureUrl.ZHINENG_URL, gson.toJson(params),
                true));
        BasicDBList resList = (BasicDBList)resEntity;

        for(Object zhinengMesObj : resList){
            ZhinengMes zhineng = new ZhinengMes();
            BasicDBList zhinengMesList = (BasicDBList)zhinengMesObj;
            Iterator<Object> it = zhinengMesList.iterator();
            float score = Float.MIN_NORMAL;
            while(it.hasNext()){
                DBObject zhinengMes = (DBObject)it.next();
                BasicDBList scoreList = (BasicDBList) zhinengMes;
                if(scoreList != null && scoreList.size() >= 4){
                    if(scoreList.get(3) != null){
                        float tmp = Float.parseFloat((String)scoreList.get(3));
                        if(tmp > score){
                            score = tmp;
                            zhineng.setZhinengName((String)scoreList.get(2));
                            zhineng.setZhinengId((Integer.parseInt((String) scoreList.get(0))));
                        }
                    }
                }
            }
            list.add(zhineng);
        }
        return list;

    }
    private ZhinengMes measureZhineng(String jobPos, String jobDesc){
        ZhinengMes zhineng = new ZhinengMes();

        Map<String, String> map = new HashMap<>();
        map.put("jobPos", jobPos);
        map.put("jobDesc", jobDesc);
        List<Map<String, String>> params = new ArrayList<>();
        params.add(map);

        List<ZhinengMes> list = this.measureZhinengs(params);
        if(list != null && !list.isEmpty() && list.get(0) != null){
            zhineng = list.get(0);
        }
        return zhineng;
    }
    private void getMeasureIncIndustryListFromJdRaw(){
        DBObject inc = (DBObject) this.jdObj.get("jdInc");
        BasicDBList list = (BasicDBList) inc.get("incIndustryList");
        if(list != null && !list.isEmpty()){
            for(Object item : list){
                String industry = (String) item;
                if(StringUtils.isNotBlank(industry)){
                    MeasureIncIndustry incIndustry = new MeasureIncIndustry();
                    incIndustry.setJdId(this.jdId);
                    incIndustry.setIncSegIndustry(industry);
                    IndustryStd industryStd = MeasureService.matchIndustry(industry, (String) this.jdObj.get("jdFrom"));
                    incIndustry.setIncSegIndustryId(industryStd != null ? industryStd.getIndId() : -1);
                    incIndustry.setSrc("-");
                    incIndustry.setRandomInt(this.getRandomInt());
                    incIndustry.setStartDate(new Date(new java.util.Date().getTime()));
                    this.measureIncIndustryList.add(incIndustry);
                }
            }
        }
    }


    public List<String> getStringListFromBasicDBList(BasicDBList list){
        List<String> res = null;
        if(list != null && !list.isEmpty()){
            res = new ArrayList<>();
            for(Object item : list){
                res.add((String) item);
            }
        }
        return res;
    }

    public String getJdRaw() {
        return jdRaw;
    }

    public JdEntry getJdEntry() {
        return jdEntry;
    }

    public JdInc getJdInc() {
        return jdInc;
    }

    public JdJob getJdJob() {
        return jdJob;
    }

    public JdOthers getJdOthers() {
        return jdOthers;
    }

    public JdPage getJdPage() {
        return jdPage;
    }

    public JobZhineng getJobZhineng() {
        return jobZhineng;
    }

    public List<JdJobType> getJobTypeList() {
        return jobTypeList;
    }

    public List<JdJobWorkCity> getWorkCityList() {
        return workCityList;
    }

    public List<JdJobMajor> getJobMajorList() {
        return jobMajorList;
    }

    public List<JdJobWorkLocation> getWorkLocList() {
        return workLocList;
    }

    public List<MeasureIncIndustry> getMeasureIncIndustryList() {
        return measureIncIndustryList;
    }

    public JdTimeline getJdTimeline() {
        return jdTimeline;
    }

    public int getRandomInt(){
        return this.random.nextInt(RANDOM_INT_MAX + 1);
    }


}
