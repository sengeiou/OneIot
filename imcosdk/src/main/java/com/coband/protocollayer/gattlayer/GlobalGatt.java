package com.coband.protocollayer.gattlayer;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.coband.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is use to manager gatt connect, to let all the activity have only a callback.
 */
public class GlobalGatt {
    // Log
    private final static String TAG = "GlobalGatt";
    private final static boolean D = true;


    private static final Object CALLBACK_LOCK = new Object();

    private static long mLastTryToConnectTime = 0L;

    private static final long RESTORE_CONNECTION_STATE_PERIOD = 15000L;

    private int mTryToUseExistGattCount = 0;

    // Callbacks
    // each address have a list of callback.
    private final List<BluetoothGattCallback> mCallbacks = new CopyOnWriteArrayList<>();
//    private HashMap<String, ArrayList<BluetoothGattCallback>> mCallbacks; // Only allow one callback

    // instance
    private static GlobalGatt mInstance;

    private static BluetoothGatt mBluetoothGatt;
    private static String mBluetoothDeviceAddress;

    // Bluetooth Manager
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    // for sync gatt callback
    private volatile boolean mGattCallbackCalled;
    private final Object mGattCallbackLock = new Object(); //used for gatt callback
    private static final int MAX_CALLBACK_LOCK_WAIT_TIME = 3000;

    // Connection state
//    private HashMap<String, Integer> mConnectionState;
    private static int mConnectionState;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private static Context mContext;

    // UUID
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public static void initial(Context context) {
        mInstance = new GlobalGatt();
        mContext = context;

//        mInstance.mCallbacks = new HashMap<>();

        BLEReceiver receiver = new BLEReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(receiver, filter);
    }


