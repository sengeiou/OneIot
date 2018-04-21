package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.HeartRateSettings;

/**
 * Created by ivan on 17-6-18.
 */

public interface HeartRateSettingsView extends BaseView {
    void showHeartRateSettings(HeartRateSettings settings);

    void showHighRateRemindStatus(boolean status);

    void showHighRateRemindValue(int highRemindValue);

    void showScheduleMeasureStatus(boolean scheduleMeasureStatus);

    void showScheduleMeasureTime(int time);

    void showDeviceDisconnected();
}
