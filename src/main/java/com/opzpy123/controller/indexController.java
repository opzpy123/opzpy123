package com.opzpy123.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.service.BarkWeatherService;
import com.opzpy123.service.BlogService;
import com.opzpy123.service.UserWeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;


@Controller
public class indexController {

    @Resource
    private UserNetdiscMapper userNetdiscMapper;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private UserWeatherMapper userWeatherMapper;

    @Resource
    private BlogMapper blogMapper;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("netdiscNum", userNetdiscMapper.getUsersNum());
        model.addAttribute("weatherNum", userWeatherMapper.getUsersNum());
        List<Blog> blogs = blogMapper.selectList(new QueryWrapper<Blog>().lambda()
                .eq(Blog::getUserName, principal.getName()).or().eq(Blog::getUserName, "admin")
                .orderByDesc(Blog::getCreateTime).last("limit 10"));
        model.addAttribute("blogs", blogs);
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
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
