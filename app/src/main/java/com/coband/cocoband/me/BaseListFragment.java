package com.coband.cocoband.me;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by mai on 17-4-25.
 */

public abstract class BaseListFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected MultiItemAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_head;
    }


    public void setupView() {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        mSwipeRefreshLayout = new SwipeRefreshLayout(mActivity);
        mRecyclerView = new RecyclerView(mActivity);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mSwipeRefreshLayout.addView(mRecyclerView);
        ((CoordinatorLayout) getRootView()).addView(mSwipeRefreshLayout, layoutParams);
        mSwipeRefreshLayout.setEnabled(false);

        setupToolbar(setTitleRes(), toolbar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mAdapter = setMultiItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    // if this method delete, will throw no @Subscribe Exception
    @Subscribe
    public void onEvent(HandleEvent event) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        mRecyclerView = null;
        EventBus.getDefault().unregister(this);
    }

    protected abstract MultiItemAdapter setMultiItemAdapter();

    @StringRes
    protected abstract int setTitleRes();
}
