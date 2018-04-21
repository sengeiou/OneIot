package com.coband.cocoband.mvp;

import com.coband.common.utils.NetWorkUtil;

/**
 * Created by ivan on 17-4-11.
 */

public abstract class BasePresenter {


    public abstract void attachView(BaseView view);

    public abstract void detachView();

    public boolean isNetworkAvaible() {
        return NetWorkUtil.isNetConnected();
    }
}
