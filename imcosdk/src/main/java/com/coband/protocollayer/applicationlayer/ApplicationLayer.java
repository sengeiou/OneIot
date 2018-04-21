package com.coband.protocollayer.applicationlayer;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.coband.interactivelayer.manager.CommandManager;
import com.coband.protocollayer.transportlayer.TransportLayer;
import com.coband.protocollayer.transportlayer.TransportLayerCallback;
import com.coband.utils.LogUtils;
import com.coband.utils.StringByteTrans;

import java.util.List;


public class ApplicationLayer {
    // Log
    private final static String TAG = "ApplicationLayer";
    private final static boolean D = true;

    // Support Command Id
    public final static byte CMD_IMAGE_UPDATE = 0x01;
    public final static byte CMD_SETTING = 0x02;
    public final static byte CMD_BOND_REG = 0x03;
    public final static byte CMD_NOTIFY = 0x04;
    public final static byte CMD_SPORTS_DATA = 0x05;
    public final static byte CMD_FACTORY_TEST = 0x06;
    public final static byte CMD_CTRL = 0x07;
    public final static byte CMD_DUMP_STACK = 0x08;
    public final static byte CMD_TEST_FLASH = 0x09;
    public final static byte CMD_LOG = 0x0a;

    /*CMD_UPDATE : key */
    public final static byte KEY_UPDATE_REQUEST = 0x01;
    public final static byte KEY_UPDATE_RESPONSE = 0x02;
    public final static byte KEY_UPDATE_RESPONSE_OK = 0x00;
    public final static byte KEY_UPDATE_RESPONSE_ERR = 0x01;
    public final static byte UPDATE_RESPONSE_ERRCODE = 0x01;       //low power

    /*CMD_SETTING : key */
    public final static byte KEY_SETTING_TIMER = 0x01;
    public final static byte KEY_SETTING_ALARM = 0x02;
    public final static byte KEY_SETTING_GET_ALARM_LIST_REQ = 0x03;
    public final static byte KEY_SETTING_GET_ALARM_LIST_RSP = 0x04;
    public final static byte KEY_SETTING_STEP_TARGET = 0x05;
    public final static byte KEY_SETTING_USER_PROFILE = 0x10;
    public final static byte KEY_SETTING_LOST_MODE = 0x20;
    public final static byte KEY_SETTING_SIT_TOOLONG_NOTIFY = 0x21;
    public final static byte KEY_SETTING_LEFT_RIGHT_HAND_NOTIFY = 0x22;
    public final static byte KEY_SETTING_PHONE_OS = 0x23;
    public final static byte KEY_SETTING_INCOMING_CALL_LIST = 0x24;
    public final static byte KEY_SETTING_INCOMING_ON_OFF = 0x25;
    public final static byte KEY_SETTING_SIT_TOOLONG_SWITCH_REQ = 0x26;
    public final static byte KEY_SETTING_SIT_TOOLONG_SWITCH_RSP = 0x27;
    public final static byte KEY_SETTING_NOTIFY_SWITCH_REQ = 0x28;
    public final static byte KEY_SETTING_NOTIFY_SWITCH_RSP = 0x29;

    public final static byte KEY_SETTING_TURN_OVER_WRIST_SET = 0x2A;
    public final static byte KEY_SETTING_TURN_OVER_WRIST_REQ = 0x2B;
    public final static byte KEY_SETTING_TURN_OVER_WRIST_RSP = 0x2C;
    public final static byte KEY_DISPLAY_SWITCH_SETTING = 0x33;
    public final static byte KEY_DISPLAY_SWITCH_REQUEST = 0x34;
    public final static byte KEY_DISPLAY_SWITCH_RETURN = 0x35;
    public final static byte KEY_FUNCTIONS_REQUEST = 0x36;
    public final static byte KEY_FUNCTIONS_RETURN = 0x37;
    //public final static byte KEY_SETTING_INCOMING_NOTIFY = 0x26;

    /*CMD_BOND_REG : key */
    public final static byte KEY_BOND_REQ = 0x01;
    public final static byte KEY_BOND_RSP = 0x02;
    public final static byte KEY_LOGIN_REQ = 0x03;
    public final static byte KEY_LOGIN_RSP = 0x04;
    public final static byte KEY_UNBOND = 0x05;
    public final static byte KEY_SUP_BOND_KEY = 0x06;
    public final static byte KEY_SUP_BOND_KEY_RSP = 0x07;

    /* CMD_NOTIFY: key:*/
    public final static byte KEY_NOTIFY_IMCOMING_CALL = 0x01;
    public final static byte KEY_NOTIFY_IMCOMING_CALL_ACC = 0x02;
    public final static byte KEY_NOTIFY_IMCOMING_CALL_REJ = 0x03;
    public final static byte KEY_NOTIFY_INCOMING_OTHER_NOTIFY = 0x04;
    public final static byte KEY_NOTIFY_END_CALL = 0x05;
    public final static byte KEY_NOTIFY_INCOMING_UNIVERSAL_OTHER_NOTIFY = 0x06;

