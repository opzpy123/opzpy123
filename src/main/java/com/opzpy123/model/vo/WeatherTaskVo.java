package com.opzpy123.model.vo;

import com.opzpy123.model.UserWeather;
import com.opzpy123.service.BarkWeatherService;
import com.opzpy123.service.UserWeatherService;
import com.opzpy123.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
public class WeatherTaskVo implements Runnable {
    //   实现runnable无法注入bean
//    @Resource
    private final BarkWeatherService barkWeatherService = (BarkWeatherService) SpringContextUtil.getBean("BarkWeatherService");

    private final UserWeather userWeather;

    public WeatherTaskVo(UserWeather userWeather) {
        this.userWeather = userWeather;
    }

    @Override
    public void run() {
        switch (userWeather.getPushType()) {
            case DAILY: {
                //早报和晚报
                barkWeatherService.sendMsgDaily(userWeather);
                break;
            }
            case EARLY_WARNING: {
                //预警 每10分钟触发一次
                barkWeatherService.sendMsgEarlyWarning(userWeather);
                break;
            }
            case TEMPERATURE_DIFFERENCE: {
                //温差 每天提醒一次
                barkWeatherService.sendMsgTemperatureDifference(userWeather);
                break;
            }
        }
    }
}
