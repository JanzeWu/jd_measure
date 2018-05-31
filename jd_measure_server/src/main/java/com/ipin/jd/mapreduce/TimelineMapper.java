package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.Base;
import com.ipin.jd.bean.db.JdTimeline;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by janze on 1/5/18.
 */
public class TimelineMapper extends Mapper<Object, Text, Text, Text>{

    private Base base = new Base();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        JdTimeline jdTimeline = base.getObjectFromHiveStringTemplate(value.toString(), JdTimeline.class);
        context.write(new Text(jdTimeline.getJdSrcId()), new Text(jdTimeline.getHiveStringTemplate()));
    }
}
