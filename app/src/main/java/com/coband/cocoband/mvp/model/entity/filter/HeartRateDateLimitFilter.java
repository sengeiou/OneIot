package com.coband.cocoband.mvp.model.entity.filter;

import android.support.annotation.NonNull;

/**
 * Created by ivan on 17-5-8.
 */

public class HeartRateDateLimitFilter extends HeartRateUserFilter {
    Limit timestamp;

    public HeartRateDateLimitFilter(long from, long to, @NonNull String userObjectId) {
        super(userObjectId);
        timestamp = new Limit(from, to);
    }

    class Limit {
        long $gte;
        long $lte;

        Limit(long from, long to) {
            $gte = from;
            $lte = to;
        }
    }
}
