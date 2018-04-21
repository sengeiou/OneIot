package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

import java.util.List;

/**
 * Created by tgc on 17-6-6.
 */

public interface SetUserInfoView extends BaseView {
    void setWeight(String weight);

    void showWeightToEditText(float inchFinal);

    void setHeight(String height);

    void showHeightInchToEditText(List<Integer> dataList);

    void showHeightMetricToEditText(float inchFinal);

    void jumpToHome();

    void showCustomToast(String message);

    void showPostAccountInfoSuccess();

    void showPostAccountInfoFailed(int code);

    void showPostAccountInfoFailed(String errorMsg);

    void showNetworkUnavailable();
}
