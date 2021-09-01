package com.opzpy123;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.opzpy123.mapper")
public class Opzpy123Application {

    public static void main(String[] args) {
        SpringApplication.run(Opzpy123Application.class, args);
    }

}
