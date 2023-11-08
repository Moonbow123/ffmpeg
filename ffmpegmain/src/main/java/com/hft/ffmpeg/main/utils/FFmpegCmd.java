package com.hft.ffmpeg.main.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 97933 on 2018/8/26.
 */

public class FFmpegCmd {

    /**
     * 使用ffmpeg命令行进行音频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    public static String[] transformAudio(String srcFile, String targetFile) {
        String transformAudioCmd = "ffmpeg -i %s %s";
        transformAudioCmd = String.format(transformAudioCmd, srcFile, targetFile);
        return transformAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行音频剪切
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param duration   剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] cutAudio(String srcFile, String startTime, String duration, String targetFile) {
        String cutAudioCmd = "ffmpeg -i %s -ss %s -to %s -acodec copy %s";
        cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile);
        return cutAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行音频剪切
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param duration   剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] cutAudioMp3(String srcFile, String startTime, String duration, String targetFile) {
        String cutAudioCmd = "ffmpeg -y -i %s -ss %s -t %s -c:a mp3 -vn %s";
        cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile);
        return cutAudioCmd.split(" ");//以空格分割为字符串数组
    }

    @SuppressLint("DefaultLocale")
    public static String[] cutAudioAAc(String srcFile, String startTime, String duration, String targetFile) {
        String cutAudioCmd = "ffmpeg -y -i %s -ss %s -t %s -c:a aac -vn %s";
        cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile);
        return cutAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 减小音量
     *
     * @param srcFile
     * @param targetFile
     * @return
     */
    public static String[] reduceVolume(String srcFile, String targetFile, String volume) {
        String command = "ffmpeg -i %s -filter:a volume=%s %s";
        command = String.format(command, srcFile, volume, targetFile);
        return command.split(" ");
    }

