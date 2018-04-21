package com.coband.common.utils;

import android.widget.TextView;

import com.coband.App;
import com.coband.watchassistant.R;

/**
 * Created by tgc on 17-5-19.
 */

public class GetTargetNumUtil {

    //获得含有单位的目标的值，从UI获取，因此睡眠目标的值需要float类型
    public static float getNum(TextView textView) {
        String str = textView.getText().toString();
        //获得睡眠（小时）的判断
        if (str.contains(App.getContext().getString(R.string.hour))) {
            int i = str.lastIndexOf(App.getContext().getString(R.string.hour));
            return Float.parseFloat(str.substring(0, i));
        }
        //获得步数（步）的判断
        else if (str.contains(App.getContext().getString(R.string.step_unit))) {
            int i = str.lastIndexOf(App.getContext().getString(R.string.step_unit));
            return Integer.parseInt(str.substring(0, i));
        }
        return -1;
    }
}
