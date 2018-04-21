package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

import com.coband.cocoband.mvp.model.entity.UserPointer;

/**
 * Created by ivan on 17-5-8.
 */

public class HeartRateUserFilter {
    UserPointer heartWithUser;

    public HeartRateUserFilter(@NonNull String userObjectId) {
        heartWithUser = new UserPointer(userObjectId);
    }
}
