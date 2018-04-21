package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-4-26.
 * the class mapping leancloud SportInfo
 */

public class SportInfo {
    public boolean finishCaloriesTarget;
    public int activityTotalTime;
    public long walkTotalCount;
    public int sportTimeTarget;
    public long walkTarget;
    public UserPointer sportInfoWithUser;
    public boolean finishWalkTarget;
    public float caloriesTotalNumber;
    public String sportInfoDevice;
    public boolean finishTimeTarget;
    public long timestamp;
    public int caloriesTarget;
    public long distance;
    public String deviceType;
    public String deviceBleMacAddress;

    public static SportInfo create() {
        SportInfo history = new SportInfo();
        return history;
    }

    public SportInfo finishCaloriesTarget(boolean finishCaloriesTarget) {
        this.finishCaloriesTarget = finishCaloriesTarget;
        return this;
    }

    public SportInfo activityTotalTime(int activityTotalTime) {
        this.activityTotalTime = activityTotalTime;
        return this;
    }

    public SportInfo walkTotalCount(long walkTotalCount) {
        this.walkTotalCount = walkTotalCount;
        return this;
    }

    public SportInfo sportTimeTarget(int sportTimeTarget) {
        this.sportTimeTarget = sportTimeTarget;
        return this;
    }

    public SportInfo finishCaloriesTarget(long walkTarget) {
        this.walkTarget = walkTarget;
        return this;
    }

    public SportInfo sportInfoWithUser(UserPointer sportInfoWithUser) {
        this.sportInfoWithUser = sportInfoWithUser;
        return this;
    }

    public SportInfo finishWalkTarget(boolean finishWalkTarget) {
        this.finishWalkTarget = finishWalkTarget;
        return this;
    }

    public SportInfo caloriesTotalNumber(int caloriesTotalNumber) {
        this.caloriesTotalNumber = caloriesTotalNumber;
        return this;
    }

    public SportInfo sportInfoDevice(String sportInfoDevice) {
        this.sportInfoDevice = sportInfoDevice;
        return this;
    }

    public SportInfo timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SportInfo caloriesTarget(int caloriesTarget) {
        this.caloriesTarget = caloriesTarget;
        return this;
    }

    public SportInfo distance(long distance) {
        this.distance = distance;
        return this;
    }

    public SportInfo deviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public SportInfo deviceBleMacAddress(String deviceBleMacAddress) {
        this.deviceBleMacAddress = deviceBleMacAddress;
        return this;
    }

    public SportInfo finishTimeTarget(boolean finishTimeTarget) {
        this.finishTimeTarget = finishTimeTarget;
        return this;
    }
}
