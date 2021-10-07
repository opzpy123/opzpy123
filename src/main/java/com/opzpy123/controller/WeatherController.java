package com.opzpy123.controller;

import com.opzpy123.config.MyApplicationRunner;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.WeatherTaskVo;
import com.opzpy123.service.DashboardService;
import com.opzpy123.service.UserWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

/**
 * 天气推送
 */
@Slf4j
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
    @PostMapping("/weather")
    public String dashboardCreateWeather(UserWeather userWeather) {
        userWeatherService.addUserWeather(userWeather);
        return "redirect:/dashboard/weather";
    }

    /**
     * 删除天气任务
     */
    @ResponseBody
    @DeleteMapping("/weather")
    public ApiResponse<String> dashboardDeleteWeather(Long userWeatherId){
        log.info("任务删除"+userWeatherId);
        return userWeatherService.delUserWeather(userWeatherId);
    }

    @ResponseBody
    @GetMapping("/open")
    public ApiResponse<String> dashboardOpenWeather(Long userWeatherId) {
        log.info("任务开启"+userWeatherId);
        return userWeatherService.openUserWeather(userWeatherId);
    }

    @ResponseBody
    @GetMapping("/stop")
    public ApiResponse<String> dashboardCloseWeather(Long userWeatherId) {
        log.info("任务停止"+userWeatherId);
        return userWeatherService.stopUserWeather(userWeatherId);
    }
}
