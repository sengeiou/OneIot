package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-10-9.
 */

public class LastRate {
    private long mDate;
    private SingleRate mSingleRate;

    public LastRate(long date, SingleRate rate) {
        this.mDate = date;
        this.mSingleRate = rate;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public SingleRate getSingleRate() {
        return mSingleRate;
    }

    public void setSingleRate(SingleRate singleRate) {
        mSingleRate = singleRate;
    }
}
