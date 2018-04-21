package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

/**
 * Created by ivan on 17-5-23.
 */

public interface UpdateFirmwareView extends BaseView {

    void showNetworkUnavailable();

    void showCheckingVersion();

    void showNoNewVersion();

    void showCheckVersionFailed();

    void showUpdateFirmwareSuccess();

    void showDeviceDisconnected();

    void showHasNewVersion();

    void showCurrentVersion(String version);

    void showAccessServerFrequent();

    void showFirmwareUpgradeProgress(int progress);

    void showServerBusy();

    void showDownloadFirmwareFail();

    void showUpgradeFailed();

    void showDeviceType(int deviceType);

    void showDeviceSyncing();

    void showLowBatteryToUpgrade();

    void showJoinBetaState(boolean state);

    void showJoinBeta(boolean success);

    void showOutBeta(boolean success);

    void disableJoinBetaButton();

    void showUploadJoinBetaError();

    void showObtainsBatteryFailed();

    void showPatchVersion(String patchVersion);

    void showObtainsFirmwareVersionFailed();

    void showOTAServiceNotInit();
}
