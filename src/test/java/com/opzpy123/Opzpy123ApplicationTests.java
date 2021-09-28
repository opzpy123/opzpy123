package com.opzpy123;

import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.WeatherTaskVo;
import com.opzpy123.util.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class Opzpy123ApplicationTests {
    @Resource
    private UserWeatherMapper userWeatherMapper;


    @Test
    void contextLoads() {
        UserWeather userWeather = new UserWeather();
        userWeather.setUserId(123123L);
        userWeather.setWeatherCity("南京");
        userWeather.setBarkId("ashhoa");
        userWeather.setEnabled(1);
        userWeather.setCronExpression("asiohofa");
        userWeather.setPushType(WeatherEnum.DAILY);
        userWeatherMapper.insert(userWeather);
    }


}
