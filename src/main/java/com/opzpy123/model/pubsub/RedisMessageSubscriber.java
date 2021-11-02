package com.opzpy123.model.pubsub;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opzpy123.model.vo.MessageVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public static HashMap<String, List<MessageVo>> messageMap = new HashMap<>();


    @Override
    @SneakyThrows
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<?> keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
        Object channel = keySerializer.deserialize(message.getChannel());
        Object body = valueSerializer.deserialize(message.getBody());
//        System.out.println("hello 这里来了");
//        MessageVo messageVo = new ObjectMapper().readValue(message.toString(), MessageVo.class);
//        System.out.println(messageVo);
        System.out.println(channel);
        System.out.println(body);


        //收到消息时 按名字存放到keyEntry每个人的List中

        //进入聊天室 则将自己的名字注册进messageMap中

        //推出聊天室 则剔除messageMap
    }
}
