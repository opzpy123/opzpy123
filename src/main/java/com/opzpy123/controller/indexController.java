package com.opzpy123.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class indexController {

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response){
        //遍历cookie 拿到token 看是否过期 否则更新过期时间 并跳转主页面
        //如果已过期或者未登录 跳转到登陆页面
        Cookie[] cookies = request.getCookies();
        return "index";
    }

}
