package com.opzpy123;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class RestTemplateTest {

    @Resource
    private RestTemplate restTemplate;

    @Test
    void test() {
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("https://www.baidu.com", HttpMethod.GET, null, String.class);

        System.out.println(responseEntity);
    }
}
