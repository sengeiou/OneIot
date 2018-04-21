package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-5-8.
 * this class mapping leancloud class Weight;
 */

public class Weight {

    private UserPointer weightWithUser;
    private int unit;
    private float weight;
    private float bodyMI;
    private long timestamp;
    private String objectId;

    public UserPointer getWeightWithUser() {
        return weightWithUser;
    }

    public void setWeightWithUser(UserPointer weightWithUser) {
        this.weightWithUser = weightWithUser;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBodyMI() {
        return bodyMI;
    }

    public void setBodyMI(float bodyMI) {
        this.bodyMI = bodyMI;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
