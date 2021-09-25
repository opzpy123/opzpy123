package com.opzpy123;

import com.opzpy123.util.WeatherUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Opzpy123ApplicationTests {

    @Test
    void contextLoads() {
        String z_id = "44esT6oeg4FZZzh8ce6sjf";
        WeatherUtils.sendMsg(z_id,"南京");
    }
}
