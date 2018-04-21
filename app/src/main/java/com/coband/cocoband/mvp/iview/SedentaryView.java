package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-5-24.
 */

public interface SedentaryView extends BaseView {

    void showSedentaryStatus(boolean open);

    void showSedentaryTime(int time);

    void showNumberPickerDialog(int index);
}
