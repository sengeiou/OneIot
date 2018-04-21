package com.coband.cocoband.mvp.model.entity;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by ivan on 3/22/16.
 */
public class BleDevice {
    @Retention(SOURCE)
    @IntDef({CONNECT_FAILED, CONNECTING, DISCONNECTED})
    @interface ConnectionStatus {
    }

    public static final int CONNECTING = 0;
    public static final int CONNECT_FAILED = 1;
    public static final int DISCONNECTED = 2;


    private int connectionStatus = DISCONNECTED;

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(@ConnectionStatus int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public BleDevice(BluetoothDevice device, int rssi, int type) {
        this.device = device;
        this.rssi = rssi;
        this.type = type;
    }

    public BleDevice(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }

    private BluetoothDevice device;
    private int rssi;
    private int type;

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
