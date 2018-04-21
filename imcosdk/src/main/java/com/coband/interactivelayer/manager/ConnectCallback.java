package com.coband.interactivelayer.manager;

import android.bluetooth.BluetoothDevice;

/**
 * Created by mai on 17-8-31.
 */

public class ConnectCallback {

    /**
     * Callback reporting an Band device found during a device scan initiated
     * by the {@link ConnectManager#scanLeDevice} function.
     *
     * @param device Identifies the remote device
     * @param rssi The RSSI value for the remote device as reported by the
     *             Bluetooth hardware. 0 if no RSSI value is available.
     * @param scanRecord The content of the advertisement record offered by
     *                   the remote device.
     */
    public void foundDevices(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
    }

    /**
     * Callback reporting a device connection status after a device try to connect
     * by the {@link ConnectManager#connect} function.
     * @param succeeded　
     *        true : a band is connected successfully
     *        false : a band is connected failed
     * @param code　
     *        if succeeded is true
     *        code is {@link CommandManager.LoginState}
     *        if succeeded is false
     *        code is {@link CommandManager.ErrorCode}
     */
    public void connectStatus(boolean succeeded, int code) {
    }
}