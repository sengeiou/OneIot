package com.coband.cocoband.mvp.presenter;


import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.GuideForgotPasswordView;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.response.ResetPwdResponse;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-5-4.
 */

@Module
public class ForgotPasswordPresenter extends BasePresenter {
    GuideForgotPasswordView iView;

    @Inject
    public ForgotPasswordPresenter() {
    }

    public void sendResetEmail(String email) {
        if (!NetWorkUtil.isNetConnected()) {
            iView.showNetworkUnavailable();
            return;
        }

        iView.showLoading();
        NetworkOperation.getInstance().requestResetPwdByEmail(email);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.RESET_PWD_SUCCESS_BY_EMAIL:
                ResetPwdResponse response = (ResetPwdResponse)event.getObject();
                if(response.getCode() == 0) {
                    iView.showResetPwdSuccess();
                } else {
                    iView.showResetPwdFailed(response.getCode());
                }
                break;
            case HandleEvent.RESET_PWD_FAILED_BY_EMAIL:
                iView.showResetPwdFailed((String)event.getObject());
                break;
            default:
                break;
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (GuideForgotPasswordView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }

}
