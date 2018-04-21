package com.coband.cocoband.mvp.model.entity.request;

/**
 * Created by ivan on 3/12/18.
 */

public class UpdateAccountInfo {

    /**
     * nickName : jack
     * gender : Male
     * birthday : 1990-10-20
     * height : 180
     * weight : 72.5
     * bloodType : O
     * unitSystem : International
     * country : 中国
     * province : 广东省
     * city : 深圳市
     * location : {"latitude":22.53,"longitude":113.93}
     */

    private String nickName;
    private String gender;
    private String birthday;
    private int height;
    private double weight;
    private String bloodType;
    private String unitSystem;
    private String country;
    private String province;
    private String city;
    private LocationBean location;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(String unitSystem) {
        this.unitSystem = unitSystem;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public static class LocationBean {
        /**
         * latitude : 22.53
         * longitude : 113.93
         */

        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
