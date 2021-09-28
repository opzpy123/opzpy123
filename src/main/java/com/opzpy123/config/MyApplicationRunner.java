package com.opzpy123.config;

import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.WeatherTaskVo;
import com.opzpy123.service.UserWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class MyApplicationRunner implements ApplicationRunner {

    @Resource
    private UserWeatherService userWeatherService;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (UserWeather userWeather : userWeatherService.list()) {
            if (userWeather.getEnabled() == 1) {
                WeatherTaskVo taskVo = new WeatherTaskVo(userWeather);
                threadPoolTaskScheduler.schedule(taskVo, new CronTrigger(userWeather.getCronExpression()));
            }
        }
    }
}
