package com.coband.cocoband.mvp.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.WechatView;
import com.coband.cocoband.mvp.model.bean.UploadAddressJson;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.network.GsonRequest;
import com.coband.common.utils.Config;
import com.coband.common.utils.Logger;
import com.coband.common.utils.NetWorkUtil;
import com.coband.common.utils.QRCodeUtil;
import com.coband.watchassistant.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by ivan on 17-6-29.
 */

@Module
public class WechatPresenter extends BasePresenter {

    private static final String TAG = "WechatPresenter";
    private WechatView mView;

    private static final String SUCCESS = "0";
    private String mAddress;

    @Inject
    public WechatPresenter() {
    }

    @Override
    public void attachView(BaseView view) {
        mView = (WechatView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    private Bitmap obtainCacheQrcode(String address) {
        File file = new File(getQRCodeFilePath(address));
        if (file.exists()) {
            return BitmapFactory.decodeFile(getQRCodeFilePath(address));
        }

        return null;
    }

    private String getQRCodeFilePath(String address) {
        String root = Environment.getExternalStorageDirectory().toString();
        return root + "/WatchManager/qrcode" + address + ".jpg";
    }


    public void obtainQrcode(final Activity activity) {
        String address = getBluetoothAddress();

        if (address == null) {
            mView.showNoDeviceAddress();
            return;
        }


        Bitmap cache = obtainCacheQrcode(address);
        if (cache != null && mView != null) {
            mView.showQrcode(cache);
            return;
        }

        if (!NetWorkUtil.isNetConnected()) {
            mView.showNetworkUnavailable();
            return;
        }


        mView.showAddressUploading();
        mAddress = address;
        uploadAddress(address, activity);
    }

    private void uploadAddress(final String address, final Activity activity) {
        final String url = Config.UPLOAD_ADDRESS_URL +
                "macAddress=" + address + "&productId=44728";

        final RequestQueue queue = Volley.newRequestQueue(activity);

        GsonRequest<UploadAddressJson> request = new GsonRequest<>(url, UploadAddressJson.class,
                null, new Response.Listener<UploadAddressJson>() {
            @Override
            public void onResponse(UploadAddressJson response) {
                if (response == null && mView != null) {
                    mView.showObtainQrcodeError();
                    return;
                }

                Bitmap bitmap = handleResponse(response, activity);

                if (mView != null) {
                    if (bitmap == null) {
                        mView.showObtainQrcodeError();
                    } else {
                        mView.showQrcode(bitmap);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Cache.Entry cache = queue.getCache().get(url);
                if (cache != null) {
                    Gson gson = new Gson();
                    UploadAddressJson json = gson.fromJson(new String(cache.data), UploadAddressJson.class);
                    Bitmap bitmap = handleResponse(json, activity);
                    if (mView != null) {
                        if (bitmap == null) {
                            mView.showObtainQrcodeError();
                        } else {
                            mView.showQrcode(bitmap);
                        }
                    }
                } else {
                    if (mView != null) {
                        mView.showObtainQrcodeError();
                    }
                }

            }
        });

        queue.add(request);
    }

    private Bitmap handleResponse(UploadAddressJson response, Activity activity) {
        if (!response.getResp_msg().getRet_code().equals(SUCCESS)) {
            return null;
        }

        return generateQRCode(response.getQrticket(), BitmapFactory.decodeResource(
                activity.getResources(), R.drawable.coband_qrcode_icon));

    }

    private String getBluetoothAddress() {
        String address = PreferencesHelper.getConnectedDeviceAddress();
        if (address != null) {
            address = address.replace(":", "");
            return address;
        }

        return null;
    }

    private Bitmap generateQRCode(String serialNumber, Bitmap logo) {
        if (serialNumber == null) {
            return null;
        }
        return QRCodeUtil.createQRImage(serialNumber, 480, 480, logo);
    }

    public void saveQRCode(Bitmap bitmap, Activity activity) {
        String filePath = getQRCodeFilePath(mAddress);
        File file = new File(filePath);
        if (file.exists()) {
            if (mView != null) {
                mView.showSaveQrcodeSuccess();
                return;
            }
        }

        File dir = file.getParentFile();
        Logger.d(TAG, "dir >>>> " + dir.getAbsolutePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                    file.getAbsolutePath(), mAddress + ".jpg", null);
            if (mView != null) {
                mView.showSaveQrcodeSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, "error msg >>>> " + e.getMessage());
            if (mView != null) {
                mView.showSaveQrcodeFailed();
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
