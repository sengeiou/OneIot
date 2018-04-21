package com.coband.watchassistant;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PUSH_APP".
 */
public class PushApp {

    private Long id;
    private String packageName;
    private String appName;

    public PushApp() {
    }

    public PushApp(Long id) {
        this.id = id;
    }

    public PushApp(Long id, String packageName, String appName) {
        this.id = id;
        this.packageName = packageName;
        this.appName = appName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}