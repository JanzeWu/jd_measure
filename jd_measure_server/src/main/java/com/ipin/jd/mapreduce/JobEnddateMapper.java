package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.Base;
import com.ipin.jd.bean.db.JdJob;
import com.ipin.mes.beans.DateMes;
import com.ipin.mes.service.MeasureService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.sql.Date;

/**
 * Created by janze on 1/30/18.
 */
public class JobEnddateMapper extends Mapper<Object, Text, NullWritable, Text> {

    Base base = new Base();
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        JdJob jdJob = base.getObjectFromHiveStringTemplate(value.toString(), JdJob.class);
        if(StringUtils.isNotBlank(jdJob.getJobEndTime())){
            DateMes res = MeasureService.matchDate(jdJob.getJobEndTime());
            if(res.getDate_long() != -1){
                jdJob.setJobEndTimeDate(new Date(res.getDate_long()));
            }
        }
        if(jdJob.getJobWorkAgeMin() != null){
            jdJob.setJobWorkAgeMin(jdJob.getJobWorkAgeMin() * 365);
        }
        if(jdJob.getJobWorkAgeMax() != null){
            jdJob.setJobWorkAgeMax(jdJob.getJobWorkAgeMax() * 365 );
        }

        context.write(NullWritable.get(), new Text(jdJob.getHiveStringTemplate()));
    }
}
