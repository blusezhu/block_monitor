package com.itheima.config;


import com.alibaba.fastjson.JSONObject;

/**
 *
 * 配置中心
 * getMailSenderInfo 方法为email外发提供发送方配置
 * @author HUAWEI
 *
 */
public class ConfigCenter {

    private static String from = "zizijie_2021@qq.com";
    private static String pass = "rfbryqrurhzphadc";
    private static String host = "smtp.qq.com";
    public static JSONObject getMailSenderInfo(){
        JSONObject obj = new JSONObject();
        obj.put("from",from );
        obj.put("password",pass );
        obj.put("host",host );
        return obj;
    }

}