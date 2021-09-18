package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.spring5.context.SpringContextUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
