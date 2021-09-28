package com.opzpy123.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class indexController {

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
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

    /**
     * 内联页面路径跳转
     *
     * @return
     */
    @GetMapping("/innerIframe")
    public String innerIframe(HttpServletRequest request, String path) {
        request.setAttribute("jump", path);
        return "innerIframe";
    }

}
