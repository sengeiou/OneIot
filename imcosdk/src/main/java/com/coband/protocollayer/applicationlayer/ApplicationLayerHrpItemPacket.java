package com.coband.protocollayer.applicationlayer;

public class ApplicationLayerHrpItemPacket {

	// Parameters
	private int mMinutes;			// 16bits


	private int mSeconds;			// 8bits
	private int mValue;				// 16bits
	
	// Packet Length
	public final static int ITEM_LENGTH = 4;
	
	public boolean parseData(byte[] data) {
		// check header length
		if(data.length < ITEM_LENGTH) {
			return false;
		}
		mMinutes = ((data[0] << 8) | (data[1] & 0xff)) & 0xffff;// here must be care shift operation of negative
//		mValue = ((data[2] << 8) | (data[3] & 0xff)) & 0xffff;// here must be care shift operation of negative
		mSeconds = data[2];
		mValue = data[3];
		return true;
	}

	public int getMinutes() {
		return mMinutes;
	}

	public int getValue() {
		return mValue;
	}

	public int getSeconds() {
		return mSeconds;
	}
}
