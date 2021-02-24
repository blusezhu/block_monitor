package com.itheima.job;

import com.itheima.service.BGosLpService;
import com.itheima.service.BGosZhiyaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class GoRunDataJob {
    @Autowired
    private BGosZhiyaService bGosZhiyaService;
    @Autowired
    private BGosLpService bGosLpService;

    private HashSet<Integer> isSendMail=new HashSet<>();

   // @Scheduled(cron = "0 0/1  * * * ?")
    @Scheduled(cron = "*/10 * * * * ?")
    public void getGosZhiYaData() {

        Integer descId = bGosZhiyaService.query().orderByDesc("id").list().get(0).getId();
        System.out.println("最新id========"+descId);
        int pageNo=1;

        int pageSize=6;

        String outOrIn = "out";
        AtomicReference<Boolean> isLatestId = new AtomicReference<>(false);
        bGosZhiyaService.getDataAndSave(pageNo, pageSize, outOrIn,descId,isLatestId);

        outOrIn = "in";
        isLatestId = new AtomicReference<>(false);
        bGosZhiyaService.getDataAndSave(pageNo, pageSize, outOrIn,descId, isLatestId);

    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void getGosLpData() {
        log.info("==========>LP 监控开始==============================");

        int pageNo=1;

        int pageSize=3;

        String outOrIn = "out";

        bGosLpService.getData(pageNo, pageSize, outOrIn,isSendMail);

        outOrIn = "in";
        bGosLpService.getData(pageNo, pageSize, outOrIn,isSendMail);
        log.info("==========>LP 监控结束==============================");
    }


}
