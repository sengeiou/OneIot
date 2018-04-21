package com.coband.cocoband.mvp.model.bean;

import com.coband.common.utils.CocoUtils;

import java.io.Serializable;

/**
 * Created by mqh on 9/23/16.
 */
public class WeightBean implements Serializable {

    public int id;
    public int itemMode;
    public long timestamp;
    private float weight;
    public float bodyMI;
    public int unit;// 0: Metric    1 :Inch
    public final static int metric = 0;
    public final static int inch = 1;

    public WeightBean() {
    }

    public WeightBean(int itemMode, long timestamp, float weight) {
        this.itemMode = itemMode;
        this.timestamp = timestamp;
        this.weight = weight;
    }

    public WeightBean(int itemMode, long timestamp, float weight, float bodyMI, int unit) {
        this.itemMode = itemMode;
        this.timestamp = timestamp;
        this.weight = weight;
        this.unit = unit;
        this.bodyMI = bodyMI;
    }

    public WeightBean(long timestamp, float weight, float bodyMI, int unit) {
        this.timestamp = timestamp;
        this.weight = weight;
        this.unit = unit;
        this.bodyMI = bodyMI;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        if (!CocoUtils.isMetric() && this.unit == metric) {
            return (float) Math.round((this.weight * 2.2) * 10) / 10;
        } else if (CocoUtils.isMetric() && this.unit == inch) {
            return (float) Math.round((this.weight / 2.2) * 10) / 10;
        } else {
            return (float) Math.round(this.weight * 10) / 10;
        }
    }

}
