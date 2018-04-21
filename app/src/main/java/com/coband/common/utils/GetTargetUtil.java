package com.coband.common.utils;

import com.coband.cocoband.mvp.model.DataManager;

/**
 * Created by tgc on 17-5-19.
 */

public class GetTargetUtil {

    public static int getWalkTargetFromDB() {
        Integer stepTarget = DataManager.getInstance().getCurrentAccount().getStepTarget();
        if (stepTarget == null) {
            return Config.DEFAULT_STEP_TARGET;
        } else {
            return stepTarget;
        }
    }

    public static int getSleepTargetFromDB() {
        Integer sleepTarget = DataManager.getInstance().getCurrentAccount().getSleepTarget();
        if (sleepTarget == null) {
            return Config.DEFAULT_SLEEP_TARGET / 60;
        } else {
            return sleepTarget;
        }
    }

    public static double getWeightTargetFromDB() {
        Double weightTarget = DataManager.getInstance().getCurrentAccount().getWeightTarget();
        if (weightTarget == null) {
            return Config.DEFAULT_WEIGHT_TARGET_METRIC;
        } else {
            return weightTarget;
        }
    }
}
