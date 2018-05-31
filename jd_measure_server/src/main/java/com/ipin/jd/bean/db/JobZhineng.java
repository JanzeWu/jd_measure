package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

public class JobZhineng extends Base {

	/**
	 * 
	 */
	protected String JdId;
	protected String jobPosition;
	protected Integer zhinengId;
	protected String zhinengName;
	protected String zhinengVersion;
	protected String src;
	protected Long rownum;
	protected Integer randomInt;
	protected Date startDate;//量化时间
	protected Date endDate;//初始化为空
	public String getJdId() {
		return JdId;
	}
	public void setJdId(String jdId) {
		JdId = jdId;
	}
	public String getJobPosition() {
		return jobPosition;
	}
	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}
	public String getZhinengName() {
		return zhinengName;
	}
	public void setZhinengName(String zhinengName) {
		this.zhinengName = zhinengName;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setZhinengId(Integer zhinengId) {
		this.zhinengId = zhinengId;
	}

	public void setZhinengVersion(String zhinengVersion) {
		this.zhinengVersion = zhinengVersion;
	}

	public void setRownum(Long rownum) {
		this.rownum = rownum;
	}

	public void setRandomInt(Integer randomInt) {
		this.randomInt = randomInt;
	}

	public boolean isLegal(){
		if(StringUtils.isAnyBlank(jobPosition, zhinengName)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "JobZhineng{" +
				"JdId='" + JdId + '\'' +
				", jobPosition='" + jobPosition + '\'' +
				", zhinengId=" + zhinengId +
				", zhinengName='" + zhinengName + '\'' +
				", zhinengVersion='" + zhinengVersion + '\'' +
				", src='" + src + '\'' +
				", rownum=" + rownum +
				", randomInt=" + randomInt +
				", startDate=" + startDate +
				", endDate=" + endDate +
				'}';
	}

	public Integer getZhinengId() {
		return zhinengId;
	}

	public String getZhinengVersion() {
		return zhinengVersion;
	}

	public Long getRownum() {
		return rownum;
	}

	public Integer getRandomInt() {
		return randomInt;
	}
}
