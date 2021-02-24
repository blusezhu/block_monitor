//package com.itheima.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.config.SocketConfig;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.util.Map.Entry;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLSession;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//public class HttpUtils {
//    private static RequestConfig requestConfig;
//
//    /**
//     * 设置 https 请求
//     *
//     * @throws Exception
//     */
//    private static void trustAllHttpsCertificates() throws Exception {
//        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//            public boolean verify(String str, SSLSession session) {
//                return true;
//            }
//        });
//        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
//        javax.net.ssl.TrustManager tm = new miTM();
//        trustAllCerts[0] = tm;
//        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
//                .getInstance("SSL");
//        sc.init(null, trustAllCerts, null);
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc
//                .getSocketFactory());
//    }
//
//
//    //设置 https 请求证书
//    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
//
//        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//            return null;
//        }
//
//        public boolean isServerTrusted(
//                java.security.cert.X509Certificate[] certs) {
//            return true;
//        }
//
//        public boolean isClientTrusted(
//                java.security.cert.X509Certificate[] certs) {
//            return true;
//        }
//
//        public void checkServerTrusted(
//                java.security.cert.X509Certificate[] certs, String authType)
//                throws java.security.cert.CertificateException {
//            return;
//        }
//
//        public void checkClientTrusted(
//                java.security.cert.X509Certificate[] certs, String authType)
//                throws java.security.cert.CertificateException {
//            return;
//        }
//
//
//    }
//
//
//    /**
//     * 向指定 URL 发送POST方法的请求
//     *
//     * @param url      发送请求的 URL
//     * @param paramMap 请求参数
//     * @return 所代表远程资源的响应结果
//     */
//    public static String sendPost(String url, Map<String, ?> paramMap) {
//        PrintWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//
//        String param = "";
//        if (paramMap != null) {
//            Iterator<String> it = paramMap.keySet().iterator();
//
//            while (it.hasNext()) {
//                String key = it.next();
//                param += key + "=" + paramMap.get(key) + "&";
//            }
//        }
//
//        try {
//            trustAllHttpsCertificates();
//
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Accept-Charset", "utf-8");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.print(param);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    public static String sendGet(String httpurl) {
//        HttpURLConnection connection = null;
//        InputStream is = null;
//        BufferedReader br = null;
//        String result = null;// 返回结果字符串
//        try {
//            // 创建远程url连接对象
//            URL url = new URL(httpurl);
//            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
//            connection = (HttpURLConnection) url.openConnection();
//            // 设置连接方式：get
//            connection.setRequestMethod("GET");
//            // 设置连接主机服务器的超时时间：15000毫秒
//            connection.setConnectTimeout(15000);
//            // 设置读取远程返回的数据时间：60000毫秒
//            connection.setReadTimeout(60000);
//            // 发送请求
//            connection.connect();
//            // 通过connection连接，获取输入流
//            if (connection.getResponseCode() == 200) {
//                is = connection.getInputStream();
//                // 封装输入流is，并指定字符集
//                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                // 存放数据
//                StringBuffer sbf = new StringBuffer();
//                String temp = null;
//                while ((temp = br.readLine()) != null) {
//                    sbf.append(temp);
//                    sbf.append("\r\n");
//                }
//                result = sbf.toString();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭资源
//            if (null != br) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (null != is) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            connection.disconnect();// 关闭远程连接
//        }
//
//        return result;
//    }
//
//
//    public static String putJson(String url, JSONObject params, JSONObject headers) {
//        try {
//            RequestConfig requestConfig =
//                    RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
//            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
//            HttpPut httpPut = new HttpPut(url);
//            httpPut.setHeader("Content-Type", "application/json;charset=UTF-8");
//            if (headers != null && !headers.isEmpty()) {
//                for (String key : headers.keySet()) {
//                    httpPut.setHeader(key, headers.getString(key));
//                }
//            }
//            if (params != null && !params.isEmpty()) {
//                StringEntity se = new StringEntity(params.toJSONString(), "UTF-8");
//                httpPut.setEntity(se);
//            }
//            HttpResponse response = client.execute(httpPut);
//            HttpEntity entity = response.getEntity();
//            return EntityUtils.toString(entity, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getJson(String url, JSONObject headers) {
//        try {
//            RequestConfig requestConfig =
//                    RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
//            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
//            HttpGet httpGet = new HttpGet(url);
//            httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
//            if (headers != null && !headers.isEmpty()) {
//                for (String key : headers.keySet()) {
//                    httpGet.setHeader(key, headers.getString(key));
//                }
//            }
//            HttpResponse response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            return EntityUtils.toString(entity, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 判断链接是否可用
//     *
//     * @param urlStr
//     * @param countMax
//     * @return
//     */
//    public static boolean isConnect(String urlStr, int countMax) {
//        URL url;
//        HttpURLConnection con;
//        int state = -1;
//
//        int counts = 0;
//        if (urlStr == null || urlStr.length() <= 0) {
//            return false;
//        }
//        while (counts < countMax) {
//            try {
//                if (!"http".startsWith(urlStr)) {
//                    urlStr = "http://" + urlStr;
//                }
//                url = new URL(urlStr);
//                con = (HttpURLConnection) url.openConnection();
//                state = con.getResponseCode();
////                System.out.println(counts +"= "+state);
//                if (state == 200) {
//                    return true;
//                }
//                break;
//            } catch (Exception ex) {
//                counts++;
//                System.out.println("URL不可用，连接第 " + counts + " 次");
//                urlStr = null;
//                continue;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 发送 POST 请求（HTTP），K-V形式
//     *
//     * @param apiUrl API接口URL
//     * @param params 参数map
//     * @return 返回结果
//     */
//    public static String doPost(String apiUrl, Map<String, Object> params) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String httpStr = null;
//        HttpPost httpPost = new HttpPost(apiUrl);
//        CloseableHttpResponse response = null;
//        try {
//            if (requestConfig == null) {
//                requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000)
//                        .setSocketTimeout(3000).setConnectTimeout(2000).build();
//            }
//            httpPost.setConfig(requestConfig);
//            List<NameValuePair> pairList = new ArrayList<>(params.size());
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
//                        .getValue().toString());
//                pairList.add(pair);
//            }
//            httpPost.setEntity(new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8));
//            response = httpClient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            httpStr = EntityUtils.toString(entity, "UTF-8");
//        } catch (IOException e) {
//            // e.printStackTrace();
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException e) {
//                }
//            }
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//            }
//        }
//        return httpStr;
//    }
//
//    /**
//     * 发送 POST 请求（HTTP），JSON形式
//     *
//     * @param apiUrl 请求路径
//     * @param json   json对象
//     * @return 返回结果
//     */
//    public static String doPost(String apiUrl, Object json) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String httpStr = null;
//        HttpPost httpPost = new HttpPost(apiUrl);
//        CloseableHttpResponse response = null;
//
//        try {
//            httpPost.setConfig(requestConfig);
//            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题
//            stringEntity.setContentEncoding("UTF-8");
//            stringEntity.setContentType("application/json");
//            httpPost.setEntity(stringEntity);
//            response = httpClient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            httpStr = EntityUtils.toString(entity, "UTF-8");
//        } catch (IOException e) {
//            log.error("请求数据异常！" + e.getMessage());
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException e) {
//                    log.error("请求数据异常！" + e.getMessage());
//                }
//            }
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                log.error("关闭数据异常！" + e.getMessage());
//            }
//        }
//        return httpStr;
//    }
//
//    /*
//     * 利用HttpClient进行post请求的工具类
//     */
//        public static String doPost(String url, Map<String, String> map, String charset) {
//            HttpClient httpClient = null;
//            HttpPost httpPost = null;
//            String result = null;
//            try {
//                httpClient = new SSLClient();
//                httpPost = new HttpPost(url);
//                //设置参数
//                List<NameValuePair> list = new ArrayList<NameValuePair>();
//                Iterator iterator = map.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Entry<String, String> elem = (Entry<String, String>) iterator.next();
//                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
//                }
//                if (list.size() > 0) {
//                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
//                    httpPost.setEntity(entity);
//                }
//                HttpResponse response = httpClient.execute(httpPost);
//                if (response != null) {
//                    HttpEntity resEntity = response.getEntity();
//                    if (resEntity != null) {
//                        result = EntityUtils.toString(resEntity, charset);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return result;
//        }
//
//
//    public static String httpsReques(String url, String map, String charset) {
//        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).build();
//        org.apache.http.client.HttpClient httpClient = null;
//        HttpGet httpGet=null;
//        String result = null;
//        try {
//            httpClient = new SSLClient();
//            httpGet = new HttpGet(url);
//            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
//            httpGet.setConfig(requestConfig);
//            //设置参数
//            System.out.println("=====请求");
//            HttpResponse response = httpClient.execute(httpGet);
//            System.out.println("=====相应");
//            if (response != null) {
//                HttpEntity resEntity = response.getEntity();
//
//                if (resEntity != null) {
//                    result = EntityUtils.toString(resEntity, charset);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return result;
//    }
//
//
//}
