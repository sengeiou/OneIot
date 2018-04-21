package com.coband.cocoband.mvp.model.local.db;

import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coband.cocoband.mvp.model.bean.AvatarBean;
import com.coband.cocoband.mvp.model.bean.ConvertUnitBean;
import com.coband.cocoband.mvp.model.bean.FollowerBean;
import com.coband.cocoband.mvp.model.bean.RateOneDayBean;
import com.coband.cocoband.mvp.model.bean.SurfaceImgBean;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.Followees;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.Followers;
import com.coband.cocoband.mvp.model.bean.message.ConversationBean;
import com.coband.cocoband.mvp.model.entity.LastRate;
import com.coband.cocoband.mvp.model.entity.SingleRate;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.Utils;
import com.coband.interactivelayer.bean.SportItemPacket;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.Conversation;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.Followee;
import com.coband.watchassistant.Follower;
import com.coband.watchassistant.HeartRate;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.Sleep;
import com.coband.watchassistant.SleepNodeDetail;
import com.coband.watchassistant.StepNodeDetail;
import com.coband.watchassistant.User;
import com.yc.pedometer.info.RateOneDayInfo;
import com.yc.pedometer.info.SleepTimeInfo;
import com.yc.pedometer.info.StepOneDayAllInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ivan on 17-4-10.
 * all database operation need to access by this class.
 */

public class DBHelper {
    public static final String TAG = "DBHelper";
    private static DBHelper instance;

