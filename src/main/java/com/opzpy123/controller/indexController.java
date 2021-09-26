package com.opzpy123.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

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
     * @return
     */
    @GetMapping("/innerIframe")
    public String innerIframe(HttpServletRequest request,String path) {
        request.setAttribute("jump",path);
        return "innerIframe";
    }



    @GetMapping("/dashboard")
    public String dashboardHome(HttpServletRequest request,String path){
        request.setAttribute("jump",path);
        return "dashboard";
    }

}
