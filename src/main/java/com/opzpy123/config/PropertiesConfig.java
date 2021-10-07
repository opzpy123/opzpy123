package com.opzpy123.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class PropertiesConfig implements InitializingBean {


    @Value("${WeatherApi}")
    private String WeatherApi;

    @Value("${qweatherKey}")
    private String qweatherKey;

    public static String WEATHER_API;
    public static String QWEATHER_KEY;

    @Override
    public void afterPropertiesSet() throws Exception {
        WEATHER_API = WeatherApi;
        QWEATHER_KEY= qweatherKey;
    }
}
