package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.RateOneDayBean;

import java.util.List;

/**
 * Created by ivan on 17-6-6.
 */

public interface HeartRateView extends BaseView {
    void showDeviceDisconnected();

    void showMeasuring();

    void showDayRate(List<RateOneDayBean> dayHeartRateDatas);

    void showMeasureValue(int rate);

    void showStopMeasure();

    void showSyncing();

    void showRealTimeValue(int value);

    void showPlaySoundEnable(boolean enable);
}
