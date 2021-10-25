package com.opzpy123.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogService {
    public ArrayList<String> getLog() {
        ArrayList<String> res = new ArrayList<>();
        try (
                FileInputStream fileInputStream = new FileInputStream("logs/opzpy123.log");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            res = bufferedReader.lines().collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return res;
    }
}
