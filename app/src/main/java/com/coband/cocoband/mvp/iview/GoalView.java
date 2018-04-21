package com.coband.cocoband.mvp.iview;

import android.support.annotation.StringRes;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-11-9.
 */

public interface GoalView extends BaseView {
    void sleepMode(int sleepTarget, float sleepTargetFloat);

    void weightMode(int unit, double weightTarget);

    void walkMode(int walkTarget);

    void setWalkDialog(int sleep, double weight);

    void setSleepDialog(int step, double weight);

    void setWeightDialog(int step, int sleep);

    void show(int step, int sleep, double weight);

    void setGoalText(String str);

    void loadSeekBar(String type);

    void showNetworkUnavailable();

    void showLoading();

    void showUpdateTargetSuccess();

    void showUpdateTargetFailed();
}
