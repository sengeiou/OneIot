package com.coband.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Created by rain1_wen on 2017/5/16.
 */

public class ImeiHelper {
    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();

        if (TextUtils.isEmpty(imei)) {
            imei = android.os.Build.SERIAL;
        }

        if (TextUtils.isEmpty(imei)) {
            imei = android.os.Build.HARDWARE;
        }
        return imei == null ? "" : imei;
    }

    // The same to android.os.Build.SERIAL
    private static String getSerialNumber() {

        String serial = null;

        try {

            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);

            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }
}
