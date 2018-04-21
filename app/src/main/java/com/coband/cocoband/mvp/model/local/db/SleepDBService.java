package com.coband.cocoband.mvp.model.local.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coband.App;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.tools.DataHandler;
import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.Sleep;
import com.coband.watchassistant.SleepDao;
import com.coband.watchassistant.SleepNodeDetail;
import com.yc.pedometer.info.SleepTimeInfo;
import com.yc.pedometer.sdk.UTESQLOperate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 17-4-11.
 */

class SleepDBService {
    private static final String TAG = "SleepDBService";

    private static final UTESQLOperate mDBOperation = UTESQLOperate.getInstance(App.getInstance());
    private static final String PATTERN = "yyyyMMdd";
    private static final int AFTERNOON = 720;
    private static final int ONE_DAY_MINUTES = 1440;


    static SleepTimeInfo queryDaySleepFromUteDB(long date) {
        String dateStr = DateUtils.getDateBySeconds(PATTERN, date);
        return mDBOperation.querySleepInfo(dateStr);
    }

    /**
     * query sleep data from sleep db.
     *
     * @param from the begin day
     * @param to   the end day.
     * @return
     */
    static List<Sleep> queryMultiSleepDataFromLocalDB(long from, long to) {
        List<Sleep> sleeps = App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Date.between(from, to),
                        SleepDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderAsc(SleepDao.Properties.Date)
                .list();

        // it means no data;
        if (sleeps.isEmpty()) {
            Logger.d(TAG, "week sleep is empty");
            return sleeps;
        }


        sleeps.clear();

        while (from <= to) {
            Sleep sleep = queryHistorySleepByDateFromLocalDB(from);
            if (sleep == null) {
                sleep = new Sleep();
                sleep.setDate(from);
                sleep.setTotalTime(0);
                sleep.setLight(0);
                sleep.setWakeCount(0);
            }
            sleeps.add(sleep);
            Logger.d(TAG, "sleep time >>>>> " + sleep.getTotalTime());
            from += C.ONE_DAY_SECONDS;
        }

        return sleeps;
    }

    /**
     * query sleep detail data from ute sleep db.
     *
     * @param from the begin day
     * @param to   the end day.
     * @return
     */
    static List<SleepTimeInfo> queryMultiSleepDetailDataFromUteDB(long from, long to) {
        List<SleepTimeInfo> infoList = new ArrayList<>();
        while (from <= to) {
            String fromStr = DateUtils.getDateBySeconds(PATTERN, from);
            SleepTimeInfo info = mDBOperation.querySleepInfo(fromStr);
            if (info == null) {
                info = new SleepTimeInfo();
            }

            infoList.add(info);
            from += C.ONE_DAY_SECONDS;
        }

        return infoList;
    }

    /**
     * get the sleep db date of latest data.
     *
     * @return the latest date. if db no data, return 0.
     */
    static long queryLocalDBLatestDate() {
        List<Sleep> sleepList = App.getSleepDao().queryBuilder()
                .orderDesc(SleepDao.Properties.Date)
                .list();
        if (sleepList.isEmpty()) {
            return 0L;
        } else {
            long date = sleepList.get(0).getDate();
            if (date > DateUtils.getToday() || date < 0) {
                date = DateUtils.getToday();
            }

            return date;
        }
    }

    /**
     * copy ute sleep to our sleep db.
     */
    static void copyUteSleepDataToLocalDB() {
        long sleepDbLatestDate = queryLocalDBLatestDate();
        if (sleepDbLatestDate == 0L) {
            copyUteAllDataToLocalDB();
        } else {
            Logger.d(TAG, "sleep no null >>>>>>>>");
            while (sleepDbLatestDate <= DateUtils.getToday()) {
                Sleep sleep = queryHistorySleepByDateFromLocalDB(sleepDbLatestDate);
                if (sleep == null) {
                    sleep = new Sleep();
                }

                String dateStr = DateUtils.getDateBySeconds(PATTERN, sleepDbLatestDate);
                SleepTimeInfo info = mDBOperation.querySleepInfo(dateStr);
                if (info != null) {
                    sleep.setDate(DateUtils.getMillionSeconds(PATTERN, dateStr));
                    sleep.setDeep(info.getDeepTime());
                    sleep.setLight(info.getLightTime());
                    sleep.setWakeCount(info.getAwakeCount());
                    sleep.setTotalTime(info.getSleepTotalTime());
                    sleep.setUpload(false);
                    sleep.setUid(PreferencesHelper.getCurrentId());
                    App.getSleepDao().insertOrReplace(sleep);
                }

                sleepDbLatestDate += C.ONE_DAY_SECONDS;
            }
        }
    }

