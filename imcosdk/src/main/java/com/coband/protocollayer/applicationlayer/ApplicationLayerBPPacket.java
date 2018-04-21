package com.coband.protocollayer.applicationlayer;

import java.util.ArrayList;

/**
 * Created by mai on 17-9-15.
 */

public class ApplicationLayerBPPacket {

    // Header
    private int mYear;			// 6bits
    private int mMonth;			// 4bits
    private int mDay;			// 5bits
    private int mItemCount;		// 8bits

    // Packet Length
    private final static int HRP_HEADER_LENGTH = 4;

    // Sport Item
    ArrayList<ApplicationLayerBPItemPacket> mBPItems = new ArrayList<>();

    public boolean parseData(byte[] data) {
        // check header length
        if(data.length < HRP_HEADER_LENGTH) {
            return false;
        }
        mYear = (data[0] & 0x7e) >> 1;// here must be care shift operation of negative
        mMonth = ((data[0] & 0x01) << 3)  | (data[1] >> 5) & 0x07;//here must be care shift operation of negative
        mDay = data[1] & 0x1f;//here must be care shift operation of negative
        mItemCount = data[3] & 0xff;
        // check the item length
        if((data.length - HRP_HEADER_LENGTH) != mItemCount * ApplicationLayerBPItemPacket.ITEM_LENGTH) {
            return false;
        }
        for(int i = 0; i < mItemCount; i ++) {
            ApplicationLayerBPItemPacket hrpItem = new ApplicationLayerBPItemPacket();

            byte[] hrpItemData = new byte[ApplicationLayerSportItemPacket.SPORT_ITEM_LENGTH];
            System.arraycopy(data, HRP_HEADER_LENGTH + i * ApplicationLayerBPItemPacket.ITEM_LENGTH,
                    hrpItemData, 0, ApplicationLayerBPItemPacket.ITEM_LENGTH);
            hrpItem.parseData(hrpItemData);
            mBPItems.add(hrpItem);
        }
        return true;
    }

    public ArrayList<ApplicationLayerBPItemPacket> getHrpItems() {
        return mBPItems;
    }

}
