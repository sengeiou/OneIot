package com.coband.cocoband.mvp.model;

import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.Utils;

/**
 * Created by mqh on 9/23/16.
 */
public abstract class IMCOCallback2<T> {
    public IMCOCallback2() {}

    public void done(final T ... obj) {
        if (mustRunOnMainThread() && !Utils.isMainThread()) {
            boolean post = CocoUtils.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    IMCOCallback2.this.done0(obj);
                }
            });
            if (!post) {
                Logger.e(this, "Handler Post runnable in IMCOCallback failed!");
            }
        } else {
            this.done0(obj);
        }
    }

    protected boolean mustRunOnMainThread() {return true;}
    protected abstract void done0(T ... obj);
}
