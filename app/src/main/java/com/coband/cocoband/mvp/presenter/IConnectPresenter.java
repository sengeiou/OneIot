package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.mvp.model.entity.BleDevice;

/**
 * Created by imco on 3/21/16.
 */
public interface IConnectPresenter {
    void connectDevice(BleDevice device);

    void scanDevices(int periodTime);

    void disconnectDevice();

    void stopScanDevices();

}
