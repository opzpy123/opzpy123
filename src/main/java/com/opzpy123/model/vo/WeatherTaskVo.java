package com.opzpy123.model.vo;

import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.model.UserWeather;
import com.opzpy123.util.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Callable;

@Slf4j
public class WeatherTaskVo implements Runnable {

    private UserWeather userWeather;

    public WeatherTaskVo(UserWeather userWeather) {
        this.userWeather = userWeather;
    }

    @Override
    public void run() {
        try {
            WeatherUtil.sendMsg(userWeather);
            log.info("定时任务完成:{}", userWeather);
        } catch (Exception e) {
            log.error("定时任务出错:{}", e.getMessage());
        }
    }
}
