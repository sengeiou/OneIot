package com.coband.cocoband.mvp.model.entity.request;

/**
 * Created by ivan on 3/7/18.
 */

public class EmailRegisterContent {

    /**
     * type : email
     * email : xxxx@gmai.com
     * password : xxxx
     */

    private String type;
    private String email;
    private String password;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
