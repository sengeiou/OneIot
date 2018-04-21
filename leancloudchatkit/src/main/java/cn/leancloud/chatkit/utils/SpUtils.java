package cn.leancloud.chatkit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by mai on 17-1-23.
 */

public class SpUtils {
    static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences("watch_assistant", Context.MODE_PRIVATE);
    }

    public void setPreferenceString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getPreferenceString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public static ArrayList<String> getMuteList() {
        String muteList = prefs.getString("mute_notification", "");

        ArrayList<String> nameList = new ArrayList<String>();

        for (int i = 0; muteList.contains("@"); i++) {
            int index = muteList.indexOf("@");
            nameList.add(muteList.substring(0, index));
            if (index + 1 >= muteList.length()) {
                break;
            }
            muteList = muteList.substring(index + 1);
        }

        return nameList;
    }


}
