package com.ipin.jd.server;

import com.ipin.jd.mapreduce.MajorMesMapper;
import com.ipin.jd.mapreduce.TimelineMapper;
import com.ipin.jd.mapreduce.TimelineReducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by janze on 1/24/18.
 */
public class MajorMes {

    /**
     * 可以将专业去重后使用该Mapper单独量化
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf, "major-mes");
        job.setJarByClass(MajorMes.class);

        job.setMapperClass(MajorMesMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path((args[0])));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        FileOutputFormat.setCompressOutput(job, false);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
