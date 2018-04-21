package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mqh on 5/17/16.
 */
public class PackageInfoBean {

    private String appName;
    private String packageName;

    public PackageInfoBean(String appName, String packageName) {
        this.packageName = packageName;
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
