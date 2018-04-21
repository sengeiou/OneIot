package com.coband.interactivelayer.bean;

/**
 * Created by mai on 17-8-31.
 */

public class SportItemPacket {
    // Parameters
    private int mOffset;            // 11bits
    private int mMode;                // 2bits 0x0 : Static; 0x1 : Walk; 0x2 : Run
    private int mStepCount;            // 12bits
    private int mActiveTime;        // 4bits
    private int mCalory;            // 19bits
    private int mDistance;            // 16bits


    public SportItemPacket(int mOffset, int mMode, int mStepCount, int mActiveTime, int mCalory, int mDistance) {
        this.mOffset = mOffset;
        this.mMode = mMode;
        this.mStepCount = mStepCount;
        this.mActiveTime = mActiveTime;
        this.mCalory = mCalory;
        this.mDistance = mDistance;
    }

    public int getOffset() {
        return mOffset;
    }

    public int getMode() {
        return mMode;
    }

    public int getStepCount() {
        return mStepCount;
    }

    public int getActiveTime() {
        return mActiveTime;
    }

    public int getCalory() {
        return mCalory;
    }

    public int getDistance() {
        return mDistance;
    }
}
