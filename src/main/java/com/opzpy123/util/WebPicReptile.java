package com.opzpy123.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebPicReptile {

    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";

    public static void main(String[] args) throws Exception {
        WebPicReptile webPicReptile = new WebPicReptile();
        //获得html文本内容
        String HTML = webPicReptile.getHtml("http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=%E6%8B%89%E9%92%A9");
        //获取图片标签
        List<String> imgUrl = webPicReptile.getImageUrl(HTML);
        //获取图片src地址
        List<String> imgSrc = webPicReptile.getImageSrc(imgUrl);
        //下载图片
        webPicReptile.Download(imgSrc);
    }

    private String getHtml(String url)throws Exception{
        URL url1=new URL(url);//使用java.net.URL
        URLConnection connection=url1.openConnection();//打开链接
        InputStream in=connection.getInputStream();//获取输入流
        InputStreamReader isr=new InputStreamReader(in);//流的包装
        BufferedReader br=new BufferedReader(isr);

        String line;
        StringBuffer sb=new StringBuffer();
        while((line=br.readLine())!=null){//整行读取
            sb.append(line,0,line.length());//添加到StringBuffer中
            sb.append('\n');//添加换行符
        }
        //关闭各种流，先声明的后关闭
        br.close();
        isr.close();
        in.close();
        return sb.toString();
    }

    private List<String> getImageUrl(String html){
        Matcher matcher= Pattern.compile(IMGURL_REG).matcher(html);
        List<String>listimgurl=new ArrayList<String>();
        while (matcher.find()){
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }

    private List<String> getImageSrc(List<String> listimageurl){
        List<String> listImageSrc=new ArrayList<String>();
        for (String image:listimageurl){
            Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()){
                listImageSrc.add(matcher.group().substring(0, matcher.group().length()-1));
            }
        }
        return listImageSrc;
    }

    private void Download(List<String> listImgSrc) {
        try {
            //开始时间
            Date begindate = new Date();
            for (String url : listImgSrc) {
                //开始时间
                Date begindate2 = new Date();
                String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
                URL uri = new URL(url);
                InputStream in = uri.openStream();
                FileOutputStream fo = new FileOutputStream(new File("C:/Users/DELL/Desktop/pic/"+imageName));//文件输出流
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + url);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                //关闭流
                in.close();
                fo.close();
                System.out.println(imageName + "下载完成");
                //结束时间
                Date overdate2 = new Date();
                double time = overdate2.getTime() - begindate2.getTime();
                System.out.println("耗时：" + time / 1000 + "s");
            }
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            System.out.println("总耗时：" + time / 1000 + "s");
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }
}
