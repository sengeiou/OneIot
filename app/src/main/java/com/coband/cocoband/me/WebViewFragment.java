package com.coband.cocoband.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.common.utils.C;
import com.coband.common.utils.DialogUtils;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import butterknife.BindView;

/**
 * Created by mai on 17-1-12.
 */

public class WebViewFragment extends BaseFragment {
    public final static String TAG = "WebViewFragment";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.web_view)
    WebView webView;

    private Bundle arguments;
    private GuideProgressDialog dialog;

    @Override
    public void onStart() {
        super.onStart();
        if (isAdded()) setupView();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_web_toolbar;
    }

    @Override
    public void setupView() {
        arguments = getArguments();
        setupToolbar(arguments.getString(C.WEB_NAME), toolbar);
    }

    @Override
    protected void init() {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (null == webView) {
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (null == webView) {
                    return false;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (null == dialog) {
                    dialog = DialogUtils.showGuideProgressDialogCanExit(getActivity(),
                            App.getContext().getString(R.string.loading));
                }
                if (newProgress == 100) {
                    dialog.cancel();
                }
                super.onProgressChanged(view, newProgress);
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (null == webView) {
                    return false;
                }
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                // 跳转到天猫APP
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                return true;
            }
        });

        if (Utils.isChineseLanguage()) {
            webView.loadUrl(arguments.getString(C.CHINA_WEB));
        } else {
            webView.loadUrl(arguments.getString(C.WORLD_WEB));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView = null;
    }
}
