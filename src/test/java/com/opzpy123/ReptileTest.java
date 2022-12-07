package com.opzpy123;

import cn.hutool.core.util.RandomUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReptileTest {



    public static void main(String[] args) throws IOException, InterruptedException {
        //纳兰小说 爬虫  https://www.nlxs.org/
        int chapter = 0;
        File file = new File("/Users/opzpy/desktop/book.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(" \n");
        String prefix = "https://www.nlxs.org";
        URL url = new URL(prefix + "/36324d212ymEb7tf24107620.html");
        while (!url.toString().equals(prefix)) {
            //2. 获取连接对象
            System.out.println("当前请求:" + url);
            //4. 获取数据
            InputStream in = null;
            int retryCount = 0;
            while (in == null) {
                try {
                    Thread.sleep(RandomUtil.randomLong(2000, 10000));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //3. 设置连接信息 请求方式 请求参数 请求头
                    urlConnection.setRequestMethod("GET");//这里一定要大写
                    urlConnection.setRequestProperty("Pragma", "no-cache");
                    urlConnection.setConnectTimeout(30000);
                    in = urlConnection.getInputStream();
                } catch (Exception e) {
                    System.out.println("url连接失败重试:" + retryCount + "次:" + e.getMessage());
                }
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "GBK"));
            String line;
            String nextUrl = "";
            while ((line = bufferedReader.readLine()) != null) {
                //获取到内容
                if (line.contains("chaptercontent")) {
                    String content = line;
                    content = content.replaceAll("<br/><br/>", "\n");
                    content = content.replaceAll("&nbsp;", "");
                    content = content.replaceAll("<br/>", "");
                    content = content.substring(content.indexOf("\n") + 1, content.lastIndexOf("\n"));
                    chapter++;
                    fileWriter.append("\n\n=====第" + chapter + "章=====\n\n" + content);
                    System.out.println("=====第" + chapter + "章=====");
                }
                //获取到下一章
                if (line.contains("function nhgnghyn() {")) {
                    bufferedReader.readLine();
                    nextUrl = bufferedReader.readLine();
                    nextUrl = nextUrl.replace("        window.location.href = \"", "");
                    nextUrl = nextUrl.replace("\";", "");
                }
            }
            //5. 关闭资源
            in.close();
            bufferedReader.close();
            //下一章
            url = new URL(prefix + nextUrl);
            Thread.sleep(RandomUtil.randomLong(2000, 10000));
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
