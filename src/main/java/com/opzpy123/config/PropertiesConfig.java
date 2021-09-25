package com.opzpy123.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class PropertiesConfig implements InitializingBean {

    @Value("${BarkApi}")
    private String BarkApi;

    @Value("${WeatherApi}")
    private String WeatherApi;

    public static String BARK_API;
    public static String WEATHER_API;

    @Override
    public void afterPropertiesSet() throws Exception {
        BARK_API = BarkApi;
        WEATHER_API = WeatherApi;
    }
}
