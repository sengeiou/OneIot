package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-5-19.
 */

public class TargetInfo {
    private int walkTarget;
    private int sleepTarget;
    private double weightTarget;

    public int getWalkTarget() {
        return walkTarget;
    }

    public void setWalkTarget(int walkTarget) {
        this.walkTarget = walkTarget;
    }

    public int getSleepTarget() {
        return sleepTarget;
    }

    public void setSleepTarget(int sleepTarget) {
        this.sleepTarget = sleepTarget;
    }

    public double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(double weightTarget) {
        this.weightTarget = weightTarget;
    }
}
