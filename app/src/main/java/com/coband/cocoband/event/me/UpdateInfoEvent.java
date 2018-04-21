package com.coband.cocoband.event.me;

/**
 * Created by tgc on 17-5-19.
 */

public class UpdateInfoEvent {
    private String updateTarget;

    public UpdateInfoEvent(String updateTarget) {
        this.updateTarget = updateTarget;
    }

    public String getUpdateTarget() {
        return updateTarget;
    }

    public void setUpdateTarget(String updateTarget) {
        this.updateTarget = updateTarget;
    }
}