    /**
     * 使用ffmpeg命令行进行音频合并
     *
     * @param textFile 待追加的文件
     * @param outFile  目标文件
     * @return 合并后的文件
     */
    public static String[] concatAudio(String textFile, String outFile) {
        String concatAudioCmd = "ffmpeg -f concat -safe 0 -i %s -acodec copy %s";
        concatAudioCmd = String.format(concatAudioCmd, textFile, outFile);
        return concatAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 合并视频
     *
     * @param filePath   视频txt地址文件
     * @param targetFile 视频输出文件
     * @return
     */
    public static String[] concatVedio(String filePath, String targetFile) {
        String concatAudioCmd = "ffmpeg -f concat -safe 0 -i %s -c copy %s";
        concatAudioCmd = String.format(concatAudioCmd, filePath, targetFile);
        return concatAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行音频混合
     *
     * @param srcFile    源文件
     * @param list       待混合文件集合
     * @param targetFile 目标文件
     * @return 混合后的文件
     */
    public static String[] mixAudio(String srcFile, List<AudioPathModel> list, String targetFile) {
        //    String mixAudioCmd = "ffmpeg -i %s -i %s -filter_complex [0:a]aformat=channel_layouts=stereo,volume=%s[a0];[1:a]aformat=channel_layouts=stereo,volume=%s[a1];[a0][a1]amix=inputs=2:dropout_transition=2:duration=shortest -strict -2 %s";
        StringBuilder builder = new StringBuilder();
        builder.append("ffmpeg -i ").append(srcFile);
        for (AudioPathModel model : list) {
            builder.append(" -i ").append(model.getPath());
        }
        String allName = "[a0]";
        builder.append(" -filter_complex ");
        builder.append("[0:a]aformat=channel_layouts=stereo,volume=1[a0];");
        int index = 1;
        for (AudioPathModel model : list) {
            String mix = "[" + index + ":a]";
            String name = "[a" + index + "]";
            allName += name;
            if (model.getStartTime() > 0) {
                builder.append(mix).append("aformat=channel_layouts=stereo,volume=2,adelay=")
                        .append(model.getStartTime()).append("|").append(model.getStartTime())
                        .append(name).append(";");
            } else {
                builder.append(mix).append("aformat=channel_layouts=stereo,volume=2")
                        .append(name).append(";");
            }
            index++;
        }
        builder.append(allName)
                .append("amix=inputs=")
                .append(list.size() + 1)
                .append(":duration=first[aout] -map [aout] -ac 2 ")
                .append(targetFile);
        return builder.toString().split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行音频混合(将背景音乐做为载体，背景音乐静音)
     *
     * @param srcFile    源文件
     * @param list       待混合文件集合
     * @param targetFile 目标文件
     * @return 混合后的文件
     */
    public static String[] mixAudioNoBgVoice(String srcFile, List<AudioPathModel> list, String targetFile) {
        //    String mixAudioCmd = "ffmpeg -i %s -i %s -filter_complex [0:a]aformat=channel_layouts=stereo,volume=%s[a0];[1:a]aformat=channel_layouts=stereo,volume=%s[a1];[a0][a1]amix=inputs=2:dropout_transition=2:duration=shortest -strict -2 %s";
        StringBuilder builder = new StringBuilder();
        builder.append("ffmpeg -i ").append(srcFile);
        for (AudioPathModel model : list) {
            builder.append(" -i ").append(model.getPath());
        }
        String allName = "[a0]";
        builder.append(" -filter_complex ");
        builder.append("[0:a]aformat=channel_layouts=stereo,volume=0[a0];");
        int index = 1;
        for (AudioPathModel model : list) {
            String mix = "[" + index + ":a]";
            String name = "[a" + index + "]";
            allName += name;
            if (model.getStartTime() > 0) {
                builder.append(mix).append("aformat=channel_layouts=stereo,volume=2,adelay=")
                        .append(model.getStartTime()).append("|").append(model.getStartTime())
                        .append(name).append(";");
            } else {
                builder.append(mix).append("aformat=channel_layouts=stereo,volume=2")
                        .append(name).append(";");
            }
            index++;
        }
        builder.append(allName)
                .append("amix=inputs=")
                .append(list.size() + 1)
                .append(":duration=first[aout] -map [aout] -ac 2 ")
                .append(targetFile);
        return builder.toString().split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行音频混合
     *
     * @param srcFile    源文件
     * @param mixFile    待混合文件
     * @param targetFile 目标文件
     * @return 混合后的文件
     */
    public static String[] mixAudio(String srcFile, String mixFile, String targetFile) {
        String mixAudioCmd = "ffmpeg -i %s -i %s -filter_complex [0:a]volume=%s[a0];[1:a]volume=%s[a1];[a0][a1]amix=inputs=2:dropout_transition=3:duration=shortest -strict -2 %s";
        mixAudioCmd = String.format(mixAudioCmd, srcFile, mixFile, 2f, 2f, targetFile);
        return mixAudioCmd.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行音视频合成
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件
     * @param duration  视频时长
     * @param muxFile   目标文件
     * @return 合成后的文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] mediaMux(String videoFile, String audioFile, String duration, String muxFile) {
        String mixAudioCmd = "ffmpeg -i %s -i %s -ss 00:00:00 -t %s -vcodec copy -acodec copy %s";
        mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile);
        return mixAudioCmd.split(" ");//以空格分割为字符串数组
    }

    @SuppressLint("DefaultLocale")
    public static String[] mediaMux(String videoFile, String audioFile, String muxFile) {
        String mixAudioCmd = "ffmpeg -i %s -i %s -vcodec copy -acodec copy %s";
        mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, muxFile);
        return mixAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 添加背景音乐
     *
     * @param videoFile
     * @param audioFile
     * @param muxFile
     * @return
     */
    public static String[] addBgMusic(String videoFile, String audioFile, String muxFile) {
        String mixAudioCmd = "ffmpeg -y -i %s -i %s -filter_complex [0:a]volume=1[a0];[1:a]volume=0.1[a1];[a0][a1]amix=inputs=2:duration=shortest[aout] -map [aout] -ac 2 -c:v copy -map 0:v:0 %s";
        mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, muxFile);
        return mixAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行抽取音频
     *
     * @param srcFile    原文件
     * @param targetFile 目标文件
     * @return 抽取后的音频文件
     */
    public static String[] extractAudio(String srcFile, String targetFile) {
        //-vn:video not
        String mixAudioCmd = "ffmpeg -i %s -acodec copy -vn %s";
        mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile);
        return mixAudioCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行抽取视频
     *
     * @param srcFile    原文件
     * @param targetFile 目标文件
     * @return 抽取后的视频文件
     */
    public static String[] extractVideo(String srcFile, String targetFile) {
        //-an audio not
        String mixAudioCmd = "ffmpeg -i %s -vcodec copy -an %s";
        mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile);
        return mixAudioCmd.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行视频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    public static String[] transformVideo(String srcFile, String targetFile) {
        //指定目标视频的帧率、码率、分辨率
//        String transformVideoCmd = "ffmpeg -i %s -r 25 -b 200 -s 1080x720 %s";
        String transformVideoCmd = "ffmpeg -i %s -vcodec copy -acodec copy %s";
        transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile);
        return transformVideoCmd.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行视频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    public static String[] transformVideo2(String srcFile, String targetFile) {
        //指定目标视频的帧率、码率、分辨率
//        String transformVideoCmd = "ffmpeg -i %s -r 25 -b 200 -s 1080x720 %s";
        String transformVideoCmd = "ffmpeg -i %s -r 25 %s";
        transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile);
        return transformVideoCmd.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行视频剪切
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间
     * @param endTime    剪切时长
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] cutVideo(String srcFile, String startTime, String endTime, String targetFile) {
        String cutVideoCmd = "ffmpeg -ss %s -i %s -codec copy -t %s -y %s";
        cutVideoCmd = String.format(cutVideoCmd, startTime, srcFile, endTime, targetFile);
        return cutVideoCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行视频截图
     *
     * @param srcFile 源文件
     * @return 截图后的文件
     */
    public static String[] screenShot(String srcFile, String targetFile, String startTime) {

        String screenShotCmd = "ffmpeg -ss %s -i %s -f image2 -y -s 320x240 %s";
        screenShotCmd = String.format(screenShotCmd, startTime, srcFile, targetFile);
        return screenShotCmd.split(" ");//以空格分割为字符串数组
//        String screenShotCmd = "ffmpeg -i %s -vf fps=1 %s%s.jpg";
//        screenShotCmd = String.format(screenShotCmd, srcFile, targetFile, "/" + System.currentTimeMillis());
//        return screenShotCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行给视频添加水印
     *
     * @param srcFile    源文件
     * @param waterMark  水印文件路径
     * @param targetFile 目标文件
     * @return 添加水印后的文件
     */
    public static String[] addWaterMark(String srcFile, String waterMark, String targetFile) {
        String waterMarkCmd = "ffmpeg -i %s -i %s -filter_complex overlay=0:0 %s";
        waterMarkCmd = String.format(waterMarkCmd, srcFile, waterMark, targetFile);
        return waterMarkCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行视频转成Gif动图
     *
     * @param srcFile    源文件
     * @param startTime  开始时间
     * @param duration   截取时长
     * @param targetFile 目标文件
     * @return Gif文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] generateGif(String srcFile, int startTime, int duration, String targetFile) {
        //String screenShotCmd = "ffmpeg -i %s -vframes %d -f gif %s";
        String screenShotCmd = "ffmpeg -i %s -ss %d -t %d -s 320x240 -f gif %s";
        screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile);
        return screenShotCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行屏幕录制
     *
     * @param size       视频尺寸大小
     * @param recordTime 录屏时间
     * @param targetFile 目标文件
     * @return 屏幕录制文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] screenRecord(String size, int recordTime, String targetFile) {
        //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
        //String screenRecordCmd = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
        String screenRecordCmd = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s";
        screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile);
        Log.i("VideoHandleActivity", "screenRecordCmd=" + screenRecordCmd);
        return screenRecordCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行图片合成视频
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件(mpg格式)
     * @return 合成的视频文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] pictureToVideo(String srcFile, String targetFile) {
        //-f image2：代表使用image2格式，需要放在输入文件前面
        String combineVideo = "ffmpeg -f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s";
        combineVideo = String.format(combineVideo, srcFile, targetFile);
        combineVideo = combineVideo.replace("#", "%");
        Log.i("VideoHandleActivity", "combineVideo=" + combineVideo);
        return combineVideo.split(" ");//以空格分割为字符串数组
    }

    /**
     * 音频编码
     *
     * @param srcFile    源文件pcm裸流
     * @param targetFile 编码后目标文件
     * @param sampleRate 采样率
     * @param channel    声道:单声道为1/立体声道为2
     * @return 音频编码的命令行
     */
    @SuppressLint("DefaultLocale")
    public static String[] encodeAudio(String srcFile, String targetFile, int sampleRate, int channel) {
        String combineVideo = "ffmpeg -f s16le -ar %d -ac %d -i %s %s";
        combineVideo = String.format(combineVideo, sampleRate, channel, srcFile, targetFile);
        return combineVideo.split(" ");
    }

    /**
     * 多画面拼接视频
     *
     * @param input1      输入文件1
     * @param input2      输入文件2
     * @param videoLayout 视频布局
     * @param targetFile  画面拼接文件
     * @return 画面拼接的命令行
     */
    public static String[] multiVideo(String input1, String input2, String targetFile, int videoLayout) {
//        String multiVideo = "ffmpeg -i %s -i %s -i %s -i %s -filter_complex " +
//                "\"[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];[c][3:v]overlay=w:h\" %s";
        String multiVideo = "ffmpeg -i %s -i %s -filter_complex hstack %s";//hstack:水平拼接，默认
        //vstack:垂直拼接
        if (videoLayout == 2) {
            // 垂直拼接
            multiVideo = multiVideo.replace("hstack", "vstack");
        }
        multiVideo = String.format(multiVideo, input1, input2, targetFile);
        return multiVideo.split(" ");
    }

    /**
     * 将秒表示时长转为00:00:00格式
     *
     * @param second 秒数时长
     * @return 字符串格式时长
     */
    private String parseTimeToString(int second) {
        int end = second % 60;
        int mid = second / 60;
        if (mid < 60) {
            return mid + ":" + end;
        } else if (mid == 60) {
            return "1:00:" + end;
        } else {
            int first = mid / 60;
            mid = mid % 60;
            return first + ":" + mid + ":" + end;
        }

    }

    /**
     * 处理视频比例
     *
     * @param srcFile
     * @param targetFile
     * @param sampleRate 比例 如 16:9
     * @return
     */
    public static String[] aspectVideo(String srcFile, String targetFile, String sampleRate) {
        String combineVideo = "ffmpeg -i %s -vf crop=ih*4/3:ih -c:v libx264 -crf 26 -tune fastdecode -preset ultrafast -keyint_min 5 -g 3 -sc_threshold 0 -threads 8 -c:a copy %s";
        combineVideo = String.format(combineVideo, srcFile, targetFile);
        return combineVideo.split(" ");
    }


    /**
     * @param inputVideo    输入视屏地址
     * @param outputVideo   输出视频地址
     * @param subtitlesPath 字幕地址
     * @param fontSize      字体大小
     * @param textColor     字体颜色
     * @param outLineColor  边框颜色
     * @param marginV       底部间距
     * @return
     */
    public static String[] addSubtitles(@NonNull String inputVideo, @NonNull String outputVideo, @NonNull String subtitlesPath, int fontSize, String textColor, String outLineColor, int marginV) {
        List<String> cmdList = new ArrayList<>();
        cmdList.add("ffmpeg");
        cmdList.add("-i");
        cmdList.add(inputVideo);
        //烧字幕
        cmdList.add("-vf");
        String sit = ":fontsdir=tljt.ttf:force_style='MarginV=" + marginV + ",Fontsize=" + fontSize + ",PrimaryColour=" + textColor + ",OutlineColour=" + outLineColor + "'";
        cmdList.add(String.format(Locale.getDefault(), "subtitles=%s%s", subtitlesPath, sit));

        //mkv字幕轨
//        cmdList.add("-i");
//        cmdList.add(subtitlesPath);
//
//        cmdList.add("-ss");
//        cmdList.add("0");
//        cmdList.add("-t");
//        cmdList.add("" + duration);
//
//        cmdList.add("-map");
//        cmdList.add("0:v");
//        cmdList.add("-map");
//        cmdList.add("0:a");
//        cmdList.add("-map");
//        cmdList.add("1:s");
//        cmdList.add("-c");
//        cmdList.add("copy");
        cmdList.add(outputVideo);
        String[] cmds = new String[cmdList.size()];
        cmdList.toArray(cmds);
        return cmds;
    }
}
