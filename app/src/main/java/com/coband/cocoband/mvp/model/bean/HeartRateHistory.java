package com.coband.cocoband.mvp.model.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.List;


@AVClassName("HeartRateInfo")
public class HeartRateHistory extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;

    public static final String DEVICE_BLE_MAC_ADDRESS = "deviceBleMacAddress";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String HEART_WITH_USER = "heartWithUser";
    public static final String HEART_RATES = "heartRates";
    public static final String TIMESTAMP = "timeStamp";


    public List getHeartRates() {
        return getList(HEART_RATES);
    }

    public void setHeartRates(List heartRates) {
        put(HEART_RATES, heartRates);
    }

    public AVUser getHeartWithUser() {
        return getAVUser(HEART_WITH_USER);
    }

    public void setHeartWithUser(AVUser avUser) {
        put(HEART_WITH_USER, avUser);
    }

    public String getDeviceType() {
        return getString(DEVICE_TYPE);
    }

    public void setDeviceType(String deviceType) {
        put(DEVICE_TYPE, deviceType);
    }

    public String getDeviceBleMacAddress() {
        return getString(DEVICE_BLE_MAC_ADDRESS);
    }

    public void setDeviceBleMacAddress(String macAddress) {
        put(DEVICE_BLE_MAC_ADDRESS, macAddress);
    }

    public void setTimestamp(long timestamp) {
        put(TIMESTAMP, timestamp);
    }

    public long getTimestamp(){
        return getLong(TIMESTAMP);
    }
}
