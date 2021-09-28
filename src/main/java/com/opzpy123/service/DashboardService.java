package com.opzpy123.service;

import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserWeather;
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


    public void getDashboardWeatherInfo(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
        List<UserWeather> userWeathers = userWeatherService.findByUserId(loginUser.getId());
        model.addAttribute("userWeathers", userWeathers);

    }


    public void addDashboardWeather(UserWeather userWeather) {
        System.out.println(userWeather);
        userWeatherService.save(userWeather);
    }


    public void addDashboardWeatherRouter(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        AuthUser loginUser = authUserService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
    }
}
