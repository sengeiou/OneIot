package com.coband.dfu.network.bean;

/**
 * Created by mai on 17-10-11.
 */

public class LastFwBean {
    public String userId = "jack@gmail.com";
    public String serial = "00:0E:12:40:58:13";
    public String appVersion = "12345";
    public String patchVersion = "15332";

    public LastFwBean(String userId, String serial, String appVersion, String patchVersion) {
        this.userId = userId;
        this.serial = serial;
        this.appVersion = appVersion;
        this.patchVersion = patchVersion;
    }
}
