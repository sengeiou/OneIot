package com.coband.cocoband.guide.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.mvp.iview.GuideForgotPasswordView;
import com.coband.cocoband.mvp.presenter.ForgotPasswordPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.common.utils.DialogUtils;
import com.coband.common.utils.MatchMailAddress;
import com.coband.common.utils.NetWorkUtil;
import com.coband.watchassistant.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindPwdByEmailFragment extends BaseFragment implements View.OnClickListener,
        TextWatcher, GuideForgotPasswordView {

    public static final String TAG = "FindPwdByEmailFragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.forgot_title_tv)
    TextView forgotTitleTv;
    @BindView(R.id.forgot_email_et)
    EditText forgotEmailEt;
    @BindView(R.id.forgot_send_mail_btn)
    Button forgotSendMailBtn;
    @BindColor(R.color.color_FFFFFFFF)
    int regButtonNoPress;
    @BindColor(R.color.color_4dffffff)
    int regButtonPress;
    @Inject
    ForgotPasswordPresenter presenter;

    private GuideProgressDialog dialog;

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
    protected int getLayout() {
        return R.layout.fragment_guide_forgot_password;
    }

    @Override
    protected void init() {
        forgotTitleTv.setText(getString(R.string.send_email_tips));
        forgotSendMailBtn.setOnClickListener(this);
        forgotEmailEt.addTextChangedListener(this);
        forgotEmailEt.setFilters(new InputFilter[]{filter});
    }

    @Override
    protected void setupView() {
        AppComponent.getInstance().inject(this);
        setPresenter(presenter);
        presenter.attachView(this);
        setupToolbar(getString(R.string.forgot_password), toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_send_mail_btn:
                String mailAddress = forgotEmailEt.getText().toString();
                if (MatchMailAddress.match(mailAddress)) {
                    presenter.sendResetEmail(mailAddress);
                } else {
                    forgotEmailEt.requestFocus();
                    forgotEmailEt.setError(getString(R.string.email_format_error_tips));
                    Observable.timer(2500, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    if (null != forgotEmailEt) {
                                        forgotEmailEt.setError(null);
                                    }
                                }
                            });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null != forgotEmailEt) {
            if (!forgotEmailEt.getText().toString().isEmpty()) {
                forgotSendMailBtn.setTextColor(regButtonNoPress);
            } else {
                forgotSendMailBtn.setTextColor(regButtonPress);
            }
        }
    }

    @Override
    public void showNetworkUnavailable() {
        showShortToast(R.string.network_error);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void showResetPwdSuccess() {
        dismissLoadingDialog();
        showLongToast(R.string.email_already_send);
    }

    @Override
    public void showResetPwdFailed(int code) {
        dismissLoadingDialog();
        switch (code) {
            case 1008:
                showLongToast(R.string.user_no_found);
                break;
            case 1014:
                showLongToast(R.string.email_not_verify);
                break;
            default:
                showShortToast(getString(R.string.reset_pwd_failed) + " >>>>>> " + code);
                break;
        }
    }

    @Override
    public void showResetPwdFailed(String errorMsg) {
        dismissLoadingDialog();
        showLongToast(errorMsg);
    }



}
