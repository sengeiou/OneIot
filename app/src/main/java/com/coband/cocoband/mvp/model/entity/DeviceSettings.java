package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-5-25.
 */

public class DeviceSettings {
    private int mDeviceType;
    private String mDeviceName;
    private int mScreenOffTime;
    private boolean mLiftWristScreenOnStatus;
    private boolean mLostWarningStatus;
    private boolean mDeviceAlarmStatus;
    private boolean mInCallRemindStatus;
    private boolean mScreenOrientationStatus;
    private boolean mTimeStyleStatus;
    private boolean mDisturbStatus;

    public boolean getDisturbStatus() {
        return mDisturbStatus;
    }

    public void setDisturbStatus(boolean disturbStatus) {
        mDisturbStatus = disturbStatus;
    }

    public boolean getScreenOrientationStatus() {
        return mScreenOrientationStatus;
    }

    public void setScreenOrientationStatus(boolean screenOrientationStatus) {
        mScreenOrientationStatus = screenOrientationStatus;
    }

    public boolean getTimeStyleStatus() {
        return mTimeStyleStatus;
    }

    public void setTimeStyleStatus(boolean timeStyleStatus) {
        mTimeStyleStatus = timeStyleStatus;
    }

    public int getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(int deviceType) {
        mDeviceType = deviceType;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public int getScreenOffTime() {
        return mScreenOffTime;
    }

    public void setScreenOffTime(int screenOffTime) {
        mScreenOffTime = screenOffTime;
    }

    public boolean getLiftWristScreenOnStatus() {
        return mLiftWristScreenOnStatus;
    }

    public void setLiftWristScreenOnStatus(boolean liftWristScreenOnStatus) {
        mLiftWristScreenOnStatus = liftWristScreenOnStatus;
    }

    public boolean getLostWarningStatus() {
        return mLostWarningStatus;
    }

    public void setLostWarningStatus(boolean lostWarningStatus) {
        mLostWarningStatus = lostWarningStatus;
    }

    public boolean getDeviceAlarmStatus() {
        return mDeviceAlarmStatus;
    }

    public void setDeviceAlarmStatus(boolean deviceAlarmStatus) {
        mDeviceAlarmStatus = deviceAlarmStatus;
    }

    public boolean getInCallRemindStatus() {
        return mInCallRemindStatus;
    }

    public void setInCallRemindStatus(boolean inCallRemindStatus) {
        mInCallRemindStatus = inCallRemindStatus;
    }
}
