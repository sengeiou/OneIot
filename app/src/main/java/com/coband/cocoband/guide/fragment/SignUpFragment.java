package com.coband.cocoband.guide.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.ThirdPartyLogin;
import com.coband.cocoband.main.BandActivity;
import com.coband.cocoband.mvp.iview.SignUpView;
import com.coband.cocoband.mvp.model.entity.request.LogInBody;
import com.coband.cocoband.mvp.presenter.SignUpPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.common.utils.C;
import com.coband.common.utils.DialogUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;
import com.owen.library.sharelib.LoginManager;
import com.owen.library.sharelib.callback.WeiXinLoginCallback;
import com.owen.library.sharelib.model.WeiXinUserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment implements View.OnClickListener, SignUpView {

    private static final int PHONE_REGISTER = 0;
    private static final int EMAIL_REGISTER = 1;

    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    public static final String TAG = "SignUpFragment";

    @Inject
    SignUpPresenter mPresenter;

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    @BindView(R.id.iv_qq_login)
    ImageView mIvQQLogin;

    @BindView(R.id.iv_wechat_login)
    ImageView mIvWechatLogin;

    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;

    @BindView(R.id.bt_register)
    Button mBtRegister;

    @BindColor(R.color.color_9f9f9f)
    int mBtnDisabledColor;

    @BindColor(R.color.color_ffc814)
    int mBtnAbleColor;

    @BindString(R.string.phone_register)
    String mPhoneRegister;

    @BindString(R.string.email_register)
    String mEmailRegister;

    private int mRegisterMode = PHONE_REGISTER;
    private TextView mTvSelectCountry;
    private LinearLayout mLLSelectCountry;
    private EditText mEtPhone, mEtVerificationCode, mEtPhonePwd, mEtPhoneConfirmPwd, mEtEmail,
            mEtEmailPwd, mEtEmailConfirmPwd;
    private Button mBtSendVerifyCode;

    private GuideProgressDialog dialog;

    private CountDownTimer mCountDownTimer;

    @Override
    protected int getLayout() {
        return R.layout.fragment_guide_sign_up;
    }

    @Override
    protected void init() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View phoneContentView = inflater.inflate(R.layout.phone_register_content, null);
        View emailContentView = inflater.inflate(R.layout.email_register_content, null);

        mEtPhone = (EditText) phoneContentView.findViewById(R.id.et_phone);
        mEtVerificationCode = (EditText) phoneContentView.findViewById(R.id.et_verification_code);
        mEtPhonePwd = (EditText) phoneContentView.findViewById(R.id.et_pwd);
        mEtPhoneConfirmPwd = (EditText) phoneContentView.findViewById(R.id.et_confirm_pwd);
        mTvSelectCountry = (TextView) phoneContentView.findViewById(R.id.tv_select_country);
        mBtSendVerifyCode = (Button) phoneContentView.findViewById(R.id.bt_send_verification_code);
        mBtSendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVerifyCode();
            }
        });
        mLLSelectCountry = (LinearLayout) phoneContentView.findViewById(R.id.ll_select_country);
        mLLSelectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mEtEmail = (EditText) emailContentView.findViewById(R.id.et_email);
        mEtEmailPwd = (EditText) emailContentView.findViewById(R.id.et_pwd);
        mEtEmailConfirmPwd = (EditText) emailContentView.findViewById(R.id.et_confirm_pwd);

        List<View> list = new ArrayList<>();
        if(Utils.isChineseLanguage()) {
            list.add(phoneContentView);
            list.add(emailContentView);

            mTabLayout.addTab(mTabLayout.newTab().setText(mPhoneRegister),true);
            mTabLayout.addTab(mTabLayout.newTab().setText(mEmailRegister),false);

        } else {
            list.add(emailContentView);
            mTabLayout.addTab(mTabLayout.newTab().setText(mEmailRegister),false);
            mRegisterMode = EMAIL_REGISTER;
        }
        mVpContent.setAdapter(new RegisterAdapter(list));

        mTabLayout.setupWithViewPager(mVpContent);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mIvQQLogin.setOnClickListener(this);
        mIvWechatLogin.setOnClickListener(this);
        mBtRegister.setOnClickListener(this);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mRegisterMode = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgreement();
            }
        });
    }

    @Override
    protected void setupView() {
        AppComponent.getInstance().inject(this);
        setPresenter(mPresenter);
        mPresenter.attachView(this);
        setupToolbar(getString(R.string.register), toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qq_login:
                qqLogIn();
                break;
            case R.id.iv_wechat_login:
                wechatLogIn();
                break;
            case R.id.bt_register:
                startRegister();
                break;
            default:
                break;
        }
    }

    private void wechatLogIn() {
        LoginManager.loginWithWeiXin(mActivity, new WeiXinLoginCallback() {
            @Override
            public void onSuccess(WeiXinUserInfo userInfo) {
                LogInBody body = new LogInBody();
                body.setType("wechat");
                body.setAccess_token(userInfo.getAccess_token());
                body.setOpenid(userInfo.getOpenid());
                body.setAvatar(userInfo.getHeadimgurl());
                body.setNickName(userInfo.getNickname());
                body.setGender(userInfo.getSex() == 1 ? "Male" : "Female");
                body.setCountry(userInfo.getCountry());
                body.setProvince(userInfo.getProvince());
                body.setCity(userInfo.getCity());
                mPresenter.logIn(body);
            }

            @Override
            public void onCancel() {
                // ...
            }

            @Override
            public void onError() {
                // ...
            }
        });
    }

    private void qqLogIn() {
        ThirdPartyLogin.qqLogIn(this, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Logger.d(TAG, "o >>>>>>>>>> " + o);
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void requestVerifyCode() {
        String phone = mEtPhone.getText().toString();
        if (phone.isEmpty()) {
            showShortToast(R.string.phone_empty);
            return;
        }

        if (phone.length() != 11 || !phone.startsWith("1")) {
            showShortToast(R.string.phone_pattern_error);
            return;
        }

        mPresenter.requestVerifyCode(phone);
    }

    private void startRegister() {
        if (mRegisterMode == PHONE_REGISTER) {
            phoneRegister();
        } else {
            emailRegister();
        }
    }

    private void phoneRegister() {
        String phoneNumber = mEtPhone.getText().toString();
        if (phoneNumber.isEmpty()) {
            showShortToast(R.string.phone_empty);
            return;
        }

        if (phoneNumber.length() != 11 || !phoneNumber.startsWith("1")) {
            showShortToast(R.string.phone_pattern_error);
            return;
        }

        String verificationCode = mEtVerificationCode.getText().toString();
        if (verificationCode.isEmpty()) {
            showShortToast(R.string.verification_code_empty);
            return;
        }

        String pwd = mEtPhonePwd.getText().toString();
//        String confirmPwd = mEtPhoneConfirmPwd.getText().toString();

        if (pwd.isEmpty()) {
            showShortToast(R.string.pwd_empty);
            return;
        }

        if (pwd.length() < 6) {
            showShortToast(R.string.pwd_too_short);
            return;
        }

        if (pwd.length() > 15) {
            showShortToast(R.string.pwd_too_long);
            return;
        }

//        if (confirmPwd.isEmpty()) {
//            showShortToast(R.string.confirm_pwd_empty);
//            return;
//        }
//
//        if (!pwd.equals(confirmPwd)) {
//            showShortToast(R.string.pwd_inconsistent);
//            return;
//        }

        mPresenter.registerWithPhone(phoneNumber, verificationCode, pwd);

    }


    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    private void emailRegister() {
        String email = mEtEmail.getText().toString();
        if (email.isEmpty()) {
            showShortToast(R.string.email_empty);
            return;
        }

        if (!isEmail(email)) {
            showShortToast(R.string.email_format_error_tips);
            return;
        }

        String pwd = mEtEmailPwd.getText().toString();
//        String confirmPwd = mEtEmailConfirmPwd.getText().toString();

        if (pwd.isEmpty()) {
            showShortToast(R.string.pwd_empty);
            return;
        }

        if (pwd.length() < 6) {
            showShortToast(R.string.pwd_too_short);
            return;
        }

        if (pwd.length() > 15) {
            showShortToast(R.string.pwd_too_long);
            return;
        }

//        if (confirmPwd.isEmpty()) {
//            showShortToast(R.string.confirm_pwd_empty);
//            return;
//        }
//
//        if (!pwd.equals(confirmPwd)) {
//            showShortToast(R.string.pwd_inconsistent);
//            return;
//        }

        mPresenter.registerWithEmail(email, pwd);
    }

    private void showAgreement() {
        Bundle bundle = new Bundle();
        bundle.putString(C.CHINA_WEB, "http://www.priodigit.com/a/guanyu/36.html?1516119758");
        bundle.putString(C.WORLD_WEB, "http://www.priodigit.com/a/guanyu/36.html?1516119758");
        bundle.putString(C.WEB_NAME, App.getContext().getString(R.string.band_agreement_privacy));

        replaceFragment(new WebViewFragment(), WebViewFragment.TAG, true, bundle);
    }

    @Override
    public void showNetworkDisconnected() {
        showShortToast(R.string.network_error);
    }

    @Override
    public void closeDialog() {
        if (null != dialog) {
            dialog.cancel();
        }
    }

    @Override
    public void showDialog(String message) {
        dialog = DialogUtils.showGuideProgressDialog(getActivity(), message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void showRequestVerifyCodeFailed(String errorMsg) {
        showShortToast(R.string.request_verify_code_failed + " " + errorMsg);
    }

    @Override
    public void showRequestVerifyCodeSuccess() {
        mBtSendVerifyCode.setTextColor(mBtnDisabledColor);
        mBtSendVerifyCode.setClickable(false);
        startCountDown();
    }

    private void startCountDown() {
        mCountDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                mBtSendVerifyCode.setText(getString(R.string.remaining) + " " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mBtSendVerifyCode.setText(getString(R.string.resend));
                mBtSendVerifyCode.setClickable(true);
                mBtSendVerifyCode.setTextColor(mBtnAbleColor);
                mCountDownTimer = null;
            }
        }.start();
    }

    @Override
    public void showRegisterWithPhoneSuccess() {
        dismissLoadingDialog();
        replaceFragment(new SetUserInfoFragment(), SetUserInfoFragment.TAG, true);
    }

    @Override
    public void showRegisterWithEmailSuccess() {
        dismissLoadingDialog();
        replaceFragment(new SetUserInfoFragment(), SetUserInfoFragment.TAG, true);
    }

    @Override
    public void showRegisterWithPhoneFailed(String errorMsg) {
        dismissLoadingDialog();
        showShortToast(R.string.register_failed + " " + errorMsg);
    }

    @Override
    public void showRegisterWithEmailFailed(int code) {
        dismissLoadingDialog();
        switch (code) {
            case 5000:
                showShortToast(R.string.email_has_been_used);
                break;
            default:
                showShortToast(R.string.register_failed);
        }
    }

    @Override
    public void showRegisterWithPhoneFailed(int code) {
        dismissLoadingDialog();
        switch (code) {
            case 5000:
                showShortToast(R.string.phone_has_been_used);
                break;
            default:
                showShortToast(R.string.register_failed);
        }
    }

    @Override
    public void showRegisterWithEmailFailed(String errorMsg) {
        dismissLoadingDialog();
        showLongToast(R.string.register_failed + " " + errorMsg);

    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    private class RegisterAdapter extends PagerAdapter {
        private List<View> mListViews;

        RegisterAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));//删除页卡
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
            container.addView(mListViews.get(position), 0);//添加页卡
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.phone_register);
                case 1:
                    return getString(R.string.email_register);
                default:
                    return null;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    public void showLogInFailed(int code) {
        dismissLoadingDialog();
        showShortToast(getString(R.string.sign_in_error) + " cause by code " + code);
    }

    @Override
    public void showLogInFailed(String object) {
        dismissLoadingDialog();
        showShortToast(getString(R.string.sign_in_error) + " cause by code " + object);
    }

    @Override
    public void showLogInSuccess(boolean setUserInfo) {
        dismissLoadingDialog();
        if (setUserInfo) {
            replaceFragment(new SetUserInfoFragment(), SetUserInfoFragment.TAG, true);
        } else {
            getActivity().finish();
            Intent intent = new Intent(App.getContext(), BandActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }
}