    /* CMD_SPORTS: key:*/
    public final static byte KEY_SPORTS_REQ = 0x01;
    public final static byte KEY_SPORTS_RUNNIG_RSP = 0x02;
    public final static byte KEY_SPORTS_SLEEP_RSP = 0x03;
    public final static byte KEY_SPORTS_RUNNIG_RSP_MORE = 0x04;
    public final static byte KEY_SPORTS_SLEEP_SET_RSP = 0x05;
    public final static byte KEY_SPORTS_DATA_SYNC = 0x06;
    public final static byte KEY_SPORTS_HIS_SYNC_BEG = 0x07;
    public final static byte KEY_SPORTS_HIS_SYNC_END = 0x08;
    public final static byte KEY_SPORTS_DATA_TODAY_SYNC = 0x09;
    public final static byte KEY_SPORTS_DATA_LAST_SYNC = 0x0a;
    public final static byte KEY_SPORTS_DATA_TODAY_ADJUST = 0x0b;// 需要确认的功能
    public final static byte KEY_SPORTS_DATA_TODAY_ADJUST_RETURN = 0x0c;// 需要确认的功能
    public final static byte KEY_SPORTS_HRP_SINGLE_REQ = 0x0d;// 单次读取心率请求
    public final static byte KEY_SPORTS_HRP_CONTINUE_SET = 0x0e;// 连续心率采集设定
    public final static byte KEY_SPORTS_HRP_DATA_RSP = 0x0f;// 心率数据返回
    public final static byte KEY_SPORTS_HRP_DEVICE_CANCEL_READ_HRP = 0x10;// 手环取消单次心率读取
    public final static byte KEY_SPORTS_HRP_CONTINUE_PARAMS_GET = 0x11;// 连续心率采集参数获取
    public final static byte KEY_SPORTS_HRP_CONTINUE_PARAMS_RSP = 0x12;// 连续心率采集参数返回
    public final static byte KEY_SPORTS_BLOOD_PRESSURE_RSP= 0x13;// 血压数据返回
    public final static byte KEY_SPORTS_BLOOD_PRESSURE_REQ= 0x14;// 血压开关
    public final static byte KEY_SPORTS_BLOOD_PRESSURE_END= 0x15;// 血压测试结束


    /* CMD_FAC_TEST: key:*/
    public final static byte KEY_FAC_TEST_ECHO_REQ = 0x01;
    public final static byte KEY_FAC_TEST_ECHO_RSP = 0x02;
    public final static byte KEY_FAC_TEST_CHAR_REQ = 0x03;
    public final static byte KEY_FAC_TEST_CHAR_RSP = 0x04;
    public final static byte KEY_FAC_TEST_LED = 0x05;
    public final static byte KEY_FAC_TEST_MOTO = 0x06;
    public final static byte KEY_FAC_TEST_WRITE_SN = 0x07;
    public final static byte KEY_FAC_TEST_READ_SN = 0x08;
    public final static byte KEY_FAC_TEST_SN_RSP = 0x09;
    public final static byte KEY_FAC_TEST_WRITE_TEST_FLAG = 0x0a;
    public final static byte KEY_FAC_TEST_READ_TEST_FLAG = 0x0b;
    public final static byte KEY_FAC_TEST_FLAG_RSP = 0x0c;
    public final static byte KEY_FAC_TEST_SENSOR_DATA_REQ = 0x0d;
    public final static byte KEY_FAC_TEST_SENSOR_DATA_RSP = 0x0e;
    public final static byte KEY_FAC_TEST_ENTER_SPUER_KEY = 0x10;
    public final static byte KEY_FAC_TEST_LEAVE_SPUER_KEY = 0x11;
    public final static byte KEY_FAC_TEST_BUTTON_TEST = 0x21;
    public final static byte KEY_FAC_TEST_MOTO_OLD = 0x31;
    public final static byte KEY_FAC_TEST_LED_OLD = 0x32;

    /* CMD_CONTROL: key:*/
    public final static byte KEY_CTRL_PHOTO_RSP = 0x01;
    public final static byte KEY_CTRL_CLICK_RSP = 0x02;
    public final static byte KEY_CTRL_DOUBLE_CLICK_RSP = 0x03;
    public final static byte KEY_CTRL_APP_REQ = 0x11;

    /* CMD_DUMP: key:*/
    public final static byte KEY_DUMP_ASSERT_LOCATE_REQ = 0x01;
    public final static byte KEY_DUMP_ASSERT_LOCATE_RSP = 0x02;
    public final static byte KEY_DUMP_ASSERT_STACK_REQ = 0x03;
    public final static byte KEY_DUMP_ASSERT_STACK_RSP = 0x14;

    /* LOG: key:*/
    public final static byte KEY_LOG_FUNC_OPEN = 0x01;
    public final static byte KEY_LOG_FUNC_CLOSE = 0x02;
    public final static byte KEY_LOG_RSP = 0x03;
    public final static byte KEY_LOG_REQ = 0x04;
    public final static byte KEY_LOG_START = 0x05;
    public final static byte KEY_LOG_END = 0x06;


    /* Login response*/
    public final static byte LOGIN_RSP_SUCCESS = 0x00;
    public final static byte LOGIN_RSP_ERROR = 0x01;
    public final static byte LOGIN_LOSS_LOGIN_INFO = 0x02;

    /* Bond response*/
    public final static byte BOND_RSP_SUCCESS = 0x00;
    public final static byte BOND_RSP_ERROR = 0x01;

    /* Sport Sync Mode */
    public final static byte SPORT_DATA_SYNC_MODE_DISABLE = 0x00;
    public final static byte SPORT_DATA_SYNC_MODE_ENABLE = 0x01;

    /* err code*/
    public final static byte SUCCESS = 0x00;
    public final static byte BOND_FAIL_TIMEOUT = 0x01;
    public final static byte SUPER_KEY_FAIL = 0x02;
    public final static byte LOW_POWER = 0x03;

