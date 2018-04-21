package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.message.FriendsItem;

import java.util.List;

/**
 * Created by mai on 17-4-12.
 */

public interface FriendsView extends BaseView {

    void friends(List<FriendsItem> friends);
    void searchFriends(List<FriendsItem> friends);
    void cacheIsNull();

}
