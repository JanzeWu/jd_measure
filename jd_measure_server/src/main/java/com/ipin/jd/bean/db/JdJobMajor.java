package com.ipin.jd.bean.db;

import org.apache.commons.lang.StringUtils;

import java.sql.Date;

/**
 * Created by janze on 12/27/17.
 */
public class JdJobMajor extends Base {

    protected String jdId;

    protected String jobMajor;
    protected String jobMajorId;
    protected String diploma;
    protected Integer diplomaId;
    protected Long rownum;
    protected Integer randomInt;
    protected Date etlDate;

    public void setJdId(String jdId) {
        this.jdId = jdId;
    }

    public void setJobMajor(String jobMajor) {
        this.jobMajor = jobMajor;
    }

    public void setJobMajorId(String jobMajorId) {
        this.jobMajorId = jobMajorId;
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

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public void setDiplomaId(Integer diplomaId) {
        this.diplomaId = diplomaId;
    }

    public boolean isLegal(){
        if(StringUtils.isNotBlank(this.jobMajor)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "JdJobMajor{" +
                "jdId='" + jdId + '\'' +
                ", jobMajor='" + jobMajor + '\'' +
                ", jobMajorId='" + jobMajorId + '\'' +
                ", diploma='" + diploma + '\'' +
                ", diplomaId=" + diplomaId +
                ", rownum=" + rownum +
                ", randomInt=" + randomInt +
                ", etlDate=" + etlDate +
                '}';
    }

    public String getJdId() {
        return jdId;
    }

    public String getJobMajor() {
        return jobMajor;
    }

    public String getJobMajorId() {
        return jobMajorId;
    }

    public String getDiploma() {
        return diploma;
    }

    public Integer getDiplomaId() {
        return diplomaId;
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
