package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

public class JdInc extends Base{

	/**
	 * 
	 */
	protected String incSegId;//裸公司id，为裸公司名+ipin.com的MD5值
	protected String incSegName;
	protected String incSegAliasName;
	protected String incSegCityId;//公司所在的城市id，为12个字符的国家行政区域编码
	protected String incSegCity;
	protected String incSegLocation;
	protected String incSegEmployee;
	protected Integer incSegEmployeeMin;
	protected Integer incSegEmployeeMax;
	protected Integer incSegTypeId;
	protected String incSegType;
	protected String incSegZipCodeRaw;//邮编
	protected String incSegZipCode;
	protected String incSegContactName;
	protected String incSegContactInfo;
	protected String incSegVestInst;//投资公司的机构名称解析值
	protected String incSegStage;//公司发展阶段解析值，如B轮融资等
	protected String incSegUrl;
	protected String incSegDesc;
	protected Long rownum;
	protected Integer randomInt;
	protected Date etlDate;

	public String getIncSegId() {
		return incSegId;
	}
	public void setIncSegId(String incSegId) {
		this.incSegId = incSegId;
	}
	public String getIncSegAliasName() {
		return incSegAliasName;
	}
	public void setIncSegAliasName(String incSegAliasName) {
		this.incSegAliasName = incSegAliasName;
	}
	public String getIncSegCityId() {
		return incSegCityId;
	}
	public void setIncSegCityId(String incSegCityId) {
		this.incSegCityId = incSegCityId;
	}
	public String getIncSegCity() {
		return incSegCity;
	}
	public void setIncSegCity(String incSegCity) {
		this.incSegCity = incSegCity;
	}
	public String getIncSegLocation() {
		return incSegLocation;
	}
	public void setIncSegLocation(String incSegLocation) {
		this.incSegLocation = incSegLocation;
	}
	public String getIncSegEmployee() {
		return incSegEmployee;
	}
	public void setIncSegEmployee(String incSegEmployee) {
		this.incSegEmployee = incSegEmployee;
	}
	public Integer getIncSegEmployeeMin() {
		return incSegEmployeeMin;
	}
	public void setIncSegEmployeeMin(Integer incSegEmployeeMin) {
		this.incSegEmployeeMin = incSegEmployeeMin;
	}
	public Integer getIncSegEmployeeMax() {
		return incSegEmployeeMax;
	}
	public void setIncSegEmployeeMax(Integer incSegEmployeeMax) {
		this.incSegEmployeeMax = incSegEmployeeMax;
	}
	public Integer getIncSegTypeId() {
		return incSegTypeId;
	}
	public void setIncSegTypeId(Integer incSegTypeId) {
		this.incSegTypeId = incSegTypeId;
	}
	public String getIncSegType() {
		return incSegType;
	}
	public void setIncSegType(String incSegType) {
		this.incSegType = incSegType;
	}
	public String getIncSegZipCodeRaw() {
		return incSegZipCodeRaw;
	}
	public void setIncSegZipCodeRaw(String incSegZipCodeRaw) {
		this.incSegZipCodeRaw = incSegZipCodeRaw;
	}
	public String getIncSegZipCode() {
		return incSegZipCode;
	}
	public void setIncSegZipCode(String incSegZipCode) {
		this.incSegZipCode = incSegZipCode;
	}
	public String getIncSegContactName() {
		return incSegContactName;
	}
	public void setIncSegContactName(String incSegContactName) {
		this.incSegContactName = incSegContactName;
	}
	public String getIncSegContactInfo() {
		return incSegContactInfo;
	}
	public void setIncSegContactInfo(String incSegContactInfo) {
		this.incSegContactInfo = incSegContactInfo;
	}
	public String getIncSegVestInst() {
		return incSegVestInst;
	}
	public void setIncSegVestInst(String incSegVestInst) {
		this.incSegVestInst = incSegVestInst;
	}
	public String getIncSegStage() {
		return incSegStage;
	}
	public void setIncSegStage(String incSegStage) {
		this.incSegStage = incSegStage;
	}
	public String getIncSegUrl() {
		return incSegUrl;
	}
	public void setIncSegUrl(String incSegUrl) {
		this.incSegUrl = incSegUrl;
	}
	public String getIncSegName() {
		return incSegName;
	}
	public void setIncSegName(String incSegName) {
		this.incSegName = incSegName;
	}
	public String getIncSegDesc() {
		return incSegDesc;
	}
	public void setIncSegDesc(String incSegDesc) {
		this.incSegDesc = incSegDesc;
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
		return "JdInc{" +
				"incSegId='" + incSegId + '\'' +
				", incSegName='" + incSegName + '\'' +
				", incSegAliasName='" + incSegAliasName + '\'' +
				", incSegCityId='" + incSegCityId + '\'' +
				", incSegCity='" + incSegCity + '\'' +
				", incSegLocation='" + incSegLocation + '\'' +
				", incSegEmployee='" + incSegEmployee + '\'' +
				", incSegEmployeeMin=" + incSegEmployeeMin +
				", incSegEmployeeMax=" + incSegEmployeeMax +
				", incSegTypeId=" + incSegTypeId +
				", incSegType='" + incSegType + '\'' +
				", incSegZipCodeRaw='" + incSegZipCodeRaw + '\'' +
				", incSegZipCode='" + incSegZipCode + '\'' +
				", incSegContactName='" + incSegContactName + '\'' +
				", incSegContactInfo='" + incSegContactInfo + '\'' +
				", incSegVestInst='" + incSegVestInst + '\'' +
				", incSegStage='" + incSegStage + '\'' +
				", incSegUrl='" + incSegUrl + '\'' +
				", incSegDesc='" + incSegDesc + '\'' +
				", rownum=" + rownum +
				", randomInt=" + randomInt +
				", etlDate=" + etlDate +
				'}';
	}

	public boolean legalInc(){
		if(StringUtils.isAnyBlank(this.incSegId, this.incSegName)){
			return false;
		}
		return true;
	}


}
