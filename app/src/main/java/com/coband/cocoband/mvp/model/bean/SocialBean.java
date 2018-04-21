package com.coband.cocoband.mvp.model.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mqh on 5/3/16.
 */
public class SocialBean implements Serializable{

    private String avatar;
    private String nickName;
    private String userName;
    private long currentSteps;
    private long likeCounts;
    private int rankNum;
    private boolean isLike;
    private List<String> likes;
    private String userObjectID;
    private String rankTableObjectID;
    private Date createdAt;
    private long totalSteps;
    private long bestDaySteps;
    private String surfaceImg;
    private List<String> archivementList;
    private int totalExerciceDays;

    public int getTotalExerciceDays() {
        return totalExerciceDays;
    }

    public void setTotalExerciceDays(int totalExerciceDays) {
        this.totalExerciceDays = totalExerciceDays;
    }


    public List<String> getArchivementList() {
        return archivementList;
    }

    public void setArchivementList(List<String> archivementList) {
        this.archivementList = archivementList;
    }

    public String getSurfaceImg() {
        return surfaceImg;
    }

    public void setSurfaceImg(String surfaceImg) {
        this.surfaceImg = surfaceImg;
    }

    public long getBestDaySteps() {
        return bestDaySteps;
    }

    public void setBestDaySteps(long bestDaySteps) {
        this.bestDaySteps = bestDaySteps;
    }

    public long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(long likeCounts) {
        this.likeCounts = likeCounts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(long currentSteps) {
        this.currentSteps = currentSteps;
    }

    public int getRankNum() {
        return rankNum;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getUserObjectID() {
        return userObjectID;
    }

    public void setUserObjectID(String mUserObjectID) {
        this.userObjectID = mUserObjectID;
    }

    public String getRankTableObjectID() {
        return rankTableObjectID;
    }

    public void setRankTableObjectID(String mRankTableObjectID) {
        this.rankTableObjectID = mRankTableObjectID;
    }
}
