package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

/**
 * Created by ivan on 17-4-26.
 */

public class SportDateFilter extends SportUserFilter {
    long timestamp;

    public SportDateFilter(long date, @NonNull String userObjectId) {
        super(userObjectId);
        this.timestamp = date;
    }
}
