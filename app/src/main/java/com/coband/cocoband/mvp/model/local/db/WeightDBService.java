package com.coband.cocoband.mvp.model.local.db;

import com.coband.App;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.DBWeightDao;

import java.util.Collections;
import java.util.List;

/**
 * Created by ivan on 17-5-8.
 */

class WeightDBService {

    private static final String TAG = "WeightDBService";

    /**
     * @param date which date
     * @return
     */
    static DBWeight getDayWeight(long date) {
        List<DBWeight> weightList = App.getWeightDao().queryBuilder()
                .where(DBWeightDao.Properties.Date.eq(date),
                        DBWeightDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (weightList.isEmpty()) {
            return null;
        } else {
            return weightList.get(0);
        }
    }

    /**
     * get multiple day's weight;
     *
     * @param from the begin day.
     * @param to   the end day.
     * @return
     */
    static List<DBWeight> getMultiWeight(long from, long to) {
        if (to < from) {
            throw new IllegalArgumentException("the begin date can't be more than end day");
        }

        return App.getWeightDao().queryBuilder()
                .where(DBWeightDao.Properties.Date.between(from, to))
                .list();
    }

    /**
     * update the weight if not exists, insert it otherwise.
     *
     * @param weight
     */
    static void insertWeight(DBWeight weight) {
        if (weight == null) {
            throw new NullPointerException("the weight is null");
        }

        App.getWeightDao().insertOrReplace(weight);
    }

    /**
     * @param weights
     */
    static void insertWeights(List<DBWeight> weights) {
        if (weights.isEmpty()) {
            return;
        }

        App.getWeightDao().insertOrReplaceInTx(weights);
    }

    static void setWeightUploaded(long date) {
        DBWeight weight = getDayWeight(date);
        if (weight != null) {
            weight.setUpload(true);
            App.getWeightDao().update(weight);
        }
    }

    static List<DBWeight> getAllWeight() {
        List<DBWeight> list = App.getWeightDao().queryBuilder()
                .where(DBWeightDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderAsc(DBWeightDao.Properties.Date)
                .build()
                .list();
        return list;
    }

    static void deleteWeight(DBWeight weight) {
        if (weight != null && weight.getDate() != 0) {
            List<DBWeight> list = App.getWeightDao().queryBuilder()
                    .where(DBWeightDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                            DBWeightDao.Properties.Date.eq(weight.getDate()))
                    .build()
                    .list();
            if (!list.isEmpty()) {
                App.getWeightDao().deleteInTx(list);
            }
        }
    }

    static List<DBWeight> getLastSevenDayWeight() {
        List<DBWeight> list = App.getWeightDao().queryBuilder()
                .where(DBWeightDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .limit(7)
                .orderDesc(DBWeightDao.Properties.Date)
                .build()
                .list();
        Collections.reverse(list);
        return list;
    }
}
