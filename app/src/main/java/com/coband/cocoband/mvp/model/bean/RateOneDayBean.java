package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mqh on 4/28/16.
 */
public class RateOneDayBean {

    private int time;

    private int rate;

    private long calendar;//Calendar

    public RateOneDayBean(int time, int rate) {
        this.time = time;
        this.rate = rate;
    }

    public RateOneDayBean(int time, int rate, long calendar){
        this.time = time;
        this.rate = rate;
        this.calendar = calendar;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getCalendar() {
        return calendar;
    }

    public void setCalendar(long calendar) {
        this.calendar = calendar;
    }

}
