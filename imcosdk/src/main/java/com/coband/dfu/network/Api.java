package com.coband.dfu.network;

import android.os.Build;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.CustomGsonConverterFactory;


/**
 * Created by mqh on 10/31/16.
 */

public class Api {
    private static final String BASE_URL = "https://fota.aimoketechnology.com/api/v2/";
    public Retrofit retrofit;
    public ApiService service;


    Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("charset", "utf-8")
                    .header("Connection", "close")
                    .build());
        }
    };

    //构造方法私有
    private Api() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        File cacheFile = new File(App.getInstance().getCacheDir(), "cache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            try {
                sslContext.init(null, null, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(7676, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(7676, TimeUnit.MILLISECONDS)
                    .addInterceptor(mInterceptor)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new HttpCacheInterceptor())
                    .build();
        } else {
            // 低于API 20+的版本是默认关闭对TLSv1.1和TLSv1.2的支持，若要支持则必须自己打开
            Tls12SocketFactory socketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(7676, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(7676, TimeUnit.MILLISECONDS)
                    .addInterceptor(mInterceptor)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new HttpCacheInterceptor())
                    .sslSocketFactory(socketFactory,new HttpsUtils.UnSafeTrustManager())
                    .build();
        }



//        // Json 解析定义
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//                .excludeFieldsWithoutExposeAnnotation() // 过滤掉 加了@Expose注释的字段
//                .serializeNulls().create();
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(new JsonQueryParameters.JsonStringConverterFactory(GsonConverterFactory.create()))
//                .addConverterFactory(ResponseConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(ApiService.class);

    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

    //获取单例
    static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
//            if (!NetWorkUtil.isNetConnected()) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//                Log.d("Okhttp", "no network");
//            }

            Response originalResponse = chain.proceed(request);
//            if (NetWorkUtil.isNetConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
//            } else {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
//                        .removeHeader("Pragma")
//                        .build();
//            }
        }
    }
}
