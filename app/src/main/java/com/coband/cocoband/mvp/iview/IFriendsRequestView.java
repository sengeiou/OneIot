package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.FollowerBean;

import java.util.ArrayList;

/**
 * Created by mai on 17-6-9.
 */

public interface IFriendsRequestView extends BaseView {
    void loadFriendsRequest(ArrayList<FollowerBean> requestList);
}
