package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.*;
import com.ipin.jd.consts.JdTable;
import com.ipin.jd.service.JdConversion;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by janze on 1/2/18.
 */
public class JdMapper extends Mapper<Object, Text, Text, Text> {


    private ExecutorService cacheThreadPool ;
    private String jdFileName;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        cacheThreadPool = Executors.newCachedThreadPool();
        jdFileName = ((FileSplit)context.getInputSplit()).getPath().getParent().getName();
    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        JdConversion jdConversion = new JdConversion(value.toString(), cacheThreadPool);
        jdConversion.convertToTableEntity();

        JdEntry jdEntry = jdConversion.getJdEntry();
        if(jdEntry == null){
            return;
        }
        jdEntry.setSrc(jdFileName);
        context.write(new Text(JdTable.TABLE_JD_ENTRY), new Text(jdEntry.getHiveStringTemplate()));

        JdInc jdInc = jdConversion.getJdInc();
        if(jdInc != null){
            context.write(new Text(JdTable.TABLE_JD_INC), new Text(jdInc.getHiveStringTemplate()));
        }


        JdJob jdJob = jdConversion.getJdJob();
        if(jdJob != null)
            context.write(new Text(JdTable.TABLE_JD_JOB), new Text(jdJob.getHiveStringTemplate()));

        List<JdJobMajor> jobMajorList = jdConversion.getJobMajorList();
        if(jobMajorList!= null){
            for(JdJobMajor major : jobMajorList){
                if(major != null)
                    context.write(new Text(JdTable.TABLE_JD_JOB_MAJOR), new Text(major.getHiveStringTemplate()));
            }
        }


        List<JdJobType> jobTypeList = jdConversion.getJobTypeList();
        if(jobTypeList != null){
            for(JdJobType type : jobTypeList){
                if(type != null)
                    context.write(new Text(JdTable.TABLE_JD_JOB_TYPE), new Text(type.getHiveStringTemplate()));
            }

        }

        List<JdJobWorkCity> workCityList = jdConversion.getWorkCityList();
        if(workCityList != null){
            for(JdJobWorkCity city : workCityList){
                if(city != null)
                    context.write(new Text(JdTable.TABLE_JD_JOB_WORK_CITY), new Text(city.getHiveStringTemplate()));
            }
        }


        List<JdJobWorkLocation> workLocationList = jdConversion.getWorkLocList();
        if(workLocationList !=null){
            for(JdJobWorkLocation location : workLocationList){
                if(location != null)
                    context.write(new Text(JdTable.TABLE_JD_JOB_WORK_LOCATION), new Text(location.getHiveStringTemplate()));
            }
        }


        JdOthers jdOthers = jdConversion.getJdOthers();
        if(jdOthers != null)
            context.write(new Text(JdTable.TABLE_JD_OTHERS), new Text(jdOthers.getHiveStringTemplate()));

        JdPage page = jdConversion.getJdPage();
        if(page != null)
            context.write(new Text(JdTable.TABLE_JD_PAGE), new Text(page.getHiveStringTemplate()));

        JdTimeline timeline = jdConversion.getJdTimeline();
        if(timeline != null)
            context.write(new Text(JdTable.TABLE_JD_TIMELINE), new Text(timeline.getHiveStringTemplate()));

        JobZhineng zhineng = jdConversion.getJobZhineng();
        if(zhineng != null){
            zhineng.setSrc(jdFileName);
            context.write(new Text(JdTable.TABLE_JOB_ZHINENG), new Text(zhineng.getHiveStringTemplate()));
        }

        List<MeasureIncIndustry> industryList = jdConversion.getMeasureIncIndustryList();
        if(industryList != null){
            for(MeasureIncIndustry industry : industryList){
                if(industry != null){
                    industry.setSrc(jdFileName);
                    context.write(new Text(JdTable.TABLE_MEASURE_INC_INDUSTRY), new Text(industry.getHiveStringTemplate()));
                }
            }
        }


    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {

        if(this.cacheThreadPool != null && !this.cacheThreadPool.isShutdown()){
            this.cacheThreadPool.shutdown();
            this.cacheThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
