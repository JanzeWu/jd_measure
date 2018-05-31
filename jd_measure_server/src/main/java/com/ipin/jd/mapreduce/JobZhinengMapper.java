package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.Base;
import com.ipin.jd.bean.db.JobZhineng;
import com.ipin.jd.bean.mes.ZhinengMes;
import com.ipin.jd.service.JdConversion;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by janze on 1/24/18.
 */
public class JobZhinengMapper extends Mapper<Object, Text, NullWritable, Text> {


    private List<String> jdIds ;
    private List<Map<String, String>> params;
    private Date startDate ;
    private Text sample;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        jdIds = new ArrayList<>();
        params = new ArrayList<>();
        try {
            startDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-24").getTime());
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


        this.sample = value;
        DBObject jdObj = (DBObject) JSON.parse(value.toString());
        DBObject indexInfo = (DBObject) jdObj.get("indexInfo");
        // 如果entityId为空，取值indexUrl
        if(StringUtils.isBlank((String)indexInfo.get("entityId"))){
            indexInfo.put("entityId", (String)indexInfo.get("indexUrl"));
        }
        String entityId = (String) indexInfo.get("entityId");
        String contentSign =  (String) indexInfo.get("contentSign");
        // 计算jdId
        if(StringUtils.isBlank(entityId) && StringUtils.isBlank(contentSign)){
            return;
        }
        String jdId = Base.generateJdId(entityId, contentSign);

        DBObject jdJob = (DBObject)jdObj.get("jdJob");
        String jobDesc = (String) jdJob.get("jobDesc");
        String jobPos = (String) jdJob.get("jobPosition");
        if(StringUtils.isBlank(jobDesc) && StringUtils.isBlank(jobPos)){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("jobPos", jobPos);
        map.put("jobDesc", jobDesc);
        params.add(map);
        jdIds.add(jdId);

        if(params.size() >= 128){
            this.measure(context, value);
            params.clear();
            jdIds.clear();
        }
    }

    private void measure(Context context, Text value) throws IOException, InterruptedException {
        JdConversion jdConversion = new JdConversion(value.toString(), null);
        List<ZhinengMes> list = jdConversion.measureZhinengs(params);
        if(list != null && !list.isEmpty()){
            for(ZhinengMes zhinengMes : list){
                JobZhineng jobZhineng = new JobZhineng();
                jobZhineng.setJdId(jdIds.get(list.indexOf(zhinengMes)));
                jobZhineng.setZhinengName(zhinengMes.getZhinengName());
                jobZhineng.setZhinengId(zhinengMes.getZhinengId());
                jobZhineng.setZhinengVersion("v3");
                jobZhineng.setJobPosition(params.get(list.indexOf(zhinengMes)).get("jobPos"));
                jobZhineng.setStartDate(startDate);
                context.write(NullWritable.get(), new Text(jobZhineng.getHiveStringTemplate()));
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        if(this.params.size() > 0){
            measure(context, this.sample);
        }
    }
}
