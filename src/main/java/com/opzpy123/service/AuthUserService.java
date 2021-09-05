package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.config.SecurityConfig;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.model.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Action;


@Slf4j
@Service
public class AuthUserService extends ServiceImpl<AuthUserMapper, AuthUser> {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private SecurityConfig securityConfig;

    public String register(AuthUser user, HttpServletRequest request, HttpServletResponse response) {

        AuthUser saveUser = authUserMapper.selectOne(new QueryWrapper<AuthUser>().lambda()
                .eq(AuthUser::getUsername, user.getUsername()));
        if (null != saveUser) {
            //同名 不能创建
            return "userRegister";
        }

        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        authUserMapper.insert(user);
        //注册成功返回登录
        return "userLogin";
    }



    public AuthUser getUserByUsername(String username) {
        return getOne(new QueryWrapper<AuthUser>().lambda().eq(AuthUser::getUsername, username));
    }
}
