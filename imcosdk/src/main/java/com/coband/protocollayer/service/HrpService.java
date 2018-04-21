package com.coband.protocollayer.service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.coband.protocollayer.gattlayer.GlobalGatt;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Battery Service
 * reference: https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.battery_service.xml
 */
public class HrpService {
    // LOG
    private static final boolean D = true;
    private static final String TAG = "HrpService";

    // Support Service UUID and Characteristic UUID
    private final static UUID HRP_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    private final static UUID HRP_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    public final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    // Support Service object and Characteristic object
    private BluetoothGattService mService;
    private BluetoothGattCharacteristic mMeasurementCharac;

    private int mHrpValue = -1;

    private GlobalGatt mGlobalGatt;

    private OnServiceListener mCallback;

    private String mBluetoothAddress;

    public HrpService(String addr, OnServiceListener callback) {
        mCallback = callback;
        mBluetoothAddress = addr;

        mGlobalGatt = GlobalGatt.getInstance();
        initial();
    }

    public void close() {
        mGlobalGatt.unRegisterCallback(mGattCallback);
    }

    private void initial() {
        // register service discovery callback
        mGlobalGatt.registerCallback(mGattCallback);
    }

    public boolean setService(BluetoothGattService service) {
        if (service.getUuid().equals(HRP_SERVICE_UUID)) {
            mService = service;
            return true;
        }
        return false;
    }

    public List<BluetoothGattCharacteristic> getNotifyCharacteristic() {
        return null;
    }

    /*
    public boolean readInfo() {
        if (mMeasurementCharac == null) {
            if (D) Log.e(TAG, "read Battery info error with null charac");
            return false;
        }
        if (D) Log.d(TAG, "read Battery info.");
        return mGlobalGatt.readCharacteristicSync(mBluetoothAddress, mBatteryLeverlCharac);
    }*/

    public boolean isInclude() {
        return (mService != null);
    }

    public String getServiceUUID() {
        return HRP_SERVICE_UUID.toString();
    }

    public String getServiceSimpleName() {
        return "HRP";
    }

    public boolean enableNotification(boolean enable) {
        return mGlobalGatt.setCharacteristicNotificationSync(mMeasurementCharac, enable);
    }

    public boolean isNotifyEnable() {
        final BluetoothGattDescriptor descriptor = mMeasurementCharac.getDescriptor(
                CLIENT_CHARACTERISTIC_CONFIG);
        byte[] data = descriptor.getValue();
        if (D) Log.d(TAG, "isNotifyEnable, data: " + Arrays.toString(data));
        return memcmp(data, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, 2);
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mService = gatt.getService(HRP_SERVICE_UUID);
                if (mService == null) {
                    Log.e(TAG, "Hrp service not found");
                    return;
                } else {
                    mMeasurementCharac = mService.getCharacteristic(HRP_MEASUREMENT_CHARACTERISTIC_UUID);
                    if (mMeasurementCharac == null) {
                        if (D) Log.e(TAG, "Hrp service characteristic not found");
                        return;
                    } else {
                        if (D)
                            Log.d(TAG, "Hrp service is found, mMeasurementCharac: " + mMeasurementCharac.getUuid());
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
            byte[] data = characteristic.getValue();
            if (mMeasurementCharac == null) {
                return;
            }
            if (mMeasurementCharac.getUuid().equals(characteristic.getUuid())) {
                if (D) Log.d(TAG, "onCharacteristicChanged, data: " + Arrays.toString(data));
                mHrpValue = ((data[0] << 8) & 0xff00) | (data[1] & 0x0ff);
                // call function to deal the data
                mCallback.onHrpValueReceive(mHrpValue);
            } else {
                //if(D) Log.w(TAG, "receive other notification");
                return;
            }
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (mMeasurementCharac == null) {
                    return;
                }
                if (descriptor.getCharacteristic().getUuid().equals(mMeasurementCharac.getUuid())) {
                    if (CLIENT_CHARACTERISTIC_CONFIG.equals(descriptor.getUuid())) {
                        if (D) Log.d(TAG, "Descriptor write ok.");
                    }
                }
            } else {
                if (D) Log.e(TAG, "Descriptor write error: " + status);
            }
        }

    };

    public int getValue() {
        return mHrpValue;
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
        public void onHrpValueReceive(int value);
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
