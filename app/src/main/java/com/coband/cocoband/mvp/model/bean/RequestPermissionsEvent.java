package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mai on 17-1-17.
 */

public class RequestPermissionsEvent {

    private int requestCode;
    public RequestPermissionsEvent(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
