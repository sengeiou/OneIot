package com.coband.cocoband;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.compat.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.coband.App;
import com.coband.common.utils.Config;
import com.coband.common.utils.NetWorkUtil;
import com.coband.watchassistant.R;
import com.meituan.android.walle.WalleChannelReader;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imco on 11/13/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    protected abstract int getLayout();

    protected abstract void init();

    protected abstract void setupView();


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(getLayout());
        mUnbinder = ButterKnife.bind(this);
        setupView();
        init();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onResume(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onPause(this);
        }
    }

    public void setupToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (title != null) {
            toolbar.setTitle(title);
        } else {
            toolbar.setTitle("");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public DrawerLayout getDrawerLayout() {
        return null;
    }

    public void replaceFragment(Fragment fragment, int contentId, boolean addToBackStack) {
        replaceFragment(fragment, contentId, addToBackStack, null);
    }

    public void replaceFragment(Fragment fragment, int contentId, boolean addToBackStack, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (null != bundle) {
            fragment.setArguments(bundle);
        }
        transaction.replace(contentId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
