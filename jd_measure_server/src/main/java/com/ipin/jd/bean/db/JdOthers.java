package com.ipin.jd.bean.db;

import com.ipin.mes.util.ContentCheckUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.List;

public class JdOthers extends Base{

	/**
	 * 
	 */
	protected String jdId;
	protected List<String> keyWords;
	protected String posType;//企业职位/猎头职位
	protected String isFulltime;//是否全日制统招
	protected String salaryCombine;//薪资构成
	protected String holidayWelfare;
	protected String trafficWelfare;
	protected String jobSubSize;//下属人数
	protected String socialWelfare;
	protected String jdRemedy;
	protected String livingWelfare;
	protected String jobReport;//汇报对象
	protected String jobReportDetail;
	protected Long rownum;
	protected Integer randomInt;
	protected Date etlDate;
	public String getJdId() {
		return jdId;
	}
	public void setJdId(String jdId) {
		this.jdId = jdId;
	}
	public List<String> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}
	public String getPosType() {
		return posType;
	}
	public void setPosType(String posType) {
		this.posType = posType;
	}
	public String getIsFulltime() {
		return isFulltime;
	}
	public void setIsFulltime(String isFulltime) {
		this.isFulltime = isFulltime;
	}
	public String getSalaryCombine() {
		return salaryCombine;
	}
	public void setSalaryCombine(String salaryCombine) {
		this.salaryCombine = salaryCombine;
	}
	public String getHolidayWelfare() {
		return holidayWelfare;
	}
	public void setHolidayWelfare(String holidayWelfare) {
		this.holidayWelfare = holidayWelfare;
	}
	public String getTrafficWelfare() {
		return trafficWelfare;
	}
	public void setTrafficWelfare(String trafficWelfare) {
		this.trafficWelfare = trafficWelfare;
	}
	public String getJobSubSize() {
		return jobSubSize;
	}
	public void setJobSubSize(String jobSubSize) {
		this.jobSubSize = jobSubSize;
	}
	public String getSocialWelfare() {
		return socialWelfare;
	}
	public void setSocialWelfare(String socialWelfare) {
		this.socialWelfare = socialWelfare;
	}
	public String getJdRemedy() {
		return jdRemedy;
	}
	public void setJdRemedy(String jdRemedy) {
		this.jdRemedy = jdRemedy;
	}
	public String getLivingWelfare() {
		return livingWelfare;
	}
	public void setLivingWelfare(String livingWelfare) {
		this.livingWelfare = livingWelfare;
	}
	public String getJobReport() {
		return jobReport;
	}
	public void setJobReport(String jobReport) {
		this.jobReport = jobReport;
	}
	public String getJobReportDetail() {
		return jobReportDetail;
	}
	public void setJobReportDetail(String jobReportDetail) {
		this.jobReportDetail = jobReportDetail;
	}
	public Date getEtlDate() {
		return etlDate;
	}
	public void setEtlDate(Date etlDate) {
		this.etlDate = etlDate;
	}

	public void setRownum(Long rownum) {
		this.rownum = rownum;
	}

	public void setRandomInt(Integer randomInt) {
		this.randomInt = randomInt;
	}

	@Override
	public String toString() {
		return "JdOthers{" +
				"jdId='" + jdId + '\'' +
				", keyWords=" + keyWords +
				", posType='" + posType + '\'' +
				", isFulltime='" + isFulltime + '\'' +
				", salaryCombine='" + salaryCombine + '\'' +
				", holidayWelfare='" + holidayWelfare + '\'' +
				", trafficWelfare='" + trafficWelfare + '\'' +
				", jobSubSize='" + jobSubSize + '\'' +
				", socialWelfare='" + socialWelfare + '\'' +
				", jdRemedy='" + jdRemedy + '\'' +
				", livingWelfare='" + livingWelfare + '\'' +
				", jobReport='" + jobReport + '\'' +
				", jobReportDetail='" + jobReportDetail + '\'' +
				", rownum=" + rownum +
				", randomInt=" + randomInt +
				", etlDate=" + etlDate +
				'}';
	}

	public boolean isLegal(){
		if(ContentCheckUtil.strAllBlank(this.posType, this.isFulltime, this.salaryCombine, this.holidayWelfare, this.trafficWelfare,
				this.jobSubSize, this.socialWelfare, this.jobSubSize, this.socialWelfare, this.jdRemedy, this.livingWelfare, this.jobReport,
				this.jobReportDetail) && (this.keyWords == null || this.keyWords.isEmpty())){
			return false;
		}
		return true;
	}
}
