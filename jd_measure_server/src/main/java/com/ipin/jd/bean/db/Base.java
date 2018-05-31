package com.ipin.jd.bean.db;

import com.ipin.mes.constants.PGDataType;
import com.ipin.jd.consts.Regulation;
import com.ipin.mes.service.helper.PostgreSqlUtil;
import com.ipin.mes.util.ContentCheckUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ipin.jd.consts.Regulation.HIVE_NULL_VALUE;

/**
 * Created by janze on 12/20/17.
 */
public class Base {


    public static final String PG_STRING_TEMPLATE = "pg";
    public static final String HIVE_STRING_TEMPLATE = "hive";

    public static String generateIncSegId(String incSegName){
        return ContentCheckUtil.getMD5(incSegName + "ipin.com");
    }
    public static String generateJdId(String entityId, String contentSign){
        return ContentCheckUtil.getMD5(entityId + contentSign);
    }
    public String generateSrc(String tableName, String oid){
        return tableName + ":" + oid;
    }

    public String subString(String content, int length){
        if(StringUtils.isNotBlank(content) && content.length() > length){
            return content.substring(0, length);
        }
        return content;
    }

    public String getPgStringTemplate(int arrayStringElementLength){

        return this.getStringTemplate(PG_STRING_TEMPLATE, arrayStringElementLength);
    }

    public String getHiveStringTemplate(){
        return this.getStringTemplate(HIVE_STRING_TEMPLATE, null);
    }


