package com.jxkj;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.AudioFormat;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class ChangeMP4ToPCM {
    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\Administrator\\Desktop\\1.mp4"; //待转音视频
        File file = new File("C:\\Users\\Administrator\\Desktop\\1.pcm"); //目标文件

        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

        FFmpegFrameGrabber frameGrabber = FFmpegFrameGrabber.createDefault(path);
        frameGrabber.setSampleRate(16000); //16K赫兹采样率
        frameGrabber.setAudioChannels(1); //单声道
        frameGrabber.start();

        Frame frame;
        Buffer buffer;
        short[] shorts;
        byte[] bytes;

        System.out.println("开始转换文件");
        while ((frame = frameGrabber.grabSamples()) != null) {
            if (frame.samples == null) {
                continue;
            }
            buffer = frame.samples[0];

            shorts = new short[buffer.limit()];
            ((ShortBuffer) buffer).get(shorts);
            bytes = shortArr2byteArr(shorts, buffer.limit());

            os.write(bytes);
        }

        os.close(); // 关闭写文件
        frameGrabber.close(); // 直接关闭拉流
        System.out.println("转换结束");
    }

    /**
     * 8位字节数组转16位字节数组，也就是16位的采样位深，转小端点字节数组
     *
     * @param shortArr
     * @param shortArrLen
     * @return
     */
    private static byte[] shortArr2byteArr(short[] shortArr, int shortArrLen) {
        byte[] byteArr = new byte[shortArrLen * 2];
        ByteBuffer.wrap(byteArr).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shortArr);
        return byteArr;
    }
}
