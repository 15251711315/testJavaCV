package com.jxkj;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 保存usb摄像头画面到本地
 */
public class SaveImageToLocal {
    static String localPath = "C:\\Users\\Administrator\\Desktop\\video\\start.mp4";
    public static void main(String[] args) throws Exception{
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);//打开本地usb摄像头
        grabber.start();
        Frame frame = null;
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(localPath, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setVideoOption("preset", "ultrafast");
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setMaxDelay(500);
        recorder.setGopSize(10);
        // 提升编码速度
        recorder.setVideoOption("preset", "ultrafast");
        //越高延时越久，画面越清楚
        recorder.setVideoBitrate(800000);
        recorder.start();
        int second = LocalDateTime.now().getMinute();
        while ((frame = grabber.grab()) != null) {
            if(recorder!=null){
                recorder.record(frame);
            }
//            间隔5秒保存一次视频
            if(LocalDateTime.now().getSecond()%5 == 0){
                if(LocalDateTime.now().getSecond() != second){
                    recorder.stop();
                    second = LocalDateTime.now().getSecond();
                    System.out.println(second);
                    recorder = new FFmpegFrameRecorder("C:\\Users\\Administrator\\Desktop\\video\\"+second+".mp4", grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
                    recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                    recorder.setFormat("mp4");
                    recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);

                    recorder.setVideoOption("tune", "zerolatency");
                    recorder.setMaxDelay(500);
                    recorder.setGopSize(10);
                    // 提升编码速度
                    recorder.setVideoOption("preset", "ultrafast");
                    //越高延时越久，画面越清楚
                    recorder.setVideoBitrate(800000);
                    recorder.start();
                }
            }
        }
        recorder.close();
        grabber.close();
    }
}
