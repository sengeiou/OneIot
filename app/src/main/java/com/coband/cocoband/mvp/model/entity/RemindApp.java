package com.coband.cocoband.mvp.model.entity;

import android.content.pm.ResolveInfo;

/**
 * Created by ivan on 17-5-27.
 */

public class RemindApp {
    private ResolveInfo mResolveinfo;
    private boolean mRemind;

    public ResolveInfo getResolveInfo() {
        return mResolveinfo;
    }

    public void setResolveInfo(ResolveInfo resolveinfo) {
        this.mResolveinfo = resolveinfo;
    }

    public boolean isRemind() {
        return mRemind;
    }

    public void setRemind(boolean mRemind) {
        this.mRemind = mRemind;
    }
}
