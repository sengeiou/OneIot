package com.coband.cocoband;

import android.content.Context;
import android.support.v4.app.FragmentManager;

/**
 * Created by tgc on 17-5-25.
 */

public abstract class BaseResumeFragment extends BaseFragment {

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
