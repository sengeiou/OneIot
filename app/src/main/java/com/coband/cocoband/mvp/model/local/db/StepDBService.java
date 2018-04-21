package com.coband.cocoband.mvp.model.local.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coband.App;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.HistoryDataDao;
import com.coband.watchassistant.StepNodeDetail;
import com.yc.pedometer.info.StepInfo;
import com.yc.pedometer.info.StepOneDayAllInfo;
import com.yc.pedometer.sdk.UTESQLOperate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ivan on 17-4-11.
 * node data query from ute db(the table name look like step_table_'date', e.g step_table_20170505).
 * the history data query from history table(HISTORY_DATA)
 */

class StepDBService {
    private static final String TAG = "StepDBService";

    private static final String PATTERN = "yyyyMMdd";
    private static final UTESQLOperate mDBOperation = UTESQLOperate.getInstance(App.getInstance());

    static StepOneDayAllInfo queryDayNodeStep(long date) {
        String dateStr = DateUtils.getDateBySeconds(PATTERN, date);
        return mDBOperation.queryRunWalkInfo(dateStr);
    }

    static boolean isStepDBEmpty() {
        return App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .build()
                .list().isEmpty();
    }

    /**
     * query step data from history data table.
     *
     * @param date which date
     * @return HistoryData
     */
    static HistoryData queryDayStepData(long date) {
        List<HistoryData> list = App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Date.eq(date),
                        HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    static List<HistoryData> queryDayStepList(long date) {
        List<HistoryData> list = App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Date.eq(date),
                        HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    // 确保当天所有数据的Upload都置为false
    static void setStepUploaded(long date) {
        List<HistoryData> data = queryDayStepList(date);
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setUpload(true);
                App.getHistoryDataDao().update(data.get(i));
            }
        }
    }

    /**
     * get the history step db date of latest data.
     *
     * @return the latest date. if db no data, return 0.
     */
    private static long queryHistoryDBLatestDate() {
        List<HistoryData> historyDataList = App.getHistoryDataDao().queryBuilder()
                .orderDesc(HistoryDataDao.Properties.Date)
                .list();
        if (historyDataList.isEmpty()) {
            return 0L;
        } else {
            long date = historyDataList.get(0).getDate();
            if (date > DateUtils.getToday() || date < 0) {
                date = DateUtils.getToday();
            }

            return date;
        }
    }

    static HistoryData queryHistoryStepData(long date) {
        List<HistoryData> list = App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Date.eq(date),
                        HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * copy the ute step data to history db.
     */
    static void copyUteStepToHistoryDB() {
        Logger.d(TAG, "copy step to local DB");
        long theHistoryLatestDate = queryHistoryDBLatestDate();

        // it means the history db has no data.
        if (theHistoryLatestDate == 0L) {
            copyAllStepDataToHistoryDB();
        } else {
            while (theHistoryLatestDate <= DateUtils.getToday()) {
                String dateStr = DateUtils.getDateBySeconds(PATTERN, theHistoryLatestDate);
                HistoryData dayData = queryHistoryStepData(theHistoryLatestDate);
                StepInfo info = mDBOperation.queryStepInfo(dateStr);
                if (dayData == null) {
                    dayData = new HistoryData();
                }


                if (info != null) {
                    dayData.setUpload(false);
                    dayData.setDate(theHistoryLatestDate);
                    dayData.setCalories(info.getCalories());
                    dayData.setDistance(info.getDistance());
                    dayData.setStep((long) info.getStep());
                    int target = Config.DEFAULT_STEP_TARGET;
                    if (DBHelper.getInstance().getCurrentAccount().getStepTarget() != null) {
                        target = DBHelper.getInstance().getCurrentAccount().getStepTarget();
                    }
                    dayData.setTarget((long) target);
                    dayData.setTargetFinish(info.getStep() >= target);
                    dayData.setUid(PreferencesHelper.getCurrentId());

                    App.getHistoryDataDao().insertOrReplace(dayData);
                }

                theHistoryLatestDate += C.ONE_DAY_SECONDS;
            }
        }
    }

    /**
     * FUCK UTE SB!!!
     */
    private static void copyAllStepDataToHistoryDB() {
        final String UTE_DB = "pedometer.db";
//        SQLiteDatabase db = App.getInstance().openOrCreateDatabase(UTE_DB, Context.MODE_PRIVATE, null);

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


        List<HistoryData> stepList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from step_total_table", null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int steps = cursor.getInt(cursor.getColumnIndex("step"));
                float calories = cursor.getInt(cursor.getColumnIndex("calories"));
                float distance = cursor.getFloat(cursor.getColumnIndex("distance"));
                int sportTime = cursor.getInt(cursor.getColumnIndex("sport_time"));

                HistoryData data = new HistoryData();
                Logger.d(TAG, "date >>>>> " + date);
                Logger.d(TAG, "date milliseconds >>>>> " + DateUtils.getMillionSeconds("yyyyMMdd", date));
                data.setDate(DateUtils.getMillionSeconds("yyyyMMdd", date) / 1000);
                data.setUpload(false);
                data.setCalories(calories);
                data.setDistance(distance);
                data.setStep((long) steps);
                data.setTime((long) sportTime);
                data.setUid(PreferencesHelper.getCurrentId());
                int target = Config.DEFAULT_STEP_TARGET;
                if (DBHelper.getInstance().getCurrentAccount().getStepTarget() != null) {
                    target = DBHelper.getInstance().getCurrentAccount().getStepTarget();
                }

                data.setTarget((long) target);
                data.setTargetFinish(steps >= target);
                stepList.add(data);
                cursor.moveToNext();
            }
        }

