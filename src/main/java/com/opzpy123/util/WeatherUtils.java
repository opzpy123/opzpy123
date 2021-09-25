package com.opzpy123.util;

import com.alibaba.fastjson.JSON;
import com.opzpy123.config.PropertiesConfig;
import com.opzpy123.model.vo.Weather;
import com.opzpy123.model.vo.WeatherResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
public class WeatherUtils {

    public static void sendMsg(String id, String place) {
        try {
            String json = JsonUitl.loadJson(PropertiesConfig.WEATHER_API + place);
            WeatherResp r = JSON.parseObject(json, WeatherResp.class);
            Weather weather = r.getResult();

            String details = weather.getIndex().get(6).get("detail")
                    .replace("年老体弱者", "")
                    .replace("体弱者", "")
                    .replace("年老者", "")
                    .replace("等服装", "")
                    .replace("应适当添加衣物", "")
                    .replace("宜着", "")
                    .replace("，。", "。")
                    .replace("。，", "。");
            boolean hasRain = weather.getHourly().contains("雨");
            String windspeed = "";
            int wind = Integer.parseInt(weather.getWindspeed().substring(0, weather.getWindspeed().indexOf(".")));
            if (wind < 3) {
                windspeed = "，无风";
            } else if (wind < 5) {
                windspeed = "，微风";
            } else if (wind < 8) {
                windspeed = "，和风";
            } else if (wind < 10) {
                windspeed = "，强风";
            } else if (wind < 15) {
                windspeed = "，狂风";
            } else {
                windspeed = "，飓风";
            }
            //构建生成数据
            String s = weather.getCity() + ":" + weather.getWeather()
                    + weather.getTemplow() + "~" + weather.getTemphigh() + "℃" + windspeed + "%0a"
                    + details
                    + (hasRain ? "%0a今天有雨!" : "%0a今日无雨。");
            String url = PropertiesConfig.BARK_API + "/" + id + "/" + "天气通知-opzpy" + "/" + s;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            client.execute(request);
        } catch (Exception e) {
            log.error("天气发送失败" + e.getMessage());
        }
    }
}
