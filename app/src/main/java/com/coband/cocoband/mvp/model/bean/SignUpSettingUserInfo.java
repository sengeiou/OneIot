package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-5-22.
 */

public class SignUpSettingUserInfo {
    private double height;
    private String nickName;
    private int sex;
    private double weight;
    private int unit;
    private String birthdayData;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getBirthdayData() {
        return birthdayData;
    }

    public void setBirthdayData(String birthdayData) {
        this.birthdayData = birthdayData;
    }
}
