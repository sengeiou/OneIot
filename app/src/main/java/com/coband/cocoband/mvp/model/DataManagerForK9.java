package com.coband.cocoband.mvp.model;

import android.util.Log;

import com.coband.watchassistant.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coband.cocoband.mvp.model.bean.RateOneDayBean;
import com.coband.cocoband.mvp.model.bean.Step;
import com.coband.cocoband.mvp.model.entity.DaySleepInfo;
import com.coband.cocoband.mvp.model.entity.DayStepInfo;
import com.coband.cocoband.mvp.model.entity.LastRate;
import com.coband.cocoband.mvp.model.entity.LastSleepData;
import com.coband.cocoband.mvp.model.entity.MultiDaySleepInfo;
import com.coband.cocoband.mvp.model.entity.MultiDayStepInfo;
import com.coband.cocoband.mvp.model.entity.SingleBloodPressure;
import com.coband.cocoband.mvp.model.entity.SingleRate;
import com.coband.cocoband.mvp.model.entity.StepInfo;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.tools.DataHandler;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.HeartRate;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.Sleep;
import com.coband.watchassistant.SleepNodeDetail;
import com.coband.watchassistant.StepNodeDetail;
import com.coband.watchassistant.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ivan on 17-7-29.
 * obtains K9 data from database
 */

class DataManagerForK9 extends AbstractManager {
    private static final String TAG = "DataManagerForK9";

    private static DataManagerForK9 mInstance;


    private DataManagerForK9() {

    }

    public static DataManagerForK9 getInstance() {
        if (mInstance == null) {
            synchronized (DataManagerForK9.class) {
                if (mInstance == null) {
                    mInstance = new DataManagerForK9();
                }
            }
        }

        return mInstance;
    }


    @Override
    protected DayStepInfo getDayStepInfo(long date) {
        List<StepNodeDetail> dayStepDetails = DBHelper.getInstance().getDayStepNodeDetail(date);
        HistoryData data = DBHelper.getInstance().queryDayTotalStepData(date);

        // the K9 can't be switch unit. the unit always metric.
        int unit = DataManager.getInstance().getUnitSystem();
//        int unit = Config.METRIC;

        DayStepInfo dayStepInfo = new DayStepInfo();
        if (data == null) {
            dayStepInfo.setDate(date);
            dayStepInfo.setUnit(unit);
            return dayStepInfo;
        }

        double distance = data.getDistance();
        double calories = data.getCalories();
        long steps = data.getStep();


        if (unit == Config.INCH) {
            distance = Utils.changeKmToMiles(distance);
        }

        dayStepInfo.setCalories(calories);
        dayStepInfo.setDistance(distance);
        dayStepInfo.setSteps(steps);
        dayStepInfo.setUnit(unit);

        Integer stepTarget = DBHelper.getInstance().getCurrentAccount().getStepTarget();
        int stepGoal = stepTarget == null ? Config.DEFAULT_STEP_TARGET : stepTarget;
        float progress = data.getStep() / (float) stepGoal;
        dayStepInfo.setProgress(progress > 1 ? 1 : progress);
        List<StepInfo> nodeInfo = new ArrayList<>();

        int currentNode = 0;
        StepInfo info = null;
        for (StepNodeDetail detail : dayStepDetails) {
            int node = detail.getOffset() * 15 / 60 + 1;
            if (node != currentNode) {
                currentNode = node;
                info = new StepInfo();
                info.setSteps(detail.getStep());
                info.setTimeNode(node);
                nodeInfo.add(info);
            } else {
                info.setSteps(info.getSteps() + detail.getStep());
            }
        }
        dayStepInfo.setNodeInfoList(nodeInfo);

        return dayStepInfo;


    }

