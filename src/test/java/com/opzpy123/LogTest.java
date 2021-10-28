package com.opzpy123;

import com.opzpy123.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;


public class LogTest {


    @Test
    void getTodayLogInfo(){
        ArrayList<String> tmp = new ArrayList<>();
        try (
                FileInputStream fileInputStream = new FileInputStream("logs" + File.separator + "opzpy123.log");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            tmp = bufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
           e.printStackTrace();
        }
        ArrayList<String> res = new ArrayList<>();
        if(tmp.size()<=100){
            res.addAll(tmp);
        }else {
            for (int i = tmp.size()-100; i <tmp.size() ; i++) {
                res.add(tmp.get(i));
            }
        }


    }

}
