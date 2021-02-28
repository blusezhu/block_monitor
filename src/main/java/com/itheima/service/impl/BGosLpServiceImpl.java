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
           if (o.getValueToString().abs().compareTo(new BigDecimal("1")) > 0) {
               boolean isTranOrLp=getIsTranOrLp(o);
               o.setTranOrLp(isTranOrLp);
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
        HashMap<String,Object> map = new HashMap<>();

        String content = "";
        String title = "";

        if (o.getOutOrIn().equals("in")) {
            content=" gos 大额 【转入】钱包地址  【"+o.getFromAddr()+"】，转入地址= 【"+o.getToAddr()+"】交易哈希 【"+
                    o.getTxHash()+"】时间 【"+DateUtil.getStringByTime(o.getTimestamp())+"】";
            if (o.isTranOrLp()) {
                title = "gos 【卖出】 gos 【" + o.getValueToString() + "】枚 ";
            } else {
                title = "gos lp 【转入】  【" + o.getValueToString() + "】枚 ";
            }
        }else {
            content=" gos 大额 【转出】 转出钱包地址 【"+o.getToAddr()+"】，from地址= 【"+o.getFromAddr()+"】交易哈希 【"+
                    o.getTxHash()+"】时间 【"+DateUtil.getStringByTime(o.getTimestamp())+"】";
            if (o.isTranOrLp()) {
                title = "gos 【买入】 gos 【" + o.getValueToString() + "】枚 ";
            } else {
                title = "gos lp 【转入】  【" + o.getValueToString() + "】枚 ";
            }
        }
        log.info("发送邮件 ："+content);

        map.put("title",title);
        map.put("content",content);


        //发送方信息配置
        return  MailUtil.getEmailSettingMap(jsondata, map);

    }

    private  boolean getIsTranOrLp(BGosZhiyaEntity o) {

        String url = "https://api-scan.hecochain.com/hsc/search/" + o.getTxHash() + "?x-b3-traceid=995a87449da336e8aa06b28527ff3a29";
        String result = httpUtil.getHtml(url);

        JSONObject resultJson = JSON.parseObject(result);
        JSONObject data = resultJson.getJSONObject("data");
        JSONArray tokenTxes = data.getJSONArray("tokenTxes");

        if (tokenTxes.size() == 2) {
            return true;
        }
        return false;
    }


}
