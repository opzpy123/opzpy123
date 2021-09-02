package com.opzpy123.config;

/**
 * spring security 配置类
 */

import com.opzpy123.service.AuthUserDetailService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@MapperScan(basePackages = "com.opzpy123,mapper")
@ComponentScan(basePackages = {"com.opzpy123"})
//开启注解控制权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthUserDetailService authUserDetailService;

    /**
     * 用户认证配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 指定用户认证时，默认从哪里获取认证用户信息
         */
        auth.userDetailsService(authUserDetailService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/fonts/**", "/images/**", "/lib/**", "/brand/**");
    }


    //(授权)配置 URL 访问权限,对应用户的权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //设置授权
        http.authorizeRequests()
//                .antMatchers("/").hasAnyRole("ADMIN")
                .antMatchers("/login").permitAll()//允许通过的路径
                .antMatchers("/user/login").permitAll()//允许通过的路径
                .antMatchers("/register").permitAll()//允许通过的路径
                .antMatchers("/user/register").permitAll()//允许通过的路径
                .anyRequest().authenticated();

        //设置表单提交
        http.formLogin()
                .loginPage("/login")//跳转登录页面
                .loginProcessingUrl("/user/login")//发送登录请求
                .successForwardUrl("/")//跳转首页
                .failureForwardUrl("/register")//跳转注册页
                .permitAll()
                .and()
                .csrf().disable();


        http.sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login")
                .sessionRegistry(sessionRegistry());

        http.logout();

    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /**
         * BCryptPasswordEncoder：相同的密码明文每次生成的密文都不同，安全性更高
         */
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册bean sessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
