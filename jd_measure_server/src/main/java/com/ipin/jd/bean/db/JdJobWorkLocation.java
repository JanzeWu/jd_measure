package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

/**
 * Created by janze on 12/27/17.
 */
public class JdJobWorkLocation extends Base {

    protected String jdId;
    protected String jobWorkLocation;
    protected String jobLocationProvinceId;
    protected String jobLocationCityId;
    protected String jobLocationRegionId;
    protected Long rownum;
    protected Integer randomInt;
    protected Date etlDate;

    public void setJdId(String jdId) {
        this.jdId = jdId;
    }

    public void setJobWorkLocation(String jobWorkLocation) {
        this.jobWorkLocation = jobWorkLocation;
    }

    public void setJobLocationProvinceId(String jobLocationProvinceId) {
        this.jobLocationProvinceId = jobLocationProvinceId;
    }

    public void setJobLocationCityId(String jobLocationCityId) {
        this.jobLocationCityId = jobLocationCityId;
    }

    public void setJobLocationRegionId(String jobLocationRegionId) {
        this.jobLocationRegionId = jobLocationRegionId;
    }

    public void setRownum(Long rownum) {
        this.rownum = rownum;
    }

    public void setRandomInt(Integer randomInt) {
        this.randomInt = randomInt;
    }

    public void setEtlDate(Date etlDate) {
        this.etlDate = etlDate;
    }

    public boolean isLegal(){
        if(StringUtils.isNotBlank(this.jobWorkLocation)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "JdJobWorkLocation{" +
                "jdId='" + jdId + '\'' +
                ", jobWorkLocation='" + jobWorkLocation + '\'' +
                ", jobLocationProvinceId='" + jobLocationProvinceId + '\'' +
                ", jobLocationCityId='" + jobLocationCityId + '\'' +
                ", jobLocationRegionId='" + jobLocationRegionId + '\'' +
                ", rownum=" + rownum +
                ", randomInt=" + randomInt +
                ", etlDate=" + etlDate +
                '}';
    }

    public String getJdId() {
        return jdId;
    }

    public String getJobWorkLocation() {
        return jobWorkLocation;
    }

    public String getJobLocationProvinceId() {
        return jobLocationProvinceId;
    }

    public String getJobLocationCityId() {
        return jobLocationCityId;
    }

    public String getJobLocationRegionId() {
        return jobLocationRegionId;
    }

    public Long getRownum() {
        return rownum;
    }

    public Integer getRandomInt() {
        return randomInt;
    }

    public Date getEtlDate() {
        return etlDate;
    }
}
