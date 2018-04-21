package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-6-17.
 */

public interface DNDScheduleView extends BaseView {
    void showScheduleStartTime(int startTime);

    void showScheduleEndTime(int endTime);

    void showDeviceDisconnected();

    void showSetStartTimeView();

    void showSetEndTimeView();
}
