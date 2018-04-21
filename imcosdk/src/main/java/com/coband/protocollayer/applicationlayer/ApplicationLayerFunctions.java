package com.coband.protocollayer.applicationlayer;

import com.coband.utils.LogUtils;

/**
 * Created by mai on 17-9-26.
 */

public class ApplicationLayerFunctions {

    private boolean fakeBP;
    private boolean realBP;
    private boolean portrait_landscape;
    private boolean heartRate;
    private boolean stepCounter;
    private boolean sleep;
    private boolean wechat;

    private final static int SENSOR_HEADER_LENGTH = 4;

    public boolean parseData(byte[] data) {
        LogUtils.d("ApplicationLayerFunctions", "parse functions data >>>>> " + String.valueOf(data));
        fakeBP = (data[3] & 0x01) != 0;
        realBP = (data[3] & 0x02) != 0;
        portrait_landscape = (data[3] & 0x04) != 0;
        heartRate = (data[3] & 0x08) != 0;
        stepCounter = (data[3] & 0x10) != 0;
        sleep = (data[3] & 0x20) != 0;
        wechat = (data[3] & 0x40) != 0;
        return true;
    }

    public boolean isWechat() {
        return wechat;
    }

    public boolean isFakeBP() {
        return fakeBP;
    }

    public boolean isRealBP() {
        return realBP;
    }

    public boolean isPortrait_landscape() {
        return portrait_landscape;
    }

    public boolean isHeartRate() {
        return heartRate;
    }

    public boolean isStepCounter() {
        return stepCounter;
    }

    public boolean isSleep() {
        return sleep;
    }
}
