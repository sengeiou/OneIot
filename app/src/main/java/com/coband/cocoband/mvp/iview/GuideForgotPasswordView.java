package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-6-7.
 */

public interface GuideForgotPasswordView extends BaseView {
    void showNetworkUnavailable();

    void showLoading();

    void showResetPwdSuccess();

    void showResetPwdFailed(int code);

    void showResetPwdFailed(String errorMsg);
}
