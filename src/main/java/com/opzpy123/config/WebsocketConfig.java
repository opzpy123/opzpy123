package com.opzpy123.config;

import com.opzpy123.component.websocket.MyWebSocketHandler;
import com.opzpy123.component.websocket.MyWebSocketInterceptor;
import com.opzpy123.util.IpAndPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Slf4j
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //1.注册WebSocket
        //设置websocket的地址
        String websocket_url = "/ws/opzpy";
        //注册Handler
        registry.addHandler(getMyWebSocketHandler(), websocket_url).
                //注册Interceptor
                        addInterceptors(getMyWebSocketInterceptor())
                //配置*代表允许所有的ip进行调用
                .setAllowedOrigins("*");

        //2.注册SockJS，提供SockJS支持(主要是兼容ie8)
        //设置sockjs的地址
        String sockjs_url = "/sockjs/opzpy";
        //注册Handler
        registry.addHandler(getMyWebSocketHandler(), sockjs_url).
                //注册Interceptor
                        addInterceptors(getMyWebSocketInterceptor())
                //配置*代表允许所有的ip进行调用
                .setAllowedOrigins("*").withSockJS();

        //获取系统ip
        MyWebSocketHandler.ipAndPort = IpAndPortUtil.getIpAddressAndPort();
    }

    @Bean
    public MyWebSocketHandler getMyWebSocketHandler() {
        return new MyWebSocketHandler();
    }

    @Bean
    public MyWebSocketInterceptor getMyWebSocketInterceptor() {
        return new MyWebSocketInterceptor();
    }

}