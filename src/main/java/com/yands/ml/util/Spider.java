package com.yands.ml.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yands.ml.entity.ResponseData;

/**
 * @program: bigdata
 * @description: 模拟http请求
 * @author: gaoyun
 * @create: 2018-03-20 15:56
 **/
public class Spider {
	
    /**
     * @param url
     * @return
     */
    public static ResponseData get(String url) {
    	ResponseData data = new ResponseData();
        Connection con = Jsoup.connect(url);
        con.ignoreHttpErrors(true);
        con.ignoreContentType(true);
        con.method(Method.GET);
        try {
            Response response = con.execute();
            data.setCode(response.statusCode());
            data.setMessage(response.statusMessage());
            data.setResult(response.body());
        } catch (IOException e) {
            data.setCode(400);
            data.setMessage(e.getMessage());
        }
        return data;
    }
	
    /**
     * @param url
     * @return
     */
    public static ResponseData get(String url, String requestBody) {
    	ResponseData data = new ResponseData();
        Connection con = Jsoup.connect(url);
        con.ignoreHttpErrors(true);
        con.ignoreContentType(true);
        con.header("Content-Type", "application/json");
        con.requestBody(requestBody);
        con.method(Method.GET);
        try {
            Response response = con.execute();
            data.setCode(response.statusCode());
            data.setMessage(response.statusMessage());
            data.setResult(response.body());
        } catch (IOException e) {
            data.setCode(400);
            data.setMessage(e.getMessage());
        }
        return data;
    }

    /**
     * @param url
     * @param requestBody
     * @return
     */
    public static ResponseData post(String url, String requestBody) {
    	ResponseData data = new ResponseData();
        Connection con = Jsoup.connect(url);
        con.ignoreHttpErrors(true);
        con.ignoreContentType(true);
        con.header("Content-Type", "application/json");
        con.requestBody(requestBody);
        con.method(Method.POST);
        try {
            Response response = con.execute();
            JSONObject json = JSON.parseObject(response.body());
            data.setCode(json.getIntValue("code"));
            data.setMessage(json.getString("message"));
            data.setResult(json.get("result"));
        } catch (IOException e) {
            data.setCode(400);
            data.setMessage(e.getMessage());
        }
		return data;
    }

    /**
     * @param url
     * @param body
     * @return
     */
    public static ResponseData put(String url, String body) {
        Connection con = Jsoup.connect(url);
        con.header("Content-Type", "application/json");
        con.ignoreHttpErrors(true);
        con.ignoreContentType(true);
        con.requestBody(body);
        con.method(Method.PUT);
    	ResponseData data = new ResponseData();
        try {
            Response response = con.execute();
            data.setCode(response.statusCode());
            data.setMessage(response.statusMessage());
            data.setResult(response.body());
        } catch (IOException e) {
            data.setCode(400);
            data.setMessage(e.getMessage());
        }
        return data;
    }

    /**
     * @param url
     * @return
     */
    public static ResponseData delete(String url) {
        Connection con = Jsoup.connect(url);
        con.ignoreHttpErrors(true);
        con.ignoreContentType(true);
        con.method(Method.DELETE);
    	ResponseData data = new ResponseData();
        try {
            Response response = con.execute();
            data.setCode(response.statusCode());
            data.setMessage(response.statusMessage());
            data.setResult(response.body());
        } catch (IOException e) {
            data.setCode(400);
            data.setMessage(e.getMessage());
        }
        return data;
    }
}