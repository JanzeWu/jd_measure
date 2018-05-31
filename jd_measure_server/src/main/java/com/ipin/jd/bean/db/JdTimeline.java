package com.ipin.jd.bean.db;

import java.sql.Date;

public class JdTimeline extends Base{

	/**
	 * 
	 */
	protected String jdSrcId;//JD来源id，和原始jd的URL相关
	protected Long jdUpdateTimestamp;//JD更新/发布时间，Unix Timestamp格式
	protected Date jdUpdateDate;//JD的更新/发布日期，日期格式
	protected String isFirst = "N";//是否第一次发布:Y/N
	protected String isLast = "N";//是否最后一次发布:Y/N
	protected Date etlDate;//ETL日期
	public String getJdSrcId() {
		return jdSrcId;
	}
	public void setJdSrcId(String jdSrcId) {
		this.jdSrcId = jdSrcId;
	}
	public Date getJdUpdateDate() {
		return jdUpdateDate;
	}
	public void setJdUpdateDate(Date jdUpdateDate) {
		this.jdUpdateDate = jdUpdateDate;
	}
	public Long getJdUpdateTimestamp() {
		return jdUpdateTimestamp;
	}
	public void setJdUpdateTimestamp(Long jdUpdateTimestamp) {
		this.jdUpdateTimestamp = jdUpdateTimestamp;
	}
	public String getIsFirst() {
		return isFirst;
	}
	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}
	public String getIsLast() {
		return isLast;
	}
	public void setIsLast(String isLast) {
		this.isLast = isLast;
	}
	public Date getEtlDate() {
		return etlDate;
	}
	public void setEtlDate(Date etlDate) {
		this.etlDate = etlDate;
	}

	@Override
	public String toString() {
		return "JdTimeline{" +
				"jdSrcId='" + jdSrcId + '\'' +
				", jdUpdateTimestamp=" + jdUpdateTimestamp +
				", jdUpdateDate=" + jdUpdateDate +
				", isFirst='" + isFirst + '\'' +
				", isLast='" + isLast + '\'' +
				", etlDate=" + etlDate +
				'}';
	}
}
