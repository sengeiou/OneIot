package com.coband.interactivelayer.manager;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.coband.dfu.network.OTAService;
import com.coband.dfu.network.bean.AllFwResultBean;
import com.coband.interactivelayer.bean.AlarmClockBean;
import com.coband.interactivelayer.bean.BloodPressurePacket;
import com.coband.interactivelayer.bean.FactorySensorPacket;
import com.coband.interactivelayer.bean.FunctionsBean;
import com.coband.interactivelayer.bean.SleepItemPacket;
import com.coband.interactivelayer.bean.SleepPacket;
import com.coband.interactivelayer.bean.SportItemPacket;
import com.coband.interactivelayer.bean.SportPacket;
import com.coband.interactivelayer.receiver.systemdatelisten.SystemDateChangeBroadcastReceive;
import com.coband.protocollayer.applicationlayer.ApplicationLayer;
import com.coband.protocollayer.applicationlayer.ApplicationLayerAlarmPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerAlarmsPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerBPItemPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerBPPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerCallback;
import com.coband.protocollayer.applicationlayer.ApplicationLayerFacSensorPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerFunctions;
import com.coband.protocollayer.applicationlayer.ApplicationLayerHrpItemPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerHrpPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerLogResponsePacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerRecentlySportPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerSleepItemPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerSleepPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerSportItemPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerSportPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerTodaySumSportPacket;
import com.coband.protocollayer.gattlayer.GlobalGatt;
import com.coband.protocollayer.service.BatteryService;
import com.coband.protocollayer.service.HrpService;
import com.coband.protocollayer.service.ImmediateAlertService;
import com.coband.protocollayer.service.LinkLossService;
import com.coband.utils.LogUtils;
import com.coband.utils.SPWristbandConfigInfo;
import com.coband.utils.StringByteTrans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.coband.interactivelayer.Flags.BOND_RSP_SUCCESS;
import static com.coband.interactivelayer.Flags.CAMERA_CONTROL_APP_IN_BACK;
import static com.coband.interactivelayer.Flags.CAMERA_CONTROL_APP_IN_FORE;
import static com.coband.interactivelayer.Flags.DEBUG_LOG_TYPE_CONFIG_DATA;
import static com.coband.interactivelayer.Flags.DEBUG_LOG_TYPE_MODULE_APP;
import static com.coband.interactivelayer.Flags.DEBUG_LOG_TYPE_MODULE_LOWERSTACK;
import static com.coband.interactivelayer.Flags.DEBUG_LOG_TYPE_MODULE_UPSTACK;
import static com.coband.interactivelayer.Flags.DEBUG_LOG_TYPE_SLEEP_DATA;
import static com.coband.interactivelayer.Flags.DEBUG_LOG_TYPE_SPORT_DATA;
import static com.coband.interactivelayer.Flags.LOGIN_LOSS_LOGIN_INFO;
import static com.coband.interactivelayer.Flags.LONG_SIT_CONTROL_DISABLE;
import static com.coband.interactivelayer.Flags.LONG_SIT_CONTROL_ENABLE;
import static com.coband.interactivelayer.Flags.PHONE_OS_ANDROID;
import static com.coband.interactivelayer.Flags.SPORT_DATA_SYNC_MODE_DISABLE;
import static com.coband.interactivelayer.Flags.SPORT_DATA_SYNC_MODE_ENABLE;

/**
 * @see CommandManager
 * @deprecated
 */
