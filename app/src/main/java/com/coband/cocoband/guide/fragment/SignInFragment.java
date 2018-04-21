package com.coband.cocoband.guide.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.App;
import com.coband.cocoband.BaseFragment;

import com.coband.cocoband.main.BandActivity;
import com.coband.cocoband.mvp.iview.SignInView;
import com.coband.cocoband.mvp.model.entity.request.LogInBody;
import com.coband.cocoband.mvp.presenter.SignInPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.common.utils.Config;
import com.coband.common.utils.NetWorkUtil;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;
import com.owen.library.sharelib.LoginManager;
import com.owen.library.sharelib.callback.WeiXinLoginCallback;
import com.owen.library.sharelib.model.WeiXinUserInfo;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends BaseFragment implements TextWatcher,
        View.OnClickListener, SignInView {

    public static final String TAG = "SignInFragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sign_in_username_et)
    EditText signInUsernameEt;
    @BindView(R.id.sign_in_password_et)
    EditText signInPasswordEt;
    @BindView(R.id.sign_in_login_btn)
    Button signInLoginBtn;
    @BindView(R.id.sign_in_forgot_tv)
    TextView signInForgotTv;
    @BindColor(R.color.color_FFFFFFFF)
    int regButtonNoPress;
    @BindColor(R.color.color_4dffffff)
    int regButtonPress;

    @BindView(R.id.bt_anonymous_log_in)
    Button mBtAnonymousLogIn;

    @BindView(R.id.iv_wechat_login)
    ImageView mIvWechatLogIn;

    @Inject
    SignInPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_guide_sign_in;
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) {
                return "";
            } else {
                return null;
            }
        }
    };

    @Override
    public void showPosting() {
        showLoadingDialog();
    }

    @Override
    protected void setupView() {
        setupToolbar(" ", toolbar);
        AppComponent.getInstance().inject(this);
        setPresenter(presenter);
        presenter.attachView(this);
        presenter.setUsernameEditText();
        signInLoginBtn.setOnClickListener(this);
        signInPasswordEt.addTextChangedListener(this);
        signInUsernameEt.addTextChangedListener(this);
        signInForgotTv.setOnClickListener(this);
        signInUsernameEt.setFilters(new InputFilter[]{filter});

        mBtAnonymousLogIn.setOnClickListener(this);
        mIvWechatLogIn.setOnClickListener(this);
    }

    @Override
    protected void init() {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null != signInPasswordEt && null != signInUsernameEt) {
            boolean isUsernameEmpty = signInUsernameEt.getText().toString().isEmpty();
            boolean isPasswordLength = signInPasswordEt.getText().toString().length() > 5;

            // set button status
            if (isPasswordLength && (!isUsernameEmpty)) {
                signInLoginBtn.setTextColor(regButtonNoPress);
                signInLoginBtn.setEnabled(true);
            } else {
                signInLoginBtn.setTextColor(regButtonPress);
                signInLoginBtn.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_login_btn:
                presenter.login(signInUsernameEt.getText().toString(), signInPasswordEt.getText().toString());
                break;
            case R.id.sign_in_forgot_tv:
                showResetPassword();
                break;
            case R.id.iv_wechat_login:
                wechatLogIn();
                break;
            case R.id.bt_anonymous_log_in:
                presenter.anonymousLogIn();
                break;
            default:
                break;
        }
    }


    private void showResetPassword() {
        if (Utils.isChineseLanguage()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_reset_password, null);

            Button btnEmail = (Button) view.findViewById(R.id.btn_email);
            Button btnPhone = (Button) view.findViewById(R.id.btn_phone);

            builder.setView(view);
            final AlertDialog dialog = builder.show();

            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    replaceFragment(new FindPwdByEmailFragment(), FindPwdByEmailFragment.TAG, true);
                }
            });

            btnPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    replaceFragment(new FindPwdByPhoneFragment(), FindPwdByPhoneFragment.TAG, true);
                }
            });

        } else {
            replaceFragment(new FindPwdByEmailFragment(), FindPwdByEmailFragment.TAG, true);
        }

    }

    private void wechatLogIn() {
        if (!NetWorkUtil.isNetConnected()) {
            showNetworkUnavailable();
            return;
        }

        LoginManager.loginWithWeiXin(mActivity, new WeiXinLoginCallback() {
            @Override
            public void onSuccess(WeiXinUserInfo userInfo) {
                LogInBody body = new LogInBody();
                body.setType("wechat");
                body.setAccess_token(userInfo.getAccess_token());
                body.setOpenid(userInfo.getOpenid());
                body.setAvatar(userInfo.getHeadimgurl());
                body.setNickName(userInfo.getNickname());
                body.setGender(userInfo.getSex() == 1 ? Config.MALE : Config.FEMALE);
                body.setCountry(userInfo.getCountry());
                body.setProvince(userInfo.getProvince());
                body.setCity(userInfo.getCity());
                presenter.wechatLogIn(body);
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

    @Override
    public void showShortToast(String message) {
        Utils.toast(message);
    }

    @Override
    public void setUsernameEditText(String username) {
        signInUsernameEt.setText(username);
    }

    @Override
    public void showNetworkUnavailable() {
        showShortToast(R.string.network_error);
    }

    @Override
    public void showLogInSuccess(boolean setUserInfo) {
        dismissLoadingDialog();
        getActivity().finish();
        Intent intent = new Intent(App.getContext(), BandActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void showLogInFailed(int code) {
        dismissLoadingDialog();
        switch (code) {
            case 1008:
                showShortToast(R.string.user_no_found);
                break;
            case 1007:
                showShortToast(R.string.account_or_pwd_error);
                break;
            default:
                showShortToast(R.string.sign_in_error);
        }
    }

    @Override
    public void showLogInFailed(String object) {
        dismissLoadingDialog();
        showLongToast(getString(R.string.sign_in_error) + " cause by " + object);
    }
}
