package com.coband.common.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVUser;
import com.coband.App;
import com.coband.watchassistant.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by imco on 5/9/16.
 */
public class Utils {

    private static final String TAG = "Utils";

    public static String format2TwoDecimalPlaces(double origin) {
        int number = (int) (origin * 100);
        return String.valueOf(number / 100f);
    }

    public static String format2OneDecimalPlaces(double origin) {
        int number = (int) (origin * 10);
        return String.valueOf(number / 10f);
    }


    public static SpannableString getTextAndColorSpanString(String text, int color, int size, int start, int end) {
        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size);
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static float dp2px(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

    public static float px2dp(float px, Context context) {
        // dp = px * 160 / dpi
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return (px * 160f / dpi + 0.5f);
    }

    public static int dp2px(int dp, Context context) {
        // px = dp * dpi / 160
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return (int) (dp * dpi / 160f + 0.5f);

    }

    public static void showDialog(Context context, String title, String message) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.ok)
                .positiveColor(context.getResources().getColor(R.color.azure))
                .show();
    }

    public static MaterialDialog showCustomViewDialog(Context context, View contentView) {
        boolean wrapInScrollView = false;
        return new MaterialDialog.Builder(context)
                .customView(contentView, wrapInScrollView)
                .build();
    }

    public static String getAppVersion(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().
                    getPackageInfo(activity.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isChineseLanguage() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        return (language != null && language.trim().equals("zh"));
    }

    public static SpannableString getSpanString(String text, int color, int size, int start, int end) {
        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size);
        // text color
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // text size.
        spannableString.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * format the minutes to hours and minutes.
     *
     * @param totalMinutes e.g 210
     * @return e.g 3小时30分 or 3h30m
     */
    public static String formatMinute(int totalMinutes) {
        int minute = totalMinutes % 60;
        int hour = (totalMinutes - minute) / 60;

        if (Locale.getDefault().getCountry().equals(Locale.CHINA.getCountry())) {
            if (hour == 0) {
                return minute + "分钟";
            } else {
                return hour + "小时" + minute + "分钟";
            }
        } else {
            if (hour == 0) {
                return minute + "m";
            } else {
                return hour + "h" + minute + "m";
            }

        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void toast(int id) {
        toast(App.getContext(), id);
    }

    public static void toast(String s) {
        toast(App.getContext(), s);
    }

    public static void toast(Context cxt, int id) {
        Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog showSpinnerDialog(Activity activity) {
        //activity = modifyDialogContext(activity);
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setMessage(App.getContext().getString(R.string.chat_utils_hardLoading));
        if (!activity.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    public static void setShowNameToText(TextView textView, AVUser user) {
        textView.setText(getShowNameFromUser(user));
    }

    public static String getShowNameFromUser(AVUser user) {
        String nickName = user.getString(C.NICKNAME);
        return (null == nickName || nickName.trim().isEmpty()) ? user.getUsername() : nickName;
    }

    public static ArrayList<String> getSPStringList(String StringList) {
        Log.d("TESTDEV", StringList);
        if (StringList.length() <= 0 || !StringList.contains("@")) {
            return null;
        }
        ArrayList<String> nameList = new ArrayList<String>();

        for (int i = 0; StringList.contains("@"); i++) {
            int index = StringList.indexOf("@");
            nameList.add(StringList.substring(0, index));
            if (index + 1 >= StringList.length()) {
                break;
            }
            StringList = StringList.substring(index + 1);
        }

        return nameList;
    }

    public static double changeKmToMiles(double km) {
        return km * 0.6213712f;
    }

    public static float changeMileToKm(float miles) {
        return miles * 1.609344f;
    }

    public static double changeLbToKg(double lb) {
        return lb * 0.4535924;
    }

    public static float changeKgToLb(float kg) {
        return kg * 2.2046226f;
    }

    public static double changeKgToLb(double kg) {
        return kg * 2.2046226f;
    }

    public static double changeFtToCm(double ft) {
        return ft * 30.48f;
    }

    public static double changeCmToFt(int metric) {
        return metric * 0.0328084;
    }

    public static String hmacSHA1Encrypt(String encryptText, String encryptKey) {
        try {
            byte[] data = encryptKey.getBytes("UTF-8");

            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = encryptText.getBytes("UTF-8");
            //完成 Mac 操作

            byte[] key = mac.doFinal(text);
            return Base64.encode(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getMd5ByFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if (file.isFile()) {
            return getFileMD5String(file);
        } else {
            return "";
        }
    }

    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    private static String getFileMD5String(File file) {
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println("初始化失败，MessageDigest不支持MD5Util");
            nsaex.printStackTrace();
        }
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }
        } catch (Exception e) {

        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

}
