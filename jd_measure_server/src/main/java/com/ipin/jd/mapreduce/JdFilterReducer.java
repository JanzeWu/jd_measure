package com.ipin.jd.mapreduce;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by janze on 1/12/18.
 */
public class JdFilterReducer extends Reducer<Object, Text, NullWritable, Text> {

    @Override
    protected void reduce(Object key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for(Text value : values){
            context.write(NullWritable.get(), value);
        }
    }
}
