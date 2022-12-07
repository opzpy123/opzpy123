package com.opzpy123.component.websocket;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opzpy123.model.vo.WSUserModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    /**
     * 先注册一个websocket服务器，将连接上的所有用户放进去
     */
    private static final Hashtable<String, WSUserModel> USER_SOCKET_SESSION_MAP;
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    public static String ipAndPort;

    static {
        USER_SOCKET_SESSION_MAP = new Hashtable<>(12);
    }

    /**
     * 前台连接并且注册了账户
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        session.setTextMessageSizeLimit(2048000);
        session.setBinaryMessageSizeLimit(2048000);
        openConn(session);
    }

    /**
     * 权限校验
     *
     * @param session
     * @return
     */
    private boolean checkPermission(WebSocketSession session) {
        //权限
        WSUserModel ws = new WSUserModel(session);
        String token = ws.getToken();
        return true;
    }


    private void sendMessage(WebSocketSession session, String message) {
        JSONObject resJson = new JSONObject();
        resJson.put("message", message);
        resJson.put("type", "error");
        resJson.put("status", "-10086");
        sendMessageToUser(session, resJson.toJSONString());
    }

    /**
     * 接受消息
     *
     * @param session
     * @param message
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        session.sendMessage(message);
    }

    /**
     * 消息传输错误处理，如果出现错误直接断开连接
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("handleTransportError:{};exception:{}", session, exception);
        if (session.isOpen()) {
            session.close();
        }
        closeConn(session, true);
    }

    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        closeConn(session, false);
    }

    private void openConn(WebSocketSession session) {
        //创建一个连接窗口，并加入的队列中
        WSUserModel ws = new WSUserModel(session);
        WSUserModel.webSocketMapAdd(USER_SOCKET_SESSION_MAP, ws);
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:" + ws.getId() + ",当前在线人数为" + getOnlineCount());
    }

    private void closeConn(WebSocketSession session, boolean isError) {
        WSUserModel wsUserModel = new WSUserModel(session);
        if (isError) {
            log.info("窗口关闭(Error):{},当前在线人数为{}", wsUserModel.getId(), getOnlineCount());
        } else {
            WSUserModel.webSocketMapRemove(USER_SOCKET_SESSION_MAP, wsUserModel);
            subOnlineCount();              //在线数减1
            log.info("窗口关闭:{},当前在线人数为:{}", wsUserModel.getId(), getOnlineCount());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
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


    /**
     * 给单个用户发消息
     *
     * @param session
     * @param message
     */
    private static void sendMessageToUser(WebSocketSession session, WebSocketMessage message) {
        try {
            log.info("sendMessageToUser--WebSocketSession");
            synchronized (session) {
                session.sendMessage(message);
            }
        } catch (Exception ex) {
            log.error(ex + ";WebSocketSession:" + session + "message" + message);
        }
    }

    private static void sendMessageToUser(WebSocketSession session, String message) {
        log.info("sendMessageToUser--onlyForUser");
        sendMessageToUser(session, new TextMessage(message));
    }
}
