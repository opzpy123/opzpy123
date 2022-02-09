package com.opzpy123;

import com.opzpy123.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CacheTest {

    @Resource
    private BlogService blogService;

    @Test
    public void testCache(){
        blogService.getBlogList();
    }
}
