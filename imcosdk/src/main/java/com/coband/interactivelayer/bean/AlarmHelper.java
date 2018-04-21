package com.coband.interactivelayer.bean;

import android.content.Context;
import android.support.annotation.NonNull;

import com.coband.interactivelayer.manager.CommandManager;
import com.coband.interactivelayer.manager.SendCommandCallback;
import com.coband.protocollayer.applicationlayer.ApplicationLayerAlarmPacket;
import com.coband.protocollayer.applicationlayer.ApplicationLayerAlarmsPacket;
import com.coband.utils.LogUtils;
import com.coband.utils.SPWristbandConfigInfo;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by mai on 17-9-20.
 */

public class AlarmHelper {

    private static final String TAG = "AlarmHelper";

    public static void saveAlarmListToSP(ArrayList<AlarmClockBean> alarmList, Context mContext) {
        for (int i = 0; i < alarmList.size(); i++) {
            AlarmClockBean bean = alarmList.get(i);
            int alarmId = bean.getAlarmId();
            String hour = String.valueOf(bean.getHour()).length() == 1
                    ? "0" + String.valueOf(bean.getHour())
                    : String.valueOf(bean.getHour());
            String minute = String.valueOf(bean.getMinute()).length() == 1
                    ? "0" + String.valueOf(bean.getMinute())
                    : String.valueOf(bean.getMinute());
            String timeString = hour + ":" + minute;
            byte dayFlag = bean.getDaysFlag();
            switch (alarmId) {
                case 0:
                    SPWristbandConfigInfo.setAlarmControlOne(mContext, bean.isEnable());
                    SPWristbandConfigInfo.setAlarmTimeOne(mContext, timeString);
                    SPWristbandConfigInfo.setAlarmFlagOne(mContext, dayFlag);
                    break;
                case 1:
                    SPWristbandConfigInfo.setAlarmControlTwo(mContext, bean.isEnable());
                    SPWristbandConfigInfo.setAlarmTimeTwo(mContext, timeString);
                    SPWristbandConfigInfo.setAlarmFlagTwo(mContext, dayFlag);
                    break;
                case 2:
                    SPWristbandConfigInfo.setAlarmControlThree(mContext, bean.isEnable());
                    SPWristbandConfigInfo.setAlarmTimeThree(mContext, timeString);
                    SPWristbandConfigInfo.setAlarmFlagThree(mContext, dayFlag);
                    break;
            }
        }
    }

    public static void saveAlarmListToSP(ApplicationLayerAlarmsPacket alarmsPacket, Context mContext) {
        ArrayList<ApplicationLayerAlarmPacket> alarmList = alarmsPacket.getAlarms();
        for (int i = 0; i < alarmList.size(); i++) {
            ApplicationLayerAlarmPacket bean = alarmList.get(i);
            int alarmId = bean.getId();
            String hour = String.valueOf(bean.getHour()).length() == 1
                    ? "0" + String.valueOf(bean.getHour())
                    : String.valueOf(bean.getHour());
            String minute = String.valueOf(bean.getMinute()).length() == 1
                    ? "0" + String.valueOf(bean.getMinute())
                    : String.valueOf(bean.getMinute());
            String timeString = hour + ":" + minute;
            byte dayFlag = bean.getDayFlags();
            switch (alarmId) {
                case 0:
                    SPWristbandConfigInfo.setAlarmControlOne(mContext, true);
                    SPWristbandConfigInfo.setAlarmTimeOne(mContext, timeString);
                    SPWristbandConfigInfo.setAlarmFlagOne(mContext, dayFlag);
                    break;
                case 1:
                    SPWristbandConfigInfo.setAlarmControlTwo(mContext, true);
                    SPWristbandConfigInfo.setAlarmTimeTwo(mContext, timeString);
                    SPWristbandConfigInfo.setAlarmFlagTwo(mContext, dayFlag);
                    break;
                case 2:
                    SPWristbandConfigInfo.setAlarmControlThree(mContext, true);
                    SPWristbandConfigInfo.setAlarmTimeThree(mContext, timeString);
                    SPWristbandConfigInfo.setAlarmFlagThree(mContext, dayFlag);
                    break;
            }
        }

    }

