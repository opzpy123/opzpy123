package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供校验用户是否存在
 */
@Component
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private AuthUserService authUserService;


    /**
     * 根据用户名获取认证用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

//            //将账号登录记录放入缓存
//            if (SystemCache.userCertificationCache.getIfPresent(username) == null) {
//                SystemCache.userCertificationCache.put(username, 1);
//            } else {
//                SystemCache.userCertificationCache.put(username, (int) SystemCache.userCertificationCache.getIfPresent(username) + 1);
//            }
//
//            //判断10分钟内当前IP指定账号登录次数
//            if ((int) SystemCache.userCertificationCache.getIfPresent(username) > 5) {
//                throw new BadCredentialsException("您的登录次数过于频繁 请10分钟后再试");
//            }
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
