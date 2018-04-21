package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

import com.coband.cocoband.mvp.model.entity.UserPointer;

/**
 * Created by ivan on 17-5-8.
 */

public class WeightUserFilter {

    private UserPointer weightWithUser;

    public WeightUserFilter(@NonNull String objectId) {
        weightWithUser = new UserPointer(objectId);
    }
}
