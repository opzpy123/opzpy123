package com.opzpy123.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opzpy123.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {
    String register(User user, HttpServletRequest request, HttpServletResponse response);

    String login(User user, HttpServletRequest request, HttpServletResponse response);
}
