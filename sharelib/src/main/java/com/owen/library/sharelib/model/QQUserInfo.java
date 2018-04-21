package com.owen.library.sharelib.model;

/**
 * TODO 不能混淆
 */
public class QQUserInfo {

    private String openid;
    private String nickname;
    private int sex;
    private String headimgurl;

    public QQUserInfo(String openid, String nickname, int sex, String headimgurl) {
        this.openid = openid;
        this.nickname = nickname;
        this.sex = sex;
        this.headimgurl = headimgurl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Override
    public String toString() {
        return "QQUserInfo{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", headimgurl='" + headimgurl + '\'' +
                '}';
    }
}
