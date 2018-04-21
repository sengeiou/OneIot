package com.coband.cocoband.mvp.model.local.prefrences;

import android.support.annotation.IntRange;

import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.EncodeUtil;
import com.coband.common.utils.SPUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ivan on 17-4-10.
 * all preferences value change must be access by this class
 */

public class PreferencesHelper {

    private static final String TAG = "PreferenceHelper";

    public static boolean getFirstLaunchFlag() {
        return SPUtil.getBoolean(C.KEY_FIRST_LAUNCH, true);
    }

    public static void setFirstLauncherFlag(boolean flag) {
        SPUtil.putBoolean(C.KEY_FIRST_LAUNCH, flag);
    }

    public static void setConnectedDeviceAddress(String address) {
        SPUtil.putString(C.KEY_ADDRESS, address);
    }

    public static String getConnectedDeviceAddress() {
        return SPUtil.getString(C.KEY_ADDRESS, "");
    }

    public static void setLatestUserName(String userName) {
        SPUtil.putString(C.KEY_USERNAME, userName);
    }

    public static int getDashboardAppearance() {
        return SPUtil.getInt(C.DASHBOARD_APPEARANCE, C.DASHBOARD_LIST);
    }

    public static void setDashboardAppearance(@IntRange(from = 0, to = 1) int appearance) {
        SPUtil.putInt(C.DASHBOARD_APPEARANCE, appearance);
    }

    public static int getDashboardModule() {
        return SPUtil.getInt(C.DASHBOARD_MODULE, C.MODULE_ALL);
    }

    public static void setDashboardModule(int module) {
        int existModule = SPUtil.getInt(C.DASHBOARD_MODULE, module);
        SPUtil.putInt(C.DASHBOARD_MODULE, existModule | module);
    }

    public static boolean is24TimeSystem() {
        return SPUtil.getBoolean(C.IS_24_TIME_SYSTEM, false);
    }

    public static void setTimeSystem(boolean is24Hours) {
        SPUtil.putBoolean(C.IS_24_TIME_SYSTEM, is24Hours);
    }

    public static int getSedentaryRemindStatus() {
        return SPUtil.getInt(C.KEY_SEDENDARY_REMIND, Config.SEDENTARY_REMIND_CLOSE);
    }

    public static void setSedentaryRemindStatus(@Config.SedentaryRemind int status) {
        SPUtil.putInt(C.KEY_SEDENDARY_REMIND, status);
    }

    public static void setSedentaryRemindPeriod(int minute) {
        SPUtil.putInt(C.KEY_SEDENDARY_TIME, minute);
    }

    public static int getSedentaryRemindPeriod() {
        return SPUtil.getInt(C.KEY_SEDENDARY_TIME, Config.SEDENTARY_PERIOD_60);
    }

    public static boolean isSupportPushContent() {
        return SPUtil.getBoolean(C.IS_SUPPORT_PUSH_CONTENT, false);
    }

    public static void setSupportPushContent(boolean support) {
        SPUtil.putBoolean(C.IS_SUPPORT_PUSH_CONTENT, support);
    }

    public static void setSleepDataSyncAlready(boolean already) {
        SPUtil.putBoolean(C.WHETHER_SYNC_SLEEP, already);
    }

    public static boolean isSleepDataSyncAlready() {
        return SPUtil.getBoolean(C.WHETHER_SYNC_SLEEP, false);
    }

    public static void setLostRemindStatus(boolean open) {
        SPUtil.putBoolean(C.KEY_LOST_WARNING, open);
    }

    public static boolean isLostRemindOpen() {
        return SPUtil.getBoolean(C.KEY_LOST_WARNING, Config.DEFAULT_LOST_WARNING_STATUS);
    }

    public static void setDeviceType(int type) {
        SPUtil.putInt(C.DEVICE_TYPE, type);
    }

    public static int getDeviceType() {
        return SPUtil.getInt(C.DEVICE_TYPE, C.DEVICE_NONE);
    }

