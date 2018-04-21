package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-5-8.
 * the class mapping leancloud SleepInfo
 */

public class SleepInfo {
    /**
     * sleepTarget : 480
     * updatedAt : 2017-04-10T07:46:05.107Z
     * finishTarget : false
     * objectId : 58eb383d61ff4b0061a90ffd
     * sleepTotalTime : 15
     * createdAt : 2017-04-10T07:46:05.107Z
     * type : 1
     * deviceBleMacAddress : 57:43:49:48:42:0C
     * lightSleepTime : 0
     * deviceType : CoBand
     * awakeTime : 0
     * deepSleepTime : 15
     * sleepWithOwnUser : {"__type":"Pointer","className":"_User","objectId":"581fd4f167f3560058a52813"}
     * timeStamp : -55058342743000
     */

    private int sleepTarget;
    private String updatedAt;
    private boolean finishTarget;
    private String objectId;
    private int sleepTotalTime;
    private String createdAt;
    private int type;
    private String deviceBleMacAddress;
    private int lightSleepTime;
    private String deviceType;
    private int awakeTime;
    private int deepSleepTime;
    private UserPointer sleepWithOwnUser;
    private long timestamp;

    public int getSleepTarget() {
        return sleepTarget;
    }

    public void setSleepTarget(int sleepTarget) {
        this.sleepTarget = sleepTarget;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isFinishTarget() {
        return finishTarget;
    }

    public void setFinishTarget(boolean finishTarget) {
        this.finishTarget = finishTarget;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getSleepTotalTime() {
        return sleepTotalTime;
    }

    public void setSleepTotalTime(int sleepTotalTime) {
        this.sleepTotalTime = sleepTotalTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceBleMacAddress() {
        return deviceBleMacAddress;
    }

    public void setDeviceBleMacAddress(String deviceBleMacAddress) {
        this.deviceBleMacAddress = deviceBleMacAddress;
    }

    public int getLightSleepTime() {
        return lightSleepTime;
    }

    public void setLightSleepTime(int lightSleepTime) {
        this.lightSleepTime = lightSleepTime;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getAwakeTime() {
        return awakeTime;
    }

    public void setAwakeTime(int awakeTime) {
        this.awakeTime = awakeTime;
    }

    public int getDeepSleepTime() {
        return deepSleepTime;
    }

    public void setDeepSleepTime(int deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }

    public UserPointer getSleepWithOwnUser() {
        return sleepWithOwnUser;
    }

    public void setSleepWithOwnUser(UserPointer sleepWithOwnUser) {
        this.sleepWithOwnUser = sleepWithOwnUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
