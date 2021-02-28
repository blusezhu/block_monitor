package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.config.ConfigCenter;
import com.itheima.data.pojo.BGosZhiyaEntity;
import com.itheima.data.mapper.BGosZhiyaDao;
import com.itheima.service.BGosZhiyaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.utils.DateUtil;
import com.itheima.utils.HttpUtil;
import com.itheima.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzj
 * @since 2021-02-22
 */
@Service
@Slf4j
public class BGosZhiyaServiceImpl extends ServiceImpl<BGosZhiyaDao, BGosZhiyaEntity> implements BGosZhiyaService {
    @Autowired
    private HttpUtil httpUtil;

    @Override
    public void getDataAndSave(int pageNo, int pageSize, String outOrIn, Integer desId, AtomicReference<Boolean> isLatestId) {

       /* if (pageNo > 1) {
            return;
        }*/

        if (isLatestId.get()) {
            log.info("最新记录拉取完毕！");
            return;
        }

        String gosUrl = getGosUrl(pageNo, pageSize, outOrIn);
        log.info("请求url={}", gosUrl);


        //String result = httpsReques(gosUrl,"","utf-8");
        String result = httpUtil.getHtml(gosUrl);
        JSONObject resultJson = JSON.parseObject(result);
        int status = (int) resultJson.get("status");
        if (status == 1) {
            log.info("获取数据成功！-----第{}页", pageNo);
            JSONObject data = resultJson.getJSONObject("data");
            JSONArray dataJSONArray = data.getJSONArray("data");
            List<BGosZhiyaEntity> bGosZhiyaEntities = new ArrayList<>();
            dataJSONArray.forEach(o -> {
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

                if (desId != null && bGosZhiyaEntity.getId() <= desId) {
                    isLatestId.set(true);
                    //log.info("该记录id={},小于desId={}已保存过 不保存该记录",bGosZhiyaEntity.getId(),desId);
                    return;
                }

                bGosZhiyaEntities.add(bGosZhiyaEntity);
            });

            if (bGosZhiyaEntities.size() < 1) {
                log.info("获取的数据为空！结束递归！");
                return;
            }

            chackSendMail(bGosZhiyaEntities);

            saveOrUpdateBatch(bGosZhiyaEntities);
            log.info("获取数据保存成功！-----第{}页", pageNo);
            getDataAndSave(pageNo += 1, pageSize, outOrIn, desId, isLatestId);
        } else {
            log.info("获取数据失败！-----第{}页 result={}", pageNo, resultJson);
        }


    }

    private void chackSendMail(List<BGosZhiyaEntity> bGosZhiyaEntities) {
        bGosZhiyaEntities.forEach(o -> {
            if (o.getValueToString().abs().compareTo(new BigDecimal("10")) > 0) {
                Map<String, Object> map = getMailMap(ConfigCenter.getMailSenderInfo(), o);
                try {
                    MailUtil.sendMail(map, false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public String getGosUrl(int pageNo, int pgeSize, String outOrIn) {
        return "https://api-scan.hecochain.com/hsc/listTx/0x24ebe1f560b0ee086bb2986042388bf9a5c04725/0x3bb34419a8e7d5e5c68b400459a8ec1affe9c56e/" + outOrIn + "/" + pageNo + "/" + pgeSize + "?x-b3-traceid=d729bf36b27491299ff0f103d6611f56";
    }


    public static Map<String, Object> getMailMap(JSONObject jsondata, BGosZhiyaEntity o) {
        HashMap<String, Object> map = new HashMap<>();
        String title = "";

        String content = "";
        if (o.getOutOrIn().equals("in")) {
            content = " gos 大额 【转入】 质押 【" + o.getValueToString() + "】枚，钱包地址= " + o.getFromAddr() + "时间 【" + DateUtil.getStringByTime(o.getTimestamp()) + "】";
            title = "gos 质押 大额【转入】【" + o.getValueToString() + "】枚";
        } else {
            content = " gos 大额 【转出】 质押 【" + o.getValueToString() + "】枚，钱包地址= " + o.getToAddr() + "时间 【" + DateUtil.getStringByTime(o.getTimestamp()) + "】";
            title = "gos 质押 大额【转出】【" + o.getValueToString() + "】枚";
        }
        log.info("发送邮件 ：" + content);
        map.put("title", title);
        map.put("content",content);
        //发送方信息配置
        return MailUtil.getEmailSettingMap(jsondata, map);

    }


   /* public  String httpsReques(String url, String map, String charset) {
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).build();
        org.apache.http.client.HttpClient httpClient = null;
        HttpGet httpGet=null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
            httpGet.setConfig(requestConfig);
            //设置参数
            System.out.println("=====请求");
            HttpResponse response = httpClient.execute(httpGet);
            System.out.println("=====相应");
            if (response != null) {
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }*/

}