    /**
     * 因为Ａｎｄｒｏｉｄ和leanCloud上的SportInfo表的DeviceType定义不一致了，这里是为了兼容
     */
    public static String getDeviceTypeForRemote() {
        int type = PreferencesHelper.getDeviceType();
        switch (type) {
            case C.DEVICE_TYPE_DBL:
                return "CoBand";
            case C.DEVICE_TYPE_K3:
                return "CoBandK3";
            case C.DEVICE_TYPE_K4:
                return "CoBandK4";
            case C.DEVICE_TYPE_K9:
                return "CoBandK9";
            case C.DEVICE_TYPE_XONE:
                return "CoBandXONE";
            default:
                return "CoBand";
        }
    }


    public static String getDeviceName(String address) {
        return SPUtil.getString(address, null);
    }

    public static void setDeviceName(String address, String aliasName) {
        SPUtil.putString(address, aliasName);
    }

    public static boolean getLightScreenStatus() {
        return SPUtil.getBoolean(C.RAISE_HAND_BRIGHT, true);
    }

    public static void setLightScreenStatus(boolean open) {
        SPUtil.putBoolean(C.RAISE_HAND_BRIGHT, open);
    }

    public static int getScreenSleepTime() {
        return SPUtil.getInt(C.KEY_SCREEN_SLEEP, Config.SCREEN_SLEEP_TIME_5);
    }

    public static void setScreenSleepTime(int time) {
        SPUtil.putInt(C.KEY_SCREEN_SLEEP, time);
    }


    public static boolean getLostWarningStatus() {
        return SPUtil.getBoolean(C.KEY_LOST_WARNING, false);
    }

    public static void setLostWarningStatus(boolean open) {
        SPUtil.putBoolean(C.KEY_LOST_WARNING, open);
    }

    public static boolean getFirstAlarmStatus() {
        return SPUtil.getBoolean(C.KEY_FIRST_ALARM_STATUS, false);
    }

    public static void setFirstAlarmStatus(boolean open) {
        SPUtil.putBoolean(C.KEY_FIRST_ALARM_STATUS, open);
    }

    public static boolean getSecondAlarmStatus() {
        return SPUtil.getBoolean(C.KEY_SECOND_ALARM_STATUS, false);
    }

    public static void setSecondAlarmStatus(boolean open) {
        SPUtil.putBoolean(C.KEY_SECOND_ALARM_STATUS, open);
    }

    public static boolean getThirdAlarmStatus() {
        return SPUtil.getBoolean(C.KEY_THIRD_ALARM_STATUS, false);
    }

    public static void setThirdAlarmStatus(boolean open) {
        SPUtil.putBoolean(C.KEY_THIRD_ALARM_STATUS, open);
    }

    public static int getFirstAlarmHour() {
        return SPUtil.getInt(C.KEY_FIRST_ALARM_HOUR, 7);
    }

    public static void setFirstAlarmHour(int hour) {
        SPUtil.putInt(C.KEY_FIRST_ALARM_HOUR, hour);
    }

    public static int getFirstAlarmMinute() {
        return SPUtil.getInt(C.KEY_FIRST_ALARM_MINUTE, 0);
    }

    public static void setFirstAlarmMinute(int minute) {
        SPUtil.putInt(C.KEY_FIRST_ALARM_MINUTE, minute);
    }


    public static int getSecondAlarmHour() {
        return SPUtil.getInt(C.KEY_SECOND_ALARM_HOUR, 8);
    }

    public static void setSecondAlarmHour(int hour) {
        SPUtil.putInt(C.KEY_SECOND_ALARM_HOUR, hour);
    }

    public static int getSecondAlarmMinute() {
        return SPUtil.getInt(C.KEY_SECOND_ALARM_MINUTE, 0);
    }

    public static void setSecondAlarmMinute(int minute) {
        SPUtil.putInt(C.KEY_SECOND_ALARM_MINUTE, minute);
    }

