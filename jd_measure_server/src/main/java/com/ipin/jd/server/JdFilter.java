package com.ipin.jd.server;

import com.ipin.jd.mapreduce.JdFilterMapper;
import com.ipin.jd.mapreduce.JdFilterReducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * Created by janze on 1/11/18.
 */
public class JdFilter {

    /**
     * 过滤edw_jd.jd_entry 中已经存在的JD，不再量化入库
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf, "jd-filter");
        job.setJarByClass(JdFilter.class);


        job.setMapperClass(JdFilterMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(JdFilterReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path((args[0])));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        FileOutputFormat.setCompressOutput(job, false);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
