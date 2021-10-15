package com.opzpy123.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JsonUitl {

    public static String loadJson(String url) {
        return loadJsonAsJsonObj(url).toString();
    }

    public static JSONObject loadJsonAsJsonObj(String url) {
        JSONObject jsonObject = null;
        try {
            Response response = HttpUtil.get(url);
            int count = 0;
            while (response.code() != HttpStatus.SC_OK && count < 3) {
                response = HttpUtil.get(url);
                count++;
            }
            if (response.code() == HttpStatus.SC_OK) {
                jsonObject = JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
            } else {
                log.error("Get 请求异常：{}，状态码{}", url, response.code());
            }
        } catch (Exception e) {
            log.error("Get 请求异常：{}，错误信息{}", url, e.getMessage());
        }

//        JSONObject jsonObject = null;
//        try (CloseableHttpClient client = HttpClients.createDefault();) {
//            HttpGet request = new HttpGet(url);
//            request.setProtocolVersion(HttpVersion.HTTP_1_0);
//            request.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
//            try (CloseableHttpResponse response = client.execute(request);) {
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == HttpStatus.SC_OK) {
//                    HttpEntity entity = response.getEntity();
//                    String responseContent = EntityUtils.toString(entity);
//                    jsonObject = JSONObject.parseObject(responseContent);
//                } else {
//                    log.error("Get 请求异常：{}，状态码{}", url, statusCode);
//                }
//            }
//        } catch (IOException e) {
//            log.error("Get 请求异常：{}，状态码{}", url, e.getMessage());
//        }
        return jsonObject;
    }

}