public class ControlManager
        implements BatteryService.OnServiceListener,
        LinkLossService.OnServiceListener, OTAService.OnServiceListener, HrpService.OnServiceListener {
    // Log
    private final static String TAG = "ControlManager";
    private final static boolean D = true;

    private String mDeviceAddress;

    // Application Layer Object
    private ApplicationLayer mApplicationLayer;

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    // Message
    public static final int MSG_STATE_CONNECTED = 0;
    public static final int MSG_STATE_DISCONNECTED = 1;
    public static final int MSG_WRIST_STATE_CHANGED = 2;
    public static final int MSG_RECEIVE_SPORT_INFO = 3;//characteristic read
    public static final int MSG_RECEIVE_HISTORY_SPORT_INFO = 0x3000;//characteristic read
    public static final int MSG_RECEIVE_SLEEP_INFO = 4;
    public static final int MSG_RECEIVE_HISTORY_SYNC_BEGIN = 5;
    public static final int MSG_RECEIVE_HISTORY_SYNC_END = 6;
    public static final int MSG_RECEIVE_ALARMS_INFO = 7;
    public static final int MSG_RECEIVE_NOTIFY_MODE_SETTING = 8;
    public static final int MSG_RECEIVE_LONG_SIT_SETTING = 9;
    public static final int MSG_RECEIVE_FAC_SENSOR_INFO = 10;
    public static final int MSG_RECEIVE_DFU_VERSION_INFO = 11;
    public static final int MSG_RECEIVE_DEVICE_NAME_INFO = 12;
    public static final int MSG_RECEIVE_BATTERY_INFO = 13;
    public static final int MSG_RECEIVE_BATTERY_CHANGE_INFO = 14;
    public static final int MSG_RECEIVE_HRP_INFO = 15;
    public static final int MSG_RECEIVE_HISTORY_HRP_INFO = 0x3001;
    public static final int MSG_RECEIVE_HISTORY_SPORT_END = 0x3002;
    public static final int MSG_RECEIVE_TAKE_PHOTO_RSP = 16;
    public static final int MSG_RECEIVE_TURN_OVER_WRIST_SETTING = 17;
    public static final int MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ = 18;
    public static final int MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP = 19;
    public static final int MSG_RECEIVE_DISPLAY_SWITCH_RETURN = 0x3003;
    public static final int MSG_RECEIVE_BLOOD_PRESSURE_RSP = 0x3004;
    public static final int MSG_RECEIVE_BLOOD_PRESSURE_END = 0x3005;
    public static final int MSG_RECEIVE_FUNCTIONS = 0x3006;

    public static final int MSG_RECEIVE_LOG_START = 50;
    public static final int MSG_RECEIVE_LOG_END = 51;
    public static final int MSG_RECEIVE_LOG_RSP = 52;


    public static final int MSG_ERROR = 100;

    // Wristband state manager
    public int mWristState;

    private boolean mErrorStatus;

    // Use to manager request and response transaction
    private final Object mRequestResponseLock = new Object();
    private final int MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME = 30000;

    private volatile boolean isResponseCome;
//    private volatile boolean isNeedWaitForResponse;

    // Use to manager command send transaction
//    private volatile boolean isInSendCommand;
    private volatile boolean isCommandSend;
    private volatile boolean isCommandSendOk;
    private final Object mCommandSendLock = new Object();
    private final int MAX_COMMAND_SEND_WAIT_TIME = 15000;

    // object
    private static ControlManager mInstance;
    private static Context mContext;

    private ArrayList<CommandCallback> mCallbacks;

    private static SystemDateChangeBroadcastReceive mSystemDateChangeBroadcastReceive;

    private BatteryService mBatteryService;
    private ImmediateAlertService mImmediateAlertService;
    private LinkLossService mLinkLossService;
    private OTAService mOTAService;

    // MyHandler

    static void initial(Context context) {
        if (D) Log.d(TAG, "init()");

        GlobalGatt.initial(context);
        GlobalGatt.getInstance().initialize();

        mInstance = new ControlManager();
        mContext = context;

        // init Wristband Application Layer and register the callback
        mInstance.mApplicationLayer = new ApplicationLayer(context, mInstance.mApplicationCallback);

        // Initial Callback list
        mInstance.mCallbacks = new ArrayList<>();

        // Initial State
        mInstance.mWristState = CommandManager.LoginState.STATE_WRIST_INITIAL;

        registerDateChangeBroadcast();

    }

    public static ControlManager getInstance() {
        return mInstance;
    }

    /*
     * close band connect, clear all callback
     */
    public void close() {
        if (D) Log.d(TAG, "close()");

        mCallbacks.clear();
        // close all wait lock
        synchronized (mRequestResponseLock) {
            isResponseCome = false;
            mRequestResponseLock.notifyAll();
        }

        synchronized (mCommandSendLock) {
            isCommandSend = false;
            isCommandSendOk = false;
            mCommandSendLock.notifyAll();
        }
        // unregister call back.
        unregisterDateChangeBroadcast();

        updateWristState(CommandManager.LoginState.STATE_WRIST_INITIAL);
    }


    public String getBluetoothAddress() {
        return mDeviceAddress;
    }


    public void registerCallback(CommandCallback callback) {
        synchronized (mCallbacks) {
            if (!mCallbacks.contains(callback)) {
                mCallbacks.add(callback);
            }
        }
    }

    public boolean isCallbackRegistered(CommandCallback callback) {
        boolean isCon = false;
        synchronized (mCallbacks) {
            isCon = mCallbacks.contains(callback);
        }
        return isCon;
    }

    public void unRegisterCallback(CommandCallback callback) {
        synchronized (mCallbacks) {
            if (mCallbacks.contains(callback)) {
                mCallbacks.remove(callback);
            }
        }
    }

    /*
     * connect to the wristband.
     */
    boolean connect(String address, CommandCallback callback) {
        if (D) Log.d(TAG, "connect to: " + address);
        // register callback
        registerCallback(callback);

        mDeviceAddress = address;

        // connect to the device
        boolean connect = mApplicationLayer.connect(address);

        // Add first init flag
        SPWristbandConfigInfo.setFirstInitialFlag(mContext, true);
        // close all.
        if (mBatteryService != null) {
            mBatteryService.close();
            if (mImmediateAlertService != null)
                mImmediateAlertService.close();
            if (mLinkLossService != null)
                mLinkLossService.close();
            if (mOTAService != null)
                mOTAService.close();
        }
        // Extend service
        mBatteryService = new BatteryService(mDeviceAddress, this);
        mImmediateAlertService = new ImmediateAlertService(mDeviceAddress);
        mLinkLossService = new LinkLossService(mDeviceAddress, this);
        mOTAService = new OTAService(mDeviceAddress, this);

        // Register Broadcast


        return connect;
    }

    public void startLoginProcess(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // update state
                updateWristState(CommandManager.LoginState.STATE_WRIST_LOGGING);
                // Request to login
                if (!RequestLogin(id)) {
                    if (!mErrorStatus) {

                        // update state
                        updateWristState(CommandManager.LoginState.STATE_WRIST_BONDING);
                        // Request to bond
                        if (RequestBond(id)) {
                            // do all the setting work.
                            setDataSync(false);
                            setNeedInfoForLogin();
                        } else {
                            if (D)
                                Log.e(TAG, "Something error in bond. isResponseCome: " + isResponseCome);
                            // some thing error
                            if (isResponseCome) {
                                sendErrorMessage(CommandManager.ErrorCode.ERROR_CODE_BOND_ERROR);
                            } else {
                                sendErrorMessage(CommandManager.ErrorCode.ERROR_CODE_NO_LOGIN_RESPONSE_COME);
                            }

                            if (isConnected()) {
                                disconnect();
                            }
                        }
                    } else {
                        // some thing error
                        sendErrorMessage(CommandManager.ErrorCode.ERROR_CODE_NO_LOGIN_RESPONSE_COME);
                        if (D) Log.e(TAG, "long time no login response, do disconnect");
                        if (isConnected()) {
                            disconnect();
                        }
                    }
                } else {
                    if (mLoginResponseStatus == ApplicationLayer.LOGIN_RSP_SUCCESS) {
                        setNeedInfoForLogin();
                    }
                }
            }
        }).start();
    }

    private void setNeedInfoForLogin() {
        LogUtils.d("setNeedInfoForLogin");
        CommandManager.setUserProfile(null);
        LogUtils.d("setUserProfile");

        CommandManager.setTargetStep(null);
        LogUtils.d("setTargetStep");

        // time sync
        CommandManager.setTimeSync(new SendCommandCallback() {
            @Override
            public void onCommandSend(boolean status) {
                if (!status)
                    updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_TIME_SYNC);
            }

            @Override
            public void onDisconnected() {
            }

            @Override
            public void onError(Throwable error) {
                updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_TIME_SYNC);
            }
        });
        LogUtils.d("setTimeSync");


        CommandManager.setPhoneOS(new SendCommandCallback() {
            @Override
            public void onCommandSend(boolean status) {
                if (!status)
                    updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_PHONE_OS);

            }

            @Override
            public void onDisconnected() {
            }

            @Override
            public void onError(Throwable error) {
                updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_PHONE_OS);
            }
        });

        // add by ivan. add read battery level on sync operation.
        CommandManager.readBatteryLevel(new SendCommandCallback() {
            @Override
            public void onCommandSend(boolean status) {
                if (!status)
                    updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_BATTERY_LEVEL);
            }

            @Override
            public void onDisconnected() {
            }

            @Override
            public void onError(Throwable error) {
                updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_BATTERY_LEVEL);
            }
        });

        CommandManager.sendClocksSyncRequest(null);

        // end by ivan.
        CommandManager.readDfuVersion(new SendCommandCallback() {
            @Override
            public void onCommandSend(boolean status) {
                if (!status) {
                    updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_FIRMWARE_VERSION);
                }
                // update state
//                                enableBatteryNotification(true);
                updateWristState(CommandManager.LoginState.STATE_WRIST_LOGIN);
            }

            @Override
            public void onDisconnected() {
            }

            @Override
            public void onError(Throwable error) {
                updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_FIRMWARE_VERSION);
            }
        });
    }

    /*
     * send disconnect band command
     *
     * @return
     */
    void disconnect() {
        mApplicationLayer.disconnect();
    }

    /*
     * set the name
     *
     * @param name the name
     */
    boolean setDeviceName(String name) {
        if (D) Log.d(TAG, "setDeviceName, name: " + name);
        initialCommandSend();

        mApplicationLayer.setDeviceName(name);

        SPWristbandConfigInfo.setInfoKeyValue(mContext, getBluetoothAddress(), name);
        return waitCommandSend();
    }

    /*
     * Get the name
     */
    boolean getDeviceName() {
        if (D) Log.d(TAG, "getDeviceName");
        initialCommandSend();

        mApplicationLayer.getDeviceName();

        return waitCommandSend();
    }

    /*
     * Use to sync the notify mode
     *
     * @param mode 0x01 enable call notify; 0x02 disable call notify
     * @return the operate result
     */
    boolean setNotifyMode(byte mode) {
        if (D) Log.d(TAG, "SetNotifyMode, mode: " + mode);
        initialCommandSend();

        // Try to set notify mode
        mApplicationLayer.settingCmdCallNotifySetting(mode);

        return waitCommandSend();
    }

    /*
     * Use to request current notify mode
     *
     * @return the operate result
     */
    boolean sendNotifyModeRequest() {
        if (D) Log.d(TAG, "sendNotifyModeRequest");
        initialCommandSend();

        // Try to request notify mode
        mApplicationLayer.settingCmdRequestNotifySwitchSetting();

        return waitCommandSend();
    }

    /*
     * Use to start/stop data sync.
     *
     * @param enable start or stop data sync.
     * @return the operate result
     */
    boolean setDataSync(boolean enable) {
        if (D) Log.d(TAG, "setDataSync(): " + enable);

        initialCommandSend();

        // Try to start/stop data sync
        if (enable) {
            mApplicationLayer.sportDataCmdSyncSetting(SPORT_DATA_SYNC_MODE_ENABLE);
        } else {
            mApplicationLayer.sportDataCmdSyncSetting(SPORT_DATA_SYNC_MODE_DISABLE);
        }

        return waitCommandSend();
    }

    /*
     * Use to send data request.
     *
     * @return the operate result
     */
    boolean sendDataRequest() {
        if (D) Log.d(TAG, "sendDataRequest()");
        if (mWristState != CommandManager.LoginState.STATE_WRIST_LOGIN
                && mWristState != CommandManager.LoginState.STATE_WRIST_SYNC_DATA) {
            if (D) Log.e(TAG, "StartDataSync failed, with error state: " + mWristState);
            return false;
        }

        initialCommandSend();

        mApplicationLayer.sportDataCmdRequestData();

        return waitCommandSend();
    }


    /*
     * Use to enable/disable the long sit set
     *
     * @param enable     : enable long sit remind
     * @param alarmTime: alarm cycle
     * @return the operate result
     */
    boolean setLongSit(boolean enable, int alarmTime) {
        if (D) Log.d(TAG, "setLongSit(), enable: " + enable);
        initialCommandSend();
//        ApplicationLayerSitPacket sit;
        if (enable) {
            mApplicationLayer.settingCmdLongSitSetting(LONG_SIT_CONTROL_ENABLE
                    , 0, alarmTime, 0, 0, (byte) 0);
        } else {
            mApplicationLayer.settingCmdLongSitSetting(LONG_SIT_CONTROL_DISABLE
                    , 0, 0, 0, 0, (byte) 0);
        }
        // Try to set long sit
        return waitCommandSend();
    }

    /*
     * Use to enable/disable the long sit set, and setup alarmTime
     *
     * @param alarmTime: alarm cycle
     * @param daysFlag   : 由底 bit 位到高 bit位,分别代表从周一到周日的重复设置。Bit 位为 1 是表示重复,
     *                   为 0 时表示不重复。所有的 bit 位都为 0时,表示只当天有效。 eg : 0x01 == mon
     * @return the operate result
     */
    boolean setLongSit(boolean enable, int alarmTime, int startTime, int endTime, byte daysFlag) {
        if (D) Log.d(TAG, "setLongSit(), enable: " + enable);
        initialCommandSend();
        if (enable) {
            // Try to set long sit
            mApplicationLayer.settingCmdLongSitSetting(LONG_SIT_CONTROL_ENABLE
                    , 0, alarmTime, startTime, endTime, daysFlag);
        } else {
            // Try to set long sit
            mApplicationLayer.settingCmdLongSitSetting(LONG_SIT_CONTROL_DISABLE
                    , 0, 0, 0, 0, (byte) 0);

        }

        return waitCommandSend();
    }

    /*
     * Use to enable/disable the continue hrp set
     *
     * @return the operate result
     */
    boolean setContinueHrp(boolean enable, int interval) {
        if (D) Log.d(TAG, "setContinueHrp(), enable: " + enable);
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.sportDataCmdHrpContinueSet(enable, interval);

        return waitCommandSend();
    }

    /*
     * Use to enable/disable the continue hrp set
     *
     * @return the operate result
     */
    boolean setContinueHrp(boolean enable) {
        if (D) Log.d(TAG, "setContinueHrp(), enable: " + enable);
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.sportDataCmdHrpContinueSet(enable, SPWristbandConfigInfo.getContinueHrpControlInterval(mContext));

        return waitCommandSend();
    }


    boolean sendContinueHrpParamRequest() {
        if (D) Log.d(TAG, "sendContinueHrpParamRequest");
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.sportDataCmdHrpContinueParamRequest();

        return waitCommandSend();
    }

    /*
     * Use to enable/disable the turn over wrist
     *
     * @return the operate result
     */
    boolean setTurnOverWrist(boolean enable) {
        if (D) Log.d(TAG, "setTurnOverWrist(), enable: " + enable);
        initialCommandSend();
        // Try to set long sit
        mApplicationLayer.settingCmdTurnOverWristSetting(enable);

        return waitCommandSend();
    }

    /*
     * Use to sync the long sit set
     *
     * @return the operate result
     */
    boolean setLongSit(byte enable, int threshold, int notify, int start, int stop, byte dayflags) {
        if (D) Log.d(TAG, "setLongSit()");
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.settingCmdLongSitSetting(enable, threshold, notify, start, stop, dayflags);

        return waitCommandSend();
    }


    /*
     * Use to request current long sit set
     *
     * @return the operate result
     */
    boolean sendLongSitRequest() {
        if (D) Log.d(TAG, "sendLongSitRequest()");
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.settingCmdRequestLongSitSetting();

        return waitCommandSend();
    }

    /*
     * Use to request current long sit set
     *
     * @return the operate result
     */
    boolean sendTurnOverWristRequest() {
        if (D) Log.d(TAG, "sendTurnOverWristRequest()");
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.settingCmdRequestTurnOverWristSetting();

        return waitCommandSend();
    }

    /*
     * Set the display status of the screen
     *
     * @param status {@link CommandManager.ScreenState#PORTRAIT} or {@link CommandManager.ScreenState#LANDSCAPE}
     */
    boolean sendDisplaySwitchSetting(@CommandManager.ScreenState int status) {

        if (D) Log.d(TAG, "sendDisplaySwitchSetting　: " + status);
        initialCommandSend();
        // Need add this
//        isNeedWaitForResponse = true;

        // Try to set long sit
        mApplicationLayer.settingCmdRequestDisplaySwitchSetting(status);

        return waitCommandSend();
    }

    /*
     * Request to get the current state of the smart bracelet screen display
     * Screen display status :
     * {@link CommandManager.ScreenState#PORTRAIT}
     * {@link CommandManager.ScreenState#LANDSCAPE}
     */
    boolean sendDisplaySwitchRequest() {
        if (D) Log.d(TAG, "sendDisplaySwitchRequest()");
        initialCommandSend();
        // Need add this
//        isNeedWaitForResponse = true;

        // Try to set long sit
        mApplicationLayer.settingCmdRequestDisplaySwitchRequest();

        return waitCommandSend();
    }


    /*
     * Use to sync the user profile, use local info
     *
     * @return the operate result
     */
    boolean setUserProfile() {
        if (D) Log.d(TAG, "setUserProfile()");
        initialCommandSend();
        boolean sex = SPWristbandConfigInfo.getGendar(mContext);
        int age = SPWristbandConfigInfo.getAge(mContext);
        int height = SPWristbandConfigInfo.getHeight(mContext);
        int weight = SPWristbandConfigInfo.getWeight(mContext);

        // Try to set user profile
        mApplicationLayer.settingCmdUserSetting(sex, age, height, weight);

        return waitCommandSend();
    }

    /*
     * Use to sync the user profile
     *
     * @return the operate result
     */
    boolean setUserProfile(boolean sex, int age, int height, int weight) {
        SPWristbandConfigInfo.setGendar(mContext, sex);
        SPWristbandConfigInfo.setAge(mContext, age);
        SPWristbandConfigInfo.setHeight(mContext, height);
        SPWristbandConfigInfo.setWeight(mContext, weight);

        if (D) Log.d(TAG, "setUserProfile()");
        initialCommandSend();

        // Try to set user profile
        mApplicationLayer.settingCmdUserSetting(sex, age, height, weight);

        return waitCommandSend();
    }

    /*
     * Use to sync the target step, user local info
     *
     * @return the operate result
     */
    boolean setTargetStep() {
        initialCommandSend();

        long step = SPWristbandConfigInfo.getTargetStep(mContext);
        if (D) Log.d(TAG, "setTargetStep, step: " + step);

        // Try to set step
        mApplicationLayer.settingCmdStepTargetSetting(step);

        return waitCommandSend();
    }

    /*
     * Use to sync the target step
     *
     * @return the operate result
     */
    boolean setTargetStep(long step) {
        if (D) Log.d(TAG, "setTargetStep, step: " + step);
        initialCommandSend();
        // Try to set step
        mApplicationLayer.settingCmdStepTargetSetting(step);
        SPWristbandConfigInfo.setTargetStep(mContext, step);

        return waitCommandSend();
    }

    /*
     * Use to set the phone os
     *
     * @return the operate result
     */
    boolean setPhoneOS() {
        if (D) Log.d(TAG, "setPhoneOS");
        initialCommandSend();

        // Try to set step
        mApplicationLayer.settingCmdPhoneOSSetting(PHONE_OS_ANDROID);

        return waitCommandSend();
    }

    /*
     * Use to set the clocks
     *
     * @return the operate result
     */
    boolean setClocks(ApplicationLayerAlarmsPacket alarms) {
        if (D) Log.d(TAG, "setClocks()");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.settingCmdAlarmsSetting(alarms);

        return waitCommandSend();
    }

    /*
     * Use to set the clock
     *
     * @return the operate result
     */
    boolean setClock(ApplicationLayerAlarmPacket alarm) {
        if (D) Log.d(TAG, "setClocks()");
        initialCommandSend();
        ApplicationLayerAlarmsPacket alarms = new ApplicationLayerAlarmsPacket();
        alarms.add(alarm);
        // Try to set alarms
        mApplicationLayer.settingCmdAlarmsSetting(alarms);

        return waitCommandSend();
    }

    /*
     * Use to sync the clock
     *
     * @return the operate result
     */
    boolean setClocksSyncRequest() {
        if (D) Log.d(TAG, "SetClocksSyncRequest()");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.settingCmdRequestAlarmList();

        return waitCommandSend();
    }

    /*
     * Use to query the current device support function
     *
     * @return the operate result
     */
    boolean setFunctionsRequest() {
        if (D) Log.d(TAG, "setFunctionsRequest()");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.settingCmdFunctionsRequest();

        return waitCommandSend();
    }


    /*
     * Use to sync the time
     *
     * @return the operate result
     */
    public boolean setTimeSync() {
        if (D) Log.d(TAG, "setTimeSync()");
        initialCommandSend();

        // Try to set time
        //in nexus9, Calendar's month is not right
        Calendar c1 = Calendar.getInstance();
        if (D) Log.d(TAG, "setTimeSync: " + c1.toString());
        mApplicationLayer.settingCmdTimeSetting(c1.get(Calendar.YEAR),
                c1.get(Calendar.MONTH) + 1, // here need add 1, because it origin range is 0 - 11;
                c1.get(Calendar.DATE),
                c1.get(Calendar.HOUR_OF_DAY),
                c1.get(Calendar.MINUTE),
                c1.get(Calendar.SECOND));

        return waitCommandSend();
    }

    /*
     * Use to send the call notify info
     *
     * @return the operate result
     */
    boolean sendCallNotifyInfo() {
        if (D) Log.d(TAG, "sendCallNotifyInfo");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdCallNotifyInfoSetting();

        return waitCommandSend();
    }

    boolean sendCallNotifyInfo(String show) {
        if (D) Log.d(TAG, "sendCallNotifyInfo");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdCallNotifyInfoSetting(show);

        return waitCommandSend();
    }

    /*
     * Use to send the call accept notify info
     *
     * @return the operate result
     */
    boolean sendCallAcceptNotifyInfo() {
        if (D) Log.d(TAG, "sendCallNotifyInfo");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdCallAcceptNotifyInfoSetting();

        return waitCommandSend();
    }

    /*
     * Use to send the call reject notify info
     *
     * @return the operate result
     */
    boolean sendCallRejectNotifyInfo() {
        if (D) Log.d(TAG, "sendCallNotifyInfo");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdCallRejectNotifyInfoSetting();

        return waitCommandSend();
    }

    /*
     * Use to send the other notify info
     *
     * @return the operate result
     */
    boolean sendOtherNotifyInfo(byte info) {
        if (D) Log.d(TAG, "SendOtherNotifyInfo, info: " + info);
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdOtherNotifyInfoSetting(info);

        return waitCommandSend();
    }

    boolean sendOtherNotifyInfo(byte info, String show) {
        if (D) Log.d(TAG, "SendOtherNotifyInfo, info: " + info + "; showData: " + show);
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdOtherNotifyInfoSetting(info, show);

        return waitCommandSend();
    }

    boolean sendOtherNotifyInfoWithVibrate(byte info, byte vibrateCount, String show) {
        if (D)
            Log.d(TAG, "SendOtherNotifyInfoWithVibrate, info: " + info + "; vibrateCount" + vibrateCount + "; showData: " + show);
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.notifyCmdOtherNotifyInfoSettingWithVibrateCount(info, vibrateCount, show);

        return waitCommandSend();
    }

    /*
     * Use to send enable fac test mode
     *
     * @return the operate result
     */
    boolean sendEnableFacTest() {
        if (D) Log.d(TAG, "SendEnableFacTest");
        if (mWristState != CommandManager.LoginState.STATE_WRIST_INITIAL) {
            return false;
        }
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.facCmdEnterTestMode(null);

        return waitCommandSend();
    }

    /*
     * Use to send disable fac test mode
     *
     * @return the operate result
     */
    boolean sendDisableFacTest() {
        if (D) Log.d(TAG, "SendDisableFacTest");
        if (mWristState != CommandManager.LoginState.STATE_WRIST_ENTER_TEST_MODE) {
            return false;
        }
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.facCmdExitTestMode(null);

        return waitCommandSend();
    }

    /*
     * Use to send enable led
     *
     * @param led the led want to enable
     * @return the operate result
     */
    boolean sendEnableFacLed(byte led) {
        if (D) Log.d(TAG, "SendEnableFacLed");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.facCmdEnableLed(led);

        return waitCommandSend();
    }

    /*
     * Use to send enable vibrate
     *
     * @return the operate result
     */
    boolean sendEnableFacVibrate() {
        if (D) Log.d(TAG, "SendEnableFacVibrate");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.facCmdEnableVibrate();

        return waitCommandSend();
    }

    /*
     * Use to send request sensor data
     *
     * @return the operate result
     */
    boolean sendEnableFacSensorDataRequest() {
        if (D) Log.d(TAG, "SendEnableFacSensorData");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.facCmdRequestSensorData();

        return waitCommandSend();
    }

    /*
     * Use to send open log command
     *
     * @return the operate result
     */
    boolean sendLogEnableCommand(byte[] keyArray) {
        if (D) Log.d(TAG, "SendLogEnableCommand");
        initialCommandSend();

        mApplicationLayer.logCmdOpenLog(keyArray);

        return waitCommandSend();
    }

    boolean sendLogEnableCommand() {
        int cnt = 0;
        byte[] temp = new byte[ApplicationLayer.DEBUG_LOG_TYPE_MAX_CNT];
        if (SPWristbandConfigInfo.getDebugLogTypeModuleApp(mContext)) {
            temp[cnt] = DEBUG_LOG_TYPE_MODULE_APP;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeModuleUpperStack(mContext)) {
            temp[cnt] = DEBUG_LOG_TYPE_MODULE_UPSTACK;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeModuleLowerStack(mContext)) {
            temp[cnt] = DEBUG_LOG_TYPE_MODULE_LOWERSTACK;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeSleep(mContext)) {
            temp[cnt] = DEBUG_LOG_TYPE_SLEEP_DATA;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeSport(mContext)) {
            temp[cnt] = DEBUG_LOG_TYPE_SPORT_DATA;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeConfig(mContext)) {
            temp[cnt] = DEBUG_LOG_TYPE_CONFIG_DATA;
            cnt++;
        }
        if (cnt == 0) {
            if (D) Log.i(TAG, "No need to enable log.");
            return true;
        }
        byte[] keyArray = new byte[cnt];
        System.arraycopy(temp, 0, keyArray, 0, cnt);

        return sendLogEnableCommand(keyArray);
    }

    /*
     * Use to send open log command
     *
     * @return the operate result
     */
    boolean sendLogCloseCommand() {
        if (D) Log.d(TAG, "SendLogCloseCommand");
        initialCommandSend();

        mApplicationLayer.logCmdCloseLog();

        return waitCommandSend();
    }

    /*
     * Use to send request log command
     *
     * @return the operate result
     */
    boolean sendLogRequestCommand(byte key) {
        if (D) Log.d(TAG, "SendLogRequestCommand");
        initialCommandSend();

        mApplicationLayer.logCmdRequestLog(key);

        return waitCommandSend();
    }

    /*
     * Use to send sync today step command
     *
     * @param mode       mode of the recently sport packet.
     * @param activeTime activeTime of the recently sport packet.
     * @param calorie    mode of the recently sport packet.
     * @param step       mode of the recently sport packet.
     * @param distance   mode of the recently sport packet.
     * @return the operate result
     */
    boolean sendSyncTodayNearlyOffsetStepCommand(byte mode, int activeTime, long calorie, int step, int distance) {
        if (D) Log.d(TAG, "SendSyncTodayNearlyOffsetStepCommand");
        initialCommandSend();
        ApplicationLayerRecentlySportPacket packet =
                new ApplicationLayerRecentlySportPacket(mode, activeTime, calorie, step, distance);
        mApplicationLayer.sportDataCmdSyncRecently(packet);
        return waitCommandSend();
    }

    /*
     * Use to send remove bond command
     *
     * @return the operate result
     */
    boolean sendRemoveBondCommand() {

        if (D) Log.d(TAG, "SendRemoveBondCommand");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.bondCmdRequestRemoveBond();

        // when send remove bond command, we just think link is lost.
        return waitCommandSend();
    }

    public void closeGatt() {
        mApplicationLayer.closeGatt();
    }

    /*
     * Use to send camera control command
     *
     * @return the operate result
     */
    boolean sendCameraControlCommand(boolean enable) {
        if (D) Log.d(TAG, "SendCameraControlCommand, enable: " + enable);
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.controlCmdCameraControl(
                enable ? CAMERA_CONTROL_APP_IN_FORE : CAMERA_CONTROL_APP_IN_BACK);

        return waitCommandSend();
    }

    /*
     * Set the remote left right hand. Command: 0x02, Key: 0x22.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the left right hand mode. 0x01 is left ; 0x02 is right;
     * @return the operation result
     */
    boolean settingCmdLeftRightSetting(byte mode) {
        if (D) Log.d(TAG, "settingCmdLeftRightSetting, enable: " + mode);
        initialCommandSend();

        mApplicationLayer.settingCmdLeftRightSetting(mode);

        return waitCommandSend();
    }


    /*
     * Use to send sync today step command
     *
     * @return the operate result
     */
    boolean sendSyncTodayStepCommand(long step, long distance, long calorie) {
        if (D) Log.d(TAG, "SendSyncTodayStepCommand");
        initialCommandSend();
        mApplicationLayer.sportDataCmdSyncToday(step, distance, calorie);

        return waitCommandSend();
    }


    private boolean initialCommandSend() {

        synchronized (mCommandSendLock) {
            // init status
            mErrorStatus = false;
            isCommandSend = false;
            isCommandSendOk = false;
        }

        return true;
    }

    private boolean waitCommandSend() {
        if (D) Log.d(TAG, "waitCommandSend()");
        boolean commendSendReady = false;
        synchronized (mCommandSendLock) {
            if (D) Log.d(TAG, "mCommandSendLock isCommandSend :" + isCommandSend);

            if (!isCommandSend) {
                try {
                    // wait a while
                    if (D)
                        Log.d(TAG, "wait the time set callback, wait for: " + MAX_COMMAND_SEND_WAIT_TIME + "ms");
                    mCommandSendLock.wait(MAX_COMMAND_SEND_WAIT_TIME);

                    if (D) Log.d(TAG, "waitCommandSend, isCommandSendOk: " + isCommandSendOk);

                    commendSendReady = isCommandSendOk;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        if (D) Log.d(TAG, "waitCommandSend return :" + commendSendReady);
        return commendSendReady;
    }


    // Login response
    private boolean mLoginResponse;
    private int mLoginResponseStatus;

    /*
     * Request Login
     *
     * @return the login result, fail or success
     * @id the user id
     */
    private boolean RequestLogin(String id) {
        if (D) Log.d(TAG, "RequestLogin, id: " + id);
        // init error status
        mErrorStatus = false;
        isResponseCome = false;
        mLoginResponse = false;
        mLoginResponseStatus = ApplicationLayer.LOGIN_RSP_SUCCESS;

        // Try to login
        mApplicationLayer.bondCmdRequestLogin(id);// it will wait the onBondCmdRequestLogin callback invoke.

        synchronized (mRequestResponseLock) {
            if (!isResponseCome) {
                try {
                    // wait a while
                    if (D)
                        Log.d(TAG, "wait the login response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                    mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (!isResponseCome) {
            mErrorStatus = true;
            return false;
        }

        if (!mLoginResponse) {
            setDataSync(false);
        }

        LogUtils.d("isResponseCome : " + isResponseCome);
        LogUtils.d("mLoginResponseStatus : " + mLoginResponseStatus);
        LogUtils.d("mLoginResponse : " + mLoginResponse);
        if (isResponseCome
                && (mLoginResponseStatus == LOGIN_LOSS_LOGIN_INFO)) {
            //
            Log.w(TAG, "Be-careful, may be last connection loss sync info, do again.");
            setNeedInfoForLogin();
            // update state
            updateWristState(CommandManager.LoginState.STATE_WRIST_LOGIN);
        }
        return mLoginResponse;
    }

    // Login response
    private boolean mBondResponse;

    /*
     * Request Bond
     *
     * @return the bond result, fail or success
     * @id the user id
     */
    private boolean RequestBond(String id) {
        if (D) Log.d(TAG, "RequestBond, id: " + id);
        // init error status
        mErrorStatus = false;
        isResponseCome = false;
        mBondResponse = false;

        // Try to login
        mApplicationLayer.bondCmdRequestBond(id);// it will wait the onBondCmdRequestLogin callback invoke.

        synchronized (mRequestResponseLock) {
            if (!isResponseCome) {
                try {
                    // wait a while
                    if (D)
                        Log.d(TAG, "wait the bond response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                    mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (!isResponseCome) {
            mErrorStatus = true;
            return false;
        }
        return mBondResponse;
    }

    public boolean isReady() {
        if (D) Log.d(TAG, "isReady, mWristState: " + mWristState);
        return mWristState == CommandManager.LoginState.STATE_WRIST_SYNC_DATA;
    }

    /*
     * Use to set the remote Immediate Alert Level
     *
     * @param enable enable/disable Immediate Alert
     */
    boolean enableImmediateAlert(boolean enable) {
        initialCommandSend();
        boolean result = mImmediateAlertService.enableAlert(enable);
        return result;
    }

    /*
     * Use to set the remote Link Loss Alert Level
     *
     * @param enable enable/disable Link Loss Alert
     */
    boolean enableLinkLossAlert(boolean enable) {
        initialCommandSend();
        boolean result = mLinkLossService.enableAlert(enable);
        return result;
    }

    /*
     * Use to read the remote Battery Level
     */
    boolean readBatteryLevel() {
        if (D) Log.d(TAG, "readBatteryLevel");
        //isInSendCommand = true;
        initialCommandSend();

        boolean result = mBatteryService.readInfo();

        return result;
    }

    /*
     * Use to read the remote Hrp Level
     */
    boolean readHrpValue() {
        if (D) Log.d(TAG, "readHrpValue");
        initialCommandSend();

        mApplicationLayer.sportDataCmdHrpSingleRequest(true);

        return waitCommandSend();
    }

    /*
     * Use to read the remote Hrp Level
     */
    boolean stopReadHrpValue() {
        if (D) Log.d(TAG, "stopReadHrpValue");
        initialCommandSend();

        mApplicationLayer.sportDataCmdHrpSingleRequest(false);

        return waitCommandSend();
    }

    /*
     * Use to read the remote Hrp Level
     */
    boolean readBPValue() {
        if (D) Log.d(TAG, "readHrpValue");
        initialCommandSend();

        mApplicationLayer.sportDataCmdBloodPressureRequest(true);

        return waitCommandSend();
    }

    /*
     * Use to read the remote Hrp Level
     */
    boolean stopReadBPValue() {
        if (D) Log.d(TAG, "stopReadHrpValue");
        initialCommandSend();

        mApplicationLayer.sportDataCmdBloodPressureRequest(false);

        return waitCommandSend();
    }

    /*
     * Use to read the remote Link loss Level
     */
    boolean readLinkLossLevel() {
        if (D) Log.d(TAG, "readLinkLossLevel");
        initialCommandSend();

        if (!mLinkLossService.readInfo()) {
            if (D) Log.e(TAG, "readLinkLossLevel, failed");
            return false;
        }

        return waitCommandSend();
    }

    /*
     * Use to read the remote version info
     */
    boolean readDfuVersion() {
        if (D) Log.d(TAG, "readDfuVersion");

        // add by ivan. thread sleep to avoid obtains firmware version fail.
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (null == mOTAService) {
            if (D) Log.e(TAG, "null == mOTAService");

            return false;
        }

        initialCommandSend();

        if (!mOTAService.readInfo()) {
            if (D) Log.e(TAG, "readDfuVersion, failed");
            return false;
        }

        return waitCommandSend();
    }

    /*
     * Use to get the remote Battery Level
     */
    int getBatteryLevel() {
        if (mBatteryService != null) {
            return mBatteryService.getBatteryValue();
        } else {
            return -1;
        }
    }

    /*
     * Use to check support extend flash
     */
    boolean checkSupportedExtendFlash() {
        if (mOTAService != null) {
            return mOTAService.checkSupportedExtendFlash();
        } else {
            return false;
        }
    }

    /*
     * Check if there is a new firmware version
     *
     * @param vendor
     * @param deviceType
     */
    boolean checkNewFwVersion(String appVersion, String userId, String vendor, String deviceType) {
        if (mOTAService == null) return false;
        mOTAService.checkNewFWVersion(appVersion, userId, vendor, deviceType);
        return true;
    }

    boolean downloadFirmware() {
        if (mOTAService == null) return false;
        mOTAService.downloadFirmware();
        return true;
    }

    /*
     * Check if there is a new firmware version
     *
     * @param vendor
     * @param deviceType
     */
    private void checkNewFwPatchVersion(String appVersion, String userId, String vendor, String deviceType) {
        if (mOTAService == null) return;
        mOTAService.checkNewPatchVersion(appVersion, userId, vendor, deviceType);
    }

    /*
     * just for test
     *
     * @param userId
     * @param vendor
     */
    public void checkAllFWVersion(String userId, String vendor) {
        if (mOTAService == null) return;
        mOTAService.checkAllNewFWVersion(userId, vendor, "k9");
    }

    public void checkLastFirmware(String userId, String appVersion, String patchVersion) {
        if (mOTAService == null) return;
        mOTAService.checkLastFirmwares(userId, appVersion, patchVersion);
    }

    public boolean isConnected() {
        return mApplicationLayer.isConnected();
    }

    public void download(String url) {
        if (mOTAService == null) return;
        mOTAService.download(url);
    }

    public int getCurrentFWVersion() {
        if (mOTAService == null) return -1;
        return mOTAService.getAppValue();
    }

    public int getPatchVersion() {
        if (mOTAService == null) return -1;
        return mOTAService.getPatchValue();
    }


    /*
     * Use to enable battery power notification
     */
    public boolean enableBatteryNotification(boolean enable) {
        initialCommandSend();
        // enable notification
        boolean result = mBatteryService.enableNotification(enable);

        return result;
    }

    private static void registerDateChangeBroadcast() {
        if (D) Log.i(TAG, "registerDateChangeBroadcast");
//        unregisterDateChangeBroadcast();
        mSystemDateChangeBroadcastReceive = new SystemDateChangeBroadcastReceive();
//        synchronized (mSystemDateChangeBroadcastReceive) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SystemDateChangeBroadcastReceive.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(SystemDateChangeBroadcastReceive.ACTION_DATE_CHANGED);
        intentFilter.addAction(SystemDateChangeBroadcastReceive.ACTION_TIME_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        mContext.registerReceiver(mSystemDateChangeBroadcastReceive, intentFilter);
//        }
    }

    public void unregisterDateChangeBroadcast() {

        if (D) Log.i(TAG, "unregisterDateChangeBroadcast");
        if (mSystemDateChangeBroadcastReceive != null) {
            synchronized (mSystemDateChangeBroadcastReceive) {
                Intent intent = new Intent();
                intent.setAction(SystemDateChangeBroadcastReceive.ACTION_TIMEZONE_CHANGED);
                intent.setAction(SystemDateChangeBroadcastReceive.ACTION_DATE_CHANGED);
                intent.setAction(SystemDateChangeBroadcastReceive.ACTION_TIME_CHANGED);
                PackageManager pm = mContext.getPackageManager();
                List<ResolveInfo> resolveInfos = pm.queryBroadcastReceivers(intent, 0);
                if (resolveInfos != null && !resolveInfos.isEmpty()) {
                    //查询到相应的BroadcastReceiver
                    LogUtils.d("查询到相应的BroadcastReceiver ---- unregisterDateChangeBroadcast");
                    mContext.unregisterReceiver(mSystemDateChangeBroadcastReceive);
                    mSystemDateChangeBroadcastReceive = null;
                }
            }
        }
    }


    // The Handler that gets information back from test thread
    //private class MyHandler extends Handler {
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATE_CONNECTED:
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            if (null == callback) continue;
                            callback.onConnectionStateChange(true);
                        }
                    }
                    break;
                case MSG_STATE_DISCONNECTED:

                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onConnectionStateChange(false);
                        }
                        // close all
                        close();
                    }

                    break;
                case MSG_ERROR:
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onError(msg.arg1);
                        }
                    }

                    break;
                case MSG_WRIST_STATE_CHANGED:
                    if (D) Log.d(TAG, "MSG_WRIST_STATE_CHANGED, current state: " + msg.arg1);

                    LogUtils.d("mCallbacks size : " + mCallbacks.size());

                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onLoginStateChange(msg.arg1);
                        }
                    }
                    //mCallback.onLoginStateChange(msg.arg1);
                    break;
                case MSG_RECEIVE_SPORT_INFO:

                    synchronized (mCallbacks) {
                        if (D) Log.d(TAG, "MSG_RECEIVE_SPORT_INFO, current state: " + msg.arg1);

                        ApplicationLayerSportPacket sportPacket = (ApplicationLayerSportPacket) msg.obj;
                        ArrayList<ApplicationLayerSportItemPacket> sportItems = sportPacket.getSportItems();
                        ArrayList<SportItemPacket> itemPackets = new ArrayList<>();
                        for (int j = 0; j < sportItems.size(); j++) {
                            ApplicationLayerSportItemPacket packet = sportItems.get(j);
                            SportItemPacket itemPacket = new SportItemPacket(packet.getOffset(), packet.getMode(),
                                    packet.getStepCount(), packet.getActiveTime(), packet.getCalory(), packet.getDistance());
                            itemPackets.add(itemPacket);
                        }

                        SportPacket packet = new SportPacket(sportPacket.getYear(), sportPacket.getMonth(),
                                sportPacket.getDay(), sportPacket.getItemCount(), itemPackets);

                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onSportDataReceivedIndication(packet);
                        }
                    }

                    break;
                case MSG_RECEIVE_HISTORY_SPORT_INFO:
                    synchronized (mCallbacks) {
                        if (D)
                            Log.d(TAG, "MSG_RECEIVE_HISTORY_SPORT_INFO, current state: " + msg.arg1);

                        ApplicationLayerSportPacket sportPacket = (ApplicationLayerSportPacket) msg.obj;
                        ArrayList<ApplicationLayerSportItemPacket> sportItems = sportPacket.getSportItems();
                        ArrayList<SportItemPacket> itemPackets = new ArrayList<>();
                        for (int j = 0; j < sportItems.size(); j++) {
                            ApplicationLayerSportItemPacket packet = sportItems.get(j);
                            SportItemPacket itemPacket = new SportItemPacket(packet.getOffset(), packet.getMode(),
                                    packet.getStepCount(), packet.getActiveTime(), packet.getCalory(), packet.getDistance());
                            itemPackets.add(itemPacket);
                        }

                        SportPacket packet = new SportPacket(sportPacket.getYear(), sportPacket.getMonth(),
                                sportPacket.getDay(), sportPacket.getItemCount(), itemPackets);

                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onSportDataCmdHistorySyncEnd(packet);
                        }
                    }
                    break;
                case MSG_RECEIVE_HISTORY_SPORT_END:
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onHistoryDataSyncEnd();
                        }
                    }
                    break;

                case MSG_RECEIVE_SLEEP_INFO:
                    synchronized (mCallbacks) {

                        ApplicationLayerSleepPacket sleep = (ApplicationLayerSleepPacket) msg.obj;
                        ArrayList<ApplicationLayerSleepItemPacket> sleepItems = sleep.getSleepItems();
                        ArrayList<SleepItemPacket> itemPackets = new ArrayList<>();
                        for (int j = 0; j < sleepItems.size(); j++) {
                            SleepItemPacket itemPacket = new SleepItemPacket(sleepItems.get(j)
                                    .getMinutes(), sleepItems.get(j).getMode());
                            itemPackets.add(itemPacket);
                        }
                        SleepPacket packet = new SleepPacket(sleep.getYear(), sleep.getMonth(), sleep.getDay(), sleep.getItemCount(), itemPackets);


                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onSleepDataReceivedIndication(packet);
                        }
                    }

                    break;
                case MSG_RECEIVE_NOTIFY_MODE_SETTING:

                    if (D) Log.w(TAG, "Current notify setting is: " + msg.arg1);
                    SPWristbandConfigInfo.setNotifyCallFlag(mContext, (msg.arg1 & CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_CALL) != 0);
                    SPWristbandConfigInfo.setNotifyMessageFlag(mContext, (msg.arg1 & CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_MESSAGE) != 0);
                    if (isNotifyManageEnabled()) {
                        SPWristbandConfigInfo.setNotifyQQFlag(mContext, (msg.arg1 & CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_QQ) != 0);
                        SPWristbandConfigInfo.setNotifyWechatFlag(mContext, (msg.arg1 & CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_WECHAT) != 0);
                    } else {
                        if (D) Log.w(TAG, "Notify not enable, should not enable these setting.");
                    }


                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onNotifyModeSettingReceived(msg.arg1);
                        }
                    }
                    break;
                case MSG_RECEIVE_LONG_SIT_SETTING:
                    byte longsitmode = (byte) msg.obj;
                    if (D) Log.w(TAG, "Current long sit setting is: " + longsitmode);

                    SPWristbandConfigInfo.setControlSwitchLongSit(mContext, longsitmode == ApplicationLayer.LONG_SIT_CONTROL_ENABLE);

                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onLongSitSettingReceived(longsitmode == LONG_SIT_CONTROL_ENABLE);
                        }
                    }
                    break;
                case MSG_RECEIVE_TURN_OVER_WRIST_SETTING:
                    boolean enable = (boolean) msg.obj;
                    if (D) Log.w(TAG, "Current turn over wrist setting is: " + enable);
                    SPWristbandConfigInfo.setControlSwitchTurnOverWrist(mContext, enable);
                    synchronized (mRequestResponseLock) {
                        isResponseCome = true;
                        mRequestResponseLock.notifyAll();
                    }
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onTurnOverWristSettingReceived(enable);
                        }
                    }
                    break;

                case MSG_RECEIVE_ALARMS_INFO:
                    //Reset all first.
                    SPWristbandConfigInfo.setAlarmControlOne(mContext, false);
                    SPWristbandConfigInfo.setAlarmControlTwo(mContext, false);
                    SPWristbandConfigInfo.setAlarmControlThree(mContext, false);

                    ApplicationLayerAlarmsPacket alarmPacket = (ApplicationLayerAlarmsPacket) msg.obj;
                    for (final ApplicationLayerAlarmPacket p : alarmPacket.getAlarms()) {
                        byte dayFlag = p.getDayFlags();
                        String hour = String.valueOf(p.getHour()).length() == 1
                                ? "0" + String.valueOf(p.getHour())
                                : String.valueOf(p.getHour());
                        String minute = String.valueOf(p.getMinute()).length() == 1
                                ? "0" + String.valueOf(p.getMinute())
                                : String.valueOf(p.getMinute());
                        String timeString = hour + ":" + minute;
                        // set check
                        if (p.getId() == 0) {
                            SPWristbandConfigInfo.setAlarmControlOne(mContext, true);
                            SPWristbandConfigInfo.setAlarmTimeOne(mContext, timeString);
                            SPWristbandConfigInfo.setAlarmFlagOne(mContext, dayFlag);
                        } else if (p.getId() == 1) {
                            SPWristbandConfigInfo.setAlarmControlTwo(mContext, true);
                            SPWristbandConfigInfo.setAlarmTimeTwo(mContext, timeString);
                            SPWristbandConfigInfo.setAlarmFlagTwo(mContext, dayFlag);
                        } else {
                            SPWristbandConfigInfo.setAlarmControlThree(mContext, true);
                            SPWristbandConfigInfo.setAlarmTimeThree(mContext, timeString);
                            SPWristbandConfigInfo.setAlarmFlagThree(mContext, dayFlag);
                        }
                        //mCallback.onSleepDataReceive(sleepData);
                    }
                    synchronized (mRequestResponseLock) {
                        isResponseCome = true;
                        mRequestResponseLock.notifyAll();
                    }
                    synchronized (mCallbacks) {
                        ArrayList<ApplicationLayerAlarmPacket> alarms = alarmPacket.getAlarms();
                        ArrayList<AlarmClockBean> packets = new ArrayList<>();
                        for (int j = 0; j < alarms.size(); j++) {
                            ApplicationLayerAlarmPacket packet = alarms.get(j);
                            AlarmClockBean clockPacket = new AlarmClockBean(packet.getHour(), packet.getMinute(),
                                    packet.getDayFlags(), packet.getId(), true);
                            packets.add(clockPacket);
                        }

                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onAlarmsDataReceived(packets);
                        }
                    }
                    break;
                case MSG_RECEIVE_TAKE_PHOTO_RSP:
                    if (D) Log.d(TAG, "MSG_RECEIVE_TAKE_PHOTO_RSP");
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onTakePhotoRsp();
                        }
                    }
                    break;
                case MSG_RECEIVE_FAC_SENSOR_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_FAC_SENSOR_INFO");
                    ApplicationLayerFacSensorPacket sensorPacket = (ApplicationLayerFacSensorPacket) msg.obj;
                    if (D) Log.d(TAG, "Receive Fac Sensor info, X: " + sensorPacket.getX() +
                            ", Y: " + sensorPacket.getY() +
                            ", Z: " + sensorPacket.getZ());
                    // show state
                    synchronized (mCallbacks) {
                        FactorySensorPacket packet = new FactorySensorPacket(sensorPacket.getX(),
                                sensorPacket.getY(), sensorPacket.getZ());
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onFacSensorDataReceived(packet);
                        }
                    }
                    break;
                case MSG_RECEIVE_LOG_START:
                    if (D) Log.d(TAG, "MSG_RECEIVE_LOG_START");
                    long logLength = (long) msg.obj;

                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onLogCmdStart(logLength);
                        }
                    }
                    break;
                case MSG_RECEIVE_LOG_END:
                    if (D) Log.d(TAG, "MSG_RECEIVE_LOG_END");
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onLogCmdEnd();
                        }
                    }
                    break;
                case MSG_RECEIVE_LOG_RSP:
                    if (D) Log.d(TAG, "MSG_RECEIVE_LOG_RSP");
                    ApplicationLayerLogResponsePacket logResponsePacket = (ApplicationLayerLogResponsePacket) msg.obj;
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onLogCmdRsp(StringByteTrans.byte2HexStr(logResponsePacket.getData()));
                        }
                    }
                    break;
                case MSG_RECEIVE_DFU_VERSION_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_DFU_VERSION_INFO");
                    int appVersion = msg.arg1;
                    int patchVersion = msg.arg2;
                    if (D) Log.d(TAG, "Receive dfu version info, appVersion: " + appVersion +
                            ", patchVersion: " + patchVersion);
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onVersionRead(appVersion, patchVersion);
                        }
                    }
                    break;
                case MSG_RECEIVE_DEVICE_NAME_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_DEVICE_NAME_INFO");
                    String name = (String) msg.obj;
                    if (D) Log.d(TAG, "Receive device name info, name: " + name);
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onNameRead(name);
                        }
                    }
                    break;
                case MSG_RECEIVE_BATTERY_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_BATTERY_INFO");
                    int battery = msg.arg1;
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onBatteryRead(battery);
                        }
                    }
                    break;
                case MSG_RECEIVE_BATTERY_CHANGE_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_BATTERY_CHANGE_INFO");
                    int batteryChange = msg.arg1;

                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onBatteryChange(batteryChange);
                        }
                    }
                    break;
                case MSG_RECEIVE_HRP_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_HRP_INFO");
                    ApplicationLayerHrpPacket hrp = (ApplicationLayerHrpPacket) msg.obj;
                    for (ApplicationLayerHrpItemPacket p : hrp.getHrpItems()) {
                        if (D) Log.d(TAG, "Find a hrp item, Minutes: " + p.getMinutes() +
                                ", Value: " + p.getValue());
                        synchronized (mCallbacks) {
                            for (int i = 0; i < mCallbacks.size(); i++) {
                                CommandCallback callback = mCallbacks.get(i);
                                callback.onHrpDataReceivedIndication(p.getMinutes(), p.getValue());
                            }
                        }
                    }
                    break;
                case MSG_RECEIVE_HISTORY_HRP_INFO:
                    if (D) Log.d(TAG, "MSG_RECEIVE_HISTORY_HRP_INFO");
                    ApplicationLayerHrpPacket hrpPkt = (ApplicationLayerHrpPacket) msg.obj;
                    Calendar hrpsCal = Calendar.getInstance();
                    hrpsCal.clear();
                    for (ApplicationLayerHrpItemPacket p : hrpPkt.getHrpItems()) {
                        if (D) Log.d(TAG, "Find a hrp item, Minutes: " + p.getMinutes() +
                                ", Value: " + p.getValue());
                        synchronized (mCallbacks) {
                            for (int i = 0; i < mCallbacks.size(); i++) {
                                hrpsCal.set(2000 + hrpPkt.getYear(), hrpPkt.getMonth() - 1, hrpPkt.getDay());
                                CommandCallback callback = mCallbacks.get(i);
                                callback.onHistoryHrpDataReceivedIndication(hrpsCal.getTimeInMillis(),
                                        p.getMinutes(), p.getValue());
                            }
                        }
                    }
                    break;
                case MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ:
                    if (D) Log.d(TAG, "MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ");

                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onDeviceCancelSingleHrpRead();
                        }
                    }
                    break;
                case MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP:
                    if (D) Log.d(TAG, "MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP");
                    /*
                    synchronized (mCallbacks) {
						                        for (int i = 0; i < mCallbacks.size(); i++) { CommandCallback callback = mCallbacks.get(i);
							callback.onDeviceCancelSingleHrpRead();
						}
					}*/
                    break;
                case MSG_RECEIVE_DISPLAY_SWITCH_RETURN:
                    if (D) Log.d(TAG, "MSG_RECEIVE_DISPLAY_SWITCH_RETURN");
                    isResponseCome = true;

                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onDisplaySwitchReturn((int) msg.obj);
                        }
                    }
                    break;

                case MSG_RECEIVE_BLOOD_PRESSURE_RSP:
                    ApplicationLayerBPPacket BPPkts = (ApplicationLayerBPPacket) msg.obj;
                    ArrayList<BloodPressurePacket> itemList = new ArrayList<>();
                    for (int i = 0; i < BPPkts.getHrpItems().size(); i++) {
                        ApplicationLayerBPItemPacket itemPacket = BPPkts.getHrpItems().get(i);
                        BloodPressurePacket item = new BloodPressurePacket();
                        item.setBpHigh(itemPacket.getBpHigh());
                        item.setBpLow(itemPacket.getBpLow());
                        item.setHeartRate(itemPacket.getHeartrate());
                        item.setSecond(itemPacket.getSecond());
                        item.setTimeStamp(itemPacket.getTimeStamp());
                        itemList.add(item);
                    }
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onBloodPressureDataReceived(itemList);
                        }
                    }
                    break;
                case MSG_RECEIVE_BLOOD_PRESSURE_END:
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onBloodPressureMeasurementStopped();
                        }
                    }
                    break;
                case MSG_RECEIVE_FUNCTIONS:
                    FunctionsBean fb = (FunctionsBean) msg.obj;
                    synchronized (mCallbacks) {
                        for (int i = 0; i < mCallbacks.size(); i++) {
                            CommandCallback callback = mCallbacks.get(i);
                            callback.onFunctions(fb);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // Application Layer callback
    private final ApplicationLayerCallback mApplicationCallback = new ApplicationLayerCallback() {
        @Override
        public void onConnectionStateChange(final boolean status, final boolean newState) {
            if (D)
                Log.d(TAG, "onConnectionStateChange, status: " + status + ", newState: " + newState);
            // if already connect to the remote device, we can do more things here.
            if (status && newState) {
                sendMessage(MSG_STATE_CONNECTED, null, -1, -1);
            } else {
                sendMessage(MSG_STATE_DISCONNECTED, null, -1, -1);
            }
        }

        @Override
        public void onSettingCmdRequestAlarmList(final ApplicationLayerAlarmsPacket alarms) {
            if (D) Log.d(TAG, "ApplicationLayerAlarmsPacket");
            sendMessage(MSG_RECEIVE_ALARMS_INFO, alarms, -1, -1);
        }

        @Override
        public void onSettingCmdRequestNotifySwitch(final byte mode) {
            if (D) Log.d(TAG, "onSettingCmdRequestNotifySwitch");
            int notifyMode = 0;
            switch ((int) mode) {
                case CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_CALL:
                    notifyMode = CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_CALL;
                    break;
                case CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_QQ:
                    notifyMode = CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_QQ;
                    break;
                case CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_WECHAT:
                    notifyMode = CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_WECHAT;
                    break;
                case CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_MESSAGE:
                    notifyMode = CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_MESSAGE;
                    break;
                default:
                    notifyMode = CommandManager.NotifyState.NOTIFY_SWITCH_SETTING_MESSAGE;
                    break;
            }
            sendMessage(MSG_RECEIVE_NOTIFY_MODE_SETTING, null, notifyMode, -1);
        }

        @Override
        public void onSettingCmdRequestLongSit(final byte mode) {
            if (D) Log.d(TAG, "onSettingCmdRequestLongSit");
            sendMessage(MSG_RECEIVE_LONG_SIT_SETTING, mode, -1, -1);
        }

        @Override
        public void onTurnOverWristSettingReceive(final boolean mode) {
            if (D) Log.d(TAG, "onTurnOverWristSettingReceived");
            sendMessage(MSG_RECEIVE_TURN_OVER_WRIST_SETTING, mode, -1, -1);
        }

        @Override
        public void onSportDataCmdSportData(final ApplicationLayerSportPacket sport) {
            if (D)
                Log.d(TAG, "onSportDataCmdSportData with ControlManager state is " + mWristState);
            ApplicationLayerSportPacket sportPacket = sport;

            if (D) Log.d(TAG, "mWristState != STATE_WRIST_SYNC_HISTORY_DATA>>>>>>>>>>>1");

            if (mWristState != CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_DATA) {
                if (D)
                    Log.d(TAG, "mWristState != STATE_WRIST_SYNC_HISTORY_DATA>>>>>>>>>>>2" + sport.getItemCount());

                sendMessage(MSG_RECEIVE_SPORT_INFO, sport, -1, -1);
            } else {
                sendMessage(MSG_RECEIVE_HISTORY_SPORT_INFO, sport, -1, -1);
            }
        }

        @Override
        public void onSportDataCmdSleepData(final ApplicationLayerSleepPacket sleep) {
            if (D) Log.d(TAG, "onSportDataCmdSleepData");

            if (D) Log.d(TAG, "Receive a sleep packet, Year: " + (sleep.getYear() + 2000) +
                    ", Month: " + sleep.getMonth() +
                    ", Day: " + sleep.getDay() +
                    ", Item count: " + sleep.getItemCount());

            sendMessage(MSG_RECEIVE_SLEEP_INFO, sleep, -1, -1);
        }

        @Override
        public void onSportDataCmdHistorySyncBegin() {
            if (D) Log.d(TAG, "onSportDataCmdHistorySyncBegin");
            updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_DATA);
        }

        @Override
        public void onSportDataCmdHistorySyncEnd(final ApplicationLayerTodaySumSportPacket packet) {
            if (D) Log.d(TAG, "onTodaySumSportDataReceived");

            Observable.just(new Object()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            synchronized (mCallbacks) {
                                for (int i = 0; i < mCallbacks.size(); i++) {
                                    CommandCallback callback = mCallbacks.get(i);
                                    callback.onTodaySumSportDataReceived(packet.getTotalStep()
                                            , packet.getTotalCalory(), packet.getTotalDistance());
                                }
                            }
                        }
                    });

            updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_DATA);
            sendMessage(MSG_RECEIVE_HISTORY_SPORT_END, null, -1, -1);
        }

        @Override
        public void onSportDataCmdHrpData(final ApplicationLayerHrpPacket hrp) {
            if (D) Log.d(TAG, "onSportDataCmdHrpData");
            ApplicationLayerHrpPacket hrpPacket = hrp;
            if (D) Log.d(TAG, "Receive a hrp packet, Year: " + (hrpPacket.getYear() + 2000) +
                    ", Month: " + hrpPacket.getMonth() +
                    ", Day: " + hrpPacket.getDay() +
                    ", Item count: " + hrpPacket.getItemCount());


            if (mWristState != CommandManager.LoginState.STATE_WRIST_SYNC_HISTORY_DATA) {
                sendMessage(MSG_RECEIVE_HRP_INFO, hrp, -1, -1);
            } else {
                sendMessage(MSG_RECEIVE_HISTORY_HRP_INFO, hrp, -1, -1);
            }
        }

        @Override
        public void onSportDataCmdDeviceCancelSingleHrpRead() {
            if (D) Log.d(TAG, "onSportDataCmdDeviceCancelSingleHrpRead");
            sendMessage(MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ, null, -1, -1);
        }

        @Override
        public void onSportDataCmdHrpContinueParamRsp(boolean enable, int interval) {
            if (D)
                Log.d(TAG, "onSportDataCmdHrpContinueParamRsp, enable: " + enable + ", interval: " + interval);
            SPWristbandConfigInfo.setContinueHrpControl(mContext, enable);

            sendMessage(MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP, null, -1, -1);
        }

        @Override
        public void onSportDataCmdBPData(ApplicationLayerBPPacket packet) {

            sendMessage(MSG_RECEIVE_BLOOD_PRESSURE_RSP, packet, -1, -1);
        }

        @Override
        public void onSportDataCmdBPSyncEnd() {
            sendMessage(MSG_RECEIVE_BLOOD_PRESSURE_END, null, -1, -1);

        }

        @Override
        public void onTakePhotoRsp() {
            if (D) Log.d(TAG, "onTakePhotoRsp");
            sendMessage(MSG_RECEIVE_TAKE_PHOTO_RSP, null, -1, -1);
        }

        @Override
        public void onFACCmdSensorData(final ApplicationLayerFacSensorPacket sensor) {
            if (D) Log.d(TAG, "onFACCmdSensorData");
            sendMessage(MSG_RECEIVE_FAC_SENSOR_INFO, sensor, -1, -1);
        }

        @Override
        public void onBondCmdRequestBond(final byte status) {
            if (D) Log.d(TAG, "onBondCmdRequestBond, status: " + status);

            mBondResponse = (status == BOND_RSP_SUCCESS);

            synchronized (mRequestResponseLock) {
                isResponseCome = true;
                mRequestResponseLock.notifyAll();
            }
        }

        @Override
        public void onBondCmdRequestLogin(final byte status) {
            if (D) Log.d(TAG, "onBondCmdRequestLogin, status: " + status);
            // Login right
            if (status == ApplicationLayer.LOGIN_RSP_SUCCESS
                    || status == LOGIN_LOSS_LOGIN_INFO) {
                mLoginResponseStatus = status;
                mLoginResponse = true;
            }
            // Login error
            else {
                mLoginResponse = false;
            }
            synchronized (mRequestResponseLock) {
                isResponseCome = true;
                mRequestResponseLock.notifyAll();
            }
        }

        public void onEndCallReceived() {
            if (D) Log.d(TAG, "onEndCallReceived");
//            endCall();
        }

        @Override
        public void onLogCmdStart(final long logLength) {
            if (D) Log.d(TAG, "onLogCmdStart");
            updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_LOG_DATA);
            sendMessage(MSG_RECEIVE_LOG_START, logLength, -1, -1);
        }

        @Override
        public void onLogCmdEnd() {
            if (D) Log.d(TAG, "onLogCmdEnd");
            updateWristState(CommandManager.LoginState.STATE_WRIST_SYNC_DATA);
            sendMessage(MSG_RECEIVE_LOG_END, null, -1, -1);
        }

        @Override
        public void onLogCmdRsp(final ApplicationLayerLogResponsePacket packet) {
            if (D) Log.d(TAG, "onLogCmdRsp");
            sendMessage(MSG_RECEIVE_LOG_RSP, packet, -1, -1);
        }

        @Override
        public void onCommandSend(final boolean status, byte command, byte key) {
            if (D)
                Log.d(TAG, "onCommandSend, status: " + status + ", command: " + command + ", key: " + key);
            // if command send not right(no ACK). we just close it, and think connection is wrong.
            // Or, we can try to reconnect, or do other things.
            if (!status) {
                isCommandSendOk = false;
                sendErrorMessage(CommandManager.ErrorCode.ERROR_CODE_COMMAND_SEND_ERROR);
                //mApplicationLayer.close(); // error
            } else {
                isCommandSendOk = true;
                if (command == ApplicationLayer.CMD_FACTORY_TEST) {
                    if (key == ApplicationLayer.KEY_FAC_TEST_ENTER_SPUER_KEY) {
                        updateWristState(CommandManager.LoginState.STATE_WRIST_ENTER_TEST_MODE);
                    } else if (key == ApplicationLayer.KEY_FAC_TEST_LEAVE_SPUER_KEY) {
                        updateWristState(CommandManager.LoginState.STATE_WRIST_INITIAL);
                    }
                }
            }
            synchronized (mCommandSendLock) {
                LogUtils.d(">>> CommandLock", "onCommandSend");

                isCommandSend = true;
                mCommandSendLock.notifyAll();
            }
        }


        @Override
        public void onNameReceive(final String data) {
            sendMessage(MSG_RECEIVE_DEVICE_NAME_INFO, data, -1, -1);
            synchronized (mCommandSendLock) {
                isCommandSend = true;
                mCommandSendLock.notifyAll();
            }
        }

        @Override
        public void onDisplaySwitchReturn(byte status) {
            sendMessage(MSG_RECEIVE_DISPLAY_SWITCH_RETURN, (int) status, -1, -1);
        }

        @Override
        public void onFunctions(ApplicationLayerFunctions functions) {
            FunctionsBean functionsBean = new FunctionsBean();
            functionsBean.setFakeBP(functions.isFakeBP());
            functionsBean.setHeartRate(functions.isHeartRate());
            functionsBean.setPortrait_landscape(functions.isPortrait_landscape());
            functionsBean.setRealBP(functions.isRealBP());
            functionsBean.setSleep(functions.isSleep());
            functionsBean.setStepCounter(functions.isStepCounter());
            functionsBean.setWechat(functions.isWechat());
            sendMessage(MSG_RECEIVE_FUNCTIONS, functionsBean, -1, -1);

        }
    };

    private void sendErrorMessage(int error) {
        LogUtils.d(TAG, ">>>>>>>>>>>> error : " + error);
        sendMessage(MSG_ERROR, null, error, -1);
    }


    private void updateWristState(@CommandManager.LoginState int state) {
        LogUtils.d(TAG, ">>>>>>>>>>>> state " + state);
        // update the wrist state
        mWristState = state;
        sendMessage(MSG_WRIST_STATE_CHANGED, null, mWristState, -1);
    }

