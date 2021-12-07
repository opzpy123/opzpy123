package com.opzpy123.controller;

import com.opzpy123.model.pubsub.RedisMessagePublisher;
import com.opzpy123.model.pubsub.RedisMessageSubscriber;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.MessageVo;
import com.opzpy123.service.TalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/talk")
public class TalkController {

    @Resource
    private TalkService talkService;


    /**
     * 进入房间，将自己注册进消息map 广播自己进入房间的消息
     */
    @GetMapping("/enter")
    @ResponseBody
    ApiResponse<String> enter(Principal principal) {
        return talkService.enter(principal);
    }

    /**
     * 指定发送内容的广播消息
     */
    @GetMapping("/send")
    @ResponseBody
    ApiResponse<String> sendMessage(Principal principal, String message) {
        return talkService.sendMessage(principal, message);
    }

    /**
     * 接受自己的消息
     */
    @GetMapping("/receive")
    @ResponseBody
    ApiResponse<List<MessageVo>> getMessage(Principal principal) {
        return talkService.getMessage(principal);
    }

    /**
     * 退出房间，广播退出时的消息 清理消息map
     */
    @GetMapping("/exit")
    @ResponseBody
    ApiResponse<String> exit(Principal principal) {
        return talkService.exit(principal);
    }
}
