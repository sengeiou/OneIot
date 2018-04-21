package com.coband.common.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.coband.cocoband.mvp.model.bean.ErrorResponse;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by tgc on 17-6-2.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "ResponseBodyConverter";
    private final TypeAdapter type;
    private final Gson gson;

    public GsonResponseBodyConverter(Gson gson, TypeAdapter type) {
        this.type = type;
        this.gson = gson;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.e(TAG, "convert: " + response);
        Response httpResult = gson.fromJson(response, Response.class);
        if (httpResult.code() == 200) {
            //200的时候就直接解析，不可能出现解析异常。因为我们实体基类中传入的泛型，就是数据成功时候的格式
//            return gson.fromJson(response, type);
            return null;
        } else {
            ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
            throw new ResultException(errorResponse.getCode());
        }
    }
}
