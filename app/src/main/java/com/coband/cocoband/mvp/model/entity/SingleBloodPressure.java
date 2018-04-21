package com.coband.cocoband.mvp.model.entity;

/**
 * Created by tgc on 17-9-21.
 */

public class SingleBloodPressure {

    private long time;
    private int heartRate;
    private int highPressure;
    private int lowPressure;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(int highPressure) {
        this.highPressure = highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(int lowPressure) {
        this.lowPressure = lowPressure;
    }
}
