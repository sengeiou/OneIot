package com.coband.interactivelayer.manager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.coband.dfu.BinInputStream;
import com.coband.dfu.OtaCallback;
import com.coband.dfu.OtaProxy;
import com.coband.interactivelayer.bean.AlarmClockBean;
import com.coband.interactivelayer.bean.AlarmHelper;
import com.coband.protocollayer.applicationlayer.ApplicationLayerAlarmPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerAlarmsPacket;
import com.coband.protocollayer.gattlayer.GlobalGatt;
import com.coband.utils.LogUtils;
import com.coband.utils.SPWristbandConfigInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mai on 17-7-18.
 */

public class CommandManager {

    private static final String TAG = "CommandManager";

    private static Context mContext;

    protected static CompositeDisposable mCompositeSubscription = new CompositeDisposable();

    // ota error
    @IntDef({
            CheckFirmwareUpdatesError.CHECK_VERSION_ERROR,
            CheckFirmwareUpdatesError.CHECK_PATCH_ERROR,
            CheckFirmwareUpdatesError.DOWNLOAD_ERROR
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface CheckFirmwareUpdatesError {
        int CHECK_VERSION_ERROR = 0x01;
        int CHECK_PATCH_ERROR = 0x02;
        int DOWNLOAD_ERROR = 0x03;
    }

    // Screen display state
    @IntDef({
            ScreenState.PORTRAIT,
            ScreenState.LANDSCAPE
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface ScreenState {
        int PORTRAIT = 0x01; // VERTICAL
        int LANDSCAPE = 0x00; // HORIZONTAL
    }

    // Login state
    @IntDef({
            LoginState.STATE_WRIST_INITIAL,
            LoginState.STATE_WRIST_LOGGING,
            LoginState.STATE_WRIST_BONDING,
            LoginState.STATE_WRIST_LOGIN,
            LoginState.STATE_WRIST_SYNC_DATA,
            LoginState.STATE_WRIST_SYNC_HISTORY_DATA,
            LoginState.STATE_WRIST_SYNC_LOG_DATA,
            LoginState.STATE_WRIST_ENTER_TEST_MODE,
            LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_PROFILE,
            LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_TARGET_STEP,
            LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_TIME_SYNC,
            LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_PHONE_OS,
            LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_FIRMWARE_VERSION,
            LoginState.STATE_WRIST_SYNC_HISTORY_ERROR_BATTERY_LEVEL
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface LoginState {
        int STATE_WRIST_INITIAL = 0;
        int STATE_WRIST_LOGGING = 1;
        int STATE_WRIST_BONDING = 2;
        int STATE_WRIST_LOGIN = 3;
        int STATE_WRIST_SYNC_DATA = 4;
        int STATE_WRIST_SYNC_HISTORY_DATA = 5;
        int STATE_WRIST_SYNC_LOG_DATA = 6;
        int STATE_WRIST_ENTER_TEST_MODE = 7;
        int STATE_WRIST_SYNC_HISTORY_ERROR_PROFILE = 8;
        int STATE_WRIST_SYNC_HISTORY_ERROR_TARGET_STEP = 9;
        int STATE_WRIST_SYNC_HISTORY_ERROR_TIME_SYNC = 10;
        int STATE_WRIST_SYNC_HISTORY_ERROR_PHONE_OS = 11;
        int STATE_WRIST_SYNC_HISTORY_ERROR_FIRMWARE_VERSION = 12;
        int STATE_WRIST_SYNC_HISTORY_ERROR_BATTERY_LEVEL = 13;

    }

    // Error state
    @IntDef({
            ErrorCode.ERROR_CODE_NO_LOGIN_RESPONSE_COME,
            ErrorCode.ERROR_CODE_BOND_ERROR,
            ErrorCode.ERROR_CODE_COMMAND_SEND_ERROR
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface ErrorCode {
        int ERROR_CODE_NO_LOGIN_RESPONSE_COME = 1;
        int ERROR_CODE_BOND_ERROR = 2; // connect band already bond
        int ERROR_CODE_COMMAND_SEND_ERROR = 3; //
    }

    // Notify state
    @IntDef({
            NotifyState.NOTIFY_SWITCH_SETTING_CALL,
            NotifyState.NOTIFY_SWITCH_SETTING_QQ,
            NotifyState.NOTIFY_SWITCH_SETTING_WECHAT,
            NotifyState.NOTIFY_SWITCH_SETTING_MESSAGE
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface NotifyState {
        int NOTIFY_SWITCH_SETTING_CALL = 0x01;
        int NOTIFY_SWITCH_SETTING_QQ = 0x02;
        int NOTIFY_SWITCH_SETTING_WECHAT = 0x04;
        int NOTIFY_SWITCH_SETTING_MESSAGE = 0x08;
    }

    // Notify mode
    @IntDef({
            NotifyMode.CALL_NOTIFY_MODE_ON,
            NotifyMode.CALL_NOTIFY_MODE_OFF,
            NotifyMode.CALL_NOTIFY_MODE_ENABLE_QQ,
            NotifyMode.CALL_NOTIFY_MODE_DISABLE_QQ,
            NotifyMode.CALL_NOTIFY_MODE_ENABLE_WECHAT,
            NotifyMode.CALL_NOTIFY_MODE_DISABLE_WECHAT,
            NotifyMode.CALL_NOTIFY_MODE_ENABLE_MESSAGE,
            NotifyMode.CALL_NOTIFY_MODE_DISABLE_MESSAGE
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface NotifyMode {
        byte CALL_NOTIFY_MODE_ON = 0x01;
        byte CALL_NOTIFY_MODE_OFF = 0x02;
        byte CALL_NOTIFY_MODE_ENABLE_QQ = 0x03;
        byte CALL_NOTIFY_MODE_DISABLE_QQ = 0x04;
        byte CALL_NOTIFY_MODE_ENABLE_WECHAT = 0x05;
        byte CALL_NOTIFY_MODE_DISABLE_WECHAT = 0x06;
        byte CALL_NOTIFY_MODE_ENABLE_MESSAGE = 0x07;
        byte CALL_NOTIFY_MODE_DISABLE_MESSAGE = 0x08;
    }

    // Notify mode
    @IntDef({
            NotifyInfo.OTHER_NOTIFY_INFO_QQ,
            NotifyInfo.OTHER_NOTIFY_INFO_WECHAT,
            NotifyInfo.OTHER_NOTIFY_INFO_MESSAGE
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface NotifyInfo {
        // notify flags
        byte OTHER_NOTIFY_INFO_QQ = 0x01;
        byte OTHER_NOTIFY_INFO_WECHAT = 0x02;
        byte OTHER_NOTIFY_INFO_MESSAGE = 0x04;
    }

    // week
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface Week {
        // notify flags
        byte TODAY = 0x00;
        byte MON = 0x01;
        byte TUE = 0x02;
        byte WED = 0x04;
        byte THURS = 0x08;
        byte FRI = 0x10;
        byte SAT = 0x20;
        byte SUN = 0x40;
    }

    @IntDef({
            SportState.STATIC,
            SportState.WALK,
            SportState.RUNNING
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface SportState {
        int STATIC = 0x00;
        int WALK = 0x01;
        int RUNNING = 0x02;
    }

    public static void init(Context context) {
        mContext = context;
        ControlManager.initial(context);
        ConnectManager.init(context);

//        BLEReceiver receiver = new BLEReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        context.registerReceiver(receiver, filter);

    }

    public static void registerCallback(CommandCallback callback) {
        ControlManager.getInstance().registerCallback(callback);
    }

    public static boolean isCallbackRegisted(CommandCallback callback) {
        return ControlManager.getInstance().isCallbackRegistered(callback);
    }

    public static void unRegisterCallback(CommandCallback callback) {
        ControlManager.getInstance().unRegisterCallback(callback);
    }

    static void sendCommand(ObservableOnSubscribe<Boolean> cmdObservable, final SendCommandCallback callback) {

        if (mCompositeSubscription.isDisposed()) {
            mCompositeSubscription = new CompositeDisposable();
        }
        if (!isConnected()) {
            if (null != callback) {
                Observable.just(new Object()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                callback.onDisconnected();
                            }
                        });
            }
            return;
        }
        Observable.create(cmdObservable)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (null != callback) {
                            callback.onCommandSend(aBoolean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (null != callback)
                            callback.onError(throwable);
                    }
                });
    }


    /**
     * set the name
     *
     * @param name the name
     */
    public static void setDeviceName(final String name, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setDeviceName(name));
            }
        }, callback);
    }


    /**
     * Query the name of the current device
     * <p>
     * <p>Results for query are reported using the
     * {@link CommandCallback#onNameRead} callback.
     */
    public static void getDeviceName(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().getDeviceName());
            }
        }, callback);
    }


    /**
     * Use to sync the notify mode
     *
     * @param mode 0x01 enable call notify; 0x02 disable call notify
     *             {@link NotifyMode#CALL_NOTIFY_MODE_ON}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_OFF}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_ENABLE_QQ}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_DISABLE_QQ}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_ENABLE_WECHAT}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_DISABLE_WECHAT}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_ENABLE_MESSAGE}
     *             {@link NotifyMode#CALL_NOTIFY_MODE_DISABLE_MESSAGE}
     */
    public static void setNotifyMode(@NotifyMode final int mode, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setNotifyMode((byte) mode));
            }
        }, callback);
    }

    /**
     * Use to send the other notify info
     * 　@param info
     * 　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_QQ}
     * 　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_WECHAT}
     * 　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_MESSAGE}
     *
     * @param callback Feedback processing after the command is sent
     */
    public static void sendOtherNotifyInfo(@NotifyInfo final int info, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendOtherNotifyInfo((byte) info));
            }
        }, callback);
    }

    /**
     * Send a reminder message to the Band device
     *
     * @param info     　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_QQ}
     *                 　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_WECHAT}
     *                 　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_MESSAGE}
     * @param show     　notify content
     * @param callback Feedback processing after the command is sent
     */
    public static void sendOtherNotifyInfo(@NotifyInfo final int info, final String show, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendOtherNotifyInfo((byte) info, show));
            }
        }, callback);
    }


    /**
     * Send a reminder message and vibrate count to the Band device
     *
     * @param info         　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_QQ}
     *                     　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_WECHAT}
     *                     　　　{@link NotifyInfo#OTHER_NOTIFY_INFO_MESSAGE}
     * @param vibrateCount vibrate count
     * @param show         　notify content
     * @param callback     Feedback processing after the command is sent
     */
    public static void sendOtherNotifyInfoWithVibrateCount(final int info,
                                                           final int vibrateCount,
                                                           final String show,
                                                           SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendOtherNotifyInfoWithVibrate((byte) info,
                        (byte) vibrateCount, show));
            }
        }, callback);
    }


    /**
     * Query the current message notify mode
     * <p>
     * <p>Results for query are reported using the
     * {@link CommandCallback#onNotifyModeSettingReceived} callback.
     */
    public static void sendNotifyModeRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendNotifyModeRequest());
            }
        }, callback);
    }


    /**
     * Use to start/stop data sync.
     * callback in {@link CommandCallback#onHistoryDataSyncEnd}
     *
     * @param enable start or stop real time data sync.
     */
    public static void setDataSync(final boolean enable, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setDataSync(enable));
            }
        }, callback);
    }


    /**
     * Use to sync history data.
     * <p>
     * <p>Results for ｀Blood　Pressure　Data｀ are reported using the
     * {@link CommandCallback#onBloodPressureDataReceived} callback.
     * <p>
     * <p>Results for ｀Heart Rate　Data｀ are reported using the
     * {@link CommandCallback#onHistoryHrpDataReceivedIndication} callback.
     * <p>
     * <p>When all data sync complete are reported using the
     * {@link CommandCallback#onHistoryDataSyncEnd} callback.
     */
    public static void syncHistoryDataRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendDataRequest());
            }
        }, callback);
    }


    /**
     * Use to enable/disable the long sit set
     *
     * @param enable     : enable long sit remind
     * @param alarmTime: alarm cycle
     */
    public static void setLongSit(final boolean enable, final int alarmTime, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setLongSit(enable, alarmTime));
            }
        }, callback);
    }

    /**
     * Use to enable/disable the long sit set, and setup alarmTime
     *
     * @param alarmTime: alarm cycle
     * @param daysFlag   :
     *                   byte TODAY = 0x00; Valid only for the day.
     *                   byte MON = 0x01;
     *                   byte TUE = 0x02;
     *                   byte WED = 0x04;
     *                   byte THURS = 0x08;
     *                   byte FRI = 0x10;
     *                   byte SAT = 0x20;
     *                   byte SUN = 0x40;
     *                   e.g. if you want remind at monday and tuesday ,
     *                   you can setting {@link Week#MON} | {@link Week#TUE} , just setting 0x01 | 0x02 = 0x03
     */
    public static void setLongSit(final boolean enable, final int alarmTime, final int startTime, final int endTime, final byte daysFlag, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setLongSit(enable, alarmTime, startTime, endTime, daysFlag));
            }
        }, callback);
    }


    /**
     * Use to setting `Sedentary reminder`
     */
    @Deprecated
    public static void setLongSit(final byte enable, final int threshold, final int notify, final int start, final int stop, final byte dayflags, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setLongSit(enable, threshold, notify, start, stop, dayflags));
            }
        }, callback);
    }


    /**
     * Query current status of `Sedentary reminder`
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onTurnOverWristSettingReceived} callback.
     */
    public static void sendLongSitRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendLongSitRequest());
            }
        }, callback);
    }


    /**
     * Use to enable/disable the periodic heart rate test
     *
     * @param interval Automatic heart rate test cycle
     */
    public static void setContinueHrp(final boolean enable, final int interval, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setContinueHrp(enable, interval));
            }
        }, callback);
    }


    /**
     * Use to enable/disable the continue hrp set
     */
    public static void setContinueHrp(final boolean enable, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setContinueHrp(enable));
            }
        }, callback);
    }


    /**
     * Query current status of `periodic heart rate test`
     * <p>
     * 　<p>Results of the query are reported using the
     * {@link CommandCallback#onHrpContinueParamRsp} callback.
     *
     * @param callback
     */
    public static void sendContinueHrpParamRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendContinueHrpParamRequest());
            }
        }, callback);
    }


    /**
     * Use to enable/disable the `turn over wrist`(`Raise the bright screen`)
     */
    public static void setTurnOverWrist(final boolean enable, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setTurnOverWrist(enable));
            }
        }, callback);
    }


    /**
     * Query the functionality of the current device support
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onTurnOverWristSettingReceived} callback.
     */
    public static void setFunctionsRequest(SendCommandCallback callback) {
        LogUtils.d(TAG, "setFunctionsRequest()");
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setFunctionsRequest());
            }
        }, callback);
    }


    /**
     * Query current status of `Raise the bright screen`
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onTurnOverWristSettingReceived} callback.
     */
    public static void sendTurnOverWristRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendTurnOverWristRequest());
            }
        }, callback);
    }


    /**
     * Use to sync the user profile, use local info
     */
    public static void setUserProfile(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setUserProfile());
            }
        }, callback);
    }


    /**
     * Use to sync the user profile
     *
     * @param sex false : female
     *            true : man
     */
    public static void setUserProfile(final boolean sex, final int age, final int height, final int weight, SendCommandCallback callback) {

        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setUserProfile(sex, age, height, weight));
            }
        }, callback);
    }


    /**
     * Use to sync the target step
     */
    public static void setTargetStep(final long step, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setTargetStep(step));
            }
        }, callback);
    }


    /**
     * Use to sync the target step, user local info
     */
    public static void setTargetStep(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setTargetStep());
            }
        }, callback);
    }


    /**
     * Use to set the phone os
     */
    public static void setPhoneOS(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setPhoneOS());
            }
        }, callback);
    }


    /**
     * Use to set the clocks
     */
    public static void setClocks(@NonNull final ArrayList<AlarmClockBean> alarms, final SendCommandCallback callback) {
        AlarmHelper.syncAlarmListToRemote(alarms, mContext, callback);
    }

    /**
     * Get current Alarm list form cache
     * <p>
     * <p>real time :
     *
     * @return current alarm list
     * @see CommandManager#sendClocksSyncRequest
     */
    public static ArrayList<AlarmClockBean> getAlarmClocks() {
        return AlarmHelper.getCurrentAlarmList(mContext);
    }

    /**
     * Use to set the clocks
     *
     * @param alarms
     * @see CommandManager#setClocks
     */
    @Deprecated
    public static void setClocksPacket(final ApplicationLayerAlarmsPacket alarms, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setClocks(alarms));
            }
        }, callback);
    }

    /**
     * Use to set the clock
     */
    private static void setClock(final ApplicationLayerAlarmPacket alarm, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setClock(alarm));
            }
        }, callback);
    }

    /**
     * Use to set alarm
     *
     * @param hour     Hour parameter of alarm clock
     * @param minute   Minute parameter of alarm clock
     * @param daysFlag :
     *                 byte TODAY = 0x00; Valid only for the day.
     *                 byte MON = 0x01;
     *                 byte TUE = 0x02;
     *                 byte WED = 0x04;
     *                 byte THURS = 0x08;
     *                 byte FRI = 0x10;
     *                 byte SAT = 0x20;
     *                 byte SUN = 0x40;
     *                 e.g. if you want remind at monday and tuesday ,
     *                 you can setting {@link Week#MON} | {@link Week#TUE} , just setting 0x01 | 0x02 = 0x03
     * @param alarmId  Valid value 0-7, alarm clock ID number, currently supports 8 alarm clock
     * @param callback 　Feedback processing after the command is sent
     */
    public static void setClockAlarm(@IntRange(from = 0, to = 23) int hour, @IntRange(from = 0, to = 59) int minute,
                                     byte daysFlag, @IntRange(from = 0, to = 7) int alarmId, SendCommandCallback callback) {
        Calendar c1 = Calendar.getInstance();
        int hourOfDay = c1.get(Calendar.HOUR_OF_DAY);
        boolean dayAddFlagOne = false;
        LogUtils.d(TAG, "syncAlarmListToRemote, hourOne: " + hour + ", minuteOne: " + minute);
        if (daysFlag == 0x00) {
            if (hour < c1.get(Calendar.HOUR_OF_DAY)) {
                dayAddFlagOne = true;
            } else if ((hour == hourOfDay)
                    && (minute <= c1.get(Calendar.MINUTE))) {
                dayAddFlagOne = true;
            }
        }
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, (dayAddFlagOne ? 1 : 0));

        ApplicationLayerAlarmPacket alarmOne =
                new ApplicationLayerAlarmPacket(c2.get(Calendar.YEAR),
                        c2.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
                        c2.get(Calendar.DAY_OF_MONTH),
                        hour,
                        minute,
                        alarmId,
                        daysFlag);

        /* Can use setClocks if setting multiple alarm */
        CommandManager.setClock(alarmOne, callback);
    }


    /**
     * Query the list of alarm clocks stored in the Band device
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onAlarmsDataReceived} callback.
     */
    public static void sendClocksSyncRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setClocksSyncRequest());
            }
        }, callback);
    }


    /**
     * Use to sync the time to Band device.
     */
    public static void setTimeSync(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().setTimeSync());
            }
        }, callback);

    }


    /**
     * Use to send the call notify info
     */
    public static void sendCallNotifyInfo(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendCallNotifyInfo());
            }
        }, callback);
    }


    public static void sendCallNotifyInfo(final String show, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendCallNotifyInfo(show));
            }
        }, callback);
    }


    /**
     * Use to send the call accept notify info
     */
    public static void sendCallAcceptNotifyInfo(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendCallAcceptNotifyInfo());
            }
        }, callback);
    }


    /**
     * Use to send the call reject notify info
     */
    public static void sendCallRejectNotifyInfo(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendCallRejectNotifyInfo());
            }
        }, callback);
    }


    /**
     * Use to send enable fac test mode
     */
    public static void sendEnableFacTest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendEnableFacTest());
            }
        }, callback);
    }


    /**
     * Use to send disable fac test mode
     */
    public static void sendDisableFacTest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendDisableFacTest());
            }
        }, callback);
    }


    /**
     * Use to send enable led
     * just for test
     *
     * @param led the led want to enable
     */
    public static void sendEnableFacLed(final byte led, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendEnableFacLed(led));
            }
        }, callback);
    }


    /**
     * Use to send enable vibrate
     * just for test
     */
    public static void sendEnableFacVibrate(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendEnableFacVibrate());
            }
        }, callback);
    }


    /**
     * Use to send request sensor data
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onFacSensorDataReceived} callback.
     * just for test
     */
    public static void sendEnableFacSensorDataRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendEnableFacSensorDataRequest());
            }
        }, callback);
    }


    /**
     * Use to send open log command
     * just for test
     */
    public static void sendLogEnableCommand(final byte[] keyArray, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendLogEnableCommand(keyArray));
            }
        }, callback);
    }


    /**
     * just for test
     */
    public static void sendLogEnableCommand(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendLogEnableCommand());
            }
        }, callback);
    }


    /**
     * Use to send open log command
     * just for test
     */
    public static void sendLogCloseCommand(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendLogCloseCommand());
            }
        }, callback);
    }


    /**
     * Use to send request log command
     * just for test
     */
    public static void sendLogRequestCommand(final byte key, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendLogRequestCommand(key));
            }
        }, callback);
    }


    /**
     * Use to send remove bond command
     */
    @Deprecated
    public static void sendRemoveBondCommand(SendCommandCallback callback) {

        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendRemoveBondCommand());
            }
        }, callback);
    }


    /**
     * Use to send camera control command
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onFacSensorDataReceived} callback.
     * <p>
     * <p>if is enable,will callback{@link CommandCallback#onTakePhotoRsp} when user Shake the band
     */
    public static void sendCameraControlCommand(final boolean enable, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendCameraControlCommand(enable));
            }
        }, callback);
    }


    /**
     * Set the remote left right hand. Command: 0x02, Key: 0x22.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * reporting the result of the operation.
     *
     * @param mode the left right hand mode. 0x01 is left ; 0x02 is right;
     */
    @Deprecated
    public static void settingCmdLeftRightSetting(final byte mode, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().settingCmdLeftRightSetting(mode));
            }
        }, callback);
    }


    /**
     * Send the number of steps in the last 15 minutes to the band,
     * both use with {@link CommandManager#sendSyncTodayStepCommand}
     *
     * @param mode static , walk and running
     */
    public static void sendSyncTodayNearlyOffsetStepCommand(@SportState final int mode, final int activeTime, final long calorie,
                                                            final int step, final int distance, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendSyncTodayNearlyOffsetStepCommand((byte) mode, activeTime, calorie, step, distance));
            }
        }, callback);
    }


    /**
     * Send the number of steps in the today to the band,
     * both use with {@link CommandManager#sendSyncTodayNearlyOffsetStepCommand}
     */
    public static void sendSyncTodayStepCommand(final long step, final long distance, final long calorie, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendSyncTodayStepCommand(step, distance, calorie));
            }
        }, callback);
    }


    /**
     * Use to set the remote Link Loss Alert Level
     *
     * @param enable enable/disable Link Loss Alert
     */
    @Deprecated
    public static void enableLinkLossAlert(final boolean enable, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().enableLinkLossAlert(enable));
            }
        }, callback);
    }


    /**
     * Use to read the remote battery Level
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onBatteryRead(int)} callback.
     * <p>
     * <p>The cache value of battery can get
     * by {@link CommandManager#getBatteryLevel()}, but it's not real time
     */
    public static void readBatteryLevel(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().readBatteryLevel());
            }
        }, callback);
    }


    /**
     * Use to read the remote Hrp Level
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onHrpDataReceivedIndication} callback.
     */
    public static void readHrpValue(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().readHrpValue());
            }
        }, callback);
    }


    /**
     * Stop heart rate test
     */
    public static void stopReadHrpValue(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().stopReadHrpValue());
            }
        }, callback);
    }


    /**
     * Use to read the remote blood pressure Level
     * <p>
     * <p>Results of the read are reported using the
     * {@link CommandCallback#onBloodPressureDataReceived} callback.
     */
    public static void readBPValue(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().readBPValue());
            }
        }, callback);
    }


    /**
     * Use to read the remote blood pressure Level
     */
    public static void stopReadBPValue(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().stopReadBPValue());
            }
        }, callback);
    }


    /**
     * Use to read the remote Link loss Level
     */
    @Deprecated
    public static void readLinkLossLevel(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().readLinkLossLevel());
            }
        }, callback);
    }


    /**
     * Use to read the remote version info
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onVersionRead} callback.
     */
    public static void readDfuVersion(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().readDfuVersion());
            }
        }, callback);
    }


    /**
     * Query the current display status of the band device
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onDisplaySwitchReturn} callback.
     * <p>
     * <p>
     * Screen display status :
     * {@link CommandManager.ScreenState#PORTRAIT}
     * {@link CommandManager.ScreenState#LANDSCAPE}
     */
    public static void sendDisplaySwitchRequest(SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendDisplaySwitchRequest());
            }
        }, callback);
    }


    /**
     * Set the display status of the screen
     *
     * @param status {@link CommandManager.ScreenState#PORTRAIT} or {@link CommandManager.ScreenState#LANDSCAPE}
     */
    public static void sendDisplaySwitchSetting(@CommandManager.ScreenState final int status, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().sendDisplaySwitchSetting(status));
            }
        }, callback);
    }


    /**
     * Send the order to shake the device
     *
     * @param enable enable/disable Immediate Alert
     */
    public static void enableImmediateAlert(final boolean enable, SendCommandCallback callback) {
        sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(ControlManager.getInstance().enableImmediateAlert(enable));
            }
        }, callback);
    }


    /**
     * Use to get the remote Battery Level
     */
    public static int getBatteryLevel() {
        return ControlManager.getInstance().getBatteryLevel();
    }


    /**
     * Use to check support extend flash
     */
    public static boolean checkSupportedExtendFlash() {
        return ControlManager.getInstance().checkSupportedExtendFlash();
    }


    /**
     * Check if there is a new firmware version
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onNoNewVersion} callback when there is no new firmware
     * <p>
     * <p>Results of the query are reported using the
     * {@link CommandCallback#onHasNewVersion} callback when has new version
     *
     * @param userId 　user id
     * @param vendor vendor
     */
    public static void checkNewFirmwareVersion(String appVersion, String userId, String vendor, String deviceType) {
        ControlManager.getInstance().checkNewFwVersion(appVersion, userId, vendor, deviceType);
    }


    /**
     * Call to download firmware after return has new firmware
     * by the {@link CommandManager#checkNewFirmwareVersion} function
     * <p>
     * <p>Requires {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
     * <p>
     * The storage path defaults to Environment.getExternalStorageDirectory().getPath() + "/fw/";
     */
    public static boolean downloadFirmware() {
        return ControlManager.getInstance().downloadFirmware();
    }

