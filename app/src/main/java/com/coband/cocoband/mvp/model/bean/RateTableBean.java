package com.coband.cocoband.mvp.model.bean;


/**
 * Created by mqh on 3/24/16.
 */
public class RateTableBean {
    private String date;
    private String time;
    private String measure;//what state measure
    private String feel;//what it feels like when you measure
    private String heartRate;

    public RateTableBean(String date, String time, String heartRate,String measure, String feel){
        this.date = date;
        this.time = time;
        this.measure = measure;
        this.feel = feel;
        this.heartRate = heartRate;
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }


}
