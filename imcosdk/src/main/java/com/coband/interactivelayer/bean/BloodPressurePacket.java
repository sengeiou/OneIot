package com.coband.interactivelayer.bean;

/**
 * Created by mai on 17-9-15.
 */

public class BloodPressurePacket {
    private long timeStamp;
    private int second;
    private int heartRate;
    private int bpLow;
    private int bpHigh;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getBpLow() {
        return bpLow;
    }

    public void setBpLow(int bpLow) {
        this.bpLow = bpLow;
    }

    public int getBpHigh() {
        return bpHigh;
    }

    public void setBpHigh(int bpHigh) {
        this.bpHigh = bpHigh;
    }
}
