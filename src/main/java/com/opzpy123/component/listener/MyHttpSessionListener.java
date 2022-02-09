package com.opzpy123.component.listener;


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class MyHttpSessionListener implements HttpSessionListener {

    private static int onlineCount = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        addOnlineCount();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        subOnlineCount();
    }

    //全局的在线人数
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

}