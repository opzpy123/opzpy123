package com.opzpy123.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping("")
    public String dashboard(HttpServletRequest request, String path) {
        request.setAttribute("jump", path);
        return "dashboard";
    }

    @GetMapping("/Dashboard")
    public String dashboardHome(HttpServletRequest request, String path) {
        return "dashboardHome";
    }

    @GetMapping("/Netdisc")
    public String dashboardNetdisc(HttpServletRequest request, String path) {
        return "dashboardNetdisc";
    }

    @GetMapping("/Weather")
    public String dashboardWeather(HttpServletRequest request, String path) {
        return "dashboardWeather";
    }
}
