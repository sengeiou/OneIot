package com.coband.cocoband.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 17-6-12.
 */

public class DayStepInfo {
    private long mDate;
    private long mSteps;
    private double mDistance;
    private int mUnit;
    private double mCalories;
    private float mProgress;
    private List<StepInfo> mNodeInfoList = new ArrayList<>();

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public long getSteps() {
        return mSteps;
    }

    public void setSteps(long steps) {
        mSteps = steps;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public int getUnit() {
        return mUnit;
    }

    public void setUnit(int unit) {
        mUnit = unit;
    }

    public double getCalories() {
        return mCalories;
    }

    public void setCalories(double calories) {
        mCalories = calories;
    }

    public List<StepInfo> getNodeInfoList() {
        return mNodeInfoList;
    }

    public void setNodeInfoList(List<StepInfo> nodeInfoList) {
        mNodeInfoList = nodeInfoList;
    }
}
