package com.coband.cocoband.mvp.model.bean;

/**
 * Created by imco on 4/29/16.
 */
public class SleepBarEntry {

    public static final int DEEP = 0;
    public static final int LIGHT = 1;
    public static final int AWAKE = 2;

    private int type;
    private float spacePrecent;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getSpacePrecent() {
        return spacePrecent;
    }

    public void setSpacePercent(float spacePrecent) {
        this.spacePrecent = spacePrecent;
    }
}
