package com.coband.watchassistant;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "ACCOUNT".
 */
public class Account {

    private Long id;
    private String account;
    private String password;
    private String token;
    private String unitSystem;
    private String birthday;
    private String gender;
    private String nickname;
    private Integer height;
    private Double weight;
    private Integer stepTarget;
    private Double weightTarget;
    private Integer sleepTarget;
    private String uid;
    private String did;
    private Boolean accountVerified;
    private String avatar;
    private String avatarMD5;
    private String background;
    private String backgroundMD5;
    private String bloodType;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private String province;
    private String language;
    private String osType;
    private String osVersion;
    private String phoneModel;
    private Integer timezone;
    private String achievements;
    private Long maxDaySteps;
    private Long startExerciseTime;
    private Double totalCalories;
    private Double totalDistance;
    private Integer totalExerciseDays;
    private Long totalSteps;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, String account, String password, String token, String unitSystem, String birthday, String gender, String nickname, Integer height, Double weight, Integer stepTarget, Double weightTarget, Integer sleepTarget, String uid, String did, Boolean accountVerified, String avatar, String avatarMD5, String background, String backgroundMD5, String bloodType, String city, String country, Double latitude, Double longitude, String province, String language, String osType, String osVersion, String phoneModel, Integer timezone, String achievements, Long maxDaySteps, Long startExerciseTime, Double totalCalories, Double totalDistance, Integer totalExerciseDays, Long totalSteps) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.token = token;
        this.unitSystem = unitSystem;
        this.birthday = birthday;
        this.gender = gender;
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.stepTarget = stepTarget;
        this.weightTarget = weightTarget;
        this.sleepTarget = sleepTarget;
        this.uid = uid;
        this.did = did;
        this.accountVerified = accountVerified;
        this.avatar = avatar;
        this.avatarMD5 = avatarMD5;
        this.background = background;
        this.backgroundMD5 = backgroundMD5;
        this.bloodType = bloodType;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
        this.language = language;
        this.osType = osType;
        this.osVersion = osVersion;
        this.phoneModel = phoneModel;
        this.timezone = timezone;
        this.achievements = achievements;
        this.maxDaySteps = maxDaySteps;
        this.startExerciseTime = startExerciseTime;
        this.totalCalories = totalCalories;
        this.totalDistance = totalDistance;
        this.totalExerciseDays = totalExerciseDays;
        this.totalSteps = totalSteps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(String unitSystem) {
        this.unitSystem = unitSystem;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getStepTarget() {
        return stepTarget;
    }

    public void setStepTarget(Integer stepTarget) {
        this.stepTarget = stepTarget;
    }

    public Double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(Double weightTarget) {
        this.weightTarget = weightTarget;
    }

    public Integer getSleepTarget() {
        return sleepTarget;
    }

    public void setSleepTarget(Integer sleepTarget) {
        this.sleepTarget = sleepTarget;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Boolean getAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(Boolean accountVerified) {
        this.accountVerified = accountVerified;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarMD5() {
        return avatarMD5;
    }

    public void setAvatarMD5(String avatarMD5) {
        this.avatarMD5 = avatarMD5;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBackgroundMD5() {
        return backgroundMD5;
    }

    public void setBackgroundMD5(String backgroundMD5) {
        this.backgroundMD5 = backgroundMD5;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public Long getMaxDaySteps() {
        return maxDaySteps;
    }

    public void setMaxDaySteps(Long maxDaySteps) {
        this.maxDaySteps = maxDaySteps;
    }

    public Long getStartExerciseTime() {
        return startExerciseTime;
    }

    public void setStartExerciseTime(Long startExerciseTime) {
        this.startExerciseTime = startExerciseTime;
    }

    public Double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getTotalExerciseDays() {
        return totalExerciseDays;
    }

    public void setTotalExerciseDays(Integer totalExerciseDays) {
        this.totalExerciseDays = totalExerciseDays;
    }

    public Long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Long totalSteps) {
        this.totalSteps = totalSteps;
    }

}
