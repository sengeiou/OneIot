package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.watchassistant.DBWeight;

import java.util.List;

/**
 * Created by ivan on 17-6-1.
 */

public interface WeightDailyView extends BaseView {

    void showAllWeight(List<DBWeight> allWeight, int unitSystem);

    void showWeightUpdate();
}
