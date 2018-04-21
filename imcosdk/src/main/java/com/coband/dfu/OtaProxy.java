package com.coband.dfu;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Keep;
import android.util.Log;

import com.coband.interactivelayer.manager.CommandManager;
import com.coband.interactivelayer.manager.ConnectManager;
import com.coband.utils.SPWristbandConfigInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OtaProxy {
    private static final String TAG = "OtaProxy";
    private static final boolean DBG = true;

    // special error: this error may stop OTA process, and may send a RESET command to the remote if the remote in OTA model
    public static final int ERROR_MASK = 0x0100;
    // for cann't connect to DFU device
    public static final int ERROR_DEVICE_DISCONNECTED = ERROR_MASK | 0x00;
    // for file io exception
    public static final int ERROR_FILE_IO_EXCEPTION = ERROR_MASK | 0x01;
    // for start gatt discovery failed
    public static final int ERROR_SERVICE_DISCOVERY_NOT_STARTED = ERROR_MASK | 0x02;
    // for no notification come
    public static final int ERROR_NO_NOTIFICATION_COME = ERROR_MASK | 0x03;
    // for cannot connect with no callback error
    public static final int ERROR_CANNOT_CONNECT_WITH_NO_CALLBACK_ERROR = ERROR_MASK | 0x04;
    // for send command but no callback, maybe a GKI_getBuffer error, or something else
    public static final int ERROR_CANNOT_SEND_COMMAND_WITH_NO_CALLBACK_ERROR = ERROR_MASK | 0x05;
    // for cannot find the special service, maybe the device is not right
    public static final int ERROR_CANNOT_FIND_SERVICE_ERROR = ERROR_MASK | 0x06;
    // for cannot find the special characteristic, maybe the device is not right
    public static final int ERROR_CANNOT_FIND_CHARAC_ERROR = ERROR_MASK | 0x07;
    // for connect with null return
    public static final int ERROR_CONNECT_ERROR = ERROR_MASK | 0x08;
    // for cannot find the device enter ota model
    public static final int ERROR_CANNOT_FIND_DEVICE_ERROR = ERROR_MASK | 0x09;
    // for cannot enable the remote notification
    public static final int ERROR_WRITE_CHARAC_NOTIFY_ERROR = ERROR_MASK | 0x0A;
    // for write characteristic failed
    public static final int ERROR_WRITE_CHARAC_ERROR = ERROR_MASK | 0x0B;
    // for send command reach the max try time
    public static final int ERROR_SEND_COMMAND_WITH_MAX_TRY_TIME_ERROR = ERROR_MASK | 0x0C;
    // for battery check
    public static final int ERROR_LOW_POWER_ERROR = ERROR_MASK | 0x0D;
    // for can't read bank info
    public static final int ERROR_READ_BANK_INFO_ERROR = ERROR_MASK | 0x0E;
    // for can't read app info
    public static final int ERROR_READ_APP_INFO_ERROR = ERROR_MASK | 0x0F;
    // for can't read patch info
    public static final int ERROR_READ_PATCH_INFO_ERROR = ERROR_MASK | 0x10;
    // for user didn't use the image
    public static final int ERROR_USER_NOT_ACTIVE_IMAGE_ERROR = ERROR_MASK | 0x11;

    // remote error: this error may stop OTA process, and will send a RESET command to the remote
    // the low byte is the error reason in the notification
    public static final int ERROR_REMOTE_MASK = 0x0200;
    // for no notification come
    public static final int ERROR_NO_NOTIFICATION_COME_ERROR = ERROR_REMOTE_MASK | 0xFF;

    // bluedroid error: this error may stop OTA process, and will send a RESET command to the remote
    // the low byte is the error reason back from bluedroid
    public static final int ERROR_BLUEDROID_MASK = 0x0400;

    // connection error: this error may try to reconnect two times when disconnect in OTA model
    // the low byte is the error reason back from bluedroid
    public static final int ERROR_CONNECTION_MASK = 0x0800;


    // Dfu process state
    public static final int PROCESS_STATE_MASK = 0x0100;

    // process state
    @IntDef({
            ProcessState.STA_ORIGIN,
            ProcessState.STA_REMOTE_ENTER_OTA,
            ProcessState.STA_FIND_OTA_REMOTE,
            ProcessState.STA_CONNECT_OTA_REMOTE,
            ProcessState.STA_START_OTA_PROCESS,
            ProcessState.STA_OTA_UPGRADE_SUCCESS
    })
    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface ProcessState {
        int STA_ORIGIN = PROCESS_STATE_MASK | 0x01; // init state
        int STA_REMOTE_ENTER_OTA = PROCESS_STATE_MASK | 0x02;
        int STA_FIND_OTA_REMOTE = PROCESS_STATE_MASK | 0x03;
        int STA_CONNECT_OTA_REMOTE = PROCESS_STATE_MASK | 0x04;
        int STA_START_OTA_PROCESS = PROCESS_STATE_MASK | 0x05;
        int STA_OTA_UPGRADE_SUCCESS = PROCESS_STATE_MASK | 0x06;
    }

    /*
    * Notification reason type
    * */
    public static final byte DFU_STATUS_SUCCESS = 0x01;
    public static final byte DFU_STATUS_NOT_SUPPORTED = 0x02;
    public static final byte DFU_STATUS_INVALID_PARAM = 0x03;
    public static final byte DFU_STATUS_OPERATION_FAILED = 0x04;
    public static final byte DFU_STATUS_DATA_SIZE_EXCEEDS_LIMIT = 0x05;
    public static final byte DFU_STATUS_CRC_ERROR = 0x06;


    /**
     * OTA Work Mode
     */
    public static final int OTA_MODE_FULL_FUNCTION = 0;
    public static final int OTA_MODE_LIMIT_FUNCTION = 1;
    public static final int OTA_MODE_EXTEND_FUNCTION = 2;

    public static final int OTA_MODE_SILENT_UPLOAD_MASK = 0x10;
    public static final int OTA_MODE_SILENT_UPLOAD_APP_FUNCTION = OTA_MODE_SILENT_UPLOAD_MASK | 0x01;
    public static final int OTA_MODE_SILENT_UPLOAD_PATCH_FUNCTION = OTA_MODE_SILENT_UPLOAD_MASK | 0x02;
    public static final int OTA_MODE_SILENT_UPLOAD_PATCH_EXTENSION_FUNCTION = OTA_MODE_SILENT_UPLOAD_MASK | 0x03;

    private Context mContext;
    private OtaCallback mRealsilDfuCallback;
    private IRealsilDfu mService;
    private BluetoothAdapter mAdapter;

    /**
     * Create a OtaProxy proxy object for interacting with the local
     * Realsil DFU service.
     */
    OtaProxy(Context context, OtaCallback callback) {
        if (DBG) Log.d(TAG, "OtaProxy");
        mContext = context;
        mRealsilDfuCallback = callback;
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        doBind();

    }

    private boolean doBind() {
        if (DBG) Log.d(TAG, "doBind");
        Intent intent = new Intent(mContext, DfuService.class);
        intent.setAction(IRealsilDfu.class.getName());
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        return true;
    }

    private void doUnbind() {
        if (DBG) Log.d(TAG, "doUnbind");
        synchronized (mConnection) {
            if (mService != null) {
                try {
                    // unregister the callback
                    mService.unregisterCallback(OtaProxy.class.getName(), mCallback);
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (Exception e) {
                    Log.e(TAG, "Unable to unbind RealsilDfuService", e);
                }
            }
        }
    }

    public void close() {
        if (DBG) Log.d(TAG, "close");
        mRealsilDfuCallback = null;

        doUnbind();
    }

    public void finalize() {
        if (DBG) Log.d(TAG, "finalize");
        close();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (DBG) Log.d(TAG, "Proxy object connected");
            mService = IRealsilDfu.Stub.asInterface(service);
            try {
                if (mService != null) {
                    mService.registerCallback(OtaProxy.class.getName(), mCallback);
                }
            } catch (RemoteException e) {
            }
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onServiceConnectionStateChange(true, OtaProxy.this);
            }
        }

        //onServiceDisconnected is only called in extreme situations (crashed / killed).
        public void onServiceDisconnected(ComponentName className) {
            if (DBG) Log.d(TAG, "Proxy object disconnected with an extreme situations");
            try {
                if (mService != null) {
                    mService.unregisterCallback(OtaProxy.class.getName(), mCallback);
                }
            } catch (RemoteException e) {
            }
            mService = null;
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onServiceConnectionStateChange(false, null);
                //mRealsilDfuCallback = null;
            }
        }
    };

    /**
     * Get the DFU proxy object.
     * <p>
     * <p>Clients must implement {@link OtaCallback} to get notified of
     * the connection status and to get the proxy object.
     *
     * @param context  Context of the application
     * @param listener The service Listener for connection callbacks.
     * @return true on success, false on error
     */
    public static boolean getDfuProxy(Context context, OtaCallback listener) {
        if (context == null || listener == null) return false;

        OtaProxy dfu = new OtaProxy(context, listener);
        return true;
    }

    ;

    private IRealsilDfuCallback.Stub mCallback = new IRealsilDfuCallback.Stub() {

        @Override
        public void onError(int e) throws RemoteException {
            if (CommandManager.isConnected()) {
                CommandManager.disconnect();
            }

            // TODO Auto-generated method stub
            if (DBG) Log.e(TAG, "onError: " + e);
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onError(e);
            }

            boolean connect = ConnectManager.getInstance().connect(SPWristbandConfigInfo.getBondedDevice(mContext));
            if (mRealsilDfuCallback != null) {
//                mRealsilDfuCallback.onConnectStatusAfterOTA(connect, SPWristbandConfigInfo.getBondedDevice(mContext));
            }
        }

        @Override
        public void onSuccess(int s) throws RemoteException {
            if (CommandManager.isConnected()) {
                CommandManager.disconnect();
            }

            // TODO Auto-generated method stub
            if (DBG) Log.e(TAG, "onSuccess: " + s);
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onSuccess(s);
            }

            // Connect device after ota success
            ConnectManager.getInstance().connect(SPWristbandConfigInfo.getBondedDevice(mContext));
        }

        @Override
        public void onProcessStateChanged(int state) throws RemoteException {
            // TODO Auto-generated method stub
            if (DBG) Log.e(TAG, "onProcessStateChanged: " + state);
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onProcessStateChanged(state);
            }
        }

        @Override
        public void onProgressChanged(int progress) throws RemoteException {
            // TODO Auto-generated method stub
            if (DBG) Log.e(TAG, "onProgressChanged: " + progress);
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onProgressChanged(progress);
            }
        }

    };

    /**
     * start OTA upgrade process, This method is ASYNCHRONOUS, it return true means start
     * OTA upgrade process, stop with the callback{@link OtaCallback}. if return false
     * means something error, please check the arguments and the bluetooth state.
     *
     * @param addr the device address which want to start OTA Upgrade
     * @param path the image file path
     * @return true/false  result of start, the
     */
    public boolean start(String addr, String path) {
        if (DBG) Log.d(TAG, " ota start");
        if (mService != null && isEnabled()) {
            try {
                return mService.start(OtaProxy.class.getName(), addr, path);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        if (!isEnabled()) Log.w(TAG, "the bluetooth didn't on");
        return false;
    }

    private boolean isEnabled() {
        if (mAdapter.getState() == BluetoothAdapter.STATE_ON) return true;
        return false;
    }

    /**
     * Sets AES secret key, this key's length must be 32
     *
     * @param key secret key
     * @return true/false  result of set key
     */
    public boolean setSecretKey(byte[] key) {
        if (DBG) Log.d(TAG, "setSecretKey");
        if (mService != null) {
            try {
                return mService.setSecretKey(key);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * check version control, default is disable
     *
     * @param enable enable/disable the version check
     * @return true/false  	result of set version check
     */
    public boolean setVersionCheck(boolean enable) {
        if (DBG) Log.d(TAG, "setVersionCheck");
        if (mService != null) {
            try {
                return mService.setVersionCheck(enable);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * check version control, default is disable
     *
     * @param enable enable/disable the version check
     * @return true/false  	result of set version check
     */
    public boolean setBatteryCheck(boolean enable) {
        if (DBG) Log.d(TAG, "setBatteryCheck");
        if (mService != null) {
            try {
                return mService.setBatteryCheck(enable);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * get the current OTA process state{@link ProcessState#STA_ORIGIN}
     *
     * @return state    result the current OTA process state
     */
    public int getCurrentOtaState() {
        if (DBG) Log.d(TAG, "setVersionCheck");
        if (mService != null) {
            try {
                return mService.getCurrentOtaState();
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return -1;
    }

    /**
     * get the current OTA process start or not
     *
     * @return true/false  	true means is in OTA process
     */
    public boolean isWorking() {
        if (DBG) Log.d(TAG, "setVersionCheck");
        if (mService != null) {
            try {
                return mService.isWorking();
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * get the current OTA work mode
     *
     * @return current work mode  	-1 means something error
     */
    public int getWorkMode() {
        if (DBG) Log.d(TAG, "getWorkMode");
        if (mService != null) {
            try {
                return mService.getWorkMode();
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return -1;
    }

    /**
     * set the current OTA work mode
     *
     * @return true/false  	true means set mode success
     */
    public boolean setWorkMode(int mode) {
        if (DBG) Log.d(TAG, "setWorkMode");
        if (mService != null) {
            try {
                return mService.setWorkMode(mode);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Set the current OTA image send speed, this only need use for Android4.4, for deal with the internal BUG.
     * The total speed should set though the current environment, if only have one connection(OTA), we may can
     * set 5000(5KB/s), if with a A2DP link, the speed suggest set with 1000(1KB/s). the normal speed is 3KB/s.
     * <p>
     * If work in silent mode, please set a min value, such as 100(100B/s) to make sure OTA process didn't affect
     * the normal traffic.
     *
     * @param en    enable or disable speed control
     * @param speed set the total speed
     * @return true/false  	true means set mode success
     */
    public boolean setSpeedControl(boolean en, int speed) {
        if (DBG) Log.d(TAG, "setSpeedControl");
        if (mService != null) {
            try {
                return mService.setSpeedControl(en, speed);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * Set need wait user check flag, in silent mode, we will download image silent, but in the last,
     * when we do all the thing, we should wait user check to active the image.
     * <p>
     * Other work mode normally didn't use this.
     *
     * @param en enable or disable user check
     * @return true/false  	true means set mode success
     */
    public boolean setNeedWaitUserCheckFlag(boolean en) {
        if (DBG) Log.d(TAG, "setNeedWaitUserCheckFlag");
        if (mService != null) {
            try {
                return mService.setNeedWaitUserCheckFlag(en);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    /**
     * When set the need wait user check flag, should call this method to active the image in the last
     * of OTA process.
     * <p>
     * Other work mode normally didn't use this.
     *
     * @param en enable or disable user check
     * @return true/false  	true means set mode success
     */
    public boolean activeImage(boolean en) {
        if (DBG) Log.d(TAG, "activeImage");
        if (mService != null) {
            try {
                return mService.activeImage(en);
            } catch (RemoteException e) {
                if (DBG) Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) Log.w(TAG, "Proxy not attached to service");
        return false;
    }


}
