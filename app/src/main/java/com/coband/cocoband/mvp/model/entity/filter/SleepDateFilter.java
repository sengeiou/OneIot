package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

/**
 * Created by ivan on 17-5-8.
 */

public class SleepDateFilter extends SleepUserFilter {
    long timestamp;

    public SleepDateFilter(long date, @NonNull String userObjectId) {
        super(userObjectId);
        this.timestamp = date;
    }
}
