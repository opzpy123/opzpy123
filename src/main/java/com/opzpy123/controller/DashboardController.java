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

    /**
     * 控制面板路由
     *
     * @param request
     * @param path
     * @return
     */
    @GetMapping("")
    public String dashboard(HttpServletRequest request, String path) {
        request.setAttribute("jump", path);
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
     * 天气面板
     *
     * @param request
     * @param session
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/weather")
    public String dashboardWeather(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        //查看当前登录用户 是否有天气
        dashboardService.getDashboardWeatherInfo(request, session, model, principal);
        return "dashboardWeather";
    }

    /**
     * 创建天气路由
     *
     * @param request
     * @param session
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/weather/create")
    public String dashboardCreateWeatherRouter(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        dashboardService.addDashboardWeatherRouter(request, session, model, principal);
        return "dashboardCreateWeather";
    }

    /**
     * 创建天气接口
     *
     * @return
     */
    @PostMapping("/weather/create")
    public String dashboardCreateWeather(UserWeather userWeather) {
        dashboardService.addDashboardWeather(userWeather);
        return "redirect:/dashboard/weather";
    }

    /**
     * 网盘服务
     *
     * @param request
     * @param session
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/netdisc")
    public String dashboardNetdisc(HttpServletRequest request, HttpSession session, Model model, Principal principal) {
        return "dashboardNetdisc";
    }


}
