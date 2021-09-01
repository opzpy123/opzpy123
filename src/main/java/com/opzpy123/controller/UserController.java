package com.opzpy123.controller;

import com.opzpy123.model.User;
import com.opzpy123.service.UserService;
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
     * 跳转登录
     */
    @GetMapping("/login")
    public String login() {
        return "userLogin";
    }

    /**
     * 用户登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public String login(User user, HttpServletRequest request, HttpServletResponse response) {
        //清除cookie中的token
        //数据库中查对象
        //存在 则登录 生成token
        //不存在 则提示注册
        //token登录逻辑

        return "userLogin";
    }

    /**
     * 跳转注册
     */
    @GetMapping("/register")
    public String register() {
        return "userRegister";
    }

    /**
     * 用户注册
     * @param user
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/register")
    public String register(User user, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(userService.register(user)+"??");
        return "userRegister";
    }
}
