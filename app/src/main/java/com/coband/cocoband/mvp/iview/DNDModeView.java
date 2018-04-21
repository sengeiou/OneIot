package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.DNDSettings;

/**
 * Created by ivan on 17-6-17.
 */

public interface DNDModeView extends BaseView {

    void showDNDSettings(DNDSettings settings);

    void showDeviceDisconnected();

    void showDNDScheduleStatus(boolean object);
}
