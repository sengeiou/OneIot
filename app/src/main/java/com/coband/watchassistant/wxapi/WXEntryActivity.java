package com.coband.watchassistant.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.coband.common.network.NetworkConfig;
import com.coband.common.utils.Logger;
import com.owen.library.sharelib.activity.WeiXinHandlerActivity;

/**
 * Created by ivan on 3/11/18.
 */

public class WXEntryActivity extends WeiXinHandlerActivity/*Activity implements IWXAPIEventHandler */ {
//    private static final String TAG = "WXEntryActivity";
//    private IWXAPI mApi;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mApi = WXAPIFactory.createWXAPI(this, NetworkConfig.Wechat_AppID, true);
//        mApi.registerApp(NetworkConfig.Wechat_AppID);
//        mApi.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        mApi.handleIntent(intent, this);
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        Logger.d(TAG, "error code >>>>>> " + baseResp.errCode);
//    }
}
