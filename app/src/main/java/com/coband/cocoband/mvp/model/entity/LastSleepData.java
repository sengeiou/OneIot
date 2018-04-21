package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-10-9.
 */

public class LastSleepData {
    private long mDate;
    private int mTotalTime;
    private DaySleepInfo mDaySleepInfo;

    public LastSleepData(long date, int totalTime, DaySleepInfo daySleepInfo) {
        this.mDate = date;
        this.mTotalTime = totalTime;
        this.mDaySleepInfo = daySleepInfo;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(int totalTime) {
        mTotalTime = totalTime;
    }

    public DaySleepInfo getDaySleepInfo() {
        return mDaySleepInfo;
    }

    public void setDaySleepInfo(DaySleepInfo daySleepInfo) {
        mDaySleepInfo = daySleepInfo;
    }
}
