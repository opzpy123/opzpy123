package com.opzpy123.util;

import org.apache.http.HttpEntity;

import org.apache.http.client.config.RequestConfig;

import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import java.io.File;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.HashSet;

import java.util.List;


public class SuperSpider {

    public static HashMap<String,Integer> websiteMap = new HashMap<>();

    //下载过的imgSet

    public static HashSet<String> imgUrlSet = new HashSet<>();

    //下载失败的imgSet

    public static HashSet<String> errorImgUrlSet = new HashSet<>();

    //爬虫爬过的

    public static List<String> overList = new ArrayList<>();

    //等待爬取的

    public static List<String> waitList = new ArrayList<>();

    //异常url

    public static List<String> exceptionList = new ArrayList<>();

    public static int currentLevel = 0;

    public static int maxSpiderLevel = 2;

    public static int maxThreadNum = 2;

    public static String topUrl = "[http://www.baidu.com/](http://www.baidu.com/)";

    public static String filter = "[www.baidu](http://www.baidu/)";

    public static String filePath = "/Users/will/Downloads/pic/";

    //生命对象，帮助进行线程的等待操作

    public static final Object lifeObj = new Object();

    public static void main(String[] args) throws Exception {

        waitList.add(topUrl);

        for (int i = 0; i < maxThreadNum; i++) {

            new SuperSpider().new SpiderThread("Thread_"+i).start();

        }

    }

    /**

     * 获取url里的a标签

     */

    public static void runSpider(String url,int level)  {

        if(overList.contains(url) || level >= maxSpiderLevel){

            return;

        }

        String content = "";

        try {

            content = getHTML(url);

        }catch (Exception e){

            System.out.println(url);

            e.printStackTrace();

            exceptionList.add(url);

        }

        List<String> imgUrlList = getImgSrc(content);

        for(String imgUrl:imgUrlList){

            downImages(filePath, imgUrl);

        }

        List<String> aUrlList = getAHref(content);

        for(String waitUrl:aUrlList){

            waitList.add(waitUrl);

            websiteMap.put(waitUrl,currentLevel+1);

        }

        overList.add(url);

    }

    /**

     * 获取目标网站的content

     * @param url url

     */

    private static String getHTML(String url) throws IOException {

        // 创建httpclient实例

        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建httpget实例

        HttpGet httpget = new HttpGet(url);

        // 模拟浏览器

        httpget.setHeader("User-Agent",

                "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");

        // 使用代理 IP

//        HttpHost proxy = new HttpHost("118.114.77.47", 8080);

        RequestConfig config = RequestConfig.custom()

//            .setProxy(proxy)

                //设置连接超时

                .setConnectTimeout(10000) // 设置连接超时时间 10秒钟

                .setSocketTimeout(10000) // 设置读取超时时间10秒钟

                .build();

        httpget.setConfig(config);

        // 执行get请求

        CloseableHttpResponse response = httpclient.execute(httpget);

        HttpEntity entity = response.getEntity();

        // 获取返回实体

        String content = EntityUtils.toString(entity, "utf-8");

        //获取响应类型、内容

        System.out.println("Status:"+response.getStatusLine().getStatusCode());

        System.out.println("Content-Type:"+entity.getContentType().getValue());

        response.close(); // response关闭

        httpclient.close(); // httpClient关闭

        return content;

    }

    /**

     * 获取图片url

     */

    private static List<String> getImgSrc(String content){

        // 解析网页 得到文档对象

        Document doc = Jsoup.parse(content);

        Elements elements = doc.getElementsByTag("img"); // 获取tag是a的所有DOM元素，数组

        List<String> imgSrcList = new ArrayList<>();

        for (Element element:elements) {

            String src = element.attr("src"); // 返回元素的文本

            System.out.println("<img>：" + src);

            //正则校验？有效校验

            int i = src.indexOf(filter);

            if(i==-1 || imgUrlSet.contains(src)){

                continue;

            }

            if(i>0){

                src = src.substring(i);

            }

            imgSrcList.add(src);

        }

        return imgSrcList;

    }

    /**

     * 获取页面超链接

     */

    private static List<String> getAHref(String content){

        // 解析网页 得到文档对象

        Document doc = Jsoup.parse(content);

        // 获取tag是a的所有DOM元素，数组

        Elements elements = doc.getElementsByTag("a");

        List<String> imgSrcList = new ArrayList<>();

        for (Element element:elements) {

            // 返回元素的信息

            String src = element.attr("href");

            System.out.println("<a>：" + src);

            imgSrcList.add(src);

        }

        return imgSrcList;

    }

    /**

     * 根据图片的URL下载的图片到本地的filePath

     * @param filePath 文件夹

     * @param imageUrl 图片的网址

     */

    public static void downImages(String filePath,String imageUrl){

        // 截取图片的名称

        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));

        //创建文件的目录结构

        File files = new File(filePath);

        // 判断文件夹是否存在，如果不存在就创建一个文件夹

        if(!files.exists()){

            files.mkdirs();

        }

        try {

            URL url = new URL("http://"+imageUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream is = connection.getInputStream();

            // 创建文件

            File file = new File(filePath+fileName);

            FileOutputStream out = new FileOutputStream(file);

            int i = 0;

            while((i = is.read()) != -1){

                out.write(i);

            }

            is.close();

            out.close();

            imgUrlSet.add(imageUrl);

        } catch (Exception e) {

            errorImgUrlSet.add(imageUrl);

            System.out.println(e.getMessage()+"下载失败+_"+imageUrl);

        }

    }

    public class SpiderThread extends Thread{

        public SpiderThread(String name){

            super(name);

        }

        @Override

        public void run() {

            //设定一个死循环，让线程一直存在

            while (true) {

                //判断是否新链接，有则获取

                if (waitList.size()>0){

                    String nextUrl = waitList.get(0);

                    waitList.remove(0);

                    runSpider(nextUrl,websiteMap.get(nextUrl));

                } else {

                    System.out.println("当前线程准备就绪，等待连接爬取：" + this.getName());

                    //建立一个对象，让线程进入等待状态，即wait（）

                    synchronized (lifeObj) {

                        try {

                            lifeObj.wait();

                        } catch (Exception e) {

                        }

                    }

                }

            }

        }

    }

}
