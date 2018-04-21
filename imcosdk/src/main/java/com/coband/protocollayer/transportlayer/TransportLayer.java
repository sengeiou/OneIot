package com.coband.protocollayer.transportlayer;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.coband.protocollayer.gattlayer.GattLayer;
import com.coband.protocollayer.gattlayer.GattLayerCallback;
import com.coband.utils.LogUtils;
import com.coband.utils.StringByteTrans;

import java.util.ArrayList;


public class TransportLayer {
	// Log
	private final static String TAG = "TransportLayer";
	private final static boolean D = true;
	
	// state control
	private ArrayList<byte[]> mTxPacketList;

	private volatile Integer mTxState = TX_STATE_IDLE;
	private final static int TX_STATE_IDLE = 0;
	private final static int TX_STATE_IN_TX = 1;
	private final static int TX_STATE_IN_SEND_ACK = 2;

	private TransportLayerPacket mCurrentRxPacket;

	private ArrayList<TransportLayerPacket> mRxPacketList;
	private volatile Integer mRxState = RX_STATE_HEADER;
	private final static int RX_STATE_HEADER = 0;
	private final static int RX_STATE_DATA = 1;
	
	// use to manager current transport layer sequence
	private volatile int mCurrentTxSequenceId;
	private volatile int mLastRxSuquenceId;
	
	// use to manager packet send
	private volatile boolean isAckCome;
	private volatile boolean isSentAckRight;
	private final Object mAckLock = new Object();
	
	// retransmit control.
	private int mRetransCounter;
	private final static int MAX_RETRANSPORT_COUNT = 3;
	
	// Thread for unpack send
	private ThreadTx mThreadTx;
	private ThreadRx mThreadRx;
	private static int MTU_PAYLOAD_SIZE_LIMIT = 20;
	
	// Use to manager data send
	private boolean isDataSend;
	private final Object mSendDataLock = new Object();
	private final int MAX_DATA_SEND_WAIT_TIME = 10000;
	
	// Transport Layer Call
	private TransportLayerCallback mCallback;
	
	// Gatt Layer
	private GattLayer mGattLayer;

	
	public TransportLayer(Context context, TransportLayerCallback callback) {
		if(D) Log.d(TAG, "init");
		// register callback
		mCallback = callback;
				
		// init receive buffer
		mTxPacketList = new ArrayList<>();
		mRxPacketList = new ArrayList<>();

		mCurrentRxPacket = new TransportLayerPacket();
		
		// init the gatt layer
		mGattLayer = new GattLayer(context, mGattCallback);
	}

	/**
	 * connect to the remote device.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link TransportLayerCallback#onConnectionStateChange} callback is invoked, reporting the result of the operation.
	 *
	 * @return the operation result
	 *
	 * */
	public boolean connect(String addr) {
		mCurrentTxSequenceId = 1;
		mRetransCounter = 0;
		mLastRxSuquenceId = -1;

		// init state
		initialState();

		startTxSchedule();
		startRxSchedule();

		return mGattLayer.connect(addr);
	}

	public boolean isConnected() {
		return mGattLayer.isConnected();
	}


	private void initialState() {
		initialTxState();
		initialRxState();
	}

	private void initialTxState() {
		synchronized (mTxState) {
			mTxState = TX_STATE_IDLE;
		}
	}

	private boolean checkTxStateInTx() {
		boolean status = false;
		synchronized (mTxState) {
			status = (mTxState != TX_STATE_IDLE);
		}
		return status;
	}

	private void changeToTxDataState() {
		synchronized (mTxState) {
			mTxState = TX_STATE_IN_TX;
		}
	}

	private void changeToTxAckState() {
		synchronized (mTxState) {
			mTxState = TX_STATE_IN_SEND_ACK;
		}
	}


	private void initialRxState() {
		synchronized (mRxState) {
			mRxState = RX_STATE_HEADER;
		}
	}

	private boolean checkRxStateInReceiveHeaderMode() {
		boolean status = false;
		synchronized (mRxState) {
			status = mRxState == RX_STATE_HEADER;
		}
		return status;
	}

	private void changeToRxDataState() {
		synchronized (mRxState) {
			mRxState = RX_STATE_DATA;
		}
	}

	private void addToTxPacketList(byte[] packet) {
		synchronized (mTxPacketList) {
			mTxPacketList.add(packet);
			mTxPacketList.notifyAll();
		}
	}

