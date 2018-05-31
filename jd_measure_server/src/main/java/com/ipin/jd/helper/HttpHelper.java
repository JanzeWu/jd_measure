package com.ipin.jd.helper;

import com.google.gson.Gson;
import com.ipin.mes.beans.JobZhinengRaw;
import com.ipin.mes.util.HttpRequests;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by janze on 1/2/18.
 */
public class HttpHelper {


    public enum JsonType{
        JSON_TYPE_OBJECT("object"),
        JSON_TYPE_LIST("list");
        private String type;
        private JsonType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
    }
    private volatile static PoolingClientConnectionManager managerJdMeasure = null;
    private volatile static PoolingClientConnectionManager managerZhinengMeasure = null;

    private static Map<String, String> resultMap = new ConcurrentHashMap<>();

    public static PoolingClientConnectionManager getManagerJdMeasure(){

        if(managerJdMeasure == null){
            synchronized (HttpRequests.class){
                if(managerJdMeasure == null){
                    SchemeRegistry schemeRegistry = new SchemeRegistry();
                    schemeRegistry.register(
                            new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
                    managerJdMeasure = new PoolingClientConnectionManager(schemeRegistry);
                    managerJdMeasure.setDefaultMaxPerRoute(20);
                    managerJdMeasure.setMaxTotal(200);
                    HttpHost httpHost = new HttpHost("measure.ipin.com");
                    managerJdMeasure.setMaxPerRoute(new HttpRoute(httpHost), 50);
                }
            }
        }
        return managerJdMeasure;
    }
    public static PoolingClientConnectionManager getManagerZhinengMeasure(){
        if(managerZhinengMeasure == null){
            synchronized (HttpRequests.class){
                if(managerZhinengMeasure == null){
                    SchemeRegistry schemeRegistry = new SchemeRegistry();
                    schemeRegistry.register(
                            new Scheme("http", 1234, PlainSocketFactory.getSocketFactory()));
                    managerZhinengMeasure = new PoolingClientConnectionManager(schemeRegistry);
                    managerZhinengMeasure.setDefaultMaxPerRoute(10);
                    managerZhinengMeasure.setMaxTotal(50);
                    HttpHost httpHost = new HttpHost("192.168.1.100");
                    managerZhinengMeasure.setMaxPerRoute(new HttpRoute(httpHost), 20);
                }
            }

        }
        return managerZhinengMeasure;

    }

    public static String get(String url, Map<String, String> params){
        HttpClient client =  new DefaultHttpClient(getManagerJdMeasure());

        BufferedReader rd = null;
        StringBuffer result = new StringBuffer();
        try{
            url = url.trim();
            if (params != null && params.size() > 0){
                StringBuilder buf = new StringBuilder(url);
                buf.append("?");
                int count = 0;
                for (Map.Entry<String, String> entry : params.entrySet()){
                    buf.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8"));
                    count += 1;
                    buf.append(count == params.size() ? "" : "&");
                }
                url = buf.toString();
            }
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = null;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        }catch(Exception e){
            try {
                System.out.println(URLDecoder.decode(url, "utf-8") + "\n请求出错:" + e.getMessage());
                throw new RuntimeException(URLDecoder.decode(url, "utf-8") + "\n请求出错:" + e.getMessage());
            }catch (UnsupportedEncodingException e1){
                e1.printStackTrace();
            }


        }finally{

            try {
                if(rd != null){
                    rd.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String post(String url, Map<String, String> params) {
        HttpClient client =  new DefaultHttpClient(getManagerJdMeasure());
        BufferedReader rd = null;
        StringBuffer result = new StringBuffer();
        try{
            url = url.trim();

            HttpPost post = new HttpPost(url);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0){
                for (Map.Entry<String, String>entry : params.entrySet()){
                    urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "utf-8"));
            HttpResponse response = client.execute(post);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = null;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        }catch(Exception e){
            System.out.println(url + "请求失败:" + e.getMessage());
            throw new RuntimeException(url + "\n请求失败:" + e.getMessage() + "\nparamContent:" + params);
        }finally{
            try{
                if(rd != null){
                    rd.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result.toString();

    }

    /**
     *
     * @param url
     * @param jsonParams
     * @return
     * @throws IOException
     */
    public static String postByJson(String url, String jsonParams, boolean isZhineng) {
        HttpClient client = null;
        if(isZhineng){
            client = new DefaultHttpClient(getManagerZhinengMeasure());
        }else {
            client = new DefaultHttpClient(getManagerJdMeasure());
        }

        BufferedReader rd = null;
        StringBuffer result = new StringBuffer();
        try{
            url = url.trim();
            HttpPost post = new HttpPost(url);

            StringEntity s = new StringEntity(jsonParams, "utf-8");
            s.setContentType("application/json");
            s.setContentEncoding("utf-8");
            post.setEntity(s);
            HttpResponse response = client.execute(post);

            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = null;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }catch(Exception e){
            System.out.println(url + "\n请求失败:" + e.getMessage() + "\nparamContent:" + jsonParams);
            throw new RuntimeException(url + "\n请求失败:" + e.getMessage() + "\nparamContent:" + jsonParams);
        }finally{
            try {
                if(rd != null){
                    rd.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String zhinengRawListToJsonArray(List<JobZhinengRaw> jobZhinengRaws){
        if(jobZhinengRaws == null){
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(jobZhinengRaws);
    }

    public static void main(String[] args) {

        class MyThread implements Runnable {
            @Override
            public void run() {
                try{
                    Map params = new HashMap();
                    params.put("diploma_str", "本科");
                    System.out.println(HttpHelper.get("http://measure.ipin.com/measure/diploma", params));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        for(int i = 0; i< 5; i++){
            MyThread t = new MyThread();
            Thread tt = new Thread(t);
            tt.start();
        }

    }
}
