package com.opzpy123;

import com.opzpy123.model.vo.WeatherTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

@Slf4j
@SpringBootTest
class ScheduledTests {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture<?> future;

    @Test
    void contextLoads() {

    }
    @Test
    public void execute() {
        WeatherTaskVo taskVo = new WeatherTaskVo("44esT6oeg4FZZzh8ce6sjf", "南京");
        threadPoolTaskScheduler.schedule(taskVo, new CronTrigger("5 * * * * ? "));
    }

}
