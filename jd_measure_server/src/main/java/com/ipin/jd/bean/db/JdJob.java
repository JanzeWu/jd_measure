package com.ipin.jd.bean.db;

import com.ipin.mes.util.ContentCheckUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.List;

public class JdJob extends Base{

	/**
	 * 
	 */
	protected String jdId;//jd的id，“jd_src_id+content_sign”的MD5值
	protected String jobPosition;
	protected String jobCate;//工作职能/类别解析值
	protected String jobSalary;
	protected Double jobSalaryMin;
	protected Double jobSalaryMax;
	protected String jobWorkAge;
	protected Integer jobWorkAgeMin;//单位是天数
	protected Integer jobWorkAgeMax;//单位是天数,不限工作经验则用0表示
	protected String jobDiploma;
	protected Integer jobDiplomaId;

	protected String jobPositionCount;//职位招聘人数解析值
	protected Integer jobPositionMin;//职位招聘人数量化值
	protected Integer jobPositionMax;//
	protected List<String> jobWelfare;//职位的福利解析值
	protected String jobAge;
	protected Integer jobAgeNumMin;	
	protected Integer jobAgeNumMax;//年龄要求上限量化值，单位为岁，0表示不限年龄
	protected String jobGenderRaw;
	protected String jobGender;//职位性别要求，M表示男，F表示女，'-'表示不限
	protected String jobEndTime;
	protected Date jobEndTimeDate;
	protected List<String> skillList;
	protected List<String> certList;
	protected List<String> jobLanguage;
	protected String requireOverseaExp = "N";//是否要求海外工作经历，Y/N
	protected String isUrgent = "N";//是否急招，Y/N
	protected String cvRecvRaw;//接受简历方式解析值
	protected String cvRecvEmail;
	protected String jobDepartment;//工作所在部门解析值
	protected String jobLabel;//职位标签
	protected String jobPositionDesc;//职位描述文本
	protected String workDemand;//工作要求
	protected String workDuty;//工作职责
	protected Long rownum;
	protected Integer randomInt;
	protected Date etlDate;
	
