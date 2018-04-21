package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-4-12.
 */

public interface MainView extends BaseView {

    void networkUnavailable();

    void loginSuccess();

    void loginFailed();

    void bleNotSupport();

    void bluetoothUnavailable();

    void startWarning();

    void stopWarning();

    void showDeviceSyncing();

    void showDeviceDisconnected();

    void showSilenceCheckFirmware(int event);

    void showSilenceDownloadFirmwareProgress(int progress);

    void showFOTAServiceNotInit();
}
