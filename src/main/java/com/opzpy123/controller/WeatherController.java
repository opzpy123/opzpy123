package com.opzpy123.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 天气推送
 */
@Controller
@RequestMapping("/weather")
public class WeatherController {

    @PostMapping("/submit")
    public String submitWeatherInfo() {
        return "";
    }
}