    public static ArrayList<AlarmClockBean> getCurrentAlarmList(Context context) {

        ArrayList<AlarmClockBean> alarmList = new ArrayList<>();

        byte dayFlagOne = SPWristbandConfigInfo.getAlarmFlagOne(context);
        int hourOne = 0, minuteOne = 0;
        String[] timeStringOne = SPWristbandConfigInfo.getAlarmTimeOne(context).split(":");
        if (timeStringOne.length == 2) {
            hourOne = Integer.valueOf(timeStringOne[0]);
            minuteOne = Integer.valueOf(timeStringOne[1]);
        }
        LogUtils.d(TAG, "syncAlarmListToRemote, hourOne: " + hourOne + ", minuteOne: " + minuteOne);
        AlarmClockBean alarmOne = new AlarmClockBean(hourOne, minuteOne, dayFlagOne, 0, SPWristbandConfigInfo.getAlarmControlOne(context));
        alarmList.add(alarmOne);


        byte dayFlagTwo = SPWristbandConfigInfo.getAlarmFlagTwo(context);
        int hourTwo = 0, minuteTwo = 0;
        String[] timeStringTwo = SPWristbandConfigInfo.getAlarmTimeTwo(context).split(":");
        if (timeStringTwo.length == 2) {
            hourTwo = Integer.valueOf(timeStringTwo[0]);
            minuteTwo = Integer.valueOf(timeStringTwo[1]);
        }
        LogUtils.d(TAG, "syncAlarmListToRemote, hourOne: " + hourTwo + ", minuteOne: " + minuteTwo);
        AlarmClockBean alarmTwo = new AlarmClockBean(hourTwo, minuteTwo, dayFlagTwo, 0, SPWristbandConfigInfo.getAlarmControlTwo(context));
        alarmList.add(alarmTwo);


        byte dayFlagThree = SPWristbandConfigInfo.getAlarmFlagThree(context);
        int hourThree = 0, minuteThree = 0;
        String[] timeStringThree = SPWristbandConfigInfo.getAlarmTimeThree(context).split(":");
        if (timeStringThree.length == 3) {
            hourThree = Integer.valueOf(timeStringThree[0]);
            minuteThree = Integer.valueOf(timeStringThree[1]);
        }
        LogUtils.d(TAG, "syncAlarmListToRemote, hourOne: " + hourThree + ", minuteOne: " + minuteThree);
        AlarmClockBean alarmThree = new AlarmClockBean(hourThree, minuteThree, dayFlagThree, 0, SPWristbandConfigInfo.getAlarmControlThree(context));
        alarmList.add(alarmThree);


        return alarmList;
    }

    public static void syncAlarmListToRemote(@NonNull ArrayList<AlarmClockBean> alarmList, Context context, SendCommandCallback callback) {
        if (alarmList.size() > 0)
            saveAlarmListToSP(alarmList, context);

        Calendar c1 = Calendar.getInstance();
        int hourOfDay = c1.get(Calendar.HOUR_OF_DAY);

        byte dayFlagOne = SPWristbandConfigInfo.getAlarmFlagOne(context);
        int hourOne = 0, minuteOne = 0;
        boolean dayAddFlagOne = false;
        String timeStringOne[] = SPWristbandConfigInfo.getAlarmTimeOne(context).split(":");
        if (timeStringOne.length == 2) {
            hourOne = Integer.valueOf(timeStringOne[0]);
            minuteOne = Integer.valueOf(timeStringOne[1]);
            LogUtils.d(TAG, "syncAlarmListToRemote, hourOne: " + hourOne + ", minuteOne: " + minuteOne);
            if (dayFlagOne == 0x00) {
                if (hourOne < hourOfDay) {
                    dayAddFlagOne = true;
                } else if ((hourOne == hourOfDay)
                        && (minuteOne <= c1.get(Calendar.MINUTE))) {
                    dayAddFlagOne = true;
                }
            }
        }

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, (dayAddFlagOne ? 1 : 0));
        ApplicationLayerAlarmPacket alarmOne =
                new ApplicationLayerAlarmPacket(c2.get(Calendar.YEAR),
                        c2.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
                        c2.get(Calendar.DATE),
                        hourOne,
                        minuteOne,
                        0,
                        dayFlagOne);

