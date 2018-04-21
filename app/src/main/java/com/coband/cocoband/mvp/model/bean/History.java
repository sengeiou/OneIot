package com.coband.cocoband.mvp.model.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by imco on 3/14/16.
 */
@AVClassName("SportInfo")
public class History extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;

    public static final String FINISH_CAL_TARGET = "finishCaloriesTarget";
    public static final String ACTIVITY_TOTAL_TIME = "activityTotalTime";
    public static final String WALK_TOTAL_COUNT = "walkTotalCount";
    public static final String SPORT_TIME_TARGET = "sportTimeTarget";
    public static final String WALK_TARGET = "walkTarget";
    public static final String SPORT_INFO_WITH_USER = "sportInfoWithUser";
    public static final String FINISH_WALK_TARGET = "finishWalkTarget";
    public static final String CALORIES_TOTAL_NUMBER = "caloriesTotalNumber";
    public static final String SPORT_INFO_DEVICE = "sportInfoDevice";
    public static final String FINISH_TIME_TARGET = "finishTimeTarget";
    public static final String TIMESTAMP = "timeStamp";
    public static final String CALORIES_TARGET = "caloriesTarget";
    public static final String DISTANCE = "distance";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String DEVICE_BLE_MAC_ADDRESS = "deviceBleMacAddress";

    public void setDistance(double distance) {
        put(DISTANCE, distance);
    }

    public double getDistance() {
        return getDouble(DISTANCE);
    }

    public void setFinishCalTarget(boolean finish) {
        put(FINISH_CAL_TARGET, finish);
    }

    public void setActivityTotalTime(long totalTime) {
        put(ACTIVITY_TOTAL_TIME, totalTime);
    }

    public void setWalkTotalCount(long totalStep) {
        put(WALK_TOTAL_COUNT, totalStep);
    }

    public void setSportTimeTarget(long timeTarget) {
        put(SPORT_TIME_TARGET, timeTarget);
    }

    public void setWalkTarget(long stepTarget) {
        put(WALK_TARGET, stepTarget);
    }

    public void setSportInfoWithUser(AVUser user) {
        put(SPORT_INFO_WITH_USER, user);
    }

    public void setFinishWalkTarget(boolean finish) {
        put(FINISH_WALK_TARGET, finish);
    }

    public void setCaloriesTotalNumber(double totalCal) {
        put(CALORIES_TOTAL_NUMBER, totalCal);
    }

    public void setSportInfoDevice(String device) {
        put(SPORT_INFO_DEVICE, device);
    }

    public void setFinishTimeTarget(boolean finish) {
        put(FINISH_TIME_TARGET, finish);
    }

    public void setTimestamp(long date) {
        put(TIMESTAMP, date);
    }

    public void setCaloriesTarget(double calTarget) {
        put(CALORIES_TARGET, calTarget);
    }


    public boolean getFinishCalTarget() {
        return getBoolean(FINISH_CAL_TARGET);
    }

    public long getActivityTotalTime() {
        return getLong(ACTIVITY_TOTAL_TIME);
    }

    public long getWalkTotalCount() {
        return getLong(WALK_TOTAL_COUNT);
    }

    public long getSportTimeTarget() {
        return getLong(SPORT_TIME_TARGET);
    }

    public long getWalkTarget() {
        return getLong(WALK_TARGET);
    }

    public AVUser getSportInfoWithUser() {
        return getAVUser(SPORT_INFO_WITH_USER);
    }

    public boolean getFinishWalkTarget() {
        return getBoolean(FINISH_WALK_TARGET);
    }

    public double getCaloriesTotalNumber() {
        return getDouble(CALORIES_TOTAL_NUMBER);
    }

    public String getSportInfoDevice() {
        return getString(SPORT_INFO_DEVICE);
    }

    public boolean getFinishTimeTarget() {
        return getBoolean(FINISH_TIME_TARGET);
    }

    public long getTimestamp() {
        return getLong(TIMESTAMP);
    }

    public double getCaloriesTarget() {
        return getDouble(CALORIES_TARGET);
    }

    public void setDeviceType(String deviceType) {
        put(DEVICE_TYPE, deviceType);
    }

    public String getDeviceType() {
        return getString(DEVICE_TYPE);
    }

    public String getDeviceBleMacAddress() {
        return getString(DEVICE_BLE_MAC_ADDRESS);
    }

    public void setDeviceBleMacAddress(String macAddress) {
        put(DEVICE_BLE_MAC_ADDRESS, macAddress);
    }
}
