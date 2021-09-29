package com.opzpy123.controller;


import com.opzpy123.model.UserWeather;
import com.opzpy123.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("")
    public String dashboard() {
        return "dashboard";
    }

    /**
     * 主面板
     *
     * @return
     */
    @GetMapping("/dashboard")
    public String dashboardHome() {
        return "dashboardHome";
    }

    /**
     * 天气详情面板
     */
    @GetMapping("/weather")
    public String dashboardWeather(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        dashboardService.getDashboardWeatherInfo(request, session, model, principal);
        return "dashboardWeather";
    }

    /**
     * 网盘面板
     */
    @GetMapping("/netdisc")
    public String dashboardNetdisc(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        return "dashboardNetdisc";
    }


}
