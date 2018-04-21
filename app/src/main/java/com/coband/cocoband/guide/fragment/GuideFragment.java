package com.coband.cocoband.guide.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.tools.AppComponent;
import com.coband.watchassistant.R;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseFragment implements /*GuideSignInAndSignUpView,*/
        View.OnClickListener /*SurfaceHolder.Callback*/ {
    public static final String TAG = "GuideFragment";

    @BindView(R.id.sign_in_tv)
    TextView signInTv;
    @BindView(R.id.sign_up_tv)
    TextView signUpTv;
    @BindColor(R.color.color_FFDEDEDE)
    int strokeColor;

    @Override
    protected int getLayout() {
        return R.layout.fragment_guide_sign_in_and_sign_up;
    }

    @Override
    protected void setupView() {
        AppComponent.getInstance().inject(this);
        signInTv.setOnClickListener(this);
        signUpTv.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.sign_in_tv:
                addFragment(new SignInFragment(), SignInFragment.TAG, true);
                break;
            //注册
            case R.id.sign_up_tv:
                addFragment(new SignUpFragment(), SignUpFragment.TAG, true);
                break;
            default:
                break;
        }
    }
}
