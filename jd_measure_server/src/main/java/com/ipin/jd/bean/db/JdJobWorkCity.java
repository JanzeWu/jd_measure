package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

/**
 * Created by janze on 12/27/17.
 */
public class JdJobWorkCity extends Base {

    protected String jdId;
    protected String jobWorkCity;
    protected String jobWorkCityId;
    protected Long rownum;
    protected Integer randomInt;
    protected Date etlDate;

    public void setJdId(String jdId) {
        this.jdId = jdId;
    }

    public void setJobWorkCity(String jobWorkCity) {
        this.jobWorkCity = jobWorkCity;
    }

    public void setJobWorkCityId(String jobWorkCityId) {
        this.jobWorkCityId = jobWorkCityId;
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

    public boolean legalJdJobWorkCity(){
        if(StringUtils.isNotBlank(this.jobWorkCity)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "JdJobWorkCity{" +
                "jdId='" + jdId + '\'' +
                ", jobWorkCity='" + jobWorkCity + '\'' +
                ", jobWorkCityId='" + jobWorkCityId + '\'' +
                ", rownum=" + rownum +
                ", randomInt=" + randomInt +
                ", etlDate=" + etlDate +
                '}';
    }

    public String getJdId() {
        return jdId;
    }

    public String getJobWorkCity() {
        return jobWorkCity;
    }

    public String getJobWorkCityId() {
        return jobWorkCityId;
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
