package com.opzpy123;

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

@Slf4j
@SpringBootTest
class Opzpy123ApplicationTests {

    @Test
    void contextLoads() {

    }


}
