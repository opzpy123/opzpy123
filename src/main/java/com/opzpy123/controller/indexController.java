package com.opzpy123.controller;


import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.service.BarkWeatherService;
import com.opzpy123.service.UserWeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;


@Controller
public class indexController {

    @Resource
    private UserNetdiscMapper userNetdiscMapper;

    @Resource
    private UserWeatherMapper userWeatherMapper;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("netdiscNum",userNetdiscMapper.getUsersNum());
        model.addAttribute("weatherNum",userWeatherMapper.getUsersNum());
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
