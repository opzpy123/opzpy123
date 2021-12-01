package com.opzpy123.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.BlogResp;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.service.BarkWeatherService;
import com.opzpy123.service.BlogService;
import com.opzpy123.service.UserWeatherService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
        //首页展示逻辑  管理员可以查看所有人的博客  非管理员只能查看自己和管理员和博客
        List<Blog> blogs;
        if (principal.getName().equals("admin")) {
            blogs = blogMapper.selectList(new QueryWrapper<Blog>().lambda()
                    .orderByDesc(Blog::getSort)
                    .orderByAsc(Blog::getCreateTime));
        } else {
            blogs = blogMapper.selectList(new QueryWrapper<Blog>().lambda()
                    .eq(Blog::getUserName, principal.getName()).or().eq(Blog::getUserName, "admin")
                    .orderByDesc(Blog::getSort)
                    .orderByAsc(Blog::getCreateTime));
        }

        List<BlogResp> blogResps = blogs.stream().map(blog -> {
            BlogResp blogResp = new BlogResp();
            blogResp.fromBlog(blog);
            return blogResp;
        }).collect(Collectors.toList());

        model.addAttribute("blogs", blogResps);
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
