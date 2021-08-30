package com.opzpy123.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class OpenCV4J {


    public static void main(String[] args) {
//        System.out.println("Welcome to OpenCV " + Core.VERSION);
//        Mat mat = Imgcodecs.imread("C:\\Users\\DELL\\Desktop\\gta.png");
//
//        ImageViewer imageViewer = new ImageViewer(mat, "第一幅图片");
//        imageViewer.imshow();


        System.out.println("Hello, OpenCV");
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new DetectFaceDemo().run();
    }
}

class DetectFaceDemo {
    public void run() {
        System.out.println("\nRunning DetectFaceDemo");
        // Create a face detector from the cascade file in the resources
        // directory.
        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/lbpcascade_frontalface.xml").getPath());
        Mat image = Imgcodecs.imread(getClass().getResource("/lena.png").getPath());
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
        // Save the visualized detection.
        String filename = "faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
    }
}
