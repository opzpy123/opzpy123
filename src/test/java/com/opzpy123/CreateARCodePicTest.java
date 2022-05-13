package com.opzpy123;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.opzpy123.model.AuthUser;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateARCodePicTest {

    @Test
    void test() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        for (AuthUser authUser : readExcel()) {
            //核酸检测
            doCreateNucleicAcid(authUser, date);
            //行程码
            doCreateItineraryCode(authUser, date);
            //安康吗
            doCreateAnkangCode(authUser, date);
        }
    }

    private void doCreateNucleicAcid(AuthUser authUser, String date) throws IOException {
        BufferedImage nucleicAcid = ImageIO.read(new File("/Users/opzpy/Documents/starlibary/p图程序素材/核酸证明素材.jpeg"));
        cleanInfo(nucleicAcid, 1);
        setInfo(nucleicAcid, authUser, date, 1);
        File file = new File("/Users/opzpy/Desktop/result/" + authUser.getUsername() + "/");
        file.mkdirs();
        ImageIO.write(nucleicAcid, "JPEG", new File("/Users/opzpy/Desktop/result/" + authUser.getUsername() + "/核酸证明.jpg"));
    }

    private void doCreateItineraryCode(AuthUser authUser, String date) throws IOException {
        BufferedImage itineraryCode = ImageIO.read(new File("/Users/opzpy/Documents/starlibary/p图程序素材/行程码素材.jpeg"));
        cleanInfo(itineraryCode, 2);
        setInfo(itineraryCode, authUser, date, 2);
        File file = new File("/Users/opzpy/Desktop/result/" + authUser.getUsername() + "/");
        file.mkdirs();
        ImageIO.write(itineraryCode, "JPEG", new File("/Users/opzpy/Desktop/result/" + authUser.getUsername() + "/行程码.jpg"));
    }

    private void doCreateAnkangCode(AuthUser authUser, String date) throws IOException {
        BufferedImage ankangCode = ImageIO.read(new File("/Users/opzpy/Documents/starlibary/p图程序素材/安康码素材.jpeg"));
        cleanInfo(ankangCode, 3);
        setInfo(ankangCode, authUser, date, 3);
        File file = new File("/Users/opzpy/Desktop/result/" + authUser.getUsername() + "/");
        file.mkdirs();
        ImageIO.write(ankangCode, "JPEG", new File("/Users/opzpy/Desktop/result/" + authUser.getUsername() + "/安康码.jpg"));
    }

    private ArrayList<AuthUser> readExcel() {
        ArrayList<AuthUser> authUsers = new ArrayList<>();
        ExcelReader reader = ExcelUtil.getReader("/Users/opzpy/Desktop/工作簿2.xlsx");
        List<List<Object>> read = reader.read();
        read.remove(0);
        for (List<Object> objects : read) {
            AuthUser authUser = new AuthUser();
            authUser.setUsername(objects.get(0).toString());
            authUser.setEmail(objects.get(1).toString());
            authUser.setPhone(objects.get(2).toString());
            authUsers.add(authUser);
        }
        return authUsers;
    }

    private void cleanInfo(BufferedImage bufferedImage, Integer code) {
        Color white = new Color(255, 255, 255);
        //核酸证明
        if (code == 1) {
            //清除名字
            setColor(bufferedImage, 761, 1107, 200, 60, white.getRGB());
            //清除身份证
            setColor(bufferedImage, 468, 1220, 460, 58, white.getRGB());
            //清除日期
            setColor(bufferedImage, 700, 1500, 270, 58, white.getRGB());
        }
        //行程码
        if (code == 2) {
            //清除名字
            setColor(bufferedImage, 182, 454, 194, 39, white.getRGB());
            //清除时间
            setColor(bufferedImage, 262, 525, 370, 45, white.getRGB());
        }
        //安康码
        if (code == 3) {
            //清除名字
            setColor(bufferedImage, 62, 315, 116, 40, white.getRGB());
            //清除时间
            setColor(bufferedImage, 186, 635, 381, 43, white.getRGB());
        }
    }

    private void setColor(BufferedImage nucleicAcid, int startX, int startY, int lenX, int lenY, int RGB) {
        for (int x = startX; x < startX + lenX; x++) {
            for (int y = startY; y < startY + lenY; y++) {
                nucleicAcid.setRGB(x, y, RGB);
            }
        }
    }

    private void setInfo(BufferedImage nucleicAcid, AuthUser authUser, String date, Integer code) {
        Graphics graphics = nucleicAcid.getGraphics();
        //核酸证明
        if (code == 1) {
            //设置姓名
            setString(graphics, 785, 1120, 45, authUser.getUsername());
            //设置身份证
            setString(graphics, 467, 1262, 43, authUser.getEmail());
            //设置核酸检测时间
            setString(graphics, 700, 1535, 41, date);
            //备注
//            graphics.setColor(new Color(217, 8, 8));
//            graphics.setFont(new Font("Times New Roman", Font.PLAIN, 75));
//            graphics.drawString(authUser.getUsername(), 350, 1000);
        }
        // 行程码
        if (code == 2) {
            //设置手机号
            String phone = authUser.getPhone().substring(0, 3) + "****" + authUser.getPhone().substring(authUser.getPhone().length()-4, authUser.getPhone().length());
            setString(graphics, 183, 479, 30, phone);
            //设置时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            Date date1 = new Date();
            date1.setHours(RandomUtil.randomInt(6,new Date().getHours()));
            date1.setMinutes(RandomUtil.randomInt(2,50));
            date1.setSeconds(RandomUtil.randomInt(2,50));
            graphics.setColor(new Color(152, 152, 152));
            graphics.setFont(new Font(null, Font.PLAIN, 34));
            graphics.drawString(sdf.format(date1), 262, 555);
            //备注
            graphics.setColor(new Color(217, 8, 8));
            graphics.setFont(new Font("Times New Roman", Font.PLAIN, 48));
            graphics.drawString(authUser.getUsername(), 300, 800);
        }
        // 安康码
        if (code == 3) {
            //设置姓名
            setString(graphics, 65, 355, 35, authUser.getUsername());
            //设置核酸检测时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = new Date();
            date1.setHours(RandomUtil.randomInt(6,new Date().getHours()));
            date1.setMinutes(RandomUtil.randomInt(2,50));
            date1.setSeconds(RandomUtil.randomInt(2,50));
            graphics.setColor(new Color(0, 0, 0));
            graphics.setFont(new Font(null, Font.BOLD, 36));
            graphics.drawString(sdf.format(date1), 176, 655);
            //备注
//            graphics.setColor(new Color(217, 8, 8));
//            graphics.setFont(new Font("Times New Roman", Font.PLAIN, 48));
//            graphics.drawString(authUser.getUsername(), 380, 760);
        }
        graphics.dispose();
    }

    private void setString(Graphics graphics, int startX, int startY, int fontSize, String content) {
        graphics.setColor(new Color(0, 0, 0));
        graphics.setFont(new Font(null, Font.PLAIN, fontSize));
        graphics.drawString(content, startX, startY);
    }
}
