package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-6-2.
 */

public class ErrorResponse {
    private int code;
    private String Msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
