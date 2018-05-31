package com.ipin.jd.bean.db;

import org.apache.commons.lang.StringUtils;

import java.sql.Date;

/**
 * Created by janze on 12/27/17.
 */
public class JdJobType extends Base {

    protected String jdId;
    protected String jobType;
    protected Integer jobTypeId;
    protected Long rownum;
    protected Integer randomInt;
    protected Date etlDate;

    public void setJdId(String jdId) {
        this.jdId = jdId;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setJobTypeId(Integer jobTypeId) {
        this.jobTypeId = jobTypeId;
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

    public boolean legalJobType(){
        if(StringUtils.isNotBlank(this.jobType)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "JdJobType{" +
                "jdId='" + jdId + '\'' +
                ", jobType='" + jobType + '\'' +
                ", jobTypeId=" + jobTypeId +
                ", rownum=" + rownum +
                ", randomInt=" + randomInt +
                ", etlDate=" + etlDate +
                '}';
    }

    public String getJdId() {
        return jdId;
    }

    public String getJobType() {
        return jobType;
    }

    public Integer getJobTypeId() {
        return jobTypeId;
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
