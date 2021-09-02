package com.opzpy123.controller;

import com.opzpy123.model.User;
import com.opzpy123.service.impl.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 用户登录
     */
    @GetMapping("/login")
    public String formLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(user, request, response);
    }


    /**
     * 用户注册
     */
    @GetMapping("/register")
    public String formRegister(User user, HttpServletRequest request, HttpServletResponse response) {
        return userService.register(user, request, response);
    }
}
