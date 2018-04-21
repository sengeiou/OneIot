package com.coband.cocoband.tools;

import android.os.Environment;

import com.coband.cocoband.mvp.model.entity.DaySleepInfo;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.interactivelayer.bean.SleepItemPacket;
import com.coband.interactivelayer.bean.SportItemPacket;
import com.coband.watchassistant.SleepNodeDetail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by ivan on 17-8-28.
 */

public class DataHandler {

    private static final String TAG = "DataHandler";


    public static void recordSleepData(long date, SleepItemPacket packet) {
        File file = new File(Environment.getExternalStorageDirectory() + "/iMCO_sleep_data.txt");
        if (!file.exists()) {
            try {
                boolean createFileStatus = file.createNewFile();
                Logger.d(TAG, "create file status >>>>>> " + createFileStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String s;

        if (date <= 0) {
            s = "date error >>>>>> " + date;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.US);
            s = format.format(date);
        }

        int minutes = packet.getMinutes();
        String time = (minutes - minutes % 60) / 60 + "时" + minutes % 60 + "分";

        String mode = "";
        switch (packet.getMode()) {
            case Config.K9_AWAKE:
                mode = "清醒";
                break;
            case Config.K9_DEEP:
                mode = "深睡";
                break;
            case Config.K9_LIGHT:
                mode = "浅睡";
                break;
        }

        s = "date---->" + s + " time---->" + time + " mode----->" + mode + "\n";
        Writer writer = null;
        try {
            writer = new FileWriter(file.getAbsolutePath(), true);
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void recordSportData(long date, SportItemPacket packet) {
        File file = new File(Environment.getExternalStorageDirectory() + "/iMCO_sport_data.txt");
        if (!file.exists()) {
            try {
                boolean createFileStatus = file.createNewFile();
                Logger.d(TAG, "create file status >>>>>> " + createFileStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String s;

        if (date <= 0) {
            s = "date error >>>>>> " + date;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.US);
            s = format.format(date);
        }

        s = "date--->" + s + " offset--->" + packet.getOffset() + " step--->" + packet.getStepCount()
                + " calories--->" + packet.getCalory() + " distance--->" + packet.getDistance() + "\n";
        Writer writer = null;
        try {
            writer = new FileWriter(file.getAbsolutePath(), true);
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static List<SleepNodeDetail> filterSleepData(List<SleepNodeDetail> list) {
        log(list, 0);
        list = isSleepNodeMatchFirstCondition(list);
        log(list, 1);
        if (list.size() <= 1) {
            return list;
        }

        list = isSleepNodeMatchSecondCondition(list);
        log(list, 2);
        if (list.size() <= 1) {
            return list;
        }

        list = isSleepNodeMatchThirdCondition(list);
        log(list, 3);
        if (list.size() <= 1) {
            return list;
        }

        list = isSleepNodeMatchFourthCondition(list);
        log(list, 4);
        if (list.size() <= 1) {
            return list;
        }

        list = isSleepNodeMatchFifthCondition(list);
        log(list, 5);
        if (list.size() <= 1) {
            return list;
        }

        list = isSleepNodeMatchSixthCondition(list);
        log(list, 6);
        if (list.size() <= 1) {
            return list;
        }

        list = isSleepNodeMatchSeventhCondition(list);
        log(list, 7);
        if (list.size() <= 1) {
            return list;
        }
        list = isSleepNodeMatchEighthCondition(list);
        log(list, 8);

        if (list.size() <= 1) {
            return list;
        }
        list = isSleepNodeMatchNinthCondition(list);
        log(list, 9);

        return list;
    }

    private static void log(List<SleepNodeDetail> list, int conditionIndex) {
        Logger.d(TAG, "condition index >>>>>> " + conditionIndex);
        for (int i = 0; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i);
            Logger.d(TAG, "date >>>> " + detail.getDate() + " minute>>>>> " + detail.getBegin() +
                    " mode >>>>>> " + detail.getMode());
        }
    }


    private static int getSleepMode(SleepNodeDetail detail) {
        if (detail == null) {
            return -1;
        }

        switch (detail.getMode()) {
            case Config.K9_DEEP:
                return DaySleepInfo.DEEP;
            case Config.K9_LIGHT:
                return DaySleepInfo.LIGHT;
            case Config.K9_AWAKE:
                return DaySleepInfo.AWAKE;
        }

        return -1;
    }

    /**
     * 在01:00点前出现清醒时间，且清醒时间（整体在01：00之前）大于等于20min，则删除之前的睡眠数据。(考虑到早睡情况，这条待确认，考虑目前的测试群体)
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchFirstCondition(List<SleepNodeDetail> list) {
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);
            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

            if (mode == DaySleepInfo.AWAKE && duration >= 20 && (end < 60 || start >= 1080)) {
                return isSleepNodeMatchFirstCondition(list.subList(i, list.size()));
            }
        }

        return list;
    }


    /**
     * 早上05:00点之后（整体在05：00之后），出现清醒时间超过30min，则之后的数据全部清除
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchSecondCondition(List<SleepNodeDetail> list) {
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);
            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

            if (mode == DaySleepInfo.AWAKE && duration > 30 && start > 60 * 5) {
                return list.subList(0, i);
            }
        }

        return list;
    }


    /**
     * 5点之后最早的一次清醒，如果之后只有清醒和浅睡，则删除该清醒状态以后的睡眠数据
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchThirdCondition(List<SleepNodeDetail> list) {
        boolean firstAwakeFlagAfterFiveClock = false;
        int awakeFlagIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);

            if (start <= 5 * 60 || start >= 1080) {
                continue;
            }

            if (!firstAwakeFlagAfterFiveClock && mode == DaySleepInfo.AWAKE) {
                firstAwakeFlagAfterFiveClock = true;
                awakeFlagIndex = i;
                continue;
            }

            if (firstAwakeFlagAfterFiveClock && mode == DaySleepInfo.DEEP) {
                return list;
            }


        }
        if (firstAwakeFlagAfterFiveClock) {
            return list.subList(0, awakeFlagIndex);
        } else {
            return list;
        }
    }


    /**
     * 01:00点之前深睡超过1.5小时（整体在01：00之前），则之前的睡眠数据全部清除
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchFourthCondition(List<SleepNodeDetail> list) {
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);
            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

            if (mode == DaySleepInfo.DEEP && duration > 90 && (start < 1410 && start > 1080)) {
                return isSleepNodeMatchFourthCondition(list.subList(i, list.size()));
            }
        }

        return list;
    }

    /**
     * 凌晨5点之后（整体在05：00之后），深睡超过1.5小时，则之后的睡眠数据全部清除
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchFifthCondition(List<SleepNodeDetail> list) {
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);
            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

            if (mode == DaySleepInfo.DEEP && duration > 90 && start > 5 * 60) {
                return list.subList(0, i);
            }
        }

        return list;
    }

    /**
     * 凌晨1点和5点之间(可包含1点或者5点)，深睡超过2个小时，所有睡眠数据清除
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchSixthCondition(List<SleepNodeDetail> list) {
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);
            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

            if (mode == DaySleepInfo.DEEP && duration > 120 && start >= 60 && start <= 300) {
                list.clear();
                return list;
            }

            if (mode == DaySleepInfo.DEEP && (start >= 1080 || start <= 60) && end >= 180 && end < 1080) {
                list.clear();
                return list;
            }
        }

        return list;
    }


    /**
     * 凌晨01：00点之前到早晨05：00点之后，为清醒状态，所有睡眠数据清除（核心区处于清醒状态）
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchSeventhCondition(List<SleepNodeDetail> list) {
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int end = nextDetail.getBegin();
            int start = detail.getBegin();
            int mode = getSleepMode(detail);
            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

//            if (mode == DaySleepInfo.AWAKE && duration > 20 && end < 60) {
//                return list.subList(i, list.size() - 1);
//            }
        }

        return list;
    }

    /**
     * 一点之前最晚的一次清醒，如果之前只有清醒和浅睡，则删除该清醒状态以前的睡眠数据
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchEighthCondition(List<SleepNodeDetail> list) {
        int lastAwakeIndexBeforeOneClock = -1;
        boolean filterFlag = true;
        for (int i = 1; i < list.size(); i++) {
            SleepNodeDetail detail = list.get(i - 1);
            SleepNodeDetail nextDetail = list.get(i);

            int start = detail.getBegin();
            int mode = getSleepMode(detail);

            if (start > 60 && start < 1080) {
                continue;
            }

            if (mode == DaySleepInfo.AWAKE) {
                lastAwakeIndexBeforeOneClock = i;
            }

            if (mode == DaySleepInfo.DEEP) {
                filterFlag = false;
            }


        }

        if (filterFlag && lastAwakeIndexBeforeOneClock != -1) {
            return list.subList(lastAwakeIndexBeforeOneClock, list.size());
        } else {
            return list;
        }
    }


    /**
     * 如果只有一种睡眠状态，且时间大于４个小时，清除所有数据。
     *
     * @param list sleep node list.
     * @return the list after filter.
     */
    private static List<SleepNodeDetail> isSleepNodeMatchNinthCondition(List<SleepNodeDetail> list) {
        if (list.size() == 2) {
            SleepNodeDetail detail = list.get(0);
            SleepNodeDetail endDetail = list.get(1);
            int start = detail.getBegin();
            int end = endDetail.getBegin();

            int duration = end - start;
            if (duration < 0) {
                duration += 1440;
            }

            if (duration > 60 * 4) {
                list.clear();
            }
        }

        return list;

    }


    public static boolean isDirtyData(long date) {
        return date > DateUtils.getToday();
    }
}
