package com.coband.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.coband.App;

/**
 * Created by imco on 11/16/15.
 */
public class SPUtil {
    public static final String currentTable = "watch_assistant";

    public static void putString(String key, String value) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        preferences.edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defaultValue) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        preferences.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defaultValue) {
        SharedPreferences preferences = App.getInstance()
                .getSharedPreferences(currentTable, Context.MODE_PRIVATE);
        return preferences.getFloat(key, defaultValue);
    }
}
