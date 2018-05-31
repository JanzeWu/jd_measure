package com.ipin.jd.bean.db;

/**
 * Created by janze on 1/5/18.
 */
public class LoadMaxRownum {

    private String tableName;
    private long maxRownum;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getMaxRownum() {
        return maxRownum;
    }

    public void setMaxRownum(long maxRownum) {
        this.maxRownum = maxRownum;
    }
}
