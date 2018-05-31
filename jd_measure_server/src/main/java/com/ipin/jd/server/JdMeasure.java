package com.ipin.jd.server;

import com.ipin.jd.mapreduce.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

/**
 * Created by janze on 12/21/17.
 */
public class JdMeasure {

    /**
     * 量化JD mapper
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf, "jd-measure");
        job.setJarByClass(JdMeasure.class);


        job.setMapperClass(JdMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);


        job.setNumReduceTasks(12);
        job.setReducerClass(JdReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path((args[0])));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        FileOutputFormat.setCompressOutput(job, false);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
