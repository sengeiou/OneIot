package com.coband.cocoband.me;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.guide.SplashActivity;
import com.coband.cocoband.me.viewholder.SettingsViewHolder;
import com.coband.cocoband.mvp.iview.SettingsView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.presenter.SettingsFragmentPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.common.utils.Config;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by mai on 17-5-12.
 */

public class SettingsFragment extends BaseListFragment implements SettingsView {
    public static final String TAG = "SettingsFragmentPresenter";
    List<String> strings;

    @Inject
    SettingsFragmentPresenter mPresenter;

    @Override
    public void setupView() {
        super.setupView();
        AppComponent.getInstance().inject(this);
        setPresenter(mPresenter);
        mPresenter.attachView(this);
    }

    @Override
    protected MultiItemAdapter setMultiItemAdapter() {
        return new MultiItemAdapter(SettingsViewHolder.class);
    }

    @Override
    protected void init() {
        mAdapter.setDataListAndNotify(strings);

    }

    @Override
    protected int setTitleRes() {
        return R.string.settings;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClick(MeItemClickEvent event) {
        TextView tvTitle = (TextView) event.view.findViewById(R.id.tv_title);
        String s = tvTitle.getText().toString();
        if (s.equals(getString(R.string.metric_system))) {
            if (DataManager.getInstance().getUnitSystem() != Config.METRIC) {
                mRecyclerView.setEnabled(false);
                mPresenter.setUnitSystem(Config.METRIC);
                mRecyclerView.setEnabled(true);
            }
        } else if (s.equals(getString(R.string.inch))) {
            if (DataManager.getInstance().getUnitSystem() == Config.METRIC) {
                mRecyclerView.setEnabled(false);
                mPresenter.setUnitSystem(Config.INCH);
                mRecyclerView.setEnabled(true);
            }
        } else if (s.equals(getString(R.string.wechat))) {
            addFragment(WechatFragment.newInstance(), WebViewFragment.TAG);
        } else if (s.equals(getString(R.string.account_and_security))) {
            addFragment(new AccountFragment(), AccountFragment.TAG);
        } else if (s.equals(getString(R.string.fragment_help_title))) {
            addFragment(new DeviceUseHelpFragment(), DeviceUseHelpFragment.TAG, true);
//            addFragment(new UseHelpFragment(), UseHelpFragment.TAG);
        } else if (s.equals(getString(R.string.feedback))) {
            FeedbackAgent agent = new FeedbackAgent(mActivity);
            agent.startDefaultThreadActivity();
        } else if (s.equals(getString(R.string.about_imco))) {
            addFragment(new AboutFragment(), AboutFragment.TAG);
        } else if (s.equals(getString(R.string.exit))) {
            showExitDialog();
        } else if (s.equals(getString(R.string.clear_cache))) {
            showDialog(mActivity, mActivity.getString(R.string.clear_cache),
                    mActivity.getString(R.string.clear_cache_tips),
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPresenter.clearCache();
                        }
                    });
        }
    }

    private void showExitDialog() {
        showDialog(mActivity, getString(R.string.exit), getString(R.string.exit_tips),
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.exit();
                    }
                });
    }

    @Override
    public void fragmentExit() {
        Intent intent = new Intent(mActivity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void showSyncing() {
        showShortToast(R.string.syncing);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void showUpdateAccountInfoSuccess(int type) {
        dismissLoadingDialog();
        mAdapter.notifyItemChanged(1);
        mAdapter.notifyItemChanged(2);
    }

    @Override
    public void showUpdateAccountInfoFailed(int type, int code) {
        dismissLoadingDialog();
        switch (type) {
            case HandleEvent.UPDATE_UNIT_SYSTEM_FAILED:
                showShortToast(getString(R.string.update_unit_system_failed));
                break;
        }
    }

    @Override
    public void showUpdateAccountInfoFailed(int type, String errorMsg) {
        switch (type) {
            case HandleEvent.UPDATE_UNIT_SYSTEM_FAILED:
                showShortToast(getString(R.string.update_unit_system_failed) + " cause by " + errorMsg);
                break;
        }
    }

    @Override
    public void showDeviceDisconnected() {
        dismissLoadingDialog();
    }
}
