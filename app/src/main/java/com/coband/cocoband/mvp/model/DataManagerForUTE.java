package com.coband.cocoband.mvp.model;

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
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.HeartRate;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.Sleep;
import com.coband.watchassistant.User;
import com.yc.pedometer.info.SleepTimeInfo;
import com.yc.pedometer.info.StepOneDayAllInfo;
import com.yc.pedometer.info.StepOneHourInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ivan on 17-7-29.
 */

class DataManagerForUTE extends AbstractManager {
    private static final String TAG = "DataManagerForUTE";

    private static DataManagerForUTE mInstance;

    private DataManagerForUTE() {

    }

    public static DataManagerForUTE getInstance() {
        if (mInstance == null) {
            synchronized (DataManagerForUTE.class) {
                if (mInstance == null) {
                    mInstance = new DataManagerForUTE();
                }
            }
        }
        return mInstance;
    }


    @Override
    protected DayStepInfo getDayStepInfo(long date) {
        StepOneDayAllInfo info = DBHelper.getInstance().getDayStepData(date);
        HistoryData data = DBHelper.getInstance().queryDayTotalStepData(date);
        int unit = DataManager.getInstance().getUnitSystem();

        DayStepInfo dayStepInfo = new DayStepInfo();
        if (data == null) {
            dayStepInfo.setDate(date);
            dayStepInfo.setUnit(unit);
            return dayStepInfo;
        }

        double distance;
        double calories;
        long steps;

        if (date == DateUtils.getToday()) {
            distance = PreferencesHelper.getTodayDistance();
            calories = PreferencesHelper.getTodayCalories();
            steps = PreferencesHelper.getTodaySteps();
        } else {
            distance = data.getDistance();
            calories = data.getCalories();
            steps = data.getStep();
        }


        if (unit == Config.INCH) {
            distance = Utils.changeKmToMiles(distance);
        }

        dayStepInfo.setCalories(calories);
        dayStepInfo.setDistance(distance);
        dayStepInfo.setSteps(steps);
        dayStepInfo.setUnit(unit);

        Integer stepGoal = DBHelper.getInstance().getCurrentAccount().getStepTarget();
        int stepTarget = stepGoal == null ? Config.DEFAULT_STEP_TARGET : stepGoal;
        float progress = data.getStep() / (float) stepTarget;
        dayStepInfo.setProgress(progress > 1 ? 1 : progress);
        List<StepInfo> nodeInfo = new ArrayList<>();
        List<StepOneHourInfo> infos = info.getStepOneHourArrayInfo();
        for (StepOneHourInfo hourInfo : infos) {
            StepInfo stepInfo = new StepInfo();
            stepInfo.setSteps(hourInfo.getStep());
            stepInfo.setTimeNode(hourInfo.getTime() / 60);
            nodeInfo.add(stepInfo);
        }
        dayStepInfo.setNodeInfoList(nodeInfo);

        return dayStepInfo;
    }

    @Override
    protected MultiDayStepInfo getWeekHistoryData(long date) {
        MultiDayStepInfo multiDayStepInfo = new MultiDayStepInfo();

        List<HistoryData> weekHistoryData = DBHelper.getInstance().getWeekHistoryData(date);
        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(date);
        int unit = DataManager.getInstance().getUnitSystem();

        long totalStep = 0L;
        float totalCalories = 0;
        float totalDistance = 0.0f;
        int finishTargetCount = 0;
        int i = 0;
        for (HistoryData data : weekHistoryData) {
            totalStep += data.getStep();
            totalCalories += (int) data.getCalories();
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
        SleepTimeInfo info = DBHelper.getInstance().getDaySleepData(date);
        if (info == null) {
            return null;
        } else {
            DaySleepInfo daySleepInfo = new DaySleepInfo();
            daySleepInfo.setDate(date);
            daySleepInfo.setSleepTotalTime(info.getSleepTotalTime());
            daySleepInfo.setAwakeCount(info.getAwakeCount());
            daySleepInfo.setDeepTime(info.getDeepTime());
            daySleepInfo.setLightTime(info.getLightTime());
            daySleepInfo.setStatusArray(info.getSleepStatueArray());
            daySleepInfo.setTimeOfStatus(info.getTimePointArray());
            daySleepInfo.setDurationTimeArray(info.getDurationTimeArray());

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
    protected Step getTodaySportData() {
        long stepSavedDate = PreferencesHelper.getStepSaveDate();
        long steps;
        float calories;
        float distance;

        if (stepSavedDate == DateUtils.getToday() && PreferencesHelper.getCurrentId().equals(
                PreferencesHelper.getUidForSavedStep())) {
            steps = PreferencesHelper.getTodaySteps();
            calories = PreferencesHelper.getTodayCalories();
            distance = PreferencesHelper.getTodayDistance();
        } else {
            steps = 0L;
            calories = 0f;
            distance = 0f;
        }

        return new Step(steps, calories, distance);
    }

    @Override
    public List<RateOneDayBean> getSevenDayHeartRateDetail(long today) {
        return DBHelper.getInstance().getSevenDayHeartRateDetail(today);
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
            return new LastSleepData(lastDate, totalTime, getDaySleepInfo(lastDate));
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

    public boolean isSupportBloodPressure() {
        return PreferencesHelper.getIsSupportBloodPressure();
    }

    public void setIsSupportBloodPressure(boolean isSupport) {
        PreferencesHelper.setBloodMeasureStatus(isSupport);
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

    public void setLatestUserName(String name) {
        PreferencesHelper.setLatestUserName(name);
    }

    public void setLatestPassword(String password) {
        PreferencesHelper.setLatestPassword(password);
    }

    public void insertUserLoginInfo(com.coband.cocoband.mvp.model.bean.User user) {
        DBHelper.getInstance().insertUserLoginInfo(user);
    }

    public void insertUserSignIn(com.coband.cocoband.mvp.model.bean.User user) {
        DBHelper.getInstance().insertUserSignIn(user);
    }

    public String getLatestUserName() {
        return PreferencesHelper.getLatestUserName();
    }

    public String getLatestPassword() {
        return PreferencesHelper.getLatestPassword();
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
