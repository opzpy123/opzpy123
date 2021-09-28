package com.opzpy123.config;

import com.opzpy123.model.vo.WeatherTaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //todo 从数据库中读取 并在项目启动时放进去
//        WeatherTaskVo taskVo = new WeatherTaskVo("44esT6oeg4FZZzh8ce6sjf", "南京");
//        threadPoolTaskScheduler.schedule(taskVo, new CronTrigger("5 * * * * ? "));
    }
}
