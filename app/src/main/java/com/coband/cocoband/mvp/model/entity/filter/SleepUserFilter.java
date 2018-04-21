package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

import com.coband.cocoband.mvp.model.entity.UserPointer;

/**
 * Created by ivan on 17-5-8.
 */

public class SleepUserFilter {
    UserPointer sleepWithOwnUser;

    public SleepUserFilter(@NonNull String userObjectId) {
        sleepWithOwnUser = new UserPointer(userObjectId);
    }
}
