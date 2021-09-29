package com.opzpy123;

import com.alibaba.fastjson.JSONObject;
import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.UserWeather;
import com.opzpy123.util.JsonUitl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
//@SpringBootTest
class WeatherTests {

    @Test
    void contextLoads() throws Exception {
        String weatherCity = "当涂";
        String url = "https://geoapi.qweather.com/v2/city/lookup?location="+weatherCity+"&key=5ca481c318344e159d8cd32039abb747";
        JSONObject jsonObject = JsonUitl.loadJsonAsJsonObj(url);
        String weatherCityId = jsonObject.getJSONArray("location").getJSONObject(0).getString("id");
        System.out.println(weatherCityId);
    }


}
