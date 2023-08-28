package com.jxkj;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_tracking.SizeTPointVectorMap;

import java.time.LocalDateTime;

/**
 * 推送usb摄像头画面到流服务器
 */
public class SendImageToRtsp {
    static String steamNumber = "1";
    static String RtmpUrl = "rtmp://127.0.0.1:1935/liveapp/"+steamNumber;  //（RTMP地址）
    public static void main(String[] args) throws Exception{
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);//打开本地usb摄像头
        grabber.start();
        Frame frame = null;
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(RtmpUrl, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
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
        }
        recorder.close();
        grabber.close();
    }
}
