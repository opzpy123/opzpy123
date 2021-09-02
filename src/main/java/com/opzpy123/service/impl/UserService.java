package com.opzpy123.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.config.SecurityConfig;
import com.opzpy123.mapper.UserMapper;
import com.opzpy123.model.User;
import com.opzpy123.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements com.opzpy123.service.UserService {

    @Resource
    private UserMapper usermapper;
    @Resource
    private SecurityConfig security;


    public String register(User user, HttpServletRequest request, HttpServletResponse response) {

        User saveUser = usermapper.selectOne(new QueryWrapper<User>().lambda()
                .eq(User::getName, user.getName()));
        if (null != saveUser) {
            //同名 不能创建
            return "userRegister";
        }
        user.setPassword(security.passwordEncoder().encode(user.getPassword()));
        usermapper.insert(user);
        //注册成功返回登录
        return "userLogin";
    }

    public String login(User user, HttpServletRequest request, HttpServletResponse response) {
        User dbUser = usermapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getName, user.getName()));
        if (dbUser == null) {
            //登陆失败 用户不存在
            return "userLogin";
        } else if (!security.passwordEncoder().matches(user.getPassword(), dbUser.getPassword())) {
            //登陆失败 密码不正确
            return "userLogin";
        } else {
            //用户存在 密码正确 跳转主页 (勾选 记住我 会自动保存token)
            return "index";
        }
    }
}
