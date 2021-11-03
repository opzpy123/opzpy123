package com.opzpy123.service;

import com.opzpy123.model.pubsub.RedisMessagePublisher;
import com.opzpy123.model.pubsub.RedisMessageSubscriber;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TalkService {

    @Resource
    private RedisMessagePublisher redisMessagePublisher;

    public ApiResponse<String> enter(Principal principal) {
        log.info(principal.getName() + "进入房间");
        //需要把自己注册进map
        RedisMessageSubscriber.messageMap.putIfAbsent(principal.getName(), new ArrayList<>());

        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(principal.getName() + "-进入房间");
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName(principal.getName());

        redisMessagePublisher.publish(messageVo);

        return ApiResponse.ofSuccess();
    }

    public ApiResponse<String> sendMessage(Principal principal, String message) {

        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(message);
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName(principal.getName());

        redisMessagePublisher.publish(messageVo);
        log.info(principal.getName() + "发送消息" + message);
        return ApiResponse.ofSuccess();
    }

    public ApiResponse<List<MessageVo>> getMessage(Principal principal) {

        List<MessageVo> res = new ArrayList<>();
        if (RedisMessageSubscriber.messageMap.get(principal.getName()) != null)
            res = RedisMessageSubscriber.messageMap.get(principal.getName());
        if (res.size() >= 5)
            res = res.stream().skip(res.size() - 5).collect(Collectors.toList());

//        log.info(principal.getName() + "获取消息" + res.stream().map(MessageVo::getMessage).collect(Collectors.toList()));
        return ApiResponse.ofSuccess(res);
    }

    public ApiResponse<String> exit(Principal principal) {
        log.info(principal.getName() + "退出房间");

        RedisMessageSubscriber.messageMap.remove(principal.getName());

        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(principal.getName() + "已退出聊天");
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName(principal.getName());

        redisMessagePublisher.publish(messageVo);

        return ApiResponse.ofSuccess();
    }
}
