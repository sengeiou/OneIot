package com.coband.cocoband.mvp.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ivan on 17-4-11.
 */

public class User implements Serializable{
    /**
     * sleepTarget : 480
     * appVersion : 4.4.9
     * email : yxh.ivan@gmail.com
     * sessionToken : ex9t0ycj4bdhjqyzq9au8liwq
     * updatedAt : 2017-04-07T08:24:55.950Z
     * sportTimeTarget : 4500
     * sex : 0
     * firmwareSystemVersion : version=RH09V000168,13117
     * archivementList : ["FirstStep","Walker"]
     * unit : 0
     * userDeviceSystemVersion : FISE S3931
     * walkTarget : 20000
     * objectId : 56ef9d99816dfa005174a262
     * username : ivan
     * birthday : {"__type":"Date","iso":"1990-06-08T03:00:00.000Z"}
     * createdAt : 2016-03-21T07:07:05.372Z
     * totalDistance : 17.21254020023346
     * copperNumber : 0
     * silverNumber : 0
     * emailVerified : true
     * nickName : ivan
     * upvotes : 0
     * dayHighestSteps : 10412
     * weight : 55
     * surfaceImg : https://dn-lk71houg.qbox.me/QHb9Hq9F91TJxLPTqa05NBCAxYImFAQuVGGHY5kj?imageView/2/w/1000/h/1000/q/100/format/png
     * totalExerciceDays : 95
     * bestSportInfoWithDay : {"__type":"Pointer","className":"SportInfo","objectId":"58ddc12da22b9d00585b9c31"}
     * avatar : https://dn-lk71houg.qbox.me/7MVeRiSKP8bcFfXCszqwjRjNKE6MBBta3YPyTgIi?imageView/2/w/200/h/200/q/100/format/png
     * goldNumber : 0
     * totalCalories : 149.64642032980913
     * ranking : 99
     * distanceTarget : 4
     * beginSportTimestamp : 1458489600000
     * userDeviceType : Android 5.1
     * location : {"longitude":113.3172904,"latitude":23.1059323,"__type":"GeoPoint"}
     * deviceTypes : ["CoBand"]
     * caloriesTarget : 300
     * authData : null
     * height : 168
     * totalWalkCount : 30553
     * mobilePhoneVerified : false
     */

    private int sleepTarget;
    private String appVersion;
    private String email;
    private String sessionToken;
    private String updatedAt;
    private float sportTimeTarget;
    private int sex;
    private String firmwareSystemVersion;
    private int unit;
    private String userDeviceSystemVersion;
    private int walkTarget;
    private String objectId;
    private String username;
    private BirthdayBean birthday;
    private String createdAt;
    private double totalDistance;
    private int copperNumber;
    private int silverNumber;
    private boolean emailVerified;
    private String nickName;
    private int upvotes;
    private int dayHighestSteps;
    private double weight;
    private String surfaceImg;
    private int totalExerciceDays;
    private BestSportInfoWithDayBean bestSportInfoWithDay;
    private String avatar;
    private int goldNumber;
    private double totalCalories;
    private int ranking;
    private float distanceTarget;
    private long beginSportTimestamp;
    private String userDeviceType;
    private LocationBean location;
    private int caloriesTarget;
    private Object authData;
    private double height;
    private double weightTarget;
    private int totalWalkCount;
    private boolean mobilePhoneVerified;
    private List<String> archivementList;
    private List<String> deviceTypes;

    public void setSportTimeTarget(float sportTimeTarget) {
        this.sportTimeTarget = sportTimeTarget;
    }

