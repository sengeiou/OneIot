package com.coband.common.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.coband.App;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.R;
import com.coband.watchassistant.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static com.coband.watchassistant.R.string.first_step;

/**
 * Created by mqh on 3/16/16.
 */
public class CocoUtils {

    private static Handler mHandler;
    private static HashMap<Class<?>, ArrayList<Runnable>> mRunnableMap;

    public static void init() {
        mRunnableMap = new HashMap<>();
    }

    public static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public static void removeCallbacks(Object object) {
        ArrayList<Runnable> runnables = mRunnableMap.get(object.getClass());
        if (null != runnables) {
            for (Runnable runnable : runnables) {
                getHandler().removeCallbacks(runnable);
            }
            mRunnableMap.remove(object.getClass());
        }
    }

    public static boolean isMetric() {
        Account account = DataManager.getInstance().getCurrentAccount();
        return !Config.METRIC_STRING.equals(account.getUnitSystem());
    }

    public static boolean isChineseLanguage() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        return (language != null && language.trim().equals("zh"));
    }

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(App.getContext());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != activeNetworkInfo) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkWifi.isConnected();
    }

    public static String getNameFromPhoneBook(Context context, String phoneNum, boolean isInCall) {

        String contactName = "";
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.getCount() != 0 && cursor.moveToFirst()) {
                do {
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (isInCall) {
                        number = number.replace("-", "").replace(" ", "");
                    } else {
                        phoneNum = phoneNum.replace("+86", "")
                                .replace("+1", "")
                                .replace("+81", "")
                                .replace("+7", "");
                        number = number.replace("-", "").replace(" ", "");
                    }

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if (number.equals(phoneNum)) {
                        contactName = name;
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        return contactName;
    }


    public static float px2dp(float px) {
        // dp = px * 160 / dpi
        DisplayMetrics metrics = App.getContext().getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return (px * 160f / dpi + 0.5f);
    }

    public static int dp2px(int dp) {
        // px = dp * dpi / 160
        DisplayMetrics metrics = App.getContext().getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return (int) (dp * dpi / 160f + 0.5f);

    }

    public static float dp2px(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

    public static void setAchievementIcon_deprecated(ImageView imageview, String achievementMessage) {
        Resources res = App.getInstance().getResources();

        if (achievementMessage.equals(res.getString(first_step))) {
            imageview.setImageResource(R.drawable.firststep);
        } else if (achievementMessage.equals(res.getString(R.string.walker))) {
            imageview.setImageResource(R.drawable.walker);
        } else if (achievementMessage.equals(res.getString(R.string.wang_t))) {
            imageview.setImageResource(R.drawable.wangt);
        } else if (achievementMessage.equals(res.getString(R.string.killer))) {
            imageview.setImageResource(R.drawable.killer);
        } else if (achievementMessage.equals(res.getString(R.string.asteroids))) {
            imageview.setImageResource(R.drawable.asteroids);
        } else if (achievementMessage.equals(res.getString(R.string.beli))) {
            imageview.setImageResource(R.drawable.beli);
        } else if (achievementMessage.equals(res.getString(R.string.hubell))) {
            imageview.setImageResource(R.drawable.hubell);
        } else if (achievementMessage.equals(res.getString(R.string.miley_q))) {
            imageview.setImageResource(R.drawable.mileyq);
        } else if (achievementMessage.equals(res.getString(R.string.wanli))) {
            imageview.setImageResource(R.drawable.wanli);
        }
    }

    public static int getAchievementIcon(String achievementMessage) {
        Resources res = App.getInstance().getResources();

        if (achievementMessage.equals(res.getString(first_step))) {
            return R.drawable.medal_firststep;
        } else if (achievementMessage.equals(res.getString(R.string.walker))) {
            return R.drawable.medal_walker;
        } else if (achievementMessage.equals(res.getString(R.string.wang_t))) {
            return R.drawable.medal_wangt;
        } else if (achievementMessage.equals(res.getString(R.string.killer))) {
            return R.drawable.medal_killer;
        } else if (achievementMessage.equals(res.getString(R.string.asteroids))) {
            return R.drawable.medal_asteroids;
        } else if (achievementMessage.equals(res.getString(R.string.beli))) {
            return R.drawable.medal_beli;
        } else if (achievementMessage.equals(res.getString(R.string.hubell))) {
            return R.drawable.medal_hubell;
        } else if (achievementMessage.equals(res.getString(R.string.miley_q))) {
            return R.drawable.medal_mileyq;
        } else if (achievementMessage.equals(res.getString(R.string.wanli))) {
            return R.drawable.medal_wanli;
        } else {
            return R.drawable.medal_firststep;
        }
    }

    public static int getAchievementSmallIcon(String achievementMessage) {
        Resources res = App.getInstance().getResources();

        if (achievementMessage.equals(res.getString(first_step))) {
            return R.drawable.firststep_show;
        } else if (achievementMessage.equals(res.getString(R.string.walker))) {
            return R.drawable.walker_show;
        } else if (achievementMessage.equals(res.getString(R.string.wang_t))) {
            return R.drawable.wangt_show;
        } else if (achievementMessage.equals(res.getString(R.string.killer))) {
            return R.drawable.killer_show;
        } else if (achievementMessage.equals(res.getString(R.string.asteroids))) {
            return R.drawable.asteroids_show;
        } else if (achievementMessage.equals(res.getString(R.string.beli))) {
            return R.drawable.beli_show;
        } else if (achievementMessage.equals(res.getString(R.string.hubell))) {
            return R.drawable.hubell_show;
        } else if (achievementMessage.equals(res.getString(R.string.miley_q))) {
            return R.drawable.mileyq_show;
        } else if (achievementMessage.equals(res.getString(R.string.wanli))) {
            return R.drawable.wanli_show;
        } else {
            return R.drawable.firststep_show;
        }
    }

    public static int getTextFromTitle(String title) {
        if (title == null) {
            return -1;
        }

        Resources res = App.getInstance().getResources();
        if (title.equals("FirstStep")) {
            return R.string.first_time_summary;
        } else if (title.equals("Walker")) {
            return R.string.more_than_10000_summary;
        } else if (title.equals("WangT")) {
            return R.string.more_than_20000_summary;
        } else if (title.equals("Killer")) {
            return R.string.more_than_30000_summary;
        } else if (title.equals("Asteroids")) {
            return R.string.total_distance_42_summary;
        } else if (title.equals("Beli")) {
            return R.string.total_distance_100_summary;
        } else if (title.equals("Hubell")) {
            return R.string.total_distance_500_summary;
        } else if (title.equals("MileyQ")) {
            return R.string.total_distance_1000_summary;
        } else if (title.equals("Wanli")) {
            return R.string.total_distance_10000_summary;
        }
        return -1;
    }

    public static String getAchievementTitle(String achievementMessage) {
        if (achievementMessage == null) {
            return "";
        }

        if (!CocoUtils.isChineseLanguage()) {
            return achievementMessage;
        }

        Resources res = App.getInstance().getResources();
        if (achievementMessage.equals(res.getString(first_step))) {
            return "初步始者";
        } else if (achievementMessage.equals(res.getString(R.string.walker))) {
            return "步行者";
        } else if (achievementMessage.equals(res.getString(R.string.wang_t))) {
            return "万行者";
        } else if (achievementMessage.equals(res.getString(R.string.killer))) {
            return "超神绝杀";
        } else if (achievementMessage.equals(res.getString(R.string.asteroids))) {
            return "小行星";
        } else if (achievementMessage.equals(res.getString(R.string.beli))) {
            return "百里";
        } else if (achievementMessage.equals(res.getString(R.string.hubell))) {
            return "伍佰";
        } else if (achievementMessage.equals(res.getString(R.string.miley_q))) {
            return "千里行";
        } else if (achievementMessage.equals(res.getString(R.string.wanli))) {
            return "万里行";
        }
        return "";
    }

    public static String getMedalTitleLocalLangFromTitle(String title) {
        if (title == null) {
            return "";
        }

        Context context = App.getContext();
        if (title.equals("FirstStep")) {
            return context.getString(R.string.first_step);
        } else if (title.equals("Walker")) {
            return context.getString(R.string.walker);
        } else if (title.equals("WangT")) {
            return context.getString(R.string.wang_t);
        } else if (title.equals("Killer")) {
            return context.getString(R.string.killer);
        } else if (title.equals("Asteroids")) {
            return context.getString(R.string.asteroids);
        } else if (title.equals("Beli")) {
            return context.getString(R.string.beli);
        } else if (title.equals("Hubell")) {
            return context.getString(R.string.hubell);
        } else if (title.equals("MileyQ")) {
            return context.getString(R.string.miley_q);
        } else if (title.equals("Wanli")) {
            return context.getString(R.string.wanli);
        }
        return "";
    }

    public static String getMedalTipFromTitle(String title) {
        if (title == null) {
            return "";
        }

        Context context = App.getContext();
        if (title.equals("FirstStep")) {
            return context.getString(R.string.first_time_tip);
        } else if (title.equals("Walker")) {
            return context.getString(R.string.more_than_10000_tip);
        } else if (title.equals("WangT")) {
            return context.getString(R.string.more_than_20000_tip);
        } else if (title.equals("Killer")) {
            return context.getString(R.string.more_than_30000_tip);
        } else if (title.equals("Asteroids")) {
            return context.getString(R.string.total_distance_42_tip);
        } else if (title.equals("Beli")) {
            return context.getString(R.string.total_distance_100_tip);
        } else if (title.equals("Hubell")) {
            return context.getString(R.string.total_distance_500_tip);
        } else if (title.equals("MileyQ")) {
            return context.getString(R.string.total_distance_1000_tip);
        } else if (title.equals("Wanli")) {
            return context.getString(R.string.total_distance_10000_tip);
        }
        return "";
    }

    public static String getAchievementTitleNew(String achievementMessage) {
        if (achievementMessage == null) {
            return "";
        }

        if (!CocoUtils.isChineseLanguage()) {
            return achievementMessage;
        }

        if (achievementMessage.equals("FirstStep")) {
            return "初步始者";
        } else if (achievementMessage.equals("Walker")) {
            return "步行者";
        } else if (achievementMessage.equals("WangT")) {
            return "万行者";
        } else if (achievementMessage.equals("Killer")) {
            return "超神绝杀";
        } else if (achievementMessage.equals("Asteroids")) {
            return "小行星";
        } else if (achievementMessage.equals("Beli")) {
            return "百里";
        } else if (achievementMessage.equals("Hubell")) {
            return "伍佰";
        } else if (achievementMessage.equals("MileyQ")) {
            return "千里行";
        } else if (achievementMessage.equals("Wanli")) {
            return "万里行";
        }
        return "";
    }

    public static float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    public static double keepADecimal(double value) {
        return ((double) Math.round(value * 10)) / 10;
    }

    public static final int TIME_TAG = R.id.click_time;
    public static final int MIN_CLICK_DELAY_TIME = 800;

    public static boolean singleClick(View view) {
        if (view == null) return false;
        Object tag = view.getTag(TIME_TAG);
        long lastClickTime = ((tag != null) ? (long) tag : 0);
        Logger.d("SingleClick", "lastClickTime:" + lastClickTime);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//过滤掉800毫秒内的连续点击
            view.setTag(TIME_TAG, currentTime);
            Logger.d("SingleClick", "currentTime:" + currentTime);
            return true;
        } else {
            return false;
        }

    }
}
