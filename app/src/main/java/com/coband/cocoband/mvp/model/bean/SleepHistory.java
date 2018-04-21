package com.coband.cocoband.mvp.model.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by imco on 3/14/16.
 */
@AVClassName("SleepInfo")
public class SleepHistory extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;

    public static final String AWAKE_TIMES = "awakeTime";
    public static final String DEEP_SLEEP_TIME = "deepSleepTime";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String FINISH_TARGET = "finishTarget";
    public static final String LIGHT_SLEEP_TIME = "lightSleepTime";
    public static final String SLEEP_TARGET = "sleepTarget";
    public static final String SLEEP_TOTAL_TIME = "sleepTotalTime";
    public static final String SLEEP_WITH_OWN_DEVICE = "sleepWithOwnDevice";
    public static final String SLEEP_WITH_OWN_USER = "sleepWithOwnUser";
    public static final String TIMESTAMP = "timeStamp";
    public static final String TYPE = "type"; //Sleep Quality 0 excellent; 1 good; 2 poor
    public static final String DEVICE_BLE_MAC_ADDRESS = "deviceBleMacAddress";

    public void setSleepTarget(int sleepTarget) {
        put(SLEEP_TARGET, sleepTarget);
    }

    public int getSleepTarget() {
        return getInt(SLEEP_TARGET);
    }

    public void setAwakeTimes(int awakeTimes) {
        put(AWAKE_TIMES, awakeTimes);
    }

    public int getAwakeTimes() {
        return getInt(AWAKE_TIMES);
    }

    public void setFinishTarget(boolean isFinish) {
        put(FINISH_TARGET, isFinish);
    }

    public boolean getFinishTarget() {
        return getBoolean(FINISH_TARGET)? false:true;
    }

    public void setSleepTotalTime(int sleepTotalTime) {
        put(SLEEP_TOTAL_TIME, sleepTotalTime);
    }

    public int getSleepTotalTime() {
        return getInt(SLEEP_TOTAL_TIME);
    }

    public void setSleepQuality(int type) {
        put(TYPE, type);
    }

    public int getSleepQuality() {
        return getInt(TYPE);
    }

    public void setSleepWithOwnDevice(AVObject sleepWithOwnDevice) {
        put(SLEEP_WITH_OWN_DEVICE, sleepWithOwnDevice);
    }

    public AVObject getSleepWithOwnDevice(){
        return getAVObject(SLEEP_WITH_OWN_DEVICE);
    }

    public void setLightSleepTime(int lightSleepTime) {
        put(LIGHT_SLEEP_TIME, lightSleepTime);
    }

    public int getLightSleepTime() {
        return getInt(LIGHT_SLEEP_TIME);
    }

    public void setDeviceType(String deviceType) {
        put(DEVICE_TYPE, deviceType);
    }

    public String getDeviceType() {
        return getString(DEVICE_TYPE);
    }

    public void setDeepSleepTime(int deepSleepTime) {
        put(DEEP_SLEEP_TIME, deepSleepTime);
    }

    public int getDeepSleepTime() {
        return getInt(DEEP_SLEEP_TIME);
    }

    public void setSleepWithOwnUser(AVUser user) {
        put(SLEEP_WITH_OWN_USER, user);
    }

    public void setTimestamp(long timestamp) {
        put(TIMESTAMP, timestamp);
    }

    public long getTimestamp(){
        return getLong(TIMESTAMP);
    }

    public String getDeviceBleMacAddress() {
        return getString(DEVICE_BLE_MAC_ADDRESS);
    }

    public void setDeviceBleMacAddress(String macAddress) {
        put(DEVICE_BLE_MAC_ADDRESS, macAddress);
    }
}
