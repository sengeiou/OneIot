package cn.leancloud.chatkit.utils;

import android.content.Context;

import com.avos.avoscloud.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.leancloud.chatkit.R;

/**
 * Created by mai on 17-6-6.
 */

public class DateUtils {

    public static int getDateByMilliseconds(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        } else {
            Calendar.getInstance().setTimeInMillis(milliseconds);
            return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
    }

    public static long getToday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 将当天的时、分、秒、毫秒清0，从而获得年月日的Long型值
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTimeInMillis();
    }



    public static List<Date> getEveryDateOfMonth(long milliseconds) {
        List<Date> monthDays = new ArrayList<>();
        if (milliseconds <= 0) {
            return null;
        } else {
            Calendar mCal = Calendar.getInstance();
            mCal.setTimeInMillis(milliseconds);
            mCal.set(Calendar.DAY_OF_MONTH, 1);
            int month = mCal.get(Calendar.MONTH);
            while (month == mCal.get(Calendar.MONTH)) {
                monthDays.add(mCal.getTime());
                mCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return monthDays;
    }

    public static String getDateByMilliseconds(String pattern, long milliseconds) {
        if (milliseconds <= 0) {
            return "";
        } else {
            Calendar mCal = Calendar.getInstance();
            mCal.setTimeInMillis(milliseconds);
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);

            String format = df.format(mCal.getTime());
            return format;
        }
    }


    public static List<Date> getWeekOfDateByMilliseconds(long millseconds) {
        if (millseconds == 0) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(millseconds);

            // "calculate" the start date of the week
            Calendar first = (Calendar) cal.clone();
            first.add(Calendar.DAY_OF_WEEK,
                    first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
            List<Date> days = new ArrayList<>();
            days.add(first.getTime());
            for (int i = 0; i < 6; i++) {
                first.add(Calendar.DAY_OF_YEAR, 1);
                Calendar day = (Calendar) first.clone();
                days.add(day.getTime());
            }
            return days;
        }
    }

    /**
     * get the week of the specified timeStamp
     *
     * @param pattern
     * @param milliseconds
     * @return
     */
    public static List<String> getWeekOfDateByMilliseconds(String pattern, long milliseconds) {
        List<String> stringDates = new ArrayList<>();
        List<Date> dates = getWeekOfDateByMilliseconds(milliseconds);
        for (int i = 0; i < dates.size(); i++) {
            stringDates.add(getDateByMilliseconds(pattern, dates.get(i).getTime()));
        }

        return stringDates;
    }

    /**
     * @param origin e.g 20160423
     * @return e.g 04/23
     */
    public static String getDate(String origin) {
        String date = origin.substring(4, origin.length());
        String first = date.substring(0, 2);
        String second = date.substring(2, date.length());
        return first + "/" + second;
    }

    /**
     * @param pattern
     * @param date
     * @return million seconds from date, 0 is error
     */
    public static long getMillionSeconds(String pattern, String date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return 0;
    }


    public static int getYearByMilliseconds(long milliseconds) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(milliseconds);
        return mCalendar.get(Calendar.YEAR);
    }

    public static String getMessageTime(long timeStamp, Context context) {
        long todayZero = getToday();
        long yesterdayZero = todayZero - 24 * 60 * 60 * 1000;
        // get current week
        List<Date> weeks = getWeekOfDateByMilliseconds(System.currentTimeMillis());
        // get first day of current week
        long time = weeks.get(0).getTime();

        if (timeStamp >= todayZero) {
            return getDateByMillisecondsLoacale("HH:mm", timeStamp);
        } else if (timeStamp >= yesterdayZero) {
            return context.getResources().getString(R.string.yesterday);
        } else if (timeStamp >= time) {
            return getDateByMillisecondsLoacale("EEE", timeStamp);
        } else {
            return getDateByMillisecondsLoacale("yyyy/MM/dd", timeStamp);
        }
    }

    public static String getDateByMillisecondsLoacale(String pattern, long milliseconds) {
        if (milliseconds <= 0) {
            return "";
        } else {
            Calendar mCal = Calendar.getInstance();
            mCal.setTimeInMillis(milliseconds);
            SimpleDateFormat df = new SimpleDateFormat(pattern);

            String format = df.format(mCal.getTime());
            return format;
        }
    }
}
