package com.opzpy123.controller;


import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserWeather;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.service.DashboardService;
import com.opzpy123.service.LogService;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @Resource
    private LogService logService;

    @Resource
    private AuthUserService userService;

    @Resource
    private SessionRegistry sessionRegistry;

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
        List<AuthUser> authUserList = new ArrayList<>();
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            System.out.println(principal);
            User user = (User)principal;
            AuthUser authUser = userService.getUserByUsername(user.getUsername());
            authUserList.add(authUser);
        }
        System.out.println(authUserList);
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
    public String dashboardNetdisc() {
        return "dashboardNetdisc";
    }

    /**
     * 日志面板
     */
    @GetMapping("/log")
    public String dashboardLog(Model model) {
        model.addAttribute("sysLog",logService.getLog());
        return "dashboardLog";
    }

}