//    /**
//     * Call to download firmware after return has new firmware
//     * by the {@link CommandManager#checkNewFirmwareVersion} function
//     * <p>
//     * <p>Requires {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
//     *
//     * @param storagePath Customized storage path for firmware
//     * @return
//     */
//    public static boolean downloadFirmware(String storagePath) {
//        return ControlManager.getInstance().downloadFirmware(storagePath);
//    }

    /**
     * @param url
     * @see {@link CommandManager#downloadFirmware}
     */
    @Deprecated
    public static void download(String url) {
        ControlManager.getInstance().download(url);
    }


    /**
     * Check if there is a new firmware Patch version
     *
     * @param vendor vendor
     * @see CommandManager#checkNewFirmwareVersion
     */
    @Deprecated
    public static boolean checkNewFwPatchVersion(String appVersion, String userId, String vendor, String deviceType) {
        return ControlManager.getInstance().checkNewFwVersion(appVersion, userId, vendor, deviceType);
    }

    // just for test use
    public static void checkAllFwVersion(String userId, String vendor) {
        ControlManager.getInstance().checkAllFWVersion(userId, vendor);
    }

    /**
     * Used for silent upgrades
     * get the latest firmware version
     * can get download address in {@link CommandCallback#onHasNewVersion}
     * and {@link CommandCallback#onNoNewVersion}
     */
    public static void checkLastFirmware(String userId, String appVersion, String patchVersion) {
        ControlManager.getInstance().checkLastFirmware(userId, appVersion, patchVersion);
    }


    /**
     * real time ge current firmware version　after bind band
     * need bind band
     *
     * @return Current firmware version
     */
    public static int getCurrentFWVersion() {
        return ControlManager.getInstance().getCurrentFWVersion();
    }

    public static int getCurrentPatchVersion() {
        return ControlManager.getInstance().getPatchVersion();
    }

    /**
     * Gets the firmware version of the binding device，
     * Whether the bracelet is connected or not, but not real time
     */
    public static int getFirmwareVersion(Context context) {
        return SPWristbandConfigInfo.getFWVersion(context);
    }


    /**
     * Use to enable battery power notification
     * Callback{@link CommandCallback#onBatteryChange(int)}
     *
     * @return true : enable battery notification; false : disable battery notification
     */
    public static boolean enableBatteryNotification(boolean enable) {
        return ControlManager.getInstance().enableBatteryNotification(enable);
    }


    /**
     * Start OTA
     *
     * @param dfu          A OtaProxy proxy object
     * @param firmwarePath Firmware path
     * @return true is success , false is failed
     */
    public static boolean startOTA(OtaProxy dfu, String firmwarePath) {
        ControlManager mWristbandManager = ControlManager.getInstance();

        if (dfu == null || firmwarePath == null) {
            Log.e(TAG, "the realsil dfu didn't ready");
            return false;
        }

        // set the total speed for android 4.4, to escape the internal error
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dfu.setSpeedControl(true, 1024);
        } else if (android.os.Build.MODEL.equals("MI 5")) {
            LogUtils.d(TAG, " >>> android.os.Build.MODEL == MI 5)");
            dfu.setSpeedControl(true, 2048);
        }
        // Use GlobalGatt do not need to disconnect, just unregister the callback
        GlobalGatt.getInstance().unRegisterAllCallback();

        if (mWristbandManager.checkSupportedExtendFlash()) {
            dfu.setWorkMode(OtaProxy.OTA_MODE_EXTEND_FUNCTION);
        } else {
            dfu.setWorkMode(OtaProxy.OTA_MODE_FULL_FUNCTION);
        }
        Log.e(TAG, "Start OTA, address is: " + mWristbandManager.getBluetoothAddress());
        if (dfu.start(mWristbandManager.getBluetoothAddress(), firmwarePath)) {
            Log.d(TAG, "ota start >>>>>>>>>");
            return true;
        } else {
            Log.e(TAG, "something error in device info or the file, false");
            return false;
        }
    }


    /**
     * init OTA proxy
     *
     * @param context  context
     * @param listener the callback for ota
     */
    public static void initOtaProxy(Context context, OtaCallback listener) {
        OtaProxy.getDfuProxy(context, listener);
    }


    /**
     * Get Ota file version
     *
     * @param path ota file path
     * @return -1 is get fail , other is version
     */
    public static int getOtaFileVersion(String path) {
        // check the file path
        if (TextUtils.isEmpty(path)) {
            Log.e("TAG", "the file path string is null");
            return -1;
        }

        // check the file type
        if (!MimeTypeMap.getFileExtensionFromUrl(path).equalsIgnoreCase("BIN")) {
            Log.e("TAG", "the file type is not right");
            return -1;
        }
        BinInputStream binInputStream;

        //get the new firmware version
        try {
            binInputStream = openInputStream(path);
        } catch (final IOException e) {
            Log.e(TAG, "An exception occurred while opening file", e);
            return -1;
        }

        short i = binInputStream.binFileVersion();
        // close the file
        if (binInputStream != null) {
            try {
                binInputStream.close();
                binInputStream = null;
            } catch (IOException e) {
                Log.e(TAG, "error in close file", e);
            }
        }
        return i;
    }


    /**
     * Opens the binary input stream from a BIN file. A Path to the BIN file is given.
     *
     * @param filePath the path to the BIN file
     * @return the binary input stream with BIN data
     * @throws IOException throws the IO exception
     */
    private static BinInputStream openInputStream(final String filePath) throws IOException {
        final InputStream is = new FileInputStream(filePath);
        return new BinInputStream(is);
    }


    /**
     * Gets the MAC address of the binding device
     */
    public static String getBondDeviceMac(Context context) {
        return SPWristbandConfigInfo.getBondedDevice(context);
    }


    public static boolean isConnected() {
        return ControlManager.getInstance().isConnected();
    }


    /**
     * Disconnect band
     */
    public static void disconnect() {
        ConnectManager.getInstance().disconnect();
    }


//    private static class BLEReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
//                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
//                        BluetoothAdapter.ERROR);
//                switch (state) {
//                    case BluetoothAdapter.STATE_OFF:
//                        if (isConnected()) {
//                            LogUtils.d(TAG, "STATE_OFF isConnected");
//                            disconnect();
//                        }
//                        LogUtils.d(TAG, "STATE_OFF ：　Bluetooth off");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        LogUtils.d(TAG, "STATE_TURNING_OFF : Bluetooth turning off");
//                        break;
//                    case BluetoothAdapter.STATE_ON:
//                        LogUtils.d(TAG, "STATE_ON : Bluetooth on");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_ON:
//                        LogUtils.d(TAG, "STATE_TURNING_ON : Bluetooth turning on");
//                        break;
//                }
//            }
//        }
//    }
}
