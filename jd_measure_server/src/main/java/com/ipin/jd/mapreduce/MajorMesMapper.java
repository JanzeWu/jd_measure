package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.Base;
import com.ipin.jd.bean.db.JdJobMajor;
import com.ipin.mes.beans.Major;
import com.ipin.mes.service.MeasureService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * Created by janze on 1/24/18.
 */
public class MajorMesMapper extends Mapper<Object,Text, NullWritable, Text> {

    private Base base = new Base();
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        JdJobMajor major = base.getObjectFromHiveStringTemplate(value.toString(), JdJobMajor.class);

        if(StringUtils.isNotBlank(major.getJobMajor()) && major.getDiplomaId() != null){
           Major m = MeasureService.measureMajor(major.getJobMajor(), major.getDiplomaId());
           if(m != null){
               major.setJobMajorId(m.getMajorId());
           }
        }
        context.write(NullWritable.get(), new Text(major.getHiveStringTemplate()));
    }
}
