package com.opzpy123.service;

import com.opzpy123.constant.enums.Status;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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

    private final HashMap<String, String> tempTimeMap = new HashMap<String, String>();

    public ApiResponse<List<MessageVo>> getMessage(Principal principal) {

        List<MessageVo> res = new ArrayList<>();
        if (RedisMessageSubscriber.messageMap.get(principal.getName()) != null)
            res = RedisMessageSubscriber.messageMap.get(principal.getName());
        //只显示过去150条消息
        if (res.size() >= 150)
            res = res.stream().skip(res.size() - 150).collect(Collectors.toList());
        //展示逻辑 未更新返回[] 已更新则返回最近150条
        tempTimeMap.putIfAbsent(principal.getName(), null);
        String tempTime = tempTimeMap.get(principal.getName());
        if (res.size() > 0) {
            if (tempTime == null) {
                tempTime = res.get(res.size() - 1).getSendTime();
                tempTimeMap.put(principal.getName(), tempTime);
            } else if (tempTime.equals(res.get(res.size() - 1).getSendTime())) {
                res = new ArrayList<>();
            } else {
                tempTime = res.get(res.size() - 1).getSendTime();
                tempTimeMap.put(principal.getName(), tempTime);
            }
        } else {
            tempTime = null;
            tempTimeMap.put(principal.getName(), tempTime);
            res = new ArrayList<>();
        }
        return ApiResponse.ofSuccess(res);
    }

    public ApiResponse<String> exit(Principal principal) {
        log.info(principal.getName() + "退出房间");
        //退出房间不清理消息
        //RedisMessageSubscriber.messageMap.remove(principal.getName());

        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(principal.getName() + "已退出聊天");
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName(principal.getName());

        redisMessagePublisher.publish(messageVo);

        return ApiResponse.ofSuccess();
    }
}
