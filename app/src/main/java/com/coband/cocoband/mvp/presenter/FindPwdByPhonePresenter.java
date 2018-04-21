package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.FindPwdByPhoneView;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.request.ResetPwdBody;
import com.coband.cocoband.mvp.model.entity.response.ResetPwdResponse;
import com.coband.cocoband.mvp.model.entity.response.VerifyCodeResult;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by xing on 10/4/2018.
 * reset password by phone
 */
@Module
public class FindPwdByPhonePresenter extends BasePresenter {
    private FindPwdByPhoneView mView;

    @Inject
    public FindPwdByPhonePresenter() {

    }

    @Override
    public void attachView(BaseView view) {
        mView = (FindPwdByPhoneView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
        mView = null;
    }

    public void requestVerifyCode(String phone) {
        if (!NetWorkUtil.isNetConnected()) {
            mView.showNetworkUnavailable();
            return;
        }

        mView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("country", "0086");
        map.put("type", "reset");
        NetworkOperation.getInstance().requestVerifyCode(map,
                HandleEvent.RESET_PWD_REQUEST_VERIFY_CODE_SUCCESS,
                HandleEvent.RESET_PWD_REQUEST_VERIFY_CODE_FAILED);
    }

    public void requestResetPwd(String phoneNumber, String verificationCode, String pwd) {
        if (!NetWorkUtil.isNetConnected()) {
            mView.showNetworkUnavailable();
            return;
        }

        mView.showLoading();
        ResetPwdBody body = new ResetPwdBody();
        body.setNewPassword(pwd);
        body.setPhone(phoneNumber);
        body.setVerifyCode(verificationCode);
        body.setType("phone");
        NetworkOperation.getInstance().requestResetPwdByPhone(body);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.RESET_PWD_FAILED:
                mView.showResetPasswordFailed((String) event.getObject());
                break;
            case HandleEvent.RESET_PWD_SUCCESS:
                ResetPwdResponse response = (ResetPwdResponse) event.getObject();
                if (response.getCode() == 0) {
                    mView.showResetPasswordSuccess();
                } else {
                    mView.showResetPasswordFailed(response.getCode());
                }
                break;
            case HandleEvent.RESET_PWD_REQUEST_VERIFY_CODE_SUCCESS:
                VerifyCodeResult result = (VerifyCodeResult) event.getObject();
                if (result.getCode() == 0) {
                    mView.showRequestVerifyCodeSuccess();
                } else {
                    mView.showRequestVerifyCodeFailed(result.getCode());
                }
                break;
            case HandleEvent.RESET_PWD_REQUEST_VERIFY_CODE_FAILED:
                mView.showRequestVerifyCodeFailed((String) event.getObject());
                break;
        }
    }
}