    private static Sleep queryHistorySleepByDateFromLocalDB(long date) {
        List<Sleep> list = App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Date.eq(date))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            Logger.d(TAG, "date >>>>> " + DateUtils.getDateBySeconds("M/d", date));
            Logger.d(TAG, "sleep total time >>>>> " + list.get(0).getTotalTime());
            return list.get(0);
        }
    }

    /**
     * query all sleep table and copy to SLEEP table.
     */
    private static void copyUteAllDataToLocalDB() {
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


        Cursor cursor = db.rawQuery("select * from sleep_total_table", null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                Logger.d(TAG, "date >>>>>> " + date);
                long dayMilliseconds = DateUtils.getMillionSeconds("yyyyMMdd", date);
                SleepTimeInfo info = mDBOperation.querySleepInfo(date);
                if (info != null) {
                    Sleep sleep = new Sleep();
                    sleep.setUpload(false);
                    sleep.setDate(dayMilliseconds / 1000);
                    sleep.setUpload(false);
                    sleep.setDeep(info.getDeepTime());
                    sleep.setLight(info.getLightTime());
                    sleep.setWakeCount(info.getAwakeCount());
                    sleep.setTotalTime(info.getSleepTotalTime());
                    sleep.setUid(PreferencesHelper.getCurrentId());
                    App.getSleepDao().insert(sleep);
                }

            }
        }