	private byte[] getFromTxPacketList() {
		byte[] packet = null;
		synchronized (mTxPacketList) {
			if(mTxPacketList.size() > 0) {
				packet = mTxPacketList.remove(0);
			}else{
				try {
					mTxPacketList.wait(1*1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}

		return packet;
	}

	private int getTxPacketListSize() {
		synchronized (mTxPacketList) {
			return mTxPacketList.size();
		}
	}

	private void addToRxPacketList(TransportLayerPacket packet) {
		synchronized (mRxPacketList) {
			mRxPacketList.add(packet);
			mRxPacketList.notifyAll();
		}
	}

	private TransportLayerPacket getFromRxPacketList() {
		TransportLayerPacket packet = null;
		synchronized (mRxPacketList) {
			if(mRxPacketList.size() > 0) {
				packet = mRxPacketList.remove(0);
			} else {
				try {
					mRxPacketList.wait(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return packet;
	}
	/**
	 * Close, it will disconnect to the remote.
	 *
	 *
	 * */
	public void close() {
		if(D) Log.d(TAG, "close()");
		// clear all the wait time.
		stopRxSchedule();
		stopTxSchedule();

		stopRxTimer();
	}

	/**
	 * Disconnect, it will disconnect to the remote.
	 *
	 *
	 * */
	public void disconnect() {
		// clear all the wait time.
		stopRxSchedule();
		stopTxSchedule();
		stopRxTimer();
		mGattLayer.disconnectGatt();
	}

	public void closeGatt(){
		mGattLayer.closeGatt();
	}

	/**
	 * Set the name
	 *
	 * @param name 		the name
	 */
	public void setDeviceName(String name) {
		if(D) Log.d(TAG, "set name, name: " + name);
		mGattLayer.setDeviceName(name);
	}
	/**
	 * Get the name
	 *
	 */
	public void getDeviceName() {
		if(D) Log.d(TAG, "getDeviceName");
		mGattLayer.getDeviceName();
	}


	/**
	 * When the Low Layer receive a packet, it will call this method
	 * 
	 * @param data the receive data
	 * */
	public void receiveData(byte[] data) {
		decodeReceiveData(data);
	}
	
	/**
	 * Send Data packet to the remote. Up stack can use this method to send data.
	 * If last packet didn't send ok, didn't allow send next packet. 
	 * 
	 * @param data the send data
	 * 
	 * */
	public boolean sendData(byte[] data){

		// generate a data packet
		byte[] sendPacket = TransportLayerPacket.prepareDataPacket(data, mCurrentTxSequenceId);

		// Pending to tx list.
		addToTxPacketList(sendPacket);
		
		return true;
	}

	/**
	 * In callback, maybe it will do lot of thing, we make let it work in a thread.
	 * */
	private void tellUpstackPacketSend(final byte[] sendData, final boolean sendOK) {
		if(TransportLayerPacket.checkIsAckPacket(sendData)) {
			if(D) Log.d(TAG, "tellUpstackPacketSend, is ack packet, don't need tell up stack.");
			return;
		}
		if(D) Log.d(TAG, "tellUpstackPacketSend, sendOK: " + sendOK + ", sendData: " + StringByteTrans.byte2HexStr(sendData));

		// update tx sequence id
		mCurrentTxSequenceId ++;

		final byte[] appData = new byte[sendData.length - TransportLayerPacket.HEADER_LENGTH];
		System.arraycopy(sendData, TransportLayerPacket.HEADER_LENGTH, appData, 0, sendData.length - TransportLayerPacket.HEADER_LENGTH);

		mCallback.onDataSend(sendOK, appData);
	}
	
	/**
	 * Save the receive data to receive buffer. when state is normal or rx.
	 * 
	 * @param data receive data
	 * 
	 * */
	private void decodeReceiveData(byte[] data) {
		int result;
		// start rx timer
		startRxTimer();

		// Check parse header or data
		if(checkRxStateInReceiveHeaderMode()) {
			// change the state
			changeToRxDataState();
			if(D) Log.d(TAG, "parse header.");
			// parse the header
			result = mCurrentRxPacket.parseHeader(data);
		} else {
			if(D) Log.d(TAG, "parse data.");
			// parse the header
			result = mCurrentRxPacket.parseData(data);
		}

		// stop rx timer
		if(result != TransportLayerPacket.LT_SUCCESS) {
			stopRxTimer();
		}
		switch(result) {
			// Receive ACK
			case TransportLayerPacket.LT_ERROR_ACK:
				isSentAckRight = false;
			case TransportLayerPacket.LT_SUCCESS_ACK:
				isSentAckRight = true;
				// update state
				initialRxState();
				// Update tx status
				synchronized (mAckLock) {
					isAckCome = true;
					if(D) Log.i(TAG, "<<<--- Receive ack, ack flag: " + isSentAckRight);
					mAckLock.notifyAll();
				}
				break;
			case TransportLayerPacket.LT_FULL_PACKET:
				// init Rx state
				initialRxState();
				if(D) Log.i(TAG, "<<<--- Receive a full packet, packet real payload: " + StringByteTrans.byte2HexStr(mCurrentRxPacket.getRealPayload()));

				LogUtils.d(TAG, "mCurrentRxPacket.getSequenceId() : " + mCurrentRxPacket.getSequenceId());
				LogUtils.d(TAG, "mLastRxSuquenceId : " + mLastRxSuquenceId);
				// check whether a retransmit packet
				if(mCurrentRxPacket.getSequenceId() == mLastRxSuquenceId) {
					if(D) Log.w(TAG, "<<<--- Maybe a retrans packet, send success ack");
					// send success ack to remote
					sendAckPacket(mCurrentRxPacket.getSequenceId(), false);
					return;
				}
				// update the last sequence id
				mLastRxSuquenceId = mCurrentRxPacket.getSequenceId();

				TransportLayerPacket packet = mCurrentRxPacket;

				// Need create a new packet
				mCurrentRxPacket = new TransportLayerPacket();

				// send success ack to remote
				sendAckPacket(packet.getSequenceId(), false);

				// Add the packet to rx list
				addToRxPacketList(packet);

				break;
			case TransportLayerPacket.LT_SUCCESS:
				// Only check in the end of a packet
				/*
				// check whether a retransmit packet
				if(mPacket.getSequenceId() == mLastRxSuquenceId) {
					if(D) Log.d(TAG, "Receive a retransmit packet, mPacket.getSequenceId(): " + mPacket.getSequenceId() +
												", mLastRxSuquenceId: " + mLastRxSuquenceId);
					// update state
					mState = STATE_NORMAL;//State change must before ack send.
					// send success ack to remote
					sendAckPacketUseThread(false);
					return;
				}*/
				break;

			case TransportLayerPacket.LT_LENGTH_ERROR:
			case TransportLayerPacket.LT_CRC_ERROR:
				if(D) Log.e(TAG, "<<<--- Some error when receive data, with result: " + result);
				// update state
				initialRxState();

				sendAckPacket(mCurrentRxPacket.getSequenceId(), true);
				break;
			case TransportLayerPacket.LT_MAGIC_ERROR:
				// if a magic error occur, just return
				if(D) Log.e(TAG, "<<<--- Some error when receive data, with result: " + result);
				// update state
				initialRxState();
				break;
			default:
				if(D) Log.e(TAG, "<<<--- Some error, with result: " + result);
				break;
		}
	}

	/**
	 * Send ACK packet to the remote.
	 *
	 * @param err error ack or a success ack
	 *
	 * */
	/*
	private void sendAckPacket(boolean err){
		if(D) Log.e(TAG, "sendAckPacket, err: " + err);
		// generate a ack packet
		final byte[] sendByte = TransportLayerPacket.prepareAckPacket(err, mCurrentRxPacket.getSequenceId());

		if(sendByte == null) {
			if(D) Log.e(TAG, "something error with null packet.");
			return;
		}

		addToTxPacketList(sendByte);
	}*/

	/**
	 * Send ACK packet to the remote.
	 *
	 * @param err error ack or a success ack
	 *
	 * */
	private void sendAckPacket(int id, boolean err) {
		// generate a ack packet
		final byte[] sendByte = TransportLayerPacket.prepareAckPacket(err, id);

		if(sendByte == null) {
			if(D) Log.e(TAG, "--->>> Something error in send ack packet, with null packet.");
			return;
		}

		//send it
		if(D) Log.i(TAG, "sendAckPacket, pending to tx list, err: " + err + ", sendByte: " + StringByteTrans.byte2HexStr(sendByte));

		addToTxPacketList(sendByte);

		// protect tx ack packet
		changeToTxAckState();
		// send the data, here we do nothing while the data send error, we just think this operation will be done
		//sendGattLayerData(sendByte);
    }
	
	/**
	 * Send transport packet to gatt layer.
	 * <p>This is an synchronous operation. It will wait the {@link GattLayerCallback#onDataSend} callback is invoked.
	 * 
	 * @param data the send data
	 * 
	 * */
    private boolean sendGattLayerData(byte[] data) {
    	isDataSend = false;
		if(!mGattLayer.sendData(data)) {
			if(D) Log.e(TAG, "sendGattLayerData error.");
			return false;
		}
		
		synchronized(mSendDataLock) {
			if(isDataSend != true) {
				try {
					mSendDataLock.wait(MAX_DATA_SEND_WAIT_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isDataSend;
    }


	public void startRxSchedule() {
		if(D) Log.d(TAG, "startRxSchedule.");
		if(mThreadRx != null) {
			mThreadRx.StopRx();
		}

		mThreadRx = new ThreadRx();
		mThreadRx.start();
	}

	public void stopRxSchedule() {
		if(D) Log.d(TAG, "stopRxSchedule.");
		if(mThreadRx != null) {
			mThreadRx.StopRx();
		}
	}

	public class ThreadRx extends Thread {

		private Boolean _stop = false;

		public void run() {
			if(D) Log.d(TAG, "ThreadRx is run");

			TransportLayerPacket receiveData = null;

			while (true) {
				// Use to protect tx packet
				if(!checkTxStateInTx()) {
					if ((receiveData = getFromRxPacketList()) != null) {
						// tell up stack, send the packet to upstack
						int len = receiveData.getPayloadLength();
						byte[] rcv = new byte[len];
						if(len != 0) {
							System.arraycopy(receiveData.getRealPayload(), 0, rcv, 0, len);
						}
						// tell up stack
						//if(D) Log.e(TAG, "tell up stack, receive full packet");

						mCallback.onDataReceive(rcv);
					}
				}

				synchronized (_stop) {
					if (_stop) {
						break;
					}
				}
			}

			if(D) Log.d(TAG, "ThreadRx stop");
		}//run

		public void StopRx() {
			synchronized (_stop) {
				_stop = true;
			}
		}
	}


	public void startTxSchedule() {
		if(D) Log.d(TAG, "startTxSchedule.");
		if(mThreadTx != null) {
			mThreadTx.StopTx();
		}

		mThreadTx = new ThreadTx();
		mThreadTx.start();
	}

	public void stopTxSchedule() {
		if(D) Log.d(TAG, "stopTxSchedule.");
		if(mThreadTx != null) {
			mThreadTx.StopTx();
			synchronized(mAckLock) {
				isAckCome = false;
				isSentAckRight = false;
				mAckLock.notifyAll();
			}
		}
	}
	
	// unpack and send thread
    public class ThreadTx extends Thread {

		private Boolean _stop = false;

    	public void run() {
    		if(D) Log.d(TAG, "ThreadTx is run");

			byte[] sendData = null;

			while (true) {

				if((sendData = getFromTxPacketList()) != null) {
					// Use to protect tx packet
					changeToTxDataState();

					mRetransCounter = 0;
					boolean packetSendStatus = false;
					packetSendStatus = UnpackSendPacket(sendData);
					// If the packet list have packet, just send it
					if(!packetSendStatus) {
						// check reach the max retrans time
						while(mRetransCounter < MAX_RETRANSPORT_COUNT) {
							synchronized (_stop) {
								if(_stop) {
									return;
								}
							}
							mRetransCounter++;

							if(D) Log.w(TAG, "---> Retrans send it, mRetransCounter: " + mRetransCounter
									+ ", sendData: " + StringByteTrans.byte2HexStr(sendData)
									+ ", isAckCome: " + isAckCome
									+ ", isSentAckRight: " + isSentAckRight);
							// resend data
							packetSendStatus = UnpackSendPacket(sendData);

							if(packetSendStatus) {
								break;
							}
						}
					}

					// tell up stack.
					tellUpstackPacketSend(sendData, packetSendStatus);

					// Use to protect tx packet
					// Maybe need send ack.
					if(getTxPacketListSize() == 0) {
						initialTxState();
					}
				}
				synchronized (_stop) {
					if(_stop) {
						break;
					}
				}
			}

    		if(D) Log.d(TAG, "ThreadTx stop");
    	}//run

		public void StopTx() {
			synchronized (_stop) {
				_stop = true;
			}
		}
    }

	private boolean UnpackSendPacket(byte[] data) {
		byte[] sendData = data;

		// send data to the remote device
		if(null != mGattLayer) {
			if(D) Log.i(TAG, "---> Send data to remote, data: " + StringByteTrans.byte2HexStr(sendData));

			// init is ack come flag.
			isSentAckRight = false;
			isAckCome = false;
			// unpack the send data, because of the MTU size is limit
			int length = sendData.length;
			int unpackCount = 0;
			byte[] realSendData;
			do {

				if(length <= MTU_PAYLOAD_SIZE_LIMIT) {
					realSendData = new byte[length];
					System.arraycopy(sendData, unpackCount * MTU_PAYLOAD_SIZE_LIMIT, realSendData, 0, length);

					// update length value
					length = 0;
				} else {
					realSendData = new byte[MTU_PAYLOAD_SIZE_LIMIT];
					System.arraycopy(sendData, unpackCount * MTU_PAYLOAD_SIZE_LIMIT, realSendData, 0, MTU_PAYLOAD_SIZE_LIMIT);

					// update length value
					length = length - MTU_PAYLOAD_SIZE_LIMIT;
				}
				// send the data, here we do nothing while the data send error, we just think this operation will be done
				if(!sendGattLayerData(realSendData)) {
					if(D) Log.e(TAG, "---> Send data error, may link is loss or gatt init failed.");
					return false;
				}

				// unpack counter increase
				unpackCount++;

			} while(length != 0);
			// Check is a ACK packet or not
			if(TransportLayerPacket.checkIsAckPacket(sendData)) {
				return true;
			}
			// make sure ack be changed in receive ack
			synchronized(mAckLock) {
				if (isAckCome != true) {
					// start wait ack timer
					try {
						mAckLock.wait(MAX_ACK_WAIT_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					return isSentAckRight;
				}
			}
		} else {//if(null != mGattLayer)
			return false;
		}

		return true;
	}


    // Ack super timer
    private final int MAX_ACK_WAIT_TIME = 5000;


    // Rx super timer
    private final int MAX_RX_WAIT_TIME = 30000;
	final Handler mRxHandler = new Handler();
	Runnable mRxSuperTask = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(D) Log.w(TAG, "Rx Packet Timeout");
			// update state
			initialRxState();

			// send error ack to remote
			sendAckPacket(mCurrentRxPacket.getSequenceId(), true);
			// stop timer
			stopRxTimer();
		}
	};
	private void startRxTimer(){
		if(D) Log.d(TAG, "startRxTimer()");
		synchronized (mRxHandler) {
			mRxHandler.postDelayed(mRxSuperTask, MAX_RX_WAIT_TIME);
			//if(D) Log.d(TAG, "mRxHandler.postDelayed");
		}
	}
	private void stopRxTimer() {
		if(D) Log.d(TAG, "stopRxTimer()");
		synchronized (mRxHandler) {
			mRxHandler.removeCallbacks(mRxSuperTask);
			//if(D) Log.d(TAG, "mRxHandler.removeCallbacks");
		}
	}
    
    GattLayerCallback mGattCallback = new GattLayerCallback() {
    	@Override
		public void onConnectionStateChange(final boolean status, final boolean newState) {
    		if(D) Log.d(TAG, "onConnectionStateChange, status: " + status + ", newState: " + newState);
			mCallback.onConnectionStateChange(status, newState);
			if (!(status && newState)) {
				close();
			}
        }
		@Override
		public void onDataLengthChanged(final int length) {
			if(D) Log.d(TAG, "onDataLengthChanged, length: " + length);

			MTU_PAYLOAD_SIZE_LIMIT = length;
		}
		@Override
    	public void onDataSend(final boolean status) {
    		if(D) Log.d(TAG, "onDataSend, status: " + status);
    		synchronized(mSendDataLock) {
    			isDataSend = true;
    			mSendDataLock.notifyAll();
			}
        }
		@Override
		public void onDataReceive(final byte[] data) {
			if(D) Log.d(TAG, "onDataReceive() : " + StringByteTrans.byte2HexStr(data));
			// be careful send ack may call by the GattCallback
			receiveData(data);
		}
		@Override
		public void onNameReceive(final String data) {
			mCallback.onNameReceive(data);
		}
	};
}
