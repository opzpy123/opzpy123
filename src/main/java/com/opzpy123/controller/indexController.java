package com.opzpy123.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.component.listener.MyHttpSessionListener;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.BlogResp;
import com.opzpy123.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;


@Controller
public class indexController {

    @Resource
    private IndexService indexService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        return indexService.getIndexInfo(model, principal);
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
