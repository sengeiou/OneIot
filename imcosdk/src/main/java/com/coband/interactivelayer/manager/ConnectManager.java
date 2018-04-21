package com.coband.interactivelayer.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.coband.utils.ImeiHelper;
import com.coband.utils.LogUtils;
import com.coband.utils.SPWristbandConfigInfo;
import com.coband.utils.SpecScanRecord;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/*
 * Created by Administrator on 2016/5/23.
 * modify by ivan on 7/13/2017
 */
public class ConnectManager {
    private static ConnectManager mInstance;

    private final static String TAG = "ConnectManager";
    private static long SCAN_PERIOD = 30000;
    private final static UUID WRISTBAND_SERVICE_UUID = UUID.fromString("000001ff-3c17-d293-8e48-14fe2e4da212");
    private final static UUID UTE1_UUID = UUID.fromString("00005554-0000-1000-8000-00805f9b34fb");
    private final static UUID UTE2_UUID = UUID.fromString("0000454d-0000-1000-8000-00805f9b34fb");
    private final static UUID UTE3_UUID = UUID.fromString("0000524b-0000-1000-8000-00805f9b34fb");
    private final static UUID UTE4_UUID = UUID.fromString("000055ff-0000-1000-8000-00805f9b34fb");
    private final static UUID UTE5_UUID = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
    private final String USER_ID = "1495015811";//1495015811,1234567890

    private static Context mContext;

    private boolean mBind;
    // connect　status


    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;

    private ControlManager mWristbandManager;

    ArrayList<ConnectCallback> mCallbacks;
    private final Object mConnectSendLock = new Object();

    private boolean mScanning;
    private boolean mAutoConnecting = false;

    // Use this count to count current receive adv count, may be something error can not scan.
    private int mReceiveAdvCount = 0;
    private Disposable mDelayStopScan;
    private boolean isLogin = false;
    private ScheduledExecutorService mAutoConnectThread;

    static void init(Context context) {
        mInstance = new ConnectManager();
        mContext = context;
        mInstance.mWristbandManager = ControlManager.getInstance();

        if (mInstance.mBluetoothManager == null) {
            mInstance.mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mInstance.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
            }
        }

