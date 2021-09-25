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
//    @Autowired
//    private SysSystemService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 将 ApplicationContext 转化为 WebApplicationContext
        WebApplicationContext webApplicationContext =
                (WebApplicationContext) contextRefreshedEvent.getApplicationContext();
        // 从 webApplicationContext 中获取  servletContext
        ServletContext servletContext = webApplicationContext.getServletContext();


//        List<SysSystem> sysSystems = service.list(null);
//        SysSystem sysSystem = sysSystems.get(0);
//
        updateSystem(servletContext);
    }

    public static void updateSystem(ServletContext servletContext) {
        log.info("哈哈哈哈哈");
        log.warn("警告1231321");

    }
}
