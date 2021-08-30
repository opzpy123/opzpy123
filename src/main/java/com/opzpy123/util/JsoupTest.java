package com.opzpy123.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsoupTest {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://getbootstrap.com/docs/4.2/examples/sign-in/").get();


        File file =new File("C:\\Users\\DELL\\Desktop\\login11.html");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        fw.write(doc.toString());
        fw.close();
    }
}
