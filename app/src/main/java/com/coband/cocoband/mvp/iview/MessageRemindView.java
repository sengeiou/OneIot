package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.RemindApp;

import java.util.List;

/**
 * Created by ivan on 17-5-27.
 */

public interface MessageRemindView extends BaseView {
    void showRemindStatus(List<RemindApp> apps, boolean remind);

    void openLicensing();

    void openListChoice(boolean isCheck);

    void updateRemindStatus(List<RemindApp> apps, boolean remind);
}
