package com.coband.interactivelayer.bean;

/**
 * Created by mai on 17-8-31.
 */

public class FactorySensorPacket {
    // Header
    private int mX;			// 16bits
    private int mY;			// 16bits
    private int mZ;			// 16bits

    public FactorySensorPacket(int mX, int mY, int mZ) {
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getZ() {
        return mZ;
    }
}
