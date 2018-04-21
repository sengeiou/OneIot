package com.coband.protocollayer.applicationlayer;

import android.util.Log;

import com.coband.utils.LogUtils;
import com.coband.utils.StringByteTrans;

public class ApplicationLayerSportItemPacket {
    private final static String TAG = "ApplicationLayerSportItemPacket";

    // Parameters
    private int mOffset;            // 11bits
    private int mMode;                // 2bits
    private int mStepCount;            // 12bits
    private int mActiveTime;        // 4bits
    private int mCalory;            // 19bits
    private int mDistance;            // 16bits

    // Packet Length
    public final static int SPORT_ITEM_LENGTH = 8;
    public final static int SPORT_ITEM_LENGTH_12BIT = 12;

    public boolean parseData(byte[] data) {
        LogUtils.d(TAG, "  ApplicationLayerSportItemPacket : " + StringByteTrans.byte2HexStr(data));

        //fixme	: Step number of data composition has not yet determined, this is a compatible
        if (data.length == SPORT_ITEM_LENGTH_12BIT) {

            mOffset = (data[0] & 0xfe) >> 1;
            mMode = ((data[0] & 0x01) << 1) | ((data[1] >> 7) & 0x01);
            mActiveTime = (data[1] >> 3) & 0x0f;
            mStepCount = ((data[1] & 0x07) << 16) | ((data[2] << 8) & 0xff00) | (data[3] & 0xff);
            mCalory = ((data[4] << 24) & 0xff000000) | ((data[5] << 16) & 0x00ff0000) | ((data[6] << 8) & 0x0000ff00) | (data[7] & 0x000000ff);
            mDistance = ((data[8] << 24) & 0xff000000) | ((data[9] << 16) & 0x00ff0000) | ((data[10] << 8) & 0x0000ff00) | (data[11] & 0x000000ff);

            Log.e("SPORT_ITEM_LENGTH", "mOffset: " + mOffset +
                    ", mMode:" + mMode +
                    ", mStepCount:" + mStepCount +
                    ", mActiveTime:" + mActiveTime +
                    ", mCalory:" + mCalory +
                    ", mDistance:" + mDistance);
            return true;

        } else if (data.length == SPORT_ITEM_LENGTH) {

            mOffset = ((data[0] & 0xff) << 3) | ((data[1] >> 5) & 0x07);// here must be care shift operation of negative
            mMode = (data[1] >> 3) & 0x03;// here must be care shift operation of negative
            mStepCount = ((data[1] & 0x07) << 9) | (data[2] << 1) & 0x1fe | ((data[3] >> 7) & 0x01);// here must be care shift operation of negative
            mActiveTime = (data[3] >> 3) & 0x0f;// here must be care shift operation of negative
            mCalory = ((data[3] & 0x07) << 16) | (data[4] << 8) & 0xff00 | (data[5] & 0xff);// here must be care shift operation of negative
            mDistance = ((data[6] << 8) | (data[7] & 0xff)) & 0xffff;// here must be care shift operation of negative
            Log.e("SPORT_ITEM_LENGTH_12BIT", "mOffset: " + mOffset +
                    ", mMode:" + mMode +
                    ", mStepCount:" + mStepCount +
                    ", mActiveTime:" + mActiveTime +
                    ", mCalory:" + mCalory +
                    ", mDistance:" + mDistance);
            return true;
        }
        return false;
    }

    public int getOffset() {
        return mOffset;
    }

    public int getMode() {
        return mMode;
    }

    public int getStepCount() {
        return mStepCount;
    }

    public int getActiveTime() {
        return mActiveTime;
    }

    public int getCalory() {
        return mCalory;
    }

    public int getDistance() {
        return mDistance;
    }
}
