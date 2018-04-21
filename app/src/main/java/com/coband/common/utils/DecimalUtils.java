package com.coband.common.utils;

/**
 * Created by tgc on 17-10-20.
 */

public class DecimalUtils {

    public static double retainDecimal(int index, double decimal) {
        double number = 1;
        if (index < 0) {
            return decimal;
        } else {
            number = number * 10 * index;
            number = number == 0 ? 1d : number;
            return Math.round(decimal * number) / number;
        }
    }
}
