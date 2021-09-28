package com.opzpy123.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class PropertiesConfig implements InitializingBean {


    @Value("${WeatherApi}")
    private String WeatherApi;

    public static String WEATHER_API;

    @Override
    public void afterPropertiesSet() throws Exception {
        WEATHER_API = WeatherApi;
    }
}
