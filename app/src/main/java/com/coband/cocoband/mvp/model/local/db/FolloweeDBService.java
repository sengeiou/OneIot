package com.coband.cocoband.mvp.model.local.db;

import android.util.Log;

import com.coband.App;

import com.coband.cocoband.mvp.model.bean.followersandfollowees.Followees;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.Followee;
import com.coband.watchassistant.FolloweeDao;
import com.coband.watchassistant.User;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by mai on 17-4-25.
 */

class FolloweeDBService {
    private static final String TAG = "FolloweeDBService";

    /**
     * @param followees
     * @param user      current user
     */
    public static void insertFollowees(List<Followees> followees, User user) {
        for (Followees followee : followees) {
            if (followee == null) continue;
            if (followee.followee == null) continue;
            // 把关注我的人的信息存储到本地
            DBHelper.getInstance().insertUser(followee.followee);
            insertFollowee(followee.followee, user);
        }
    }

    /**
     * @param followee
     * @param user     current user
     */
    public static void insertFollowee(com.coband.cocoband.mvp.model.bean.User followee, User user) {
        com.coband.watchassistant.Followee dbFollowee = new com.coband.watchassistant.Followee();
        Logger.d(TAG, ">>>>>objectID : "+followee.getObjectId());
        dbFollowee.setObjectId(followee.getObjectId());
        dbFollowee.setAvatar(followee.getAvatar());
        dbFollowee.setNickName(followee.getNickName());
        dbFollowee.setSurfaceImg(followee.getSurfaceImg());
        dbFollowee.setUserName(followee.getUsername());
        dbFollowee.setUser(user);
        dbFollowee.setDayHighestSteps(followee.getDayHighestSteps());
        List<String> list = followee.getArchivementList();
        if (null != list && list.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i != list.size() - 1)
                    sb.append(",");
            }
            dbFollowee.setArchivementList(sb.toString());
        }
        dbFollowee.setUserId(user.getId());
        long l = App.getDaoSession().insertOrReplace(dbFollowee);
        if (l == -1) {
            Log.e(TAG, "insert followee:" + followee.getUsername() + " error");
        }
    }



    public static List<com.coband.watchassistant.Followee> queryFollowee(User user) {
        FolloweeDao followeeDao = App.getDaoSession().getFolloweeDao();
        QueryBuilder<Followee> where = followeeDao.queryBuilder().where(FolloweeDao.Properties.UserId.eq(user.getId()));
        return where.list();
    }

    public static void deleteFollowee(String userId) {
        FolloweeDao followeeDao = App.getDaoSession().getFolloweeDao();
        followeeDao.deleteByKey(userId);
    }

}
