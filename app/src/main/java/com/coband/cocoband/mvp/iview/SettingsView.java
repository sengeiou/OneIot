package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by tgc on 17-6-9.
 */

public interface SettingsView extends BaseView {
    void fragmentExit();

    void showLoading();

    void showUpdateAccountInfoSuccess(int type);

    void showUpdateAccountInfoFailed(int type, String errorMsg);

    void showUpdateAccountInfoFailed(int type, int code);

    void showDeviceDisconnected();

    void showSyncing();
}
