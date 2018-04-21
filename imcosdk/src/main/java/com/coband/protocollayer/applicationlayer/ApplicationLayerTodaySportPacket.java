package com.coband.protocollayer.applicationlayer;

import android.util.Log;

public class ApplicationLayerTodaySportPacket {

    // Parameters
    private long mStepTarget;            // 4byte
    private long mDistance;                // 4byte
    private long mCalorie;                // 4byte

    // Packet Length
    private final static int TODAY_SPORT_HEADER_LENGTH = 12;

    public ApplicationLayerTodaySportPacket(long step, long distance, long calorie) {
        mStepTarget = step;
        mDistance = distance;
        mCalorie = calorie;
    }

    public byte[] getPacket() {
        Log.d("ApplicationLayer", "mStepTarget: " + mStepTarget
                + ", mDistance: " + mDistance
                + ", mCalory: " + mCalorie);

        byte[] data = new byte[TODAY_SPORT_HEADER_LENGTH];
        data[0] = (byte) ((mStepTarget >> 24) & 0xff);
        data[1] = (byte) ((mStepTarget >> 16) & 0xff);
        data[2] = (byte) ((mStepTarget >> 8) & 0xff);
        data[3] = (byte) (mStepTarget & 0xff);
        data[4] = (byte) ((mDistance >> 24) & 0xff);
        data[5] = (byte) ((mDistance >> 16) & 0xff);
        data[6] = (byte) ((mDistance >> 8) & 0xff);
        data[7] = (byte) (mDistance & 0xff);
        data[8] = (byte) ((mCalorie >> 24) & 0xff);
        data[9] = (byte) ((mCalorie >> 16) & 0xff);
        data[10] = (byte) ((mCalorie >> 8) & 0xff);
        data[11] = (byte) (mCalorie & 0xff);
        return data;
    }
}
