package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-4-26.
 */

public class UserPointer {
    String __type = "Pointer";
    String className = "_User";
    String objectId = "";

    public UserPointer(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
