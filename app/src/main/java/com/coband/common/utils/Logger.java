package com.coband.common.utils;

import android.util.Log;

/**
 * Created by imco on 4/12/16.
 * logcat config
 */
public class Logger {

    private static boolean isDebug = true;

    public static void d(String tag, String message) {
        if (isDebug)
            Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        if (isDebug)
            Log.d(tag, message);
    }

    public static void d(Object object, String message) {
        if (isDebug)
            Log.d(object.getClass().getSimpleName(), message);
    }

    public static void e(Object object, String message) {
        if (isDebug)
            Log.d(object.getClass().getSimpleName(), message);
    }

    public static void i(Object object, String message) {
        if (isDebug)
            Log.d(object.getClass().getSimpleName(), message);
    }

    public static void v(Object object, String message) {
        if (isDebug)
            Log.d(object.getClass().getSimpleName(), message);
    }

    public static void logException(Object object, Throwable tr) {
        if (isDebug) {
            Log.e(object.getClass().getSimpleName(), getDebugInfo(), tr);
        }
    }

    private static String getDebugInfo() {
        Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
        int n = 2;
        return trace[n].getClassName() + " " + trace[n].getMethodName() + "()" + ":" + trace[n].getLineNumber() +
                " ";
    }
}
