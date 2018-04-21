package com.coband.interactivelayer.receiver.systemdatelisten;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.coband.interactivelayer.manager.ConnectManager;
import com.coband.interactivelayer.manager.ControlManager;


public class SystemDateChangeBroadcastReceive extends BroadcastReceiver {
    // Log
    private final static String TAG = "SystemDateChange";
    private final static boolean D = true;

    public static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;
    public static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;
    public static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;

    public SystemDateChangeBroadcastReceive() {
        if(D) Log.d(TAG, "init");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(D) Log.d(TAG, "onReceive, action: " + intent.getAction());
        if(D) Log.e(TAG, "ControlManager.getInstance().isConnected(): " + ConnectManager.getInstance().isConnected()
                + ", ControlManager.getInstance().mWristState: " + ControlManager.getInstance().mWristState
                + ", ConnectManager.getInstance().isInLogin(): " + ConnectManager.getInstance().isInLogin());
        // Need check current link again
        if(!(ConnectManager.getInstance().isConnected()
                && ControlManager.getInstance().isReady()
                && !ConnectManager.getInstance().isInLogin())) {
            if(D) Log.e(TAG, "Receive broadcast with state error, do nothing!");
            return;
        }
        if(intent.getAction().equals(ACTION_TIMEZONE_CHANGED)
                || intent.getAction().equals(ACTION_DATE_CHANGED)
                || intent.getAction().equals(ACTION_TIME_CHANGED)){
            doReceiveTimeChange(context, intent);
        }
    }

    private boolean isInSyncDate = false;
    public void doReceiveTimeChange(Context context, Intent intent) {
        if(!isInSyncDate) {
            isInSyncDate = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ControlManager.getInstance().setTimeSync();
                    isInSyncDate = false;
                }
            }).start();
        }
    }


}
