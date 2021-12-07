package com.opzpy123.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.opzpy123.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里oss工具类
 * author zbq 2018-11-05
 */
@Slf4j
public class OssUtils {

    private OSS ossClient;

    public OssUtils() {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setConnectionTimeout(1000 * 3);
        conf.setMaxErrorRetry(3);
        ossClient = new OSSClientBuilder().build(PropertiesConfig.ALI_OSS_ENDPOINT, PropertiesConfig.ALI_OSS_ACCESS_KEY_ID,
                PropertiesConfig.ALI_OSS_ACCESS_KEY_SECRET, conf);
    }


    public boolean doseBucketExit(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }


    public String createBucket(String bucketName) {
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            log.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketName;
    }

    public void deleteBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
        log.info("删除" + bucketName + "Bucket成功");
    }


    public String createFolder(String bucketName, String folder) {
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

    public synchronized String upload(InputStream inputStream, String fileName) {
        long all = System.currentTimeMillis();
        String url = "";
        try {
            ossClient.putObject(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName, inputStream);
            Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
            //返回上传文件下载路径
            url = ossClient.generatePresignedUrl(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName, expiration).toString();
        } catch (Exception e) {
            log.error("oss上传文件失败:{}", e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        log.info("上传" + fileName + "总upload耗时" + (System.currentTimeMillis() - all));
        return url;
    }

    //分片上传
    public synchronized String multipartUpload(File file, String fileName, long fileLength) throws IOException {
        long all = System.currentTimeMillis();
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName);
        // 初始化分片。
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
        final long partSize = 1 * 1024 * 1024L;   //1 MB。

        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。

        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }

        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = new FileInputStream(file);
            // 跳过已经上传的分片。
            instream.skip(startPos);
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(PropertiesConfig.ALI_OSS_BUCKET_NAME);
            uploadPartRequest.setKey(fileName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(instream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
            uploadPartRequest.setPartNumber(i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }


        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName, uploadId, partETags);

        // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
        // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

        // 完成上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);

        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
        //返回上传文件下载路径
        String url = ossClient.generatePresignedUrl(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName, expiration).toString();
        // 关闭OSSClient。
        ossClient.shutdown();
        log.info("上传" + fileName + "总upload耗时" + (System.currentTimeMillis() - all));
        return url;
    }

    public boolean isFileExist(String fileName) {
        return ossClient.doesObjectExist(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName);
    }

    public void deleteFile(String fileName) {
        if (isFileExist(fileName)) {
            ossClient.deleteObject(PropertiesConfig.ALI_OSS_BUCKET_NAME, fileName);
        }
    }
}