    /**
     * Get the Global gatt object.
     * <p>
     * <p>It will return a instance.
     *
     * @return The GlobalGatt instance.
     */
    public static GlobalGatt getInstance() {
        return mInstance;
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (D) Log.d(TAG, "initialize()");
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
                return false;
            }
        }
        return true;
    }

    public String getBondAddress() {
        return mBluetoothDeviceAddress;
    }

    public boolean isConnected() {
        return mConnectionState == STATE_CONNECTED;
    }

    public boolean isHostConnected(final String address) {
        if (mBluetoothManager == null) {
            if (D) Log.w(TAG, "isHostConnected, addr: " + address + ", mBluetoothManager == null");
            return false;
        }
        List<BluetoothDevice> lists = mBluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        if (lists.isEmpty()) {
            for (BluetoothDevice device : lists) {
                if (device.getAddress().equals(address)) {
                    if (D) Log.d(TAG, "isHostConnected, addr: " + address + ", Connected.");
                    return true;
                }
            }
        }
        if (D) Log.w(TAG, "isHostConnected, addr: " + address + ", Disconnected.");
        return false;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address  The device address of the destination device.
     * @param callback The gatt callback.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public synchronized boolean connect(@NonNull final String address, final BluetoothGattCallback callback) {

        if (mConnectionState == STATE_CONNECTING) {
            if (System.currentTimeMillis() - mLastTryToConnectTime > RESTORE_CONNECTION_STATE_PERIOD) {
                mConnectionState = STATE_DISCONNECTED;
                mLastTryToConnectTime = System.currentTimeMillis();
            } else {
                if (D) Log.d(TAG, "connecting device, do nothing >>>>>>>>>");
                return false;
            }
        }

        if (mBluetoothAdapter == null) {
            if (D) Log.e(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // if connect, an other want connect
        if (isConnected()) {
            if (D) Log.d(TAG, "another device want to connect. addr: " + address);
            // register a callback
            registerCallback(callback);
            // call the connection state change callback to tell it connect
            callback.onConnectionStateChange(mBluetoothGatt, BluetoothGatt.GATT_SUCCESS,
                    BluetoothProfile.STATE_CONNECTED);
            return true;
        }

        // if try to use exists gatt connect count more than twice, close this gatt and create
        // new gatt to connect device.
        if (address.equals(mBluetoothDeviceAddress) && mTryToUseExistGattCount < 3) {
            if (D) Log.d(TAG, "trying to use an exists bluetooth gatt to connect.");
            if (mBluetoothGatt != null && mBluetoothGatt.connect()) {
                mTryToUseExistGattCount++;
                // update state
                mConnectionState = STATE_CONNECTING;
                return true;
            }

//            if (connectDevice(mBluetoothDeviceAddress)) {
//                // update state
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                mBluetoothDeviceAddress = null;
//                mBluetoothGatt = null;
//                return false;
//            }
        }

        mTryToUseExistGattCount = 0;

        registerCallback(callback);
        closeBluetoothGatt();
        connectDevice(address);

//        closeBluetoothGatt();
//
//        // register a callback
//        registerCallback(callback);
//
//        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//        if (device == null) {
//            if (D) Log.e(TAG, "Device not found.  Unable to connect.");
//            return false;
//        }
//        if (D) Log.d(TAG, "Trying to create a new connection.");
//        // We want to directly connect to the device, so we are setting the auto connect
//        // parameter to false.
//
//        device.connectGatt(mContext, false, new GattCallback());
//
//        mBluetoothDeviceAddress = address;

        return true;
    }

    private boolean connectDevice(String address) {
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            if (D) Log.e(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        mBluetoothGatt = device.connectGatt(mContext, false, new GattCallback());
        LogUtils.d(TAG, "connecting >>>>>>>>>>>>> ");
//        mBluetoothGatt.connect();
        mConnectionState = STATE_CONNECTING;
        mBluetoothDeviceAddress = address;
        return true;
    }


    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void closeBluetoothGatt() {
        if (D)
            Log.d(TAG, "closeBluetoothGatt >>>>>");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothDeviceAddress = null;
            mBluetoothGatt = null;
            mConnectionState = STATE_DISCONNECTED;
        }
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    void disconnectGatt() {
        if (D) Log.d(TAG, "disconnect gatt >>>>>");
        if ((mBluetoothGatt != null)) {
            mBluetoothGatt.disconnect();
        }
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     * @return Return true if the read is initiated successfully. The read result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
        return true;
    }

    /**
     * Request a write on a given {@code BluetoothGattCharacteristic}. The write result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to write.
     * @return Return true if the write is initiated successfully. The read result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);
        return true;
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     * @return Return true if the read is initiated successfully. The read result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onDescriptorWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattDescriptor, int)}
     * callback.
     */
    boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        // enable notifications locally
        boolean b = mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        if (D) Log.d(TAG, "mBluetoothGatts.get(addr).setCharacteristicNotification　return: " + b);

        // enable notifications on the device
        final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                WRISTBAND_NOTIFY_CHARACTERISTIC_UUID);
                UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        if (enabled) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        return mBluetoothGatt.writeDescriptor(descriptor);
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}.
     * This is a sync method, only the callback is called, it will return.
     *
     * @param characteristic The characteristic to read from.
     * @return Return true if the read is initiated successfully. The read result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    public boolean readCharacteristicSync(BluetoothGattCharacteristic characteristic) {
        if (D) Log.d(TAG, "readCharacteristicSync");
        mGattCallbackCalled = false;

        if (!readCharacteristic(characteristic)) {
            return false;
        }

        synchronized (mGattCallbackLock) {
            try {
                // here only wait for 3 seconds
                if (!mGattCallbackCalled) {
                    if (D) Log.d(TAG, "wait for " + MAX_CALLBACK_LOCK_WAIT_TIME + "ms");
                    mGattCallbackLock.wait(MAX_CALLBACK_LOCK_WAIT_TIME);

                    if (D) Log.d(TAG, "wait time reached");
                }
            } catch (final InterruptedException e) {
                if (D) Log.e(TAG, "readCharacteristicSync Sleeping interrupted, e:" + e);
            }
        }
        return true;
    }

    /**
     * Request a write on a given {@code BluetoothGattCharacteristic}. The write result is reported
     * This is a sync method, only the callback is called, it will return.
     *
     * @param characteristic The characteristic to write.
     * @return Return true if the write is initiated successfully. The read result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    public boolean writeCharacteristicSync(BluetoothGattCharacteristic characteristic) {
        if (D) Log.d(TAG, "writeCharacteristicSync");
        mGattCallbackCalled = false;
        LogUtils.d(">>> TEST_THEARD ", ">>> 1 init : Thread " + Thread.currentThread().getId());

        if (!writeCharacteristic(characteristic)) {
            return false;
        }

        synchronized (mGattCallbackLock) {
            try {
                // here only wait for 3 seconds
//                if (mGattCallbackCalled == false) {
                if (D) Log.d(TAG, "wait for " + MAX_CALLBACK_LOCK_WAIT_TIME + "ms");
                mGattCallbackLock.wait(MAX_CALLBACK_LOCK_WAIT_TIME);
                LogUtils.d(">>> TEST_THEARD ", ">>> 2 wait for : Thread " + Thread.currentThread().getId());

                if (D) Log.d(TAG, "wait time reached");
//                }
            } catch (final InterruptedException e) {
                if (D) Log.e(TAG, "readCharacteristicSync Sleeping interrupted, e:" + e);
            }
        }
        return true;
    }

    /**
     * Enables or disables notification on a give characteristic.
     * This is a sync method, only the callback is called, it will return.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     * @return Return true if the read is initiated successfully. The read result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onDescriptorWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattDescriptor, int)}
     * callback.
     */
    public boolean setCharacteristicNotificationSync(BluetoothGattCharacteristic characteristic,
                                                     boolean enabled) {
        if (D) Log.d(TAG, "setCharacteristicNotificationSync");
        mGattCallbackCalled = false;

        if (!setCharacteristicNotification(characteristic, enabled)) {
            return false;
        }

        synchronized (mGattCallbackLock) {
            try {
                // here only wait for 3 seconds
                if (!mGattCallbackCalled) {
                    if (D) Log.d(TAG, "wait for " + MAX_CALLBACK_LOCK_WAIT_TIME + "ms");
                    mGattCallbackLock.wait(MAX_CALLBACK_LOCK_WAIT_TIME);
                    if (D) Log.d(TAG, "wait time reached");

                }
            } catch (final InterruptedException e) {
                if (D) Log.e(TAG, "readCharacteristicSync Sleeping interrupted, e:" + e);
            }
        }
        return true;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices(final String addr) {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public BluetoothDevice getConnectDevices() {
        return mBluetoothGatt.getDevice();
    }

    public String getDeviceName(final String addr) {
        if (mBluetoothGatt == null) {
            if (D) Log.e(TAG, "bluetooth gatt is null, addr: " + addr);
            return null;
        }
        return mBluetoothGatt.getDevice().getName();
    }

    /**
     * Clears the device cache. After uploading new firmware the DFU target will have other services than before.
     *
     * @param gatt the GATT device to be refreshed
     */
    private void refreshDeviceCache(final BluetoothGatt gatt) {
        /*
         * There is a refresh() method in BluetoothGatt class but for now it's hidden. We will call it using reflections.
		 */
        try {
            final Method refresh = gatt.getClass().getMethod("refresh");
            if (refresh != null) {
                final boolean success = (Boolean) refresh.invoke(gatt);
                Log.d(TAG, "Refreshing result: " + success);
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception occured while refreshing device", e);
        }
    }

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private class GattCallback extends BluetoothGattCallback {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            if (D) Log.d(TAG, "onMtuChanged new mtu is " + mtu);
            if (D) Log.d(TAG, "onMtuChanged new status is " + String.valueOf(status));
//            String addr = gatt.getDevice().getAddress();
            // tell all the callback
//            for (BluetoothGattCallback callback : mCallbacks.get(addr)) {
//                callback.onMtuChanged(gatt, mtu, status);
//            }
            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onMtuChanged(gatt, mtu, status);
                }
            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            String addr = gatt.getDevice().getAddress();
            LogUtils.d(TAG, "onConnectionStateChange >>>> status >>>> " + status + " newSate >>>" + newState);
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED;
//                mBluetoothGatt = gatt;
                if (mBluetoothGatt.equals(gatt)) {
                    LogUtils.d(TAG, "equals >>>>>>>>");
                } else {
                    LogUtils.d(TAG, "not equals >>>>>>>>");
                }
            } else {
                mConnectionState = STATE_DISCONNECTED;
                closeBluetoothGatt();
            }

            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onConnectionStateChange(gatt, status, newState);
                }
            }

//            if (status != BluetoothGatt.GATT_SUCCESS || newState != BluetoothProfile.STATE_CONNECTED) {
//                mCallbacks.remove(addr);
//            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            String addr = gatt.getDevice().getAddress();
            // tell all the callback
//            for (BluetoothGattCallback callback : mCallbacks.get(addr)) {
//                callback.onServicesDiscovered(gatt, status);
//            }
            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onServicesDiscovered(gatt, status);
                }

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            String addr = gatt.getDevice().getAddress();
            // notify waiting thread
            synchronized (mGattCallbackLock) {
                mGattCallbackCalled = true;
                mGattCallbackLock.notifyAll();

            }
            // tell all the callback
//            for (BluetoothGattCallback callback : mCallbacks.get(addr)) {
//                callback.onCharacteristicRead(gatt, characteristic, status);
//            }
            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onCharacteristicRead(gatt, characteristic, status);
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            String addr = gatt.getDevice().getAddress();
//            if (D) Log.d(TAG, "onCharacteristicChanged, addr: " + addr);
            // tell all the callback
//            for (BluetoothGattCallback callback : mCallbacks.get(addr)) {
//                callback.onCharacteristicChanged(gatt, characteristic);
//            }
            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onCharacteristicChanged(gatt, characteristic);
                }
            }
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
//            String addr = gatt.getDevice().getAddress();
            // notify waiting thread
            synchronized (mGattCallbackLock) {
                mGattCallbackCalled = true;
                mGattCallbackLock.notifyAll();
            }
            // tell all the callback