        if (mInstance.mBluetoothAdapter == null) {
            mInstance.mBluetoothAdapter = mInstance.mBluetoothManager.getAdapter();
            if (mInstance.mBluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            }
        }
        // Initial Callback list
        mInstance.mCallbacks = new ArrayList<>();
    }

    public static ConnectManager getInstance() {
        return mInstance;
    }


    /**
     * register connect call back
     */
    public void registerCallback(ConnectCallback callback) {
        synchronized (mConnectSendLock) {
            if (!mCallbacks.contains(callback)) {
                boolean add = mCallbacks.add(callback);
                LogUtils.d(TAG, "register : " + add);
            }
        }
    }

    /**
     * unregister connect call back
     */
    public void unregisterCallback(ConnectCallback callback) {
        synchronized (mConnectSendLock) {
            if (mCallbacks.contains(callback)) {
                boolean remove = mCallbacks.remove(callback);
                LogUtils.d(TAG, "remove register : " + remove);

            }
        }
    }

    /**
     * disconnect device
     */
    public void disconnect() {
        mWristbandManager.disconnect();
    }

    /**
     * unbind device
     */
    public void unbind(final SendCommandCallback sendCommandCallback) {
        LogUtils.d(TAG, ">>>>>>>>>> unbind");
        SPWristbandConfigInfo.setBondedDevice(mContext, null);
        stopAutoConnect();
        if (!isConnected()) {
            ControlManager.getInstance().closeGatt();
        }

        CommandManager.sendCommand(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                isLogin = false;
                e.onNext(mWristbandManager.sendRemoveBondCommand());
            }
        }, new SendCommandCallback() {
            @Override
            public void onCommandSend(boolean status) {
                disconnect();
                sendCommandCallback.onCommandSend(status);
            }

            @Override
            public void onDisconnected() {
                sendCommandCallback.onDisconnected();
            }

            @Override
            public void onError(Throwable error) {
                sendCommandCallback.onError(error);

            }
        });
    }

    /**
     * start auto connect band
     */
    public void startAutoConnect() {
        if (SPWristbandConfigInfo.getBondedDevice(mContext) == null) {
            return;
        }

        stopAutoConnect();
        mAutoConnectThread = Executors.newSingleThreadScheduledExecutor();
        mAutoConnectThread.scheduleAtFixedRate(mAutoConnectTask, 0, 15000,
                TimeUnit.MILLISECONDS);
    }

    /**
     * the run() method must be add try catch. the thread pool will be stop to execute next runnable
     * while error occur otherwise.
     * <a href="http://blog.csdn.net/tianxiangshan/article/details/8599337"></a>
     */
    private final Runnable mAutoConnectTask = new Runnable() {
        @Override
        public void run() {
            try {
                if (mBluetoothAdapter.isEnabled() && !isConnected() &&
                        SPWristbandConfigInfo.getBondedDevice(mContext) != null) {
                    LogUtils.d(TAG, "run connect task >>>>>>>>>> ");
                    connect(SPWristbandConfigInfo.getBondedDevice(mContext));
                }
            } catch (Exception e) {

            }
        }
    };

    /**
     * stop auto connect band
     */
    public void stopAutoConnect() {
        if (mAutoConnectThread != null && !mAutoConnectThread.isShutdown()) {
            LogUtils.d(TAG, "stop auto connect >>>>>>>>>>>");
            mAutoConnectThread.shutdownNow();
        }
    }

    /**
     * Connect band
     * Recommended use {@link ConnectManager#connect(String, String)}
     *
     * @param address bluetooth address of device
     * @See {@link ConnectManager#connect(String, String)}
     */
    public boolean connect(final String address) {
        return connect(address, "");
    }

    /**
     * Connect band with no bind
     * just for test, recommended use {@link ConnectManager#connect(String, String)}
     *
     * @param address bluetooth address of Band device
     * @param bind    whether to bind after connecting the band device
     * @return
     */
    public boolean connectWithNoBind(String address, boolean bind) {
        mBind = bind;
        return connect(address, "");
    }

    /**
     * Connect band
     *
     * @param address Bluetooth address of device
     * @param userId  As a unique flag for the binding Band device
     */
    public boolean connect(final String address, String userId) {
        if (null == address) {
            Log.e(TAG, ">>> The device address is null!!!");
            return false;
        }

        if (isConnected()) {
            Log.e(TAG, ">>> The device is connected !!!");
            return false;
        }

        if (!userId.isEmpty()) {
            SPWristbandConfigInfo.setUserId(mContext, userId);
        }

        return mWristbandManager.connect(address.toUpperCase(), mWristbandManagerCallback);
    }


    /**
     * Scan Bluetooth Device
     *
     * @param enable true is start scan, false is stop scan
     */
    public void scanLeDevice(boolean enable) {
        scanLeDevice(enable, 30000);
    }

    /**
     * Scan Bluetooth Device
     *
     * @param enable     true is start scan, false is stop scan
     * @param scanPeriod scan period
     */
    public void scanLeDevice(boolean enable, long scanPeriod) {
        if (scanPeriod <= 0) {
            Log.d(TAG, "the period must be more than 0");
            return;
        }

        SCAN_PERIOD = scanPeriod;

        if (!mBluetoothAdapter.isEnabled()) {
            LogUtils.d(TAG, "scanLeDevice, enable: " + enable + ", wrong with bt not enable.");
            mScanning = false;
            return;
        }
        if (enable) {
            if (mScanning) {
                LogUtils.e(TAG, "the le scan is already on");
                if (mReceiveAdvCount == 0) {
                    LogUtils.w(TAG, "May be something wrong, le scan may be not real start, try restart it.");

                    delayStopScan(scanPeriod);
                }

            } else {
                LogUtils.d(TAG, "start the le scan, on time is " + SCAN_PERIOD + "ms");
                mReceiveAdvCount = 0;
                delayStopScan(scanPeriod);
            }
        } else {
            // avoid repetition operator
            if (!mScanning) {
                LogUtils.e(TAG, "the le scan is already off");
            } else {
                LogUtils.d(TAG, "stop the le scan");
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                closeDelayStopScan();
            }
        }
        // update le scan status
        mScanning = enable;
    }

    public boolean isConnected() {
        return mWristbandManager.isConnected();
    }

    private void delayStopScan(long scanPeriod) {
        closeDelayStopScan();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        mDelayStopScan = Observable.timer(scanPeriod, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                });
    }

    private void closeDelayStopScan() {
        if (null != mDelayStopScan && !mDelayStopScan.isDisposed()) {
            mDelayStopScan.dispose();
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            LogUtils.e(TAG, "onLeScan>>>>>>>>>>>>>>");

            if (!mScanning) {
                LogUtils.e(TAG, "is stop le scan, return");
                return;
            }
            mReceiveAdvCount++;

            SpecScanRecord record = SpecScanRecord.parseFromBytes(scanRecord);

            LogUtils.d(TAG, record.toString());

            if ((record.getServiceUuids() == null)) {
                return;
            }

            if (!(record.getServiceUuids().contains(new ParcelUuid(WRISTBAND_SERVICE_UUID)))) {

                return;
            }

            synchronized (mConnectSendLock) {
                for (int i = 0; i < mCallbacks.size(); i++) {
                    mCallbacks.get(i).foundDevices(device, rssi, scanRecord);

                }
            }
        }
    };

    // Application Layer callback
    private final CommandCallback mWristbandManagerCallback = new CommandCallback() {
        @Override
        public void onConnectionStateChange(final boolean status) {
            mAutoConnecting = false;

            LogUtils.d(TAG, "onConnectionStateChange, status: " + status);
            if (mBind) return;//just factory test
//                CommandManager.sendOtherNotifyInfoWithVibrateCount(0xfe, 0, "关机",null);
            if (status) {
                String userId = SPWristbandConfigInfo.getUserId(mContext);
                if (userId != null) {
                    mWristbandManager.startLoginProcess(userId);
                } else {
                    mWristbandManager.startLoginProcess(getUserIdByImei());
                }

            } else {
                synchronized (mConnectSendLock) {
                    for (int i = 0; i < mCallbacks.size(); i++) {
                        // TODO: 17-8-2 add by ivan
                        mCallbacks.get(i).connectStatus(false, -1);
                    }
                }
            }
        }

        @Override
        public void onLoginStateChange(final int state) {

            LogUtils.d(TAG, "onLoginStateChange, state: " + state);

            if (state == CommandManager.LoginState.STATE_WRIST_LOGIN) {
                SPWristbandConfigInfo.setBondedDevice(mContext, mWristbandManager.getBluetoothAddress());
                Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        // Enable battery notification
                        mWristbandManager.enableBatteryNotification(true);
                    }
                });
            }
            isLogin = true;

            synchronized (mConnectSendLock) {
                for (int i = 0; i < mCallbacks.size(); i++) {
                    mCallbacks.get(i).connectStatus(true, state);
                }
            }
        }

        @Override
        public void onError(final int error) {
            LogUtils.d(TAG, "ConnectError, error: " + error);
            // Need start scan
            synchronized (mConnectSendLock) {
                for (int i = 0; i < mCallbacks.size(); i++) {
                    mCallbacks.get(i).connectStatus(false, error);

                }
            }
        }
    };

    private String getUserIdByImei() {
        String userId = "";
        String imei = ImeiHelper.getIMEI(mContext);
        if (imei != null
                && !imei.equals("")
                && imei.length() >= 10) {
            userId = imei.substring(imei.length() - 10);
        } else {
            userId = USER_ID;
        }

        LogUtils.d(TAG, "getUserIdByImei, imei: " + imei + ", userId: " + userId);
        return userId;
    }

    public boolean isInLogin() {
        return isLogin;
    }

}
