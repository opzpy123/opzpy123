package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.config.SecurityConfig;
import com.opzpy123.constant.enums.Status;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.vo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@Service
public class AuthUserService extends ServiceImpl<AuthUserMapper, AuthUser> {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private SecurityConfig securityConfig;

    public String register(AuthUser user, HttpServletRequest request) {

        AuthUser saveUser = authUserMapper.selectOne(new QueryWrapper<AuthUser>().lambda()
                .eq(AuthUser::getUsername, user.getUsername()));
        if (null != saveUser) {
            //同名 不能创建
            request.getSession().setAttribute("registerValide", "该用户已存在");
            return "redirect:/register";
        }

        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        authUserMapper.insert(user);
        request.getSession().removeAttribute("registerValide");
        //注册成功返回登录
        log.info("注册成功->{}", user);
        return "redirect:/login";
    }


    public AuthUser getUserByUsername(String username) {
        return getOne(new QueryWrapper<AuthUser>().lambda().eq(AuthUser::getUsername, username));
    }

    public ApiResponse<String> updateOfflineMessageState(Integer state, Principal principal) {
        AuthUser userByUsername = getUserByUsername(principal.getName());
        userByUsername.setBarkOfflineMessage(state);
        baseMapper.updateById(userByUsername);
        return ApiResponse.ofSuccess();
    }
}
