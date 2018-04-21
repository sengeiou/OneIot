package com.coband.cocoband.mvp.model.bean;

import java.util.List;

/**
 * Created by mqh on 3/22/16.
 */
public class SleepDataBean {

    private int AverageSleepTime;
    private int AverageAwakeTime;
    private int AverageLightTime;
    private int AverageDeepTime;
    private int CompleteCount;
    private List<EachDayDateBean> eachDayDateBeans;

    public SleepDataBean(){}
    public SleepDataBean(int AverageSleepTime, int AverageAwakeTime,int AverageLightTime,int AverageDeepTime,int CompleteCount, List<EachDayDateBean> eachDayDateBeans){
        this.AverageSleepTime = AverageSleepTime;
        this.AverageAwakeTime = AverageAwakeTime;
        this.AverageLightTime = AverageLightTime;
        this.AverageDeepTime = AverageDeepTime;
        this.CompleteCount = CompleteCount;
        this.eachDayDateBeans = eachDayDateBeans;
    }

    public int getCompleteCount() {
        return CompleteCount;
    }

    public void setCompleteCount(int completeCount) {
        CompleteCount = completeCount;
    }

    public int getAverageSleepTime() {
        return AverageSleepTime;
    }

    public void setAverageSleepTime(int AverageSleepTime) {
        this.AverageSleepTime = AverageSleepTime;
    }

    public int getAverageAwakeTime() {
        return AverageAwakeTime;
    }

    public void setAverageAwakeTime(int AverageAwakeTime) {
        this.AverageAwakeTime = AverageAwakeTime;
    }

    public int getAverageLightTime() {
        return AverageLightTime;
    }

    public void setAverageLightTime(int AverageLightTime) {
        this.AverageLightTime = AverageLightTime;
    }

    public int getAverageDeepTime() {
        return AverageDeepTime;
    }

    public void setAverageDeepTime(int AverageDeepTime) {
        this.AverageDeepTime = AverageDeepTime;
    }


    public List<EachDayDateBean> getEachDayDateBeans() {
        return eachDayDateBeans;
    }

    public void setEachDayDateBeans(List<EachDayDateBean> eachDayDateBeans) {
        this.eachDayDateBeans = eachDayDateBeans;
    }

}
