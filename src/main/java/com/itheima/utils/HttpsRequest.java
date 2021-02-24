package com.itheima.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class HttpsRequest {

    private final Logger logger = LoggerFactory.getLogger(HttpsRequest.class);




    public  JSONObject httpsRequest(String requestUrl, String requestMethod, String requestStr) {
        logger.info("req---->:" + requestMethod + requestStr);
        HttpsURLConnection httpsConnection = null;
        try {
// 创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new TrustManager()};
// 初始化
            sslContext.init(null, tm, new java.security.SecureRandom());

// 获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            HostnameVerifier HostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String var1, SSLSession var2) {
                    return true;
                }

            };

            URL url = new URL(requestUrl);
            httpsConnection = (HttpsURLConnection) url.openConnection();
            httpsConnection.setDoOutput(false);
            httpsConnection.setDoInput(true);
            httpsConnection.setConnectTimeout(3000);
            httpsConnection.setReadTimeout(15000);
            httpsConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpsConnection.setRequestProperty("Charset", "UTF-8");
            httpsConnection.setRequestProperty("Authorization", "Basic aXdidXNlcjp0ZXN0MDAwMA==");
            httpsConnection.setRequestProperty("User-Agent", "Client identifier");
            httpsConnection.setRequestMethod("GET");
             httpsConnection.setUseCaches(false);
             httpsConnection.setRequestMethod(requestMethod);


// 设置当前实例使用的SSLSoctetFactory
            httpsConnection.setSSLSocketFactory(ssf);
            httpsConnection.setHostnameVerifier(HostnameVerifier);
            httpsConnection.connect();
// 往服务器端写内容
// 读取服务器端返回的内容
            InputStream inputStream = httpsConnection.getInputStream();
            if (httpsConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("connect ezcs service failed: " + httpsConnection.getResponseCode());
                JSONObject responseJson = new JSONObject();
                responseJson.put(ResponseKey.KEY_RESULT,
                        "200");
                return responseJson;
            }
            //String response = Utils.convertStreamToString(inputStream, Constant.ENCODING_UTF_8);
            String response ="";
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            logger.debug("response from ezcs service: " + response);
            JSONObject responseJson = JSON.parseObject(response);
            return responseJson;
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("connect local ezcs service exception: " + e.getMessage());
            JSONObject responseJson = new JSONObject();
            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                responseJson.put(ResponseKey.KEY_RESULT,
                        "200");
            } else {
                responseJson.put(ResponseKey.KEY_RESULT,
                        "200");
            }
    return responseJson;
        } finally {
            if (httpsConnection != null) {
                httpsConnection.disconnect();
            }
        }
    }

    class TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

    }

    public static class ResponseKey {
        public static final String KEY_RESULT = "result";
        public static final String KEY_REASON = "reason";
        public static final String KEY_DATA = "data";
        public static final String KEY_EXTRA = "extra";
    }

    public static void main(String[] args) {
        JSONObject get = new HttpsRequest().httpsRequest("https://api-scan.hecochain.com/hsc/listTx/0x24ebe1f560b0ee086bb2986042388bf9a5c04725/0x3bb34419a8e7d5e5c68b400459a8ec1affe9c56e/in/1/20?x-b3-traceid=d729bf36b27491299ff0f103d6611f56", "get", "");


        System.out.println(get);
    }
}
