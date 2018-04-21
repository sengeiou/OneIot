package com.coband.dfu.network.bean;

/**
 * Created by mai on 17-7-10.
 */

public class FwBean {
    public String userId;
    public String vendor;
    public String model;
    public String fwType;
    public String serial;
    public String fwVersion;

    public FwBean(String userId, String vendor, String model, String fwType, String serial, String fwVersion) {
        this.userId = userId;
        this.vendor = vendor;
        this.model = model;
        this.fwType = fwType;
        this.serial = serial;
        this.fwVersion = fwVersion;
    }
}
