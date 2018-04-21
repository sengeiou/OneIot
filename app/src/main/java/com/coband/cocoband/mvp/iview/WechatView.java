package com.coband.cocoband.mvp.iview;

import android.graphics.Bitmap;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-6-29.
 */

public interface WechatView extends BaseView {
    void showNetworkUnavailable();

    void showDeviceDisconnected();

    void showNoDeviceAddress();

    void showAddressUploading();

    void showObtainQrcodeError();

    void showQrcode(Bitmap bitmap);

    void showSaveQrcodeSuccess();

    void showSaveQrcodeFailed();
}
