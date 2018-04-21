package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-6-9.
 */

public interface PersonalInfoView extends BaseView {
    void showLocalHeadPic(String path);

    void showNetworkUnavailable();

    void showLoading();

    void showUpdateAccountSuccess(int type);

    void showUpdateAccountFailed(int type, String errorMsg);

    void showUpdateAccountFailed(int type, int code);
}
