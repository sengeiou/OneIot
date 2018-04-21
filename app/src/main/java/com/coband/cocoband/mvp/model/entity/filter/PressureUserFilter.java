package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

import com.coband.cocoband.mvp.model.entity.UserPointer;

/**
 * Created by tgc on 17-9-27.
 */

public class PressureUserFilter {
    UserPointer bloodPressureWithUser;

    public PressureUserFilter(@NonNull String userObjectId) {
        bloodPressureWithUser = new UserPointer(userObjectId);
    }
}
