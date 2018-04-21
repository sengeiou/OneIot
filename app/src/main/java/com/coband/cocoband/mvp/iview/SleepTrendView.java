package com.coband.cocoband.mvp.iview;

import com.github.mikephil.charting.charts.BarChart;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.DaySleepInfo;
import com.coband.cocoband.mvp.model.entity.MultiDaySleepInfo;
import com.coband.cocoband.widget.widget.SleepBarChart;

/**
 * Created by ivan on 17-5-18.
 */

public interface SleepTrendView extends BaseView {

    void showDayNodeSleep(DaySleepInfo info, SleepBarChart barChart);

    void showDaySleep(DaySleepInfo info);

    void showWeekSleep(MultiDaySleepInfo info);

    void showWeekNodeSleep(MultiDaySleepInfo info, BarChart barChart);

    void showMonthSleep(MultiDaySleepInfo info);

    void showMonthNodeSleep(MultiDaySleepInfo info, BarChart barChart);
}
