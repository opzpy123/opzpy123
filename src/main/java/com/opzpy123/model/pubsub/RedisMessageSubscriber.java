package com.opzpy123.model.pubsub;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.MessageVo;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.service.UserWeatherService;
import com.opzpy123.util.HttpUtil;
import com.opzpy123.util.SpringContextUtil;
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

        //将实时消息推送给我的bark
        if (!messageVo.getMessage().endsWith("进入房间") && !messageVo.getMessage().endsWith("已退出聊天")) {
            AuthUserService authUserService = SpringContextUtil.getBean(AuthUserService.class);
            List<AuthUser> userList = authUserService.list(new QueryWrapper<AuthUser>().lambda().eq(AuthUser::getBarkOfflineMessage, 1));
            sendMessageToBark(userList, messageVo);
        }
    }

    private void sendMessageToBark(List<AuthUser> userNames, MessageVo messageVo) {
        log.info("bark消息-{}-{}", messageVo.getMessage(), userNames);
        UserWeatherService userWeatherService = SpringContextUtil.getBean(UserWeatherService.class);

        for (AuthUser authUser : userNames) {
            List<UserWeather> userWeathersByName = userWeatherService.findByUserId(authUser.getId());
            if (!CollectionUtils.isEmpty(userWeathersByName)) {
                String barkId = userWeathersByName.get(0).getBarkId();
                HttpUtil.get(barkId + messageVo.getUserName() + "说:/" + messageVo.getMessage());
            }
        }
    }
}
