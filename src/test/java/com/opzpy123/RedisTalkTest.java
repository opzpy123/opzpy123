package com.opzpy123;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opzpy123.model.pubsub.RedisMessagePublisher;
import com.opzpy123.model.pubsub.RedisMessageSubscriber;
import com.opzpy123.model.vo.MessageVo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.codec.json.Jackson2JsonDecoder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 使用redis 制作聊天室
 */
@SpringBootTest
public class RedisTalkTest {

    @Resource
    private RedisMessagePublisher redisMessagePublisher;


    @Test
    @SneakyThrows
    void test() {

        MessageVo messageVo = new MessageVo();
        messageVo.setMessage("hello everybody");
        messageVo.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        messageVo.setUserName("opzpy123");

        redisMessagePublisher.publish(messageVo);
    }

    @Test
    void serializeTest() throws JsonProcessingException {
        MessageVo messageVo = new ObjectMapper().readValue("{\"message\":\"hello everybody\",\"userName\":\"opzpy123\",\"sendTime\":\"2021-11-02 14:39:28\"}", MessageVo.class);
        System.out.println(messageVo);
    }

}
