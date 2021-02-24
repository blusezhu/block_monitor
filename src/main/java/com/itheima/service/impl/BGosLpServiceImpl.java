package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.ConfigCenter;
import com.itheima.data.mapper.BGosZhiyaDao;
import com.itheima.data.pojo.BGosZhiyaEntity;
import com.itheima.service.BGosLpService;
import com.itheima.utils.DateUtil;
import com.itheima.utils.HttpUtil;
import com.itheima.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzj
 * @since 2021-02-22
 */
@Service
@Slf4j
public class BGosLpServiceImpl extends ServiceImpl<BGosZhiyaDao, BGosZhiyaEntity> implements BGosLpService {

    @Autowired
    private HttpUtil httpUtil;

    @Override
    public void getData(int pageNo, int pageSize, String outOrIn, HashSet<Integer> isSendMail) {

        String gosUrl = getGosUrl(pageNo, pageSize, outOrIn);

        log.info("Lp请求url={}",gosUrl);

        String result = httpUtil.getHtml(gosUrl);

        JSONObject resultJson = JSON.parseObject(result);
        int status =(int )resultJson.get("status");
        if (status == 1) {
            log.info("获取数据成功！-----第{}页", pageNo);
            JSONObject data = resultJson.getJSONObject("data");
            JSONArray dataJSONArray = data.getJSONArray("data");
            List<BGosZhiyaEntity> bGosZhiyaEntities = new ArrayList<>();
            dataJSONArray.forEach(o->{
                JSONObject jsonObj = (JSONObject) o;
                BGosZhiyaEntity bGosZhiyaEntity = new BGosZhiyaEntity();
                bGosZhiyaEntity.setId(jsonObj.getInteger("id"));
                bGosZhiyaEntity.setTxHash(jsonObj.getString("txHash"));
                bGosZhiyaEntity.setNumber(jsonObj.getInteger("number"));
                bGosZhiyaEntity.setFromAddr(jsonObj.getString("fromAddr"));
                bGosZhiyaEntity.setToAddr(jsonObj.getString("toAddr"));
                bGosZhiyaEntity.setValue(jsonObj.getString("value"));
                bGosZhiyaEntity.setValueToString(jsonObj.getBigDecimal("valueToString"));
                bGosZhiyaEntity.setTokenName(jsonObj.getString("tokenName"));
                bGosZhiyaEntity.setTokenSymbol(jsonObj.getString("tokenSymbol"));
                bGosZhiyaEntity.setTokenType(jsonObj.getInteger("tokenType"));
                bGosZhiyaEntity.setDecimals(jsonObj.getInteger("decimals"));
                bGosZhiyaEntity.setTimestamp(DateUtil.getTimestampOfDateTime(jsonObj.getLong("timestamp")));
                bGosZhiyaEntity.setAddress(jsonObj.getString("address"));
                bGosZhiyaEntity.setLogIndex(jsonObj.getString("logIndex"));

                bGosZhiyaEntity.setOutOrIn(outOrIn);
                if (outOrIn.equals("out")) {
                    bGosZhiyaEntity.setValueToString(new BigDecimal("-" + bGosZhiyaEntity.getValueToString().toString()));
                }
                if (!isSendMail.contains(bGosZhiyaEntity.getId())) {
                    bGosZhiyaEntities.add(bGosZhiyaEntity);
                } else {
                    log.info("该id已发送过邮件不再发送！{}",bGosZhiyaEntity.getId());
                }

            });

            chackSendMail(bGosZhiyaEntities,isSendMail);

        }
    }

    private void chackSendMail(List<BGosZhiyaEntity> bGosZhiyaEntities, HashSet<Integer> isSendMail) {
       bGosZhiyaEntities.forEach(o->{
           if (o.getValueToString().abs().compareTo(new BigDecimal("10")) > 0) {
               Map<String,Object> map = getMailMap(ConfigCenter.getMailSenderInfo(),o);
               try {
                   MailUtil.sendMail(map, false, false);
                   isSendMail.add(o.getId());
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });

    }


    public String getGosUrl(int pageNo, int pgeSize,String outOrIn) {
        return "https://api-scan.hecochain.com/hsc/listTx/0xdade2b002d135c5796f7caad544f9bc043d05c9b/0x3bb34419a8e7d5e5c68b400459a8ec1affe9c56e/"+outOrIn+"/"+pageNo+"/"+pgeSize+"?x-b3-traceid=874d1e297766a40cbfa2caa9a84c943e";
    }


    public static Map<String,Object> getMailMap(JSONObject jsondata, BGosZhiyaEntity o){
        String content = "";
        if (o.getOutOrIn().equals("in")) {
            content=" gos 大额 【转入】 lp 【"+o.getValueToString()+"】枚，钱包地址= "+o.getFromAddr()+"时间 【"+DateUtil.getStringByTime(o.getTimestamp())+"】";
        }else {
            content=" gos 大额 【转出】 lp 【"+o.getValueToString()+"】枚，钱包地址= "+o.getToAddr()+"时间 【"+DateUtil.getStringByTime(o.getTimestamp())+"】";
        }
        log.info("发送邮件 ："+content);
        //发送方信息配置
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("host",jsondata.getString("host"));
        map.put("from",jsondata.getString("from"));
        map.put("password",jsondata.getString("password"));
        //发送内容配置
        map.put("datasrc","src/resource/fujian/dh.jpg");
        map.put("contentid","dh.jpg");

        map.put("content",content);
        //map.put("to","zizijie_2021@qq.com");
        map.put("to","156321781@qq.com");
        map.put("title","gos 大额lp 交易进出提醒");
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


}