//    /*
//     *  
//     *      * 挂断电话 
//     *      
//     */
//    private void endCall() {
//        try {
//            Method method = Class.forName("android.os.ServiceManager")
//                    .getMethod("getService", String.class);
//            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
//            ITelephony telephony = ITelephony.Stub.asInterface(binder);
//            telephony.endCall();
//        } catch (NoSuchMethodException e) {
//            Log.d(TAG, "", e);
//        } catch (ClassNotFoundException e) {
//            Log.d(TAG, "", e);
//        } catch (Exception e) {
//        }
//
//    }

    /*
     * send message
     *
     * @param msgType Type message type
     * @param obj     object sent with the message set to null if not used
     * @param arg1    parameter sent with the message, set to -1 if not used
     * @param arg2    parameter sent with the message, set to -1 if not used
     **/
    private void sendMessage(int msgType, Object obj, int arg1, int arg2) {
        if (mHandler != null) {
            //	Message msg = new Message();
            Message msg = Message.obtain();
            msg.what = msgType;
            if (arg1 != -1) {
                msg.arg1 = arg1;
            }
            if (arg2 != -1) {
                msg.arg2 = arg2;
            }
            if (null != obj) {
                msg.obj = obj;
            }
            mHandler.sendMessage(msg);
        } else {
            if (D) Log.e(TAG, "handler is null, can't send message");
        }
    }

    @Override
    public void onBatteryValueReceive(int value) {
        if (D) Log.d(TAG, "onBatteryValueReceive, value: " + value);
        sendMessage(MSG_RECEIVE_BATTERY_INFO, null, value, -1);
    }

    @Override
    public void onBatteryValueChanged(int value) {
        if (D) Log.d(TAG, "onBatteryValueChanged, value: " + value);
        sendMessage(MSG_RECEIVE_BATTERY_CHANGE_INFO, null, value, -1);
    }

    @Override
    public void onHrpValueReceive(int value) {
        if (D) Log.d(TAG, "onHrpValueReceive, value: " + value);
        sendMessage(MSG_RECEIVE_HRP_INFO, null, value, -1);
    }


    @Override
    public void onVersionRead(int appVersion, int patchVersion) {
        if (D)
            Log.d(TAG, "onVersionRead, appVersion: " + appVersion + ", patchVersion: " + patchVersion);
        sendMessage(MSG_RECEIVE_DFU_VERSION_INFO, null, appVersion, patchVersion);
        SPWristbandConfigInfo.setFWVersion(mContext, appVersion);
        synchronized (mCommandSendLock) {
            isCommandSendOk = true;
            isCommandSend = true;
            mCommandSendLock.notifyAll();
        }
    }

    @Override
    public void noNewVersion(int code, String message) {
        synchronized (mCallbacks) {
            // do something
            for (int i = 0; i < mCallbacks.size(); i++) {
                CommandCallback callback = mCallbacks.get(i);
                callback.onNoNewVersion(code, message);
            }
        }
    }

    @Override
    public void hasNewVersion(String description, String version, String path) {
        synchronized (mCallbacks) {
            // do something
            for (int i = 0; i < mCallbacks.size(); i++) {
                CommandCallback callback = mCallbacks.get(i);
                callback.onHasNewVersion(description, version, path);
            }
        }

    }


    @Override
    public void allFirmware(ArrayList<AllFwResultBean.PayloadBean> firmwareList) {
        synchronized (mCallbacks) {
            // do something
            for (int i = 0; i < mCallbacks.size(); i++) {
                CommandCallback callback = mCallbacks.get(i);
                callback.allFWVersion(firmwareList);
            }
        }
    }


    @Override
    public void downloadProgress(int progressRate) {
        // show state
        synchronized (mCallbacks) {
            // do something
            for (int i = 0; i < mCallbacks.size(); i++) {
                CommandCallback callback = mCallbacks.get(i);
                callback.downloadProgress(progressRate);
            }
        }
    }

    @Override
    public void error(Throwable e,
                      @CommandManager.CheckFirmwareUpdatesError int errorCode) {
        synchronized (mCallbacks) {
            // do something
            for (int i = 0; i < mCallbacks.size(); i++) {
                CommandCallback callback = mCallbacks.get(i);
                callback.getNewFirmwareError(e, errorCode);

            }
        }
    }

    @Override
    public void downloadComplete(String fwPath) {
        synchronized (mCallbacks) {
            // do something
            for (int i = 0; i < mCallbacks.size(); i++) {
                CommandCallback callback = mCallbacks.get(i);
                callback.downloadComplete(fwPath);
            }
        }
    }

    @Override
    public void onLinkLossValueReceive(boolean value) {
        if (D) Log.d(TAG, "onLinkLossValueReceive, value: " + value);
        SPWristbandConfigInfo.setControlSwitchLost(mContext, value);
        synchronized (mCommandSendLock) {
            isCommandSendOk = true;
            isCommandSend = true;
            mCommandSendLock.notifyAll();
        }

        for (int i = 0; i < mCallbacks.size(); i++) {
            CommandCallback callback = mCallbacks.get(i);
            callback.onLinkLossValueReceive(value);
        }
    }

    private boolean isNotifyManageEnabled() {
        if (D) Log.d(TAG, "isNotifyManageEnabled");
        String pkgName = mContext.getPackageName();
        final String flat = Settings.Secure.getString(mContext.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
