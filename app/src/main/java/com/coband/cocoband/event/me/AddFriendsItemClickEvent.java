package com.coband.cocoband.event.me;

import com.coband.cocoband.mvp.model.bean.followersandfollowees.ActiveFriendsBean;

/**
 * Created by mai on 31/05/17.
 */

public class AddFriendsItemClickEvent {
    public AddFriendsItemClickEvent(int position, ActiveFriendsBean bean) {
        this.position = position;
        this.bean = bean;
    }

    public int position;
    public ActiveFriendsBean bean;
}
