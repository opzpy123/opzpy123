package com.opzpy123.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opzpy123.config.PropertiesConfig;
import com.opzpy123.constant.enums.WeatherEnum;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.CaiYunApiResp;
import com.opzpy123.model.vo.WeatherResp;
import com.opzpy123.util.JsonUitl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发送天气服务信息
 */
@Slf4j
@Service
public class BarkWeatherService {

    //注册为bean 使用上下文获取
    @Bean("BarkWeatherService")
    public BarkWeatherService barkWeatherService() {
        return this;
    }

    ;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void sendMsgDaily(UserWeather userWeather) {
        try {
            String cityId = getCityIdByName(userWeather.getWeatherCity());
            CloseableHttpClient client = HttpClients.createDefault();
            StringBuilder barkMsg;
            GregorianCalendar ca = new GregorianCalendar();
            ca.setTime(new Date());
            String weatherUrl = PropertiesConfig.QWEATHER_URL + "/weather/3d?location=" + cityId + "&key=" + PropertiesConfig.QWEATHER_KEY;
            String indicesUrl = PropertiesConfig.QWEATHER_URL + "/indices/1d?type=3&location=" + cityId + "&key=" + PropertiesConfig.QWEATHER_KEY;
            if (ca.get(GregorianCalendar.AM_PM) == Calendar.AM) {
                //7点发送早报
                JSONObject weatherJson = JsonUitl.loadJsonAsJsonObj(weatherUrl).getJSONArray("daily").getJSONObject(0);
                JSONObject indicesJson = JsonUitl.loadJsonAsJsonObj(indicesUrl).getJSONArray("daily").getJSONObject(0);

                barkMsg = new StringBuilder().append(userWeather.getBarkId()).append("天气早报-")
                        .append(userWeather.getWeatherCity()).append("/").append(weatherJson.getString("textDay"))
                        .append(weatherJson.getString("tempMin")).append("~")
                        .append(weatherJson.getString("tempMax"))
                        .append(windScaleToString(weatherJson.getString("windScaleDay")))
                        .append("%0a").append(indicesJson.getString("text"));
            } else {
                //19点发送今晚天气以及第二天的预报
                JSONObject weatherJson = JsonUitl.loadJsonAsJsonObj(weatherUrl).getJSONArray("daily").getJSONObject(0);

                barkMsg = new StringBuilder().append(userWeather.getBarkId()).append("天气晚报-")
                        .append(userWeather.getWeatherCity()).append("/").append("今晚:")
                        .append(weatherJson.getString("textNight"))
                        .append(weatherJson.getString("tempMin")).append("~")
                        .append(weatherJson.getString("tempMax"))
                        .append(windScaleToString(weatherJson.getString("windScaleNight")))
                        .append("%0a");

                JSONObject weatherJsonTomorrow = JsonUitl.loadJsonAsJsonObj(weatherUrl).getJSONArray("daily").getJSONObject(1);
                barkMsg.append("明日:").append(weatherJsonTomorrow.getString("textDay"))
                        .append(weatherJsonTomorrow.getString("tempMin")).append("~")
                        .append(weatherJsonTomorrow.getString("tempMax"))
                        .append(windScaleToString(weatherJsonTomorrow.getString("windScaleDay")))
                        .append("%0a");
            }
            HttpGet request = new HttpGet(barkMsg.toString());
            client.execute(request);
            log.info("日报推送成功->{}", userWeather);
        } catch (Exception e) {
            log.error("日报推送失败->{}::{}", e.getMessage(),userWeather);
        }
    }

