package com.opzpy123.config;

/**
 * spring security 配置类
 */


import com.opzpy123.service.AuthUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthUserDetailService myAuthUserDetailService;

    @Autowired
    private DataSource dataSource;


    /**
     * 用户认证配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 指定用户认证时，默认从哪里获取认证用户信息
         */
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("opzpy123")).roles("ADMIN");
//        auth.userDetailsService(myAuthUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/fonts/**", "/images/**", "/lib/**", "/brand/**");
    }


    //(授权)配置 URL 访问权限,对应用户的权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许同源iframe请求
        http.headers().frameOptions().sameOrigin();

        //设置表单提交
        http.formLogin()
                .loginPage("/login")//跳转登录页面
                .loginProcessingUrl("/user/login")//发送登录请求
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .csrf().disable();

        //设置授权
        http.authorizeRequests()
                .antMatchers("/login").permitAll()//允许通过的路径
                .antMatchers("/user/login").permitAll()//允许通过的路径
                .antMatchers("/register").permitAll()//允许通过的路径
                .antMatchers("/user/register").permitAll()//允许通过的路径
                .anyRequest().authenticated();

        //对记住我进行设置
        http.rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 24) //设置Token的有效时间
                .userDetailsService(myAuthUserDetailService);    //使用userDetailsService用Token从数据库中获取用户自动登录


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

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false);  //系统在启动的时候生成“记住我”的数据表（只能使用一次）
        return tokenRepository;
    }


}
