package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-5-24.
 */

public interface BandAlarmView extends BaseView {
    void showFirstAlarmStatus(boolean open, int hour, int minute, byte period);

    void showSecondAlarmStatus(boolean open, int hour, int minute, byte period);

    void showThirdAlarmStatus(boolean open, int hour, int minute, byte period);

    void showDeviceDisconnected();

    void showSetAlarmSuccess();

    void showSetAlarmFailed();
}
