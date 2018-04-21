package com.coband.cocoband.mvp.model.bean;

/**
 * Created by ivan on 17-4-24.
 * the sport info object mapping leancloud.
 */

public class SportInfo {
    private boolean finishCaloriesTarget;
    private long activityTotalTime;
    private long walkTotalCount;
    private long sportTimeTarget;
    private int walkTarget;
    private String sportInfoWithUser;
    private boolean finishWalkTarget;
    private int caloriesTotalNumber;
    private String sportInfoDevice;
    private boolean finishTimeTarget;
    private long timestamp;
    private int caloriesTarget;
    private long distance;
    private String deviceType;
    private String deviceBleMacAddress;

    public boolean isFinishCaloriesTarget() {
        return finishCaloriesTarget;
    }

    public void setFinishCaloriesTarget(boolean finishCaloriesTarget) {
        this.finishCaloriesTarget = finishCaloriesTarget;
    }

    public long getActivityTotalTime() {
        return activityTotalTime;
    }

    public void setActivityTotalTime(long activityTotalTime) {
        this.activityTotalTime = activityTotalTime;
    }

    public long getWalkTotalCount() {
        return walkTotalCount;
    }

    public void setWalkTotalCount(long walkTotalCount) {
        this.walkTotalCount = walkTotalCount;
    }

    public long getSportTimeTarget() {
        return sportTimeTarget;
    }

    public void setSportTimeTarget(long sportTimeTarget) {
        this.sportTimeTarget = sportTimeTarget;
    }

    public int getWalkTarget() {
        return walkTarget;
    }

    public void setWalkTarget(int walkTarget) {
        this.walkTarget = walkTarget;
    }

    public String getSportInfoWithUser() {
        return sportInfoWithUser;
    }

    public void setSportInfoWithUser(String sportInfoWithUser) {
        this.sportInfoWithUser = sportInfoWithUser;
    }

    public boolean isFinishWalkTarget() {
        return finishWalkTarget;
    }

    public void setFinishWalkTarget(boolean finishWalkTarget) {
        this.finishWalkTarget = finishWalkTarget;
    }

    public int getCaloriesTotalNumber() {
        return caloriesTotalNumber;
    }

    public void setCaloriesTotalNumber(int caloriesTotalNumber) {
        this.caloriesTotalNumber = caloriesTotalNumber;
    }

    public String getSportInfoDevice() {
        return sportInfoDevice;
    }

    public void setSportInfoDevice(String sportInfoDevice) {
        this.sportInfoDevice = sportInfoDevice;
    }

    public boolean isFinishTimeTarget() {
        return finishTimeTarget;
    }

    public void setFinishTimeTarget(boolean finishTimeTarget) {
        this.finishTimeTarget = finishTimeTarget;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCaloriesTarget() {
        return caloriesTarget;
    }

    public void setCaloriesTarget(int caloriesTarget) {
        this.caloriesTarget = caloriesTarget;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBleMacAddress() {
        return deviceBleMacAddress;
    }

    public void setDeviceBleMacAddress(String deviceBleMacAddress) {
        this.deviceBleMacAddress = deviceBleMacAddress;
    }
}
