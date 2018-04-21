package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-6-9.
 */

public interface FindDeviceView extends BaseView {
    void showDeviceDisconnected();

    void showFindingDevice();

    void stopRippleAnimate();
}
