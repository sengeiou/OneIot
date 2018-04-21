package com.coband.cocoband.mvp.model.entity.response;

/**
 * Created by ivan on 3/9/18.
 */

public class VerifyCodeResult {
    /**
     * code : 0
     * msg :
     * payload : null
     */

    private int code;
    private String msg;
    private Object payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
