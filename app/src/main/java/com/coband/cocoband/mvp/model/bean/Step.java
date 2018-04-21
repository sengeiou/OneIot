package com.coband.cocoband.mvp.model.bean;

/**
 * Created by ivan on 17-4-20.
 */

public class Step {
    private long mStep;
    private double mDistance;
    private double mCalories;

    public Step() {

    }

    public Step(long step, double calories, double distance) {
        this.mStep = step;
        this.mCalories = calories;
        this.mDistance = distance;
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
}
