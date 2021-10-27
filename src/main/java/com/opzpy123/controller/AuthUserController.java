package com.opzpy123.controller;

import com.opzpy123.model.AuthUser;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.AuthUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/user")
public class AuthUserController {

    @Resource
    private AuthUserService authUserService;


    @GetMapping("/center")
    public String center(){
        return "userCenter";
    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public String formRegister(AuthUser user, HttpServletRequest request) {
        return authUserService.register(user, request);
    }
}
