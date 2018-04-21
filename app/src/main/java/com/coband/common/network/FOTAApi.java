package com.coband.common.network;

import com.coband.App;
import com.coband.common.utils.FOTAConfig;
import com.coband.common.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ivan on 17-8-14.
 */

public class FOTAApi {

    public Retrofit retrofit;
    public ApiService service;


    private Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder()
//                    .addHeader("X-LC-Id", FOTAConfig.K9_APP_KEY)
//                    .addHeader("X-LC-Key", FOTAConfig.K9_APP_SECRET)
                    .addHeader("Content-Type", "application/json")
                    .header("Connection", "close")
                    .build());
        }
    };

    //构造方法私有
    private FOTAApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File cacheFile = new File(App.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .addInterceptor(mInterceptor)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new FOTAApi.HttpCacheInterceptor())
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(new JsonQueryParameters.JsonStringConverterFactory(GsonConverterFactory.create()))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(FOTAConfig.BASE_URL)
                .build();


        service = retrofit.create(ApiService.class);

    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final FOTAApi INSTANCE = new FOTAApi();
    }

    //获取单例
    public static FOTAApi getInstance() {
        return FOTAApi.SingletonHolder.INSTANCE;
    }

    private class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}
