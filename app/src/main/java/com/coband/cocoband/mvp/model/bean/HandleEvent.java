package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mqh on 6/1/16.
 */
public class HandleEvent {

    public static final int MSG_DISCONNECTED = 0;
    public static final int MSG_CONNECTED = 1;
    public static final int MSG_SEDENTARY_CLOSE = 2;
    public static final int MSG_SEDENTARY_OPEN = 3;
    public static final int MSG_SET_STEPLEN_WEIGHT_OK = 4;
    public static final int MSG_SYNC_OFFLINE_DATA_COMPLETED = 5;
    public static final int MSG_SYNC_OFFLINE_DATA = 6;
    public static final int MSG_SHOW_LOST_WARNING = 7;
    public static final int MSG_STOP_LOST_WARNING = 8;
    public static final int MSG_SYNC_OFFLINE_DATA_FAILED = 9;
    public static final int MSG_SET_UNIT_METRIC_OK = 10;
    public static final int MSG_SET_UNIT_INCH_OK = 11;
    public static final int MSG_THEME_CHANGED = 12;
    public static final int MSG_SWITCH_LOST_WARNING = 13;
    public static final int MSG_WEIGHT_CLICK = 14;
    public static final int CURRENT_RSSI = 15;
    public static final int CONTACT_ITEM_LONG_CLICK_EVENT = 16;
    public static final int MEMBER_LETTER_EVENT = 17;
    public static final int SEARCH_USER_ITEM_CLICK = 18;
    public static final int CONTACT_ITEM_CLICK = 19;
    public static final int RATE_TEST_FAILE = 20;
    public static final int SCAN_FINISH = 21;
    public static final int SCANNED_DEVICE = 22;
    public static final int DEVICE_CONNECTING = 23;
    public static final int LOGIN_SUCCESS = 24;
    public static final int LOGIN_FAILED = 25;
    public static final int STEP_CHANGE = 26;
    public static final int SET_SEDENTARY_REMIND_TIME_SUCCESS = 27;
    public static final int UPGRADE_FIRMWARE_PROGRESS = 28;
    public static final int UPGRADE_FIRMWARE_SERVER_BUSY = 29;
    public static final int DOWNLOAD_FIRMWARE_FAIL = 30;
    public static final int START_UPGRADE_FIRMWARE = 31;
    public static final int UPGRADE_FIRMWARE_SUCCESS = 32;
    public static final int UPGRADE_FIRMWARE_FAIL = 33;
    public static final int SET_SCREEN_OFF_TIME_SUCCESS = 34;
    public static final int RATE_VALUE = 35;
    public static final int BLUETOOTH_OFF = 36;
    public static final int BLUETOOTH_ON = 37;
    public static final int SET_DEVICE_ALARM_SUCCESS = 38;
    public static final int SET_LIFT_WRIST_SCREEN_ON_STATUS_SUCCESS = 39;
    public static final int DND_SCHEDULE_START_TIME = 40;
    public static final int DND_SCHEDULE_END_TIME = 41;
    public static final int HIGH_RATE_REMIND_STATUS = 42;
    public static final int HIGH_RATE_REMIND_VALUE = 43;
    public static final int SCHEDULE_MEASURE_STATUS = 44;
    public static final int SCHEDULE_MEASURE_TIME = 45;
    public static final int DND_SCHEDULE_STATUS = 46;
    public static final int DND_MESSAGE_STATUS = 47;
    public static final int DND_SCREEN_STATUS = 48;
    public static final int DND_VIBRATION_STATUS = 49;
    public static final int WEIGHT_UPDATE = 50;
    public static final int MODIFY_DEVICE_NAME_SUCCESS = 51;

