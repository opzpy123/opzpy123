package com.opzpy123;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.opzpy123.config.PropertiesConfig;
import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.UserWeather;
import com.opzpy123.util.JsonUitl;
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
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

@Slf4j
//@SpringBootTest
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
    void httpTest() throws InterruptedException {
        while (true) {
//            System.out.println(JsonUitl.loadJson("https://devapi.qweather.com/v7/warning/now?location=101190101&key=5ca481c318344e159d8cd32039abb747"));
            System.out.println(JsonUitl.loadJson("https://devapi.qweather.com/v7/warning/now?location=101180901&key=5ca481c318344e159d8cd32039abb747"));
            System.out.println("休息10秒");
            Thread.sleep(10000);
        }
    }


    @Test
    void propertiesTest() {

    }

    @Test
    void testCallBack() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("templates/dashboard.html");
        System.out.println(ossUtils.upload(resourceAsStream, "test.html"));
    }

    @Test
    void getRandomStringSet() {
        int maxRandomLen = 30;
        String s = RandomUtil.randomString(10000);
        HashSet<String> res = new HashSet<>();
        for (int i = maxRandomLen + 1; i < s.length(); i++) {
            int randInt1 = RandomUtil.randomInt(5, maxRandomLen);
            int randInt2 = RandomUtil.randomInt(5, maxRandomLen);
            int randInt3 = RandomUtil.randomInt(5, maxRandomLen);
            res.add(s.substring(i-randInt1, i));
            res.add(s.substring(i-randInt2, i));
            res.add(s.substring(i-randInt3, i));
        }
        System.out.println(res);
    }

}
