package com.coband.cocoband.mvp.model.bean;

import com.yc.pedometer.info.StepOneHourInfo;

import java.util.List;

/**
 * Created by mqh on 3/26/16.
 */
public class SportDayDateBean {

    private String date;
    private int step;
    private float distance;
    private float calories;
    private String progressCompletion;
    private List<StepOneHourInfo> mOneHourInfos;

    public SportDayDateBean(String date, int step, float distance, float calories, String progressCompletion, List<StepOneHourInfo> mOneHourInfos) {
        this.date = date;
        this.step = step;
        this.distance = distance;
        this.calories = calories;
        this.progressCompletion = progressCompletion;
        this.mOneHourInfos = mOneHourInfos;
    }

    public List<StepOneHourInfo> getOneHourInfos() {
        return this.mOneHourInfos;
    }

    public void setOneHourInfos(List<StepOneHourInfo> mOneHourInfos) {
        this.mOneHourInfos = mOneHourInfos;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getProgressCompletion() {
        return this.progressCompletion;
    }

    public void setProgressCompletion(String progressCompletion) {
        this.progressCompletion = progressCompletion;
    }

}
