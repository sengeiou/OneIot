package com.coband.cocoband.mvp.model.bean;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Constants;

import java.util.Date;
import java.util.List;

/**
 * Created by imco on 11/20/15.
 */
public class UserWrapper {

    private AVUser avUser;

    public UserWrapper(AVUser avUser) {
        this.avUser = avUser;
    }

    private static final String AVATAR = "avatar";
    private static final String GENDAR = "sex";
    private static final String WEIGHT = "weight";
    private static final String HEIGHT = "height";
    private static final String BIRTHDAY = "birthday";
    private static final String NICKNAME = "nickName";
    private static final String WALK_TARGET = "walkTarget";
    private static final String DIS_TARGET = "distanceTarget";
    private static final String CALORY_TARGET = "caloriesTarget";
    private static final String TOTAL_WALK_COUNT = "totalWalkCount";
    private static final String SPORT_TIME_TARGET = "sportTimeTarget";
    private static final String BEGIN_SPORT_TIMESTAMP = "beginSportTimestamp";
    private static final String CREATED_AT = "createdAt";
    private static final String TOTAL_DISTANCE = "totalDistance";
    private static final String ACHIEVEMENT_LIST = "archivementList";
    private static final String DAY_HIGHEST_STEPS = "dayHighestSteps";
    private static final String SURFACE_IMG = "surfaceImg";
    private static final String TOTAL_EXERCICE_DAYS = "totalExerciceDays";
    private static final String APP_VERSION = "appVersion";
    private static final String SLEEP_TARGET = "sleepTarget";
    private static final String DEVICE_TYPES = "deviceTypes";
    private static final String USER_DEVICE_TYPE = "userDeviceType";
    private static final String USER_DEVICE_SYSTEM_VERSION = "userDeviceSystemVersion";
    private static final String WEIGHT_TARGET = "weightTarget";
    private static final String AVATAR_FILE = "avatarFile";
    private static final String SURFACE_FILE = "surfaceFile";
    private static final String FIRMWARE_SYSTEM_VERSION = "firmwareSystemVersion";

    public void setWeightTarget(float weightTarget) {
        avUser.put(WEIGHT_TARGET, weightTarget);
    }

    public float getWeightTarget() {
        return (float) avUser.getDouble(WEIGHT_TARGET);
    }

    public void setUserDeviceSystemVersion(String userDeviceSystemVersion) {
        avUser.put(USER_DEVICE_SYSTEM_VERSION, userDeviceSystemVersion);
    }

    public String getUserDeviceSystemVersion() {
        return null != avUser? "": avUser.getString(USER_DEVICE_SYSTEM_VERSION);
    }

    public void setUserDeviceType(String userDeviceType) {
        avUser.put(USER_DEVICE_TYPE, userDeviceType);
    }
    public String getUserDeviceType() {
        return avUser.getString(USER_DEVICE_TYPE);
    }

    public void setDeviceTypes(String[] deviceTypes) {
        avUser.put(DEVICE_TYPES, deviceTypes);
    }

    public List getDeviceTypes() {
        return avUser.getList(DEVICE_TYPES);
    }

    public int getSleepTarget(){
        return avUser.getInt(SLEEP_TARGET);
    }

    public void setSleepTarget(int sleepTarget) {
        avUser.put(SLEEP_TARGET, sleepTarget);
    }

    public String getAppVersion(){
        return avUser.getString(APP_VERSION);
    }

    public void setAppVersion(String appVersion) {
        avUser.put(APP_VERSION, appVersion);
    }

    public int getTotalExerciceDays() {
        return avUser.getInt(TOTAL_EXERCICE_DAYS);
    }

    public void setTotalExerciceDays(int totalExerciceDays) {
        avUser.put(TOTAL_EXERCICE_DAYS, totalExerciceDays);
    }

    public String getSurfaceImg() {
        String surfaceUrl = avUser.getString(SURFACE_IMG);
        if (null == surfaceUrl || surfaceUrl.isEmpty()){
            surfaceUrl =  getSurfaceFile() == null ? null :getSurfaceFile().getThumbnailUrl(true, 200, 200);
        }
        if (!(null == surfaceUrl || surfaceUrl.isEmpty()) && surfaceUrl.contains("http://ac-lk71houg.clouddn.com")) {
            surfaceUrl.replace("http://ac-lk71houg.clouddn.com", "https://dn-lk71houg.qbox.me");
        }
        return surfaceUrl;
    }

    public void setSurfaceImg(String surfaceImg) {
        avUser.put(SURFACE_IMG, surfaceImg);
    }

    public void setSurfaceFile(AVFile surfaceFile) {
        avUser.put(SURFACE_FILE, surfaceFile);
    }

    public AVFile getSurfaceFile() {
        return avUser.getAVFile(SURFACE_FILE);
    }

    public void setDayHighestSteps(long dayHighestSteps) {
        avUser.put(DAY_HIGHEST_STEPS, dayHighestSteps);
    }

    public long getDayHighestSteps() {
        return avUser.getLong(DAY_HIGHEST_STEPS);
    }


    public void setAchievementList(List<String> achievementList) {
        avUser.put(ACHIEVEMENT_LIST, achievementList);
    }

    public List<String> getAchievementList() {
        if (avUser != null) {
            return avUser.getList(ACHIEVEMENT_LIST);
        }else {
            return null;
        }
    }

