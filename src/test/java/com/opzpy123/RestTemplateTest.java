package com.opzpy123;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTemplateTest {

    @Resource
    private RestTemplate restTemplate;

    @Test
    void test() {
        String weatherCity = "当涂";
        String url = "https://geoapi.qweather.com/v2/city/lookup?location=" + weatherCity + "&key=5ca481c318344e159d8cd32039abb747";
        JSONObject jsonObject = restTemplate.getForObject(url, JSONObject.class);
        System.out.println(jsonObject);
    }
}
