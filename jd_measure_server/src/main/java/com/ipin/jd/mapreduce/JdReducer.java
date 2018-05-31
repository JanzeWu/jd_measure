package com.ipin.jd.mapreduce;

import com.ipin.jd.bean.db.*;
import com.ipin.jd.consts.JdTable;
import com.ipin.jd.helper.HiveJdbcClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

/**
 * Created by janze on 1/3/18.
 */
public class JdReducer extends Reducer<Object, Text, NullWritable, Text> {

    private Base base = new Base();
    private MultipleOutputs<NullWritable, Text> mos = null;
    private HiveJdbcClient hiveClient = null;
    private Date etlDate = new Date(new java.util.Date().getTime());

    private long entryRownum = 0L;
    private long incRownum = 0L;
    private long jobRownum = 0L;
    private long othersRownum = 0L;
    private long zhinengRownum = 0L;
    private long industryRownum = 0L;
    private long jobTypeRownum = 0L;
    private long workCityRownum = 0L;
    private long workLocationRownum = 0L;
    private long jobMajorRownum = 0L;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        mos = new MultipleOutputs<>(context);
        hiveClient = new HiveJdbcClient();

        Map<String, Long> loadMaxRownum = hiveClient.loadMaxRownum();

        entryRownum          = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_ENTRY   );
        incRownum            = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_INC   );
        jobRownum            = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_JOB   );
        othersRownum         = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_OTHERS   );
        zhinengRownum        = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JOB_ZHINENG  );
        industryRownum       = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_MEASURE_INC_INDUSTRY  );
        jobTypeRownum        = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_JOB_TYPE   );
        workCityRownum       = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_JOB_WORK_CITY   );
        workLocationRownum   = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_JOB_WORK_LOCATION  );
        jobMajorRownum       = this.getMaxRownum(loadMaxRownum,  JdTable.TABLE_JD_JOB_MAJOR  );

    }

    private long getMaxRownum(Map<String, Long> loadMaxRownum, String tableName){
        return loadMaxRownum.get(tableName) != null ? loadMaxRownum.get(tableName) : 0L;
    }

    @Override
    protected void reduce(Object key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String table = key.toString();
        for( Text value : values){
            String res = null;
            switch (table){
                case JdTable.TABLE_JD_ENTRY : res = handleJdEntry(value); break;
                case JdTable.TABLE_JD_INC : res = handleJdInc(value); break;
                case JdTable.TABLE_JD_JOB : res = handleJob(value);break;
                case JdTable.TABLE_JD_OTHERS : res = handleJdOthers(value); break;
                case JdTable.TABLE_JOB_ZHINENG : res = handleZhineng(value);break;
                case JdTable.TABLE_MEASURE_INC_INDUSTRY : res = handleIndustry(value);break;
                case JdTable.TABLE_JD_JOB_TYPE : res = handleJobType(value);break;
                case JdTable.TABLE_JD_JOB_MAJOR : res = handleMajor(value); break;
                case JdTable.TABLE_JD_JOB_WORK_CITY : res = handleWorkCity(value); break;
                case JdTable.TABLE_JD_JOB_WORK_LOCATION : res = handleWorkLocation(value); break;
                case JdTable.TABLE_JD_TIMELINE : res = handleTimeline(value); break;
                case JdTable.TABLE_JD_PAGE : res = handleJdPage(value); break;

            }
            if(StringUtils.isNotBlank(res))
                mos.write(NullWritable.get(), new Text(res), generatePath(table));
        }
    }

    private String handleJdPage(Text value){
        JdPage page = base.getObjectFromHiveStringTemplate(value.toString(), JdPage.class);
        page.setEtlDate(this.etlDate);
        return page.getHiveStringTemplate();
    }
    private String handleTimeline(Text value){
        JdTimeline timeline = base.getObjectFromHiveStringTemplate(value.toString(), JdTimeline.class);
        timeline.setEtlDate(this.etlDate);
        return timeline.getHiveStringTemplate();
    }

    private String handleWorkLocation(Text value) {

        JdJobWorkLocation workLocation = base.getObjectFromHiveStringTemplate(value.toString(), JdJobWorkLocation.class);
        workLocation.setRownum(++workLocationRownum);
        workLocation.setEtlDate(this.etlDate);
        return workLocation.getHiveStringTemplate();
    }

    private String handleWorkCity(Text value) {
        JdJobWorkCity workCity = base.getObjectFromHiveStringTemplate(value.toString(), JdJobWorkCity.class);
        workCity.setRownum(++workCityRownum);
        workCity.setEtlDate(this.etlDate);
        return workCity.getHiveStringTemplate();
    }

    private String handleMajor(Text value) {
        JdJobMajor major = base.getObjectFromHiveStringTemplate(value.toString(), JdJobMajor.class);
        major.setRownum(++jobMajorRownum);
        major.setEtlDate(this.etlDate);
        return major.getHiveStringTemplate();
    }

    private String handleJobType(Text value) {
        JdJobType jobType = base.getObjectFromHiveStringTemplate(value.toString(), JdJobType.class);
        jobType.setRownum(++jobTypeRownum);
        jobType.setEtlDate(this.etlDate);
        return jobType.getHiveStringTemplate();
    }

    private String handleIndustry(Text value) {

        MeasureIncIndustry industry = base.getObjectFromHiveStringTemplate(value.toString(), MeasureIncIndustry.class);
        industry.setRownum(++industryRownum);
        industry.setStartDate(this.etlDate);
        return industry.getHiveStringTemplate();
    }

    private String handleZhineng(Text value) {

        JobZhineng zhineng = base.getObjectFromHiveStringTemplate(value.toString(), JobZhineng.class);
        zhineng.setRownum(++zhinengRownum);
        zhineng.setStartDate(this.etlDate);
        return zhineng.getHiveStringTemplate();
    }

    private String handleJdOthers(Text value) {

        JdOthers others = base.getObjectFromHiveStringTemplate(value.toString(), JdOthers.class);
        others.setRownum( ++othersRownum );
        others.setEtlDate(etlDate);
        return others.getHiveStringTemplate();
    }

    private String handleJob(Text value) {

        JdJob job = base.getObjectFromHiveStringTemplate(value.toString(), JdJob.class);
        job.setRownum( ++jobRownum );
        job.setEtlDate(this.etlDate);
        return job.getHiveStringTemplate();
    }

    private String handleJdInc(Text value) {

        JdInc inc = base.getObjectFromHiveStringTemplate(value.toString(), JdInc.class);
        inc.setRownum(++incRownum);
        inc.setEtlDate(this.etlDate);
        return inc.getHiveStringTemplate();
    }

    private String handleJdEntry(Text value) {

        JdEntry entry = base.getObjectFromHiveStringTemplate(value.toString(), JdEntry.class);
        entry.setRownum(++entryRownum);
        entry.setEtlDate(etlDate);
        return entry.getHiveStringTemplate();
    }

    private String generatePath(String dirName) {
        return dirName + "/part";
    }


    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
}
