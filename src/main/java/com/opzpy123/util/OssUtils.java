package com.opzpy123.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialBlob;

import com.aliyun.oss.OSS;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.*;
import com.opzpy123.config.OssConfig;
import com.opzpy123.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * 阿里oss工具类
 * author zbq 2018-11-05
 */
@Slf4j
@Component
public class OssUtils {

    @Resource
    private OssConfig ossConfig;

    public boolean doseBucketExit(String bucketName){

        OSS ossClient = ossConfig.getBean();
        return ossClient.doesBucketExist(bucketName);
    }


    public String createBucket(String bucketName) {
        OSS ossClient = ossConfig.getBean();
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            log.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketName;
    }

    public void deleteBucket(String bucketName) {
        OSS ossClient = ossConfig.getBean();
        ossClient.deleteBucket(bucketName);
        log.info("删除" + bucketName + "Bucket成功");
    }


    public String createFolder(String bucketName, String folder) {
        OSS ossClient = ossConfig.getBean();
        if (!ossClient.doesObjectExist(bucketName, folder)) {
            ossClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
            log.info("创建文件夹成功");
            return ossClient.getObject(bucketName, folder).getKey();
        }
        return folder;
    }

//    public void deleteFile(String folder, String fileName) {
//        OSS ossClient = ossConfig.getBean();
//        ossClient.deleteObject(PropertiesConfig.ALI_OSS_BUCKET_NAME, folder + fileName);
//        log.info("删除" + PropertiesConfig.ALI_OSS_BUCKET_NAME + "下的文件" + folder + fileName + "成功");
//    }


    public String upload(File file, String name) {
        String url = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            url = upload(fileInputStream, name);
        } catch (Exception e) {
            log.error("oss上传文件失败:{}", e.getMessage());
        }
        return url;
    }

    public String upload(InputStream inputStream, String name) {
        OSS ossClient = ossConfig.getBean();
        String url = "";
        try {
            ossClient.putObject(PropertiesConfig.ALI_OSS_BUCKET_NAME, name, inputStream);
            Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
            //返回上传文件下载路径
            url = ossClient.generatePresignedUrl(PropertiesConfig.ALI_OSS_BUCKET_NAME, name, expiration).toString();
            log.info("oss上传文件成功:{}", name);
        } catch (Exception e) {
            log.error("oss上传文件失败:{}", e.getMessage());
        }finally {
            ossClient.shutdown();
        }
        return url;
    }

    public boolean isFileExist(String fileKey){
        OSS ossClient = ossConfig.getBean();
        return ossClient.doesObjectExist(PropertiesConfig.ALI_OSS_BUCKET_NAME,fileKey);
    }

    public void deleteFile(String fileKey) {
        OSS ossClient = ossConfig.getBean();
        if(isFileExist(fileKey)) {
            ossClient.deleteObject(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileKey);
        }
    }
}