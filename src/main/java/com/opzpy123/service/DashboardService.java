package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserNetdisc;
import com.opzpy123.model.UserWeather;
import com.opzpy123.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class DashboardService {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private UserWeatherService userWeatherService;

    @Resource
    private UserNetdiscMapper userNetdiscMapper;


    public void getDashboardWeatherInfo(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
        List<UserWeather> userWeathers = userWeatherService.findByUserId(loginUser.getId());
        model.addAttribute("userWeathers", userWeathers);
    }


    public void addDashboardWeatherRouter(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
    }

    public void getDashboardNetdiscInfo(Model model, Principal principal) {
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
        List<UserNetdisc> netdiscFileList = userNetdiscMapper.selectList(new QueryWrapper<UserNetdisc>()
                .lambda().eq(UserNetdisc::getUserId, loginUser.getId()));
        model.addAttribute("netdiscFileList", netdiscFileList);
    }
}
