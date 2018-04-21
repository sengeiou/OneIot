package com.coband.cocoband.guide.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.mvp.iview.FindPwdByPhoneView;
import com.coband.cocoband.mvp.presenter.FindPwdByPhonePresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.watchassistant.R;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by xing on 10/4/2018.
 */

public class FindPwdByPhoneFragment extends BaseFragment implements FindPwdByPhoneView {
    public static final String TAG = "FindPwdByPhoneFragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_phone)
    EditText mEtPhone;

    @BindView(R.id.et_verification_code)
    EditText mEtVerifyCode;

    @BindView(R.id.et_pwd)
    EditText mEtPwd;

    @BindView(R.id.et_confirm_pwd)
    EditText mEtConfirmPwd;

    @BindView(R.id.bt_send_verification_code)
    Button mBtSendVerifyCode;

    @BindView(R.id.btn_reset)
    Button mBtnReset;

    @Inject
    FindPwdByPhonePresenter mPresenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_reset_password_by_phone;
    }

    @Override
    protected void init() {
        AppComponent.getInstance().inject(this);
        mPresenter.attachView(this);
        setPresenter(mPresenter);
    }

    @Override
    protected void setupView() {
        setupToolbar(getString(R.string.forgot_password), toolbar);
        mBtSendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestVerifyCode();
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestResetPwd();
            }
        });
    }


    private void requestVerifyCode() {
        String phone = mEtPhone.getText().toString();
        if(phone.isEmpty()) {
            showShortToast(R.string.phone_empty);
            return;
        }

        if(phone.length() != 11 || !phone.startsWith("1")) {
            showShortToast(R.string.phone_pattern_error);
            return;
        }

        mPresenter.requestVerifyCode(phone);
    }

    private void requestResetPwd() {
        String phoneNumber = mEtPhone.getText().toString();
        if (phoneNumber.isEmpty()) {
            showShortToast(R.string.phone_empty);
            return;
        }

        if (phoneNumber.length() != 11 || !phoneNumber.startsWith("1")) {
            showShortToast(R.string.phone_pattern_error);
            return;
        }

        String verificationCode = mEtVerifyCode.getText().toString();
        if (verificationCode.isEmpty()) {
            showShortToast(R.string.verification_code_empty);
            return;
        }

        String pwd = mEtPwd.getText().toString();
        String confirmPwd = mEtConfirmPwd.getText().toString();

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

        mPresenter.requestResetPwd(phoneNumber, verificationCode, pwd);

    }


    @Override
    public void showNetworkUnavailable() {
        showShortToast(R.string.network_error);
    }

    @Override
    public void showResetPasswordSuccess() {
        dismissLoadingDialog();
        showShortToast(R.string.reset_pwd_success);
    }

    @Override
    public void showResetPasswordFailed(int code) {
        dismissLoadingDialog();
        switch (code) {
            default:
                showLongToast(getString(R.string.reset_pwd_failed) + " >>>>>> " + code);
                break;
        }
    }

    @Override
    public void showResetPasswordFailed(String errorMsg) {
        dismissLoadingDialog();
        showLongToast(getString(R.string.reset_pwd_failed) + " cause by " + errorMsg);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void showRequestVerifyCodeSuccess() {
        showShortToast(R.string.verify_code_already_send);
        dismissLoadingDialog();
    }

    @Override
    public void showRequestVerifyCodeFailed(int code) {
        dismissLoadingDialog();
        switch (code) {
            case 9000:
                showLongToast(R.string.not_support_feature_yet);
                break;
            case 6000:
                showLongToast(getString(R.string.request_verify_code_failed) + " >>>>>> " + code);
                break;
            default:
                showLongToast(getString(R.string.request_verify_code_failed) + " code >>>>>> " + code);
                break;
        }
    }

    @Override
    public void showRequestVerifyCodeFailed(String errorMsg) {
        dismissLoadingDialog();
        showLongToast(getString(R.string.request_verify_code_failed) + " cause by " + errorMsg);
    }


}
