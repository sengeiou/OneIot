package com.coband.cocoband.mvp.model.entity;

import java.util.List;

/**
 * Created by ivan on 17-5-8.
 * the class mapping leancloud HeartRateInfo
 */

public class HeartRateInfo {
    /**
     * updatedAt : 2016-10-08T07:19:58.403Z
     * objectId : 57f89e1ea0bb9f00582db2c7
     * createdAt : 2016-10-08T07:19:58.403Z
     * deviceBleMacAddress : 00:4D:48:21:CE:05
     * deviceType : CoBand
     * heartWithUser : {"__type":"Pointer","className":"_User","objectId":"565d725bddb2084aa030cd55"}
     * heartRates : [{"time":1475316060000,"rate":79},{"time":1475321520000,"rate":97},{"time":1475322720000,"rate":90},{"time":1475322780000,"rate":72},{"time":1475322840000,"rate":71},{"time":1475325540000,"rate":72},{"time":1475329980000,"rate":73}]
     * timeStamp : 1475251200000
     */

    private String updatedAt;
    private String objectId;
    private String createdAt;
    private String deviceBleMacAddress;
    private String deviceType;
    private UserPointer heartWithUser;
    private long timestamp;
    private List<HeartRatesBean> heartRates;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceBleMacAddress() {
        return deviceBleMacAddress;
    }

    public void setDeviceBleMacAddress(String deviceBleMacAddress) {
        this.deviceBleMacAddress = deviceBleMacAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public UserPointer getHeartWithUser() {
        return heartWithUser;
    }

    public void setHeartWithUser(UserPointer heartWithUser) {
        this.heartWithUser = heartWithUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<HeartRatesBean> getHeartRates() {
        return heartRates;
    }

    public void setHeartRates(List<HeartRatesBean> heartRates) {
        this.heartRates = heartRates;
    }


    public static class HeartRatesBean {
        /**
         * time : 1475316060000
         * rate : 79
         */

        private long time;
        private int rate;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }
    }
}
