package com.coband.cocoband.mvp.model.local.db;

import android.util.Log;

import com.coband.App;
import com.coband.cocoband.mvp.model.bean.AvatarBean;
import com.coband.cocoband.mvp.model.bean.ConvertUnitBean;
import com.coband.cocoband.mvp.model.bean.SurfaceImgBean;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.MatchMailAddress;
import com.coband.watchassistant.User;
import com.coband.watchassistant.UserDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by ivan on 17-4-10.
 * user database manager. all user data transaction must be access by this class.
 */


public class UserDBService {
    private static final long BIRTHDAY_DEFAULT = 649436400000L;
    private static final String TAG = "UserDBService";

    public UserDBService() {

    }

    //register time
    static void insertUserReg(com.coband.cocoband.mvp.model.bean.User user) {
//        MasterUser dbUser = new MasterUser();

        if (null != getDataID(user.getUsername())) {
            User dbUser = getDataID(user.getUsername());
            if (null != user.getAppVersion()) {
                dbUser.setAppVersion(user.getAppVersion());
            }
            if (null != user.getUsername()) {
                dbUser.setUsername(user.getUsername());
            }
            if (null != user.getEmail()) {
                dbUser.setEmail(user.getEmail());
            }
            if (null != user.getSessionToken()) {
                dbUser.setSessionToken(user.getSessionToken());
            }
            if (null != user.getCreatedAt()) {
                dbUser.setCreatedAt(user.getCreatedAt());
            }
            if (null != user.getUpdatedAt()) {
                dbUser.setUpdatedAt(user.getUpdatedAt());
            }
            if (null != user.getObjectId()) {
                dbUser.setObjectId(user.getObjectId());
            }
            App.getUserDao().update(dbUser);
        } else {
            insertUser(user);
        }

    }

    public static boolean insertUser(com.coband.cocoband.mvp.model.bean.User user) {
        Logger.d(TAG, ">>>>>>>>>>>>>>>>1");
        User dbUser = new User();
        UserDao userDao = App.getDaoSession().getUserDao();

        dbUser.setWalkTarget(user.getWalkTarget());
        dbUser.setSleepTarget(user.getSleepTarget());
        dbUser.setWeightTarget(user.getWeightTarget());

        if (null != user.getAppVersion()) {
            dbUser.setAppVersion(user.getAppVersion());
        }
        if (null != user.getUsername()) {
            dbUser.setUsername(user.getUsername());
        }
        if (null != user.getEmail()) {
            dbUser.setEmail(user.getEmail());
        }
        if (null != user.getSessionToken()) {
            dbUser.setSessionToken(user.getSessionToken());
        }
        if (null != user.getCreatedAt()) {
            dbUser.setCreatedAt(user.getCreatedAt());
        }
        if (null != user.getUpdatedAt()) {
            dbUser.setUpdatedAt(user.getUpdatedAt());
        }
        if (null != user.getObjectId()) {
            dbUser.setObjectId(user.getObjectId());
        }
        if (null != user.getAvatar()) {
            dbUser.setAvatar(user.getAvatar());
        }
        if (null != user.getSurfaceImg()) {
            dbUser.setSurfaceImg(user.getSurfaceImg());
        }
        if (null != user.getNickName()) {
            dbUser.setNick(user.getNickName());
        }
        Logger.d(TAG, ">>>>>>>>>>>>>>>>2");

        dbUser.setDayHighestSteps(user.getDayHighestSteps());
        if (null != user.getBirthday()) {
            Date birthday = new Date();
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'");
                birthday = simpleDateFormat.parse(user.getBirthday().getIso());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dbUser.setBirthday(birthday.getTime());
        } else {
            // 默认的生日时间戳
            dbUser.setBirthday(BIRTHDAY_DEFAULT);
        }

        dbUser.setGendar(user.getSex());

        dbUser.setHeight(user.getHeight());

        dbUser.setWeight(user.getWeight());

        dbUser.setUnit(user.getUnit());

        dbUser.setSex(user.getSex());
        Logger.d(TAG, ">>>>>>>>>>>>>>>>3");

        if (null != user.getArchivementList()) {

            List<String> list = user.getArchivementList();
            if (null != list && list.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < list.size() - 1; i++) {
                    sb.append(list.get(i));
                    sb.append(",");
                }
                sb.append(list.get(list.size() - 1));
                dbUser.setArchivementList(sb.toString());
            }
        }

        dbUser.setBeginSportTimestamp(user.getBeginSportTimestamp());
        dbUser.setTotalCalories(user.getTotalCalories());
        dbUser.setTotalDistance(user.getTotalDistance());
        dbUser.setTotalExerciceDays(user.getTotalExerciceDays());
        dbUser.setTotalWalkCount(user.getTotalWalkCount());
        dbUser.setCaloryTarget(user.getCaloriesTarget());
        dbUser.setDayHighestSteps(user.getDayHighestSteps());
        if (user.getBestSportInfoWithDay() != null) {
            // note : just save object id of BestSportInfoWithDay , because login is 'post' that it can't add 'include'
            dbUser.setBestSportInfoWithDay(user.getBestSportInfoWithDay().getObjectId());
        }
        Logger.d(TAG, ">>>>>>>>>>>>>>>>4");

