package com.coband.interactivelayer.bean;

import java.util.ArrayList;

/**
 * Created by mai on 17-8-31.
 */

public class SportPacket {
    // Header
    private int mYear;            // 6bits
    private int mMonth;            // 4bits
    private int mDay;            // 5bits
    private int mItemCount;        // 8bits

    public SportPacket(int mYear, int mMonth, int mDay, int mItemCount, ArrayList<SportItemPacket> mSportItems) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mItemCount = mItemCount;
        this.mSportItems = mSportItems;
    }

    // Sport Item
    ArrayList<SportItemPacket> mSportItems = new ArrayList<>();

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

    public ArrayList<SportItemPacket> getSportItems() {
        return mSportItems;
    }
}
