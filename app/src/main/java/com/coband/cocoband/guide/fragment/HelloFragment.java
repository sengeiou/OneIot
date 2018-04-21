package com.coband.cocoband.guide.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.main.BandActivity;
import com.coband.cocoband.mvp.iview.GuideHelloView;
import com.coband.cocoband.mvp.presenter.GuideHelloPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.watchassistant.R;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelloFragment extends BaseFragment implements GuideHelloView {

    public static final String TAG = "HelloFragment";
    @Inject
    GuideHelloPresenter mPresenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_guide_hello;
    }

    @Override
    protected void setupView() {
        AppComponent.getInstance().inject(this);
        setPresenter(mPresenter);
        mPresenter.attachView(this);
    }

    @Override
    protected void init() {
        mPresenter.autoLogin();
    }


    @Override
    public void showLoginFail() {
        replaceFragment(new GuideFragment(),
                GuideFragment.TAG, false);
    }

    @Override
    public void showLoginSuccess() {
        getActivity().finish();

        Intent intent = new Intent(mActivity, BandActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showCustomToast(String message) {
        showShortToast(message);
    }
}