    @Override
    protected MultiDayStepInfo getMonthHistoryData(long date) {
        MultiDayStepInfo multiDayStepInfo = new MultiDayStepInfo();

        List<HistoryData> monthHistoryData = DBHelper.getInstance().getMonthHistoryData(date);
        List<Date> monthDates = DateUtils.getEveryDateOfMonth(date);
        int unit = DataManager.getInstance().getUnitSystem();

        long totalStep = 0L;
        float totalCalories = 0;
        float totalDistance = 0.0f;
        int finishTargetCount = 0;

        int i = 0;
        for (HistoryData data : monthHistoryData) {
            totalStep += data.getStep();
            totalCalories += data.getCalories();
            totalDistance += data.getDistance();
            finishTargetCount += data.getTargetFinish() ? 1 : 0;
            if (data.getStep() > 0) {
                i++;
            }
        }
        i = (i <= 0 ? 1 : i);

        float aveCalories = totalCalories / i;
        float aveDistance = totalDistance / i;
        long aveStep = totalStep / i;

        multiDayStepInfo.setUnit(unit);
        multiDayStepInfo.setAveCalories(aveCalories);
        multiDayStepInfo.setAveDistance(aveDistance);
        multiDayStepInfo.setAveSteps(aveStep);
        multiDayStepInfo.setDayStepInfoList(monthHistoryData);
        multiDayStepInfo.setTotalSteps(totalStep);
        multiDayStepInfo.setDateList(monthDates);
        multiDayStepInfo.setFinishTargetCount(finishTargetCount);

        return multiDayStepInfo;
    }

