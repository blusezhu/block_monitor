import com.alibaba.fastjson.JSONObject;
import com.itheima.config.ConfigCenter;
import com.itheima.utils.MailUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailTest {

    public static Map<String,Object> getMailMap(JSONObject jsondata){
        //发送方信息配置
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("host",jsondata.getString("host"));
        map.put("from",jsondata.getString("from"));
        map.put("password",jsondata.getString("password"));
        //发送内容配置
        map.put("datasrc","src/resource/fujian/dh.jpg");
        map.put("contentid","dh.jpg");
        map.put("content","请注意，我不是广告<img src='cid:dh.jpg'>");
        map.put("to","zizijie_2021@qq.com");
        map.put("title","just a jok");
        //附件部分
        Map<String,String> mapfj = new HashMap<String, String>();
       // mapfj.put("filesrc","src/resource/fujian/spring.xml");
       // mapfj.put("filename","spring.xml");
        Map<String,String> mapfj1 = new HashMap<String, String>();
        //mapfj1.put("filesrc","src/resource/fujian/1.txt");
        //mapfj1.put("filename","1.txt");
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
       // list.add(mapfj);
        list.add(mapfj1);
        map.put("fujian", list);
        return map;
    }

    public static void main(String[] args) throws Exception {
        Map<String,Object> map = getMailMap(ConfigCenter.getMailSenderInfo());
        MailUtil.sendMail(map, false, false);
    }
}
