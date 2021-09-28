package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserWeather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserWeatherService extends ServiceImpl<UserWeatherMapper, UserWeather> {

    @Resource
    private UserWeatherMapper userWeatherMapper;

    public List<UserWeather> findByUserId(Long userId) {
        return userWeatherMapper.selectList(new QueryWrapper<UserWeather>().lambda()
                .eq(UserWeather::getUserId, userId));
    }

}
