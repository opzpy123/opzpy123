package com.opzpy123.util;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class opzpyCameraTest {

    public static void main(String[] args) {
       getCamera();
    }


    public static void getCamera() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //我们的操作
        VideoCapture camera = new VideoCapture();//创建Opencv中的视频捕捉对象
        camera.open(0);//open函数中的0代表当前计算机中索引为0的摄像头，如果你的计算机有多个摄像头，那么一次1,2,3……
        if (!camera.isOpened()) {//isOpened函数用来判断摄像头调用是否成功
            System.out.println("Camera Error");//如果摄像头调用失败，输出错误信息
        } else {
            Mat frame = new Mat();//创建一个输出帧
            camera.read(frame);//read方法读取摄像头的当前帧
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", frame, mob);
            BufferedImage bufferedImage = mat2img.Mat2BufImg(frame, ".jpg");
            try {
                ImageIO.write(bufferedImage, "jpg", new File("C:\\Users\\DELL\\Desktop\\gta.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
