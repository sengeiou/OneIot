package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-8-14.
 */

public class FOTAResult<T> {

    /**
     * code : 0
     * payload : {"isTester":false}
     */

    private int code;
    private T payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
