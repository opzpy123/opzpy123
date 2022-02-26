package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.BlogResp;
import com.opzpy123.model.vo.SearchVo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private BlogService blogService;

    public String getIndexInfo(Model model, Principal principal) {
        model.addAttribute("netdiscNum", userNetdiscMapper.getUsersNum());
        model.addAttribute("weatherNum", userWeatherMapper.getUsersNum());
        //首页展示逻辑  管理员可以查看所有人的博客  非管理员只能查看自己和管理员和博客
        ApiResponse<List<Blog>> blogList = blogService.getBlogList();
        List<Blog> blogs = blogList.getData();
        if (!"admin".equals(principal.getName())) {
            blogs = blogs.stream()
                    .filter(a -> a.getUserName().equals(principal.getName()) || "admin".equals(a.getUserName()))
                    .collect(Collectors.toList());
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

    public ApiResponse<SearchVo> getSearchResult( String param){
        //todo 启动 注册blogId 如果存在直接返回id 如果不存在 则 查询数据库 or 此接口上redis缓存
        SearchVo searchVo = new SearchVo();
        List<Blog> blogs = blogService.searchBlog(param);
        if(CollectionUtils.isNotEmpty(blogs))searchVo.setBlogIdList(blogs);

        return ApiResponse.ofSuccess(searchVo);
    }
}
