package com.coband.cocoband.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.coband.App;
import com.coband.cocoband.BaseLazyFragment;
import com.coband.cocoband.event.me.MeMenuClickEvent;
import com.coband.cocoband.me.viewholder.MeViewHolder;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by ivan on 17-4-11.
 */

public class MeFragment extends BaseLazyFragment {

    private static final String TAG = "MeFragment";
    @BindString(R.string.friend)
    String mFriend;
    @BindString(R.string.medal)
    String mMedal;
    @BindString(R.string.leaderboard)
    String mLeaderboard;
    @BindString(R.string.band_target)
    String mTarget;
    @BindString(R.string.settings)
    String mSettings;
    @BindView(R.id.rv)
    RecyclerView mRefreshView;
    private MultiItemAdapter adapter;


    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.recyclerview;
    }

    @Override
    protected void setupView() {
        EventBus.getDefault().register(this);
        adapter = new MultiItemAdapter(MeViewHolder.class);
        mRefreshView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.me_fragment));
        adapter.setDataList(strings);
        mRefreshView.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {
        Logger.d(TAG, "lazyLoad >>>>> ");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClick(MeMenuClickEvent event) {
        int position = event.position;
        if (position == 0) {
            addFragment(new UserInfoFragment(), UserInfoFragment.TAG);
        } else {
            TextView textTitle = (TextView) event.view.findViewById(R.id.tv_title);
            String title = textTitle.getText().toString();
            if (title.equals(mSettings)) {
                addFragment(new SettingsFragment(), SettingsFragment.TAG);
            } else if (title.equals(getString(R.string.feedback))) {
                FeedbackAgent agent = new FeedbackAgent(mActivity);
                agent.startDefaultThreadActivity();
            } else if (title.equals(getString(R.string.fragment_help_title))) {
                addFragment(new DeviceUseHelpFragment(), DeviceUseHelpFragment.TAG, true);
//            addFragment(new UseHelpFragment(), UseHelpFragment.TAG);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onFragmentResume() {
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }
}
