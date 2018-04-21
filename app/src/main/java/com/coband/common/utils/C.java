package com.coband.common.utils;

/**
 * Created by mqh on 3/15/16.
 */
public class C {
    //me model
    public static final String AVATAR = "avatar";

    //target
    public static final String STEPS_TARGET = "steps_target2";
    public static final String SLEEP_TARGET = "sleep_target";

    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String WEIGHT_TARGET = "weight_target";//// FIXME: 11/9/16 discarded
    public static final String BACK_STACK = "back_stack";
    public static final String SHOW_AD = "SHOW_AD";
    public static final String INVITATION_ACTION = "invitation_action";
    public static final int PAGE_SIZE = 20;
    public static final String UPDATED_AT = "updatedAt";
    public static final int ORDER_UPDATED_AT = 1;
    public static final int ORDER_DISTANCE = 0;
    public static final String SOCIAL_BEAN = "social_bean";
    public static final String USERAVATARURL = "userAvatarUrl";
    public static final String USERNAME = "userName";
    public static final String NICKNAME = "nickName";
    public static final String EMAIL = "email";
    public static final String CURRENT_DEVICE_BLE_NAME = "current_device_ble_name";
    public static final String BAND_BLE_DEFAULT_NAME = "MH08";
    public static final String MUTE_NOTIFICATION = "mute_notification";
    public static final String MUTE_PEER = "mute_peer";
    public static final String HOR_AND_VER = "hor_and_ver";
    public static final String RAISE_HAND_BRIGHT = "light_on_screen";
    public static final String IS_24_TIME_SYSTEM = "is_24_time_system";
    public static final String CHINA_WEB = "china_web";
    public static final String WORLD_WEB = "world_web";
    public static final String BATTERY = "battery";
    public static final String WEB_NAME = "web_name";
    public static final String IS_SUPPORT_PUSH_CONTENT = "is_suppoert_push_content";
    public static final String KEY_UPDATE_STATUS = "update_status";
    public static final String PLAY_SOUND = "play_sound";
    public static final String FIRST_MEASURE_BLOOD = "first_measure_blood";
    public static final String IS_SUPPORT_BLOOD_PRESSURE = "is_support_blood_pressure";
    public static final String IS_SUPPORT_WECHAT = "is_support_wechat";
    public static final String CURRENT_UID = "uid";


    public static String yyyy_MM_dd = "yyyy-MM-dd";

    //HeartRateDataBase
    public static final String TABLE_NAME = "rate";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String MEASURE = "measure";
    public static final String FEEL = "feel";

    //local record heart rate
    public static final String HR_RECORD_TABLE_NAME = "heart_rate_record";
    public static final String HR_RECORD_RATE = "record_rate";
    public static final String HR_RECORD_TIME = "record_time";
    public static final String HR_RECORD_CALENDER = "record_calender";

    //local record sleep date
    public static final String SLEEP_RECORD_TABLE_NAME = "sleep_table_name";
    public static final String FINISH_TARGET = "finish_target";
    public static final String TOTAL_SLEEP_TIME = "total_sleep_time";
    public static final String LIGHT_SLEEP_TIME = "light_sleep_time";
    public static final String DEEP_SLEEP_TIME = "deep_sleep_time";
    public static final String AWAKE_TIME = "awake_time";
    public static final String SLEEP_RECORD_CALENDER = "sleep_record_calender";

    //device name
    public static final String BAND_DEFAULT_NAME = "CoBand_DBL";
    public static final CharSequence BAND_BLE_DEFAULT_NAME_Prefix = "MH";
    public static final CharSequence BAND_BLE_DEFAULT_NAME_Prefix_2 = "RH";
    public static final CharSequence BAND_BLE_DEFAULT_NAME_Prefix_3 = "TW";
    public static final CharSequence BAND_BLE_DEFAULT_NAME_Prefix_4 = "M6";
    public static final String DEVICE_TYPE = "device_type";
    public static final long HOUR_MILLISECONDS = 21600000;
    public static final String COCO_SHARE_PATH = "/coco_share";

    //The flag when user select device from devices fragment
    public static final String SELECT_DEVICE = "SELECTE_DEVICE";

