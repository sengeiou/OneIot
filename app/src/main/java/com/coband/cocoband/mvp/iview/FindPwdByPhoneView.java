package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by xing on 10/4/2018.
 */

public interface FindPwdByPhoneView extends BaseView {
    void showNetworkUnavailable();

    void showResetPasswordSuccess();

    void showResetPasswordFailed(int code);

    void showResetPasswordFailed(String errorMsg);

    void showLoading();

    void showRequestVerifyCodeSuccess();

    void showRequestVerifyCodeFailed(int code);

    void showRequestVerifyCodeFailed(String errorMsg);


}