        if (user.getDeviceTypes() != null)
            dbUser.setDeviceTypes(user.getDeviceTypes().toString());
        dbUser.setDistanceTarget(user.getDistanceTarget());
        long b = userDao.insertOrReplace(dbUser);
        Logger.d(TAG, ">>>>>>>>>>>>>>>>5");

        return b != -1;
    }

    public static void updateUser(User user) {
        UserDao userDao = App.getDaoSession().getUserDao();
        userDao.update(user);
    }

    public static User queryUser(String objectID) {
        UserDao userDao = App.getDaoSession().getUserDao();
        QueryBuilder<User> where = userDao.queryBuilder().where(UserDao.Properties.Id.eq(Long.parseLong(objectID, 16)));
        return where.unique();
    }


    public static void deleteUser(String objectID) {
        UserDao userDao = App.getDaoSession().getUserDao();
        userDao.deleteByKey(Long.parseLong(objectID, 16));
    }

    static void updateWalkCount(int step) {
        User dbUser = getDataID(DBHelper.getInstance().getUser().getUsername());
        dbUser.setTotalWalkCount(step);
        Logger.d("UserDBService", step+"!!++__");
        App.getUserDao().update(dbUser);
    }

    //login time
    static void insertUserSignIn(com.coband.cocoband.mvp.model.bean.User user) {
//        MasterUser dbUser = new MasterUser();

        if (null != getDataID(user.getUsername())) {
            User dbUser = getDataID(user.getUsername());
            if (null != user.getSessionToken()) {
                dbUser.setSessionToken(user.getSessionToken());
            }
            if (null != user.getUpdatedAt()) {
                dbUser.setUpdatedAt(user.getUpdatedAt());
            }
            if (null != user.getObjectId()) {
                dbUser.setObjectId(user.getObjectId());
            }
            if (null != user.getUsername()) {
                dbUser.setUsername(user.getUsername());
            }
            if (null != user.getEmail()) {
                dbUser.setEmail(user.getEmail());
            }
            if (null != user.getCreatedAt()) {
                dbUser.setCreatedAt(user.getCreatedAt());
            }
            dbUser.setEmailVerified(user.isEmailVerified());
            dbUser.setMobilePhoneVerified(user.isMobilePhoneVerified());
            App.getUserDao().update(dbUser);

        } else {
            insertUser(user);
        }
    }

    //user来自本地的数据,set info time
    static void insertUserInfo(com.coband.cocoband.mvp.model.bean.User user) {
//        MasterUser dbUser = new MasterUser();

        if (null != getDataID(user.getUsername())) {
            User dbUser = getDataID(user.getUsername());
            if (null != user.getUsername()) {
                dbUser.setUsername(user.getUsername());
            }
            if (null != user.getNickName()) {
                dbUser.setNick(user.getNickName());
            }
            if (null != user.getEmail()) {
                dbUser.setEmail(user.getEmail());
            }
            if (null != user.getUpdatedAt()) {
                dbUser.setUpdatedAt(user.getUpdatedAt());
            }
            dbUser.setSex(user.getSex());
            dbUser.setBirthday(Long.parseLong(user.getBirthday().getIso()));
            dbUser.setHeight(user.getHeight());
            dbUser.setWeight(user.getWeight());
            dbUser.setUnit(user.getUnit());
            if (null != user.getAppVersion()) {
                dbUser.setAppVersion(user.getAppVersion());
            }

            dbUser.setBeginSportTimestamp(user.getBeginSportTimestamp());

            dbUser.setWalkTarget(user.getWalkTarget());
            dbUser.setSleepTarget(user.getSleepTarget());
            dbUser.setWeightTarget(user.getWeightTarget());


            App.getUserDao().update(dbUser);

        } else {
            insertUser(user);
        }


    }

    static String getSession(String str) {
        Query<User> build;
        if (MatchMailAddress.match(str)) {
            build = App.getUserDao().queryBuilder().where(UserDao.Properties.Email.eq(str)).build();
        } else {
            build = App.getUserDao().queryBuilder().where(UserDao.Properties.Username.eq(str)).build();
        }
        List<User> list = build.list();
        User user = list.get(0);
        if (null != user) {
            return user.getSessionToken();
        } else {
            return "1";
        }
    }

    static String getObjID(String username) {
        Query<User> build = App.getUserDao().queryBuilder().where(UserDao.Properties.Username.eq(username)).build();
        List<User> list = build.list();
        User user = list.get(0);
        if (null != user) {
            return user.getObjectId();
        } else {
            return "1";
        }

    }

    static User getDataID(String str) {
        Query<User> build;
        if (MatchMailAddress.match(str)) {
            build = App.getUserDao().queryBuilder().where(UserDao.Properties.Email.eq(str)).build();
        } else {
            build = App.getUserDao().queryBuilder().where(UserDao.Properties.Username.eq(str)).build();
        }

        User queryUser = build.unique();
        if (null != queryUser) {
            return queryUser;
        } else {
            return null;
        }
    }

    //本地数据库存目标
    static void updateTarget(com.coband.cocoband.mvp.model.bean.User user) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setWalkTarget(user.getWalkTarget());
        dbUser.setSleepTarget(user.getSleepTarget());
        dbUser.setWeightTarget(user.getWeightTarget());
        dbUser.setUpdatedAt(DateUtils.getDateStrByMilliseconds(System.currentTimeMillis()));

        App.getUserDao().update(dbUser);
    }

    static void updateAvatarUrl(AvatarBean bean) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setAvatar(bean.getAvatar());
        dbUser.setUpdatedAt(DateUtils.getDateStrByMilliseconds(System.currentTimeMillis()));

        App.getUserDao().update(dbUser);
    }

    static void updateSurfaceImgUrl(SurfaceImgBean bean) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setSurfaceImg(bean.getSurfaceImg());
        dbUser.setUpdatedAt(DateUtils.getDateStrByMilliseconds(System.currentTimeMillis()));

        App.getUserDao().update(dbUser);
    }

    static void updateUserInfoInSetting(User user) {
        User dbUser = getDataID(getUser().getUsername());
        /*if (type.equals("weight")){
            dbUser.setWeight(user.getWeight());
            dbUser.setUnit(user.getUnit());
        }*/

        //防止数据库更新成null，unit的值需要每次随着更改而存入
        dbUser.setUnit(user.getUnit());
        dbUser.setWeight(user.getWeight());
        dbUser.setHeight(user.getHeight());
        dbUser.setNick(user.getNick());
        //防止数据库更新成null，sex的值需要每次随着更改而存入
        dbUser.setSex(user.getSex());
        dbUser.setBirthday(user.getBirthday());
        dbUser.setUpdatedAt(DateUtils.getDateStrByMilliseconds(System.currentTimeMillis()));

        App.getUserDao().update(dbUser);
    }

    static void updateUnit(int unit) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setUnit(unit);
        dbUser.setUpdatedAt(DateUtils.getDateStrByMilliseconds(System.currentTimeMillis()));

        App.getUserDao().update(dbUser);
    }

    static void updateUnitNoUpdateTime(int unit) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setUnit(unit);

        App.getUserDao().update(dbUser);
    }

