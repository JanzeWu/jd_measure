package com.ipin.jd.mapreduce;

import com.ipin.jd.consts.Regulation;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by janze on 1/11/18.
 * 将已经存在hive表中的JD过滤掉
 */
public class JdFilterMapper extends Mapper<Object, Text, Text, Text> {

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String jd = value.toString().replaceAll(Regulation.HIVE_FIELD_SPLIT_STR, "");

        DBObject jdObj = (DBObject) JSON.parse(jd);

        DBObject indexInfo = (DBObject) jdObj.get("indexInfo");
        String contentSign = (String) indexInfo.get("contentSign");
        String entityId = (String) indexInfo.get("entityId");
        if(StringUtils.isBlank(entityId)){
            entityId = (String) indexInfo.get("indexUrl");
        }
        StringBuffer buf = new StringBuffer();
        buf.append(entityId)
                .append(Regulation.HIVE_FIELD_SPLIT_CHAR)
                .append(contentSign)
                .append(Regulation.HIVE_FIELD_SPLIT_CHAR)
                .append(jd);

        context.write(new Text(entityId), new Text(buf.toString()));
    }
}
