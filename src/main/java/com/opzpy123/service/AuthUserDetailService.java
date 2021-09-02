package com.opzpy123.service;

import com.opzpy123.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private AuthUserService authUserService;

    /**
     * 根据用户名获取认证用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("UserDetailsService没有接收到用户账号");
        } else {
            /**
             * 根据用户名查找用户信息
             */
            AuthUser authUser = authUserService.getUserByUsername(username);
            if (authUser == null) {
                throw new UsernameNotFoundException(String.format("用户'%s'不存在", username));
            }
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

            //封装用户信息和角色信息到SecurityContextHolder全局缓存中
            grantedAuthorities.add(new SimpleGrantedAuthority(authUser.getRoles().name()));

            /**
             * 创建一个用于认证的用户对象并返回，包括：用户名，密码，角色
             */
            return new User(authUser.getName(), authUser.getPassword(), grantedAuthorities);
        }
    }
}
