import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.SpringBootApplicationRunner;
import com.itheima.data.pojo.BGosZhiyaEntity;
import com.itheima.service.BGosZhiyaService;
import com.itheima.utils.DateUtil;
import com.itheima.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootApplicationRunner.class)
@Slf4j
public class BlockTest {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private BGosZhiyaService bGosZhiyaService;

    @Test
   // @Transactional
    public  void runData() {
        Map<String, String> map = new HashMap<>();

        int pageNo=1;

        int pageSize=50;

        int pageCount;

        String outOrIn = "out";

        String url = getGosUrl(pageNo,pageSize,outOrIn);

        getDataAndSave(pageNo,pageSize,outOrIn,null);

    }

    private void getDataAndSave(int pageNo, int pageSize, String outOrIn,Integer desId) {
        String gosUrl = getGosUrl(pageNo, pageSize, outOrIn);

        log.info("请求url={}",gosUrl);

       // String result = HttpUtils.httpsReques(gosUrl,"","utf-8");
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

             /*   if (desId != null && bGosZhiyaEntity.getId() <= desId) {
                    isLatestId.set(true);
                    log.info("该记录id={},小于desId={}已保存过 不保存该记录",bGosZhiyaEntity.getId(),desId);
                    return;
                }*/

                bGosZhiyaEntities.add(bGosZhiyaEntity);
            });

            if (bGosZhiyaEntities.size() < 1) {
                log.info("获取的数据为空！结束递归！");
                return;
            }

            bGosZhiyaService.saveOrUpdateBatch(bGosZhiyaEntities);
            log.info("获取数据保存成功！-----第{}页 保存{}条", pageNo,bGosZhiyaEntities);
            getDataAndSave(pageNo+=1, pageSize, outOrIn,desId);
        } else {
            log.info("获取数据失败！-----第{}页 result={}",pageNo,resultJson);
        }


    }

    public String getGosUrl(int pageNo, int pgeSize,String outOrIn) {
        return "https://api-scan.hecochain.com/hsc/listTx/0x24ebe1f560b0ee086bb2986042388bf9a5c04725/0x3bb34419a8e7d5e5c68b400459a8ec1affe9c56e/"+outOrIn+"/"+pageNo+"/"+pgeSize+"?x-b3-traceid=d729bf36b27491299ff0f103d6611f56";
    }


    @Test
    public void jobTest() {
        int pageNo=1;

        int pageSize=2;

        String outOrIn = "out";

        getDataAndSave(pageNo, pageSize, outOrIn,92904539);

         outOrIn = "in";

        getDataAndSave(pageNo, pageSize, outOrIn,92904539);
    }

}
