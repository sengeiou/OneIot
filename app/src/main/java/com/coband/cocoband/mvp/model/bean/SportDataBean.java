package com.coband.cocoband.mvp.model.bean;

import java.util.List;

/**
 * Created by mqh on 3/22/16.
 */
public class SportDataBean {
    private long totalStepCount;
    private int completeCount;
    private float totalDistance;
    private int totalCalories;
    private int averageStepCount;
    private List<EachDayDateBean> eachDayStepCount;

    public SportDataBean(){}
    public SportDataBean(long totalStepCount, int completeCount, float totalDistance, int totalCalories, int averageStepCount, List<EachDayDateBean> eachDayStepCount){
        this.totalStepCount = totalStepCount;
        this.completeCount = completeCount;
        this.totalDistance = totalDistance;
        this.totalCalories = totalCalories;
        this.averageStepCount = averageStepCount;
        this.eachDayStepCount = eachDayStepCount;
    }


    public int getAverageStepCount() {
        return averageStepCount;
    }

    public void setAverageStepCount(int averageStepCount) {
        this.averageStepCount = averageStepCount;
    }

    public long getTotalStepCount() {
        return this.totalStepCount;
    }

    public void setTotalStepCount(long totalStepCount) {
        this.totalStepCount = totalStepCount;
    }

    public int getCompleteCount() {
        return this.completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public float getTotalDistance() {
        return this.totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }


    public List<EachDayDateBean> getEachDayStepCount() {
        return eachDayStepCount;
    }

    public void setEachDayStepCount(List<EachDayDateBean> eachDayStepCount) {
        this.eachDayStepCount = eachDayStepCount;
    }

    public int getTotalCalories() {
        return this.totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

}
