package com.coband.cocoband.mvp.model.bean;

import java.util.List;

/**
 * Created by mqh on 5/4/16.
 */
public class RateOneDayDetailBean {
    private int averageHeartRate;
    private int highestHeartRate;
    private int lowestHeartRate;
    private List<RateOneDayBean> oneDayHeartRates;

    public RateOneDayDetailBean(int averageHeartRate, int highestHeartRate, int lowestHeartRate, List<RateOneDayBean> oneDayHeartRates) {
        this.averageHeartRate = averageHeartRate;
        this.highestHeartRate = highestHeartRate;
        this.lowestHeartRate = lowestHeartRate;
        this.oneDayHeartRates = oneDayHeartRates;
    }

    public List<RateOneDayBean> getOneDayHeartRates() {
        return oneDayHeartRates;
    }

    public void setOneDayHeartRates(List<RateOneDayBean> oneDayHeartRates) {
        this.oneDayHeartRates = oneDayHeartRates;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public int getHighestHeartRate() {
        return highestHeartRate;
    }

    public void setHighestHeartRate(int highestHeartRate) {
        this.highestHeartRate = highestHeartRate;
    }

    public int getLowestHeartRate() {
        return lowestHeartRate;
    }

    public void setLowestHeartRate(int lowestHeartRate) {
        this.lowestHeartRate = lowestHeartRate;
    }

}
