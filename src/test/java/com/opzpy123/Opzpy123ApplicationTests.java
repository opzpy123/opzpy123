package com.opzpy123;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.opzpy123.config.PropertiesConfig;
import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.UserWeather;
import com.opzpy123.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Slf4j
@SpringBootTest
class Opzpy123ApplicationTests {
    @Resource
    private UserWeatherMapper userWeatherMapper;

    @Resource
    private OssUtils ossUtils;


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

    @Test
    void propertiesTest() {

    }

    @Test
    void testCallBack(){
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("templates/dashboard.html");
        System.out.println(ossUtils.upload(resourceAsStream, "test.html" ));
    }


}
