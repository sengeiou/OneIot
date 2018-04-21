package com.coband.cocoband.mvp.iview;

import com.github.mikephil.charting.charts.BarChart;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.DayStepInfo;
import com.coband.cocoband.mvp.model.entity.MultiDayStepInfo;
import com.coband.cocoband.mvp.model.entity.StepInfo;

import java.util.List;

/**
 * Created by ivan on 17-5-16.
 */

public interface SportTrendView extends BaseView {
    void showDayStep(DayStepInfo info);

    void showWeekStep(MultiDayStepInfo info);

    void showMonthStep(MultiDayStepInfo info);

    void showNodeStepInfo(List<StepInfo> nodeInfoList, BarChart barChart);

    void showWeekNodeStep(MultiDayStepInfo info, BarChart barChart);

    void showMonthNodeStep(MultiDayStepInfo info, BarChart barChart);
}