    public static int getThirdAlarmHour() {
        return SPUtil.getInt(C.KEY_THIRD_ALARM_HOUR, 9);
    }

    public static void setThirdAlarmHour(int hour) {
        SPUtil.putInt(C.KEY_THIRD_ALARM_HOUR, hour);
    }

    public static int getThirdAlarmMinute() {
        return SPUtil.getInt(C.KEY_THIRD_ALARM_MINUTE, 0);
    }

    public static void setThirdAlarmMinute(int minute) {
        SPUtil.putInt(C.KEY_THIRD_ALARM_MINUTE, minute);
    }

    public static void setFirstAlarmPeriod(int period) {
        SPUtil.putInt(C.KEY_FIRST_ALARM_PEROID, period);
    }

    public static int getFirstAlarmPeriod() {
        return SPUtil.getInt(C.KEY_FIRST_ALARM_PEROID, 127); // 127 means everyday
    }

    public static void setSecondAlarmPeriod(int period) {
        SPUtil.putInt(C.KEY_SECOND_ALARM_PEROID, period);
    }

    public static int getSecondAlarmPeriod() {
        return SPUtil.getInt(C.KEY_SECOND_ALARM_PEROID, 127); // 127 means everyday
    }

    public static void setThirdAlarmPeriod(int period) {
        SPUtil.putInt(C.KEY_THIRD_ALARM_PEROID, period);
    }

    public static int getThirdAlarmPeriod() {
        return SPUtil.getInt(C.KEY_THIRD_ALARM_PEROID, 127);// 127 means everyday
    }

    public static void setInCallRemindStatus(boolean open) {
        SPUtil.putBoolean(C.KEY_CALL_REMIND, open);
    }

    public static boolean getInCallRemindStatus() {
        return SPUtil.getBoolean(C.KEY_CALL_REMIND, false);
    }

    public static void setDeviceVersion(String version) {
        SPUtil.putString(C.KEY_DEVICE_VERSION, version);
    }

    public static String getDeviceVersion() {
        return SPUtil.getString(C.KEY_DEVICE_VERSION, "");
    }

    public static boolean getAppMessageRemindStatus() {
        return SPUtil.getBoolean(C.KEY_REMIND, false);
    }

    public static void setAppMessageRemindStatus(boolean status) {
        SPUtil.putBoolean(C.KEY_REMIND, status);
    }

    public static void setNetWorkStatus(boolean status) {
        SPUtil.putBoolean(C.KEY_NET_CONNECT, status);
    }

    public static boolean getNetWorkStatus() {
        return SPUtil.getBoolean(C.KEY_NET_CONNECT, true);
    }

    public static boolean getScreenOrientationStatus() {
        return SPUtil.getBoolean(C.KEY_SCREEN_ORIENTATION, false);
    }

    public static void setScreenOrientationStatus(boolean status) {
        SPUtil.putBoolean(C.KEY_SCREEN_ORIENTATION, status);
    }

    public static void setLastSyncTime(long time) {
        SPUtil.putLong(C.KEY_SYNC_TIME, time);
    }

    public static long getLastSyncTime() {
        return SPUtil.getLong(C.KEY_SYNC_TIME, 0L);
    }

    public static void setUserName(String userName) {
        SPUtil.putString(C.KEY_USERNAME, userName);
    }

    //save device name when lgoin
    public static void setDeviceNameWhenLogin(String tag, String deviceName) {
        SPUtil.putString(tag, deviceName);
    }

    public static int getTodaySteps() {
        return SPUtil.getInt(C.TODAY_STEPS, 0);
    }

    public static float getTodayCalories() {
        return SPUtil.getFloat(C.TODAY_CALORIES, 0);
    }

    public static float getTodayDistance() {
        return SPUtil.getFloat(C.TODAY_DISTANCE, 0f);
    }

    public static long getStepSaveDate() {
        return SPUtil.getLong(C.STEP_SAVED_DATE, 0);
    }

