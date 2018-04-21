package com.coband.interactivelayer;

/*
 * Created by mai on 17-8-31.
 */

public class Flags {


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
    public final static byte REPETITION_ONLY = 0x00;
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

    // Display switch control
    public final static byte VERTICAL_SCREEN = 0x01;
    public final static byte HORIZONTAL_SCREEN= 0x00;

    // Hrp single request
    public final static byte HRP_SINGLE_REQ_MODE_ENABLE = 0x01;
    public final static byte HRP_SINGLE_REQ_MODE_DISABLE = 0x00;

    // Hrp continue request
    public final static byte HRP_CONTINUE_REQ_MODE_ENABLE = 0x01;
    public final static byte HRP_CONTINUE_REQ_MODE_DISABLE = 0x00;

    // connect　status
    public static final int STATE_WRIST_INITIAL = 0;
    public static final int STATE_WRIST_LOGGING = 1;
    public static final int STATE_WRIST_BONDING = 2;
    public static final int STATE_WRIST_LOGIN = 3;

    // connect error status
    public static final int ERROR_CODE_NO_LOGIN_RESPONSE_COME = 1;
    public static final int ERROR_CODE_BOND_ERROR = 2;
    public static final int ERROR_CODE_COMMAND_SEND_ERROR = 3;







}
