package com.coband.protocollayer.applicationlayer;

/**
 * Created by mai on 17-9-15.
 */

public class ApplicationLayerBPItemPacket {

    private long timeStamp;
    private int second;
    private int heartrate;
    private int bpLow;
    private int bpHigh;

    public final static int ITEM_LENGTH= 8;
    public boolean parseData(byte[] data) {
        if (data.length < ITEM_LENGTH) {
            return false;
        }
        bpHigh = (data[7] & 0xff);

        bpLow = (data[6] & 0xff);

        heartrate = (data[5] & 0xff);

        second = (data[4] & 0xff);
        
        timeStamp = (((data[2] << 8) & 0xff00) | (data[3] & 0xff));
        return true;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public int getBpLow() {
        return bpLow;
    }

    public void setBpLow(int bpLow) {
        this.bpLow = bpLow;
    }

    public int getBpHigh() {
        return bpHigh;
    }

    public void setBpHigh(int bpHigh) {
        this.bpHigh = bpHigh;
    }
}
