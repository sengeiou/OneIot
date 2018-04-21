package com.coband.protocollayer.applicationlayer;

import com.coband.utils.LogUtils;
import com.coband.utils.StringByteTrans;

public class ApplicationLayerSleepItemPacket {
	private final static String TAG = "ApplicationLayerSleepItemPacket";

	// Parameters
	private int mMinutes;			// 16bits
	private int mMode;				// 4bits
	
	// Packet Length
	public final static int SLEEP_ITEM_LENGTH = 4;
	
	public boolean parseData(byte[] data) {
		LogUtils.d(TAG, "  ApplicationLayerSleepItemPacket : " + StringByteTrans.byte2HexStr(data));

		// check header length
		if(data.length < SLEEP_ITEM_LENGTH) {
			return false;
		}
		mMinutes = ((data[0] << 8) | (data[1] & 0xff)) & 0xffff;// here must be care shift operation of negative
		mMode = data[3] & 0x0f;// here must be care shift operation of negative
		return true;
	}

	public int getMinutes() {
		return mMinutes;
	}

	public int getMode() {
		return mMode;
	}
}
