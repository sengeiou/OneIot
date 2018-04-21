package com.coband.cocoband.mvp.model.bean;

import android.widget.ImageView;

import com.imcorecyclerviewlib.BaseTitleBean;

/**
 * Created by tgc on 17-5-17.
 */

public class AccountBean extends BaseTitleBean {
    public String titleText;
    public String tip;
    public ImageView nextIcon;

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public ImageView getNextIcon() {
        return nextIcon;
    }

    public void setNextIcon(ImageView nextIcon) {
        this.nextIcon = nextIcon;
    }
}
