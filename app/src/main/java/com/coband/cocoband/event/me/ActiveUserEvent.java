package com.coband.cocoband.event.me;

import com.coband.cocoband.mvp.model.bean.ResultsList;
import com.coband.cocoband.mvp.model.bean.User;

/**
 * Created by mai on 17-5-31.
 */

public class ActiveUserEvent {
    public ActiveUserEvent(ResultsList<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public ResultsList<User> activeUsers;
}
