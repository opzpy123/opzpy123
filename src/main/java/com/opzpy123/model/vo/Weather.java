package com.opzpy123.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Weather {
    private String city;
    private Integer cityid;
    private String citycode;
    private String date;
    private String week;
    private String weather;
    private String temp;
    private String temphigh;
    private String templow;
    private String img;
    private String humidity;
    private String pressure;
    private String windspeed;
    private String winddirect;
    private String windpower;
    private String updatetime;
    private List<Map<String, String>> index;
    private String aqi;
    private String daily;
    private String hourly;

}
