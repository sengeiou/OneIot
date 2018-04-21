package com.coband.cocoband.mvp.model.local.db;

import com.coband.App;
import com.coband.watchassistant.PushApp;
import com.coband.watchassistant.PushAppDao;

import java.util.List;

/**
 * Created by ivan on 17-5-27.
 */

public class PushAppDBService {

    public static List<PushApp> getAllEnablePushApp() {
        return App.getPushAppDao().loadAll();
    }

    static boolean isPackageEnabledPush(String packageName) {
        List<PushApp> pushApps = App.getPushAppDao().queryBuilder()
                .where(PushAppDao.Properties.PackageName.eq(packageName))
                .list();

        if (pushApps.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    static void addRemindPackage(String packageName, String appName) {
        PushApp app = new PushApp();
        app.setPackageName(packageName);
        app.setAppName(appName);
        App.getPushAppDao().insert(app);
    }

    static void removeRemindPackage(String packageName) {
        List<PushApp> pushApps = App.getPushAppDao().queryBuilder()
                .where(PushAppDao.Properties.PackageName.eq(packageName))
                .list();

        if (!pushApps.isEmpty()) {
            App.getPushAppDao().delete(pushApps.get(0));
        }
    }

    public static String getAppName(String packageName) {
        List<PushApp> pushApps = App.getPushAppDao().queryBuilder()
                .where(PushAppDao.Properties.PackageName.eq(packageName))
                .list();

        if (pushApps.isEmpty()) {
            return "unknown";
        } else {
            return pushApps.get(0).getAppName();
        }
    }
}
