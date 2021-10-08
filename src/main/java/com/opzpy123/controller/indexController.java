package com.opzpy123.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class indexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 跳转登录
     */
    @GetMapping("/login")
    public String login() {
        return "userLogin";
    }

    /**
     * 跳转注册
     */
    @GetMapping("/register")
    public String register() {
        return "userRegister";
    }

}
