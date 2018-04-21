package com.coband.cocoband.mvp.iview;

import com.github.mikephil.charting.charts.LineChart;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.SingleRate;

import java.util.List;

/**
 * Created by ivan on 17-6-15.
 */

public interface RateRecordView extends BaseView {
    void showRateDetail(List<SingleRate> rateList, LineChart linChart);

    void showRateSummary(int highestRate, int aveRate, int lowestRate);
}
