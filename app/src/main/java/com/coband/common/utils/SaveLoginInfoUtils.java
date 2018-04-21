package com.coband.common.utils;

import com.coband.App;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.watchassistant.R;

/**
 * Created by tgc on 17-4-27.
 */

public class SaveLoginInfoUtils {

    public static double getHeight(String str) {
        //小数点的索引
        if (DataManager.getInstance().getUnitSystem() == Config.METRIC) {
            int index = str.indexOf(".");
            String height = str.substring(0, index + 2);
            return Double.parseDouble(height);
        } else {
            String ftStr = str.substring(0, str.lastIndexOf(App.getContext().getString(R.string.foot)));
            String temp = App.getContext().getString(R.string.foot);
            String inchStr = str.substring(str.lastIndexOf(temp) + temp.length(), str.lastIndexOf(App.getContext().getString(R.string.inch_unit)));

            double inchDouble = Double.parseDouble(inchStr);
            double ftDouble = Double.parseDouble(ftStr);
            return ftDouble + inchDouble / 12;

        }

    }

    public static double getWeight(String str) {
        //小数点的索引
        int index = str.indexOf(".");
        String weight = str.substring(0, index + 2);
        return Double.parseDouble(weight);
    }
}