        byte dayFlagTwo = SPWristbandConfigInfo.getAlarmFlagTwo(context);
        int hourTwo = 0, minuteTwo = 0;
        boolean dayAddFlagTwo = false;
        String timeStringTwo[] = SPWristbandConfigInfo.getAlarmTimeTwo(context).split(":");
        if (timeStringTwo.length == 2) {
            hourTwo = Integer.valueOf(timeStringTwo[0]);
            minuteTwo = Integer.valueOf(timeStringTwo[1]);
            LogUtils.d(TAG, "syncAlarmListToRemote, hourTwo: " + hourTwo + ", minuteTwo: " + minuteTwo);
            if (dayFlagTwo == 0x00) {
                if (hourTwo < hourOfDay) {
                    dayAddFlagTwo = true;
                } else if ((hourTwo == hourOfDay)
                        && (minuteTwo <= c1.get(Calendar.MINUTE))) {
                    dayAddFlagTwo = true;
                }
            }
        }

        c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, (dayAddFlagTwo ? 1 : 0));
        ApplicationLayerAlarmPacket alarmTwo =
                new ApplicationLayerAlarmPacket(c2.get(Calendar.YEAR),
                        c2.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
                        c2.get(Calendar.DATE),
                        hourTwo,
                        minuteTwo,
                        1,
                        dayFlagTwo);

        byte dayFlagThree = SPWristbandConfigInfo.getAlarmFlagThree(context);
        int hourThree, minuteThree;
        boolean dayAddFlagThree = false;
        String timeStringThree[] = SPWristbandConfigInfo.getAlarmTimeThree(context).split(":");
        hourThree = Integer.valueOf(timeStringThree[0]);
        minuteThree = Integer.valueOf(timeStringThree[1]);
        LogUtils.d(TAG, "syncAlarmListToRemote, hourThree: " + hourThree + ", minuteThree: " + minuteThree);
        if (dayFlagThree == 0x00) {
            if (hourThree < hourOfDay) {
                dayAddFlagThree = true;
            } else if ((hourThree == hourOfDay)
                    && (minuteThree <= c1.get(Calendar.MINUTE))) {
                dayAddFlagThree = true;
            }
        }
        c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, (dayAddFlagThree ? 1 : 0));
        ApplicationLayerAlarmPacket alarmThree =
                new ApplicationLayerAlarmPacket(c2.get(Calendar.YEAR),
                        c2.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
                        c2.get(Calendar.DATE),
                        hourThree,
                        minuteThree,
                        2,
                        dayFlagThree);

        ApplicationLayerAlarmsPacket alarmsPacket = new ApplicationLayerAlarmsPacket();
        if (SPWristbandConfigInfo.getAlarmControlOne(context)) {
            alarmsPacket.add(alarmOne);
        }
        if (SPWristbandConfigInfo.getAlarmControlTwo(context)) {

            alarmsPacket.add(alarmTwo);
        }
        if (SPWristbandConfigInfo.getAlarmControlThree(context)) {

            alarmsPacket.add(alarmThree);
        }

        if (CommandManager.isConnected()) {
            if (alarmsPacket.size() == 0) {
                CommandManager.setClocksPacket(null, callback);
            } else {
                CommandManager.setClocksPacket(alarmsPacket, callback);
            }
        }
    }
}
