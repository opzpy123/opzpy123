package com.opzpy123.model;


import lombok.Data;


@Data
public class UserWeather extends BaseModel {

    private Long userId;            //用户Id

    private String barkId;          //消息推送(bark)Id

    private String weatherCity;        //通知天气的城市

    private String cornExpression;  //corn表达式

    private Integer enabled;        //是否启用 1启用 0不启用

}
