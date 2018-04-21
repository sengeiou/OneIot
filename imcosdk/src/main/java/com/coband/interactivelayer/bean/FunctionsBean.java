package com.coband.interactivelayer.bean;

/**
 * Created by mai on 17-9-27.
 */

public class FunctionsBean {
    private boolean fakeBP;
    private boolean realBP;
    private boolean portrait_landscape;
    private boolean heartRate;
    private boolean stepCounter;
    private boolean sleep;
    private boolean wechat;

    public FunctionsBean() {
    }

    public FunctionsBean(boolean fakeBP, boolean realBP, boolean portrait_landscape, boolean heartRate, boolean stepCounter, boolean sleep) {
        this.fakeBP = fakeBP;
        this.realBP = realBP;
        this.portrait_landscape = portrait_landscape;
        this.heartRate = heartRate;
        this.stepCounter = stepCounter;
        this.sleep = sleep;
    }

    public boolean isWechat() {
        return wechat;
    }

    public void setWechat(boolean wechat) {
        this.wechat = wechat;
    }

    public boolean isFakeBP() {
        return fakeBP;
    }

    public void setFakeBP(boolean fakeBP) {
        this.fakeBP = fakeBP;
    }

    public boolean isRealBP() {
        return realBP;
    }

    public void setRealBP(boolean realBP) {
        this.realBP = realBP;
    }

    public boolean isPortrait_landscape() {
        return portrait_landscape;
    }

    public void setPortrait_landscape(boolean portrait_landscape) {
        this.portrait_landscape = portrait_landscape;
    }

    public boolean isHeartRate() {
        return heartRate;
    }

    public void setHeartRate(boolean heartRate) {
        this.heartRate = heartRate;
    }

    public boolean isStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(boolean stepCounter) {
        this.stepCounter = stepCounter;
    }

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }
}
