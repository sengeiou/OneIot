package com.coband.cocoband.mvp.model.entity.request;

/**
 * Created by ivan on 3/7/18.
 */

public class PhoneRegisterContent {


    /**
     * type : phone
     * phone : xxxxxx
     * password : xxxx
     * verifyCode : 1234
     */

    private String type;
    private String phone;
    private String password;
    private String verifyCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
