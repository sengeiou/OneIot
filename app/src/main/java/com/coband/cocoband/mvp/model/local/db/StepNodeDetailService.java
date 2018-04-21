package com.coband.cocoband.mvp.model.local.db;

import com.coband.App;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.StepNodeDetail;
import com.coband.watchassistant.StepNodeDetailDao;

import java.util.List;

/**
 * Created by ivan on 17-7-21.
 */

public class StepNodeDetailService {
    private static final String TAG = "StepNodeDetailService";

    static long insertStepNodeDetail(long date, int offset, int step, int calories, int distance) {
        if (PreferencesHelper.getCurrentId() == null) {
            return -1;
        }

        StepNodeDetailDao dao = App.getStepNodeDetailDao();
        List<StepNodeDetail> details = dao.queryBuilder().
                where(StepNodeDetailDao.Properties.Date.eq(date),
                        StepNodeDetailDao.Properties.Offset.eq(offset),
                        StepNodeDetailDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        Logger.d(TAG, "details size >>>>> " + details.size());
        if (details.isEmpty()) {
            StepNodeDetail detail = new StepNodeDetail();
            detail.setDate(date);
            detail.setStep((long) step);
            detail.setOffset(offset);
            detail.setCalories(calories);
            detail.setUid(PreferencesHelper.getCurrentId());
            detail.setDistance(distance);
            return dao.insert(detail);
        } else {
            Logger.d(TAG, "update detail >>>>>>>>.");
            StepNodeDetail existsDetail = details.get(0);
            existsDetail.setDistance(distance);
            existsDetail.setCalories(calories);
            existsDetail.setStep((long) step);
            dao.update(existsDetail);
            return existsDetail.getId();
        }
    }

    static List<StepNodeDetail> getStepNodeDetail(long date) {
        return App.getStepNodeDetailDao().queryBuilder()
                .where(StepNodeDetailDao.Properties.Date.eq(date),
                        StepNodeDetailDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderAsc(StepNodeDetailDao.Properties.Offset)
                .list();
    }

    static StepNodeDetail getOffsetStep(long date, int offset) {
        List<StepNodeDetail> list = App.getStepNodeDetailDao().queryBuilder()
                .where(StepNodeDetailDao.Properties.Date.eq(date),
                        StepNodeDetailDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                        StepNodeDetailDao.Properties.Offset.eq(offset))
                .build()
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }

    }
}