    public static void setTodayData(int steps, float calories, float distance) {
        SPUtil.putInt(C.TODAY_STEPS, steps);
        SPUtil.putFloat(C.TODAY_CALORIES, calories);
        SPUtil.putFloat(C.TODAY_DISTANCE, distance);
        SPUtil.putLong(C.STEP_SAVED_DATE, DateUtils.getToday());
        SPUtil.putString(C.TODAY_STEP_USER, getCurrentId());
    }

    public static void setConversationMute(String conversationId) {
        String s = SPUtil.getString(C.MUTE_NOTIFICATION, "");
        StringBuffer buffer = new StringBuffer();
        buffer.append(s);
        buffer.append(conversationId);
        buffer.append("@");
        SPUtil.putString(C.MUTE_NOTIFICATION, buffer.toString());
    }

    // 从某些界面进入会话界面时是没有conversationID的，只有PeerId，这里记录PeerId作静音回显示
    public static void setPeerMute(String peerId) {
        String s = SPUtil.getString(C.MUTE_PEER, "");
        StringBuffer buffer = new StringBuffer();
        buffer.append(s);
        buffer.append(peerId);
        buffer.append("@");
        SPUtil.putString(C.MUTE_PEER, buffer.toString());
    }

    public static void removePeerMute(String peerId) {
        String s = SPUtil.getString(C.MUTE_PEER, "");
        if (s.contains(peerId)) {
            String replace = s.replace(peerId + "@", "");
            SPUtil.putString(C.MUTE_PEER, replace);
        }
    }

    public static boolean isPeerMute(String peerId) {
        return SPUtil.getString(C.MUTE_PEER, "").contains(peerId);
    }

    public static List<String> getConversationMute() {
        String s = SPUtil.getString(C.MUTE_NOTIFICATION, "");
        String[] muteList = s.split("@");
        return Arrays.asList(muteList);
    }

    public static void removeConversationMute(String conversationId) {
        String s = SPUtil.getString(C.MUTE_NOTIFICATION, "");
        if (s.contains(conversationId)) {
            String replace = s.replace(conversationId + "@", "");
            SPUtil.putString(C.MUTE_NOTIFICATION, replace);
        }
    }

