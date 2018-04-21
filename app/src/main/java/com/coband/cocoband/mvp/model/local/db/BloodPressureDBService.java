package com.coband.cocoband.mvp.model.local.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coband.App;
import com.coband.cocoband.mvp.model.entity.SingleBloodPressure;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.C;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.BloodPressureDao;
import com.yc.pedometer.info.BPVOneDayInfo;
import com.yc.pedometer.sdk.UTESQLOperate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgc on 17-9-21.
 */

class BloodPressureDBService {
    private static final String TAG = "BloodPressureDBService";
    private static final String PATTERN = "yyyyMMdd";
    private static final UTESQLOperate mDBOperate = UTESQLOperate.getInstance(App.getInstance());

    static List<BloodPressure> queryMultiDayBloodPressure(long from, long to) {
        return App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Date.between(from, to),
                        BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderDesc(BloodPressureDao.Properties.Date)
                .list();
    }

    static void setBloodPressureUploaded(long date) {
        List<BloodPressure> pressure = queryDayBloodPressureListFromDB(date);
        if (pressure != null && !pressure.isEmpty()) {
            for (int i = 0; i < pressure.size(); i++) {
                pressure.get(i).setUpload(true);
                App.getBloodPressureDao().update(pressure.get(i));
            }
        }
    }

    static boolean isBloodDBEmpty() {
        return App.getBloodPressureDao().loadAll().isEmpty();
    }

    static int bloodDBSize() {
        return App.getBloodPressureDao().loadAll().size();
    }

    static boolean currentUserDBEmpty() {
        List<BloodPressure> list = App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (null == list || list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    static BloodPressure queryDayBloodPressureFromDB(long date) {
        List<BloodPressure> list = App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Date.eq(date),
                        BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    static List<BloodPressure> queryDayBloodPressureListFromDB(long date) {
        List<BloodPressure> list = App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Date.eq(date),
                        BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    static BloodPressure queryDayBloodPressureWithTime(long dateNow) {
        long date = DateUtils.getStartTimeOfDay(dateNow);
        Logger.d("queryDayBloodPressureWithTime", "-- " + date);
        List<BloodPressure> list = App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Date.eq(date),
                        BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    static long queryBloodPressureDBLatestDate() {
        List<BloodPressure> historyDataList = App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .orderDesc(BloodPressureDao.Properties.Date)
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

    static void insertBloodPressure(List<BloodPressure> bloodPressures) {
        App.getBloodPressureDao().insertOrReplaceInTx(bloodPressures);
    }

    static List<BloodPressure> getNeedToUploadBloodData() {
        return App.getBloodPressureDao().queryBuilder()
                .where(BloodPressureDao.Properties.Upload.eq(false),
                        BloodPressureDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()),
                        BloodPressureDao.Properties.Date.notEq(DateUtils.getToday()))
                .orderAsc(BloodPressureDao.Properties.Date)
                .list();
    }

    /**************************************** K9 ************************************/

    static void insertBloodPressure(long date, long time, int heartRateValue,
                                    int highPressureValue, int lowPressureValue) {
        Gson gson = new Gson();

        SingleBloodPressure pressure = new SingleBloodPressure();
//        pressure.setHeartRate(heartRateValue);
        pressure.setHighPressure(highPressureValue);
        pressure.setLowPressure(lowPressureValue);
        pressure.setTime(time + date);

        BloodPressure bloodPressure = queryDayBloodPressureFromDB(date);

        if (null == bloodPressure) {
            bloodPressure = new BloodPressure();
            List<SingleBloodPressure> list = new ArrayList<>();
            list.add(pressure);
            String json = gson.toJson(list);
            bloodPressure.setUid(PreferencesHelper.getCurrentId());
            bloodPressure.setUpload(false);
            bloodPressure.setDate(date);
            bloodPressure.setBloodPressures(json);
            App.getBloodPressureDao().insertOrReplace(bloodPressure);
        } else {
            String json = bloodPressure.getBloodPressures();
            List<SingleBloodPressure> pressureInfoList;
            Type listType = new TypeToken<List<SingleBloodPressure>>() {
            }.getType();
            pressureInfoList = gson.fromJson(json, listType);

            pressureInfoList.add(pressure);

            String newJson = gson.toJson(pressureInfoList);
            bloodPressure.setBloodPressures(newJson);
            bloodPressure.setUpload(false);
            App.getBloodPressureDao().update(bloodPressure);
        }
    }

    static void copyUteBloodPressureToLocalDB() {
        long localDBLatestDate = queryBloodPressureDBLatestDate();
        if (localDBLatestDate == 0L) {
            copyAllBloodPressureDataToLocalDB();
        } else {
            Gson gson = new Gson();

            while (localDBLatestDate <= DateUtils.getToday()) {
                String dateStr = DateUtils.getDateBySeconds(PATTERN, localDBLatestDate);
                BloodPressure bp = queryDayBloodPressureFromDB(localDBLatestDate);
//                List<SingleBloodPressure> exitsBP = new ArrayList<>();
                if (bp == null) {
                    bp = new BloodPressure();
//                } else {
//                    String bpJson = bp.getBloodPressures();
//                    Type listType = new TypeToken<List<SingleBloodPressure>>() {
//                    }.getType();
//                    exitsBP = gson.fromJson(bpJson, listType);
                }

                List<BPVOneDayInfo> list = mDBOperate.queryBloodPressureOneDayInfo(dateStr);
                Logger.d(TAG, "list size >>>>>>>>> " + list.size());
                if (!list.isEmpty()) {
                    bp.setDate(localDBLatestDate);
                    bp.setUpload(false);
                    bp.setUid(PreferencesHelper.getCurrentId());

                    List<SingleBloodPressure> singleBPList = new ArrayList<>();
                    for (BPVOneDayInfo info : list) {
                        SingleBloodPressure singleBP = new SingleBloodPressure();
                        singleBP.setHighPressure(info.getHightBloodPressure());
                        singleBP.setLowPressure(info.getLowBloodPressure());
                        singleBP.setTime(localDBLatestDate + info.getBloodPressureTime() * 60);
                        singleBPList.add(singleBP);
                    }

//                    exitsBP.addAll(singleBPList);

                    bp.setBloodPressures(gson.toJson(singleBPList));
                    App.getBloodPressureDao().insertOrReplace(bp);
                }

                localDBLatestDate += C.ONE_DAY_SECONDS;
            }
        }
    }

    /**
     * FUCK UTE SB!!!
     */
    private static void copyAllBloodPressureDataToLocalDB() {
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
            List<BPVOneDayInfo> list = mDBOperate.queryBloodPressureOneDayInfo(dayStr);
            if (!list.isEmpty()) {
                BloodPressure bloodPressure = new BloodPressure();
                bloodPressure.setUpload(false);
                bloodPressure.setDate(todaySeconds);
                bloodPressure.setUid(PreferencesHelper.getCurrentId());
                List<SingleBloodPressure> singleBPList = new ArrayList<>();
                for (BPVOneDayInfo info : list) {
                    SingleBloodPressure singleBP = new SingleBloodPressure();
                    singleBP.setHighPressure(info.getHightBloodPressure());
                    singleBP.setLowPressure(info.getLowBloodPressure());
                    singleBP.setTime(todaySeconds + info.getBloodPressureTime() * 60);
                    singleBPList.add(singleBP);
                }
                bloodPressure.setBloodPressures(gson.toJson(singleBPList));
                App.getBloodPressureDao().insert(bloodPressure);
            }

            tryObtainDataCount++;
            todaySeconds -= C.ONE_DAY_SECONDS;
            count++;
        }
    }
}
