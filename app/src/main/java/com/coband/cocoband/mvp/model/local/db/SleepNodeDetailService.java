package com.coband.cocoband.mvp.model.local.db;

import com.coband.App;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.C;
import com.coband.watchassistant.SleepNodeDetail;
import com.coband.watchassistant.SleepNodeDetailDao;

import java.util.List;

/**
 * Created by ivan on 17-7-29.
 */

public class SleepNodeDetailService {
    private static final int AFTERNOON_MINUTES = 720;
    private static final String TAG = "SleepNodeDetailService";

    static void insertSleepNodeDetail(final SleepNodeDetail detail) {
        List<SleepNodeDetail> list = App.getSleepNodeDetailDao().queryBuilder()
                .where(SleepNodeDetailDao.Properties.Date.eq(detail.getDate()),
                        SleepNodeDetailDao.Properties.Begin.eq(detail.getBegin()))
                .list();
        if (list.isEmpty()) {
            App.getSleepNodeDetailDao().insert(detail);
        } else {
            SleepNodeDetail existsDetail = list.get(0);
            existsDetail.setBegin(detail.getBegin());
            existsDetail.setMode(detail.getMode());
            App.getSleepNodeDetailDao().update(existsDetail);
        }
    }

    static List<SleepNodeDetail> getSleepNodeDetailByDate(long date) {
        List<SleepNodeDetail> todaySleepNodeList = App.getSleepNodeDetailDao().queryBuilder()
                .where(SleepNodeDetailDao.Properties.Date.eq(date),
                        SleepNodeDetailDao.Properties.Begin.le(AFTERNOON_MINUTES),
                        SleepNodeDetailDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderAsc(SleepNodeDetailDao.Properties.Begin)
                .list();

        long yesterday = date - C.ONE_DAY_SECONDS;
        List<SleepNodeDetail> yesterdaySleepNodeList = App.getSleepNodeDetailDao().queryBuilder()
                .where(SleepNodeDetailDao.Properties.Date.eq(yesterday),
                        SleepNodeDetailDao.Properties.Begin.gt(AFTERNOON_MINUTES),
                        SleepNodeDetailDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderAsc(SleepNodeDetailDao.Properties.Begin)
                .list();

        yesterdaySleepNodeList.addAll(todaySleepNodeList);

        return yesterdaySleepNodeList;
    }

    static long getLastSleepDate() {
        List<SleepNodeDetail> list = App.getSleepNodeDetailDao().queryBuilder()
                .where(SleepNodeDetailDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderDesc(SleepNodeDetailDao.Properties.Date)
                .build().list();
        if (!list.isEmpty()) {
            return 0L;
        } else {
            return list.get(0).getDate();
        }
    }
}