    @Override
    protected DaySleepInfo getDaySleepInfo(long date) {
        List<SleepNodeDetail> list = DBHelper.getInstance().getSleepNodeDetailByDate(date);

        if (list.size() <= 1) {
            Sleep sleep = DBHelper.getInstance().getSleepDataByDateFromLocalDB(date);
            if (sleep == null) {
                return null;
            } else {
                DaySleepInfo daySleepInfo = new DaySleepInfo();
                daySleepInfo.setDate(date);
                daySleepInfo.setSleepTotalTime(sleep.getTotalTime());
                daySleepInfo.setDeepTime(sleep.getDeep());
                daySleepInfo.setLightTime(sleep.getLight());
                daySleepInfo.setAwakeCount(sleep.getWakeCount());

                return daySleepInfo;
            }
        } else {
            list = DataHandler.filterSleepData(list);
            if (list.size() <= 1) {
                return null;
            }


            DaySleepInfo daySleepInfo = new DaySleepInfo();
            daySleepInfo.setDate(date);
            int totalTime = 0;
            int awakeCount = 0;
            int deepTime = 0;
            int lightTime = 0;
            int[] statusArray = new int[list.size() - 1];
            int[] timePointArray = new int[list.size() - 1];
            int[] durationTimeArray = new int[list.size() - 1];

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

//                statusArray[i - 1] = detail.getMode();
                switch (detail.getMode()) {
                    case Config.K9_AWAKE:
                        statusArray[i - 1] = DaySleepInfo.AWAKE;
                        break;
                    case Config.K9_DEEP:
                        statusArray[i - 1] = DaySleepInfo.DEEP;
                        break;
                    case Config.K9_LIGHT:
                        statusArray[i - 1] = DaySleepInfo.LIGHT;
                        break;
                }

                timePointArray[i - 1] = next.getBegin();
                durationTimeArray[i - 1] = nextBegin - detailBegin;
            }

            // filter the total time less than 30 minutes sleep data.
            if (totalTime <= 30) {
                return null;
            }

            daySleepInfo.setSleepTotalTime(totalTime);
            daySleepInfo.setAwakeCount(awakeCount);
            daySleepInfo.setDeepTime(deepTime);
            daySleepInfo.setLightTime(lightTime);
            daySleepInfo.setStatusArray(statusArray);
            daySleepInfo.setTimeOfStatus(timePointArray);
            daySleepInfo.setDurationTimeArray(durationTimeArray);

            return daySleepInfo;

        }
    }


    @Override
    protected MultiDaySleepInfo getMonthSleepData(long date) {
        List<Date> monthDates = DateUtils.getEveryDateOfMonth(date);
        List<Sleep> sleeps = DBHelper.getInstance().getMonthSleep(date);
        if (sleeps.isEmpty()) {
            return null;
        } else {
            MultiDaySleepInfo info = new MultiDaySleepInfo();
            info.setDateList(monthDates);

            long totalSleep = 0L;
            int totalDeepSleep = 0;
            int totalLightSleep = 0;
            int totalAwake = 0;

            int i = 0;
            for (Sleep sleep : sleeps) {
                totalSleep += sleep.getTotalTime();
                totalDeepSleep += sleep.getDeep();
                totalLightSleep += sleep.getLight();
                totalAwake += sleep.getWakeCount();
                if (sleep.getTotalTime() > 0) {
                    i++;
                }
            }
            i = (i <= 0 ? 1 : i);

            info.setAveSleep((int) (totalSleep / i));
            info.setAveDeep((totalDeepSleep / i));
            info.setAveLight(totalLightSleep / i);
            info.setAveWake(totalAwake / i);
            info.setDaySleepInfoList(sleeps);

            return info;
        }
    }

    @Override
    protected MultiDaySleepInfo getWeekSleepData(long date) {
        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(date);
        List<Sleep> sleeps = DBHelper.getInstance().getWeekSleep(date);
        if (sleeps.isEmpty()) {
            Logger.d(TAG, "week sleep is empty");
            return null;
        } else {
            for (int i = 0; i < sleeps.size(); i++) {
                Sleep sleep = sleeps.get(i);
                Logger.d(TAG, "sleep time >>>>> " + sleep.getTotalTime());
            }

            MultiDaySleepInfo info = new MultiDaySleepInfo();
            info.setDateList(weekDates);

            long totalSleep = 0L;
            int totalDeepSleep = 0;
            int totalLightSleep = 0;
            int totalAwake = 0;

            int i = 0;
            for (Sleep sleep : sleeps) {
                totalSleep += sleep.getTotalTime();
                totalDeepSleep += sleep.getDeep();
                totalLightSleep += sleep.getLight();
                totalAwake += sleep.getWakeCount();
                if (sleep.getTotalTime() > 0) {
                    i++;
                }
            }

            i = (i <= 0 ? 1 : i);

            info.setAveSleep((int) (totalSleep / i));
            info.setAveDeep((totalDeepSleep / i));
            info.setAveLight(totalLightSleep / i);
            info.setAveWake(totalAwake / i);
            info.setDaySleepInfoList(sleeps);

            return info;
        }
    }

    @Override
    protected List<SingleRate> getDayRateDetail(long date) {
        HeartRate rate = DBHelper.getInstance().getDayHeartRateFromLocalDB(date);

        List<SingleRate> rateInfoList = new ArrayList<>();
        if (rate != null) {
            String json = rate.getHeartRate();

            Type listType = new TypeToken<List<SingleRate>>() {
            }.getType();
            rateInfoList = new Gson().fromJson(json, listType);
        }

        for (SingleRate singleRate : rateInfoList) {
            if (singleRate.getTime() > 1440) {
                singleRate.setTime(DateUtils.getMinuteOfDay(singleRate.getTime()));
            }
        }

        return rateInfoList;
    }

    @Override
    protected List<SingleBloodPressure> getDayBloodDetail(long date) {
        BloodPressure pressure = DBHelper.getInstance().queryDayBloodPressureFromDB(date);

        List<SingleBloodPressure> pressureInfoList = new ArrayList<>();
        if (null != pressure) {
            String json = pressure.getBloodPressures();

            Type listType = new TypeToken<List<SingleBloodPressure>>() {
            }.getType();
            pressureInfoList = new Gson().fromJson(json, listType);
        }

        return pressureInfoList;
    }

    @Override
    protected List<BloodPressure> getWeekBloodData(long endDate) {
        return DBHelper.getInstance().getWeekBloodPressureData(endDate);
    }

    @Override
    protected Step getTodaySportData() {
        HistoryData data = DBHelper.getInstance().queryDayTotalStepData(DateUtils.getToday());
        long steps = 0;
        float calories = 0;
        float distance = 0.0f;
        if (data != null) {
            steps = data.getStep();
            calories = (int) data.getCalories();
            distance = data.getDistance();
        }

        return new Step(steps, calories, distance);
    }

    @Override
    protected MultiDayStepInfo getWeekHistoryData(long date) {
        MultiDayStepInfo multiDayStepInfo = new MultiDayStepInfo();

        List<HistoryData> weekHistoryData = DBHelper.getInstance().getWeekHistoryData(date);
        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(date);

        int unit = DataManager.getInstance().getUnitSystem();

        long totalStep = 0L;
        float totalCalories = 0.0f;
        float totalDistance = 0.0f;
        int finishTargetCount = 0;
        int i = 0;
        for (HistoryData data : weekHistoryData) {
            totalStep += data.getStep();
            totalCalories += data.getCalories();
            totalDistance += data.getDistance();
            finishTargetCount += data.getTargetFinish() ? 1 : 0;
            if (data.getStep() > 0) {
                i++;
            }
        }
        i = (i <= 0 ? 1 : i);

        float aveCalories = totalCalories / i;
        float aveDistance = totalDistance / i;
        long aveStep = totalStep / i;

        multiDayStepInfo.setUnit(unit);
        multiDayStepInfo.setAveCalories(aveCalories);
        multiDayStepInfo.setAveDistance(aveDistance);
        multiDayStepInfo.setAveSteps(aveStep);
        multiDayStepInfo.setDayStepInfoList(weekHistoryData);
        multiDayStepInfo.setTotalSteps(totalStep);
        multiDayStepInfo.setDateList(weekDates);
        multiDayStepInfo.setFinishTargetCount(finishTargetCount);

        return multiDayStepInfo;
    }

    @Override
    public List<RateOneDayBean> getSevenDayHeartRateDetail(long today) {
        return DBHelper.getInstance().getSevenDayHeartRateDetailForK9(today);
    }

    @Override
    public LastRate getLastHeartRateData() {
        return DBHelper.getInstance().getLastHeartRate();
    }

    @Override
    public LastSleepData getLastSleepData() {
        long lastDate = DBHelper.getInstance().getLastSleepDate();
        Sleep sleep = DBHelper.getInstance().getSleepDataByDateFromLocalDB(lastDate);
        if (lastDate == 0L || sleep == null) {
            return null;
        } else {
            int totalTime = sleep.getTotalTime();
            return new LastSleepData(lastDate, totalTime, getDaySleepDetail(lastDate));
        }
    }

    @Override
    public List<DBWeight> getLastSevenDayWeight() {
        return DBHelper.getInstance().getLastSevenDayWeight();
    }

    @Override
    public void insertWeight(long date, float weight, int unit) {
        DBHelper.getInstance().insertWeight(date, weight);
    }

    @Override
    public User getUser() {
        return DBHelper.getInstance().getUser();
    }

    private DaySleepInfo getDaySleepDetail(long date) {
        List<SleepNodeDetail> list = DBHelper.getInstance().getSleepNodeDetailByDate(date);

        if (list.size() <= 1) {
            return null;
        } else {
            list = DataHandler.filterSleepData(list);
            if (list.size() <= 1) {
                return null;
            }


            DaySleepInfo daySleepInfo = new DaySleepInfo();
            daySleepInfo.setDate(date);
            int totalTime = 0;
            int awakeCount = 0;
            int deepTime = 0;
            int lightTime = 0;
            int[] statusArray = new int[list.size() - 1];
            int[] timePointArray = new int[list.size() - 1];
            int[] durationTimeArray = new int[list.size() - 1];

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

//                statusArray[i - 1] = detail.getMode();
                switch (detail.getMode()) {
                    case Config.K9_AWAKE:
                        statusArray[i - 1] = DaySleepInfo.AWAKE;
                        break;
                    case Config.K9_DEEP:
                        statusArray[i - 1] = DaySleepInfo.DEEP;
                        break;
                    case Config.K9_LIGHT:
                        statusArray[i - 1] = DaySleepInfo.LIGHT;
                        break;
                }

                timePointArray[i - 1] = next.getBegin();
                durationTimeArray[i - 1] = nextBegin - detailBegin;
            }

            // filter the total time less than 30 minutes sleep data.
            if (totalTime <= 30) {
                return null;
            }

            daySleepInfo.setSleepTotalTime(totalTime);
            daySleepInfo.setAwakeCount(awakeCount);
            daySleepInfo.setDeepTime(deepTime);
            daySleepInfo.setLightTime(lightTime);
            daySleepInfo.setStatusArray(statusArray);
            daySleepInfo.setTimeOfStatus(timePointArray);
            daySleepInfo.setDurationTimeArray(durationTimeArray);

            return daySleepInfo;

        }
    }

    public boolean isSupportBloodPressure() {
        return PreferencesHelper.getIsSupportBloodPressure();
    }

    public void setIsSupportBloodPressure(boolean isSupport) {
        PreferencesHelper.setIsSupportBloodPressure(isSupport);
    }

    public String getUserName() {
        return PreferencesHelper.getLatestUserName();
    }

    public boolean currentUserBloodDBEmpty() {
        return DBHelper.getInstance().currentUserDBEmpty();
    }

    public boolean getBloodMeasureStatus() {
        return PreferencesHelper.getBloodMeasureStatus();
    }

    public List<HistoryData> getUserStepData() {
        return DBHelper.getInstance().getUserStepData();
    }

    public void updateDayHighestSteps(int steps) {
        DBHelper.getInstance().updateDayHighestSteps(steps);
    }

    public double getTotalDistance() {
        return DBHelper.getInstance().getUser().getTotalDistance();
    }

    public int getTotalStep() {
        return DBHelper.getInstance().getUser().getTotalWalkCount();
    }

    public void updateMedal(String[] medal) {
        DBHelper.getInstance().updateMedal(medal);
    }

    public void setDeviceType(int type) {
        PreferencesHelper.setDeviceType(type);
    }

    @Override
    public void setLatestUserName(String name) {
        PreferencesHelper.setUserName(name);
    }

    @Override
    public void setLatestPassword(String password) {

    }

    public void insertUserLoginInfo(com.coband.cocoband.mvp.model.bean.User user) {
        DBHelper.getInstance().insertUserLoginInfo(user);
    }

    public void insertUserSignIn(com.coband.cocoband.mvp.model.bean.User user) {
        DBHelper.getInstance().insertUserSignIn(user);
    }

    public String getLatestPassword() {
        return PreferencesHelper.getLatestPassword();
    }

    @Override
    public String getLatestUserName() {
        return PreferencesHelper.getLatestUserName();
    }

    /***************************** CoBand *********************************/
    public void insertAccount(String account, String pwd, String token) {
        DBHelper.getInstance().insertAccount(account, pwd, token);
    }

    @Override
    public String getToken(String uid) {
        return DBHelper.getInstance().getToken(uid);
    }

    @Override
    public Account getAccount(String uid) {
        return DBHelper.getInstance().getAccount(uid);
    }

    @Override
    public void updateOrInsertAccount(Account account) {
        DBHelper.getInstance().updateOrInsertAccount(account);
    }

    @Override
    public void setCurrentUid(String uid) {
        PreferencesHelper.setCurrentUid(uid);
    }

    @Override
    public String getCurrentUid() {
        return PreferencesHelper.getCurrentId();
    }

    @Override
    public Account getCurrentAccount() {
        return DBHelper.getInstance().getCurrentAccount();
    }

    @Override
    public void updateAccountTarget(int stepTarget, int sleepTarget, double weightTarget) {
        DBHelper.getInstance().updateAccountTarget(stepTarget, sleepTarget, weightTarget);
    }

    @Override
    public long queryBloodPressureDBLatestDate() {
        return DBHelper.getInstance().queryBloodPressureDBLatestDate();
    }
}
