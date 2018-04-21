package com.coband.cocoband.guide;

import android.content.Intent;

import com.coband.cocoband.BaseActivity;
import com.coband.cocoband.guide.fragment.HelloFragment;
import com.coband.watchassistant.R;

public class    SplashActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void setupView() {
        replaceFragment(new HelloFragment(), R.id.content, false);
    }
}
