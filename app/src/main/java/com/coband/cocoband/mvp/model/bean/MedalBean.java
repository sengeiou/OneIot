package com.coband.cocoband.mvp.model.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by tgc on 17-5-10.
 */

public class MedalBean {
    private String medalName;
    private String medalTips;
    @DrawableRes
    private int medalRes;
    private boolean isGet;

    public String getMedalName() {
        return medalName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public String getMedalTips() {
        return medalTips;
    }

    public void setMedalTips(String medalTips) {
        this.medalTips = medalTips;
    }

    public int getMedalRes() {
        return medalRes;
    }

    public void setMedalRes(int medalRes) {
        this.medalRes = medalRes;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }
}
