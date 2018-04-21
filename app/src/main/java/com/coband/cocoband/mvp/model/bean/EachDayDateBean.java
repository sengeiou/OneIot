package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mqh on 3/26/16.
 */
public class EachDayDateBean {
    private int totalCount;
    private String Date;

    public EachDayDateBean() {
    }

    public EachDayDateBean(int totalCount, String date) {
        this.totalCount = totalCount;
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
