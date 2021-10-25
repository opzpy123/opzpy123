package com.opzpy123.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.http.HttpStatus;


import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JsonUitl {

    public static String loadJson(String url) {
        return loadJsonAsJsonObj(url).toString();
    }

    public static JSONObject loadJsonAsJsonObj(String url) {
        JSONObject jsonObject = null;
        Response response = HttpUtil.get(url);

        int count = 0;
        while ((response == null || response.code() != HttpStatus.SC_OK) && count < 5) {
            response = HttpUtil.get(url);
            count++;
        }
        if (response != null && response.code() == HttpStatus.SC_OK) {
            try {
                jsonObject = JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("Get 请求异常：{}", url);
        }
        return jsonObject;
    }

}
