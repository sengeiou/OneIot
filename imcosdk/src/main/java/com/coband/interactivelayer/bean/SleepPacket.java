package com.coband.interactivelayer.bean;

import java.util.ArrayList;

/**
 * Created by mai on 17-8-31.
 */

public class SleepPacket {
    private int mYear;            // 6bits
    private int mMonth;            // 4bits
    private int mDay;            // 5bits
    private int mItemCount;        // 8bits

    // Sport Item
    ArrayList<SleepItemPacket> mSleepItems = new ArrayList<SleepItemPacket>();

    public SleepPacket(int mYear, int mMonth, int mDay, int mItemCount, ArrayList<SleepItemPacket> mSleepItems) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mItemCount = mItemCount;
        this.mSleepItems = mSleepItems;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getItemCount() {
        return mItemCount;
    }

    public ArrayList<SleepItemPacket> getSleepItems() {
        return mSleepItems;
    }
}
