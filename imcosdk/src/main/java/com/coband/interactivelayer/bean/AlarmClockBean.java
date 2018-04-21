package com.coband.interactivelayer.bean;

import android.support.annotation.IntRange;

/**
 * Created by mai on 17-8-29.
 */


public class AlarmClockBean {
    private int hour;
    private int minute;
    private byte daysFlag;
    private  int alarmId;

    private boolean enable;

    public AlarmClockBean(@IntRange(from = 0, to = 23)int hour, @IntRange(from = 0, to = 59)int minute,
                          byte daysFlag,@IntRange(from = 0, to = 2) int alarmId, boolean enable) {
        this.hour = hour;
        this.minute = minute;
        this.daysFlag = daysFlag;
        this.alarmId = alarmId;
        this.enable = enable;
    }

    public int getHour() {
        return hour;
    }


    public void setHour(@IntRange(from = 0, to = 23) int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(@IntRange(from = 0, to = 59)int minute) {
        this.minute = minute;
    }

    public byte getDaysFlag() {
        return daysFlag;
    }

    public void setDaysFlag(byte daysFlag) {
        this.daysFlag = daysFlag;
    }

    public int getAlarmId() {
        return alarmId;
    }


    public void setAlarmId(@IntRange(from = 0, to = 2)int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
