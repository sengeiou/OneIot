package com.coband.cocoband.mvp.iview;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.SingleBloodPressure;
import com.coband.interactivelayer.bean.BloodPressurePacket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TGC on 2017/9/17.
 */

public interface BloodPressureChartView extends BaseView {

    void clearChart(LineChart lineChart);

    void setChartData(LineChart lineChart, ArrayList<Entry> yVals, ArrayList<Entry> yValsLow,
                      List<SingleBloodPressure> list, List<Date> dateList, Long date);

    void showAveBlood(String value);

    void showNoData();

    void setRecyclerDate(ArrayList<BloodPressurePacket> bppsList, List<Long> timeList);
}
