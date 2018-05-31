package com.ipin.jd.server;

import com.ipin.jd.mapreduce.JdMapper;
import com.ipin.jd.mapreduce.JdReducer;
import com.ipin.jd.mapreduce.TimelineMapper;
import com.ipin.jd.mapreduce.TimelineReducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

/**
 * Created by janze on 1/5/18.
 */
public class JdTimelineSort {

    /**
     * jd_timeline 排序，设置is_first is_last
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf, "jd-timeline-sort");
        job.setJarByClass(JdTimelineSort.class);


        job.setMapperClass(TimelineMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);


        job.setReducerClass(TimelineReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path((args[0])));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        FileOutputFormat.setCompressOutput(job, false);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
