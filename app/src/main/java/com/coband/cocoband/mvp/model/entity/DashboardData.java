package com.coband.cocoband.mvp.model.entity;

import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.Sleep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 17-5-12.
 */

public class DashboardData {
    private long mStep;
    private double mDistance;
    private double mCalories;
    private double mWeight;
    private List<DBWeight> mWeekWeights = new ArrayList<>();
    private List<Sleep> mWeekSleeps = new ArrayList<>();
    private List<SingleRate> mHeartRates = new ArrayList<>();
    private int mSleepTotalTime;
    private int mAveHeartRate;
    private int mUnitSystem;
    private BloodPressure mPressure;
    private long mPressureDate;
    private LastRate mLastRate;
    private LastSleepData mLastSleepData;
    private List<DBWeight> mLastSevenDayWeight = new ArrayList<>();

    public LastSleepData getLastSleepData() {
        return mLastSleepData;
    }

    public void setLastSleepData(LastSleepData lastSleepData) {
        mLastSleepData = lastSleepData;
    }

    public LastRate getLastRate() {
        return mLastRate;
    }

    public void setLastRate(LastRate lastRate) {
        mLastRate = lastRate;
    }

    public long getPressureDate() {
        return mPressureDate;
    }

    public void setPressureDate(long mPressureDate) {
        this.mPressureDate = mPressureDate;
    }

    public BloodPressure getPressure() {
        return mPressure;
    }

    public void setPressure(BloodPressure pressure) {
        this.mPressure = pressure;
    }

    public int getUnitSystem() {
        return mUnitSystem;
    }

    public void setUnitSystem(int unitSystem) {
        mUnitSystem = unitSystem;
    }

    public int getAveHeartRate() {
        return mAveHeartRate;
    }

    public void setAveHeartRate(int aveHeartRate) {
        mAveHeartRate = aveHeartRate;
    }


    public int getSleepTotalTime() {
        return mSleepTotalTime;
    }

    public void setSleepTotalTime(int sleepTotalTime) {
        mSleepTotalTime = sleepTotalTime;
    }

    public long getStep() {
        return mStep;
    }

    public void setStep(long step) {
        mStep = step;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public double getCalories() {
        return mCalories;
    }

    public void setCalories(double calories) {
        mCalories = calories;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public List<DBWeight> getWeekWeights() {
        return mWeekWeights;
    }

    public void setWeekWeights(List<DBWeight> weekWeights) {
        mWeekWeights = weekWeights;
    }

    public List<Sleep> getWeekSleeps() {
        return mWeekSleeps;
    }

    public void setWeekSleeps(List<Sleep> weekSleeps) {
        mWeekSleeps = weekSleeps;
    }

    public List<SingleRate> getDayHeartRates() {
        return mHeartRates;
    }

    public void setDayHeartRates(List<SingleRate> dayHeartRates) {
        mHeartRates = dayHeartRates;
    }

    public void setLastSevenDayWeight(List<DBWeight> weights) {
        mLastSevenDayWeight = weights;
    }

    public List<DBWeight> getLastSevenDayWeight() {
        return mLastSevenDayWeight;
    }
}