    public static boolean isConversationMute(String conversationId) {
        String s = SPUtil.getString(C.MUTE_NOTIFICATION, "");
        if (s.contains(conversationId)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getUidForSavedStep() {
        return SPUtil.getString(C.TODAY_STEP_USER, "");
    }


    public static boolean getDNDMessageRemindStatus() {
        return SPUtil.getBoolean(C.DND_MESSAGE_REMIND, false);
    }

    public static void setDNDMessageRemindStatus(boolean status) {
        SPUtil.putBoolean(C.DND_MESSAGE_REMIND, status);
    }

    public static boolean getDNDScreenRemindStatus() {
        return SPUtil.getBoolean(C.DND_SCREEN_REMIND, false);
    }

    public static void setDNDScreenRemindStatus(boolean status) {
        SPUtil.putBoolean(C.DND_SCREEN_REMIND, status);
    }

    public static boolean getDNDVibrationStatus() {
        return SPUtil.getBoolean(C.DND_VIBRATION_REMIND, false);
    }

    public static void setDNDVibrationStatus(boolean status) {
        SPUtil.putBoolean(C.DND_VIBRATION_REMIND, status);
    }

    public static int getDNDScheduleStartTime() {
        return SPUtil.getInt(C.DND_START_TIME, 1320); // 22:00 format to minute.
    }

    public static void setDNDScheduleStartTime(int time) { // the time is minute unit.
        SPUtil.putInt(C.DND_START_TIME, time);
    }

    public static int getDNDScheduleEndTime() {
        return SPUtil.getInt(C.DND_END_TIME, 480); // 08:00 format to minute.
    }

    public static void setDNDScheduleEndTime(int time) { // the time is minute unit.
        SPUtil.putInt(C.DND_END_TIME, time);
    }

    public static boolean getDNDScheduledStatus() {
        return SPUtil.getBoolean(C.DND_SCHEDULED_STATUS, false);
    }

    public static void setDNDScheduledStatus(boolean status) {
        SPUtil.putBoolean(C.DND_SCHEDULED_STATUS, status);
    }

    public static boolean getRealTimeHeartRateStatus() {
        return SPUtil.getBoolean(C.REAL_TIME_RATE, false);
    }

    public static void setRealTimeHeartRateStatus(boolean status) {
        SPUtil.putBoolean(C.REAL_TIME_RATE, status);
    }

    public static boolean getHighRateRemindStatus() {
        return SPUtil.getBoolean(C.HIGH_RATE_REMIND, false);
    }

    public static void setHighRateRemindStatus(boolean status) {
        SPUtil.putBoolean(C.HIGH_RATE_REMIND, status);
    }

    public static int getHighRateRemindValue() {
        return SPUtil.getInt(C.HIGH_RATE_REMIND_VALUE, 160);
    }

    public static void setHighRateRemindValue(int value) {
        SPUtil.putInt(C.HIGH_RATE_REMIND_VALUE, value);
    }

    public static boolean getScheduleMeasureStatus() {
        return SPUtil.getBoolean(C.SCHEDULE_MEASURE_STATUS, false);
    }

    public static void setScheduleMeasureStatus(boolean status) {
        SPUtil.putBoolean(C.SCHEDULE_MEASURE_STATUS, status);
    }

    public static int getScheduleMeasureTime() {
        return SPUtil.getInt(C.SCHEDULE_MEASURE_TIME, 1);
    }

    public static void setScheduleMeasureTime(int time) {
        SPUtil.putInt(C.SCHEDULE_MEASURE_TIME, time);
    }

    public static void setNotRemindVersionUpdate(String version) {
        SPUtil.putString(C.NO_REMIND_UPDATE, version);
    }

    public static String getNotRemindVersionUpdate() {
        return SPUtil.getString(C.NO_REMIND_UPDATE, "version");
    }

    public static boolean getPlaySoundEnable() {
        return SPUtil.getBoolean(C.PLAY_SOUND, true);
    }

    public static void setPlaySoundEnable(boolean playSoundEnable) {
        SPUtil.putBoolean(C.PLAY_SOUND, playSoundEnable);
    }

    public static void setBloodMeasureStatus(boolean status) {
        SPUtil.putBoolean(C.FIRST_MEASURE_BLOOD, status);
    }

    public static boolean getBloodMeasureStatus() {
        return SPUtil.getBoolean(C.FIRST_MEASURE_BLOOD, true);
    }

    public static void setIsSupportBloodPressure(boolean isSupport) {
        SPUtil.putBoolean(C.IS_SUPPORT_BLOOD_PRESSURE, isSupport);
    }

    public static boolean getIsSupportBloodPressure() {
        return SPUtil.getBoolean(C.IS_SUPPORT_BLOOD_PRESSURE, false);
    }

    public static boolean isSupportBPFunction() {
        return SPUtil.getBoolean(C.IS_SUPPORT_BLOOD_PRESSURE, false);
    }

    public static void setIsSupportWechat(boolean wechat) {
        SPUtil.putBoolean(C.IS_SUPPORT_BLOOD_PRESSURE, wechat);
    }

    public static boolean isSupportWechat() {
        return SPUtil.getBoolean(C.IS_SUPPORT_BLOOD_PRESSURE, false);
    }

    public static void setLatestPassword(String latestPassword) {
        SPUtil.putString(C.KEY_PASSWORD, latestPassword);
    }

    public static String getLatestPassword() {
        return SPUtil.getString(C.KEY_PASSWORD, "");
    }

    public static String getLatestUserName() {
        return SPUtil.getString(C.KEY_USERNAME, "");
    }

    public static void setCurrentUid(String uid) {
        SPUtil.putString(C.CURRENT_UID, uid);
    }

    public static String getCurrentId() {
        return SPUtil.getString(C.CURRENT_UID, null);
    }
}
