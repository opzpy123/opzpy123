package com.opzpy123;

import com.aliyun.oss.OSS;
import com.opzpy123.util.OssUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;

@SpringBootTest
public class OssTest {

    @Resource
    private OssUtils ossUtils;

    @Test
    void test(){

//        File file = new File("D:\\opzpy123.sql");
//        ossUtils.createFolder("opzpy","opzpy");
//        ossUtils.upload(file,"opzpy/"+file.getName());
    }
}
