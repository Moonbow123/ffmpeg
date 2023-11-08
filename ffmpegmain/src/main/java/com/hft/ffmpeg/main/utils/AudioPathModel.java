package com.hft.ffmpeg.main.utils;

/**
 * author huyi
 * date 2018/8/30
 * desc:
 */
public class AudioPathModel {
    /**
     * 录音地址
     */
    private String path;

    /**
     * 开始位置
     */
    private int startDX;

    /**
     * 结束位置
     */
    private int endDX;

    /**
     * 结束时间
     */
    private int endTime;

    /**
     * 开始时间
     */
    private int startTime;

    public AudioPathModel() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStartDX() {
        return startDX;
    }

    public void setStartDX(int startDX) {
        this.startDX = startDX;
    }

    public int getEndDX() {
        return endDX;
    }

    public void setEndDX(int endDX) {
        this.endDX = endDX;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
