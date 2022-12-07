package com.opzpy123;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSONObject;
import com.opzpy123.util.JsonUitl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

@SpringBootTest
public class ErrorCodeTest {

    @Test
    public void read4() throws IOException, InterruptedException {
        File file = new File("/Users/opzpy/Desktop/4.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
        HashMap<String, String> hashMap = new HashMap<>();
        while (br.ready()) {
            String line = br.readLine();
            if (line.startsWith("/") || line.startsWith(",") || StrUtil.isBlank(line)) continue;
            String[] split = line.replace("#define", "").split(",");
            String[] split1 = split[1].split("//");
            String code = split1[0];
            if (code.contains("+")) {
                String[] s = code.trim().replace("(", "").replace(")", "").replace(" ", "").split("\\+");
                if (NumberUtil.isNumber(s[0])) {
                    int i = Integer.parseInt(s[0]) + Integer.parseInt(s[1]);
                    code = i + "";
                } else {
                    String s1 = hashMap.get(s[0]);
                    int i = Integer.parseInt(s1.trim().replace("\t","")) + Integer.parseInt(s[1].trim().replace("\t",""));
                    code = i + "";
                }
            }
            String enumStr = split[0].trim().replace("\t", "");
            hashMap.put(enumStr, code);
            System.out.println(enumStr + ",(" + code + "," + "\"" + split1[1] + "\"" + "),");
        }
    }


    @Test
    public void read3() throws IOException, InterruptedException {
        File file = new File("/Users/opzpy/Desktop/3.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String first = br.readLine().replace(",", "").trim();
        System.out.println(first + ",(" + 0 + "" + "\"" + first + "\"" + "),");
        int idx = 50000;
        while (br.ready()) {
            String line = br.readLine();
            String[] split = line.split(",");
            String enumStr = split[0];
            String code = idx++ + "";
            String name = StrUtil.isBlank(split[1]) ? "===未知===" : split[1].replace("//", "").trim();
            System.out.println(enumStr + ",(" + code + "," + "\"" + name + "\"" + "),");
        }
    }


    @Test
    public void read2() throws IOException, InterruptedException {
        File file = new File("/Users/opzpy/Desktop/2.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
        while (br.ready()) {
            String line = br.readLine();
            if (StrUtil.isBlank(line)) continue;
            String[] split = line.split("//");
            String code = split[0].trim();
            String name = split[1] == null ? "" : split[1].trim();
            String engName = trans(name).replace(" ", "_").toUpperCase(Locale.ROOT);
            System.out.println(engName + "(" + code + "" + "\"" + name + "\"" + "),");
        }
    }

    private String trans(String str) throws UnsupportedEncodingException, InterruptedException {
        String api = "https://fanyi-api.baidu.com/api/trans/vip/translate";
        String sign = "20220620001252916" + str + "opzpy097zFnnGkiibTmaTp0V7";
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String url = api + "?" + "q=" + str + "&from=zh&to=en&appid=20220620001252916&salt=opzpy&sign=" + md5.digestHex(sign);
        JSONObject json = JsonUitl.loadJsonAsJsonObj(url);
        Thread.sleep(300);
        return json.getJSONArray("trans_result").getJSONObject(0).getString("dst");
    }


    private static void read1() throws IOException {
        File file = new File("/Users/opzpy/Desktop/1.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));

        while (br.ready()) {
            String s1 = br.readLine();
            String s2 = br.readLine().trim().replace("*", "");
            String s3 = br.readLine();
            String[] s4 = br.readLine().split("=");
            System.out.println(s4[0] + "(" + s4[1] + "" + "\"" + s2 + "\"" + "),");
        }
    }
}
