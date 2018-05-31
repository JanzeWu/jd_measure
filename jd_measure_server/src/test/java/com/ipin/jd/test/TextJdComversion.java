package com.ipin.jd.test;


import com.ipin.jd.bean.db.*;
import com.ipin.jd.service.JdConversion;
import com.ipin.mes.util.ContentCheckUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by janze on 12/29/17.
 */
public class TextJdComversion {

    private JdConversion jdConversion;

    private ExecutorService cacheThreadPool = Executors.newCachedThreadPool();

    @Before
    public void init() throws  Exception{
        File file = new File("/home/janze/git_project/jd_measure/jd_measure_server/src/test/resources/jd_raw.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuffer buf = new StringBuffer();
        String line = null;
        while((line = reader.readLine()) != null){
            buf.append(line);
        }
        jdConversion = new JdConversion(buf.toString(), cacheThreadPool);
        jdConversion.convertToTableEntity();
    }

    @Test
    public void testConvertToJdEntry() throws Exception{


        JdEntry jdEntry = jdConversion.getJdEntry();
        Assert.assertTrue(jdEntry.getContentSign().equals("93cba649450da555e506acff363e0322"));
        Assert.assertTrue(jdEntry.getEntityId().equals("jd_liepin://1910875237"));
        Assert.assertTrue(jdEntry.getIncSegId().equals(ContentCheckUtil.getMD5(ContentCheckUtil.cleanBlankString("加诚博教(北京)教育咨询有限公司") + "ipin.com")));
        Assert.assertTrue(jdEntry.getJdId().equals(Base.generateJdId("jd_liepin://1910875237" , "93cba649450da555e506acff363e0322")));
        Assert.assertTrue(jdEntry.getJdSrcId().equals("jd_liepin://1910875237"));
        Assert.assertTrue(jdEntry.getJdSrcUrl().equals("https://www.liepin.com/job/1910875237.shtml"));
        Assert.assertTrue(jdEntry.getSrc().equals("binf::/data/crawler/jd/jd_liepin/2017/jd_liepin_201711.bin::274151508"));
        Assert.assertTrue(jdEntry.getJdContentPubDate().toString().equals("2017-12-25"));
        Assert.assertTrue(jdEntry.getJdContentPubTimestamp() == new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-25").getTime());
        Assert.assertTrue(jdEntry.getJdFromId() == 3);
        Assert.assertTrue(jdEntry.getStatus() == 0);
    }

    @Test
    public void testConvertJdInc(){
        JdInc jdInc = jdConversion.getJdInc();
        System.out.println(jdInc);
        Assert.assertTrue(jdInc.getIncSegId().equals(Base.generateIncSegId("加诚博教(北京)教育咨询有限公司")));
        Assert.assertTrue(jdInc.getIncSegName().equals("加诚博教(北京)教育咨询有限公司"));
        Assert.assertTrue(jdInc.getIncSegAliasName().equals("加诚博教"));
        Assert.assertTrue(jdInc.getIncSegCity().equals("北京"));
        Assert.assertTrue(jdInc.getIncSegCityId().equals("110100000000"));
        Assert.assertTrue(jdInc.getIncSegLocation().equals("朝阳区建外SOHO写字楼B座802室"));
        Assert.assertTrue(jdInc.getIncSegEmployee().equals("100-499人"));
        Assert.assertTrue(jdInc.getIncSegEmployeeMin() == 100);
        Assert.assertTrue(jdInc.getIncSegEmployeeMax() == 499);
        Assert.assertTrue(jdInc.getIncSegType().equals("合资"));
        Assert.assertTrue(jdInc.getIncSegTypeId() == 3);
        Assert.assertTrue(jdInc.getIncSegZipCodeRaw().equals("515154"));
        Assert.assertTrue(jdInc.getIncSegContactName().equals("公司联系人"));
        Assert.assertTrue(jdInc.getIncSegContactInfo().equals("公司联系信息"));
        Assert.assertTrue(jdInc.getIncSegVestInst().equals("iPIN"));
        Assert.assertTrue(jdInc.getIncSegStage().equals("B轮"));
        Assert.assertTrue(jdInc.getIncSegUrl().equals("https://www.wmzy.com"));
        Assert.assertTrue(jdInc.getIncSegDesc().equals("公司简介"));
    }

    @Test
    public void testConvertJdJob() throws Exception{
        JdJob jdJob = jdConversion.getJdJob();
        System.out.println(jdJob);
        Assert.assertTrue(jdJob.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(jdJob.getJobPosition().equals("留学文案"));
        Assert.assertTrue(jdJob.getJobCate().equals("工作类别"));
        Assert.assertTrue(jdJob.getJobSalary().equals("7-10万/年"));
        Assert.assertTrue(jdJob.getJobSalaryMin() == 5833);
        Assert.assertTrue(jdJob.getJobSalaryMax() == 8333);
        Assert.assertTrue(jdJob.getJobWorkAge().equals("经验不限"));
        Assert.assertTrue(jdJob.getJobWorkAgeMin() == -1);
        Assert.assertTrue(jdJob.getJobWorkAgeMax() == 99);
        Assert.assertTrue(jdJob.getJobDiploma().equals("本科及以上"));
        Assert.assertTrue(jdJob.getJobDiplomaId() == 7);
        Assert.assertTrue(jdJob.getJobPositionCount().equals("3人"));
        Assert.assertTrue(jdJob.getJobPositionMin() == 3);
        Assert.assertTrue(jdJob.getJobPositionMax() == 3);
        List<String> welfares = jdJob.getJobWelfare();
        Assert.assertTrue(welfares.get(0).equals("1. 月休六天，提供食宿，宿舍内设独立卫生间、冲凉房、热水、空调等"));
        Assert.assertTrue(welfares.get(1).equals("2. 伙食叁餐加夜宵，每餐四菜一汤"));
        Assert.assertTrue(jdJob.getJobAge().equals("年龄不限"));
        Assert.assertTrue(jdJob.getJobAgeNumMin() == null);
        Assert.assertTrue(jdJob.getJobAgeNumMax() == null);
        Assert.assertTrue(jdJob.getJobGenderRaw().equals("男"));
        Assert.assertTrue(jdJob.getJobGender().equals("M"));
        Assert.assertTrue(jdJob.getJobEndTime().equals("2010-10-10"));
        Assert.assertTrue(jdJob.getJobEndTimeDate().getTime() == new SimpleDateFormat("yyyy-MM-dd").parse("2010-10-10").getTime());
        List<String> skillList = jdJob.getSkillList();
        Assert.assertTrue(skillList.get(0).equals("java"));
        List<String> certList = jdJob.getCertList();
        Assert.assertTrue(certList.get(0).equals("英语四级"));
        List<String> languages = jdJob.getJobLanguage();
        Assert.assertTrue(languages.get(0).equals("英语"));
        Assert.assertTrue(jdJob.getRequireOverseaExp().equals("Y"));
        Assert.assertTrue(jdJob.getIsUrgent().equals("Y"));
        Assert.assertTrue(jdJob.getCvRecvRaw().equals("邮件"));
        Assert.assertTrue(jdJob.getCvRecvEmail().equals("someone@ipin.com"));
        Assert.assertTrue(jdJob.getJobDepartment().equals("工作部门"));
        Assert.assertTrue(jdJob.getJobLabel().equals("互联网,IT"));
        Assert.assertTrue(jdJob.getJobPositionDesc().equals("岗位职责：<BR>1、负责院校申请材料的审核，提交，通知书跟进，及签证状态跟进；<BR>2、负责学生住宿及接机安排及确认；<BR>3、与国外院校和国内合作伙伴保持良好的合作关系；<BR>4、配合团队维护数据系统及项目手册的更新；<BR>5、配合团队完成项目统计，及数据分析工作。<BR>任职资格：<BR>1、英语相关专业，本科及以上学历，良好的英文听，说，读，写能力；<BR>2、有两年以上留学行业文案工作经验者，优先考虑；<BR>3、有好的沟通能力和服务意识；<BR>4、有较强的团队合作精神；"));
        Assert.assertTrue(jdJob.getWorkDemand().equals("软件开发"));
        Assert.assertTrue(jdJob.getWorkDuty().equals("后台开发"));

    }

    @Test
    public void testConvertJobMajor(){
        List<JdJobMajor> jobMajorList = jdConversion.getJobMajorList();
        JdJobMajor jobMajor = jobMajorList.get(0);
        System.out.println(jobMajor);
        Assert.assertTrue(jobMajor.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(jobMajor.getJobMajor().equals("软件工程"));
        Assert.assertTrue(jobMajor.getJobMajorId().equals("52aedf5b747aec1cfc84166e"));
        Assert.assertTrue(jobMajor.getDiploma().equals("本科及以上"));
        Assert.assertTrue(jobMajor.getDiplomaId() == 7);
    }

    @Test
    public void testConvertJobType(){
        List<JdJobType> jobTypeList = jdConversion.getJobTypeList();
        JdJobType jobType = jobTypeList.get(0);
        System.out.println(jobType);
        Assert.assertTrue(jobType.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(jobType.getJobType().equals("全职"));
        Assert.assertTrue(jobType.getJobTypeId() == 1);
    }

    @Test
    public void testConvertJobWorkCity(){
        List<JdJobWorkCity> workCityList = jdConversion.getWorkCityList();
        JdJobWorkCity workCity = workCityList.get(0);
        System.out.println(workCity);
        Assert.assertTrue(workCity.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(workCity.getJobWorkCity().equals("北京"));
        Assert.assertTrue(workCity.getJobWorkCityId().equals("110100000000"));
    }

    @Test
    public void testConvertJobWorkLoc(){
        List<JdJobWorkLocation> locations = jdConversion.getWorkLocList();
        System.out.println(locations);
        JdJobWorkLocation workLoc = locations.get(0);
        Assert.assertTrue(workLoc.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(workLoc.getJobWorkLocation().equals("北京中关村"));
        Assert.assertTrue(workLoc.getJobLocationProvinceId().equals("110000000000"));
        Assert.assertTrue(workLoc.getJobLocationCityId().equals("110100000000"));
        Assert.assertTrue(workLoc.getJobLocationRegionId().equals("-"));
    }

    @Test
    public void testConvertJdOthers() {
        JdOthers others = jdConversion.getJdOthers();
        Assert.assertTrue(others.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        List<String> keyworks = others.getKeyWords();
        Assert.assertTrue(keyworks.get(0).equals("关键字"));
        Assert.assertTrue(others.getPosType().equals("企业职位"));
        Assert.assertTrue(others.getIsFulltime().equals("是全日制"));
        Assert.assertTrue(others.getSalaryCombine().equals("薪酬组成"));
        Assert.assertTrue(others.getHolidayWelfare().equals("元旦<BR>春节"));
        Assert.assertTrue(others.getTrafficWelfare().equals("交通补贴"));
        Assert.assertTrue(others.getJobSubSize().equals("0人"));
        Assert.assertTrue(others.getSocialWelfare().equals("社保"));
        Assert.assertTrue(others.getJdRemedy().equals("不知道是什么"));
        Assert.assertTrue(others.getLivingWelfare().equals("生活补贴"));
        Assert.assertTrue(others.getJobReport().equals("部门经理"));
        Assert.assertTrue(others.getJobReportDetail().equals("报告细节"));
    }

    @Test
    public void testConvertJdPage(){
        JdPage page = jdConversion.getJdPage();
        Assert.assertTrue(page.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(page.getJdSrcId().equals("jd_liepin://1910875237"));
        Assert.assertTrue(page.getContentSign().equals("93cba649450da555e506acff363e0322"));
        Assert.assertTrue(page.getPageContentPath().equals("binf::/data/crawler/jd/jd_liepin/2017/jd_liepin_201711.bin::274151508"));
        Assert.assertTrue(page.getCrawlerUpdateTime() == 1466053295000L );
        Assert.assertTrue(page.getCrawlerUpdateDate().getTime() == 1466053295000L);
    }

    @Test
    public void testConvertTimeline() throws Exception{
        JdTimeline timeline = jdConversion.getJdTimeline();
        Assert.assertTrue(timeline.getJdSrcId().equals("jd_liepin://1910875237"));
        Assert.assertTrue(timeline.getJdUpdateTimestamp() == 1466053295000L);
        Assert.assertTrue(timeline.getJdUpdateDate().getTime() == timeline.getJdUpdateTimestamp());
    }

    @Test
    public void testConvertJobZhineng(){
        JobZhineng zn = jdConversion.getJobZhineng();
        System.out.println(zn);
        Assert.assertTrue(zn.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(zn.getJobPosition().equals("留学文案"));
        Assert.assertTrue(zn.getZhinengName().equals("教学/教务管理"));
        Assert.assertTrue(zn.getZhinengId() == 77);
        Assert.assertTrue(zn.getZhinengVersion().equals("v3"));
    }
    @Test
    public void testConvertMeasureIncIndustry(){
        List<MeasureIncIndustry> industryList = jdConversion.getMeasureIncIndustryList();
        System.out.println(industryList);
        MeasureIncIndustry industry = industryList.get(0);
        Assert.assertTrue(industry.getJdId().equals("7c153856622af33acce46d39d0ebf48c"));
        Assert.assertTrue(industry.getIncSegIndustry().equals("教育/培训/学术/科研/院校"));
        Assert.assertTrue(industry.getIncSegIndustryId() == 26);
    }


    @After
    public void clearUp() throws  Exception {
        if(this.cacheThreadPool != null && !this.cacheThreadPool.isShutdown()){
            this.cacheThreadPool.shutdown();
            this.cacheThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
