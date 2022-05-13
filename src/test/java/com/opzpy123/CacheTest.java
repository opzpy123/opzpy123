package com.opzpy123;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.opzpy123.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//@SpringBootTest
public class CacheTest {


    @Test
    public void test() {
        List<String> strings = List.of("a","b","c","d");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("a","asfas");
        hashMap.put("w","asfas");
        hashMap.put("was","asfas");
        hashMap.put("wgasg","asfas");
        hashMap.put("c","asfas");
        hashMap.keySet().removeIf(strings::contains);
        System.out.println(hashMap.keySet());


    }
}
