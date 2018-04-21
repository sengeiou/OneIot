package com.coband.cocoband.mvp.model.entity.request;

/**
 * Created by ivan on 3/15/18.
 */

public class UpdateTargetRequest {


    /**
     * stepTarget : 8000
     * weightTarget : 70
     * sleepTarget : 480
     */

    private int stepTarget;
    private double weightTarget;
    private int sleepTarget;

    public int getStepTarget() {
        return stepTarget;
    }

    public void setStepTarget(int stepTarget) {
        this.stepTarget = stepTarget;
    }

    public double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(double weightTarget) {
        this.weightTarget = weightTarget;
    }

    public int getSleepTarget() {
        return sleepTarget;
    }

    public void setSleepTarget(int sleepTarget) {
        this.sleepTarget = sleepTarget;
    }
}
