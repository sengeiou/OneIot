package com.coband.cocoband;

import android.support.v4.app.Fragment;

import com.coband.App;
import com.coband.common.network.NetworkConfig;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * Created by ivan on 3/11/18.
 */

public class ThirdPartyLogin {

    public static void qqLogIn(Fragment fragment, IUiListener listener) {
        Tencent tencent = Tencent.createInstance(NetworkConfig.QQ_AppKEY, App.getInstance());
        if (!tencent.isSessionValid()) {
            tencent.login(fragment, "all", listener);
        }
    }

//    public static boolean wxLogIn() {
//        if (!App.mWxApi.isWXAppInstalled()) {
//            return false;
//        }
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "coband_wx_login";
//        App.mWxApi.sendReq(req);
//        return true;
//    }
}
