package com.coband.cocoband.mvp.model.bean;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by ivan on 17-4-11.
 */

@Module
public class LoginInfo {

    /**
     * username : ivan
     * password : 111111
     */

    @Inject
    public LoginInfo() {

    }

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}
