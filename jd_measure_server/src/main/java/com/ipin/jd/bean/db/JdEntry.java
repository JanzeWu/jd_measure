package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

public class JdEntry extends Base{



	protected String jdId; //JD每次发布的历史记录生成一个jdId.jd的id，“jd_src_id+content_sign”的MD5值
	protected String entityId;
	protected String jdSrcId = "-"; //JdRaw中的jdId,JD来源id，和原始jd的URL相关
	protected String contentSign = "-";//验签值，标识JD内容是否变化
	protected Integer jdFromId = -1;//JD来源渠道id
	protected Long jdContentPubTimestamp;//JD版本表动的更新/发布日期，Unix Timestamp格式
	protected Date jdContentPubDate;//JD版本表动的更新/发布日期，日期格式
	protected String jdSrcUrl;
	protected String incSegId = "-";//裸公司id，为裸公司名+ipin.com的MD5值
	protected Integer status = 0;//状态：0-生效中,1-下架/过期,2-历史版本
	protected String src = "-";//系统来源
	protected Long rownum;
	protected Integer randomInt;
	protected Date etlDate;//ETL日期
	public String getJdId() {
		return jdId;
	}
	public void setJdId(String jdId) {
		this.jdId = jdId;
	}
	public String getJdSrcId() {
		return jdSrcId;
	}
	public void setJdSrcId(String jdSrcId) {
		this.jdSrcId = jdSrcId;
	}
	public Integer getJdFromId() {
		return jdFromId;
	}
	public void setJdFromId(Integer jdFromId) {
		this.jdFromId = jdFromId;
	}
	
	public String getContentSign() {
		return contentSign;
	}
	public void setContentSign(String contentSign) {
		this.contentSign = contentSign;
	}
	
	public Date getJdContentPubDate() {
		return jdContentPubDate;
	}
	public void setJdContentPubDate(Date jdContentPubDate) {
		this.jdContentPubDate = jdContentPubDate;
	}
	public Long getJdContentPubTimestamp() {
		return jdContentPubTimestamp;
	}
	public void setJdContentPubTimestamp(Long jdContentPubTimestamp) {
		this.jdContentPubTimestamp = jdContentPubTimestamp;
	}
	public String getIncSegId() {
		return incSegId;
	}
	public void setIncSegId(String incSegId) {
		this.incSegId = incSegId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public Date getEtlDate() {
		return etlDate;
	}
	public void setEtlDate(Date etlDate) {
		this.etlDate = etlDate;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setJdSrcUrl(String jdSrcUrl) {
		this.jdSrcUrl = jdSrcUrl;
	}

	public void setRownum(Long rownum) {
		this.rownum = rownum;
	}

	public void setRandomInt(Integer randomInt) {
		this.randomInt = randomInt;
	}

	public String getEntityId() {
		return entityId;
	}

	public String getJdSrcUrl() {
		return jdSrcUrl;
	}

	public Long getRownum() {
		return rownum;
	}

	public Integer getRandomInt() {
		return randomInt;
	}

	public boolean legalEntry(){
		if(StringUtils.isAnyBlank(this.jdSrcId, this.contentSign, this.incSegId)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "JdEntry{" +
				"jdId='" + jdId + '\'' +
				", entityId='" + entityId + '\'' +
				", jdSrcId='" + jdSrcId + '\'' +
				", contentSign='" + contentSign + '\'' +
				", jdFromId=" + jdFromId +
				", jdContentPubTimestamp=" + jdContentPubTimestamp +
				", jdContentPubDate=" + jdContentPubDate +
				", jdSrcUrl='" + jdSrcUrl + '\'' +
				", incSegId='" + incSegId + '\'' +
				", status=" + status +
				", src='" + src + '\'' +
				", rownum=" + rownum +
				", randomInt=" + randomInt +
				", etlDate=" + etlDate +
				'}';
	}
}
