package com.opzpy123.model.vo;

import lombok.Data;

/**
 * 天气api返回结果
 */
@Data
public class WeatherResp {
    private Integer status;
    private String msg;
    private Weather result;
}
