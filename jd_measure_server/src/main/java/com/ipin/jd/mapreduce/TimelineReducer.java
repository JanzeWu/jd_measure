package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.Base;
import com.ipin.jd.bean.db.JdTimeline;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by janze on 1/9/18.
 */
public class TimelineReducer extends Reducer<Object, Text, NullWritable, Text> {

    private Base base = new Base();

    @Override
    protected void reduce(Object key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        List<JdTimeline> list = new ArrayList<>();
        Map<String, Text> uniqe = new HashMap<>();
        for (Text value : values) {
            JdTimeline jdTimeline = base.getObjectFromHiveStringTemplate(value.toString(), JdTimeline.class);
            if (jdTimeline.getJdUpdateTimestamp() != null) {
                if (uniqe.get(jdTimeline.getJdSrcId() + jdTimeline.getJdUpdateTimestamp()) == null) {
                    uniqe.put(jdTimeline.getJdSrcId() + jdTimeline.getJdUpdateTimestamp(), value);
                    jdTimeline.setIsFirst("N");
                    jdTimeline.setIsLast("N");
                    list.add(jdTimeline);
                }
            }


        }
        if (list.isEmpty())
            return;

        Collections.sort(list, new Comparator<JdTimeline>() {
            @Override
            public int compare(JdTimeline o1, JdTimeline o2) {
                if(o1.getJdUpdateTimestamp() > o2.getJdUpdateTimestamp()){
                    return 1;
                }else if (o1.getJdUpdateTimestamp() < o2.getJdUpdateTimestamp()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        list.get(0).setIsFirst("Y");
        list.get(list.size() - 1).setIsLast("Y");

        for (JdTimeline timeline : list) {
            context.write(NullWritable.get(), new Text(timeline.getHiveStringTemplate()));
        }
    }
}