    private DBHelper() {

    }

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }

        return instance;
    }

    public boolean isPackageEnabledPush(String packageName) {
        return PushAppDBService.isPackageEnabledPush(packageName);
    }

    public String getAppNameByPackage(String packageName) {
        return PushAppDBService.getAppName(packageName);
    }

    public void addRemindApp(String packageName, String appName) {
        PushAppDBService.addRemindPackage(packageName, appName);
    }

    public void removeRemindApp(String packageName) {
        PushAppDBService.removeRemindPackage(packageName);
    }

    public boolean insertUser(com.coband.cocoband.mvp.model.bean.User user) {
        return UserDBService.insertUser(user);
    }

    public User getUser() {
        return UserDBService.getUser();
    }

    public User getUserByObjectId(String objectId) {
        return UserDBService.getUserByObjectId(objectId);
    }

    /**
     * @param date which date.
     * @return step list split by time node.
     */
    public StepOneDayAllInfo getDayStepData(long date) {
        return StepDBService.queryDayNodeStep(date);
    }

    public HistoryData queryDayTotalStepData(long date) {
        return StepDBService.queryDayStepData(date);
    }

    /**
     * @param date the date in week;
     * @return
     */
    public List<HistoryData> getWeekHistoryData(long date) {

        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(date);
        for (Date date1 : weekDates) {
            Logger.d(TAG, "date >>>>>>" + DateUtils.getDateByMillisecondsLoacale("yyyy-MM-dd", date1.getTime()));
        }
        long from = weekDates.get(0).getTime() / 1000;
        long to = weekDates.get(weekDates.size() - 1).getTime() / 1000;
        return getMultiDayHistoryData(from, to);
    }

    private List<HistoryData> getMultiDayHistoryData(long from, long to) {
        List<HistoryData> historyDataList = new ArrayList<>();
        while (from <= to) {
            HistoryData data = StepDBService.queryHistoryStepData(from);
            if (data == null) {
                data = new HistoryData();
                data.setDate(from);
                data.setDistance(0f);
                data.setCalories(0f);
                data.setStep(0L);
                data.setTargetFinish(false);
                data.setTime(0L);
            }

            historyDataList.add(data);
            from += C.ONE_DAY_SECONDS;
        }

        return historyDataList;
    }

    /**
     * @param date the date in month;
     * @return
     */
    public List<HistoryData> getMonthHistoryData(long date) {
        List<Date> monthDates = DateUtils.getEveryDateOfMonth(date);
        long from = monthDates.get(0).getTime() / 1000;
        Logger.d(this, "date size >>>>>> " + monthDates.size());
        long to = monthDates.get(monthDates.size() - 1).getTime() / 1000;
        return getMultiDayHistoryData(from, to);
    }

    /**
     * @param date which date.
     * @return
     */
    public SleepTimeInfo getDaySleepData(long date) {
        return SleepDBService.queryDaySleepFromUteDB(date);
    }


    public List<SleepTimeInfo> getWeekSleepData(long date) {
        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(date);
        long from = weekDates.get(0).getTime();
        long to = weekDates.get(weekDates.size() - 1).getTime();
        return SleepDBService.queryMultiSleepDetailDataFromUteDB(from, to);
    }

    public HeartRate getDayHeartRateFromLocalDB(long date) {
        return HeartRateDBService.queryDayHeartRateFromDB(date);
    }

    public List<RateOneDayBean> getSevenDayHeartRateDetail(long today) {
        List<RateOneDayBean> wrapList = new ArrayList<>();
        int count = 0;
        while (count < 7) {
            long date = today - count * C.ONE_DAY_SECONDS;
            List<RateOneDayInfo> dayDatas = HeartRateDBService.queryDayHeartRateDetail(date);
            Collections.reverse(dayDatas);
            for (RateOneDayInfo info : dayDatas) {
                RateOneDayBean bean = new RateOneDayBean(info.getTime(), info.getRate(), date);
                wrapList.add(bean);
            }

            count++;
        }

        return wrapList;
    }

    public List<RateOneDayBean> getSevenDayHeartRateDetailForK9(long today) {
        Gson gson = new Gson();
        List<RateOneDayBean> wrapList = new ArrayList<>();
        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(today);
        long from = weekDates.get(0).getTime() / 1000;
        long to = weekDates.get(weekDates.size() - 1).getTime() / 1000;
        List<HeartRate> dayDatas = HeartRateDBService.queryMultiDayHeartRate(from, to);
        for (HeartRate info : dayDatas) {
            String json = info.getHeartRate();
            if (json == null) {
                continue;
            }

            List<SingleRate> rateInfoList;
            Type listType = new TypeToken<List<SingleRate>>() {
            }.getType();
            rateInfoList = gson.fromJson(json, listType);
            Collections.reverse(rateInfoList);

            for (SingleRate rate : rateInfoList) {
                if (rate.getTime() > 1440) {
                    rate.setTime(DateUtils.getMinuteOfDay(rate.getTime()));
                }

                RateOneDayBean bean = new RateOneDayBean((int) rate.getTime(), rate.getRate(), info.getDate());
                wrapList.add(bean);
            }
        }


        return wrapList;
    }

    public void copyUteTotalStepToHistoryDB() {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) {
                StepDBService.copyUteStepToHistoryDB();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void copyUteTotalSleepToSleepDB() {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) {
                SleepDBService.copyUteSleepDataToLocalDB();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void copyUteRateToHeartRateDB() {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                HeartRateDBService.copyUteHeartRateToLocalDB();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    public List<Sleep> getWeekSleep(long date) {
        List<Date> weekDate = DateUtils.getWeekOfDateBySeconds(date);
        long from = weekDate.get(0).getTime() / 1000;
        long to = weekDate.get(weekDate.size() - 1).getTime() / 1000;
        return SleepDBService.queryMultiSleepDataFromLocalDB(from, to);
    }

    public List<Sleep> getMonthSleep(long date) {
        List<Date> monthDate = DateUtils.getEveryDateOfMonth(date);
        long from = monthDate.get(0).getTime() / 1000;
        long to = monthDate.get(monthDate.size() - 1).getTime() / 1000;
        return SleepDBService.queryMultiSleepDataFromLocalDB(from, to);
    }


    public void setSportInfoUploaded(long date) {
        StepDBService.setStepUploaded(date);
    }


    public void insertUserReg(com.coband.cocoband.mvp.model.bean.User user) {
        UserDBService.insertUserReg(user);
    }

    public void insertUserSignIn(com.coband.cocoband.mvp.model.bean.User user) {
        UserDBService.insertUserSignIn(user);
    }

    public void insertUserLoginInfo(com.coband.cocoband.mvp.model.bean.User user) {
        UserDBService.insertUserInfo(user);
    }

    public String getObjID(String username) {
//        return UserDBService.getObjID(username);
        return PreferencesHelper.getLatestUserName();
    }

    public String getSession(String username) {
        return UserDBService.getSession(username);
    }

    public void updateUnit(int unit) {
        UserDBService.updateUnit(unit);
    }

    public void updateUnitNoUpdateTime(int unit) {
        UserDBService.updateUnitNoUpdateTime(unit);
    }

    public void updateUnit(ConvertUnitBean bean) {
        UserDBService.updateUnit(bean);
    }


    public void updateDayHighestSteps(int steps) {
        UserDBService.updateDayHighestSteps(steps);
    }

    /***** Follower *****/
    public void insertFollowers(List<Followers> followers) {
        FollowerDBService.insertFollowers(followers, getUser());
    }

    public void insertFollwer(com.coband.cocoband.mvp.model.bean.User follower) {
        FollowerDBService.insertFollower(follower, getUser());
    }

    public Follower getFollower(String followerId) {
        return FollowerDBService.queryFollower(followerId);
    }

    /***** Followee *****/
    public void insertFollowees(List<Followees> followees) {
        FolloweeDBService.insertFollowees(followees, getUser());
    }

    public List<Followee> getFollowees() {
        return FolloweeDBService.queryFollowee(getUser());
    }

    public void setSleepInfoUploaded(long date) {
        SleepDBService.setSleepUploaded(date);
    }

    public void setWeightUploaded(long date) {
        WeightDBService.setWeightUploaded(date);
    }

    public void setHeartRateUploaded(long date) {
        HeartRateDBService.setHeartRateUploaded(date);
    }

    public DBWeight getDayWeight(long date) {
        return WeightDBService.getDayWeight(date);
    }

    public List<DBWeight> getMultiDayWeight(long from, long to) {
        return WeightDBService.getMultiWeight(from, to);
    }

    public void insertSportInfoToDB(List<HistoryData> dataList) {
        StepDBService.insertHistoryDataToDB(dataList);
    }

    public void insertSleepInfoToDB(List<Sleep> sleeps) {
        if (sleeps.isEmpty()) {
            return;
        }

        SleepDBService.insertSleepList(sleeps);
    }

    public void insertWeightInfoToDB(List<DBWeight> weights) {
        if (weights.isEmpty()) {
            return;
        }
        WeightDBService.insertWeights(weights);
    }

    public List<DBWeight> getWeekWeight(long from, long to) {
        return WeightDBService.getMultiWeight(from, to);
    }

    public void insertHeartRate(final List<HeartRate> heartRates) {
        if (heartRates.isEmpty()) {
            return;
        }
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                HeartRateDBService.insertHeartRates(heartRates);
                e.onComplete();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    public void insertHeartRate(final long date, final long time, final int heartRateValue) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                HeartRateDBService.insertHeartRate(date, time, heartRateValue);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    public List<Follower> getFollowers() {
        return FollowerDBService.queryFollowers(getUser());
    }


    public void updateTarget(com.coband.cocoband.mvp.model.bean.User user) {
        UserDBService.updateTarget(user);
    }

    public void updateAvatarUrl(AvatarBean bean) {
        UserDBService.updateAvatarUrl(bean);
    }

    public void updateSurfaceImgUrl(SurfaceImgBean bean) {
        UserDBService.updateSurfaceImgUrl(bean);
    }


    public void updateUserInfoInSetting(User user) {
        UserDBService.updateUserInfoInSetting(user);
    }

    /**
     * message
     **/
    public void saveConversations(ArrayList<Conversation> list) {
        ConversationDBService.insertConversations(list);
    }

    public List<Conversation> getConversation() {
        List<Conversation> conversations = ConversationDBService.queryConversation();
        return conversations;
    }

    public Conversation getConversationById(String objectId) {
        return ConversationDBService.queryConversationById(objectId);
    }

    public void saveConversation(String conversationId, Conversation bean) {
        ConversationDBService.insertConversation(conversationId, bean);
    }

    public boolean isStepDBEmpty() {
        return StepDBService.isStepDBEmpty();
    }

    public ArrayList<Conversation> saveConversationFromNetWork(ArrayList<ConversationBean> resultList) {
        ArrayList<Conversation> conversationList = new ArrayList<Conversation>();

        String userId = DBHelper.getInstance().getUser().getObjectId();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < resultList.size(); i++) {
            ConversationBean conversation = resultList.get(i);
            List<String> m = conversation.m;
            // 后台有些对话的成员的只有一个或者不是objectId而是一张图片的url,这里过滤下
            if (m.size() >= 2) {
                String s = m.get(0);
                if (s.equals(userId)) {
                    if (m.get(1).contains("http")) continue;
                    conversationList.add(beanToDao(conversation));

                    if (DBHelper.getInstance().getUserByObjectId(m.get(1)) == null) {
                        buffer.append("{\"objectId\":");
                        buffer.append("\"" + m.get(1) + "\"}");
                        if (i != resultList.size() - 1) buffer.append(",");
                    }
                } else {
                    if (s.isEmpty() || s.contains("http")) continue;
                    conversationList.add(beanToDao(conversation));

                    if (DBHelper.getInstance().getUserByObjectId(s) == null) {

                        buffer.append("{\"objectId\":");
                        buffer.append("\"" + s + "\"}");
                        if (i != resultList.size() - 1) buffer.append(",");
                    }
                }
            }
        }
        return conversationList;
    }

    public Conversation beanToDao(ConversationBean bean) {
        Conversation conversation = new Conversation();
        conversation.setConversationId(bean.objectId);
        Logger.d(TAG, bean.m.toString());
        conversation.setMembersId(bean.m.toString());
        conversation.setCreaterId(bean.c);
        if (bean.lm != null && bean.lm.iso != null)
            conversation.setLast_message_time(bean.lm.iso);

        return conversation;
    }

    // Get local cache
    public ArrayList<FollowerBean> getCacheFriends() {
        List<Follower> followers = DBHelper.getInstance().getFollowers();
        List<Followee> followees = DBHelper.getInstance().getFollowees();
        ArrayList<FollowerBean> userList = new ArrayList<>();
        for (int i = 0; i < followees.size(); i++) {
            if (followees.get(i) == null) {
                continue;
            }
            for (int j = 0; j < followers.size(); j++) {
                if (followers.get(j) == null) {
                    continue;
                }
                if (followees.get(i).getUserName().equals(followers.get(j).getUserName())) {
                    FollowerBean follower = new FollowerBean();
                    follower.userName = followers.get(j).getUserName();
                    follower.nickName = followers.get(j).getNickName() != null ? followers.get(j).getNickName() : followers.get(j).getUserName();
                    follower.avatar = followers.get(j).getAvatar();
                    follower.objId = followers.get(j).getObjectId();
                    userList.add(follower);
                }
            }
        }
        return userList;
    }


    public void deleteFollowee(String userId) {
        FolloweeDBService.deleteFollowee(userId);
    }

    public void insertWeight(long date, double weight) {
        double bmi;
        double height = getCurrentAccount().getHeight();
        height = height / 100f;
        bmi = weight / height / height;

        DBWeight dbWeight = new DBWeight();
        dbWeight.setWeight((float) weight);
        dbWeight.setDate(date);
        dbWeight.setUpload(false);
        dbWeight.setBodyMI((float) bmi);
        dbWeight.setUid(PreferencesHelper.getCurrentId());
        WeightDBService.insertWeight(dbWeight);
    }

    public List<DBWeight> getAllWeight() {
        return WeightDBService.getAllWeight();
    }

    public List<HistoryData> getNeedToUploadStep() {
        List<HistoryData> list = StepDBService.getNeedToUploadStepData();
        for (int i = 0; i < list.size(); i++) {
            long step = list.get(i).getStep();
            Logger.d("DBHelper", step + "");
        }
        return list;
    }

    public List<Sleep> getNeedToUploadSleep() {
        return SleepDBService.getNeedToUploadSleepData();
    }

    public List<HeartRate> getNeedToUploadHeartRate() {
        return HeartRateDBService.getNeedToUploadSleepData();
    }

    public boolean isSleepDBEmpty() {
        return SleepDBService.isSleepDBEmpty();
    }

    public boolean isRateDBEmpty() {
        return HeartRateDBService.isRateDBEmpty();
    }

    public boolean isWeightDBEmpty() {
        return WeightDBService.getAllWeight().isEmpty();
    }

    public void deleteWeight(DBWeight weight) {
        WeightDBService.deleteWeight(weight);
    }

    public void updateWalkCount(int step) {
        UserDBService.updateWalkCount(step);
    }

    public List<HistoryData> getAllStepData() {
        return StepDBService.getAllStepData();
    }

    public List<HistoryData> getUserStepData() {
        return StepDBService.getUserStepData();
    }

    // blood pressure
    public List<BloodPressure> queryMultiDayBloodPressure(long from, long to) {
        return BloodPressureDBService.queryMultiDayBloodPressure(from, to);
    }

    public void setBloodPressureUploaded(long date) {
        BloodPressureDBService.setBloodPressureUploaded(date);
    }

    public boolean isBloodDBEmpty() {
        return BloodPressureDBService.isBloodDBEmpty();
    }

    public BloodPressure queryDayBloodPressureFromDB(long date) {
        return BloodPressureDBService.queryDayBloodPressureFromDB(date);
    }

    public void insertBloodPressure(List<BloodPressure> bloodPressures) {
        BloodPressureDBService.insertBloodPressure(bloodPressures);
    }

    public List<BloodPressure> getNeedToUploadBloodData() {
        return BloodPressureDBService.getNeedToUploadBloodData();
    }

    public long queryBloodPressureDBLatestDate() {
        return BloodPressureDBService.queryBloodPressureDBLatestDate();
    }

    public int bloodDBSize() {
        return BloodPressureDBService.bloodDBSize();
    }

    public List<BloodPressure> getWeekBloodPressureData(long date) {
        List<Date> weekDates = DateUtils.getWeekOfDateBySeconds(date);
        long from = weekDates.get(0).getTime();
        long to = weekDates.get(weekDates.size() - 1).getTime();
        return BloodPressureDBService.queryMultiDayBloodPressure(from, to);
    }

    public BloodPressure queryDayBloodPressureWithTime(long date) {
        return BloodPressureDBService.queryDayBloodPressureWithTime(date);
    }

    public boolean currentUserDBEmpty() {
        return BloodPressureDBService.currentUserDBEmpty();
    }

    public void updateMedal(String[] medal) {
        UserDBService.updateMedal(medal);
    }

    /********************************************
     *                                          *
     *                                          *
     *   the below methods just for K9 data.    *
     *                                          *
     *                                          *
     ********************************************/

    public void insertStepNodeDetail(SportItemPacket packet, final long date) {
        if (packet == null) {
            return;
        }

        if (!DateUtils.isDateValid(date)) {
            Logger.d(TAG, "the date is invalid");
            return;
        }

        final int step = packet.getStepCount();
        final int calories = packet.getCalory();
        final int distance = packet.getDistance();
        final int offset = packet.getOffset();

        if (offset < 0 || offset > 96) {
            return;
        }

        if (step == 0) {
            return;
        }

        if (distance == 0) {
            return;
        }

        if (calories == 0) {
            return;
        }

        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                StepNodeDetailService.insertStepNodeDetail(date, offset, step, calories, distance);
                updateDayStep(date);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertHistoryDayStep(long date, int dayHistoryTotalStep, int dayHistoryTotalCalories,
                                     float dayHistoryTotalDistance) {
        final HistoryData data = new HistoryData();
        data.setDate(date);
        data.setDistance(dayHistoryTotalCalories / 1000f);
        data.setStep(dayHistoryTotalStep);
        data.setDistance(dayHistoryTotalDistance / 1000f);
        data.setUpload(false);
        data.setTargetFinish(dayHistoryTotalStep > getUser().getWalkTarget());
        data.setUid(PreferencesHelper.getCurrentId());

        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                StepDBService.insertHistoryDataToDB(data);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();


    }

    public void insertSleepNodeDetail(SleepNodeDetail detail) {
        SleepNodeDetailService.insertSleepNodeDetail(detail);
    }

    public List<SleepNodeDetail> getSleepNodeDetailByDate(long date) {
        return SleepNodeDetailService.getSleepNodeDetailByDate(date);
    }

    public void insertDaySleepTime(long date, int time, int mode) {
        SleepDBService.insertDaySleepTime(date, time, mode);
    }

    public List<StepNodeDetail> getDayStepNodeDetail(long date) {
        return StepNodeDetailService.getStepNodeDetail(date);
    }

    public void updateDayStep(final long date) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                StepDBService.updateDayStep(date);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertTotalSleepData(List<Long> receivedSleepDate, Action completedAction) {
        Observable.fromIterable(receivedSleepDate)
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(final Long aLong) throws Exception {
                        return Observable.create(new ObservableOnSubscribe() {
                            @Override
                            public void subscribe(ObservableEmitter e) throws Exception {
                                SleepDBService.insertTotalSleepData(aLong);
                                e.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(completedAction)
                .subscribe();
    }

    public void insertDaySleepTotalData(final long date) {
        Observable.wrap(new ObservableSource() {
            @Override
            public void subscribe(Observer observer) {
                SleepDBService.insertTotalSleepData(date);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertBloodPressure(final long date, final long time, final int heartRateValue,
                                    final int highPressureValue, final int lowPressureValue) {
        BloodPressureDBService.insertBloodPressure(date, time, heartRateValue,
                highPressureValue, lowPressureValue);

    }

    public LastRate getLastHeartRate() {
        return HeartRateDBService.queryLastHeartRateData();
    }

    public long getLastSleepDate() {
        return SleepDBService.queryLocalDBLatestDate();
    }

    public Sleep getSleepDataByDateFromLocalDB(long date) {
        return SleepDBService.querySleepByDateFromLocalDB(date);
    }

    public List<DBWeight> getLastSevenDayWeight() {
        return WeightDBService.getLastSevenDayWeight();
    }

    public StepNodeDetail getOffsetStep(long date, int offset) {
        return StepNodeDetailService.getOffsetStep(date, offset);
    }

    public void copyUteBloodPressureToLocalDB() {
        BloodPressureDBService.copyUteBloodPressureToLocalDB();
    }

    /******************* CoBand ****************************/
    public void insertAccount(String account, String pwd, String token) {
        Account insertAccount = new Account();
        insertAccount.setAccount(account);
        insertAccount.setToken(token);
        insertAccount.setPassword(pwd);
        AccountDBService.insertAccount(insertAccount);
    }

    public String getToken(String uid) {
        return AccountDBService.queryTokenByUid(uid);
    }

    public Account getAccount(String uid) {
        return AccountDBService.getAccount(uid);
    }

    public void updateOrInsertAccount(Account account) {
        AccountDBService.updateOrInsertAccount(account);
    }

    public Account getCurrentAccount() {
        return AccountDBService.getCurrentAccount();
    }

    public void updateAccountTarget(int stepTarget, int sleepTarget, double weightTarget) {
        AccountDBService.updateAccountTarget(stepTarget, sleepTarget, weightTarget);
    }

    public void updateAccountWeight(double weight) {
        AccountDBService.updateWeight(weight);
    }

    public void updateNickname(String nickname) {
        AccountDBService.updateNickname(nickname);
    }

    public void updateGender(String gender) {
        AccountDBService.updateGender(gender);
    }

    public void updateAccountHeight(int height) {
        AccountDBService.updateHeight(height);
    }

    public void updateAccountBirthday(String birthday) {
        AccountDBService.updateBirthday(birthday);
    }

    public void updateAccountAvatar(String avatarPath, String avatarMD5) {
        AccountDBService.updateAvatar(avatarPath, avatarMD5);
    }

    public void updateAccountBackground(String bgPath, String bgMD5) {
        AccountDBService.updateBackground(bgPath, bgMD5);
    }

    public void updateAccountUnitSystem(String unitSystem) {
        AccountDBService.updateUnitSystem(unitSystem);
    }

    public void updateAccountInfo(UpdateAccountInfo info) {
        AccountDBService.updateAccountInfo(info);
    }

    public void setDayDataUploaded(long date) {
        StepDBService.setStepUploaded(date);
        SleepDBService.setSleepUploaded(date);
        BloodPressureDBService.setBloodPressureUploaded(date);
        HeartRateDBService.setHeartRateUploaded(date);
    }
}
