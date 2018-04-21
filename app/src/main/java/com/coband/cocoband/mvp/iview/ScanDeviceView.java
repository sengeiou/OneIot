package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.BleDevice;

/**
 * Created by ivan on 17-5-19.
 */

public interface ScanDeviceView extends BaseView {
    void showBluetoothDisabled();

    void onStartScan();

    void onDeviceScan(BleDevice device);

    void onScanFinish();

    void showDeviceConnected();

    void showConnectFailed();

    void showConnectDeviceTimeOut();

    void showDeviceBondAlready();
}
