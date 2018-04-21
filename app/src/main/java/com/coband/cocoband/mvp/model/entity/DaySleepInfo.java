package com.coband.cocoband.mvp.model.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by ivan on 17-6-14.
 */

public class DaySleepInfo {
    @Retention(SOURCE)
    @IntDef({QUALITY_GOOD, QUALITY_LACK, QUALITY_POOR})
    public @interface SleepQuality {

    }

    public static final int DEEP = 0;
    public static final int LIGHT = 1;
    public static final int AWAKE = 2;


    public static final int QUALITY_GOOD = 2;
    public static final int QUALITY_LACK = 1;
    public static final int QUALITY_POOR = 0;

    private long mDate;
    private int mSleepTotalTime;
    private int mDeepTime;
    private int mLightTime;
    private int awakeCount;
    private int[] mStatusArray;
    private int[] mTimeOfStatus;
    private int[] mDurationTimeArray;

    public int[] getDurationTimeArray() {
        return mDurationTimeArray;
    }

    public void setDurationTimeArray(int[] durationTimeArray) {
        mDurationTimeArray = durationTimeArray;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public int getSleepTotalTime() {
        return mSleepTotalTime;
    }

    public void setSleepTotalTime(int sleepTotalTime) {
        mSleepTotalTime = sleepTotalTime;
    }

    public int getDeepTime() {
        return mDeepTime;
    }

    public void setDeepTime(int deepTime) {
        mDeepTime = deepTime;
    }

    public int getLightTime() {
        return mLightTime;
    }

    public void setLightTime(int lightTime) {
        mLightTime = lightTime;
    }

    public int getAwakeCount() {
        return awakeCount;
    }

    public void setAwakeCount(int awakeCount) {
        this.awakeCount = awakeCount;
    }

    public int[] getStatusArray() {
        return mStatusArray;
    }

    public void setStatusArray(int[] statusArray) {
        mStatusArray = statusArray;
    }

    public int[] getTimeOfStatus() {
        return mTimeOfStatus;
    }

    public void setTimeOfStatus(int[] timeOfStatus) {
        mTimeOfStatus = timeOfStatus;
    }

    public int getQuality() {
        if (getSleepTotalTime() < 360) {
            return DaySleepInfo.QUALITY_LACK;
        } else {
            return mDeepTime / ((float) mSleepTotalTime) * 100 < 25 ?
                    DaySleepInfo.QUALITY_POOR : DaySleepInfo.QUALITY_GOOD;
        }
    }
}
