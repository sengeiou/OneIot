package com.coband;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.facebook.stetho.Stetho;
import com.coband.cocoband.mvp.model.bean.HeartRateHistory;
import com.coband.cocoband.mvp.model.bean.History;
import com.coband.cocoband.mvp.model.bean.SleepHistory;
import com.coband.common.network.NetworkConfig;
import com.coband.common.utils.LeanCloudConfig;
import com.coband.common.utils.UTESPUtil;
import com.coband.watchassistant.AccountDao;
import com.coband.watchassistant.BloodPressureDao;
import com.coband.watchassistant.DBWeightDao;
import com.coband.watchassistant.DaoMaster;
import com.coband.watchassistant.DaoSession;
import com.coband.watchassistant.HeartRateDao;
import com.coband.watchassistant.HistoryDataDao;
import com.coband.watchassistant.PushAppDao;
import com.coband.watchassistant.R;
import com.coband.watchassistant.SleepDao;
import com.coband.watchassistant.SleepNodeDetailDao;
import com.coband.watchassistant.StepNodeDetailDao;
import com.coband.watchassistant.UserDao;
import com.meituan.android.walle.WalleChannelReader;
import com.owen.library.sharelib.ShareBlock;
import com.owen.library.sharelib.ShareBlockConfig;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.yc.pedometer.utils.GlobalVariable;

import java.util.Iterator;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.utils.SpUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Ivan on 11/16/15.
 */
public class App extends MultiDexApplication {
    private static App instance;

    private static DaoSession daoSession;
//    public static IWXAPI mWxApi;


    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());

//        MultiDex.install(this);
        App.instance = this;

        initTinker();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        if (WalleChannelReader.getChannel(App.getContext()) == null) {
            Stetho.initializeWithDefaults(this);
        }

        setupDatabase();

        //init leancloud
        AVObject.registerSubclass(History.class);
        AVObject.registerSubclass(HeartRateHistory.class);
        AVObject.registerSubclass(SleepHistory.class);
        AVOSCloud.initialize(this, LeanCloudConfig.APP_ID, LeanCloudConfig.APP_KEY);


        SpUtils.init(this);
        UTESPUtil.remove(GlobalVariable.YC_PED_CALORIES_SP);
        UTESPUtil.remove(GlobalVariable.YC_PED_DISTANCE_SP);
        UTESPUtil.remove(GlobalVariable.YC_PED_STEPS_SP);

//        registerToWX();
        initShareLib();
    }

    private void initShareLib() {
        ShareBlock.init(new ShareBlockConfig.Builder()
                .weiXin(NetworkConfig.Wechat_AppID, NetworkConfig.Wechat_AppSecret)
                .qq(NetworkConfig.QQ_AppKEY)
                .build());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initTinker() {
        String channel = WalleChannelReader.getChannel(this);
        if (channel == null) return;
        if (channel.contains("googleplay")) return;

        // 我们可以从这里获得Tinker加载过程的信息
        ApplicationLike tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

        // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
        TinkerPatch.init(tinkerApplicationLike)
                .reflectPatchLibrary()
                .setPatchRollbackOnScreenOff(true)
                .setPatchRestartOnSrceenOff(true)
                .addIgnoreAppChannel("googleplay");

        // 每隔3个小时去访问后台时候有更新,通过handler实现轮训的效果
        new FetchPatchHandler().fetchPatchWithInterval(3);
    }


    private String getVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String getCurProcessName() {
        int i = Process.myPid();
        Iterator iterator = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) iterator.next();
            if (localRunningAppProcessInfo.pid == i)
                return localRunningAppProcessInfo.processName;
        }
        return null;
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "assistant_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static HistoryDataDao getHistoryDataDao() {
        return daoSession.getHistoryDataDao();
    }

    public static SleepDao getSleepDao() {
        return daoSession.getSleepDao();
    }

    public static HeartRateDao getHeartRateDao() {
        return daoSession.getHeartRateDao();
    }

    public static DBWeightDao getWeightDao() {
        return daoSession.getDBWeightDao();
    }

    public static PushAppDao getPushAppDao() {
        return daoSession.getPushAppDao();
    }

    public static UserDao getUserDao() {
        return daoSession.getUserDao();
    }

    public static StepNodeDetailDao getStepNodeDetailDao() {
        return daoSession.getStepNodeDetailDao();
    }

    public static SleepNodeDetailDao getSleepNodeDetailDao() {
        return daoSession.getSleepNodeDetailDao();
    }

    public static BloodPressureDao getBloodPressureDao() {
        return daoSession.getBloodPressureDao();
    }

    public static AccountDao getAccountDao() {
        return daoSession.getAccountDao();
    }
}
