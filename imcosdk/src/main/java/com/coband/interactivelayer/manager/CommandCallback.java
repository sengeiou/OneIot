package com.coband.interactivelayer.manager;


import com.coband.dfu.network.bean.AllFwResultBean;
import com.coband.interactivelayer.bean.AlarmClockBean;
import com.coband.interactivelayer.bean.FactorySensorPacket;
import com.coband.interactivelayer.bean.SleepPacket;
import com.coband.interactivelayer.bean.SportPacket;
import com.coband.interactivelayer.bean.BloodPressurePacket;
import com.coband.interactivelayer.bean.FunctionsBean;

import java.util.ArrayList;

public class CommandCallback {


    /**
     * Callback indicating when gatt connected/disconnected to/from a remote device
     *
     * @param status : true is connect, false is disconnect
     */
    public void onConnectionStateChange(final boolean status) {
    }


    /**
     * Callback indicating when login in to a wristband
     * Callback for {@link CommandManager#setLongSit}
     * 　Recommended use　{@link ConnectCallback#connectStatus}　to processing login status
     *
     * @param state {@link CommandManager.LoginState}
     */
    public void onLoginStateChange(@CommandManager.LoginState final int state) {
    }


    /**
     * Callback reporting a {@link CommandManager.ErrorCode} during the connection of a Band device
     * by the {@link ConnectManager#scanLeDevice} function.
     * <p>
     * 　Recommended use　{@link ConnectCallback#connectStatus} to
     * processing error status during　connect Band device
     *
     * @param error error code
     */
    public void onError(@CommandManager.ErrorCode final int error) {
    }


    /**
     * Callback indicating a sport data receive.
     */
    public void onSportDataReceivedIndication(SportPacket sportPacket) {

    }


    /**
     * Callback a history sport data receive.
     */
    public void onSportDataCmdHistorySyncEnd(SportPacket sportPacket) {
    }


    /**
     * Callback a today sport data receive.
     * add by mai
     */
    public void onTodaySumSportDataReceived(long totalStep, long totalCalories, long totalDistance) {
    }


    /**
     * Callback indicating a sleep data receive.
     */
    public void onSleepDataReceivedIndication(SleepPacket packet) {
    }


    /**
     * Callback indicating a device cancel hrp read.
     */
    public void onDeviceCancelSingleHrpRead() {
    }


    /**
     * callback when sync data {@link CommandManager#syncHistoryDataRequest} end
     */
    public void onHistoryDataSyncEnd() {
    }


    /**
     * Callback indicating a real time hrp data receive after a request
     * by the {@link CommandManager#readHrpValue}　function
     *
     * @param minutes  the minutes for the heart rate value
     * @param hrpValue the heart rate value
     */
    public void onHrpDataReceivedIndication(int minutes, int hrpValue) {
    }


    /**
     * Callback indicating a history hrp data receive after a request
     * by the {@link CommandManager#syncHistoryDataRequest}　function
     *
     * @param timestamp the timestamp for the heart rate value
     * @param minutes   the minutes for the heart rate value
     * @param hrpValue  the heart rate value
     */
    public void onHistoryHrpDataReceivedIndication(long timestamp, int minutes, int hrpValue) {
    }


    /**
     * Callback indicating　the state for `periodic heart rate test`　after a setting
     * by the {@link CommandManager#setContinueHrp}　function.
     *
     * @param enable   whether it is open
     * @param interval Measurement period
     */
    public void onHrpContinueParamRsp(boolean enable, int interval) {
    }


    /**
     * Callback reporting a list of currently existing alarms after query
     * by the {@link CommandManager#sendClocksSyncRequest} function.
     *
     * @param clockPackets the receive alarm data packet
     */
    public void onAlarmsDataReceived(ArrayList<AlarmClockBean> clockPackets) {
    }


    /**
     * Callback reporting current notify mode after query
     * by the {@link CommandManager#sendNotifyModeRequest} function.
     *
     * @param data the current notify mode
     *             phone call : {@link CommandManager.NotifyState#NOTIFY_SWITCH_SETTING_CALL}
     *             QQ : {@link CommandManager.NotifyState#NOTIFY_SWITCH_SETTING_QQ}
     *             wechat  : {@link CommandManager.NotifyState#NOTIFY_SWITCH_SETTING_WECHAT}
     *             message : {@link CommandManager.NotifyState#NOTIFY_SWITCH_SETTING_MESSAGE}
     */
    public void onNotifyModeSettingReceived(@CommandManager.NotifyState int data) {
    }


    /**
     * Callback reporting the status of `long sit` after query
     * by the {@link CommandManager#sendLongSitRequest(SendCommandCallback)} function.
     *
     * @param enable the current long sit mode
     *               true is enable long sit remind
     *               false is disable long sit remind
     */
    public void onLongSitSettingReceived(boolean enable) {
    }


    /**
     * Callback reporting the status of `Turn Over Wrist Setting` after query
     * by the {@link CommandManager#sendTurnOverWristRequest} function.
     *
     * @param status 　Turn Over Wrist setting
     *               true is enable `Turn Over Wrist Setting`
     *               false is disable `Turn Over Wrist Setting`
     */
    public void onTurnOverWristSettingReceived(final boolean status) {
    }


    /**
     * Callback when user Shake the band　after a request
     * by the {@link CommandManager#sendCameraControlCommand} function
     */
    public void onTakePhotoRsp() {
    }


