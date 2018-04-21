package com.coband.cocoband.mvp.model.entity;

import com.coband.watchassistant.HistoryData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ivan on 17-6-13.
 */

public class MultiDayStepInfo {
    private long mTotalSteps;
    private int mFinishTargetCount;
    private float mAveCalories;
    private float mAveDistance;
    private long mAveSteps;
    private int mUnit;
    private List<HistoryData> mDayStepInfoList = new ArrayList<>();
    private List<Date> mDateList;

    public List<Date> getDateList() {
        return mDateList;
    }

    public void setDateList(List<Date> dateList) {
        mDateList = dateList;
    }

    public int getUnit() {
        return mUnit;
    }

    public void setUnit(int unit) {
        mUnit = unit;
    }


    public long getTotalSteps() {
        return mTotalSteps;
    }

    public void setTotalSteps(long totalSteps) {
        mTotalSteps = totalSteps;
    }

    public int getFinishTargetCount() {
        return mFinishTargetCount;
    }

    public void setFinishTargetCount(int finishTargetCount) {
        mFinishTargetCount = finishTargetCount;
    }

    public float getAveCalories() {
        return mAveCalories;
    }

    public void setAveCalories(float aveCalories) {
        mAveCalories = aveCalories;
    }

    public float getAveDistance() {
        return mAveDistance;
    }

    public void setAveDistance(float aveDistance) {
        mAveDistance = aveDistance;
    }

    public long getAveSteps() {
        return mAveSteps;
    }

    public void setAveSteps(long aveSteps) {
        mAveSteps = aveSteps;
    }

    public List<HistoryData> getDayStepInfoList() {
        return mDayStepInfoList;
    }

    public void setDayStepInfoList(List<HistoryData> dayStepInfoList) {
        mDayStepInfoList = dayStepInfoList;
    }
}
