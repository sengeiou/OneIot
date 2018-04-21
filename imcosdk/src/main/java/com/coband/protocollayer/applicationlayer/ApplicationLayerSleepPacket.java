package com.coband.protocollayer.applicationlayer;

import com.coband.utils.LogUtils;
import com.coband.utils.StringByteTrans;

import java.util.ArrayList;


public class ApplicationLayerSleepPacket {

    private static final String TAG = "ApplicationLayerSleepPacket";
    // Header
    private int mYear;            // 6bits
    private int mMonth;            // 4bits
    private int mDay;            // 5bits
    private int mItemCount;        // 8bits

    // Packet Length
    private final static int SLEEP_HEADER_LENGTH = 4;

    // Sport Item
    ArrayList<ApplicationLayerSleepItemPacket> mSleepItems = new ArrayList<ApplicationLayerSleepItemPacket>();

    public boolean parseData(byte[] data) {
//        LogUtils.d(TAG, "  ApplicationLayerSleepPacket before ^ : " + StringByteTrans.byte2HexStr(data));
//
//        for (int i = 0; i < data.length; i++) {
//            data[i] ^= 0xff;
//        }

        LogUtils.d(TAG, "  ApplicationLayerSleepPacket after ^ : " + StringByteTrans.byte2HexStr(data));

        // check header length
        if (data.length < SLEEP_HEADER_LENGTH) {
            return false;
        }
        mYear = (data[0] & 0x7e) >> 1;// here must be care shift operation of negative
        mMonth = ((data[0] & 0x01) << 3) | (data[1] >> 5) & 0x07;//here must be care shift operation of negative
        mDay = data[1] & 0x1f;//here must be care shift operation of negative
        mItemCount = data[3] & 0xff;
        // check the item length
        if ((data.length - SLEEP_HEADER_LENGTH) != mItemCount * ApplicationLayerSleepItemPacket.SLEEP_ITEM_LENGTH) {
            return false;
        }
        for (int i = 0; i < mItemCount; i++) {
            ApplicationLayerSleepItemPacket sleepItem = new ApplicationLayerSleepItemPacket();

            byte[] sleepItemData = new byte[ApplicationLayerSportItemPacket.SPORT_ITEM_LENGTH];
            System.arraycopy(data, SLEEP_HEADER_LENGTH + i * ApplicationLayerSleepItemPacket.SLEEP_ITEM_LENGTH,
                    sleepItemData, 0, ApplicationLayerSleepItemPacket.SLEEP_ITEM_LENGTH);
            LogUtils.d(TAG, "sleep item byte >>>>> " +StringByteTrans.byte2HexStr(sleepItemData));
            sleepItem.parseData(sleepItemData);
            mSleepItems.add(sleepItem);
        }
        return true;
    }

    public ArrayList<ApplicationLayerSleepItemPacket> getSleepItems() {
        return mSleepItems;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getItemCount() {
        return mItemCount;
    }
}
