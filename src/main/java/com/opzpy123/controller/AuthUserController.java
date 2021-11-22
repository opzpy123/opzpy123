package com.opzpy123.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.util.OssUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


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
        //管理页面逻辑  管理员可以查看所有博客 ，非管理员只能查看自己的博客
        if (principal.getName().equals("admin")) {
            blogs = blogMapper.selectList(new QueryWrapper<Blog>()
                    .lambda().orderByDesc(Blog::getSort)
                    .orderByAsc(Blog::getCreateTime));
        } else {
            blogs = blogMapper.selectList(new QueryWrapper<Blog>()
                    .lambda().eq(Blog::getUserName, principal.getName())
                    .orderByDesc(Blog::getSort)
                    .orderByAsc(Blog::getCreateTime));
        }
        model.addAttribute("blogs", blogs);
        return "userCenter";
    }

    @ResponseBody
    @PutMapping("/center")
    public ApiResponse<AuthUser> changeUserInfo(@RequestBody AuthUser user,Principal principal) {
        authUserService.updateById(user);
        log.info(principal.getName() + "修改了个人信息" + user);
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

    /**
     *  上传头像
     */

    @SneakyThrows
    @ResponseBody
    @PostMapping("/avatar")
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> uploadAvatar(MultipartFile file, Principal principal) {
        OssUtils ossUtils = new OssUtils();
        String url = ossUtils.upload(file.getInputStream(), "avatar/"+file.getOriginalFilename());
        AuthUser authUser = authUserService.getUserByUsername(principal.getName());
        authUser.setAvatar(url);
        authUserService.updateById(authUser);
        return ApiResponse.ofSuccess();
    }


}
