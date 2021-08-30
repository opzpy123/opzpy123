package com.opzpy123.util;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CameraBasic {
    static{System. loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private JFrame frame;
    static JLabel label;
    static int flag=0;//类静态变量，用于控制按下按钮后 停止摄像头的读取

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CameraBasic window = new CameraBasic();
                    window.frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //我们的操作
        VideoCapture camera=new VideoCapture();//创建Opencv中的视频捕捉对象
        camera.open(0);//open函数中的0代表当前计算机中索引为0的摄像头，如果你的计算机有多个摄像头，那么一次1,2,3……
        if(!camera.isOpened()){//isOpened函数用来判断摄像头调用是否成功
            System.out.println("Camera Error");//如果摄像头调用失败，输出错误信息
        }
        else{
            Mat frame=new Mat();//创建一个输出帧
            while(flag==0){
                camera.read(frame);//read方法读取摄像头的当前帧
                MatOfByte mob = new MatOfByte();
                Imgcodecs.imencode(".jpg", frame, mob);
                // convert the "matrix of bytes" into a byte array
                byte[] byteArray = mob.toArray();
                System.out.print("[");
                for (int i=0;i<byteArray.length;i++){
                    System.out.print(byteArray[i]+" ");
                }
                System.out.println("]");
                label.setIcon(new ImageIcon(mat2img.Mat2BufImg(frame,".jpg")));//转换图像格式并输出
                try {
                    Thread.sleep(1000);//线程暂停100ms
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Create the application.
     */
    public CameraBasic() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(){
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnNewButton = new JButton("\u62CD\u7167");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                flag=1;//静态变量设置为1，从而按下按钮时会停止摄像头的调用
            }
        });
        btnNewButton.setBounds(33, 13, 113, 27);
        frame.getContentPane().add(btnNewButton);

        label = new JLabel("");
        label.setBounds(0, 0, 800, 450);
        frame.getContentPane().add(label);
    }

}
