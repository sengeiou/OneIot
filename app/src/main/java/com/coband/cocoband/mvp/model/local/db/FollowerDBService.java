package com.coband.cocoband.mvp.model.local.db;

import android.util.Log;

import com.coband.App;

import com.coband.cocoband.mvp.model.bean.followersandfollowees.Followers;

import com.coband.common.utils.Logger;
import com.coband.watchassistant.FollowerDao;
import com.coband.watchassistant.User;


import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mai on 17-4-25.
 */

class FollowerDBService {
    private static final String TAG = "FollowerDBService";

    /**
     * @param followers
     * @param user      current user
     */
    public static void insertFollowers(List<Followers> followers, User user) {
        for (Followers follower : followers) {
            Logger.d(TAG, "insert Followers : "+ follower.objectid);
            if (follower == null) continue;
            if (follower.follower == null) continue;
            // 把我关注的人的信息存储到本地
            DBHelper.getInstance().insertUser(follower.follower);
            insertFollower(follower.follower, user);
        }
    }

    /**
     * @param follower
     * @param user     current user
     */
    public static void insertFollower(com.coband.cocoband.mvp.model.bean.User follower, User user) {
        com.coband.watchassistant.Follower dbFollower = new com.coband.watchassistant.Follower();
        Logger.d(TAG, ">>>>>objectID : " + follower.getObjectId());
        dbFollower.setObjectId(follower.getObjectId());
        dbFollower.setAvatar(follower.getAvatar());
        dbFollower.setNickName(follower.getNickName());
        dbFollower.setSurfaceImg(follower.getSurfaceImg());
        dbFollower.setUserName(follower.getUsername());
        dbFollower.setUser(user);
        dbFollower.setDayHighestSteps(follower.getDayHighestSteps());
        List<String> list = follower.getArchivementList();
        if (null != list && list.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i != list.size() - 1)
                    sb.append(",");
            }
            dbFollower.setArchivementList(sb.toString());
        }
        dbFollower.setUserId(user.getId());
        long l = App.getDaoSession().insertOrReplace(dbFollower);
        if (l == -1) {
            Log.e(TAG, "insert follower:" + follower.getUsername() + " error");
        }
    }


    /**
     * @param user current user
     * @return
     */
    public static List<com.coband.watchassistant.Follower> queryFollowers(User user) {
        Logger.d("FriendsListPresenter", user.getUsername());
        Logger.d("FriendsListPresenter", "user ID"+user.getId());

        FollowerDao followerDao = App.getDaoSession().getFollowerDao();
        QueryBuilder<com.coband.watchassistant.Follower> where = followerDao.queryBuilder().where(FollowerDao.Properties.UserId.eq(user.getId()));
        return where.list();
    }

    /**
     * @param followerId The objectId of follower
     * @return
     */
    public static com.coband.watchassistant.Follower queryFollower(String followerId) {
        FollowerDao followerDao = App.getDaoSession().getFollowerDao();
        QueryBuilder<com.coband.watchassistant.Follower> where = followerDao.queryBuilder().where(FollowerDao.Properties.ObjectId.eq(followerId));
        return where.unique();
    }

}
