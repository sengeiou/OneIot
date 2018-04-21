package com.coband.interactivelayer.bean;

/**
 * Created by mai on 17-7-20.
 */

public class StepPacket {
    private int mOffset;    // Starting at 0 o'clock every day, offset by +1 every 15 minutes
    private int mMode;
    private int mStepCount;
    private int mActiveTime; // Unit is minute ， The total active time of the day,
    private int mCalory;
    private int mDistance;

    public int getmOffset() {
        return mOffset;
    }

    public void setmOffset(int mOffset) {
        this.mOffset = mOffset;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }

    public int getmStepCount() {
        return mStepCount;
    }

    public void setmStepCount(int mStepCount) {
        this.mStepCount = mStepCount;
    }

    public int getmActiveTime() {
        return mActiveTime;
    }

    public void setmActiveTime(int mActiveTime) {
        this.mActiveTime = mActiveTime;
    }

    public int getmCalory() {
        return mCalory;
    }

    public void setmCalory(int mCalory) {
        this.mCalory = mCalory;
    }

    public int getmDistance() {
        return mDistance;
    }

    public void setmDistance(int mDistance) {
        this.mDistance = mDistance;
    }
}
