package com.opzpy123.controller;

import com.opzpy123.model.AuthUser;
import com.opzpy123.service.AuthUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/user")
public class AuthUserController {

    @Resource
    private AuthUserService authUserService;


    /**
     * 用户登录
     */
    @GetMapping("/login")
    public String formLogin(AuthUser user, HttpServletRequest request, HttpServletResponse response) {
        return authUserService.login(user, request, response);
    }


    /**
     * 用户注册
     */
    @GetMapping("/register")
    public String formRegister(AuthUser user, HttpServletRequest request, HttpServletResponse response) {
        return authUserService.register(user, request, response);
    }
}
