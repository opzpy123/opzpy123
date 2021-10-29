package com.opzpy123.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogService {
    public ArrayList<String> getTodayLogInfo() {
        ArrayList<String> tmp = new ArrayList<>();
        try (
                FileInputStream fileInputStream = new FileInputStream("logs" + File.separator + "opzpy123.log");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            tmp = bufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return getLastLogs(tmp);
    }

    public ArrayList<String> getAllLogDebug() {
        ArrayList<String> tmp = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream("." + File.separator + "opzpy123.log");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            tmp = bufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return getLastLogs(tmp);
    }

    @NotNull
    private ArrayList<String> getLastLogs(ArrayList<String> tmp) {
        ArrayList<String> res = new ArrayList<>();
        if(tmp.size()<=300){
            res.addAll(tmp);
        }else {
            for (int i = tmp.size()-300; i <tmp.size() ; i++) {
                res.add(tmp.get(i));
            }
        }
        return res;
    }
}
