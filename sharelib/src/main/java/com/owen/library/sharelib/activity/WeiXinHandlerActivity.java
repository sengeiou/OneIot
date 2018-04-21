package com.owen.library.sharelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.owen.library.sharelib.ShareBlock;
import com.owen.library.sharelib.api.WeiXinApiService;
import com.owen.library.sharelib.callback.ShareCallback;
import com.owen.library.sharelib.callback.WeiXinLoginCallback;
import com.owen.library.sharelib.model.WeiXinAccessToken;
import com.owen.library.sharelib.model.WeiXinUserInfo;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeiXinHandlerActivity extends Activity implements IWXAPIEventHandler {

    public static ShareCallback sShareCallback;
    public static WeiXinLoginCallback sLoginCallback;
    private IWXAPI mApi;
    public static String accessToken, openID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = WXAPIFactory.createWXAPI(this, ShareBlock.getShareBlockConfig().getWeiXinAppId(), true);
        mApi.registerApp(ShareBlock.getShareBlockConfig().getWeiXinAppId());
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    public void onReq(BaseReq req) {
    }

    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            // 分享
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                if (sShareCallback != null) {
                    sShareCallback.onSuccess();
                }
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                if (sShareCallback != null) {
                    sShareCallback.onCancel();
                }
            } else {
                if (sShareCallback != null) {
                    sShareCallback.onError(resp.errCode, resp.errStr);
                }
            }
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            // 登录
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                SendAuth.Resp newResp = (SendAuth.Resp) resp;
                String code = newResp.code;


                final WeiXinApiService weiXinApiService = getWeiXinApiService();
                weiXinApiService.getAccessToken(ShareBlock.getShareBlockConfig().getWeiXinAppId(),
                        ShareBlock.getShareBlockConfig().getWeiXinSecret(), code, "authorization_code")
                        .flatMap(new Function<WeiXinAccessToken, Observable<WeiXinUserInfo>>() {
                            @Override
                            public Observable<WeiXinUserInfo> apply(WeiXinAccessToken weiXinAccessToken) throws Exception {
                                accessToken = weiXinAccessToken.getAccess_token();
                                return weiXinApiService
                                        .getWechatUserInfo(weiXinAccessToken.getAccess_token(),
                                                weiXinAccessToken.getOpenid());
                            }

                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeiXinUserInfo>() {
                            @Override
                            public void accept(WeiXinUserInfo weiXinUserInfo) throws Exception {
                                if (sLoginCallback != null) {
                                    weiXinUserInfo.setAccess_token(accessToken);
                                    sLoginCallback.onSuccess(weiXinUserInfo);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (sLoginCallback != null) {
                                    sLoginCallback.onError();
                                }
                            }
                        });
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                if (sLoginCallback != null) {
                    sLoginCallback.onCancel();
                }
            } else {
                if (sLoginCallback != null) {
                    sLoginCallback.onError();
                }
            }
        }

        finish();
    }

    private WeiXinApiService getWeiXinApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        final long DEFAULT_TIMEOUT = 30;
        OkHttpClient client = httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WeiXinApiService.class);
    }
}
