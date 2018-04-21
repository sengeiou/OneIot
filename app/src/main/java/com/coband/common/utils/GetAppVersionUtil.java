package com.coband.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by tgc on 17-5-20.
 */

public class GetAppVersionUtil {

    public static String getVersion(Context context){
        PackageInfo packageInfo;
        PackageManager pm = context.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
