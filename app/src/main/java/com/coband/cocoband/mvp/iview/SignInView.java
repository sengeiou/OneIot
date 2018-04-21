package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-6-6.
 */

public interface SignInView extends BaseView {

    void setUsernameEditText(String username);

    void showNetworkUnavailable();

    void showPosting();

    void showLogInSuccess(boolean setUserInfo);

    void showLogInFailed(int code);

    void showLogInFailed(String object);
}
