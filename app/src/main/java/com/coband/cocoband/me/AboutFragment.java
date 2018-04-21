package com.coband.cocoband.me;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.common.utils.C;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */

public class AboutFragment extends BaseFragment implements View.OnClickListener {

    public final static String TAG = "AboutFragment";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.about_version_text)
    TextView versionText;
    @BindView(R.id.about_version_ll)
    LinearLayout aboutVersion;
    @BindView(R.id.about_update_ll)
    LinearLayout aboutUpdate;
    @BindView(R.id.about_jump_ll)
    LinearLayout aboutMarket;
    @BindView(R.id.about_agreement_ll)
    LinearLayout aboutAgreement;
    @BindView(R.id.about_root_ll)
    LinearLayout aboutRootView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected void init() {
        aboutVersion.setOnClickListener(this);
        aboutUpdate.setOnClickListener(this);
        aboutMarket.setOnClickListener(this);
        aboutAgreement.setOnClickListener(this);
    }

    @Override
    protected void setupView() {
        setupToolbar(getString(R.string.about_imco), toolbar);
        versionText.setText(getString(R.string.version_text) + Utils.getAppVersion(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_version_ll:
//                addFragment(new OwlFragment(), OwlFragment.TAG);
                break;
            case R.id.about_update_ll:
                break;
            case R.id.about_jump_ll:
                openMarket();
                break;
            case R.id.about_agreement_ll:
                Bundle bundle = new Bundle();
                bundle.putString(C.CHINA_WEB, "http://www.priodigit.com/a/guanyu/36.html?1516119758");
                bundle.putString(C.WORLD_WEB, "http://www.priodigit.com/a/guanyu/36.html?1516119758");
                bundle.putString(C.WEB_NAME, getString(R.string.band_agreement_privacy));
                addFragment(new WebViewFragment(), WebViewFragment.TAG, true, bundle);
                break;
            default:
                break;
        }
    }

    private void openMarket() {
        Uri uri = Uri.parse("market://details?id=" + App.getContext().getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!TextUtils.isEmpty("")) {
            intent.setPackage("");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(intent);
    }
}
