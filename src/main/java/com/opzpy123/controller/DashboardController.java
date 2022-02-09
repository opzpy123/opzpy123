package com.opzpy123.controller;


import com.opzpy123.component.listener.MyHttpSessionListener;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.Blog;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

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

    @Resource
    private BlogMapper blogMapper;

    //存放定时任务Future
    @Resource(name = "scheduledFutureMap")
    private ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledFutureMap;

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
    public String dashboardHome(Model model) {
        List<AuthUser> authUserList = new ArrayList<>();
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            User user = (User) principal;
            AuthUser authUser = userService.getUserByUsername(user.getUsername());
            authUserList.add(authUser);
        }

        int weatherTaskSize = scheduledFutureMap.size();
        int onlineUser = MyHttpSessionListener.getOnlineCount();
        model.addAttribute("weatherTaskSize", weatherTaskSize);
        model.addAttribute("onlineUser", onlineUser);

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
    public String dashboardNetdisc(Model model, Principal principal) {
        dashboardService.getDashboardNetdiscInfo(model, principal);
        return "dashboardNetdisc";
    }

    /**
     * 日志面板
     */
    @GetMapping("/log")
    public String dashboardLog(Model model) {
        model.addAttribute("sysLogTodayInfo", logService.getTodayLogInfo());
        model.addAttribute("sysLogAllDebug", logService.getAllLogDebug());
        return "dashboardLog";
    }

    /**
     * 交流
     */
    @GetMapping("/talk")
    public String dashboardTalk(Principal principal, Model model) {
        AuthUser userByUsername = userService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", userByUsername);
        return "dashboardTalk";
    }

    /**
     * 博客
     */
    @GetMapping("/blog")
    public String dashboardBlog(Principal principal, Model model, String blogId) {
        AuthUser userByUsername = userService.getUserByUsername(principal.getName());
        model.addAttribute("loginUser", userByUsername);
        Blog blog = blogMapper.selectById(blogId);
        model.addAttribute("blog", blog);
        return "dashboardBlog";
    }

}
