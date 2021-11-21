package com.opzpy123.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/user")
public class AuthUserController {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private BlogMapper blogMapper;


    @GetMapping("/center")
    public String center(Model model, Principal principal) {
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        if (loginUser.getEmail() == null) loginUser.setEmail("");
        if (loginUser.getPhone() == null) loginUser.setPhone("");
        model.addAttribute("loginUser", loginUser);
        List<Blog> blogs;
        if (principal.getName().equals("admin")) {
            blogs = blogMapper.selectList(null);
        } else {
            blogs = blogMapper.selectList(new QueryWrapper<Blog>().lambda().eq(Blog::getUserName, principal.getName()));
        }
        model.addAttribute("blogs", blogs);
        return "userCenter";
    }

    @ResponseBody
    @PutMapping("/center")
    public ApiResponse<AuthUser> changeUserInfo(@RequestBody AuthUser user) {
        authUserService.updateById(user);
        log.info(user.getUsername() + "修改了个人信息" + user);
        return ApiResponse.ofSuccess(user);
    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public String formRegister(AuthUser user, HttpServletRequest request) {
        log.info(user.getUsername() + "注册了" + user);
        return authUserService.register(user, request);
    }

    /**
     * 开关离线消息推送
     */
    @GetMapping("/offline")
    @ResponseBody
    public ApiResponse<String> updateOfflineMessageState(Principal principal, Integer state) {
        log.info(principal.getName() + (state == 1 ? "开启" : "关闭") + "了离线消息推送");
        return authUserService.updateOfflineMessageState(state, principal);
    }

}