//            for (BluetoothGattCallback callback : mCallbacks.get(addr)) {
//                callback.onDescriptorWrite(gatt, descriptor, status);
//            }
            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onDescriptorWrite(gatt, descriptor, status);
                }
            }
        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt,
                                          final BluetoothGattCharacteristic characteristic, final int status) {
            String addr = gatt.getDevice().getAddress();
            if (D) Log.d(TAG, "onCharacteristicWrite: addr is " + addr);

            // notify waiting thread
            synchronized (mGattCallbackLock) {
                mGattCallbackCalled = true;
                mGattCallbackLock.notifyAll();
                LogUtils.d(">>> TEST_THEARD ", ">>> 3 notifyAll : Thread " + Thread.currentThread().getId());
            }
            // tell all the callback
//            for (BluetoothGattCallback callback : mCallbacks.get(addr)) {
//                callback.onCharacteristicWrite(gatt, characteristic, status);
//            }

            synchronized (CALLBACK_LOCK) {
                Iterator it = mCallbacks.iterator();
                while (it.hasNext()) {
                    BluetoothGattCallback callback = (BluetoothGattCallback) it.next();
                    callback.onCharacteristicWrite(gatt, characteristic, status);
                }
            }
        }
    }

    ;

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

//    public ArrayList<BluetoothGattCallback> getCallback(final String addr) {
//        return mCallbacks.get(addr);
//    }

    public void registerCallback(BluetoothGattCallback callback) {
        // register a callback
//        if (mCallbacks.get(addr) == null) {
//            if (D) Log.d(TAG, "registerCallback, mCallbacks.get(addr) == null, addr: " + addr);
//            ArrayList<BluetoothGattCallback> c = new ArrayList<>();
//            c.add(callback);
//            mCallbacks.put(addr, c);
//            if (D) Log.d(TAG, "registerCallback, mCallbacks.get(addr) = " + mCallbacks.get(addr)
//                    + ". mCallbacks.size(): " + mCallbacks.size() + ", addr: " + addr);
//
//            if (D) {
//                Iterator key = mCallbacks.keySet().iterator();
//                while (key.hasNext()) {
//                    if (D) Log.e(TAG, "registerCallback, mCallbacks list: " + key.next());
//                }
//            }
//            return;
//        }
//
//        if (!(mCallbacks.get(addr).contains(callback))) {
//            // register a callback
//            ArrayList<BluetoothGattCallback> c = mCallbacks.get(addr);
//            c.add(callback);
//            mCallbacks.put(addr, c);
//            if (D)
//                Log.d(TAG, "registerCallback, mCallbacks.get(addr).contains(callback). mCallbacks.size(): " + mCallbacks.size() + ", addr: " + addr);
//
//            if (D) {
//                Iterator key = mCallbacks.keySet().iterator();
//                while (key.hasNext()) {
//                    Log.e(TAG, "registerCallback, mCallbacks list: " + key.next());
//                }
//            }
//        }

        synchronized (CALLBACK_LOCK) {
            if (!mCallbacks.contains(callback)) {
                mCallbacks.add(callback);
            }
        }
    }

    public void unRegisterCallback(BluetoothGattCallback callback) {
//        if (D) Log.d(TAG, "unRegisterCallback, addr: " + addr);
//        if (mCallbacks.get(addr) == null) {
//            if (D) Log.d(TAG, "unRegisterCallback, mCallbacks.get(addr) == null");
//            return;
//        }
//        // unregister a callback
//        if (mCallbacks.get(addr).contains(callback)) {
//            if (D) Log.d(TAG, "unRegisterCallback, unregister a callback");
//            // unregister a callback
//            ArrayList<BluetoothGattCallback> c = mCallbacks.get(addr);
//            c.remove(callback);
//            mCallbacks.put(addr, c);
//        }
        synchronized (CALLBACK_LOCK) {
            if (mCallbacks.contains(callback)) {
                mCallbacks.remove(callback);
            }
        }

    }

    public void unRegisterAllCallback() {
//        if (D) Log.d(TAG, "unRegisterAllCallback, addr: " + addr);
//        if (mCallbacks.get(addr) == null) {
//            if (D) Log.d(TAG, "unRegisterAllCallback, mCallbacks.get(addr) == null");
//            return;
//        }
//        mCallbacks.remove(addr);
        synchronized (CALLBACK_LOCK) {
            mCallbacks.clear();
        }
    }

    private static class BLEReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mConnectionState = STATE_DISCONNECTED;
                        LogUtils.d(TAG, "STATE_OFF ：　Bluetooth off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        LogUtils.d(TAG, "STATE_TURNING_OFF : Bluetooth turning off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        LogUtils.d(TAG, "STATE_ON : Bluetooth on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        LogUtils.d(TAG, "STATE_TURNING_ON : Bluetooth turning on");
                        break;
                }
            }
        }
    }
}
