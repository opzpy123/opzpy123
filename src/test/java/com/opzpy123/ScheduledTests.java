package com.opzpy123;

import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.WeatherTaskVo;
import com.opzpy123.service.BarkWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@SpringBootTest
class ScheduledTests {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture<?> future;

    @Resource
    private BarkWeatherService barkWeatherService;

    @Test
    public void execute() {

            UserWeather userWeather = new UserWeather();
            userWeather.setBarkId("https://api.day.app/44esT6oeg4FZZzh8ce6sjf/");
            userWeather.setWeatherCity("南京");
            userWeather.setEnabled(1);
            userWeather.setCronExpression("0/30 * * * * ?");
            userWeather.setId(10000000L);
            userWeather.setPushType(WeatherEnum.EARLY_WARNING);
            WeatherTaskVo weatherTaskVo = new WeatherTaskVo(userWeather);
            threadPoolTaskScheduler.schedule(weatherTaskVo, new CronTrigger(userWeather.getCronExpression()));

        while (true) {}
    }

}