    //The key of put to leanCloud

    public static final String LEANCLOUD_RANK_TABLE = "Rank";
    public static final String LEANCLOUD_RANK_RANKING = "ranking";
    public static final String LEANCLOUD_RANK_USER = "user";
    public static final String LEANCLOUD_RANK_LIKES = "likes";
    public static final String SOCIAL_CACHE = "social_cache";
    public static final String LEANCLOUD_TODAYRANK_TABLE = "TodayRankBean";
    public static final String LEANCLOUD_TODAYRANK_STEP = "step";
    public static final String LEANCLOUD_TODAYRANK_TIMESTAMP = "timeStamp";

    public static final long ONE_DAY_MILLISECONDS = 86400000L;
    public static final long ONE_DAY_SECONDS = 86400l;

    public static final boolean DEFAULT_SMART_UNLOCK_STATUS = false;
    public static final String LOCK_SCREEN_PASS_WORD = "lock_screen_pass_word";
    public static final String QQ_MESSAGE_REMIND = "qq_message_remind";
    public static final String WECHAT_MESSAGE_REMIND = "wechat_message_remind";
    public static final String APP_MESSAGE_NOTIFICATION = "app_message_notification";
    public static final String NO_CACHE = "noCache";

    public static final int TYPE_NEVER_BOND = 0;
    public static final int TYPE_UNBOND = 1;
    public static final int TYPE_SPORT_NO_GOAL = 2;
    public static final int TYPE_SPORT_COMPLETION = 3;
    public static final int TYPE_SPORT_COMPLETED_GOAL_20000 = 4;
    public static final int TYPE_SPORT_COMPLETED_GOAL_16000 = 5;
    public static final int TYPE_SPORT_COMPLETED_GOAL_8000 = 6;
    public static final int TYPE_SPORT_COMPLETED_GOAL_4000 = 7;
    public static final int TYPE_SPORT_COMPLETED_GOAL_2000 = 8;
    public static final int TYPE_SLEEP_TIPS = 9;
    public static final int TYPE_SLEEP_COMPLETED = 10;
    public static final int TYPE_SLEEP_NO_GOAL = 11;
    public static final int TYPE_SLEEP_QUALITY_GOOD = 12;
    public static final int TYPE_SLEEP_QUALITY_NORMAL = 13;
    public static final int TYPE_SLEEP_QUALITY_BAD = 14;
    public static final int TYPE_LEADERBOARD_FIRST = 15;
    public static final int TYPE_LEADERBOARD_SECOND = 16;
    public static final int TYPE_LEADERBOARD_THIRD = 17;
    public static final int TYPE_LEADERBOARD_IN_100 = 18;
    public static final int TYPE_WELCOME = 19;
    public static final int TYPE_ACHIEVEMENT = 20;
    public static final int TYPE_HEART_RATE_NORMAL = 21;
    public static final int TYPE_HEART_RATE_HIGH = 22;
    public static final int TYPE_HEART_RATE_TOO_HIGH = 23;
    public static final int TYPE_HEART_RATE_LOW = 24;
    public static final int TYPE_HEART_RATE_TOO_LOW = 25;
    public static final int TYPE_HEART_RATE_HIGHEST = 26;
    public static final int TYPE_SPORT_UNCOMPLETE_STEP = 27;

    public static final String SPORT_INFO = "SportInfo";
    public static final String SLEEP_INFO = "SleepInfo";
    public static final String HEART_RATE_INFO = "HeartRateInfo";

    public static final int METRIC_SYSTEM = 0;
    public static final int INCH = 1;
    public static final String WHETHER_SYNC_SLEEP = "current_day_heart_sync";
    public static final String UPLOAD_YESTERDAY_DATA = "upload_yesterday_data";

    public static final String LAST_DAY_RECORD = "last_day_record";
    public static final String DATA_UPDATE_SUCCESS = "data_update_success";

    public static final String CURRENT_STEPS = "data_update_success";

    public static final String RSSI_MAX = "rssi_max";

