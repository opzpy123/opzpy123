package com.opzpy123;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@ServletComponentScan
@EnableScheduling
@SpringBootApplication
@MapperScan("com.opzpy123.mapper")
public class Opzpy123Application {

    public static void main(String[] args) {
        SpringApplication.run(Opzpy123Application.class, args);
    }

}
