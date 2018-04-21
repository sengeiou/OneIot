package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-5-23.
 */

public interface ModifyDeviceNameView extends BaseView {
    void showModifySuccess();

    void showInvalidName();

    void showDeviceName(String name);

    void showModifyFailed();
}
