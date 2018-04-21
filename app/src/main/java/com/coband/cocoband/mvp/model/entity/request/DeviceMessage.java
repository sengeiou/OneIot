package com.coband.cocoband.mvp.model.entity.request;

/**
 * Created by xing on 10/4/2018.
 */

public class DeviceMessage {
    private String mac;
    private String name;
    private String model;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
