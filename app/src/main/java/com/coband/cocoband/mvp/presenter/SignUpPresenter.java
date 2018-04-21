package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.SignUpView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.request.LogInBody;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.entity.response.RegisterResult;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.NetWorkUtil;
import com.coband.watchassistant.Account;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-4-24.
 */

@Module
public class SignUpPresenter extends BasePresenter {
    private SignUpView iView;

    private String mPassword;
    private String mAccount;

    @Inject
    public SignUpPresenter() {

    }

    public void registerWithPhone(String phoneNumber, String verificationCode, String password) {
        if (!NetWorkUtil.isNetConnected()) {
            iView.showNetworkDisconnected();
            return;
        }

        mPassword = password;
        mAccount = phoneNumber;

        iView.showLoading();
        NetworkOperation.getInstance().phoneRegister(phoneNumber, verificationCode, password);
    }

    public void registerWithEmail(String email, String password) {
        if (!NetWorkUtil.isNetConnected()) {
            iView.showNetworkDisconnected();
            return;
        }

        mPassword = password;
        mAccount = email;

        iView.showLoading();
        NetworkOperation.getInstance().emailRegister(email, password);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void query(HandleEvent handleEvent) {
        switch (handleEvent.getTag()) {
            case HandleEvent.REGISTER_WITH_PHONE_SUCCESS:
                handleRegisterWithPhoneResponse((RegisterResult) handleEvent.getObject());
                break;
            case HandleEvent.REGISTER_WITH_EMAIL_SUCCESS:
                handleRegisterWithEmailResponse((RegisterResult) handleEvent.getObject());
                break;
            case HandleEvent.REGISTER_WITH_PHONE_FAILED:
                iView.showRegisterWithPhoneFailed((String) handleEvent.getObject());
                break;
            case HandleEvent.REGISTER_WITH_EMAIL_FAILED:
                iView.showRegisterWithEmailFailed((String) handleEvent.getObject());
                break;
            case HandleEvent.REQUEST_VERIFY_CODE_SUCCESS:
                iView.showRequestVerifyCodeSuccess();
                break;
            case HandleEvent.REQUEST_VERIFY_CODE_FAILED:
                iView.showRequestVerifyCodeFailed((String) handleEvent.getObject());
                break;

            case HandleEvent.LOGIN_SUCCESS:
                LogInResponse response = (LogInResponse) handleEvent.getObject();
                if (response.getCode() == 0) {
                    handleThirdPartyLogInSuccess(response);
                } else {
                    iView.showLogInFailed(response.getCode());
                }
                break;
            case HandleEvent.LOGIN_FAILED:
                iView.showLogInFailed((String) handleEvent.getObject());
                break;
            default:
                break;
        }
    }

    private void handleThirdPartyLogInSuccess(LogInResponse response) {
        LogInResponse.PayloadBean.UserBean.PersonalInfoBean bean = response.getPayload().getUser().
                getPersonalInfo();
        DataManager.getInstance().setCurrentUid(response.getPayload().getUser().getUid());
        DataManager.getInstance().updateOrInsertAccount(response);
        if (bean.getHeight() == 0 || bean.getWeight() == 0 || bean.getBirthday() == null) {
            iView.showLogInSuccess(true);
        } else {
            iView.showLogInSuccess(false);
        }
    }

    private void handleRegisterWithEmailResponse(RegisterResult result) {
        if (result.getCode() == 0) {
            DataManager.getInstance().setCurrentUid(result.getPayload().getUid());
            String token = result.getPayload().getToken();
            Account account = new Account();
            account.setAccount(mAccount);
            account.setPassword(mPassword);
            account.setUid(result.getPayload().getUid());
            account.setToken(token);
            DataManager.getInstance().updateOrInsertAccount(account);
            iView.showRegisterWithEmailSuccess();
        } else {
            iView.showRegisterWithEmailFailed(result.getCode());
        }
    }

    private void handleRegisterWithPhoneResponse(RegisterResult result) {
        if (result.getCode() == 0) {
            DataManager.getInstance().setCurrentUid(result.getPayload().getUid());
            String token = result.getPayload().getToken();
            Account account = new Account();
            account.setAccount(mAccount);
            account.setPassword(mPassword);
            account.setUid(result.getPayload().getUid());
            account.setToken(token);
            DataManager.getInstance().updateOrInsertAccount(account);
            iView.showRegisterWithPhoneSuccess();
        } else {
            iView.showRegisterWithPhoneFailed(result.getCode());
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (SignUpView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }

    public void requestVerifyCode(String phone) {
        if (!NetWorkUtil.isNetConnected()) {
            iView.showNetworkDisconnected();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);

        NetworkOperation.getInstance().requestVerifyCode(map, HandleEvent.REQUEST_VERIFY_CODE_SUCCESS,
                HandleEvent.REQUEST_VERIFY_CODE_FAILED);
    }

    public void logIn(LogInBody body) {
        if (NetWorkUtil.isNetConnected()) {
            iView.showLoading();
            NetworkOperation.getInstance().logIn(body);
        } else {
            iView.showNetworkDisconnected();
        }
    }
}
