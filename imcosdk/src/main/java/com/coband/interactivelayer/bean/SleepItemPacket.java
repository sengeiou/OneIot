package com.coband.interactivelayer.bean;

/**
 * Created by mai on 17-8-31.
 */

public class SleepItemPacket {
    private int mMinutes;			// 16bits
    private int mMode;				// 4bits

    public SleepItemPacket(int mMinutes, int mMode) {
        this.mMinutes = mMinutes;
        this.mMode = mMode;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public int getMode() {
        return mMode;
    }
}