    public void sendMsgEarlyWarning(UserWeather userWeather) {
        try {
            String cityId = getCityIdByName(userWeather.getWeatherCity());
            String url = PropertiesConfig.QWEATHER_URL + "warning/now?location="
                    + cityId + "&key=" + PropertiesConfig.QWEATHER_KEY;
            JSONObject jsonObject = JsonUitl.loadJsonAsJsonObj(url);
            if (jsonObject.containsKey("warning") && jsonObject.getJSONArray("warning").size() != 0) {
                JSONArray warnings = jsonObject.getJSONArray("warning");
                for (int i = 0; i < warnings.size(); i++) {
                    JSONObject warning = warnings.getJSONObject(i);
                    //预警id 唯一标识
                    String warningId = warning.getString("id");
                    //redis 判断是否和上次的有变化 如果无变化 则不做处理 如果有变化则推送 并重新存放id
                    String redisKey = "EarlyWarning:" + userWeather.getId();
                    //同时可能会存在多个预警
                    List<Object> range = redisTemplate.opsForList().range(redisKey, 0, -1);
                    if (range == null || !range.contains(warningId)) {
                        redisTemplate.opsForList().leftPush(redisKey, warningId);
                        String title = userWeather.getWeatherCity() + ":" + warning.getString("typeName")
                                + warning.getString("level") + "预警";
                        String barkMsg = userWeather.getBarkId() + title + "-" + warning.getString("status") + "/"
                                + warning.getString("text");
                        CloseableHttpClient client = HttpClients.createDefault();
                        HttpGet request = new HttpGet(barkMsg);
                        client.execute(request);
                        log.info("预警推送成功->{}", userWeather);
                    } else {
                        log.info("预警未更新->{}", userWeather);
                    }
                }
            } else {
                log.info("无预警信息->{}", userWeather);
            }
        } catch (Exception e) {
            log.error("预警推送失败->{}::{}",e.getMessage(), userWeather );
        }
    }

    public void sendMsgTemperatureDifference(UserWeather userWeather) {
        String cityId = getCityIdByName(userWeather.getWeatherCity());
    }

    public String getCityIdByName(String cityName) {
        String url = "https://geoapi.qweather.com/v2/city/lookup?location=" + cityName + "&key=" + PropertiesConfig.QWEATHER_KEY;
        JSONObject jsonObject = JsonUitl.loadJsonAsJsonObj(url);
        return jsonObject.getJSONArray("location").getJSONObject(0).getString("id");
    }


    /**
     * (旧)使用聚合数据做推送
     *
     * @param userWeather
     */
    @Deprecated
    public void sendMsg(UserWeather userWeather) {
        try {
            String json = JsonUitl.loadJson(PropertiesConfig.WEATHER_API + userWeather.getWeatherCity());
            WeatherResp r = JSON.parseObject(json, WeatherResp.class);
            CaiYunApiResp caiyunApi = r.getResult();
            String s = constructStrDaily(caiyunApi);
            String url = userWeather.getBarkId() + userWeather.getPushType() + "-" + userWeather.getWeatherCity() + "/" + s;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            client.execute(request);
        } catch (Exception e) {
            log.error("天气发送失败::{}", e.getMessage());
        }
    }

    private String constructStrDaily(CaiYunApiResp weather) {
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
        int wind = Integer.parseInt(weather.getWindspeed().substring(0, weather.getWindspeed().indexOf(".")));
        String windSpeed = windLevelToString(wind);
        //构建生成数据
        String s = weather.getCity() + ":" + weather.getWeather()
                + weather.getTemplow() + "~" + weather.getTemphigh() + "℃" + windSpeed + "%0a"
                + details
                + (hasRain ? "%0a今天有雨!" : "%0a今日无雨。");
        return s;
    }

    private String windLevelToString(int wind) {
        String windSpeed;
        if (wind < 3) {
            windSpeed = "，无风";
        } else if (wind < 5) {
            windSpeed = "，微风";
        } else if (wind < 8) {
            windSpeed = "，和风";
        } else if (wind < 10) {
            windSpeed = "，强风";
        } else if (wind < 15) {
            windSpeed = "，狂风";
        } else {
            windSpeed = "，飓风";
        }
        return windSpeed;
    }

    private String windScaleToString(String scale) {
        String[] windScaleDays = scale.split("-");
        String windScaleMin = windLevelToString(Integer.parseInt(windScaleDays[0]));
        String windScaleMax = windLevelToString(Integer.parseInt(windScaleDays[1]));
        if (windScaleMin.equals(windScaleMax)) {
            return windScaleMax;
        } else {
            return windScaleMin + "至" + windScaleMax;
        }
    }
}
