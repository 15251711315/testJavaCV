package com.jxkj;


import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

/**
 * 窗口展示本地摄像头画面
 *
 */
public class CanvasShowImage {

    public static void main(String[] args) throws Exception{
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);//打开本地usb摄像头
        grabber.start();
        Frame frame = null;
        CanvasFrame canvas = new CanvasFrame("人脸检测"); // 新建一个预览窗口

        canvas.setCanvasSize(1000, 800);//设置大小

        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas.setVisible(true);
        canvas.setFocusable(true);
        // 窗口置顶
        if (canvas.isAlwaysOnTopSupported()) {
            canvas.setAlwaysOnTop(true);
        }
        for (; canvas.isVisible() && (frame = grabber.grab()) != null; ) {
            // 显示视频图像
            canvas.showImage(frame);
        }

        canvas.dispose();
        grabber.close();
    }

}

