package com.coband.watchassistant;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "HEART_RATE".
 */
public class HeartRate {

    private Long id;
    private long date;
    private String heartRate;
    private boolean upload;
    /** Not-null value. */
    private String uid;

    public HeartRate() {
    }

    public HeartRate(Long id) {
        this.id = id;
    }

    public HeartRate(Long id, long date, String heartRate, boolean upload, String uid) {
        this.id = id;
        this.date = date;
        this.heartRate = heartRate;
        this.upload = upload;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public boolean getUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    /** Not-null value. */
    public String getUid() {
        return uid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUid(String uid) {
        this.uid = uid;
    }

}
