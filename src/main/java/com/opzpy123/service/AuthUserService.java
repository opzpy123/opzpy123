package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.model.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Service
public class AuthUserService extends ServiceImpl<AuthUserMapper, AuthUser> {

    @Resource
    private AuthUserMapper authUserMapper;



    public String register(AuthUser user, HttpServletRequest request, HttpServletResponse response) {

        AuthUser saveUser = authUserMapper.selectOne(new QueryWrapper<AuthUser>().lambda()
                .eq(AuthUser::getName, user.getName()));
        if (null != saveUser) {
            //同名 不能创建
            return "userRegister";
        }
        authUserMapper.insert(user);
        //注册成功返回登录
        return "userLogin";
    }

    public String login(AuthUser user, HttpServletRequest request, HttpServletResponse response) {
        AuthUser dbUser = authUserMapper.selectOne(new QueryWrapper<AuthUser>().lambda().eq(AuthUser::getName, user.getName()));
        if (dbUser == null) {
            //登陆失败 用户不存在
            return "userLogin";
        } else {
            //用户存在 密码正确 跳转主页 (勾选 记住我 会自动保存token)
            return "index";
        }
    }

    public AuthUser getUserByUsername(String username) {
        return getOne(new QueryWrapper<AuthUser>().lambda().eq(AuthUser::getName, username));
    }
}