    // Day Flags
    public final static byte REPETITION_NULL = 0x00;
    public final static byte REPETITION_ALL = 0x7f;
    public final static byte REPETITION_MON = 0x01;
    public final static byte REPETITION_TUES = 0x02;
    public final static byte REPETITION_WED = 0x04;
    public final static byte REPETITION_THU = 0x08;
    public final static byte REPETITION_FRI = 0x10;
    public final static byte REPETITION_SAT = 0x20;
    public final static byte REPETITION_SUN = 0x40;

    // Sex Flags
    public final static boolean SEX_MAN = true;
    public final static boolean SEX_WOMAN = false;

    // Call notify flags
    public final static byte PHONE_OS_IOS = 0x01;
    public final static byte PHONE_OS_ANDROID = 0x02;

    // Call notify flags
    public final static byte CALL_NOTIFY_MODE_ON = 0x01;
    public final static byte CALL_NOTIFY_MODE_OFF = 0x02;
    public final static byte CALL_NOTIFY_MODE_ENABLE_QQ = 0x03;
    public final static byte CALL_NOTIFY_MODE_DISABLE_QQ = 0x04;
    public final static byte CALL_NOTIFY_MODE_ENABLE_WECHAT = 0x05;
    public final static byte CALL_NOTIFY_MODE_DISABLE_WECHAT = 0x06;
    public final static byte CALL_NOTIFY_MODE_ENABLE_MESSAGE = 0x07;
    public final static byte CALL_NOTIFY_MODE_DISABLE_MESSAGE = 0x08;

    // notify flags
    public final static byte OTHER_NOTIFY_INFO_QQ = 0x01;
    public final static byte OTHER_NOTIFY_INFO_WECHAT = 0x02;
    public final static byte OTHER_NOTIFY_INFO_MESSAGE = 0x04;

    public final static byte NOTIFY_SWITCH_SETTING_CALL = 0x01;
    public final static byte NOTIFY_SWITCH_SETTING_QQ = 0x02;
    public final static byte NOTIFY_SWITCH_SETTING_WECHAT = 0x04;
    public final static byte NOTIFY_SWITCH_SETTING_MESSAGE = 0x08;

    // Long sit Flags
    public final static byte LONG_SIT_CONTROL_ENABLE = 0x01;
    public final static byte LONG_SIT_CONTROL_DISABLE = 0x00;

    // FAC Led Flags
    public final static byte FAC_LED_CONTROL_ENABLE_ALL = (byte) 0xFF;
    public final static byte FAC_LED_CONTROL_ENABLE_0 = 0x00;
    public final static byte FAC_LED_CONTROL_ENABLE_1 = 0x01;
    public final static byte FAC_LED_CONTROL_ENABLE_2 = 0x02;

    // Sleep Mode
    public static final int SLEEP_MODE_START_SLEEP = 1;
    public static final int SLEEP_MODE_START_DEEP_SLEEP = 2;
    public static final int SLEEP_MODE_START_WAKE = 3;

    // Debug Log type
    public final static byte DEBUG_LOG_TYPE_MODULE_APP = (byte) 0x01;
    public final static byte DEBUG_LOG_TYPE_MODULE_LOWERSTACK = (byte) 0x02;
    public final static byte DEBUG_LOG_TYPE_MODULE_UPSTACK = (byte) 0x03;
    public final static byte DEBUG_LOG_TYPE_SLEEP_DATA = (byte) 0x11;
    public final static byte DEBUG_LOG_TYPE_SPORT_DATA = (byte) 0x21;
    public final static byte DEBUG_LOG_TYPE_CONFIG_DATA = (byte) 0x31;

    public final static int DEBUG_LOG_TYPE_MAX_CNT = 6;

    // Camera control
    public final static byte CAMERA_CONTROL_APP_IN_FORE = 0x00;
    public final static byte CAMERA_CONTROL_APP_IN_BACK = 0x01;


    // Turn over wrist control
    public final static byte TURN_OVER_WRIST_CONTROL_ENABLE = 0x01;
    public final static byte TURN_OVER_WRIST_CONTROL_DISABLE = 0x00;


    // Hrp single request
    public final static byte HRP_SINGLE_REQ_MODE_ENABLE = 0x01;
    public final static byte HRP_SINGLE_REQ_MODE_DISABLE = 0x00;

    // Hrp continue request
    public final static byte HRP_CONTINUE_REQ_MODE_ENABLE = 0x01;
    public final static byte HRP_CONTINUE_REQ_MODE_DISABLE = 0x00;


    // Blood Pressure request
    public final static byte BP_SINGLE_REQ_MODE_ENABLE = 0x01;
    public final static byte BP_SINGLE_REQ_MODE_DISABLE = 0x00;

    // Transport Layer Object
    TransportLayer mTransportLayer;

    // Application Layer Call
    private ApplicationLayerCallback mCallback;

    public ApplicationLayer(Context context, ApplicationLayerCallback callback) {
        if (D) Log.d(TAG, "init");
        // register callback
        mCallback = callback;

        // init the transport layer
        mTransportLayer = new TransportLayer(context, mTransportCallback);

    }

    /**
     * connect to the remote device.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onConnectionStateChange} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean connect(String addr) {
        if (D) Log.d(TAG, "connect with: " + addr);
        return mTransportLayer.connect(addr);
    }

    public boolean isConnected() {
        return mTransportLayer.isConnected();
    }


    /**
     * Disconnect, it will disconnect to the remote.
     */
    public void disconnect() {
        mTransportLayer.disconnect();
    }

    /**
     * Set the name
     *
     * @param name the name
     */
    public void setDeviceName(String name) {
        if (D) Log.d(TAG, "set name, name: " + name);
        mTransportLayer.setDeviceName(name);
    }

