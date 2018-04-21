package com.coband.cocoband.mvp.model.entity;

import com.coband.watchassistant.Sleep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ivan on 17-6-21.
 */

public class MultiDaySleepInfo {

    private long mTotalSleep;
    private int mFinishTargetCount;
    private int mAveSleep;
    private int mAveDeep;
    private int mAveLight;
    private int mAveWake;
    private List<Sleep> mDaySleepInfo = new ArrayList<>();
    private List<Date> mDateList = new ArrayList<>();

    public int getAveSleep() {
        return mAveSleep;
    }

    public void setAveSleep(int aveSleep) {
        mAveSleep = aveSleep;
    }

    public long getTotalSleep() {
        return mTotalSleep;
    }

    public void setTotalSleep(long totalSleep) {
        mTotalSleep = totalSleep;
    }

    public int getFinishTargetCount() {
        return mFinishTargetCount;
    }

    public void setFinishTargetCount(int finishTargetCount) {
        mFinishTargetCount = finishTargetCount;
    }

    public int getAveDeep() {
        return mAveDeep;
    }

    public void setAveDeep(int aveDeep) {
        mAveDeep = aveDeep;
    }

    public int getAveLight() {
        return mAveLight;
    }

    public void setAveLight(int aveLight) {
        mAveLight = aveLight;
    }

    public int getAveWake() {
        return mAveWake;
    }

    public void setAveWake(int aveWake) {
        mAveWake = aveWake;
    }

    public List<Sleep> getDaySleepInfoList() {
        return mDaySleepInfo;
    }

    public void setDaySleepInfoList(List<Sleep> daySleepInfoList) {
        mDaySleepInfo = daySleepInfoList;
    }

    public List<Date> getDateList() {
        return mDateList;
    }

    public void setDateList(List<Date> dateList) {
        mDateList = dateList;
    }
}
