package com.coband.cocoband;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.coband.cocoband.me.BaseListFragment;

/**
 * Created by tgc on 17-5-31.
 */

public abstract class BaseResumeListFragment extends BaseListFragment {

    protected abstract void onFragmentResume();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BaseActivity activity = (BaseActivity) context;
        activity.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        onFragmentResume();
                    }
                });
    }
}