    public double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(double weightTarget) {
        this.weightTarget = weightTarget;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getSleepTarget() {
        return sleepTarget;
    }

    public void setSleepTarget(int sleepTarget) {
        this.sleepTarget = sleepTarget;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public float getSportTimeTarget() {
        return sportTimeTarget;
    }

    public void setSportTimeTarget(int sportTimeTarget) {
        this.sportTimeTarget = sportTimeTarget;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getFirmwareSystemVersion() {
        return firmwareSystemVersion;
    }

    public void setFirmwareSystemVersion(String firmwareSystemVersion) {
        this.firmwareSystemVersion = firmwareSystemVersion;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUserDeviceSystemVersion() {
        return userDeviceSystemVersion;
    }

    public void setUserDeviceSystemVersion(String userDeviceSystemVersion) {
        this.userDeviceSystemVersion = userDeviceSystemVersion;
    }

    public int getWalkTarget() {
        return walkTarget;
    }

    public void setWalkTarget(int walkTarget) {
        this.walkTarget = walkTarget;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BirthdayBean getBirthday() {
        return birthday;
    }

    public void setBirthday(BirthdayBean birthday) {
        this.birthday = birthday;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getCopperNumber() {
        return copperNumber;
    }

    public void setCopperNumber(int copperNumber) {
        this.copperNumber = copperNumber;
    }

    public int getSilverNumber() {
        return silverNumber;
    }

    public void setSilverNumber(int silverNumber) {
        this.silverNumber = silverNumber;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDayHighestSteps() {
        return dayHighestSteps;
    }

    public void setDayHighestSteps(int dayHighestSteps) {
        this.dayHighestSteps = dayHighestSteps;
    }



    public String getSurfaceImg() {
        return surfaceImg;
    }

    public void setSurfaceImg(String surfaceImg) {
        this.surfaceImg = surfaceImg;
    }

    public int getTotalExerciceDays() {
        return totalExerciceDays;
    }

    public void setTotalExerciceDays(int totalExerciceDays) {
        this.totalExerciceDays = totalExerciceDays;
    }

    public BestSportInfoWithDayBean getBestSportInfoWithDay() {
        return bestSportInfoWithDay;
    }

    public void setBestSportInfoWithDay(BestSportInfoWithDayBean bestSportInfoWithDay) {
        this.bestSportInfoWithDay = bestSportInfoWithDay;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGoldNumber() {
        return goldNumber;
    }

    public void setGoldNumber(int goldNumber) {
        this.goldNumber = goldNumber;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public float getDistanceTarget() {
        return distanceTarget;
    }

    public void setDistanceTarget(float distanceTarget) {
        this.distanceTarget = distanceTarget;
    }

    public long getBeginSportTimestamp() {
        return beginSportTimestamp;
    }

    public void setBeginSportTimestamp(long beginSportTimestamp) {
        this.beginSportTimestamp = beginSportTimestamp;
    }

    public String getUserDeviceType() {
        return userDeviceType;
    }

    public void setUserDeviceType(String userDeviceType) {
        this.userDeviceType = userDeviceType;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public int getCaloriesTarget() {
        return caloriesTarget;
    }

    public void setCaloriesTarget(int caloriesTarget) {
        this.caloriesTarget = caloriesTarget;
    }

    public Object getAuthData() {
        return authData;
    }

    public void setAuthData(Object authData) {
        this.authData = authData;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    public int getTotalWalkCount() {
        return totalWalkCount;
    }

    public void setTotalWalkCount(int totalWalkCount) {
        this.totalWalkCount = totalWalkCount;
    }

    public boolean isMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
    }

    public List<String> getArchivementList() {
        return archivementList;
    }

    public void setArchivementList(List<String> archivementList) {
        this.archivementList = archivementList;
    }

    public List<String> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<String> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public static class BirthdayBean {
        /**
         * __type : Date
         * iso : 1990-06-08T03:00:00.000Z
         */

        private String __type;
        private String iso;

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }

        public String getIso() {
            return iso;
        }

        public void setIso(String iso) {
            this.iso = iso;
        }
    }

    public static class BestSportInfoWithDayBean {
        /**
         * __type : Pointer
         * className : SportInfo
         * objectId : 58ddc12da22b9d00585b9c31
         */

        private String __type;
        private String className;
        private String objectId;

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }

    public static class LocationBean {
        /**
         * longitude : 113.3172904
         * latitude : 23.1059323
         * __type : GeoPoint
         */

        private double longitude;
        private double latitude;
        private String __type;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }
    }
}
