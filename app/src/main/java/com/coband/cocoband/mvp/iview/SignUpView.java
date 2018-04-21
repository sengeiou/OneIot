package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-6-6.
 */

public interface SignUpView extends BaseView {
    void closeDialog();

    void showDialog(String message);

    void showNetworkDisconnected();

    void showRequestVerifyCodeFailed(String errorMsg);

    void showRequestVerifyCodeSuccess();

    void showRegisterWithPhoneSuccess();

    void showRegisterWithEmailSuccess();

    void showRegisterWithPhoneFailed(String errorMsg);

    void showRegisterWithEmailFailed(String errorMsg);

    void showRegisterWithEmailFailed(int code);

    void showRegisterWithPhoneFailed(int code);

    void showLoading();

    void showLogInFailed(int code);

    void showLogInFailed(String object);

    void showLogInSuccess(boolean setUserInfo);
}
