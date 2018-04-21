package com.coband.cocoband.mvp.model.bean;

import com.coband.watchassistant.R;

/**
 * Created by imco on 2/24/16.
 */
public class Achievement {

    public static final int STEP_ICON_ID[] = {R.drawable.firststep, R.drawable.walker,
            R.drawable.wangt, R.drawable.killer};
    public static final int DIS_ICON_ID[] = {R.drawable.asteroids, R.drawable.beli, R.drawable.hubell, R.drawable.mileyq, R.drawable.wanli};

    public static final int STEP_ICON_SHARE[] = {R.drawable.firststep_share, R.drawable.walker_share,
            R.drawable.wangt_share, R.drawable.killer_share};
    public static final int DIS_ICON_SHARE[] = {R.drawable.asteroids_share, R.drawable.beli_share,
            R.drawable.hubell_share, R.drawable.mileyq_share, R.drawable.wanli_share};

    private int iconId;
    private String title;
    private String subTitle;
    private boolean isClick;
    private String summary;
    private String cardBkgColor;
    private int shareIconId;

    public int getShareIconId() {
        return shareIconId;
    }

    public void setShareIconId(int shareIconId) {
        this.shareIconId = shareIconId;
    }

    public String getCardBkgColor() {
        return cardBkgColor;
    }

    public void setCardBkgColor(String cardBkgColor) {
        this.cardBkgColor = cardBkgColor;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
