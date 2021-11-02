package com.opzpy123.controller;

import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.MessageVo;
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
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;



    @GetMapping("/enter")
    @ResponseBody
    ApiResponse<String> enter(Principal principal) throws InterruptedException {
        //进入房间，将自己添加到订阅列表


        //通知其他人

        return ApiResponse.ofSuccess();
    }

    @GetMapping("/send")
    @ResponseBody
    ApiResponse<String> sendMessage(Principal principal,String message) {
        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(message);
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName(principal.getName());
        redisTemplate.convertAndSend("opzpy123", messageVo);
        return ApiResponse.ofSuccess();
    }


    @GetMapping("/exit")
    @ResponseBody
    ApiResponse<String> exit(Principal principal) {
        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(principal.getName() + "已退出聊天");
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName(principal.getName());
        redisTemplate.convertAndSend("opzpy123", messageVo);
        return ApiResponse.ofSuccess();
    }
}