    /**
     * Callback indicating a fac sensor data received after a request
     * by the {@link CommandManager#sendEnableFacSensorDataRequest} function
     *
     * @param data the receive sensor data
     */
    public void onFacSensorDataReceived(FactorySensorPacket data) {
    }


    /**
     * Callback reporting the current firmware version code after query
     * by the {@link CommandManager#readDfuVersion} function
     *
     * @param firmwareVersion firmware version code
     * @param patchVersion    patch version value
     */
    public void onVersionRead(int firmwareVersion, int patchVersion) {
    }


    /**
     * Callback indicating current firmware version is last after query
     * by the {@link CommandManager#checkNewFirmwareVersion} function
     *
     * @param code    code != 0，Did not find a new firmware, or the server side of the error,
     *                or parameters wrong, the specific reference to the following error code:
     *                CheckInNoNewVersion = 40200
     *                ErrNumCheckInFailed = 40201
     *                ErrNumCheckInWrongParameter = 40202
     *                ErrNumCheckInResourceNofound = 40203
     * @param message notes
     */
    public void onNoNewVersion(int code, String message) {
    }


    /**
     * Callback indicating a new firmware version after query
     * by the {@link CommandManager#checkNewFirmwareVersion} function
     *
     * @param description version description
     * @param version     version code
     * @param path  Used for silent upgrades{@link CommandManager#checkLastFirmware}. other is null
     *
     */
    public void onHasNewVersion(String description, String version, String path) {
    }


    /**
     * just for test
     *
     * @param firmwareList
     */
    @Deprecated
    public void allFWVersion(ArrayList<AllFwResultBean.PayloadBean> firmwareList) {
    }


    /**
     * Callback reporting an error code that has failed during　the query for a new firmware
     * by the {@link CommandManager#checkNewFirmwareVersion} function
     *
     * @param e         error exception
     * @param errorCode error code
     *                  app error :  {@link CommandManager.CheckFirmwareUpdatesError#CHECK_VERSION_ERROR}
     *                  patch error :  {@link CommandManager.CheckFirmwareUpdatesError#CHECK_PATCH_ERROR}
     *                  download error :  {@link CommandManager.CheckFirmwareUpdatesError#DOWNLOAD_ERROR}
     */
    public void getNewFirmwareError(Throwable e,
                                    @CommandManager.CheckFirmwareUpdatesError int errorCode) {
    }


    /**
     * Callback indicating the progress of downloading new firmware when calling
     * by the {@link CommandManager#downloadFirmware()} function
     *
     * @param progressRate Download the progress of the firmware , Ranges from 0 to 100
     */
    public void downloadProgress(int progressRate) {
    }


    /**
     * Callback reporting the storage address of the download firmware when calling
     * by the {@link CommandManager#downloadFirmware} function
     *
     * @param fwPath the storage address of the download firmware
     */
    public void downloadComplete(String fwPath) {
    }


    /**
     * Callback reporting a name of current device after query
     * by the {@link CommandManager#getDeviceName} function
     *
     * @param deviceName Current device name
     */
    public void onNameRead(final String deviceName) {
    }


    /**
     * Callback reporting a battery value after query
     * by the {@link CommandManager#readBatteryLevel} function.
     *
     * @param value battery level value
     */
    public void onBatteryRead(int value) {
    }


    /**
     * Callback indicating value change from battery.
     *
     * @param value battery level value
     */
    public void onBatteryChange(int value) {
    }


    /**
     * Callback received the measured blood pressure value after calling
     * the {@link CommandManager#readBPValue(SendCommandCallback)} function or the
     * {@link CommandManager#syncHistoryDataRequest(SendCommandCallback)} function
     * <p>
     * <p>
     * Real time value :  {@link CommandManager#readBPValue(SendCommandCallback)}
     * History value : {@link CommandManager#syncHistoryDataRequest(SendCommandCallback)}.
     */
    public void onBloodPressureDataReceived(ArrayList<BloodPressurePacket> bpps) {
    }


    /**
     * Callback when the blood pressure measurement is stopped
     * <p>
     * You can start the blood pressure measurement
     * by calling {@link CommandManager#readBPValue(SendCommandCallback)} function
     */
    public void onBloodPressureMeasurementStopped() {
    }


    @Deprecated
    public void onLinkLossValueReceive(boolean value) {
    }


    /**
     * Callback reporting the display status of the current screen when query
     * by the {@link CommandManager#sendDisplaySwitchRequest} function
     *
     * @param status 　current screen status
     *               VERTICAL : {@link CommandManager.ScreenState#PORTRAIT}
     *               HORIZONTAL : {@link CommandManager.ScreenState#LANDSCAPE}
     */
    public void onDisplaySwitchReturn(@CommandManager.ScreenState int status) {
    }


    /**
     * Callback reporting the functions supported by the current firmware when query
     * by the {@link ConnectManager#scanLeDevice} function.
     *
     * @param functions 　the functions supported by the current firmware
     */
    public void onFunctions(FunctionsBean functions) {
    }


    /**
     * Callback indicate log sync start.
     *
     * @param logLength the log total length
     */
    public void onLogCmdStart(final long logLength) {
    }


    /**
     * Callback indicate log sync end.
     */
    public void onLogCmdEnd() {
    }


    /**
     * Callback indicate log data. just for test
     *
     * @param log receive log data
     */
    public void onLogCmdRsp(String log) {
    }
}
