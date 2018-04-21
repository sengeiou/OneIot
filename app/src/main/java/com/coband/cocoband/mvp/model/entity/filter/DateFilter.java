package com.coband.cocoband.mvp.model.entity.filter;

/**
 * Created by ivan on 17-6-27.
 */

public class DateFilter {
    private long timestamp;

    public DateFilter(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
