package com.coband.cocoband.mvp.model.entity.response;


/**
 * Created by xing on 10/4/2018.
 */

public class UploadDeviceResponse {
    private int code;
    private String message;
    private Product product;
    private Device device;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static class Product {

    }

    public static class Device {

    }




}