        cursor.close();

        if (!stepList.isEmpty()) {
            insertHistoryDataToDB(stepList);
        }

    }


    static void insertHistoryDataToDB(List<HistoryData> dataList) {
        App.getHistoryDataDao().insertOrReplaceInTx(dataList);
    }

    static List<HistoryData> getNeedToUploadStepData() {
        return App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Upload.eq(false),
                        HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                        HistoryDataDao.Properties.Date.notEq(DateUtils.getToday()))
                .orderAsc(HistoryDataDao.Properties.Date)
                .list();
    }

    static List<HistoryData> getAllStepData() {
        return App.getHistoryDataDao().queryBuilder().list();
    }

    static List<HistoryData> getUserStepData() {
        return App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                        HistoryDataDao.Properties.Date.notEq(DateUtils.getToday()))
                .list();
    }


    /*************************************** for k9 data *********************************/

    static void insertHistoryDataToDB(HistoryData data) {
        long date = data.getDate();
        List<HistoryData> historyDatas = App.getHistoryDataDao().queryBuilder()
                .where(HistoryDataDao.Properties.Date.eq(date),
                        HistoryDataDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        Logger.d(TAG, "history date >>>>> " + date + " size >>>> " + historyDatas.size());
        if (historyDatas.isEmpty()) {
            Logger.d(TAG, "insert >>>>>");
            App.getHistoryDataDao().insert(data);
        } else {
            Logger.d(TAG, "update >>>>>>");
            HistoryData existsData = historyDatas.get(0);
            existsData.setCalories(data.getCalories());
            existsData.setStep(data.getStep());
            existsData.setDistance(data.getDistance());
            Integer stepTarget = DBHelper.getInstance().getCurrentAccount().getSleepTarget();
            int stepGoal;
            if (stepTarget == null) {
                stepGoal = Config.DEFAULT_STEP_TARGET;
            } else {
                stepGoal = stepTarget;
            }
            existsData.setTargetFinish(existsData.getStep() > stepGoal);
            existsData.setUpload(false);
            App.getHistoryDataDao().update(existsData);
        }
    }

    static void updateDayStep(long date) {
        HistoryData data = new HistoryData();
        long totalStep = 0L;
        int totalCalories = 0;
        int totalDistance = 0;
        List<StepNodeDetail> list = DBHelper.getInstance().getDayStepNodeDetail(date);
        for (StepNodeDetail detail : list) {
            totalStep += detail.getStep();
            totalCalories += detail.getCalories();
            totalDistance += detail.getDistance();
        }

        data.setCalories(totalCalories / 1000);
        data.setStep(totalStep);
        Integer stepTarget = DBHelper.getInstance().getCurrentAccount().getSleepTarget();
        int stepGoal;
        if (stepTarget == null) {
            stepGoal = Config.DEFAULT_STEP_TARGET;
        } else {
            stepGoal = stepTarget;
        }

        data.setTargetFinish(totalStep > stepGoal);
        data.setUpload(false);
        data.setDate(date);
        data.setDistance(totalDistance / 1000f);
        data.setUid(PreferencesHelper.getCurrentId());

        insertHistoryDataToDB(data);
        Logger.d(TAG, "updateDayStep");
    }
}
