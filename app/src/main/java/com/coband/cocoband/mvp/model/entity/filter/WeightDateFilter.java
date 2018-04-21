package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

/**
 * Created by ivan on 17-5-8.
 */

public class WeightDateFilter extends WeightUserFilter {
    long timestamp;

    public WeightDateFilter(@NonNull String objectId, long date) {
        super(objectId);
        this.timestamp = date;
    }
}