    public static final String LEANCLOUD_WEIGHT = "Weight";
    public static final String WEIGHT_WITH_USER = "weightWithUser";
    public static final String WEIGHT_RECORD_TABLE_NAME = "weight_table_name";
    public static final String WEIGHT_TIME_STAMP = "timeStamp";
    public static final String WEIGHT = "weight";
    public static final String WEIGHT_BODY_MI = "bodyMI";
    public static final String WEIGHT_UNIT = "unit";

    public static final String WEiGHT_DATA = "weight_data";

    public static final int STONG = -45;
    public static final int WEAK = -95;
    public static final int NORMAL = -75;

    public static final String OBJECT_ID = "objectId";
    public static final String FOLLOWER = "follower";
    public static final String FOLLOWEE = "followee";

    public static final String emailPattern = "^[a-zA-Z0-9]+([._\\\\-]*[a-zA-Z0-9])*@([a-zA-Z0-9]+[-a-zA-Z0-9]*[a-zA-Z0-9]+.){1,63}[a-zA-Z0-9]+$";

    public static final String OLD_HOST = "http://ac-lk71houg.clouddn.com";
    public static final String NEW_HOST = "https://dn-lk71houg.qbox.me";
    public static final String IS_INIT = "is_init";


    // permissions request Code
    public static final int READ_CONTACTS_CODE = 1;
    public static final int READ_SMS_CODE = 2;
    public static final int CAMERA_CODE = 3;
    public static final int READ_EXTERNAL_STORAGE_CODE = 4;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 5;
    public static final int BIND_NOTIFICATION_LISTENER_SERVICE_CODE = 6;
    public static final int RECORD_AUDIO_CODE = 7;
    public static final int CALL_PHONE_CODE = 8;

    public static final int BLUE = 2;
    public static final int BLACK = 3;

    public static final String NO_DISTURBING = "no_disturbing";
    public static final String TIME_OPEN = "time_open";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String MESSAGE_NO_DISTURBING = "message_no_disturbing";
    public static final String VIBRATION_NO_DISTURBING = "vibration_no_disturbing";
    public static final String SCREEN_NO_DISTURBING = "screen_no_disturbing";
    public static final String NO_DISTURBING_START_HOUR = "no_disturbing_start_hour";
    public static final String NO_DISTURBING_START_MINUTE = "no_disturbing_start_minute";
    public static final String NO_DISTURBING_END_HOUR = "no_disturbing_end_hour";
    public static final String NO_DISTURBING_END_MINUTE = "no_disturbing_end_minute";
    public static final String HEART_RATE_REMIND = "heart_rate_remind";
    public static final String TIME_MEASURE = "time_measure";
    public static final String HEART_RATE_REMIND_VALUE = "heart_rate_remind_value";
    public static final String TIME_SPAN = "time_span";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_DEVICE_TYPE = "device_type";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_WATCH_BOND = "bond";
    public static final String KEY_BONDED_ADDRESS = "address";

    public static final String KEY_BAND_FIRST = "band_first";

    public static final String KEY_CALL = "call";
    public static final String KEY_SMS = "sms";
    public static final String KEY_SMART_UNLOCK = "smart_unlock";

    public static final String KEY_SEDENDARY_REMIND = "sedentary_remind";
    public static final String KEY_SEDENDARY_TIME = "sedentary_time";
    public static final String KEY_CALL_REMIND = "call_remind";
    public static final String KEY_SMS_REMIND = "sms_remind";
    public static final String KEY_LOST_WARNING = "lost_warning";
    public static final String KEY_SCREEN_SLEEP = "screen_sleep";
    public static final String KEY_PIN = "pin";

    public static final String KEY_FIRST_ALARM_STATUS = "first_alarm_status";
    public static final String KEY_SECOND_ALARM_STATUS = "second_alarm_status";
    public static final String KEY_THIRD_ALARM_STATUS = "third_alarm_status";

    public static final String KEY_FIRST_ALARM_HOUR = "first_alarm_hour";
    public static final String KEY_SECOND_ALARM_HOUR = "second_alarm_hour";
    public static final String KEY_THIRD_ALARM_HOUR = "third_alarm_hour";

