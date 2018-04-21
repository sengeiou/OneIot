package com.coband.common.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by imco on 5/11/16.
 */
public class Config {
    @Retention(SOURCE)
    @IntDef({SEDENTARY_REMIND_OPEN, SEDENTARY_REMIND_CLOSE})
    public @interface SedentaryRemind {
    }

    public static final int SEDENTARY_REMIND_OPEN = 1;
    public static final int SEDENTARY_REMIND_CLOSE = 0;

    @Retention(SOURCE)
    @IntDef({SEDENTARY_PERIOD_30, SEDENTARY_PERIOD_60, SEDENTARY_PERIOD_90, SEDENTARY_PERIOD_120})
    public @interface SedentaryRemindPeriod {
    }

    public static final int SEDENTARY_PERIOD_30 = 30;
    public static final int SEDENTARY_PERIOD_60 = 60;
    public static final int SEDENTARY_PERIOD_90 = 90;
    public static final int SEDENTARY_PERIOD_120 = 120;


    public static final int DEFAULT_SEDENTARY_REMIND_TIME = 60;
    public static final boolean DEFAULT_SEDENTARY_REMIND_STATUS = false;
    public static final boolean DEFAULT_CALL_REMIND_STATUS = false;
    public static final boolean DEFAULT_SMS_REMIND_STATUS = false;
    public static final boolean DEFAULT_SMART_UNLOCK_STATUS = false;
    public static final boolean DEFAULT_LOST_WARNING_STATUS = false;
    public static final int DEFAULT_SCREEN_SLEEP_TIME = 5;

    public static final String FIR_IM_TOKEN = "ee2ff4786af8a1ae2bb4f77ecaf65fb1";

    public static final String FIR_IM_VERSION_PATH = "http://api.fir.im/apps/latest/" +
            "56efa8e4e75e2d37be00002a?api_token=ee2ff4786af8a1ae2bb4f77ecaf65fb1";

    public static final String FIR_IM_APP_ID = "56efa8e4e75e2d37be00002a";

    public static final String FIR_IM = "firim";
    public static final String GOOGLE_PLAY = "googleplay";

    //    public static final String UPLOAD_ADDRESS_URL = "https://www.aimoke.cc/wechat/wechatSport.php?";
    public static final String UPLOAD_ADDRESS_URL = "https://mp.priodigit.com/api/get_device_qr?";

    @Retention(SOURCE)
    @IntDef({SCREEN_SLEEP_TIME_5, SCREEN_SLEEP_TIME_10, SCREEN_SLEEP_TIME_15, SCREEN_SLEEP_TIME_20,
            SCREEN_SLEEP_TIME_25})
    public @interface ScreenSleepTime {
    }

    public static final int SCREEN_SLEEP_TIME_5 = 5;
    public static final int SCREEN_SLEEP_TIME_10 = 10;
    public static final int SCREEN_SLEEP_TIME_15 = 15;
    public static final int SCREEN_SLEEP_TIME_20 = 20;
    public static final int SCREEN_SLEEP_TIME_25 = 25;


    @Retention(SOURCE)
    @IntDef({FIRST_ALARM, SECOND_ALARM, THIRD_ALARM})
    public @interface DeviceAlarm {
    }

    public static final int FIRST_ALARM = 1;
    public static final int SECOND_ALARM = 2;
    public static final int THIRD_ALARM = 3;


    @Retention(SOURCE)
    @IntDef({METRIC, INCH})
    public @interface UnitSystem {

    }

    public static final int METRIC = 0;
    public static final int INCH = 1;


    @Retention(SOURCE)
    @IntDef({CONTENT_TYPE_SMS, CONTENT_TYPE_PHONE, CONTENT_TYPE_QQ, CONTENT_TYPE_WECHAT})
    public @interface ContentType {

    }

    public static final int CONTENT_TYPE_SMS = 3;
    public static final int CONTENT_TYPE_PHONE = 0;
    public static final int CONTENT_TYPE_QQ = 1;
    public static final int CONTENT_TYPE_WECHAT = 2;


    @Retention(SOURCE)
    @IntDef({NUMBER_TYPE_SMS, NUMBER_TYPE_PHONE})
    public @interface NumberType {

    }

    public static final int NUMBER_TYPE_SMS = 3;
    public static final int NUMBER_TYPE_PHONE = 2;


    @Retention(SOURCE)
    @IntDef({NOTIFICATION_QQ, NOTIFICATION_WECHAT})
    public @interface NotificationType {

    }


    public static final int NOTIFICATION_QQ = 1;
    public static final int NOTIFICATION_WECHAT = 2;


    public static final String ACTION_READ_BLE_BATTERY = "read_battery_action";
    public static final String ACTION_READ_BLE_VERSION = "read_ble_version_action";

    public static final int K9_DEEP = 2;
    public static final int K9_LIGHT = 1;
    public static final int K9_AWAKE = 3;

    public static final int FIRMWARE_VERSION_SWITCH_ORIENTATION_FEATURE = 16218;

    public static final String DEFAULT_DEVICE_NAME = "CoBand";

    public static final String METRIC_STRING = "Metric";
    public static final String BRITISH_STRING = "British";

    public static final int DEFAULT_STEP_TARGET = 10000;
    public static final int DEFAULT_SLEEP_TARGET = 480;
    public static final int DEFAULT_WEIGHT_TARGET_METRIC = 60;
    public static final int DEFAULT_WEIGHT_TARGET_INCH = 120;

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
}

