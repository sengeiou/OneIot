package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.ActiveFriendsBean;

import java.util.ArrayList;

/**
 * Created by mai on 17-5-31.
 */

public interface IAddFriendsView extends BaseView {
    void getActiveUser(ArrayList<ActiveFriendsBean> list);
}
