package com.coband.cocoband.mvp.model.entity;

import java.util.List;

/**
 * Created by tgc on 17-9-21.
 * the class mapping leancloud BloodPressureInfo
 */

public class BloodPressureInfo {

    private String updatedAt;
    private String objectId;
    private String createdAt;
    private String deviceBleMacAddress;
    private String deviceType;
    private long timestamp;
    private UserPointer bloodPressureWithUser;
    private List<BloodPressuresBean> bloodPressures;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceBleMacAddress() {
        return deviceBleMacAddress;
    }

    public void setDeviceBleMacAddress(String deviceBleMacAddress) {
        this.deviceBleMacAddress = deviceBleMacAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UserPointer getBloodPressureWithUser() {
        return bloodPressureWithUser;
    }

    public void setBloodPressureWithUser(UserPointer bloodPressureWithUser) {
        this.bloodPressureWithUser = bloodPressureWithUser;
    }

    public List<BloodPressuresBean> getBloodPressures() {
        return bloodPressures;
    }

    public void setBloodPressures(List<BloodPressuresBean> bloodPressures) {
        this.bloodPressures = bloodPressures;
    }

    public static class BloodPressuresBean {
        private long time;
        private int heartRate;
        private int highPressure;
        private int lowPressure;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }

        public int getHighPressure() {
            return highPressure;
        }

        public void setHighPressure(int highPressure) {
            this.highPressure = highPressure;
        }

        public int getLowPressure() {
            return lowPressure;
        }

        public void setLowPressure(int lowPressure) {
            this.lowPressure = lowPressure;
        }
    }
}
