package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.event.me.DeleteFriendsEvent;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.IUserInfoView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.FollowerBean;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.Followees;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.Followers;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.FollowersAndFolloweesBean;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.Logger;

import com.coband.watchassistant.Account;
import com.coband.watchassistant.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by mai on 17-5-25.
 */
@Module
public class UserInfoPresenter extends BasePresenter {
    private IUserInfoView mView;
    private static final String TAG = "UserInfoPresenter";

    @Inject
    public UserInfoPresenter() {
    }

    private String mUserName;

    @Override
    public void attachView(BaseView view) {
        this.mView = (IUserInfoView) view;
        EventBus.getDefault().register(this);
    }

    public void init() {
        setupAllUserData();
    }

    private void setupAllUserData() {
        Account account = DataManager.getInstance().getCurrentAccount();
        String nickName = account.getNickname();
        mView.showNickName(nickName == null ? "CoBand" : nickName);

        String avatar = account.getAvatar();
        mView.showAvatar(avatar);

        String background = account.getBackground();
        if (background != null) {
            mView.showSurfaceImage(background);
        }

        Long dayHighestSteps = account.getMaxDaySteps();
        if (dayHighestSteps == null) {
            mView.showDayHighestSteps(0);
        } else {
            mView.showDayHighestSteps(dayHighestSteps.intValue());
        }

        if (null != account.getAchievements()) {
            mView.showAchievementList(Arrays.asList(account.getAchievements().split(",")));
        }
    }

    // 这是排行榜跳转过来的，排行榜用户数据过多，只缓存在内存
    public void init(com.coband.cocoband.mvp.model.bean.User user) {

        mView.showNickName(user.getNickName() != null ? user.getNickName() : user.getUsername());
        mView.showAvatar(user.getAvatar());
        mView.showSurfaceImage(user.getSurfaceImg());
        mView.showDayHighestSteps(user.getDayHighestSteps());
        mView.showAchievementList(user.getArchivementList());
        mUserName = user.getUsername();

        ArrayList<FollowerBean> cacheFriends = DBHelper.getInstance().getCacheFriends();
        boolean isFriend = isFriends(cacheFriends, user.getUsername());
        if (!isFriend) {
            NetworkOperation.getInstance().getFollowersAndFollowees(DBHelper.getInstance()
                    .getUser().getObjectId());
        }
        mView.isFriends(isFriend);
    }


    // Monitor network data
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFriends(FollowersAndFolloweesBean bean) {
        List<Followees> followees = bean.followees;
        List<Followers> followers = bean.followers;

        ArrayList<FollowerBean> userList = new ArrayList<>();
        for (int i = 0; i < followees.size(); i++) {
            if (followees.get(i).followee == null) {
                continue;
            }
            for (int j = 0; j < followers.size(); j++) {
                if (followers.get(j).follower == null) {
                    continue;
                }

                if (followees.get(i).followee.getUsername().equals(followers.get(j).follower.getUsername())) {
                    FollowerBean follower = new FollowerBean();
                    follower.userName = followers.get(j).follower.getUsername();
                    follower.nickName = followers.get(j)
                            .follower.getNickName() != null ? followers.get(j).follower.getNickName() : followers.get(j).follower.getUsername();
                    follower.avatar = followers.get(j).follower.getAvatar();
                    follower.objId = followers.get(j).follower.getObjectId();
                    userList.add(follower);
                }
            }
        }
        mView.isFriends(isFriends(userList, mUserName));
    }


    private boolean isFriends(ArrayList<FollowerBean> list, String userName) {
        boolean isFriend = false;
        for (int i = 0; i < list.size(); i++) {
            FollowerBean followerBean = list.get(i);

            if (followerBean.userName.equals(userName)) {
                Logger.d(TAG, "is Friend : " + isFriend);
                isFriend = true;
            }
        }
        return isFriend;
    }

    public List<String> getUserImgAndAvatar() {
        List<String> list = new ArrayList<>();
        Account account = DBHelper.getInstance().getCurrentAccount();
        list.add(account.getAvatar());
        list.add(account.getAvatar());
        list.add(account.getNickname());
        return list;
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }
}
