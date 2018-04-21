package com.coband.cocoband.mvp.model.entity.response;

/**
 * Created by ivan on 4/18/18.
 */

public class UploadDataResponse {

    /**
     * code : 0
     * message : ok
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
