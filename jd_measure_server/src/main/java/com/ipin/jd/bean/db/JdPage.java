package com.ipin.jd.bean.db;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

public class JdPage extends Base{

	/**
	 * 
	 */
	protected String jdId;
	protected String jdSrcId;
	protected String contentSign;
	protected String pageContentPath;
	protected Long crawlerUpdateTime;
	protected Date crawlerUpdateDate;
	protected Date etlDate;
	public String getJdId() {
		return jdId;
	}
	public void setJdId(String jdId) {
		this.jdId = jdId;
	}
	public String getPageContentPath() {
		return pageContentPath;
	}
	public void setPageContentPath(String pageContentPath) {
		this.pageContentPath = pageContentPath;
	}
	public Long getCrawlerUpdateTime() {
		return crawlerUpdateTime;
	}
	public void setCrawlerUpdateTime(Long crawlerUpdateTime) {
		this.crawlerUpdateTime = crawlerUpdateTime;
	}
	public Date getEtlDate() {
		return etlDate;
	}
	public void setEtlDate(Date etlDate) {
		this.etlDate = etlDate;
	}

	public void setJdSrcId(String jdSrcId) {
		this.jdSrcId = jdSrcId;
	}

	public void setContentSign(String contentSign) {
		this.contentSign = contentSign;
	}

	public void setCrawlerUpdateDate(Date crawlerUpdateDate) {
		this.crawlerUpdateDate = crawlerUpdateDate;
	}

	public boolean isLegal(){
		if(StringUtils.isAnyBlank(this.jdSrcId, this.contentSign)){
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "JdPage{" +
				"jdId='" + jdId + '\'' +
				", jdSrcId='" + jdSrcId + '\'' +
				", contentSign='" + contentSign + '\'' +
				", pageContentPath='" + pageContentPath + '\'' +
				", crawlerUpdateTime=" + crawlerUpdateTime +
				", crawlerUpdateDate=" + crawlerUpdateDate +
				", etlDate=" + etlDate +
				'}';
	}

	public String getJdSrcId() {
		return jdSrcId;
	}

	public String getContentSign() {
		return contentSign;
	}

	public Date getCrawlerUpdateDate() {
		return crawlerUpdateDate;
	}
}
