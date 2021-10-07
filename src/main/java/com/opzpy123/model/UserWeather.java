package com.opzpy123.model;


import com.opzpy123.constant.enums.WeatherEnum;
import lombok.Data;


@Data
public class UserWeather extends BaseModel {

    private String weatherName;     //天气任务名称

    private Long userId;            //用户Id

    private String barkId;          //消息推送(bark)Id

    private String weatherCity;        //通知天气的城市

    private String cronExpression;  //corn表达式

    private Integer enabled;        //是否启用 1启用 0不启用

    private WeatherEnum pushType;   //推送类型 日报/温差/预警

}
