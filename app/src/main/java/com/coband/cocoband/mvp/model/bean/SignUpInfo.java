package com.coband.cocoband.mvp.model.bean;

import com.coband.watchassistant.BuildConfig;

import javax.inject.Inject;

import dagger.Module;

/**
 * <<<<<<< HEAD
 * Created by tgc on 17-4-26.
 * =======
 * Created by tgc on 17-4-21.
 * >>>>>>> refactoring_mai
 */

@Module
public class SignUpInfo {

    @Inject
    public SignUpInfo() {

    }

    private String username;
    private String password;
    private String email;
    private String source = BuildConfig.SOURCE;
    private String userDeviceSystemVersion = android.os.Build.MANUFACTURER
            + " " + android.os.Build.MODEL;

    private Birthday birthday;
    private double height;
    private String nickName;
    private int sex;
    private double weight;
    private int unit;
    private String appVersion;
    private long beginSportTimestamp;

    //walkTarget,sleepTarget,weightTarget 为 默认数据
    private int walkTarget;
    private int sleepTarget;
    private double weightTarget;

    public static class Birthday {
        public String __type = "Date";
        public String iso;

        public void setIso(String iso) {
            this.iso = iso;
        }

        public String getIso() {
            return iso;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getBeginSportTimestamp() {
        return beginSportTimestamp;
    }

    public void setBeginSportTimestamp(long beginSportTimestamp) {
        this.beginSportTimestamp = beginSportTimestamp;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getWalkTarget() {
        return walkTarget;
    }

    public void setWalkTarget(int walkTarget) {
        this.walkTarget = walkTarget;
    }

    public int getSleepTarget() {
        return sleepTarget;
    }

    public void setSleepTarget(int sleepTarget) {
        this.sleepTarget = sleepTarget;
    }

    public double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(double weightTarget) {
        this.weightTarget = weightTarget;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public void setBirthday(Birthday birthday) {
        this.birthday = birthday;
    }

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
}
