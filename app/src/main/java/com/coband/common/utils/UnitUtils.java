package com.coband.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgc on 17-5-9.
 */

public class UnitUtils {

    //厘米转xx英尺xx英寸
    public static List<Integer> cmToInch(float cmFloat) {
        float inchFloat = cmFloat * 0.3937008f;//得到英寸
        float ftFloat = inchFloat * 0.0833333f;//得到英尺
        float inchFloatFromFt = ftFloat * 12;//将得到的英尺换算成英寸
        int inchInteger = (int) Math.rint(inchFloatFromFt);//得到英寸整数
        int ftFinal = inchInteger / 12;
        int inchFinal = inchInteger % 12;
        List<Integer> list = new ArrayList<>();
        list.add(ftFinal);
        list.add(inchFinal);
        return list;
    }

    //xx英尺转xx英尺xx英寸
    public static List<Integer> inchToFtInch(double ftInch) {
        ftInch = ftInch * 12;
        int ft = (int) ftInch / 12;
        int inch = (int) (ftInch - ft * 12);
        List<Integer> list = new ArrayList<>();
        list.add(ft);
        list.add(inch);
        return list;
    }
}
