package com.opzpy123.controller;

import com.opzpy123.model.UserWeather;
import com.opzpy123.service.DashboardService;
import com.opzpy123.service.UserWeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * 天气推送
 */
@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Resource
    private DashboardService dashboardService;

    @Resource
    private UserWeatherService userWeatherService;

    /**
     * 创建天气路由
     */
    @GetMapping("/create")
    public String dashboardCreateWeatherRouter(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        dashboardService.addDashboardWeatherRouter(request, session, model, principal);
        return "dashboardCreateWeather";
    }

    /**
     * 创建天气接口
     */
    @PostMapping("/create")
    public String dashboardCreateWeather(UserWeather userWeather) {
        userWeatherService.addUserWeather(userWeather);
        return "redirect:/dashboard/weather";
    }
}
