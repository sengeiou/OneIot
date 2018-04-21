package com.coband.protocollayer.service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.coband.protocollayer.gattlayer.GlobalGatt;
import com.coband.utils.LogUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Battery Service
 * reference: https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.battery_service.xml
 */
public class BatteryService {
    // LOG
    private static final boolean D = true;
    private static final String TAG = "BatteryService";

    // Support Service UUID and Characteristic UUID
    private final static UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    private final static UUID BATTERY_LEVEL_CHARACTERISTIC_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");

    private final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    // Support Service object and Characteristic object
    private BluetoothGattService mService;
    private BluetoothGattCharacteristic mBatteryLevelCharac;

    // Current Battery value
    private int mBatteryValue = -1;

    private GlobalGatt mGlobalGatt;

    private OnServiceListener mCallback;

    private String mBluetoothAddress;

    public BatteryService(String addr, OnServiceListener callback) {
        mCallback = callback;
        mBluetoothAddress = addr;

        mGlobalGatt = GlobalGatt.getInstance();
        initial();
    }

    public void close() {
        mGlobalGatt.unRegisterCallback(mGattCallback);
    }

    private void initial() {

        LogUtils.d(TAG, ">>> BatteryService initial");

        // register service discovery callback
        mGlobalGatt.registerCallback(mGattCallback);
    }

    public boolean setService(BluetoothGattService service) {
        if (service.getUuid().equals(BATTERY_SERVICE_UUID)) {
            mService = service;
            return true;
        }
        return false;
    }

    public List<BluetoothGattCharacteristic> getNotifyCharacteristic() {
        return null;
    }

    public boolean readInfo() {
        if (mBatteryLevelCharac == null) {
            if (D) Log.e(TAG, "read Battery info error with null charac");
            return false;
        }
        if (D) Log.d(TAG, "read Battery info.");
        return mGlobalGatt.readCharacteristicSync(mBatteryLevelCharac);
    }

    public String getServiceUUID() {
        return BATTERY_SERVICE_UUID.toString();
    }

    public String getServiceSimpleName() {
        return "Battery";
    }

    public boolean enableNotification(boolean enable) {
        if (enable == isBatteryNotifyEnable()) {
            return true;
        }
        if (null == mBatteryLevelCharac) return false;
        return mGlobalGatt.setCharacteristicNotificationSync(mBatteryLevelCharac, enable);
    }

    public boolean isBatteryNotifyEnable() {
        if (mBatteryLevelCharac != null) {
            final BluetoothGattDescriptor descriptor = mBatteryLevelCharac.getDescriptor(
                    CLIENT_CHARACTERISTIC_CONFIG);
            byte[] data = descriptor.getValue();
            if (D) Log.d(TAG, "isBatteryNotifyEnable, data: " + Arrays.toString(data));
            return memcmp(data, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, 2);
        } else {
            return false;
        }
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mService = gatt.getService(BATTERY_SERVICE_UUID);
                if (mService == null) {
                    Log.e(TAG, "Battery service not found");
                } else {
                    mBatteryLevelCharac = mService.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC_UUID);
                    if (mBatteryLevelCharac == null) {
                        if (D) Log.e(TAG, "Battery service characteristic not found");
                    } else {
                        if (D)
                            Log.d(TAG, "Battery service is found, mBatteryCharacteristic: " + mBatteryLevelCharac.getUuid());
                    }
                }
                //isConnected = true;
            } else {
                if (D) Log.e(TAG, "Discovery service error: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            // add by ivan.
            Log.d(TAG, "uuid >>>>> " + characteristic.getUuid());
            byte[] data = characteristic.getValue();
            if (mBatteryLevelCharac == null) return;

            if (mBatteryLevelCharac.getUuid().equals(characteristic.getUuid())) {
                mBatteryValue = data[0];
                // call function to deal the data
                mCallback.onBatteryValueChanged(mBatteryValue);
            }
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (mBatteryLevelCharac == null) return;

                if (descriptor.getCharacteristic().getUuid().equals(mBatteryLevelCharac.getUuid())) {
                    if (CLIENT_CHARACTERISTIC_CONFIG.equals(descriptor.getUuid())) {
                        if (D) Log.d(TAG, "Descriptor write ok.");
                    }
                }
            } else {
                if (D) Log.e(TAG, "Descriptor write error: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //if(D) Log.d(TAG, "onCharacteristicRead UUID is: " + characteristic.getUuid() + ", addr: " +mBluetoothAddress);
            //if(D) Log.d(TAG, "onCharacteristicRead data value:"+ Arrays.toString(characteristic.getValue()) + ", addr: " +mBluetoothAddress);
            byte[] data = characteristic.getValue();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (null == mBatteryLevelCharac) return;
                if (mBatteryLevelCharac.getUuid().equals(characteristic.getUuid())) {
                    mBatteryValue = data[0];
                    // call function to deal the data
                    mCallback.onBatteryValueReceive(mBatteryValue);
                }
            } else {
                if (D) Log.e(TAG, "Characteristic read error: " + status);
            }

        }
    };

    public int getBatteryValue() {
        return mBatteryValue;
    }

    /**
     * Interface required to be implemented by activity
     */
    public static interface OnServiceListener {
        /**
         * Fired when value come.
         *
         * @param value receive value
         */
        public void onBatteryValueReceive(int value);

        public void onBatteryValueChanged(int value);
    }

    /**
     *   * 比较两个byte数组数据是否相同,相同返回 true
     *   *
     *   * @param data1
     *   * @param data2
     *   * @param len
     *   * @return
     *  
     */
    public static boolean memcmp(byte[] data1, byte[] data2, int len) {
        if (data1 == null && data2 == null) {
            return true;
        }
        if (data1 == null || data2 == null) {
            return false;
        }
        if (data1 == data2) {
            return true;
        }
        boolean bEquals = true;
        int i;
        for (i = 0; i < data1.length && i < data2.length && i < len; i++) {
            if (data1[i] != data2[i]) {
                bEquals = false;
                break;
            }
        }
        return bEquals;
    }
}
