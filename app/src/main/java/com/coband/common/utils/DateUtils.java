package com.coband.common.utils;

import com.avos.avoscloud.LogUtil;
import com.coband.App;
import com.coband.watchassistant.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by imco on 12/22/15.
 */
public class DateUtils {
    public static int getCurrentOffset() {
        int minutes = getCurrentMinutes();
        return minutes / 15;

    }

    public static long getToday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 将当天的时、分、秒、毫秒清0，从而获得年月日的Long型值
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTimeInMillis() / 1000;
    }

    public static long getZeroByMilliSeconds(long seconds) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(seconds);
        // 将当天的时、分、秒、毫秒清0，从而获得年月日的Long型值
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTimeInMillis();
    }

    public static List<String> getEveryDateOfMonth(String pattern, long milliseconds) {
        List<String> stringMonthDays = new ArrayList<>();
        List<Date> monthDays = getEveryDateOfMonth(milliseconds);
        for (int i = 0; i < monthDays.size(); i++) {
            stringMonthDays.add(getDateBySeconds(pattern, monthDays.get(i).getTime()));
        }

        return stringMonthDays;
    }

    public static List<Date> getEveryDateOfMonth(long seconds) {
        List<Date> monthDays = new ArrayList<>();
        if (seconds <= 0) {
            return null;
        } else {
            Calendar mCal = Calendar.getInstance();
            mCal.setTimeInMillis(seconds * 1000);
            mCal.set(Calendar.DAY_OF_MONTH, 1);
            int month = mCal.get(Calendar.MONTH);
            while (month == mCal.get(Calendar.MONTH)) {
                monthDays.add(mCal.getTime());
                mCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return monthDays;
    }

    public static String getDateStrByMilliseconds(long milliseconds) {
        Logger.d("GuideHelloPresenter", "XXX  " + milliseconds + "");
        Logger.d("GuideHelloPresenter", "XXX  " + getUTCDateByMilliseconds("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'", milliseconds));
        return getUTCDateByMilliseconds("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'", milliseconds);
    }

    public static String getUTCDateByMilliseconds(String pattern, long milliseconds) {
//        if (milliseconds <= 0) {
//            return "";
//        } else {
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(milliseconds);
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = df.format(mCal.getTime());
        return format;
//        }
    }

    public static String getDateBySeconds(String pattern, long seconds) {
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(seconds * 1000);
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);

        String format = df.format(mCal.getTime());
        return format;
//        }
    }

    public static List<Date> getWeekOfDateBySeconds(long seconds) {
        if (seconds == 0) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(seconds * 1000);

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
     * @param seconds
     * @return
     */
    public static List<String> getWeekOfDateBySeconds(String pattern, long seconds) {
        List<String> stringDates = new ArrayList<>();
        List<Date> dates = getWeekOfDateBySeconds(seconds);
        for (int i = 0; i < dates.size(); i++) {
            stringDates.add(getDateBySeconds(pattern, dates.get(i).getTime()));
        }

        return stringDates;
    }

    /**
     * 用于cocoBand中获取指定日期所在的那一周的７个日期集合
     *
     * @param queryDate e.g.20101010
     * @return the week each day
     */
    public static List<String> getWeekOfDate(String queryDate) {
        long time = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat(C.yyyyMMdd, Locale.ENGLISH);
            time = format.parse(queryDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.log.e("query Date Error");
        }
        List<String> weekOfDates = getWeekOfDateBySeconds(C.yyyyMMdd, time);
        return weekOfDates;
    }

    /**
     * 用于cocoBand中获取指定日期所在的那一月的全部日期集合
     *
     * @param queryDate queryDate e.g.20101010
     * @return the month each day
     */
    public static List<String> getMonthOfDate(String queryDate) {
        long time = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat(C.yyyyMMdd, Locale.ENGLISH);
            time = format.parse(queryDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.log.e("query Date Error");
        }
        List<String> monthOfDates = DateUtils.getEveryDateOfMonth(C.yyyyMMdd, time);
        return monthOfDates;
    }

    /**
     * 根据输入常数
     *
     * @param offset 代表返回日期距离本月多少个月时间
     * @return　所求月份的第一天
     */
    public static String computeQueryDateOfMonth(int offset) {

        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < offset; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentTime);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            currentTime = cal.getTime().getTime() / 1000 - C.ONE_DAY_SECONDS;
        }
        return DateUtils.getDateBySeconds(C.yyyyMMdd, currentTime);
    }

    /**
     * 根据输入常数
     *
     * @param offset 距离今天所在月份的偏移
     * @return　所求月份的第一天
     */
    public static long getMonthFirstDay(int offset) {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < offset; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentTime);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            currentTime = cal.getTime().getTime() - C.ONE_DAY_MILLISECONDS;
        }
        return getZeroByMilliSeconds(currentTime) / 1000;
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
     * @param date e.g 20160427
     * @return
     */
    public static String getMonthByDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat(C.yyyyMMdd, Locale.ENGLISH);
        try {
            long time = format.parse(date).getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            if (Locale.getDefault().getCountry().equals(Locale.CHINA.getCountry())) {
                SimpleDateFormat chinaMonthFormat = new SimpleDateFormat("M月", Locale.CHINA);
                return chinaMonthFormat.format(cal.getTime());
            } else {
                SimpleDateFormat englishMonthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
                return englishMonthFormat.format(cal.getTime());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param timestamp
     * @return return M月d日 when in china, other return MM/dd
     */
    public static String getFormatDate(long timestamp) {
        if (Locale.getDefault().getCountry().equals(Locale.CHINA.getCountry())) {
            SimpleDateFormat chinaMonthFormat = new SimpleDateFormat("M月d日", Locale.CHINA);
            return chinaMonthFormat.format(timestamp * 1000);
        } else {
            SimpleDateFormat englishMonthFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
            return englishMonthFormat.format(timestamp * 1000);
        }
    }

    public static String getFormatDateEnglish(long timestamp) {
        SimpleDateFormat englishMonthFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        return englishMonthFormat.format(timestamp * 1000);
    }

    public static String getFormatDateToDay(long timestamp) {
        SimpleDateFormat englishMonthFormat = new SimpleDateFormat("dd", Locale.ENGLISH);
        return englishMonthFormat.format(timestamp * 1000);
    }

    public static String getFormatDateToHour(long timestamp) {
        SimpleDateFormat englishMonthFormat = new SimpleDateFormat("HH", Locale.ENGLISH);
        return englishMonthFormat.format(timestamp * 1000);
    }

    /**
     * @param timestamp
     * @return return yyyy年 when in china, other return MM/dd
     */
    public static String getFormatYear(long timestamp) {
        if (Locale.getDefault().getCountry().equals(Locale.CHINA.getCountry())) {
            SimpleDateFormat chinaMonthFormat = new SimpleDateFormat("yyyy年", Locale.CHINA);
            return chinaMonthFormat.format(timestamp);
        } else {
            SimpleDateFormat englishMonthFormat = new SimpleDateFormat("yyyy/", Locale.ENGLISH);
            return englishMonthFormat.format(timestamp);
        }
    }

    /**
     * @param pattern format time pattern.
     * @param minutes the minutes run in one day.
     * @return
     */
    public static String getTimeByMinute(String pattern, int minutes) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(DateUtils.getToday() * 1000 + minutes * 60 * 1000);
        return formatter.format(mCalendar.getTime());
    }


    public static long dateStr2MillionSeconds(String date) {
        String pattern = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
        return getMillionSeconds(pattern, date);
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

    /**
     * get today noon timeStamp
     *
     * @return
     */
    public static long getTodayNoon() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 将当天的时、分、秒、毫秒清0，从而获得年月日的Long型值
        mCalendar.set(Calendar.HOUR_OF_DAY, 12);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTimeInMillis();
    }

    /**
     * get Greenwich Mean Time of current day 0:00
     *
     * @return
     */
    public static long getGMTzero() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //设置原子钟协调世界时（UTC）
        TimeZone utc = TimeZone.getTimeZone("UTC");
        mCalendar.setTimeZone(utc);
        // 将当天的时、分、秒、毫秒清0，从而获得年月日的Long型值
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTimeInMillis();
    }

    public static int getCurrentMinutes() {
        Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);
        minute += hour * 60;
        return minute;
    }

    public static int getCurrentHour() {
        Calendar calender = Calendar.getInstance();
        return calender.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentSeconds() {
        Calendar calender = Calendar.getInstance();
        return calender.get(Calendar.SECOND);
    }

    public static int getYearByMilliseconds(long milliseconds) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(milliseconds);
        return mCalendar.get(Calendar.YEAR);
    }

    public static int getCurrentYear() {
        return getYearByMilliseconds(System.currentTimeMillis());
    }

    public static String getMessageTime(long timeStamp) {
        long todayZero = getToday();
        long yesterdayZero = todayZero - 24 * 60 * 60 * 1000;
        // get current week
        List<Date> weeks = getWeekOfDateBySeconds(System.currentTimeMillis());
        // get first day of current week
        long time = weeks.get(0).getTime();

        if (timeStamp >= todayZero) {
            return getDateByMillisecondsLoacale("HH:mm", timeStamp);
        } else if (timeStamp >= yesterdayZero) {
            return App.getContext().getResources().getString(R.string.yesterday);
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

    public static long getSeconds(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static boolean isDateValid(long date) {
        return date > 0 && date < System.currentTimeMillis();
    }

    /**
     * @param date format is yyyy-mm-dd
     * @return array, first item is year, second is month, third is day
     */
    public static String[] getDateArray(String date) {
        return date.split("-");
    }

    /**
     * @param t mm:s or mm:ss
     * @return mm:ss
     */
    public static String getTime(String t) {
        int index = t.indexOf(":");
        String newString = t.substring(index + 1, t.length());
        if (newString.length() == 1) {
            return t.replace(":" + newString, ":" + "0" + newString);
        } else {
            return t;
        }
    }

    public static long getStartTimeOfDay(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now * 1000);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static int getMinuteOfDay(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }
}
