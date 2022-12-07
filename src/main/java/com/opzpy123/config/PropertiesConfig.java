package com.opzpy123.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig implements InitializingBean {


    @Value("${WeatherApi}")
    private String WeatherApi;

    @Value("${qweatherKey}")
    private String qweatherKey;

    @Value("${qweatherUrl}")
    private String qweatherUrl;

    @Value("${alioss.endpoint}")
    private String aliOssEndpoint;
    @Value("${alioss.bucketName}")
    private String aliOssBucketName;
    @Value("${alioss.accessKeyId}")
    private String aliOssAccessKeyId;
    @Value("${alioss.accesskeySecret}")
    private String aliossAccesskeySecret;
    @Value("${chatGPT.email}")
    private String chatGPTEmail;
    @Value("${chatGPT.password}")
    private String chatGPTPassword;
    @Value("${chatGPT.session_token}")
    private String chatGPTSessionToken;


    public static String WEATHER_API;
    public static String QWEATHER_KEY;
    public static String QWEATHER_URL;
    public static String ALI_OSS_ENDPOINT;
    public static String ALI_OSS_BUCKET_NAME;
    public static String ALI_OSS_ACCESS_KEY_ID;
    public static String ALI_OSS_ACCESS_KEY_SECRET;
    public static String CHAT_GPT_EMAIL;
    public static String CHAT_GPT_PASSWORD;
    public static String CHAT_GPT_SESSION_TOKEN;

    @Override
    public void afterPropertiesSet() throws Exception {
        WEATHER_API = WeatherApi;
        QWEATHER_KEY = qweatherKey;
        QWEATHER_URL = qweatherUrl;
        ALI_OSS_ENDPOINT = aliOssEndpoint;
        ALI_OSS_BUCKET_NAME = aliOssBucketName;
        ALI_OSS_ACCESS_KEY_ID = aliOssAccessKeyId;
        ALI_OSS_ACCESS_KEY_SECRET = aliossAccesskeySecret;
        CHAT_GPT_EMAIL = chatGPTEmail;
        CHAT_GPT_PASSWORD = chatGPTPassword;
        CHAT_GPT_SESSION_TOKEN = chatGPTSessionToken;
    }
}
