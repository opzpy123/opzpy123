package com.opzpy123.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

@Configuration
@Slf4j
public class SystemConfig implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 将 ApplicationContext 转化为 WebApplicationContext
        WebApplicationContext webApplicationContext =
                (WebApplicationContext) contextRefreshedEvent.getApplicationContext();
        // 从 webApplicationContext 中获取  servletContext
        ServletContext servletContext = webApplicationContext.getServletContext();

        updateSystem(servletContext);
    }

    public static void updateSystem(ServletContext servletContext) {
        //配置全局参数
        //servletContext.setAttribute();
    }
}
