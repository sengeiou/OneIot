package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 6/18/17.
 */

public class HeartRateSettings {
    private boolean mRealTimeRate;
    private boolean mHighRateRemind;
    private int mHighRateValue;
    private boolean mScheduleMeasure;
    private int mScheduleMeasureTime;

    public boolean isRealTimeRate() {
        return mRealTimeRate;
    }

    public void setRealTimeRate(boolean realTimeRate) {
        mRealTimeRate = realTimeRate;
    }

    public boolean isHighRateRemind() {
        return mHighRateRemind;
    }

    public void setHighRateRemind(boolean highRateRemind) {
        mHighRateRemind = highRateRemind;
    }

    public int getHighRateValue() {
        return mHighRateValue;
    }

    public void setHighRateValue(int highRateValue) {
        mHighRateValue = highRateValue;
    }

    public boolean isScheduleMeasure() {
        return mScheduleMeasure;
    }

    public void setScheduleMeasure(boolean scheduleMeasure) {
        mScheduleMeasure = scheduleMeasure;
    }

    public int getScheduleMeasureTime() {
        return mScheduleMeasureTime;
    }

    public void setScheduleMeasureTime(int scheduleMeasureTime) {
        mScheduleMeasureTime = scheduleMeasureTime;
    }
}
