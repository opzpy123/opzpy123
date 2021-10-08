package com.opzpy123.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class JsonUitl {

    private static RequestConfig requestConfig;

    public  static String loadJson(String url)  {
        JSONObject jsonObject = null;
        try (CloseableHttpClient client = HttpClients.createDefault();) {
            HttpGet request = new HttpGet(url);
            request.setConfig(requestConfig);
            try (CloseableHttpResponse response = client.execute(request);) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    String responseContent = EntityUtils.toString(entity);
                    jsonObject = JSONObject.parseObject(responseContent);
                } else {
                    log.error("Get 请求异常：{}，状态码{}" , url , statusCode);
                }
            }
        } catch (IOException e) {
            log.error("Get 请求异常：{}，状态码{}" , url , e.getMessage());
        }
        return jsonObject.toString();
    }
    public  static JSONObject loadJsonAsJsonObj(String url) {
        JSONObject jsonObject = null;
        try (CloseableHttpClient client = HttpClients.createDefault();) {
            HttpGet request = new HttpGet(url);
            request.setConfig(requestConfig);
            try (CloseableHttpResponse response = client.execute(request);) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    String responseContent = EntityUtils.toString(entity);
                    jsonObject = JSONObject.parseObject(responseContent);
                } else {
                    log.error("Get 请求异常：{}，状态码{}" , url , statusCode);
                }
            }
        } catch (IOException e) {
            log.error("Get 请求异常：{}，状态码{}" , url , e.getMessage());
        }
        return jsonObject;
    }

}
