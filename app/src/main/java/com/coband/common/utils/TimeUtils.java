package com.coband.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tgc on 17-4-27.
 */

public class TimeUtils {

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long i = Long.parseLong(time);
        String times = sdr.format(new Date(i));
        return times;
    }

    public static Date getDateFormat(String dateStr) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = sdr.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //传入获得的xxxx年xx月xx日，返回可以转换成long类型的数据，用于存入数据库
    public static long getData(String time) {
        String[] dateArray = time.split("/");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        calendar.set(calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
        return calendar.getTimeInMillis();
    }

//    public static String getData(String time) {
//        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
//        Date date;
//        String times = null;
//        try {
//            date = sdr.parse(time);
//            long l = date.getTime();
//            times = String.valueOf(l);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return times;
//    }
}
