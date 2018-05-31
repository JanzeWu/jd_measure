package com.ipin.jd.bean.db;

import java.io.Serializable;
import java.sql.Date;

public class JdFrom {

	/**
	 * 
	 */
	private int jdFromId;
	private String jdFromName;
	private Date etlDate;
	public int getJdFromId() {
		return jdFromId;
	}
	public void setJdFromId(int jdFromId) {
		this.jdFromId = jdFromId;
	}
	public String getJdFromName() {
		return jdFromName;
	}
	public void setJdFromName(String jdFromName) {
		this.jdFromName = jdFromName;
	}
	public Date getEtlDate() {
		return etlDate;
	}
	public void setEtlDate(Date etlDate) {
		this.etlDate = etlDate;
	}
	
}
