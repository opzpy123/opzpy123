package com.opzpy123.model.pubsub;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opzpy123.model.vo.MessageVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {

    public static HashMap<String, List<MessageVo>> messageMap = new HashMap<>();


    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        String s = message.toString();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') {
                s = (String) s.subSequence(i + 1, s.length() - 1);
                break;
            }
        }

        MessageVo messageVo = JSONObject.parseObject(s, MessageVo.class);
        //收到消息时 按名字存放到keyEntry每个人的List中
        //进入聊天室 则将自己的名字注册进messageMap中
        //推出聊天室 则剔除messageMap
        for (String key : messageMap.keySet()) {
            List<MessageVo> messageVos = messageMap.get(key);
            messageVos.add(messageVo);
        }

    }
}
