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
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.User;

import java.util.List;

/**
 * Created by ivan on 17-4-10.
 */

public class DataManager {
    private static DataManager instance;
    private static final String TAG = "DataManager";
    private static AbstractManager mManager;

    private static int mDeviceType = -1;

    private DataManager() {

    }

    public static DataManager getInstance() {
        initDataManagerImpl();

        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }

        return instance;
    }

    private static void initDataManagerImpl() {
        int deviceType = PreferencesHelper.getDeviceType();
        if (deviceType == C.DEVICE_NONE) {
            mManager = DataManagerForK9.getInstance();
            return;
        }

        if (mDeviceType != deviceType) {
            mDeviceType = deviceType;
            if (deviceType == C.DEVICE_TYPE_K9) {
                mManager = DataManagerForK9.getInstance();
            } else {
                mManager = DataManagerForUTE.getInstance();
            }
        }
    }

    public DayStepInfo getDayStepInfo(long date) {
        return mManager.getDayStepInfo(date);
    }

    public MultiDayStepInfo getWeekHistoryData(long date) {
        return mManager.getWeekHistoryData(date);
    }

    public MultiDayStepInfo getMonthHistoryData(long date) {
        return mManager.getMonthHistoryData(date);
    }


    public DaySleepInfo getDaySleepInfo(long date) {
        return mManager.getDaySleepInfo(date);
    }

    public MultiDaySleepInfo getMonthSleepData(long date) {
        return mManager.getMonthSleepData(date);
    }

    public MultiDaySleepInfo getWeekSleepData(long date) {
        return mManager.getWeekSleepData(date);
    }

    public List<SingleRate> getDayRateDetail(long date) {
        return mManager.getDayRateDetail(date);
    }

    public Step getTodaySportData() {
        return mManager.getTodaySportData();
    }

    public List<RateOneDayBean> getSevenDayHeartRateDetail(long today) {
        return mManager.getSevenDayHeartRateDetail(today);
    }

    public List<SingleBloodPressure> getDayBloodDetail(long date) {
        return mManager.getDayBloodDetail(date);
    }

    public List<BloodPressure> getWeekBloodData(long endDate) {
        return mManager.getWeekBloodData(endDate);
    }

    public LastRate getLastHeartRateData() {
        return mManager.getLastHeartRateData();
    }

    public LastSleepData getLastSleepData() {
        return mManager.getLastSleepData();
    }

    public List<DBWeight> getLastSevenDayWeight() {
        return DBHelper.getInstance().getLastSevenDayWeight();
    }

    public void insertWeight(long date, float weight) {
        DBHelper.getInstance().insertWeight(date, weight);
    }

    public User getUser() {
        return mManager.getUser();
    }

    public String getUserName() {
        return mManager.getLatestUserName();
    }

    public boolean currentUserBloodDBEmpty() {
        return mManager.currentUserBloodDBEmpty();
    }

    public boolean getBloodMeasureStatus() {
        return mManager.getBloodMeasureStatus();
    }

    public List<HistoryData> getUserStepData() {
        return mManager.getUserStepData();
    }

    public void updateDayHighestSteps(int steps) {
        mManager.updateDayHighestSteps(steps);
    }

    public double getTotalDistance() {
        return mManager.getTotalDistance();
    }

    public int getTotalStep() {
        return mManager.getTotalStep();
    }

    public void updateMedal(String[] medal) {
        mManager.updateMedal(medal);
    }

    public void setDeviceType(int type) {
        mManager.setDeviceType(type);
    }

    public void setLatestUserName(String name) {
        mManager.setLatestUserName(name);
    }

    public String getLatestPassword() {
        return mManager.getLatestPassword();
    }

    public void setLatestPassword(String password) {
        mManager.setLatestPassword(password);
    }

    public void insertUserLoginInfo(com.coband.cocoband.mvp.model.bean.User user) {
        mManager.insertUserLoginInfo(user);
    }

    public void insertUserSignIn(com.coband.cocoband.mvp.model.bean.User user) {
        mManager.insertUserSignIn(user);
    }

    public String getLatestUserName() {
        return mManager.getLatestUserName();
    }

    /****************************** CoBand *****************************/
    public String getToken(String uid) {
        return DBHelper.getInstance().getToken(uid);
    }

    public Account getAccount(String uid) {
        return DBHelper.getInstance().getAccount(uid);
    }

    public void updateOrInsertAccount(Account account) {
        DBHelper.getInstance().updateOrInsertAccount(account);
    }

    public void updateOrInsertAccount(LogInResponse response) {
        Account account = handleLogInResponse(response);
        updateOrInsertAccount(account);
    }

    public Account handleLogInResponse(LogInResponse response) {
        LogInResponse.PayloadBean bean = response.getPayload();

        Account a = new Account();

        String token = bean.getToken();
        String did = bean.getUser().getDid();
        String uid = bean.getUser().getUid();

        int height = 0;
        double weight = 0, latitude = 0, longitude = 0;
        String avatarPath = null, avatarMD5 = null, birthday = null, bloodType = null, city = null,
                country = null, gender = null, nickName = null, province = null, unitSystem = null,
                bgPath = null, bgMD5 = null;

        if (bean.getUser().getPersonalInfo() != null) {
            birthday = bean.getUser().getPersonalInfo().getBirthday();
            bloodType = bean.getUser().getPersonalInfo().getBloodType();
            city = bean.getUser().getPersonalInfo().getCity();
            country = bean.getUser().getPersonalInfo().getCountry();
            gender = bean.getUser().getPersonalInfo().getGender();
            nickName = bean.getUser().getPersonalInfo().getNickname();
            province = bean.getUser().getPersonalInfo().getProvince();
            height = bean.getUser().getPersonalInfo().getHeight();
            weight = bean.getUser().getPersonalInfo().getWeight();
            unitSystem = bean.getUser().getPersonalInfo().getUnitSystem();


            if (bean.getUser().getPersonalInfo().getLocation() != null) {
                latitude = bean.getUser().getPersonalInfo().getLocation().getLatitude();
                longitude = bean.getUser().getPersonalInfo().getLocation().getLongitude();
            }


            if (bean.getUser().getPersonalInfo().getAvatar() != null) {
                avatarPath = bean.getUser().getPersonalInfo().getAvatar().getPath();
                avatarMD5 = bean.getUser().getPersonalInfo().getAvatar().getMd5();
            }

            if (bean.getUser().getPersonalInfo().getBackground() != null) {
                bgPath = bean.getUser().getPersonalInfo().getBackground().getPath();
                bgMD5 = bean.getUser().getPersonalInfo().getBackground().getMd5();
            }
        }


        int timezone = -1;
        String language = null, osType = null, osVersion = null, phoneModel = null;
        if (bean.getUser().getPhoneInfo() != null) {
            timezone = bean.getUser().getPhoneInfo().getTimezone();
            language = bean.getUser().getPhoneInfo().getLanguage();
            osType = bean.getUser().getPhoneInfo().getOsType();
            osVersion = bean.getUser().getPhoneInfo().getOsVersion();
            phoneModel = bean.getUser().getPhoneInfo().getPhoneModel();
        }

        int totalExerciseDays = 0;
        long maxDaySteps = 0, totalSteps = 0, startExerciseTime = 0;
        double totalDistance = 0, totalCalories = 0;
        List<String> achievements = null;
        if (bean.getUser().getSportSummary() != null) {
            achievements = bean.getUser().getSportSummary().getAchievements();
            maxDaySteps = bean.getUser().getSportSummary().getMaxDaySteps();
            totalSteps = bean.getUser().getSportSummary().getTotalSteps();
            startExerciseTime = bean.getUser().getSportSummary().getStartExerciseTime();
            totalCalories = bean.getUser().getSportSummary().getTotalCalories();
            totalDistance = bean.getUser().getSportSummary().getTotalDistance();
            totalExerciseDays = bean.getUser().getSportSummary().getTotalExerciseDays();
        }
        int sleepTarget = 0, stepTarget = 0;
        double weightTarget = 0;
        if (bean.getUser().getSportTarget() != null) {
            sleepTarget = bean.getUser().getSportTarget().getSleepTarget();
            stepTarget = bean.getUser().getSportTarget().getStepTarget();
            weightTarget = bean.getUser().getSportTarget().getWeightTarget();
        }

        a.setToken(token);
        a.setUid(uid);

        if (did != null) {
            a.setDid(did);
        }

        if (avatarPath != null) {
            a.setAvatar(avatarPath);
        }

        if (avatarMD5 != null) {
            a.setAvatarMD5(avatarMD5);
        }

        if (bgPath != null) {
            a.setBackground(bgPath);
        }

        if (bgMD5 != null) {
            a.setBackgroundMD5(bgMD5);
        }

        if (birthday != null) {
            a.setBirthday(birthday);
        }

        if (bloodType != null) {
            a.setBloodType(bloodType);
        }

        if (city != null) {
            a.setCity(city);
        }

        if (country != null) {
            a.setCountry(country);
        }

        if (gender != null) {
            a.setGender(gender);
        }

        if (unitSystem != null) {
            a.setUnitSystem(unitSystem);
        }

        if (nickName != null) {
            a.setNickname(nickName);
        }

        if (province != null) {
            a.setProvince(province);
        }

        if (height != 0) {
            a.setHeight(height);
        }

        if (latitude != 0) {
            a.setLatitude(latitude);
        }

        if (longitude != 0) {
            a.setLongitude(longitude);
        }

        if (weight != 0) {
            a.setWeight(weight);
        }

        if (timezone != -1) {
            a.setTimezone(timezone);
        }

        if (language != null) {
            a.setLanguage(language);
        }

        if (osType != null) {
            a.setOsType(osType);
        }

        if (osVersion != null) {
            a.setOsVersion(osVersion);
        }

        if (phoneModel != null) {
            a.setPhoneModel(phoneModel);
        }

        if (achievements != null && !achievements.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String achievement : achievements) {
                builder.append(achievement + ",");
            }
            a.setAchievements(builder.toString());
        }

        if (maxDaySteps != 0) {
            a.setMaxDaySteps(maxDaySteps);
        }

        if (totalSteps != 0) {
            a.setTotalSteps(totalSteps);
        }

        if (totalCalories != 0) {
            a.setTotalCalories(totalCalories);
        }

        if (totalDistance != 0) {
            a.setTotalDistance(totalDistance);
        }

        if (totalExerciseDays != 0) {
            a.setTotalExerciseDays(totalExerciseDays);
        }

        if (startExerciseTime != 0) {
            a.setStartExerciseTime(startExerciseTime);
        }

        if (sleepTarget != 0) {
            a.setSleepTarget(sleepTarget);
        }

        if (stepTarget != 0) {
            a.setStepTarget(stepTarget);
        }

        if (weightTarget != 0) {
            a.setWeightTarget(weightTarget);
        }

        return a;
    }


    public void setCurrentUid(String uid) {
        PreferencesHelper.setCurrentUid(uid);
    }

    public String getCurrentUid() {
        return PreferencesHelper.getCurrentId();
    }

    public Account getCurrentAccount() {
        return DBHelper.getInstance().getCurrentAccount();
    }

    public int getUnitSystem() {
        Account account = mManager.getCurrentAccount();
        String unitSystem = account.getUnitSystem();
        return Config.BRITISH_STRING.equals(unitSystem) ? Config.INCH : Config.METRIC;
    }

    public void updateAccountTarget(int stepTarget, int sleepTarget, double weightTarget) {
        mManager.updateAccountTarget(stepTarget, sleepTarget, weightTarget);
    }

    public long queryBloodPressureDBLatestDate() {
        return mManager.queryBloodPressureDBLatestDate();
    }

    public void updateNickname(String nickname) {
        DBHelper.getInstance().updateNickname(nickname);
    }

    public void updateWeight(double weight) {
        DBHelper.getInstance().updateAccountWeight(weight);
    }

    public void insertWeight(long date, double weight) {
        DBHelper.getInstance().insertWeight(date, weight);
    }

    public void updateGender(String gender) {
        DBHelper.getInstance().updateGender(gender);
    }

    public void updateHeight(int height) {
        DBHelper.getInstance().updateAccountHeight(height);
    }

    public void updateBirthday(String birthday) {
        DBHelper.getInstance().updateAccountBirthday(birthday);
    }

    public void updateAvatar(String avatarPath, String avatarMD5) {
        DBHelper.getInstance().updateAccountAvatar(avatarPath, avatarMD5);
    }

    public void updateBackground(String bgPath, String bgMD5) {
        DBHelper.getInstance().updateAccountBackground(bgPath, bgMD5);
    }

    public void updateUnitSystem(String unitSystem) {
        DBHelper.getInstance().updateAccountUnitSystem(unitSystem);
    }

    public void updateAccountInfo(UpdateAccountInfo info) {
        DBHelper.getInstance().updateAccountInfo(info);
    }

    public void setDayDataUploaded(long date) {
        DBHelper.getInstance().setDayDataUploaded(date);
    }
}
