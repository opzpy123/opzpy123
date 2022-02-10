package com.opzpy123.model.vo;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Hashtable;
import java.util.Optional;

@Data
public class WSUserModel {

    public static final String USER_TOKEN = "t";    // 用户token

    private WebSocketSession ws;// ws-session
    private String id;          // session id
    private String token;       // 接收token
    private String userName;    // 连接的用户名

    public WSUserModel(WebSocketSession ws) {
        this.id = ws.getId();
        this.userName = "user-" + ws.getId();

        this.token = Optional.ofNullable(ws.getAttributes().get(USER_TOKEN)).map(Object::toString).orElse("i");
        this.ws = ws;
    }

    public static void webSocketMapAdd(Hashtable<String,  WSUserModel> maps, WSUserModel wm) {
        maps.put(wm.getId(),wm);
    }

    public static void webSocketMapRemove(Hashtable<String,  WSUserModel> maps, WSUserModel wm) {
        maps.remove(wm.getId());
    }

}