	public String getJdId() {
		return jdId;
	}
	public void setJdId(String jdId) {
		this.jdId = jdId;
	}
	public String getJobPosition() {
		return jobPosition;
	}
	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}
	public String getJobCate() {
		return jobCate;
	}
	public void setJobCate(String jobCate) {
		this.jobCate = jobCate;
	}
	public String getJobSalary() {
		return jobSalary;
	}
	public void setJobSalary(String jobSalary) {
		this.jobSalary = jobSalary;
	}
	public Double getJobSalaryMin() {
		return jobSalaryMin;
	}
	public void setJobSalaryMin(Double jobSalaryMin) {
		this.jobSalaryMin = jobSalaryMin;
	}
	public Double getJobSalaryMax() {
		return jobSalaryMax;
	}
	public void setJobSalaryMax(Double jobSalaryMax) {
		this.jobSalaryMax = jobSalaryMax;
	}
	public String getJobWorkAge() {
		return jobWorkAge;
	}
	public void setJobWorkAge(String jobWorkAge) {
		this.jobWorkAge = jobWorkAge;
	}
	public Integer getJobWorkAgeMin() {
		return jobWorkAgeMin;
	}
	public void setJobWorkAgeMin(Integer jobWorkAgeMin) {
		this.jobWorkAgeMin = jobWorkAgeMin;
	}
	public Integer getJobWorkAgeMax() {
		return jobWorkAgeMax;
	}
	public void setJobWorkAgeMax(Integer jobWorkAgeMax) {
		this.jobWorkAgeMax = jobWorkAgeMax;
	}
	public String getJobDiploma() {
		return jobDiploma;
	}
	public void setJobDiploma(String jobDiploma) {
		this.jobDiploma = jobDiploma;
	}
	public Integer getJobDiplomaId() {
		return jobDiplomaId;
	}
	public void setJobDiplomaId(Integer jobDiplomaId) {
		this.jobDiplomaId = jobDiplomaId;
	}
	
	
	public String getJobPositionCount() {
		return jobPositionCount;
	}
	public void setJobPositionCount(String jobPositionCount) {
		this.jobPositionCount = jobPositionCount;
	}
	public Integer getJobPositionMin() {
		return jobPositionMin;
	}
	public void setJobPositionMin(Integer jobPositionMin) {
		this.jobPositionMin = jobPositionMin;
	}
	public Integer getJobPositionMax() {
		return jobPositionMax;
	}
	public void setJobPositionMax(Integer jobPositionMax) {
		this.jobPositionMax = jobPositionMax;
	}
	public List<String> getJobWelfare() {
		return jobWelfare;
	}
	public void setJobWelfare(List<String> jobWelfare) {
		this.jobWelfare = jobWelfare;
	}
	public String getJobAge() {
		return jobAge;
	}
	public void setJobAge(String jobAge) {
		this.jobAge = jobAge;
	}
	public Integer getJobAgeNumMin() {
		return jobAgeNumMin;
	}
	public void setJobAgeNumMin(Integer jobAgeNumMin) {
		this.jobAgeNumMin = jobAgeNumMin;
	}
	public Integer getJobAgeNumMax() {
		return jobAgeNumMax;
	}
	public void setJobAgeNumMax(Integer jobAgeNumMax) {
		this.jobAgeNumMax = jobAgeNumMax;
	}
	public String getJobGenderRaw() {
		return jobGenderRaw;
	}
	public void setJobGenderRaw(String jobGenderRaw) {
		this.jobGenderRaw = jobGenderRaw;
	}
	public String getJobGender() {
		return jobGender;
	}
	public void setJobGender(String jobGender) {
		this.jobGender = jobGender;
	}
	public String getJobEndTime() {
		return jobEndTime;
	}
	public void setJobEndTime(String jobEndTime) {
		this.jobEndTime = jobEndTime;
	}
	public Date getJobEndTimeDate() {
		return jobEndTimeDate;
	}
	public void setJobEndTimeDate(Date jobEndTimeDate) {
		this.jobEndTimeDate = jobEndTimeDate;
	}
	public List<String> getSkillList() {
		return skillList;
	}
	public void setSkillList(List<String> skillList) {
		this.skillList = skillList;
	}
	public List<String> getCertList() {
		return certList;
	}
	public void setCertList(List<String> certList) {
		this.certList = certList;
	}
	public List<String> getJobLanguage() {
		return jobLanguage;
	}
	public void setJobLanguage(List<String> jobLanguage) {
		this.jobLanguage = jobLanguage;
	}
	public String getRequireOverseaExp() {
		return requireOverseaExp;
	}
	public void setRequireOverseaExp(String requireOverseaExp) {
		this.requireOverseaExp = requireOverseaExp;
	}
	public String getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}
	public String getCvRecvRaw() {
		return cvRecvRaw;
	}
	public void setCvRecvRaw(String cvRecvRaw) {
		this.cvRecvRaw = cvRecvRaw;
	}
	public String getCvRecvEmail() {
		return cvRecvEmail;
	}
	public void setCvRecvEmail(String cvRecvEmail) {
		this.cvRecvEmail = cvRecvEmail;
	}
	public String getJobDepartment() {
		return jobDepartment;
	}
	public void setJobDepartment(String jobDepartment) {
		this.jobDepartment = jobDepartment;
	}
	public String getJobLabel() {
		return jobLabel;
	}
	public void setJobLabel(String jobLabel) {
		this.jobLabel = jobLabel;
	}
	public String getJobPositionDesc() {
		return jobPositionDesc;
	}
	public void setJobPositionDesc(String jobPositionDesc) {
		this.jobPositionDesc = jobPositionDesc;
	}
	public String getWorkDemand() {
		return workDemand;
	}
	public void setWorkDemand(String workDemand) {
		this.workDemand = workDemand;
	}
	public String getWorkDuty() {
		return workDuty;
	}
	public void setWorkDuty(String workDuty) {
		this.workDuty = workDuty;
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

	public boolean legalJob(){
		if(StringUtils.isAnyBlank(this.jobPosition)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "JdJob{" +
				"jdId='" + jdId + '\'' +
				", jobPosition='" + jobPosition + '\'' +
				", jobCate='" + jobCate + '\'' +
				", jobSalary='" + jobSalary + '\'' +
				", jobSalaryMin=" + jobSalaryMin +
				", jobSalaryMax=" + jobSalaryMax +
				", jobWorkAge='" + jobWorkAge + '\'' +
				", jobWorkAgeMin=" + jobWorkAgeMin +
				", jobWorkAgeMax=" + jobWorkAgeMax +
				", jobDiploma='" + jobDiploma + '\'' +
				", jobDiplomaId=" + jobDiplomaId +
				", jobPositionCount='" + jobPositionCount + '\'' +
				", jobPositionMin=" + jobPositionMin +
				", jobPositionMax=" + jobPositionMax +
				", jobWelfare=" + jobWelfare +
				", jobAge='" + jobAge + '\'' +
				", jobAgeNumMin=" + jobAgeNumMin +
				", jobAgeNumMax=" + jobAgeNumMax +
				", jobGenderRaw='" + jobGenderRaw + '\'' +
				", jobGender='" + jobGender + '\'' +
				", jobEndTime='" + jobEndTime + '\'' +
				", jobEndTimeDate=" + jobEndTimeDate +
				", skillList=" + skillList +
				", certList=" + certList +
				", jobLanguage=" + jobLanguage +
				", requireOverseaExp='" + requireOverseaExp + '\'' +
				", isUrgent='" + isUrgent + '\'' +
				", cvRecvRaw='" + cvRecvRaw + '\'' +
				", cvRecvEmail='" + cvRecvEmail + '\'' +
				", jobDepartment='" + jobDepartment + '\'' +
				", jobLabel='" + jobLabel + '\'' +
				", jobPositionDesc='" + jobPositionDesc + '\'' +
				", workDemand='" + workDemand + '\'' +
				", workDuty='" + workDuty + '\'' +
				", rownum=" + rownum +
				", randomInt=" + randomInt +
				", etlDate=" + etlDate +
				'}';
	}

	public static void main(String[] args) {
		System.out.println(ContentCheckUtil.getMD5("123" + ""));
	}
}