    public static final int GET_UPDATE_DATE_SUCCESS = 52;
    public static final int CONNECT_NET_SUCCESS = 53;
    public static final int UPDATE_DB_SUCCESS = 54;
    public static final int UPDATE_DB_FAIL = 55;
    public static final int GET_UPDATE_DATE_FAIL = 56;
    public static final int CONNECT_DEVICE_TIME_OUT = 57;
    public static final int BATTERY = 58;
    public static final int DELETE_WEIGHT_SUCCESS = 59;
    public static final int QUERY_USER_SUCCESS = 60;
    public static final int QUERY_MAIL_SUCCESS = 61;
    public static final int RATE_STATUS = 62;
    public static final int DEVICE_SYNCING = 63;
    public static final int LOST_WARNING_STATE = 64;
    public static final int SEND_COMMAND_DISCONNECTED = 65;
    public static final int TAKE_PIC = 66;
    public static final int FIRMWARE_VERSION = 67;
    public static final int NO_NEW_VERSION = 68;
    public static final int CHECKING_FIRMWARE_VERSION = 69;
    public static final int ACCESS_SERVER_FREQUENT = 70;
    public static final int FOUND_NEW_VERSION = 71;
    public static final int MODIFY_DEVICE_NAME_FAILED = 72;
    public static final int BATTERY_TOO_LOW_TO_UPGRADE = 73;
    public static final int DEVICE_BOND_ALREADY = 74;
    public static final int UPLOAD_JOIN_BETA_MESSAGE_SUCCESS = 75;
    public static final int UPLOAD_JOIN_BETA_MESSAGE_FAILED = 76;
    public static final int UPLOAD_QUERY_JOIN_BETA_STATE_FAILED = 78;
    public static final int UPLOAD_QUERY_JOIN_BETA_STATE_SUCCESS = 79;
    public static final int UPLOAD_OUT_BETA_MESSAGE_FAILED = 80;
    public static final int UPLOAD_OUT_BETA_MESSAGE_SUCCESS = 81;
    public static final int UPLOAD_JOIN_BETA_MESSAGE_ERROR = 82;
    public static final int OBTAINS_BETTAERY_FAILED = 83;
    public static final int HEART_RATE_REAL_TIME_VALUE = 84;
    public static final int OPEN_REMIND_LIST = 85;
    public static final int DROP_REMIND_LIST = 86;
    public static final int PATCH_VERSION = 87;
    public static final int CHECK_NEW_VERSION_FAILED = 88;
    public static final int MODIFY_SCREEN_ORIENTATION_FAILED = 89;
    public static final int QUERY_TOTAL_STEP_SPORT_SUCCESS = 90;
    public static final int UPDATE_TOTAL_STEP_STEPS_SUCCESS = 91;
    public static final int SET_SCREEN_ORIENTATION_INVALID_CAUSED_BY_SYNCING = 92;
    public static final int OBTAINS_FIRMWARE_VERSION_FAILED = 93;
    public static final int SET_DEVICE_ALARM_FAILED = 94;
    public static final int BLOOD_MEASURE_RECEIVED_END = 95;
    public static final int BLOOD_MEASURE_RECEIVED = 96;
    public static final int BAND_STATE_DISCONNECTION = 97;
    public static final int QUERY_USER_FAILED = 98;
    public static final int QUERY_MAIL_FAILED = 99;
    public static final int UPDATE_UNIT_SUCCESS = 100;
    public static final int UPDATE_UNIT_FAILED = 101;
    public static final int CHECKING_FIRMWARE_VERSION_IN_SILENCE = 102;
    public static final int NO_NEW_VERSION_IN_SILENCE = 103;
    public static final int FOUND_NEW_VERSION_IN_SILENCE = 104;
    public static final int DOWNLOAD_FIRMWARE_PROGRESS_IN_SILENCE = 105;
    public static final int CHECK_NEW_VERSION_FAILED_IN_SILENCE = 106;
    public static final int DOWNLOAD_FIRMWARE_FAIL_IN_SILENCE = 107;
    public static final int DOWNLOAD_FIRMWARE_SUCCESS_IN_SILENCE = 108;
    public static final int FOUND_NEW_VERSION_ALREADY_DOWNLOAD_IN_SILENCE = 109;
    public static final int UPDATE_FIRMWARE_SERVICE_NOT_INIT = 110;

    public static final int UPDATE_USER_INFO_SETTING_SUCCESS = 111;
    public static final int UPDATE_USER_INFO_SETTING_FAILED = 112;
    public static final int REG_SUCCESS = 113;
    public static final int REG_FAILED = 114;
    public static final int CLOSE_ALL_DIALOG = 115;
    public static final int CLOSE_ALL_DIALOG_ERROR = 116;
    public static final int SEND_PASSWORD_EMAIL_SUCCESS = 117;
    public static final int SEND_PASSWORD_EMAIL_FAILED = 118;
    public static final int REGISTER_WITH_PHONE_SUCCESS = 119;
    public static final int REGISTER_WITH_PHONE_FAILED = 120;
    public static final int REGISTER_WITH_EMAIL_SUCCESS = 121;
    public static final int REGISTER_WITH_EMAIL_FAILED = 122;
    public static final int REQUEST_VERIFY_CODE_SUCCESS = 123;
    public static final int REQUEST_VERIFY_CODE_FAILED = 124;
    public static final int UPDATE_ACCOUNT_INFO_SUCCESS = 125;
    public static final int UPDATE_ACCOUNT_INFO_FAILED = 126;
    public static final int UPDATE_TARGET_SUCCESS = 127;
    public static final int UPDATE_TARGET_FAILED = 128;
    public static final int UPDATE_NICKNAME_SUCCESS = 129;
    public static final int UPDATE_NICKNAME_FAILED = 130;
    public static final int UPDATE_GENDER_SUCCESS = 131;
    public static final int UPDATE_GENDER_FAILED = 132;
    public static final int UPDATE_HEIGHT_SUCCESS = 133;
    public static final int UPDATE_HEIGHT_FAILED = 134;
    public static final int UPDATE_WEIGHT_SUCCESS = 135;
    public static final int UPDATE_WEIGHT_FAILED = 136;
    public static final int UPDATE_BIRTHDAY_SUCCESS = 137;
    public static final int UPDATE_BIRTHDAY_FAILED = 138;
    public static final int UPLOAD_AVATAR_SUCCESS = 139;
    public static final int UPLOAD_AVATAR_FAILED = 140;
    public static final int UPDATE_UNIT_SYSTEM_SUCCESS = 141;
    public static final int UPDATE_UNIT_SYSTEM_FAILED = 142;
    public static final int UPLOAD_BACKGROUND_SUCCESS = 143;
    public static final int UPLOAD_BACKGROUND_FAILED = 144;
    public static final int RESET_PWD_REQUEST_VERIFY_CODE_SUCCESS = 145;
    public static final int RESET_PWD_REQUEST_VERIFY_CODE_FAILED = 146;
    public static final int RESET_PWD_SUCCESS = 147;
    public static final int RESET_PWD_FAILED = 148;
    public static final int RESET_PWD_SUCCESS_BY_EMAIL = 149;
    public static final int RESET_PWD_FAILED_BY_EMAIL = 150;
    public static final int UPLOAD_DAY_DATA_SUCCESS = 151;

    private int tag;
    private Object object;
    private int index;
    private int rssi;
    public String memberId;
    public String userAvatarUrl;
    public String username;
    public int search_item;

    public HandleEvent(int tag) {
        this.tag = tag;
    }

    public HandleEvent() {

    }

    public HandleEvent(int tag, Object object) {
        this.tag = tag;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }


    public int getIndex() {
        return index;
    }

    public HandleEvent setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getRssi() {
        return rssi;
    }

    public HandleEvent setRssi(int rssi) {
        this.rssi = rssi;
        return this;
    }
}
