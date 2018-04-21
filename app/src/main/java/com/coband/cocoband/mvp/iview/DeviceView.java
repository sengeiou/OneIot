package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.DeviceSettings;

/**
 * Created by ivan on 17-4-19.
 */

public interface DeviceView extends BaseView {
    void showDeviceDisconnected(int deviceType, String deviceName);

    void showDeviceUnbind(int time);

    void showDeviceConnected(int deviceType, String deviceName);

    void showDeviceBattery(int battery);

    void showDeviceDisconnectedToast();

    void showDeviceConnecting();

    void showSetSedentaryRemind();

    void showSetAlarm();

    void showResetDevice();

    void showUpdateDeviceFirmware();

    void showSetScreenTime();

    void showSetScreenLight();

    void showTakePic();

    void showResetSuccess();

    void showScreenTime(int time);

    void showFindBandView();

    void showDeviceSettings(DeviceSettings settings);

    void showDeviceAlarmStatus(boolean on);

    void showShowLightScreenStatus(boolean on);

    void showDNDSettings();

    void showHeartRateSettings();

    void showDNDStatus(boolean status);

    void showDeviceName(String deviceName);

    void showLostWarningState(boolean status);

    void showBluetoothDisabled();

    void showModifyScreenOrientationFailed();

    void showWaitingDeviceSyncCompleted();

    void resetScreenOrientationSwitch();
}
