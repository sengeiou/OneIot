package com.coband.cocoband.mvp.model;

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
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.User;

import java.util.List;

/**
 * Created by ivan on 17-7-29.
 */

public abstract class AbstractManager {
    protected abstract DayStepInfo getDayStepInfo(long date);

    protected abstract MultiDayStepInfo getWeekHistoryData(long date);

    protected abstract MultiDayStepInfo getMonthHistoryData(long date);

    protected abstract DaySleepInfo getDaySleepInfo(long date);

    protected abstract MultiDaySleepInfo getMonthSleepData(long date);

    protected abstract MultiDaySleepInfo getWeekSleepData(long date);

    protected abstract List<SingleRate> getDayRateDetail(long date);

    protected abstract Step getTodaySportData();

    public abstract List<RateOneDayBean> getSevenDayHeartRateDetail(long today);

    protected abstract List<SingleBloodPressure> getDayBloodDetail(long date);

    protected abstract List<BloodPressure> getWeekBloodData(long endDate);

    public abstract LastRate getLastHeartRateData();

    public abstract LastSleepData getLastSleepData();

    public abstract List<DBWeight> getLastSevenDayWeight();

    public abstract void insertWeight(long date, float weight, int unit);

    public abstract User getUser();

    public abstract boolean isSupportBloodPressure();

    public abstract void setIsSupportBloodPressure(boolean isSupport);

    public abstract boolean currentUserBloodDBEmpty();

    public abstract boolean getBloodMeasureStatus();

    public abstract List<HistoryData> getUserStepData();

    public abstract void updateDayHighestSteps(int steps);

    public abstract double getTotalDistance();

    public abstract int getTotalStep();

    public abstract void updateMedal(String[] medal);

    public abstract void setDeviceType(int type);

    public abstract void setLatestUserName(String name);

    public abstract void setLatestPassword(String password);

    public abstract void insertUserLoginInfo(com.coband.cocoband.mvp.model.bean.User user);

    public abstract void insertUserSignIn(com.coband.cocoband.mvp.model.bean.User user);

    public abstract String getLatestUserName();

    public abstract void insertAccount(String account, String pwd, String token);

    public abstract String getToken(String uid);

    public abstract Account getAccount(String uid);

    public abstract String getLatestPassword();

    public abstract void updateOrInsertAccount(Account account);

    public abstract void setCurrentUid(String uid);

    public abstract String getCurrentUid();

    public abstract Account getCurrentAccount();

    public abstract void updateAccountTarget(int stepTarget, int sleepTarget, double weightTarget);

    public abstract long queryBloodPressureDBLatestDate();
}
