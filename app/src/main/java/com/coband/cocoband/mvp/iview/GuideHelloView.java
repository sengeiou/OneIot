package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-6-7.
 */

public interface GuideHelloView extends BaseView {
    void showLoginFail();

    void showLoginSuccess();

    void showCustomToast(String message);
}

