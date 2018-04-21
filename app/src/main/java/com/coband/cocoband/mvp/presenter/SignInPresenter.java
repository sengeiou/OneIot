package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.SignInView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.request.LogInBody;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.NetWorkUtil;
import com.coband.watchassistant.Account;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import dagger.Module;

/**
 * Has Network Login Presenter
 * <p>
 * Created by tgc on 17-4-22.
 */

@Module
public class SignInPresenter extends BasePresenter {

    private static final String TAG = "SignInPresenter";
    public static final int INCH = 1;
    public static final int METRIC = 0;

    private String mAccount, mPwd;

    SignInView iView;

    @Inject
    public SignInPresenter() {

    }

    public void login(String account, String password) {
        if (!NetWorkUtil.isNetConnected()) {
            iView.showNetworkUnavailable();
            return;
        }


        mAccount = account;
        mPwd = password;

        iView.showPosting();
        LogInBody body = new LogInBody();
        body.setPassword(password);
        if (account.contains("@")) {
            body.setType("email");
            body.setEmail(account);
        } else {
            body.setType("phone");
            body.setPhone(account);
        }
        NetworkOperation.getInstance().logIn(body);
    }

    public void setUsernameEditText() {
        if (null != DataManager.getInstance().getLatestUserName()) {
            iView.setUsernameEditText(PreferencesHelper.getLatestUserName());
        }
    }

    @Subscribe
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.LOGIN_SUCCESS:
                LogInResponse response = (LogInResponse) event.getObject();
                if (response.getCode() == 0) {
                    handleThirdPartyLogInSuccess(response);
                } else {
                    iView.showLogInFailed(response.getCode());
                }
                break;
            case HandleEvent.LOGIN_FAILED:
                iView.showLogInFailed((String) event.getObject());
                break;
            default:
                break;
        }
    }

    private void handleThirdPartyLogInSuccess(LogInResponse response) {
        LogInResponse.PayloadBean.UserBean.PersonalInfoBean bean = response.getPayload().getUser().
                getPersonalInfo();
        DataManager.getInstance().setCurrentUid(response.getPayload().getUser().getUid());
        Account account = DataManager.getInstance().handleLogInResponse(response);
        if (mAccount != null) {
            account.setAccount(mAccount);
            account.setPassword(mPwd);
        }
        DataManager.getInstance().updateOrInsertAccount(account);
        if (bean == null || bean.getHeight() == 0 || bean.getWeight() == 0 ||
                bean.getBirthday() == null) {
            iView.showLogInSuccess(true);
        } else {
            iView.showLogInSuccess(false);
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (SignInView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }

    public void anonymousLogIn() {
        mPwd = null;
        mAccount = null;

//        if (NetWorkUtil.isNetConnected()) {
//            iView.showPosting();
        String serialNum = android.os.Build.SERIAL;
//            Logger.d(TAG, "serialNum >>>>>>>> " + serialNum);
//            LogInBody body = new LogInBody();
//            body.setType("anonymous");
//            body.setAnonymousID(serialNum);
//            NetworkOperation.getInstance().logIn(body);
//        } else {
        DataManager.getInstance().setCurrentUid(serialNum);
        Account account = DataManager.getInstance().getAccount(serialNum);
        if (account == null) {
            account = new Account();
            account.setUid(serialNum);
            DataManager.getInstance().updateOrInsertAccount(account);
            iView.showLogInSuccess(true);
        } else {
            iView.showLogInSuccess(false);
        }
//        }
    }

    public void wechatLogIn(LogInBody body) {
        mPwd = null;
        mAccount = null;

        if (NetWorkUtil.isNetConnected()) {
            iView.showPosting();
            NetworkOperation.getInstance().logIn(body);
        } else {
            iView.showNetworkUnavailable();
        }
    }
}
