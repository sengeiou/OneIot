package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

import com.coband.cocoband.mvp.model.entity.UserPointer;

/**
 * Created by ivan on 17-4-24.
 */

public class SportUserFilter {
    UserPointer sportInfoWithUser;

    public SportUserFilter(@NonNull String userObjectId) {
        sportInfoWithUser = new UserPointer(userObjectId);
    }
}
