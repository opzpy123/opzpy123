package com.opzpy123.model.vo;

import com.opzpy123.util.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Callable;

@Slf4j
public class WeatherTaskVo implements Runnable {

    private String  barkId;
    private String location;

    public WeatherTaskVo(String barkId, String location) {
        this.barkId = barkId;
        this.location = location;
    }

    @Override
    public void run() {
        try {
            WeatherUtil.sendMsg(barkId,location);
            log.info("天气任务完成");
        }catch (Exception e){
            log.error("定时任务出错:{}",e.getMessage());
        }
    }
}
