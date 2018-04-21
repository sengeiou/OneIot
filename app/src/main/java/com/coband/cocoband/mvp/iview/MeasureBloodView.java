package com.coband.cocoband.mvp.iview;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.coband.cocoband.mvp.BaseView;
import com.coband.interactivelayer.bean.BloodPressurePacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgc on 17-9-18.
 */

public interface MeasureBloodView extends BaseView {
    void showDeviceDisconnected();

    void showSyncing();

    void showMeasuring();

    void stopMeasure();

    void setBloodDate(ArrayList<BloodPressurePacket> bppsList);

    void setBloodDate(ArrayList<BloodPressurePacket> bppsList, List<Long> timeList);

    void showBloodValue(String date, int... value);

    void goneStatus();

    void resetHistogram();

    void setHistogram(int level);

    void showResultValue(@DrawableRes int res,
                         @StringRes int stringRes, @ColorRes int colorRes, int level);
}
