package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.LastRate;
import com.coband.cocoband.mvp.model.entity.LastSleepData;
import com.coband.cocoband.mvp.model.entity.SingleRate;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.Sleep;

import java.util.List;

/**
 * Created by ivan on 17-4-14.
 */

public interface DashboardView extends BaseView {

    void showSteps(long steps);

    void showDistance(double distance);

    void showCalorie(double calorie);

    void showDashboardAppearance(int appearance, int module);

    void showWeekSleepData(List<Sleep> weekSleeps);

    void showTodaySleepData(int totalSleepTime);

    void showHeartRate(List<SingleRate> heartRates, int aveHeartRate);

    void showWeekWeight(List<DBWeight> weights, double todayWeight);

    void showDeviceName(String name);

    void showLastSyncTime(long time);

    void syncCompleted(boolean success, long syncTime);

    void showSyncing();

    void showDisconnected();

    void showSyncAvailable(boolean available);

    void showUnitSystem(int unitSystem);

    void showPressureValue(BloodPressure pressure, long date);

    void showLastHeartRate(LastRate rate);

    void showLastSleepData(LastSleepData data);

    void showLastSevenDayWeight(List<DBWeight> weights);

    void showGuidePage();

    void showBloodMeasure();

    void showBloodChart();

}
