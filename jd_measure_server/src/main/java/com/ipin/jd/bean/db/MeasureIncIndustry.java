package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

public class MeasureIncIndustry extends Base{

	/**
	 * 
	 */
	protected String jdId;//jd的id，“jd_src_id+content_sign”的MD5值
	protected String incSegIndustry;//公司行业解析值
	protected Integer incSegIndustryId;//公司行业id，量化值
	protected String src;

	protected Long rownum;
	protected Integer randomInt;

	protected Date startDate;//量化时间
	protected Date endDate;//失效时间

	public String getJdId() {
		return jdId;
	}
	public void setJdId(String jdId) {
		this.jdId = jdId;
	}
	public Integer getIncSegIndustryId() {
		return incSegIndustryId;
	}
	public void setIncSegIndustryId(Integer incSegIndustryId) {
		this.incSegIndustryId = incSegIndustryId;
	}
	public String getIncSegIndustry() {
		return incSegIndustry;
	}
	public void setIncSegIndustry(String incSegIndustry) {
		this.incSegIndustry = incSegIndustry;
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

	public void setRownum(Long rownum) {
		this.rownum = rownum;
	}

	public void setRandomInt(Integer randomInt) {
		this.randomInt = randomInt;
	}

	public boolean isLegal(){
		if(StringUtils.isNotBlank(incSegIndustry)){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "MeasureIncIndustry{" +
				"jdId='" + jdId + '\'' +
				", incSegIndustry='" + incSegIndustry + '\'' +
				", incSegIndustryId=" + incSegIndustryId +
				", src='" + src + '\'' +
				", rownum=" + rownum +
				", randomInt=" + randomInt +
				", startDate=" + startDate +
				", endDate=" + endDate +
				'}';
	}
}
