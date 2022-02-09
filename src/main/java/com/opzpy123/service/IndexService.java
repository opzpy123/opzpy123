package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.BlogResp;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexService {


    @Resource
    private UserNetdiscMapper userNetdiscMapper;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private UserWeatherMapper userWeatherMapper;

    @Resource
    private BlogMapper blogMapper;

    public String getIndexInfo(Model model, Principal principal) {
        model.addAttribute("netdiscNum", userNetdiscMapper.getUsersNum());
        model.addAttribute("weatherNum", userWeatherMapper.getUsersNum());
        //首页展示逻辑  管理员可以查看所有人的博客  非管理员只能查看自己和管理员和博客
        List<Blog> blogs;
        if ("admin".equals(principal.getName())) {
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
}
