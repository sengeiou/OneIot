package com.coband.cocoband.mvp.model.local.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coband.App;
import com.coband.cocoband.mvp.model.entity.LastRate;
import com.coband.cocoband.mvp.model.entity.SingleRate;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.tools.DataHandler;
import com.coband.common.utils.C;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.HeartRate;
import com.coband.watchassistant.HeartRateDao;
import com.yc.pedometer.info.RateOneDayInfo;
import com.yc.pedometer.sdk.UTESQLOperate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 17-4-11.
 */

class HeartRateDBService {

    private static final String TAG = "HeartRateDBService";

    private static final String PATTERN = "yyyyMMdd";
    private static final UTESQLOperate mDBOperate = UTESQLOperate.getInstance(App.getInstance());

    /**
     * @param date which day. milliseconds
     * @return the day heart rate detail info. include testing time and the heart rate value
     * on the time.
     */
    static List<RateOneDayInfo> queryDayHeartRateDetail(long date) {
        String dateStr = DateUtils.getDateBySeconds(PATTERN, date);
        return mDBOperate.queryRateOneDayDetailInfo(dateStr);
    }

    static List<HeartRate> queryMultiDayHeartRate(long from, long to) {
        return App.getHeartRateDao().queryBuilder()
                .where(HeartRateDao.Properties.Date.between(from, to),
                        HeartRateDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderDesc(HeartRateDao.Properties.Date)
                .list();
    }


    /**
     * copy the ute heart rate(the table name look like rate_table_20170505) to heart rate db(HEART_RATE).
     */
    static void copyUteHeartRateToLocalDB() {
        long latestDate = queryHeartRateDBLatestDate();
        if (latestDate == 0L) {
            copyAllUteHeartRateDataToLocalDB();
        } else {
            latestDate = DateUtils.getMillionSeconds(PATTERN, "20180101") / 1000;
            Gson gson = new Gson();
            while (latestDate <= DateUtils.getToday()) {
                String dateStr = DateUtils.getDateBySeconds(PATTERN, latestDate);
                Logger.d(TAG, "dateString >>>>>>> " + dateStr);
                HeartRate rate = queryDayHeartRateFromDB(latestDate);
                if (rate == null) {
                    rate = new HeartRate();
                }

                List<RateOneDayInfo> list = mDBOperate.queryRateOneDayDetailInfo(dateStr);
                if (!list.isEmpty()) {
                    rate.setDate(latestDate);
                    rate.setUpload(false);
                    rate.setUid(PreferencesHelper.getCurrentId());

                    List<SingleRate> singleRateList = new ArrayList<>();
                    for (RateOneDayInfo info : list) {
                        SingleRate singleRate = new SingleRate();
                        singleRate.setRate(info.getRate());
                        Logger.d(TAG, "rate >>>>> " + info.getRate());
                        singleRate.setTime(info.getTime());
                        singleRateList.add(singleRate);
                    }
                    rate.setHeartRate(gson.toJson(singleRateList));

                    App.getHeartRateDao().insertOrReplace(rate);
                }

                latestDate += C.ONE_DAY_SECONDS;
            }
        }
    }

    static void setHeartRateUploaded(long date) {
        List<HeartRate> rate = queryDayHeartRateListFromDB(date);
        if (rate != null && !rate.isEmpty()) {
            for (int i = 0; i < rate.size(); i++) {
                rate.get(i).setUpload(true);
                App.getHeartRateDao().update(rate.get(i));
            }
        }
    }

    /**
     * 因为手环只能存储7天的数据，所以从UTE的数据库更新到我们自己的数据库时，最多只有7天的数据需要更新
     */
    private static void copyAllUteHeartRateDataToLocalDB() {
        final String UTE_DB = "pedometer.db";
        SQLiteOpenHelper helper = new SQLiteOpenHelper(App.getInstance(), UTE_DB, null, 4) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        SQLiteDatabase db = helper.getWritableDatabase();

        if (db == null) {
            return;
        }


        int tryObtainDataCount = 0;
        long todaySeconds = DateUtils.getToday();
        int count = 0;
        Gson gson = new Gson();
        while (count < 7) {
            // 如果尝试获取数据超过30次,不再获取. FUCK UTE SB!!!
            if (tryObtainDataCount > 30) {
                break;
            }

            String dayStr = DateUtils.getDateBySeconds(PATTERN, todaySeconds);
            List<RateOneDayInfo> list = mDBOperate.queryRateOneDayDetailInfo(dayStr);
            if (!list.isEmpty()) {
                HeartRate rate = new HeartRate();
                rate.setUpload(false);
                rate.setDate(todaySeconds);
                rate.setUid(PreferencesHelper.getCurrentId());
                List<SingleRate> singleRateList = new ArrayList<>();
                for (RateOneDayInfo info : list) {
                    SingleRate singleRate = new SingleRate();
                    singleRate.setRate(info.getRate());
                    singleRate.setTime(info.getTime());
                    singleRateList.add(singleRate);
                }
                rate.setHeartRate(gson.toJson(singleRateList));
                App.getHeartRateDao().insert(rate);
            }

            tryObtainDataCount++;
            todaySeconds -= C.ONE_DAY_SECONDS;
            count++;
        }
    }


    /**
     * get the heart rate db date of latest data.
     *
     * @return the latest date, seconds. if db no data, return 0.
     */
    private static long queryHeartRateDBLatestDate() {
        List<HeartRate> historyDataList = App.getHeartRateDao().queryBuilder()
                .orderDesc(HeartRateDao.Properties.Date)
                .list();
        if (historyDataList.isEmpty()) {
            return 0L;
        } else {
            long date = historyDataList.get(0).getDate() / 1000;
            if (date > DateUtils.getToday() || date < 0) {
                date = DateUtils.getToday();
            }

            return date;
        }
    }

    static HeartRate queryDayHeartRateFromDB(long date) {
        List<HeartRate> list = App.getHeartRateDao().queryBuilder()
                .where(HeartRateDao.Properties.Date.eq(date),
                        HeartRateDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    static List<HeartRate> queryDayHeartRateListFromDB(long date) {
        List<HeartRate> list = App.getHeartRateDao().queryBuilder()
                .where(HeartRateDao.Properties.Date.eq(date),
                        HeartRateDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    static void insertHeartRates(List<HeartRate> heartRates) {
        App.getHeartRateDao().insertOrReplaceInTx(heartRates);
    }

    static boolean isRateDBEmpty() {
        return App.getHeartRateDao().queryBuilder()
                .where(HeartRateDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .build()
                .list()
                .isEmpty();
//        return App.getHeartRateDao().loadAll().isEmpty();
    }

    static List<HeartRate> getNeedToUploadSleepData() {
        return App.getHeartRateDao().queryBuilder()
                .where(HeartRateDao.Properties.Upload.eq(false),
                        HeartRateDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                        HeartRateDao.Properties.Date.notEq(DateUtils.getToday()))
                .orderAsc(HeartRateDao.Properties.Date)
                .list();
    }


    /**************************************** K9 ************************************/

    static void insertHeartRate(long date, long time, int heartRateValue) {
        Gson gson = new Gson();

        SingleRate rate = new SingleRate();
        rate.setRate(heartRateValue);
        rate.setTime(time);

        HeartRate heartRate = queryDayHeartRateFromDB(date);

        if (heartRate == null) {
            heartRate = new HeartRate();
            List<SingleRate> list = new ArrayList<>();
            list.add(rate);
            String json = gson.toJson(list);
            heartRate.setUid(PreferencesHelper.getCurrentId());
            heartRate.setUpload(false);
            heartRate.setDate(date);
            heartRate.setHeartRate(json);
            App.getHeartRateDao().insertOrReplace(heartRate);
        } else {
            String json = heartRate.getHeartRate();
            List<SingleRate> rateInfoList;
            Type listType = new TypeToken<List<SingleRate>>() {
            }.getType();
            rateInfoList = gson.fromJson(json, listType);

            for (SingleRate singleRate : rateInfoList) {
                if (singleRate.getTime() == rate.getTime()) {
                    singleRate.setRate(rate.getRate());
                    String newJson = gson.toJson(rateInfoList);
                    heartRate.setHeartRate(newJson);
                    heartRate.setUpload(false);
                    App.getHeartRateDao().update(heartRate);
                    return;
                }
            }

            rateInfoList.add(rate);

            String newJson = gson.toJson(rateInfoList);
            Logger.d(TAG, "new json >>>>> " + newJson);
            heartRate.setHeartRate(newJson);
            heartRate.setUpload(false);
            App.getHeartRateDao().update(heartRate);
        }

    }

    private static HeartRate filterDirtyData(List<HeartRate> rateList) {
        if (rateList.isEmpty()) {
            return null;
        }

        for (HeartRate rate : rateList) {
            if (!DataHandler.isDirtyData(rate.getDate())) {
                if (rate.getHeartRate() != null && !rate.getHeartRate().isEmpty()) {
                    return rate;
                }
            }
        }

        return null;
    }

    static LastRate queryLastHeartRateData() {
        List<HeartRate> list = App.getHeartRateDao().queryBuilder()
                .where(HeartRateDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderDesc(HeartRateDao.Properties.Date)
                .build()
                .list();
        HeartRate rate = filterDirtyData(list);
        if (rate == null) {
            return null;
        }

        Gson gson = new Gson();

        Type listType = new TypeToken<List<SingleRate>>() {
        }.getType();
        List<SingleRate> rateInfoList = gson.fromJson(rate.getHeartRate(), listType);
        if (rateInfoList.isEmpty()) {
            return null;
        }

        long minutes = 0;
        SingleRate lastRate = null;
        for (SingleRate singleRate : rateInfoList) {
            // add by ivan. fix bug about the rate time inconsistent.
            if (singleRate.getTime() > 1440) {
                singleRate.setTime(DateUtils.getMinuteOfDay(singleRate.getTime()));
            }

            if (singleRate.getTime() >= minutes) {
                minutes = singleRate.getTime();
                lastRate = singleRate;
            }
        }

        return new LastRate(rate.getDate(), lastRate);


    }
}
