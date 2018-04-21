package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.FollowerBean;

import java.util.List;

/**
 * Created by mai on 17-4-12.
 */

public interface IFriendsListView extends BaseView {
    void friends(List<FollowerBean> friends);
    void cacheIsNull();
}