//    public void insertUser(MasterUser user){
//        UserDao userDao = App.getDaoSession().getUserDao();
//        userDao.insert(user);
//    }


//    public static void insertUser(MasterUser user) {
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                //TODO
//            }
//        });
//
//        realm.close();
//
//    }
//

    //获得User对象
    public static User getUser() {
        UserDao userDao = App.getDaoSession().getUserDao();
        QueryBuilder<User> where = userDao.queryBuilder().where(UserDao.Properties.Username.eq(
                PreferencesHelper.getLatestUserName()));
        return where.unique();
    }

    public static User getUserByObjectId(String objectId) {
        UserDao userDao = App.getDaoSession().getUserDao();
        QueryBuilder<User> where = userDao.queryBuilder().where(UserDao.Properties.ObjectId.eq(objectId));
        if (where.list().size() > 0) {
            return where.unique();
        } else {
            return null;
        }
    }

    //转换单位
    static void updateUnit(ConvertUnitBean bean) {
        User dbUser = getDataID(getUser().getUsername());
        if (null != bean.getDistanceTarget()) {
            dbUser.setDistanceTarget(bean.getDistanceTarget());
        }
        if (null != bean.getTotalDistance()) {
            dbUser.setTotalDistance(bean.getTotalDistance());
        }
        if (null != bean.getWeightTarget()) {
            dbUser.setWeightTarget(bean.getWeightTarget());
        }
        if (null != bean.getHeight()) {
            dbUser.setHeight(bean.getHeight());
        }
        if (null != bean.getWeight()) {
            dbUser.setWeight(bean.getWeight());
        }
        dbUser.setUnit(bean.getUnit());
        dbUser.setUpdatedAt(DateUtils.getDateStrByMilliseconds(System.currentTimeMillis()));

        App.getUserDao().update(dbUser);
    }

    static void updateWeight(double weight) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setWeight(weight);
        App.getUserDao().update(dbUser);
    }

    static void updateDayHighestSteps(int steps) {
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setDayHighestSteps(steps);
        App.getUserDao().update(dbUser);
    }

    static void updateMedal(String[] medal) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < medal.length; i++) {
            builder.append(medal[i]).append(",");
        }
        String medalStr = builder.toString();
        medalStr = medalStr.substring(0, medalStr.length() - 1);
        User dbUser = getDataID(getUser().getUsername());
        dbUser.setArchivementList(medalStr);
        App.getUserDao().update(dbUser);
    }
}
