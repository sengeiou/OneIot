package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mqh on 3/28/16.
 */
public class SleepDayDateBean {
    private long date;
    private int sleepTime;
    private int awakeCount;
    private int lightTime;
    private int deepTime;
    private int sleepQuality;//0 差, 1 良, 2　优

    private int[] sleepStatusArray;
    private int[] durationTimeArray;
    private int[] timePointArray;

    public SleepDayDateBean() {

    }

    public SleepDayDateBean(long date, int sleepTime, int awakeCount, int lightTime, int deepTime, int sleepQuality, int[] sleepStatusArray, int[] durationTimeArray, int[] timePointArray) {
        this.date = date;
        this.sleepTime = sleepTime;
        this.awakeCount = awakeCount;
        this.lightTime = lightTime;
        this.deepTime = deepTime;
        this.sleepQuality = sleepQuality;
        this.sleepStatusArray = sleepStatusArray;
        this.durationTimeArray = durationTimeArray;
        this.timePointArray = timePointArray;
    }

    public int[] getDurationTimeArray() {
        return durationTimeArray;
    }

    public void setDurationTimeArray(int[] durationTimeArray) {
        this.durationTimeArray = durationTimeArray;
    }

    public int getSleepTime() {
        return this.sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getAwakeCount() {
        return this.awakeCount;
    }

    public void setAwakeCount(int awakeCount) {
        this.awakeCount = awakeCount;
    }

    public int getLightTime() {
        return this.lightTime;
    }

    public void setLightTime(int lightTime) {
        this.lightTime = lightTime;
    }

    public int getDeepTime() {
        return this.deepTime;
    }

    public void setDeepTime(int deepTime) {
        this.deepTime = deepTime;
    }

    public int getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(int sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public int[] getSleepStatusArray() {
        return sleepStatusArray;
    }

    public void setSleepStatusArray(int[] sleepStatusArray) {
        this.sleepStatusArray = sleepStatusArray;
    }

    public int[] getTimePointArray() {
        return timePointArray;
    }

    public void setTimePointArray(int[] timePointArray) {
        this.timePointArray = timePointArray;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
