package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.mapper.UserMapper;
import com.opzpy123.model.User;
import com.opzpy123.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserMapper usermapper;

    public String register(User user) {
        User saveUser = usermapper.selectOne(new QueryWrapper<User>().lambda()
                .eq(User::getName, user.getName()));
        if (null != saveUser) return "";
        usermapper.insert(user);
        return JwtUtils.getJwtToken("user", user.getId(), new HashMap<>());
    }
}
