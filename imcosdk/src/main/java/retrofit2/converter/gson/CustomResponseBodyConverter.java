package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.coband.dfu.network.bean.FwResultBean;
import com.coband.utils.LogUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 因为后台在没有更新和有更新时返回的json格式不一致，这里自定义一个Converter对特定的json进行解析
 * Created by mai on 17-7-14.
 */

public class CustomResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "CustomResponseBodyConverter";
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Type mType;

    CustomResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
        this.gson = gson;
        this.adapter = adapter;
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String body = value.string();
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("code");
        int code = 0;
        if (jsonPrimitive != null) {
            code = jsonPrimitive.getAsInt();
            if (code == 0) {
                // Have new firmware version
                FwResultBean bean = (FwResultBean) adapter.fromJson(body);
                String url = bean.payload.resourceUrl;
                String fwType = bean.payload.fwType;
                String md5sum = bean.payload.md5sum;
                String sha1sum = bean.payload.sha1sum;
                String sha256sum = bean.payload.sha256sum;
                String version = bean.payload.version;
                String description = bean.payload.description;

                FwResultBean.ExtraBean extra = bean.payload.extra;
                String fileName = extra.fileName;
                String fileSize = extra.fileSize;
                LogUtils.d(TAG, "url >>>>>>>> " + url);
                LogUtils.d(TAG, "fwType >>>>>>>> " + fwType);
                LogUtils.d(TAG, "description >>>>>>>> " + description);
                LogUtils.d(TAG, "version >>>>>>>> " + version);
                LogUtils.d(TAG, "sha256sum >>>>>>>> " + sha256sum);
                LogUtils.d(TAG, "sha1sum >>>>>>>> " + sha1sum);
                LogUtils.d(TAG, "md5sum >>>>>>>> " + md5sum);
                LogUtils.d(TAG, "fileName >>>>>>>> " + fileName);
                LogUtils.d(TAG, "fileSize >>>>>>>> " + fileSize);
                return adapter.fromJson(body);
            } else {
                // not new firmware version
                LogUtils.d("T t = null;");
                T t = null;
                try {
                    // 通过反射获取泛型的实例对象
                    Class<T> clazz = (Class<T>) mType;
                    t = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((FwResultBean) t).code = code;
                ((FwResultBean) t).errorStr = jsonObject.getAsJsonPrimitive("payload").getAsString();
                return t;
            }
        } else {
            return adapter.fromJson(body);
        }
    }

//        String json = value.string();
//        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
//        // 解析code
//        JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("code");
//        int code = 0;
//        if (jsonPrimitive != null) {
//            code = jsonPrimitive.getAsInt();
//        }
//
//        T t = null;
//        try {
//            // 通过反射获取泛型的实例对象
//
//            t = (T) t.getClass().newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (code == 1) {
//            t.errorStr = gson.fromJson(jsonObject.getAsJsonArray("payload"), String.class);
//        } else {
//            return gson.fromJson(json, type);
//        }
//        return t;
//    }
}
