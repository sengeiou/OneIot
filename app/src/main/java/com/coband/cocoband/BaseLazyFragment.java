package com.coband.cocoband;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.coband.common.utils.Logger;

/**
 * Created by ivan on 17-4-11.
 */

public abstract class BaseLazyFragment extends BaseFragment {
    private static final String TAG = "BaseLazyFragment --->";

    private boolean mInit;
    private boolean mLoad;

    protected abstract void onFragmentResume();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getUserVisibleHint()) {
                            Logger.d(BaseLazyFragment.this, "onBackStackChanged >>>");
                            onFragmentResume();
                        }
                    }
                });
    }

    @Override
    protected void init() {
        Logger.d(this, "call init >>>>");
        mInit = true;
        isCanLoadData();
    }

    private void isCanLoadData() {
        Logger.d(TAG, "init >>>>> " + mInit + " visible >>>> " + getUserVisibleHint());
        if (!mInit) {
            return;
        }

        if (getUserVisibleHint()) {
            Logger.d(TAG, "init >>>>> " + mInit + " visible >>>> " + getUserVisibleHint());
            lazyLoad();
            mLoad = true;
        } else {
            if (mLoad) {
                stopLoad();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInit = false;
        mLoad = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    protected abstract void lazyLoad();

    /**
     * you can over write this method while need to stop load something(e.g stop load data thread)
     * when the fragment invisible.
     */
    public void stopLoad() {
    }
}
