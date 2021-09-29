package com.opzpy123.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opzpy123.config.PropertiesConfig;
import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.CaiYunApiResp;
import com.opzpy123.model.vo.WeatherResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class WeatherUtil {

    public static void sendMsg(UserWeather userWeather) {
        try {
            String json = JsonUitl.loadJson(PropertiesConfig.WEATHER_API + userWeather.getWeatherCity());
            WeatherResp r = JSON.parseObject(json, WeatherResp.class);
            CaiYunApiResp caiyunApi = r.getResult();
            String s = "";
            switch (userWeather.getPushType()) {
                case DAILY:
                    s = constructStrDaily(caiyunApi);
                    break;
                case EARLY_WARNING:
                    s = constructStrEarlyWarning(caiyunApi);
                    break;
                case TEMPERATURE_DIFFERENCE:
                    s = constructStrTemperatureDifference(caiyunApi);
                    break;
            }
            String url = userWeather.getBarkId() + userWeather.getPushType() + "-" + userWeather.getWeatherCity() + "/" + s;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            client.execute(request);
        } catch (Exception e) {
            log.error("天气发送失败" + e.getMessage());
        }
    }

    private static String constructStrDaily(CaiYunApiResp weather) {
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
        return s;
    }

    private static String constructStrEarlyWarning(CaiYunApiResp weather) {
        //todo 预警
        return "预警";
    }

    private static String constructStrTemperatureDifference(CaiYunApiResp weather) {
        //todo 温差
        return "温差";
    }

    public String getCityIdByName(String cityName){
        String url = "https://geoapi.qweather.com/v2/city/lookup?location="+cityName+"&key=5ca481c318344e159d8cd32039abb747";
        JSONObject jsonObject = JsonUitl.loadJsonAsJsonObj(url);
        return jsonObject.getJSONArray("location").getJSONObject(0).getString("id");
    }

}