//        long todayMilliseconds = DateUtils.getToday();
//        int count = 7;
//        while (count > 0) {
//            String dayStr = DateUtils.getDateBySeconds(PATTERN, todayMilliseconds);
//            SleepTimeInfo info = mDBOperation.querySleepInfo(dayStr);
//            if (info != null) {
//                Sleep sleep = new Sleep();
//                sleep.setUpload(false);
//                sleep.setDate(todayMilliseconds);
//                sleep.setUpload(false);
//                sleep.setDeep(info.getDeepTime());
//                sleep.setLight(info.getLightTime());
//                sleep.setWakeCount(info.getAwakeCount());
//                sleep.setTotalTime(info.getSleepTotalTime());
//                App.getSleepDao().insert(sleep);
//            }
//            todayMilliseconds -= C.ONE_DAY_MILLISECONDS;
//            count--;
//        }
    }

    static Sleep querySleepByDateFromLocalDB(long date) {
        List<Sleep> sleeps = App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Date.eq(date),
                        SleepDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (sleeps.isEmpty()) {
            return null;
        } else {
            return sleeps.get(0);
        }
    }

    static List<Sleep> querySleepByListFromLocalDB(long date) {
        List<Sleep> sleeps = App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Date.eq(date),
                        SleepDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (sleeps.isEmpty()) {
            return null;
        } else {
            return sleeps;
        }
    }

    // 确保当天所有数据的Upload都置为false
    static void setSleepUploaded(long date) {
        List<Sleep> sleep = querySleepByListFromLocalDB(date);
        if (sleep != null && !sleep.isEmpty()) {
            for (int i = 0; i < sleep.size(); i++) {
                sleep.get(i).setUpload(true);
                App.getSleepDao().update(sleep.get(i));
            }
        }
    }

    static void insertSleepList(List<Sleep> sleepList) {
        Logger.d(TAG, "sleep list size >>>>>> " + sleepList.size());
        App.getSleepDao().insertOrReplaceInTx(sleepList);
    }

    static boolean isSleepDBEmpty() {
        return App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .build()
                .list()
                .isEmpty();
    }

    static List<Sleep> getNeedToUploadSleepData() {
        return App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Upload.eq(false),
                        SleepDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                        SleepDao.Properties.Date.notEq(DateUtils.getToday()))
                .orderAsc(SleepDao.Properties.Date)
                .list();
    }


    /************************************* for K9 ************************/

    static void insertDaySleepTime(long date, int beginTime, int mode) {
        if (beginTime > AFTERNOON) {
            date += C.ONE_DAY_SECONDS;
        }

        List<Sleep> list = App.getSleepDao().queryBuilder()
                .where(SleepDao.Properties.Date.eq(date),
                        SleepDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            Sleep sleep = new Sleep();
            sleep.setDate(date);
            sleep.setBeginTime(beginTime);
            sleep.setMode(mode);
            sleep.setUid(PreferencesHelper.getCurrentId());
            sleep.setUpload(false);
            App.getSleepDao().insert(sleep);
        } else {
            Sleep existsSleep = list.get(0);
            if (existsSleep.getBeginTime() != null && existsSleep.getMode() != null) {
                int existsSleepMode = existsSleep.getMode();
                int existsSleepBeginTime = existsSleep.getBeginTime();
                if (beginTime < existsSleepBeginTime) {
                    beginTime += ONE_DAY_MINUTES;
                }

                int durationTime = beginTime - existsSleepBeginTime;

                existsSleep.setTotalTime(existsSleep.getTotalTime() + durationTime);

                if (existsSleepMode == Config.K9_DEEP) {
                    existsSleep.setDeep(existsSleep.getDeep() + durationTime);
                } else if (existsSleepMode == Config.K9_LIGHT) {
                    existsSleep.setLight(existsSleep.getLight() + durationTime);
                }

                existsSleep.setMode(mode);
                existsSleep.setBeginTime(beginTime);

                App.getSleepDao().update(existsSleep);

            }
        }
    }

    static void insertTotalSleepData(long date) {
        Logger.d(TAG, "date >>>>>> " + date);
        List<SleepNodeDetail> list = DBHelper.getInstance().getSleepNodeDetailByDate(date);
        Logger.d(TAG, "origin list size >>>>>>>> " + list.size());
        if (list.size() <= 1) {
            return;
        }


        list = DataHandler.filterSleepData(list);
        Logger.d(TAG, "filter list size >>>>>>>> " + list.size());
        if (list.size() <= 1) {
            return;
        }


        Sleep sleep = new Sleep();
        sleep.setDate(date);
        sleep.setUpload(false);
        sleep.setUid(PreferencesHelper.getCurrentId());
        int totalTime = 0;
        int awakeCount = 0;
        int deepTime = 0;
        int lightTime = 0;

        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail next = list.get(i);
            int nextBegin = next.getBegin();
            int detailBegin = detail.getBegin();

            if (nextBegin < detailBegin) {
                nextBegin += 1440;
            }

            totalTime += nextBegin - detailBegin;
            if (detail.getMode() == Config.K9_AWAKE) {
                awakeCount++;
            } else if (detail.getMode() == Config.K9_DEEP) {
                deepTime += nextBegin - detailBegin;
            } else {
                lightTime += nextBegin - detailBegin;
            }
        }

        // filter the total time less than 30 minutes sleep data.
        if (totalTime <= 30) {
            return;
        }

        sleep.setTotalTime(totalTime);
        sleep.setWakeCount(awakeCount);
        sleep.setDeep(deepTime);
        sleep.setLight(lightTime);

        insertDaySleep(sleep);
    }

    private static void insertDaySleep(Sleep sleep) {
        List<Sleep> sleeps = new ArrayList<>();
        sleeps.add(sleep);
        insertSleepList(sleeps);
    }

//    static List<HistoryData> getNeedToUploadSleepData() {
//        return App.getSleepDao().queryBuilder()
//                .where(SleepDao.Properties.Upload.eq(false),
//                        SleepDao.Properties.Date.notEq(DateUtils.getToday()))
//                .list();
//    }
}