    public void setTotalDistance(double totalDistance) {
        avUser.put(TOTAL_DISTANCE, totalDistance);
    }

    public double getTotalDistance() {
        return avUser.getDouble(TOTAL_DISTANCE);
    }

    public void setSportTimeTarget(long sportTimeTarget) {
        avUser.put(SPORT_TIME_TARGET, sportTimeTarget);
    }

    public long getSportTimeTarget() {
        return avUser.getLong(SPORT_TIME_TARGET);
    }

    public void setTotalWalkCount(long totalWalkCount) {
        avUser.put(TOTAL_WALK_COUNT, totalWalkCount);
    }

    public long getTotalWalkCount() {
        if (avUser != null) {
            return avUser.getLong(TOTAL_WALK_COUNT);
        }else {
            return 1000;
        }
    }

    public String getAvatar() {
        String avatarUrl = null;

        if (avUser != null) {
            avatarUrl = avUser.getString(AVATAR);
        }
        if (null == avatarUrl || avatarUrl.isEmpty()) {
            return getAvatarFile() == null ? null : getAvatarFile().getThumbnailUrl(true, 200, 200);
        }else {
            return avatarUrl;
        }
    }

    public int getGendar() {
        if (avUser != null) {
            return avUser.getInt(GENDAR);
        } else {
            return Constants.FEMALE;
        }
    }

    public double getWeight() {
        double currentUserWeight = 0f;
        if (avUser != null) {
            currentUserWeight = avUser.getDouble(WEIGHT);
        }

        if (CocoUtils.isMetric()) {
            return currentUserWeight;
        } else {
            return CocoUtils.keepADecimal(currentUserWeight * 2.2);
        }
    }

    public double getHeight() {
        if (avUser != null) {
            return avUser.getDouble(HEIGHT);
        }else {
            return 170;
        }
    }

    public Date getBirthday() {
        if (avUser != null) {
            return avUser.getDate(BIRTHDAY);
        }else {
            return null;
        }
    }

    public String getNickname() {
        if (avUser != null) {
            return avUser.getString(NICKNAME);
        }else {
            return null;
        }
    }

    public void setAvatar(String url, AVFile avatarFile) {
        avUser.put(AVATAR, url);
        setAvatarFile(avatarFile);
    }

    public void setGendar(int gendar) {
        avUser.put(GENDAR, gendar);
    }

    public void setWeight(double weight) {
        avUser.put(WEIGHT, weight);
    }

    public void setHeight(int height) {
        avUser.put(HEIGHT, height);
    }

    public void setBirthday(Date date) {
        avUser.put(BIRTHDAY, date);
    }

    public void setNickname(String nickname) {
        avUser.put(NICKNAME, nickname);
    }

    public void setCaloryTarget(int caloryTarget) {
        avUser.put(CALORY_TARGET, caloryTarget);
    }

    public int getCaloryTarget() {
        if (avUser != null) {
            return avUser.getInt(CALORY_TARGET);
        }else {
            return 0;
        }
    }

    public void setWalkTarget(long walkTarget) {
        avUser.put(WALK_TARGET, walkTarget);
    }

    public long getWalkTarget() {
        if (avUser != null) {
            return avUser.getLong(WALK_TARGET);
        }else {
            return 0;
        }
    }

    public void setDisTarget(int disTarget) {
        avUser.put(DIS_TARGET, disTarget);
    }

    public int getDisTarget() {
        if (avUser != null) {
            return avUser.getInt(DIS_TARGET);
        }else {
            return 0;
        }
    }

    public long getBeginSportTimestamp() {
        if (avUser != null) {
            return avUser.getLong(BEGIN_SPORT_TIMESTAMP);
        }else {
            return 0;
        }
    }

    public void setBeginSportTimestamp(long beginSportTime) {
        avUser.put(BEGIN_SPORT_TIMESTAMP, beginSportTime);
    }

    public void setAvatarFile(AVFile avatarFile) {
        avUser.put(AVATAR_FILE, avatarFile);
    }

    public AVFile getAvatarFile() {
        if (avUser != null) {
            return avUser.getAVFile(AVATAR_FILE);
        }else {
            return null;
        }
    }

    public void saveInBackground(SaveCallback saveCallback) {
        avUser.saveInBackground(saveCallback);
    }

    public void saveInBackground() {
        avUser.saveInBackground();
    }

    public Date getCreatedAt() {
        if (avUser != null) {
            return avUser.getDate(CREATED_AT);
        }else {
            return new Date(System.currentTimeMillis());
        }
    }

    public void increment(String key) {
        avUser.increment(key);
    }

    public void increment(String key, int count) {
        avUser.increment(key, count);
    }

    public float getBodyMI(double weight) {
        double bodyMI;
        double height = ((double) getHeight()) / 100;
        boolean isMetric = CocoUtils.isMetric();
        if (isMetric) {
            //公制, 单位为千克
            bodyMI = weight / (height * height);
        } else {
            //英制, 单位为磅,一千克　等于　2.2磅
            bodyMI = (weight / 2.2f) / (height * height);
        }
        return (float) Math.round(bodyMI*10)/10;
    }

    public String getFirmwareVersion() {
        if (avUser != null) {
            return avUser.getString(FIRMWARE_SYSTEM_VERSION);
        } else {
            return "";
        }
    }

    public void setFirmwareVersion(String firmwareVersion) {
        avUser.put(FIRMWARE_SYSTEM_VERSION, firmwareVersion);
    }
}
