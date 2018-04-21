package com.coband.cocoband.me;


import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.coband.cocoband.BaseFragment;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwlFragment extends BaseFragment {
    public static final String TAG = "OwlFragment";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.owl_version)
    TextView versionText;

    @Override
    protected int getLayout() {
        return R.layout.fragment_owl;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setupView() {
        setupToolbar(getString(R.string.version_num), toolbar);
        versionText.setText(getString(R.string.current_version) + "V" +
                Utils.getAppVersion(getActivity()));
    }
}