    private String getStringTemplate(String type, Integer arrayStringElementLength){
        StringBuilder buf = new StringBuilder();
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for(int i = 0; i < fields.length; i++){
                Field field = fields[i];
                Object o = field.get(this);
                if(o != null){
                    if(field.getGenericType().toString().startsWith("java.util.List")){
                        //hive 数组格式
                        if(type.equals(HIVE_STRING_TEMPLATE)) {
                            List<Object> list = (List<Object>) o;
                            for (int index = 0; index < list.size(); index++) {
                                Object item = list.get(index);
                                buf.append(item != null ? item : HIVE_NULL_VALUE);
                                buf.append(index == list.size() - 1 ? "" : Regulation.HIVE_COLLECTION_ELEMENT_SPLIT);
                            }
                        }else if(type.equals(PG_STRING_TEMPLATE)){
                            // pg 数组格式
                            if(field.getGenericType().toString().equals("java.util.List<java.lang.String>")){
                                List<String> list = (List<String>) o;
                                if(list != null && !list.isEmpty()) {
                                    List<String> strList = new ArrayList<>();
                                    for (String item : list) {
                                        strList.add(item);
                                    }
                                    buf.append(PostgreSqlUtil.getArrayByList(strList, PGDataType.VARCHAR.getDataType(), arrayStringElementLength));
                                }
                            }else if(field.getGenericType().toString().equals("java.util.List<java.lang.Integer>")){
                                List<Integer> list = (List<Integer>) o;
                                if(type.equals(PG_STRING_TEMPLATE)){
                                    List<Integer> intList = new ArrayList<>();
                                    for(Integer item : list){
                                        intList.add(item);
                                    }
                                    buf.append(PostgreSqlUtil.getArrayByIntegerList(intList, PGDataType.INT.getDataType()));
                                }
                            }else{
                                throw new RuntimeException("PG 数组格式暂不支持除（String | Integer）类型的转换！");
                            }
                        }
                    }else if(field.get(this).getClass().equals(java.sql.Date.class)){
                        Date dat = new Date(((java.sql.Date)o).getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        buf.append(sdf.format(dat));
                    }else {
                        buf.append(o);
                    }
                }else{
                    buf.append(HIVE_NULL_VALUE);
                }
                buf.append(i == fields.length - 1? "" : Regulation.HIVE_FIELD_SPLIT_CHAR);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return buf.toString();
    }

    public <T extends Base> T getObjectFromPgStringTemplate(String template, Class<T> clazz){
        return this.getObjectFromStringTemplate(template, clazz, PG_STRING_TEMPLATE);
    }

    public <T extends Base> T getObjectFromHiveStringTemplate(String template, Class<T> clazz){
        return this.getObjectFromStringTemplate(template, clazz, HIVE_STRING_TEMPLATE);
    }
    /**
     * 数组类型的没法转化，无法使用。。。
     * @param template
     * @param clazz
     * @param type
     * @return
     */
    private  <T extends Base> T getObjectFromStringTemplate(String template, Class<T> clazz, String type){
        T base = null;
        InputStreamReader reader = null;
        try {
            if(StringUtils.isBlank(template))
                return base;

            reader = new InputStreamReader(new ByteArrayInputStream(template.getBytes()), "utf-8");
            Iterable<CSVRecord> records = CSVFormat.RFC4180
                    .withDelimiter(Regulation.HIVE_FIELD_SPLIT_CHAR)
                    .withQuote(Regulation.QUOTE_FOUR).parse(reader);
            CSVRecord record = records.iterator().next();

            base = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            if(record.size() != fields.length)
                return base;

            for(int i =0; i< record.size(); i++){
                Field f = fields[i];
                if(List.class.equals(f.getType()) || LinkedList.class.equals(f.getType())
                        || ArrayList.class.equals(f.getType())){
                    String arr_str = record.get(i);
                    if(StringUtils.isNotBlank(arr_str)){
                        if(type.equals(PG_STRING_TEMPLATE)){
                            //将原字符串中的\2字符去掉
                            arr_str = arr_str.replaceAll(Regulation.HIVE_COLLECTION_ELEMENT_SPLIT, "")
                                    .replaceAll(Regulation.QUOTE_FOUR + "", "");
                            int start_char = arr_str.indexOf('{');
                            int end_char = arr_str.lastIndexOf('}');
                            //存在"{}"
                            if(start_char > -1 && end_char > start_char){
                                arr_str = arr_str.substring(arr_str.indexOf('{'), arr_str.lastIndexOf('}'));
                                arr_str = arr_str.replaceFirst("[{]", "");
                                //字符串
                                if(f.getGenericType().toString().equals("java.util.List<java.lang.String>")){
                                    // 将数组元素内部的","转换成"\u0010"
                                    // 数组元素分隔符","拆分数组
                                    String regex = "([\"][^\"]+[\"])";
                                    Pattern pattern = Pattern.compile(regex);
                                    Matcher m = pattern.matcher(arr_str);
                                    while( true ){
                                        if (m.find()){
                                            for( int t = 0; t < m.groupCount(); t++){
                                                arr_str = arr_str.replace(m.group(t), m.group(t).replaceAll(",", "\\u0010"));
                                            }
                                        }else{
                                            break;
                                        }
                                    }
                                    String[] as = arr_str.split(",");
                                    List<String> list = new ArrayList<>();
                                    for( String e : as){
                                        e = e.replaceAll("\\u0010", ",");
                                        list.add(e);
                                    }
                                    f.set(base, list);
                                }else{
                                    throw new RuntimeException("无法转化非string数组类型");
                                    //TODO suport not string array
                                }
                            }
                        }else if(type.equals(HIVE_STRING_TEMPLATE)) {
                            try{
                                List<String> list = Arrays.asList(arr_str.split(Regulation.HIVE_COLLECTION_ELEMENT_SPLIT));
                                for(int index = 0; index < list.size(); index ++){
                                    if(HIVE_NULL_VALUE.equals(list.get(index))){
                                        list.set(index, null);
                                    }
                                }
                                f.set(base, list);
                            }catch (Exception e){
                                throw new RuntimeException("hive string template 转object 暂不支持 除（string|数字）的转换");
                            }

                        }

                    }
                }else{
                    if(StringUtils.isNotBlank(record.get(i)) ) {
                        // hive \N 为null
                        if( (type.equals(HIVE_STRING_TEMPLATE) && HIVE_NULL_VALUE.equals(record.get(i)))){
                            continue;
                        }
                        if(Integer.class.equals(f.getType()) || int.class.equals(f.getType())){
                            f.set(base, Integer.parseInt(record.get(i)));
                        }else if(Long.class.equals(f.getType()) || long.class.equals(f.getType()) ){
                            f.set(base, Long.parseLong(record.get(i)));
                        }else if(BigDecimal.class.equals(f.getType())){
                            f.set(base, new BigDecimal(record.get(i)));
                        }else if( java.util.Date.class.equals(f.getType())
                                ||java.sql.Date.class.equals(f.getType())){

                            SimpleDateFormat sdf = record.get(i).length() == 8 ? new SimpleDateFormat("yyyyMMdd") : new SimpleDateFormat("yyyy-MM-dd");
                            Date t = sdf.parse(record.get(i));
                            if(t != null){
                                f.set(base, new java.sql.Date(t.getTime()));
                            }
                        }else if(Double.class.equals(f.getType()) || double.class.equals(f.getType())){
                            f.set(base, Double.parseDouble(record.get(i)));
                        }else if(Boolean.class.equals(f.getType()) || boolean.class.equals(f.getType())){
                            String s = record.get(i).toLowerCase();
                            if(s.startsWith("t") || s.startsWith("y") || s.equals("on") || s.equals("1")){
                                f.set(base, Boolean.TRUE);
                            }else{
                                f.set(base, Boolean.FALSE);
                            }
                        }else if(String.class.equals(f.getType())){
                            f.set(base, record.get(i).replaceAll(Regulation.HIVE_COLLECTION_ELEMENT_SPLIT, "").replaceAll("\4", ""));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        } catch (InstantiationException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base;
    }

}
