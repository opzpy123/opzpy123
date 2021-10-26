package com.opzpy123.model.vo;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.opzpy123.config.PropertiesConfig;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.model.UserNetdisc;
import com.opzpy123.service.BarkWeatherService;
import com.opzpy123.util.OssUtils;
import com.opzpy123.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.concurrent.Callable;

@Slf4j
public class OssUploadTask implements Runnable {

    private final UserNetdiscMapper userNetdiscMapper = SpringContextUtil.getBean(UserNetdiscMapper.class);


    private Long userNetdiscId;

    //文件名
    private String fileName;

    //文件流
    private InputStream inputStream;


    public OssUploadTask(String fileName, InputStream inputStream, Long userNetdiscId) {
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.userNetdiscId = userNetdiscId;
    }

    @Override
    public void run() {
        log.info("线程池任务开始");
        OssUtils ossUtils = new OssUtils();
        String url = ossUtils.upload(inputStream, fileName);
        UserNetdisc userNetdisc = new UserNetdisc();
        userNetdisc.setId(userNetdiscId);
        userNetdisc.setUrl(url);
        userNetdiscMapper.updateById(userNetdisc);
        log.info("线程池任务结束，文件同步结束");
    }

}
