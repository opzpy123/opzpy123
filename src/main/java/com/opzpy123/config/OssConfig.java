package com.opzpy123.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Bean("ossClient")
    public OSS getBean() {
        //设置超时机制和重试机制
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setConnectionTimeout(5000);
        conf.setMaxErrorRetry(3);
        return new OSSClientBuilder().build(PropertiesConfig.ALI_OSS_ENDPOINT, PropertiesConfig.ALI_OSS_ACCESS_KEY_ID,
                PropertiesConfig.ALI_OSS_ACCESS_KEY_SECRET,conf);
    }
}
