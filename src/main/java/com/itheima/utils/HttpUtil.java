package com.itheima.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.UUID;

@Component
public class HttpUtil {
    //创建HttpClient连接池
    private PoolingHttpClientConnectionManager em;

    public HttpUtil(){
        em = new PoolingHttpClientConnectionManager();
        em.setMaxTotal(200);
        em.setDefaultMaxPerRoute(100);
    }

    public static void main(String[] args) {
        String html = new HttpUtil().getHtml("https://api-scan.hecochain.com/hsc/listTx/0x24ebe1f560b0ee086bb2986042388bf9a5c04725/0x3bb34419a8e7d5e5c68b400459a8ec1affe9c56e/in/1/200?x-b3-traceid=d729bf36b27491299ff0f103d6611f56");
        System.out.println(html);
    }

    //爬取下面数据
    public String getHtml(String url){
        try {
            //获得连接
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(em).build();
            //创建请求对象
            HttpGet get = new HttpGet(url);

            //执行请求操作
            CloseableHttpResponse httpResponse = client.execute(get);
            //返回结果
            if(httpResponse.getStatusLine().getStatusCode()==200){
                if(httpResponse.getEntity()!=null){
                    String content = EntityUtils.toString(httpResponse.getEntity());
                    return content;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //下载图片
    public String getImg(String imgUrl){
        try {
            //获得连接
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(em).build();
            //创建请求对象
            HttpGet get = new HttpGet(imgUrl);

            //执行请求操作
            CloseableHttpResponse httpResponse = client.execute(get);
            //返回结果
            if(httpResponse.getStatusLine().getStatusCode()==200){
                if(httpResponse.getEntity()!=null){
                    // httpResponse.getEntity();
                    //得到扩展名 .jpg
                    String extName = imgUrl.substring(imgUrl.lastIndexOf(".")); // .jpg
                    //uuid.jpg
                    String imgName = UUID.randomUUID()+extName; // fdsafdsa3432sdfwe.jpg
                    //保存图片
                    httpResponse.getEntity().writeTo(new FileOutputStream("C:\\Users\\ma_ch\\Desktop\\images\\"+imgName));
                    //返回图片名称
                    return imgName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
