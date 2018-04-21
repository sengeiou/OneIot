package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

import java.util.List;

/**
 * Created by mai on 17-5-25.
 */

public interface IUserInfoView extends BaseView {
    void showNickName(String nickName);

    void isFriends(boolean isFriends);

    void showAvatar(String avatar);

    void showSurfaceImage(String imagePath);

    void showAchievementList(List<String> split);

    void showDayHighestSteps(int steps);

    void deleteFriends(boolean success);
}