    /**
     * Get the name
     */
    public void getDeviceName() {
        if (D) Log.d(TAG, "getDeviceName");
        mTransportLayer.getDeviceName();
    }

    /**
     * Request Remote enter OTA mode. Command: 0x01, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onUpdateCmdRequestEnterOtaMode}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean updateCmdRequestEnterOtaMode() {
        if (D) Log.d(TAG, "updateCmdRequestEnterOtaMode");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UPDATE_REQUEST, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_IMAGE_UPDATE, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote time. Command: 0x02, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param year the current time of year.
     * @param mon  the current time of mon.
     * @param day  the current time of day.
     * @param hour the current time of hour.
     * @param min  the current time of min.
     * @param sec  the current time of sec.
     * @return the operation result
     */
    public boolean settingCmdTimeSetting(int year, int mon, int day, int hour, int min, int sec) {
        if (D) Log.d(TAG, "settingCmdTimeSetting, year: " + year
                + ", mon: " + mon
                + ", day: " + day
                + ", hour: " + hour
                + ", min: " + min
                + ", sec: " + sec);
        // generate key value data
        ApplicationLayerTimerPacket timePackt = new ApplicationLayerTimerPacket(year, mon, day, hour, min, sec);
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TIMER, timePackt.getPacket());
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote time. Command: 0x02, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param Alarms the alarm list.
     * @return the operation result
     */
    public boolean settingCmdAlarmsSetting(ApplicationLayerAlarmsPacket Alarms) {
        if (D) Log.d(TAG, "SettingCmdAlarmSetting");
        // generate key value data
        byte[] keyValue = null;
        if (Alarms == null || (Alarms.size() == 0)) {
            //do nothing
        } else {
            if (Alarms.size() > 8) {
                return false;
            }
            keyValue = Alarms.getPacket();
        }
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_ALARM, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request Remote alarms list. Command: 0x01, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestAlarmList}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdRequestAlarmList() {
        if (D) Log.d(TAG, "settingCmdRequestAlarmList");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_GET_ALARM_LIST_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote user profile. Command: 0x02, Key: 0x10.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param gender the user gender.
     * @param age    the user age.
     * @param height the user height.
     * @param weight the user weight.
     * @return the operation result
     */
    public boolean settingCmdUserSetting(boolean gender, int age, int height, int weight) {
        ApplicationLayerUserPacket user = new ApplicationLayerUserPacket(gender, age, height, weight);
        if (D) Log.d(TAG, "settingCmdUserSetting");
        // generate key value data
        byte[] keyValue = user.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_USER_PROFILE, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote lost mode. Command: 0x02, Key: 0x20.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the lost mode.
     * @return the operation result
     */
    public boolean settingCmdLostModeSetting(byte mode) {
        if (D) Log.d(TAG, "settingCmdLostModeSetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_LOST_MODE, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote step target. Command: 0x02, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param step the step target.
     * @return the operation result
     */
    public boolean settingCmdStepTargetSetting(long step) {
        if (D) Log.d(TAG, "settingCmdStepTargetSetting, step: " + step);
        // generate key value data
        ApplicationLayerStepPacket p = new ApplicationLayerStepPacket(step);
        byte[] keyValue = p.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_STEP_TARGET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote long sit notification. Command: 0x02, Key: 0x21.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable    enable @link this#LONG_SIT_CONTROL_ENABLE long sit notify
     *                  disable @link this#LONG_SIT_CONTROL_DISABLE long sit notify
     * @param threshold It will remind you when the steps is less than the threshold during the sedentary time
     * @param notify    the sit packet.
     * @param start     the sit packet.
     * @param stop      the sit packet.
     * @param dayflags  the sit packet.
     * @return the operation result
     */
    public boolean settingCmdLongSitSetting(byte enable, int threshold, int notify, int start, int stop, byte dayflags) {
        ApplicationLayerSitPacket packet = new ApplicationLayerSitPacket(enable, threshold, notify, start, stop, dayflags);
        if (D) Log.d(TAG, "settingCmdLongSitSetting");
        // generate key value data
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SIT_TOOLONG_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote left right hand. Command: 0x02, Key: 0x22.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the left right hand mode.
     * @return the operation result
     */
    public boolean settingCmdLeftRightSetting(byte mode) {
        if (D) Log.d(TAG, "settingCmdLeftRightSetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_LEFT_RIGHT_HAND_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the phone OS. Command: 0x02, Key: 0x23.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the phone os.
     * @return the operation result
     */
    public boolean settingCmdPhoneOSSetting(byte mode) {
        if (D) Log.d(TAG, "settingCmdPhoneOSSetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode, 0x00};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_PHONE_OS, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote call notify mode. Command: 0x02, Key: 0x25.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode call notify mode.
     * @return the operation result
     */
    public boolean settingCmdCallNotifySetting(byte mode) {
        if (D) Log.d(TAG, "settingCmdCallNotifySetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_INCOMING_ON_OFF, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote current long sit setting. Command: 0x02, Key: 0x26.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestLongSit}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdRequestLongSitSetting() {
        if (D) Log.d(TAG, "settingCmdRequestLongSitSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SIT_TOOLONG_SWITCH_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote current notify setting. Command: 0x02, Key: 0x28.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestNotifySwitch}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdRequestNotifySwitchSetting() {
        if (D) Log.d(TAG, "settingCmdRequestNotifySwitchSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_NOTIFY_SWITCH_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote Turn Over Wrist. Command: 0x02, Key: 0x2A.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable ENABLE/DISABLE.
     * @return the operation result
     */
    public boolean settingCmdTurnOverWristSetting(boolean enable) {
        if (D) Log.d(TAG, "settingCmdTurnOverWristSetting, enable: " + enable);
        // generate key value data
        byte[] keyValue = {enable ? TURN_OVER_WRIST_CONTROL_ENABLE : TURN_OVER_WRIST_CONTROL_DISABLE};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TURN_OVER_WRIST_SET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote current Turn Over Wrist setting. Command: 0x02, Key: 0x2B.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onTurnOverWristSettingReceive(boolean)}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdRequestTurnOverWristSetting() {
        if (D) Log.d(TAG, "settingCmdRequestTurnOverWristSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TURN_OVER_WRIST_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request the remote current notify setting. Command: 0x02, Key: 0x33.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onDisplaySwitchReturn(byte)}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdRequestDisplaySwitchSetting(@CommandManager.ScreenState int status) {
        if (D) Log.d(TAG, "settingCmdRequestDisplaySwitchSetting");
        byte[] keyValue = {(byte) status};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_DISPLAY_SWITCH_SETTING, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request the remote current Turn Over Wrist setting. Command: 0x02, Key: 0x34.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onDisplaySwitchReturn(byte)}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdRequestDisplaySwitchRequest() {
        if (D) Log.d(TAG, "settingCmdRequestDisplaySwitchSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_DISPLAY_SWITCH_REQUEST, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request the remote current Turn Over Wrist setting. Command: 0x02, Key: 0x36.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onFunctions(ApplicationLayerFunctions)}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean settingCmdFunctionsRequest() {
        if (D) Log.d(TAG, "settingCmdRequestDisplaySwitchSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FUNCTIONS_REQUEST, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request bond to remote. Command: 0x03, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onBondCmdRequestBond}
     * callback will invoked, response some information to host
     *
     * @param userId the user id string.
     * @return the operation result
     */
    public boolean bondCmdRequestBond(String userId) {
        if (D) Log.d(TAG, "bondCmdRequestBond, user id: " + userId);
        // generate key value data
        // be careful java use the UTF-16 code, so we need change the ascii byte array
        byte[] asciiArray = StringByteTrans.Str2Bytes(userId);
        byte[] keyValue = new byte[32];
        if (asciiArray.length > 32) {
            System.arraycopy(asciiArray, 0, keyValue, 0, 32);
        } else {
            System.arraycopy(asciiArray, 0, keyValue, 0, asciiArray.length);
        }
        Log.e(TAG, "keyValue: " + StringByteTrans.byte2HexStr(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_BOND_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request login to remote. Command: 0x03, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onBondCmdRequestLogin}
     * callback will invoked, response some information to host
     *
     * @param userId the user id string.
     * @return the operation result
     */
    public boolean bondCmdRequestLogin(String userId) {
        if (D) Log.d(TAG, "bondCmdRequestLogin, user id: " + userId);
        // generate key value data
        // be careful java use the UTF-16 code, so we need change the ascii byte array
        byte[] asciiArray = StringByteTrans.Str2Bytes(userId);
        byte[] keyValue = new byte[32];
        if (asciiArray.length > 32) {
            System.arraycopy(asciiArray, 0, keyValue, 0, 32);
        } else {
            System.arraycopy(asciiArray, 0, keyValue, 0, asciiArray.length);
        }
        Log.e(TAG, "keyValue: " + StringByteTrans.byte2HexStr(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOGIN_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request clear bond to remote. Command: 0x03, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean bondCmdRequestRemoveBond() {
        if (D) Log.d(TAG, "bondCmdRequestRemoveBond()");
        // generate key value data, reserve one byte
        byte[] keyValue = new byte[1];
        Log.e(TAG, "keyValue: " + StringByteTrans.byte2HexStr(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UNBOND, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send notify info to the remote. Command: 0x04, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean notifyCmdCallNotifyInfoSetting() {
        if (D) Log.d(TAG, "notifyCmdCallNotifyInfoSetting");
        return notifyCmdCallNotifyInfoSetting("");
        /*
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
        */
    }

    public boolean notifyCmdCallNotifyInfoSetting(String showData) {
        if (D) Log.d(TAG, "notifyCmdCallNotifyInfoSetting: " + showData);
        // generate key data
        byte[] keyData;
        if (TextUtils.isEmpty(showData)) {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, null);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, showData.getBytes());
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send call accept notify info to the remote. Command: 0x04, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean notifyCmdCallAcceptNotifyInfoSetting() {
        if (D) Log.d(TAG, "notifyCmdCallAcceptNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL_ACC, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send call reject notify info to the remote. Command: 0x04, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean notifyCmdCallRejectNotifyInfoSetting() {
        if (D) Log.d(TAG, "notifyCmdCallRejectNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL_REJ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send notify info to the remote. Command: 0x04, Key: 0x04.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param info call notify info.
     * @return the operation result
     */
    public boolean notifyCmdOtherNotifyInfoSetting(byte info) {
        if (D) Log.d(TAG, "notifyCmdOtherNotifyInfoSetting, info: " + info);
        /*
        // generate key value data
        byte[] keyValue = {info};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_INCOMING_OTHER_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
        */
        return notifyCmdOtherNotifyInfoSetting(info, "");
    }

    public boolean notifyCmdOtherNotifyInfoSetting(byte info, String showData) {
        if (D)
            Log.d(TAG, "notifyCmdOtherNotifyInfoSetting, info: " + info + "showData: " + showData);
        // generate key value data
        //byte[] keyValue = {info};
        byte[] showDataByte = null;
        byte[] keyValue = null;
        if (TextUtils.isEmpty(showData)) {
            keyValue = new byte[1];
            keyValue[0] = info;
        } else {
            showDataByte = showData.getBytes();    //UTF-8
            int len = showDataByte.length;
            keyValue = new byte[1 + len];
            keyValue[0] = info;
            System.arraycopy(showDataByte, 0, keyValue, 1, len);
        }

        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_INCOMING_OTHER_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean notifyCmdOtherNotifyInfoSettingWithVibrateCount(byte info, byte vibrateCount, String showData) {
        if (D)
            Log.d(TAG, "notifyCmdOtherNotifyInfoSetting, info: " + info + "showData: " + showData);
        // generate key value data
        //byte[] keyValue = {info};
        byte[] showDataByte = null;
        byte[] keyValue = null;
        if (TextUtils.isEmpty(showData)) {
            keyValue = new byte[2];
            keyValue[0] = info;
            keyValue[1] = vibrateCount;
        } else {
            showDataByte = showData.getBytes();    //UTF-8
            int len = showDataByte.length;
            keyValue = new byte[2 + len];
            keyValue[0] = info;
            keyValue[1] = vibrateCount;
            System.arraycopy(showDataByte, 0, keyValue, 2, len);
        }

        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_INCOMING_UNIVERSAL_OTHER_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request remote send sport data. Command: 0x05, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSportDataCmdSportData}
     * and {@link ApplicationLayerCallback#onSportDataCmdSleepData}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean sportDataCmdRequestData() {
        if (D) Log.d(TAG, "sportDataCmdRequestData");
        // generate key value data

        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request remote send blood　pressure data. Command: 0x05, Key: 0x14.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSportDataCmdSportData}
     * and {@link ApplicationLayerCallback#onSportDataCmdBPData}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean sportDataCmdBloodPressureRequest(boolean enable) {
        if (D) Log.d(TAG, "sportDataCmdBloodPressureRequest, enable: " + enable);
        // generate key value data
        byte[] keyValue = new byte[]{enable ? BP_SINGLE_REQ_MODE_ENABLE : BP_SINGLE_REQ_MODE_DISABLE};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_BLOOD_PRESSURE_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Set the remote sync mode. Command: 0x05, Key: 0x06.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the left right hand mode.
     * @return the operation result
     */
    public boolean sportDataCmdSyncSetting(byte mode) {
        if (D) Log.d(TAG, "sportDataCmdSyncSetting, mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_SYNC, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote sync today. Command: 0x05, Key: 0x09.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param step     the step of today sport .
     * @param distance the distance of today sport .
     * @param calorie  the calorie of today sport .
     * @return the operation result
     */
    public boolean sportDataCmdSyncToday(long step, long distance, long calorie) {
        ApplicationLayerTodaySportPacket packet = new ApplicationLayerTodaySportPacket(step, distance, calorie);
        if (D) Log.d(TAG, "sportDataCmdSyncToday");
        // generate key value data
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_TODAY_SYNC, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote sync recently. Command: 0x05, Key: 0x0a.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean sportDataCmdSyncRecently(ApplicationLayerRecentlySportPacket packet) {
        if (D) Log.d(TAG, "sportDataCmdSyncRecently");
        // generate key value data
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_LAST_SYNC, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp single request. Command: 0x05, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable enable/disable hrp single request .
     * @return the operation result
     */
    public boolean sportDataCmdHrpSingleRequest(boolean enable) {
        if (D) Log.d(TAG, "sportDataCmdHrpSingleRequest, enable: " + enable);
        // generate key value data
        byte[] keyValue = new byte[]{enable ? HRP_SINGLE_REQ_MODE_ENABLE : HRP_SINGLE_REQ_MODE_DISABLE};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_HRP_SINGLE_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp continue set. Command: 0x05, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable   enable/disable hrp continue request .
     * @param interval Unit minutes, only enable valid.
     * @return the operation result
     */
    public boolean sportDataCmdHrpContinueSet(boolean enable, int interval) {
        if (D)
            Log.d(TAG, "SportDataCmdHrpContinueRequest, enable: " + enable + ", interval: " + interval);
        // generate key value data
        byte[] keyValue = new byte[]{enable ? HRP_CONTINUE_REQ_MODE_ENABLE : HRP_CONTINUE_REQ_MODE_DISABLE
                , (byte) (interval & 0xff)};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_HRP_CONTINUE_SET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp continue set. Command: 0x05, Key: 0x11.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSportDataCmdHrpContinueParamRsp}
     * callback will invoked, response some information to host
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean sportDataCmdHrpContinueParamRequest() {
        if (D) Log.d(TAG, "sportDataCmdHrpContinueParamRequest");
        // generate key value data
        byte[] keyValue = null;
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_HRP_CONTINUE_PARAMS_GET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Fac Command use to enable led. Command: 0x06, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param led the led want to enable
     * @return the operation result
     */
    public boolean facCmdEnableLed(byte led) {
        if (D) Log.d(TAG, "facCmdEnableLed");
        byte[] keyData;
        if (led != FAC_LED_CONTROL_ENABLE_ALL) {
            // generate key value data
            byte[] keyValue = {led};
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LED, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LED, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enable vibrate. Command: 0x06, Key: 0x06.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean facCmdEnableVibrate() {
        if (D) Log.d(TAG, "facCmdEnableVibrate");
        // generate key value data
        //byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_MOTO, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to request sensor data. Command: 0x06, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onFACCmdSensorData}
     * and {@link ApplicationLayerCallback#onSportDataCmdSleepData}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean facCmdRequestSensorData() {
        if (D) Log.d(TAG, "facCmdRequestSensorData");
        // generate key value data
        //byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_SENSOR_DATA_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enter test mode. Command: 0x06, Key: 0x0e.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean facCmdEnterTestMode(byte[] key) {
        if (D) Log.d(TAG, "facCmdEnterTestMode");
        byte[] keyData;
        if (key != null) {
            if (key.length != 32) {
                if (D) Log.d(TAG, "The length is not right.");
                return false;
            }
            byte[] keyValue = new byte[32];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, 32);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_ENTER_SPUER_KEY, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_ENTER_SPUER_KEY, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to exit test mode. Command: 0x06, Key: 0x0e.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean facCmdExitTestMode(byte[] key) {
        if (D) Log.d(TAG, "facCmdExitTestMode");
        byte[] keyData;
        if (key != null) {
            if (key.length != 32) {
                if (D) Log.d(TAG, "The length is not right.");
                return false;
            }
            byte[] keyValue = new byte[32];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, 32);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LEAVE_SPUER_KEY, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LEAVE_SPUER_KEY, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send control cmd to the remote. Command: 0x07, Key: 0x11.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param cmd cmd.
     * @return the operation result
     */
    public boolean controlCmdCameraControl(byte cmd) {
        if (D) Log.d(TAG, "controlCmdCameraControl, cmd: " + cmd);
        // generate key value data
        byte[] keyValue = {cmd};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_CTRL_APP_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_CTRL, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Log Command use to open log. Command: 0x0a, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean logCmdOpenLog(byte[] key) {
        if (D) Log.d(TAG, "logCmdOpenLog");
        byte[] keyData;
        if (key != null) {
            byte[] keyValue = new byte[key.length];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, key.length);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_FUNC_OPEN, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_FUNC_OPEN, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_LOG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Log Command use to close log. Command: 0x0a, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean logCmdCloseLog() {
        if (D) Log.d(TAG, "logCmdCloseLog");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_FUNC_CLOSE, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_LOG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Log Command use to request log. Command: 0x0a, Key: 0x04.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean logCmdRequestLog(byte key) {
        if (D) Log.d(TAG, "logCmdRequestLog");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_REQ, new byte[]{key});

        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_LOG, keyData);
        Log.e(TAG, "appData: " + StringByteTrans.byte2HexStr(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public void closeGatt() {
        mTransportLayer.closeGatt();
    }

    TransportLayerCallback mTransportCallback = new TransportLayerCallback() {
        @Override
        public void onConnectionStateChange(final boolean status, final boolean newState) {
            if (D)
                Log.d(TAG, "onConnectionStateChange, status: " + status + ", newState: " + newState);
            mCallback.onConnectionStateChange(status, newState);

        }

        @Override
        public void onDataSend(final boolean status, byte[] data) {
            if (D) Log.d(TAG, "onDataSend, status: " + status);
            // decode the packet
            ApplicationLayerPacket appPacket = new ApplicationLayerPacket();
            appPacket.parseData(data);
            // dispatch the key value
            List<ApplicationLayerKeyPacket> keyPackets = appPacket.getKeyPacketArrays();
            for (ApplicationLayerKeyPacket keyPacket : keyPackets) {
                byte commandId = appPacket.getCommandId();
                byte keyId = keyPacket.getKey();
                if (D) Log.d(TAG, "onDataSend, commandId: " + commandId + ", keyId: " + keyId);
                mCallback.onCommandSend(status, commandId, keyId);
            }

        }

        @Override
        public void onDataReceive(byte[] data) {
            if (D) Log.d(TAG, "onDataReceive, data length: " + data.length);
            // decode the packet
            ApplicationLayerPacket appPacket = new ApplicationLayerPacket();
            appPacket.parseData(data);

            // dispatch the key value
            List<ApplicationLayerKeyPacket> keyPackets = appPacket.getKeyPacketArrays();
            for (ApplicationLayerKeyPacket keyPacket : keyPackets) {
                byte commandId = appPacket.getCommandId();
                byte keyId = keyPacket.getKey();
                byte[] keyData = keyPacket.getKeyData();
                if (D) Log.d(TAG, "onDataReceive, commandId: " + commandId + ", keyId: " + keyId);
                // check the command id
                switch (commandId) {
                    case CMD_IMAGE_UPDATE:
                        // check the key id
                        switch (keyId) {
                            case KEY_UPDATE_RESPONSE:
                                mCallback.onUpdateCmdRequestEnterOtaMode(keyData[0], keyData[1]);
                                break;

                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_SETTING:
                        // check the key id
                        switch (keyId) {
                            case KEY_SETTING_GET_ALARM_LIST_RSP:
                                // parse the alarm list
                                ApplicationLayerAlarmsPacket alarms = new ApplicationLayerAlarmsPacket();
                                alarms.parseData(keyData);
                                mCallback.onSettingCmdRequestAlarmList(alarms);
                                break;
                            case KEY_SETTING_NOTIFY_SWITCH_RSP:
                                mCallback.onSettingCmdRequestNotifySwitch(keyData[0]);
                                break;
                            case KEY_SETTING_SIT_TOOLONG_SWITCH_RSP:
                                mCallback.onSettingCmdRequestLongSit(keyData[0]);
                                break;
                            case KEY_SETTING_TURN_OVER_WRIST_RSP:
                                LogUtils.d(TAG, " KEY_SETTING_TURN_OVER_WRIST_RSP ");
                                mCallback.onTurnOverWristSettingReceive(keyData[0] == TURN_OVER_WRIST_CONTROL_ENABLE);
                                break;
                            case KEY_DISPLAY_SWITCH_RETURN:
                                mCallback.onDisplaySwitchReturn(keyData[0]);
                                break;
                            case KEY_FUNCTIONS_RETURN:
                                ApplicationLayerFunctions functions = new ApplicationLayerFunctions();
                                functions.parseData(keyData);
                                mCallback.onFunctions(functions);
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_BOND_REG:
                        // check the key id
                        switch (keyId) {
                            case KEY_BOND_RSP:
                                mCallback.onBondCmdRequestBond(keyData[0]);
                                break;
                            case KEY_LOGIN_RSP:
                                mCallback.onBondCmdRequestLogin(keyData[0]);
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_NOTIFY:
                        switch (keyId) {
                            case KEY_NOTIFY_END_CALL:
                                mCallback.onEndCallReceived();
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_SPORTS_DATA:
                        // check the key id
                        switch (keyId) {
                            case KEY_SPORTS_RUNNIG_RSP:
                                // parse the alarm list
                                ApplicationLayerSportPacket sport = new ApplicationLayerSportPacket();
                                boolean b = sport.parseData(keyData);
                                mCallback.onSportDataCmdSportData(sport);
                                LogUtils.recordSportData(StringByteTrans.byte2HexStr(keyData));
                                break;
                            case KEY_SPORTS_SLEEP_RSP:
                                // parse the alarm list
                                ApplicationLayerSleepPacket sleep = new ApplicationLayerSleepPacket();
                                sleep.parseData(keyData);
                                LogUtils.recordSleepData(StringByteTrans.byte2HexStr(keyData));
                                mCallback.onSportDataCmdSleepData(sleep);
                                break;
                            case KEY_SPORTS_RUNNIG_RSP_MORE:
                                mCallback.onSportDataCmdMoreData();
                                break;
                            case KEY_SPORTS_SLEEP_SET_RSP:
                                // parse the alarm list
                                ApplicationLayerSleepPacket sleepSet = new ApplicationLayerSleepPacket();
                                sleepSet.parseData(keyData);
                                mCallback.onSportDataCmdSleepSetData(sleepSet);
                                break;
                            case KEY_SPORTS_HIS_SYNC_BEG:
                                mCallback.onSportDataCmdHistorySyncBegin();
                                break;
                            case KEY_SPORTS_HIS_SYNC_END:
                                // Here need check have total data or not
                                ApplicationLayerTodaySumSportPacket todaySumSportPacket = new ApplicationLayerTodaySumSportPacket();

                                if (keyData.length != 0) {
                                    if (!todaySumSportPacket.parseData(keyData)) {
                                        todaySumSportPacket = null;
                                    }
                                } else {
                                    todaySumSportPacket = null;
                                }
                                mCallback.onSportDataCmdHistorySyncEnd(todaySumSportPacket);
                                break;
                            case KEY_SPORTS_HRP_DATA_RSP:
                                // parse the alarm list
                                ApplicationLayerHrpPacket hrp = new ApplicationLayerHrpPacket();
                                hrp.parseData(keyData);
                                mCallback.onSportDataCmdHrpData(hrp);
                                break;
                            case KEY_SPORTS_BLOOD_PRESSURE_RSP:
                                ApplicationLayerBPPacket bpPacket = new ApplicationLayerBPPacket();
                                bpPacket.parseData(keyData);
                                mCallback.onSportDataCmdBPData(bpPacket);
                                break;
                            case KEY_SPORTS_BLOOD_PRESSURE_END:
                                mCallback.onSportDataCmdBPSyncEnd();
                                break;
                            case KEY_SPORTS_HRP_DEVICE_CANCEL_READ_HRP:
                                mCallback.onSportDataCmdDeviceCancelSingleHrpRead();
                                break;
                            case KEY_SPORTS_HRP_CONTINUE_PARAMS_RSP:
                                boolean hrpEnable = (keyData[0] == HRP_CONTINUE_REQ_MODE_ENABLE);
                                int hrpInterval = (keyData[1] & 0xff);
                                mCallback.onSportDataCmdHrpContinueParamRsp(hrpEnable, hrpInterval);
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_CTRL:
                        // check the key id
                        switch (keyId) {
                            case KEY_CTRL_PHOTO_RSP:
                                mCallback.onTakePhotoRsp();
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_FACTORY_TEST:
                        // check the key id
                        switch (keyId) {
                            case KEY_FAC_TEST_SENSOR_DATA_RSP:
                                // parse the alarm list
                                ApplicationLayerFacSensorPacket sensor = new ApplicationLayerFacSensorPacket();
                                sensor.parseData(keyData);
                                mCallback.onFACCmdSensorData(sensor);
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_LOG:
                        // check the key id
                        switch (keyId) {
                            case KEY_LOG_START:
                                // parse the log length
                                long logLength = 0;
                                for (int i = 0; i < keyData.length; i++) {
                                    logLength = (keyData[i] & 0xFF) << (8 * (keyData.length - (i + 1)));
                                }
                                mCallback.onLogCmdStart(logLength);
                                break;
                            case KEY_LOG_END:
                                mCallback.onLogCmdEnd();
                                break;
                            case KEY_LOG_RSP:
                                // parse the alarm list
                                ApplicationLayerLogResponsePacket logResponsePacket = new ApplicationLayerLogResponsePacket();
                                logResponsePacket.parseData(keyData);
                                mCallback.onLogCmdRsp(logResponsePacket);
                                break;
                            default:
                                if (D) Log.e(TAG, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    default:
                        if (D) Log.e(TAG, "onDataReceive, unknown command id: " + commandId);
                        break;
                }
            }
        }

        @Override
        public void onNameReceive(final String data) {
            mCallback.onNameReceive(data);
        }
    };
}