    public static final String KEY_FIRST_ALARM_MINUTE = "first_alarm_minute";
    public static final String KEY_SECOND_ALARM_MINUTE = "second_alarm_minute";
    public static final String KEY_THIRD_ALARM_MINUTE = "third_alarm_minute";

    public static final String KEY_FIRST_ALARM_PEROID = "first_alarm_peroid";
    public static final String KEY_SECOND_ALARM_PEROID = "second_alarm_peroid";
    public static final String KEY_THIRD_ALARM_PEROID = "third_alarm_peroid";

    public static final String KEY_BONDED_ONCE = "bond_once";

    public static final String KEY_THEME = "theme";

    public static final String KEY_UNIT = "unit";
    public static final String NO_REMIND_UPDATE = "no_remind_update";

    public static final String TODAY_CALORIES = "today_calories";
    public static final String TODAY_STEPS = "today_steps";
    public static final String TODAY_DISTANCE = "today_distance";
//    public static final String CURRENT_DAY_STEPS = "current_day_steps";
//    public static final String CURRENT_DAY_DISTANCE = "current_day_distance";
//    public static final String CURRENT_DAY_CALORIES = "current_day_calories";


    //refactoring
    public static final String DASHBOARD_APPEARANCE = "appearance";
    public static final String KEY_REMIND = "remind";

    public static final int DASHBOARD_LIST = 0;
    public static final int DASHBOARD_BOX = 1;

    public static final String DASHBOARD_MODULE = "module";

    public static final int MODULE_ALL = 15;
    public static final int MODULE_NONE = 0;
    public static final int MODULE_SLEEP = 1;
    public static final int MODULE_HEART_RATE = 2;
    public static final int MODULE_WEIGHT = 4;
    public static final int MODULE_PRESSURE = 8;


    public static final String KEY_FIRST_LAUNCH = "first";
    public static final String KEY_CONNECTION_STATUS = "connection";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_BOND = "bond";


    public static final int DEVICE_NONE = -1;
    public static final int DEVICE_TYPE_DBL = 0;
    public static final int DEVICE_TYPE_K3 = 1;
    public static final int DEVICE_TYPE_K4 = 2;
    public static final int DEVICE_TYPE_WATCH = 3;
    public static final int DEVICE_TYPE_K9 = 4;
    public static final int DEVICE_TYPE_XONE = 5;

    public static final String KEY_DEVICE_VERSION = "device_version";

    public static final String KEY_NET_CONNECT = "network_connect";

    public static final String KEY_SCREEN_ORIENTATION = "screen_orientation";

    public static final String KEY_SYNC_TIME = "sync_time";

    public static final String STEP_SAVED_DATE = "step_saved_day";

    public static final String KEY_UNIT_SYSTEM = "unit_system";

    public static final String TODAY_STEP_USER = "step_user";

    public static final String DND_MESSAGE_REMIND = "dnd_message_remind";
    public static final String DND_SCREEN_REMIND = "dnd_screen_remind";
    public static final String DND_VIBRATION_REMIND = "dnd_vibration_remind";
    public static final String DND_START_TIME = "dnd_start_time";
    public static final String DND_END_TIME = "dnd_end_time";
    public static final String DND_SCHEDULED_STATUS = "dnd_scheduled";

    public static final String REAL_TIME_RATE = "real_time_rate";
    public static final String HIGH_RATE_REMIND = "high_rate_remind";
    public static final String HIGH_RATE_REMIND_VALUE = "high_rate_remind_value";
    public static final String SCHEDULE_MEASURE_STATUS = "schedule_measure";
    public static final String SCHEDULE_MEASURE_TIME = "schedule_measure_time";

    // bundle
    public static final String BLOOD_MEASURE = "measure";

    public static final String DBL_ORIGIN_NAME = "MH08";
    public static final String DBL_V1_ORIGIN_NAME = "RH08";
    public static final String K3_ORIGIN_NAME = "RH23";
    public static final String K4_WITH_BP_ORIGIN_NAME = "RB09";
    public static final String K4_ORIGIN_NAME = "RH09";
    public static final String X1_ORIGIN_NAME = "RB32B";

}
