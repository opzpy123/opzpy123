package com.opzpy123.config;

import com.opzpy123.mapper.UserMapper;
import com.opzpy123.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;

/**
 * spring security 配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserMapper userMapper;


    //数据源
    @Autowired
    private DataSource dataSource;



    // 指定密码的加密方式，不然定义认证规则那里会报错
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //创建密码加密对象
    }

    //配置忽略掉的 URL 地址,一般用于js,css,图片等静态资源
    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring() 用来配置忽略掉的 URL 地址，一般用于静态文件
        web.ignoring().antMatchers("/js/**", "/css/**", "/fonts/**", "/images/**", "/lib/**", "/brand/**");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        List<User> users = userMapper.selectList(null);
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authMem = auth.inMemoryAuthentication();
        if(!users.isEmpty()) {
            for (User user : users) {
                authMem.withUser(user.getName()).password(user.getPassword()).roles(user.getRoles().name());
            }
        }
    }

    //(授权)配置 URL 访问权限,对应用户的权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()//允许通过的路径
                .antMatchers("/user/login").permitAll()//允许通过的路径
                .antMatchers("/register").permitAll()//允许通过的路径
                .antMatchers("/user/register").permitAll()//允许通过的路径
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")//跳转登录页面
                .loginProcessingUrl("/user/login")//发送登录请求
                .successForwardUrl("/")//跳转首页
                .failureForwardUrl("/register")//跳转注册页
                .permitAll()
                .and()
                .csrf().disable();

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        //设置数据源
        repository.setDataSource(dataSource);
        //自动建表 第一次启动开启 第二次关闭
        repository.setCreateTableOnStartup(false);

        return repository;
    }
}
