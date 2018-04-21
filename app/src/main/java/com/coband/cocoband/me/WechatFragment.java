package com.coband.cocoband.me;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.mvp.iview.WechatView;
import com.coband.cocoband.mvp.presenter.WechatPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.watchassistant.R;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by imco on 17-3-27.
 */
@RuntimePermissions
public class WechatFragment extends BaseFragment implements WechatView {

    private static final String TAG = "WechatFragment";

    private Bitmap mQrcodeBitmap;

    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    @BindString(R.string.fragment_wechat_title)
    String fragmentTitle;

    @OnClick(R.id.btn_save_qrcode)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_qrcode:
                mPresenter.saveQRCode(mQrcodeBitmap, getActivity());
                break;
        }
    }

    @BindView(R.id.btn_save_qrcode)
    Button mBtnSaveQRCode;

    @BindView(R.id.progress_upload_address)
    MaterialProgressBar mProgressBar;

    @BindString(R.string.fragment_wechat_title)
    String toolbarTitle;

    @Inject
    WechatPresenter mPresenter;

    public static WechatFragment newInstance() {
        WechatFragment fragment = new WechatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_wechat;
    }

    @Override
    protected void init() {
        AppComponent.getInstance().inject(this);
        mPresenter.attachView(this);
        setPresenter(mPresenter);
        WechatFragmentPermissionsDispatcher.realObtainQrcodeWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WechatFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void realObtainQrcode() {
        mPresenter.obtainQrcode(getActivity());
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForWriteExternalStorage(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(R.string.permission_write_external_storage, request);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onWriteExternalStorageDeny() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        showShortToast(R.string.no_write_external_storage_permission);
    }

    @Override
    protected void setupView() {
        setupToolbar(mToolbar, toolbarTitle);
        mBtnSaveQRCode.setVisibility(View.GONE);
    }

    @Override
    public void showNetworkUnavailable() {
        showShortToast(R.string.network_error);
    }

    @Override
    public void showDeviceDisconnected() {
        showShortToast(R.string.please_connect_bracelet);
    }

    @Override
    public void showNoDeviceAddress() {
        showShortToast(R.string.get_device_address_failed);
    }

    @Override
    public void showAddressUploading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showQrcode(Bitmap bitmap) {
        mQrcodeBitmap = bitmap;
        mProgressBar.setVisibility(View.GONE);
        mIvQrcode.setImageBitmap(bitmap);
        mBtnSaveQRCode.setVisibility(View.VISIBLE);
    }

    @Override
    public void showObtainQrcodeError() {
        mProgressBar.setVisibility(View.GONE);
        showShortToast(getString(R.string.get_serail_number_error));
    }

    @Override
    public void showSaveQrcodeSuccess() {
        showShortToast(R.string.save_qrcode_success);
    }

    @Override
    public void showSaveQrcodeFailed() {
        showShortToast(R.string.save_qrcode_failed);
    }
}


