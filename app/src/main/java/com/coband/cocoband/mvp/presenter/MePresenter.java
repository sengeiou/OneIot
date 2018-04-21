package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by mai on 17-4-20.
 */
@Module
public class MePresenter extends BasePresenter {

    @Inject
    public MePresenter() {
    }

    @Override
    public void attachView(BaseView view) {

    }

    @Override
    public void detachView() {

    }
}
