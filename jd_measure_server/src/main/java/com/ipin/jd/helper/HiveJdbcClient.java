package com.ipin.jd.helper;

/**
 * Created by janze on 1/3/18.
 */
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class HiveJdbcClient {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    /**
     * @throws SQLException
     */
    public Map<String, Long> loadMaxRownum(){
        Map<String, Long> map = new HashMap<>();
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:hive2://192.168.1.85:10000/", "wujianzhen", "wujianzhen123");
            Statement stmt = con.createStatement();
            String tableName = "edw_jd.load_max_rownum";
            ResultSet resultSet = stmt.executeQuery("SELECT table_name, load_max_rownum FROM " + tableName);
            while(resultSet.next()){
                String tn = resultSet.getString("table_name");
                Long maxRownum = resultSet.getLong("load_max_rownum");
                map.put(tn, maxRownum);
            }
        }catch (SQLException e){
            e.printStackTrace();

        }finally {
            try {
                if(con != null){
                    con.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return map;
    }
}
