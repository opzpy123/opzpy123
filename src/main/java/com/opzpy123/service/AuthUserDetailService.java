package com.opzpy123.service;

import com.opzpy123.model.AuthUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 提供校验用户是否存在
 */
@Component
public class AuthUserDetailService implements UserDetailsService {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 根据用户名获取认证用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

        //查询用户名是否存在
        AuthUser authUser = authUserService.getUserByUsername(username);
        if (authUser == null) {
            throw new BadCredentialsException("用户不存在");
        }
        //返回账号密码权限
        return new org.springframework.security.core.userdetails
                .User(authUser.getUsername(), authUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(authUser.getRoles().name()));
    }
}
