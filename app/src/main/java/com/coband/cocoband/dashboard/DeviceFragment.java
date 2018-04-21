package com.coband.cocoband.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coband.cocoband.BaseLazyFragment;
import com.coband.iot.ConfigurationDevice;
import com.coband.iot.FileOps;
import com.coband.iot.SCCtlOps;
import com.coband.iot.Wifi;
import com.coband.watchassistant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.realtek.simpleconfiglib.SCLibrary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.realtek.simpleconfiglib.SCNetworkOps.IntegerLE2IPStr;

/**
 * Created by ivan on 4/21/18.
 */

public class DeviceFragment extends BaseLazyFragment {
    public static final String TAG = "DeviceFragment";
    private static final int CONFIGTIMEOUT = 120000;
    private AlertDialog.Builder APList_builder;
    private boolean WifiConnected;
    private List<ScanResult> mScanResults;
    private List<HashMap<String, Object>> wifiArrayList = new ArrayList<HashMap<String, Object>>();
    private int APNumber = 100;
    private ConfigurationDevice.DeviceInfo[] APInfo;

    private SCLibrary SCLib = new SCLibrary();
    private FileOps fileOps = new FileOps();
    private ConfigurationDevice.DeviceInfo[] configuredDevices;
    private static final int deviceNumber = 32;
    private ConfigurationDevice.DeviceInfo SelectedAPInfo;

    @BindView(R.id.listView1)
    ListView listView;

    private ConfigurationDevice.DeviceInfo[] deviceList;
    private AlertDialog APList_alert;
    private int g_SingleChoiceID;
    private String AP_password;
    private boolean ConnectAPProFlag;
    private ProgressDialog pd;
    private Thread connectAPThread;
    private ScanResult g_ScanResult;
    private static final String pwfileName = "preAPInfoFile";
    private boolean TimesupFlag_cfg;
    private LayoutInflater factory;
    private View wifiPW_EntryView;
    private EditText edittxt_PINcode;
    private String pinCodeText;
    private String PINGet;
    private String PINSet;
    private String presave_pinCode;
    private boolean ConfigureAPProFlag;
    private boolean ShowCfgSteptwo;
    private static final String aboutMsg = "uncheck device if any unwanted";
    private static final String pinfileName = "prePINFile";
    private String[] delConfirmIP;
    private String CurrentControlIP;
    private boolean DiscovEnable;
    private SimpleAdapter adapter_deviceInfo;
    private boolean isScanFinish;
    private boolean uncheckDevice;
    private Thread backgroundThread;

    // handler for the background updating
    Handler progressHandler = new Handler() {
        public void handleMessage(Message msg) {
            pd.incrementProgressBy(1);
        }
    };
    private String defaultPINcode = "";
    private int cfgDeviceNumber = 1;
    private String allSSID = "";
    private String ssid_name = "";
    private WifiManager mWifiManager;
    private int CurrentItem;
    private ArrayList<HashMap<String, Object>> DevInfo;
    final String[] CtrlListStr = {"Rename", "Remove Device", "Connect to other AP"};
    private boolean isControlSingleDevice;
    private boolean TimesupFlag_rename;
    private boolean isCancel_ctl_newAP;
    private boolean isConnectNewAP;
    private boolean isDeletedDevice;
    private boolean is_getIP_finish;
    private ConnectivityManager mConnManager;
    private LayoutInflater mLayoutInflater;
    private static final int discoveryTimeout = 3000;

    public enum SECURITY_TYPE {
        SECURITY_NONE, SECURITY_WEP, SECURITY_PSK, SECURITY_EAP
    }

    @OnClick({R.id.btn_scan, R.id.btn_sc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                scanDevice();
                break;
            case R.id.btn_sc:
                configNewDevice_OnClick(null);
                break;
        }
    }


    static {
        System.loadLibrary("simpleconfiglib");
    }

    public static DeviceFragment newInstance() {
        return new DeviceFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.iot_fragment_device;
    }

    @Override
    protected void onFragmentResume() {

    }

    @Override
    protected void init() {
        setupAp();
        SCLib.rtk_sc_init();
        SCLib.TreadMsgHandler = new MsgHandler();

        // wifi manager init
        SCLib.WifiInit(mActivity);
        fileOps.SetKey(SCLib.WifiGetMacStr());
        mWifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        mConnManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        mLayoutInflater = mActivity.getLayoutInflater();
    }

    private Runnable Cfg_changeMessage = new Runnable() {
        @Override
        public void run() {
            // Log.v(TAG, strCharacters);
            pd.setMessage("Waiting for the device");
        }
    };

    /**
     * Handler class to receive send/receive message
     */
    private class MsgHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // Log.d(TAG, "msg.what: " + msg.what);
            switch (msg.what) {

                case SCCtlOps.Flag.CfgSuccessACKFinish: // Not Showable
                    Log.d("MsgHandler", "Config Finish");

                    SCLib.rtk_sc_stop();
                    TimesupFlag_cfg = true;
                    break;
                case ~SCCtlOps.Flag.CfgSuccessACK:// Config Timeout
                    Log.e("MsgHandler", "Config Timeout");
                    SCLib.rtk_sc_stop();
                    TimesupFlag_cfg = true;
                    break;
                case SCCtlOps.Flag.CfgSuccessACK: // Not Showable
                    Log.d("MsgHandler", "Config SuccessACK");
                    // Toast.makeText(getApplication(), "Get CfgSuccessACK",
                    // Toast.LENGTH_SHORT).show();
                    // Log.d(TAG, "CurrentView: " + CurrentView);

                    mActivity.runOnUiThread(Cfg_changeMessage);

                    break;
                case SCCtlOps.Flag.DiscoverACK:
                    // Log.d("MsgHandler","DiscoverACK");
                    SCCtlOps.handle_discover_ack((byte[]) msg.obj);
                    if (SCCtlOps.DiscoveredNew) {
                        show_discoverDevice();
                    }

                    break;
                case ~SCCtlOps.Flag.DiscoverACK:
                    // Log.d("MsgHandler","Discovery timeout.");
                    DiscovEnable = false;
                    break;
                case SCCtlOps.Flag.DelProfACK:

                    // Toast.makeText(getApplication(), "Get DelProfACK",
                    // Toast.LENGTH_SHORT).show();

                    // Log.d("MsgHandler","Del Profile ACK");
                    rtk_sc_send_confirm_packet(SCCtlOps.Flag.DelProf);
                    isDeletedDevice = true;

                    SCCtlOps.rtk_sc_control_reset();

                    DiscovEnable = true;

                    new Thread(new Runnable() {
                        public void run() {
                            Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                            byte[] DiscovCmdBuf = SCCtlOps
                                    .rtk_sc_gen_discover_packet(SCLib
                                            .rtk_sc_get_default_pin());

                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }

                            SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf,
                                    "255.255.255.255");
                            // Update Status
                            Message msg = Message.obtain();
                            msg.obj = null;
                            msg.what = ~SCCtlOps.Flag.DiscoverACK; // timeout
                            SCLib.TreadMsgHandler.sendMessage(msg);
                        }
                    }).start();

                    show_discoverDevice();

                    break;
                case SCCtlOps.Flag.RenameDevACK:
                    // Toast.makeText(getApplication(), "Get RenameDevACK",
                    // Toast.LENGTH_SHORT).show();

                    // Log.d("MsgHandler","Rename Device ACK");
                    rtk_sc_send_confirm_packet(SCCtlOps.Flag.RenameDev);

                    SCCtlOps.rtk_sc_control_reset();
                    TimesupFlag_rename = true;
                    break;
                case SCCtlOps.Flag.NewAPACK:
                    rtk_sc_send_confirm_packet(SCCtlOps.Flag.NewAPACK);
                    SCCtlOps.rtk_sc_control_reset();
                    isConnectNewAP = true;
                    break;
                case SCCtlOps.Flag_Other.SiteSurveyFinish:
                    Log.d(TAG, "SiteSurveyFinish!!!!");
                    allSSID = (String) msg.obj;
                    break;
                default:
                    // Log.d("MsgHandler","SC unknown MsgHandler");
                    break;
            }
        }
    }

    private void rtk_sc_send_confirm_packet(final int flag) {
        new Thread(new Runnable() {
            String pin;

            public void run() {
                byte[] buf;
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

                pin = PINSet;

                // Log.d(TAG, "Comfirm PIN: " + pin);
                // Log.d(TAG, "Comfirm IP: " + CurrentControlIP);

                // for confirm to delete
                if (flag == SCCtlOps.Flag.DelProf && delConfirmIP != null
                        && !isControlSingleDevice) {
                    int DelArraySize = delConfirmIP.length;

                    for (int i = 0; i < DelArraySize; i++) {
                        if (delConfirmIP[i].length() > 0) {
                            // Log.d(TAG,"DelProf rtk_sc_send_confirm_packet :"+delConfirmIP[i]);
                            buf = SCCtlOps.rtk_sc_gen_control_confirm_packet(
                                    flag, SCLib.rtk_sc_get_default_pin(), pin);
                            if (delConfirmIP[i].equals("0.0.0.0")) {
                                // Toast.makeText(MainActivity.this,
                                // "rtk_sc_send_confirm_packet1:"+CurrentControlIP,
                                // Toast.LENGTH_SHORT).show();
                                // Log.d(TAG,"rtk_sc_send_confirm_packet1: "+CurrentControlIP);
                                for (int retry = 0; retry < 5; retry++) {
                                    try {
                                        Thread.sleep(1);
                                        SCLib.rtk_sc_send_control_packet(buf, CurrentControlIP);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {

                                for (int retry = 0; retry < 5; retry++) {
                                    try {
                                        Thread.sleep(1);

                                        SCLib.rtk_sc_send_control_packet(buf,
                                                delConfirmIP[i]);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } else {

                    buf = SCCtlOps.rtk_sc_gen_control_confirm_packet(flag, SCLib.rtk_sc_get_default_pin(), pin);
                    for (int retry = 0; retry < 2; retry++) {
                        try {
                            Thread.sleep(10);
                            SCLib.rtk_sc_send_control_packet(buf, CurrentControlIP);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }).start();
    }

    private List<? extends Map<String, ?>> getData_Device() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        String tmp = "";
        int i = 0;
        DevInfo = new ArrayList<HashMap<String, Object>>();
        SCCtlOps.rtk_sc_get_discovered_dev_info(DevInfo);
        for (i = 0; i < SCCtlOps.rtk_sc_get_discovered_dev_num(); i++) {
            map = new HashMap<String, Object>();
            if (DevInfo.get(i).get("Name") == null) {

                map.put("title", DevInfo.get(i).get("MAC"));
                map.put("info", DevInfo.get(i).get("MAC") + "   "
                        + DevInfo.get(i).get("Status"));

            } else {
                // Log.d(TAG,"getData_Device "+(String)
                // DevInfo.get(i).get("Name"));
                map.put("title", DevInfo.get(i).get("Name"));
                map.put("info", DevInfo.get(i).get("MAC") + "   "
                        + DevInfo.get(i).get("Status"));
            }

            tmp = (String) DevInfo.get(i).get("IP");
            if (DevInfo.get(i).get("IP") != null && tmp.length() > 0)
                list.add(map);

        }

        isScanFinish = true;

        return list;
    }

    private boolean show_discoverDevice() {
        adapter_deviceInfo = new SimpleAdapter(mActivity, getData_Device(),
                R.layout.device_list, new String[]{"title", "info"},
                new int[]{R.id.title, R.id.info});
        listView.setAdapter(adapter_deviceInfo);
        if (adapter_deviceInfo.getCount() > 0)
            setListViewHeightBasedOnChildren(listView);
        else if (adapter_deviceInfo.getCount() == 0) {
            listView.setAdapter(null);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int itemId, long arg3) {

                boolean isCtlPIN = false;
                String deviceType = "";
                ListView listView_parent = (ListView) parent;
                listView_parent.setTag(view);

                String[] tmp = listView_parent.getItemAtPosition(itemId)
                        .toString().split("title=");
                String tmp1 = tmp[1];
                String[] deviceName = tmp1.split(",");

                tmp = listView_parent.getItemAtPosition(itemId).toString()
                        .split("info=");
                String macAddress = tmp[1];
                macAddress = macAddress.replaceFirst("\\}", "");
                macAddress = macAddress.substring(0, 17);
                CurrentItem = itemId;

                Log.d(TAG, "listView choose " + deviceName[0] + " "
                        + macAddress);

                SCCtlOps.rtk_sc_get_discovered_dev_info(DevInfo);

                if (isScanFinish) {
                    // Log.e(TAG,"DevInfo.get(CurrentItem): "
                    // +(String)DevInfo.get(CurrentItem).get("Name"));
                    CurrentControlIP = (String) DevInfo.get(CurrentItem).get("IP");

                    deviceType = (String) DevInfo.get(CurrentItem).get("Type");

                    if (DevInfo.get(CurrentItem).get("PIN") != null) {
                        isCtlPIN = (Boolean) DevInfo.get(CurrentItem).get("PIN");
                    }

                    if (!isCtlPIN)
                        CtlDefaultPINcode(deviceName[0], (String) DevInfo.get(CurrentItem).get("MAC"), deviceType);
                    else
                        ControlPINcode(deviceName[0], (String) DevInfo.get(CurrentItem).get("MAC"), deviceType);

                } else {
                    // check list info after first confirm device
                    getkDeviceInformation(deviceName[0], macAddress);
                }
            }
        });
        return true;
    }

    private boolean checkDeviceInformation(String macAddress) {
        boolean ret = false;
        int deviceNum = 0;
        String tmp_mac = "";

        if (macAddress.length() == 0 || macAddress == null)
            return ret;

        SCLib.rtk_sc_get_connected_sta_info(DevInfo);
        deviceNum = DevInfo.size();

        for (int i = 0; i < deviceNum; i++) {

            tmp_mac = (String) DevInfo.get(i).get("MAC");
            // Log.d(TAG,"macAddress: "+macAddress+" V.S. "+tmp_mac);
            if (macAddress.equals(tmp_mac)) {
                // Log.d(TAG,"DevInfo: " +(String)DevInfo.get(i).get("Name")
                // +" "+(String)DevInfo.get(i).get("IP"));
                CurrentControlIP = (String) DevInfo.get(i).get("IP");
                if (CurrentControlIP.length() == 0
                        || CurrentControlIP.equals("0.0.0.0"))
                    continue;
                ret = true;
                break;
            }
        }

        return ret;
    }

    private void getkDeviceInformation(final String device,
                                       final String macAddress) {
        Log.d(TAG, "getkDeviceInformation macAddress :" + macAddress);

        is_getIP_finish = false;

        pd = new ProgressDialog(mActivity);

        pd.setTitle("Waiting");

        pd.setMessage("Getting " + device + " information...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

                boolean isCtlPIN = false;
                int deviceNum = -1;
                int iDevice = -1;
                int i = -1;
                String tmp_mac = "";
                byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib
                        .rtk_sc_get_default_pin());
                long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();
                while (!is_getIP_finish && (endTime - startTime) < 30000) {

                    try {
                        Thread.sleep(500);
                        SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf,
                                "255.255.255.255");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    endTime = System.currentTimeMillis();

                    // check the ip of device
                    if (checkDeviceInformation(macAddress)) {
                        is_getIP_finish = true;
                        break;
                    }
                }
                handler_pd.sendEmptyMessage(0);

                // Update Status
                Message msg = Message.obtain();
                msg.obj = null;
                msg.what = ~SCCtlOps.Flag.DiscoverACK; // timeout
                SCLib.TreadMsgHandler.sendMessage(msg);

                SCLib.rtk_sc_get_connected_sta_info(DevInfo);
                deviceNum = DevInfo.size();
                // Log.d(TAG,"deviceNum :" + deviceNum);

                if (is_getIP_finish) {

                    for (i = 0; i < deviceNum; i++) {
                        tmp_mac = (String) DevInfo.get(i).get("MAC");
                        // Log.d(TAG,"tmp_mac :" + tmp_mac);
                        if (macAddress.equals(tmp_mac)) {
                            iDevice = i;
                            break;
                        }
                    }

                    // Log.d(TAG,"iDevice :" + iDevice);

                    if (iDevice >= 0) {

                        if (DevInfo.get(iDevice).get("PIN") != null) {
                            isCtlPIN = (Boolean) DevInfo.get(iDevice).get("PIN");
                        }

                        final boolean isDevicePIN = isCtlPIN;
                        final String deviceName = (String) DevInfo.get(iDevice).get("Name");
                        final String deviceMAC = (String) DevInfo.get(iDevice).get("MAC");
                        final String deviceType = (String) DevInfo.get(CurrentItem).get("Type");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Setting: " + deviceName + " " + deviceMAC + " " + isDevicePIN);

                                if (!isDevicePIN)
                                    CtlDefaultPINcode(deviceName, deviceMAC, deviceType);
                                else
                                    ControlPINcode(deviceName, deviceMAC, deviceType);
                            }
                        });
                    }

                }

            }
        }).start();
    }

    public void ControlPINcode(final String deviceName, final String macAddress, final String deviceType) {

        factory = LayoutInflater.from(mActivity);
        wifiPW_EntryView = factory
                .inflate(R.layout.confirm_pincode_entry, null);
        edittxt_PINcode = (EditText) wifiPW_EntryView
                .findViewById(R.id.id_ap_password);

        edittxt_PINcode.setText("", TextView.BufferType.EDITABLE);
        edittxt_PINcode.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittxt_PINcode.setTextSize(20);

        // try to get success pin code by pinfilename
        String content = "";
        byte[] buff = new byte[256]; // input stream buffer
        try {
            FileInputStream reader = mActivity.openFileInput(pinfileName);
            while ((reader.read(buff)) != -1) {
                content += new String(buff).trim();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "pin code FileNotFoundException: " + content);
        } catch (IOException e) {
            Log.e(TAG, "pin code IOException: " + content);
        }
        // Log.d(TAG,"PIN Code info: "+content);

        // read pin
        if (content.length() > 0) {
            String[] DeviceItem = content.split(";");
            int itemNumber = DeviceItem.length;

            for (int i = 0; i < itemNumber; i++) {
                String[] array = DeviceItem[i].split(",");
                if (macAddress.equals(array[0])) {// bssid is same
                    edittxt_PINcode.setText(array[1],
                            TextView.BufferType.EDITABLE);
                    break;
                }
            }
        }

        pinCodeText = edittxt_PINcode.getText().toString();
        PINGet = edittxt_PINcode.getText().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setCancelable(false);
        alert.setTitle("Key in " + deviceName + " PIN code");
        alert.setMessage("The PIN code will be display on device if the PIN code is exist.");
        alert.setCancelable(false);
        alert.setView(wifiPW_EntryView);
        alert.setPositiveButton("Cancel", null);
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (edittxt_PINcode.getText().toString().length() <= 0) {
                    Toast.makeText(mActivity,
                            "Warning: The PIN code is empty!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Log.d(TAG,"pinCodeText: "+pinCodeText);

                if (edittxt_PINcode.getText().toString().length() > 0) {
                    pinCodeText = edittxt_PINcode.getText().toString();
                    PINGet = edittxt_PINcode.getText().toString();

                    byte[] pinget = PINGet.getBytes();
                    byte[] pinset;
                    if (pinget.length > 0) {
                        if (pinget.length < 8) {
                            pinset = new byte[8];
                            System.arraycopy(pinget, 0, pinset, 0,
                                    pinget.length);
                            for (int i = pinget.length; i < 8; i++) {
                                pinset[i] = '0';
                            }
                        } else if (pinget.length >= 8 && pinget.length <= 64) {
                            pinset = new byte[pinget.length];
                            System.arraycopy(pinget, 0, pinset, 0,
                                    pinget.length);
                        } else {
                            pinset = new byte[64];
                            System.arraycopy(pinget, 0, pinset, 0, 64);
                        }
                        PINSet = new String(pinset);
                    } else {
                        PINSet = new String(pinget);
                    }
                    fileOps.UpdateCfgPinFile((PINSet != null && PINSet.length() > 0) ? PINSet
                            : "null");
                }

                AlertDialog.Builder dialogaction = new AlertDialog.Builder(mActivity);
                // Get the layout inflater
                dialogaction.setCancelable(false);
                dialogaction.setTitle("Setting action");
                dialogaction.setIcon(R.drawable.settings);

                if (deviceType.equals("Multi_Media")) {
                    dialogaction.setItems(CtrlListStr, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                rename_OnClick(deviceName);
                            } else if (which == 1) {
                                remove_OnClick();
                            } else if (which == 2) {
                                SwitchOtherAP_OnClick(deviceName);
                            }
                        }
                    });
                } else {
                    dialogaction.setNeutralButton("Remove device",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    remove_OnClick();

                                }
                            });

                    dialogaction.setNegativeButton("Rename",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    rename_OnClick(deviceName);
                                }
                            });
                }


                dialogaction.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                dialogaction.show();

            }

        });
        alert.show();
    }

    public void CtlDefaultPINcode(final String deviceName, final String macAddress, final String deviceType) {

        pinCodeText = SCLib.rtk_sc_get_default_pin();
        PINSet = SCLib.rtk_sc_get_default_pin();

        AlertDialog.Builder dialogaction = new AlertDialog.Builder(mActivity);
        // Get the layout inflater
        dialogaction.setCancelable(false);
        dialogaction.setTitle("Setting action");
        dialogaction.setIcon(R.drawable.settings);

        if (deviceType.equals("Multi_Media")) {
            dialogaction.setItems(CtrlListStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        rename_OnClick(deviceName);
                    } else if (which == 1) {
                        remove_OnClick();
                    } else if (which == 2) {
                        SwitchOtherAP_OnClick(deviceName);
                    }
                }
            });
        } else {
            dialogaction.setNeutralButton("Remove device",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            remove_OnClick();

                        }
                    });

            dialogaction.setNegativeButton("Rename",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            rename_OnClick(deviceName);
                        }
                    });
        }


        dialogaction.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialogaction.show();
    }

    public void remove_OnClick() {
        pd = new ProgressDialog(mActivity);
        pd.setTitle("Device Removing ");
        pd.setMessage("Please wait...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DiscovEnable = false;
                        isDeletedDevice = false;
                    }
                });
        pd.show();

        isControlSingleDevice = true;
        SendCtlDevPacket(SCCtlOps.Flag.DelProf, pinCodeText, CurrentControlIP, null);

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(20000);

                    isControlSingleDevice = false;

                    if (isDeletedDevice) {
                        SCCtlOps.rtk_sc_control_reset();
                        DiscovEnable = true;
                        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                        byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib.rtk_sc_get_default_pin());

                        for (int i = 0; i < 15; i++) {
                            Thread.sleep(200);
                            SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf, "255.255.255.255");
                        }

                        // Update Status
                        Message msg = Message.obtain();
                        msg.obj = null;
                        msg.what = ~SCCtlOps.Flag.DiscoverACK; // timeout
                        SCLib.TreadMsgHandler.sendMessage(msg);

                        mActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                handler_pd.sendEmptyMessage(0);
                                show_discoverDevice();
                            }
                        });

                    } else {
                        mActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                handler_pd.sendEmptyMessage(0);
                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                        mActivity);
                                alert.setCancelable(false);
                                // switch password input type
                                alert.setTitle("Remove Device Failed");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", null);
                                alert.show();
                                isDeletedDevice = false;
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();

    }

    private static boolean isAllASCII(String input) {
        boolean isASCII = true;
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 0x7F || c < 0x20) {
                isASCII = false;
                break;
            }
        }
        return isASCII;
    }


    public void SwitchOtherAP_OnClick(String deviceName) {
        LayoutInflater factory = LayoutInflater.from(mActivity);
        final View wifi_New_EntryView = factory.inflate(R.layout.wifi_ssid_password_entry, null);
        final EditText edittxt_new_ap_ssid = (EditText) wifi_New_EntryView.findViewById(R.id.id_new_ap_ssid);
        final EditText edittxt_new_ap_pwd = (EditText) wifi_New_EntryView.findViewById(R.id.id_new_ap_password);
        CheckBox password_checkbox;
        password_checkbox = (CheckBox) wifi_New_EntryView.findViewById(R.id.checkBox_password);

        edittxt_new_ap_ssid.setText("", TextView.BufferType.EDITABLE);
        edittxt_new_ap_pwd.setText("", TextView.BufferType.EDITABLE);

        password_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(
                    CompoundButton buttonView,
                    boolean isChecked) {

                if (isChecked)
                    edittxt_new_ap_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                else
                    edittxt_new_ap_pwd.setInputType(129);
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setCancelable(false);
        alert.setTitle("New AP Configuration");
        alert.setCancelable(false);
        alert.setView(wifi_New_EntryView);
        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton("Connect", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                String strSSID = edittxt_new_ap_ssid.getText().toString();

                if (strSSID.length() > 0 && isAllASCII(strSSID)) {
                    ssid_name = strSSID;
                } else {
                    AlertDialog.Builder msgAlert = new AlertDialog.Builder(mActivity);
                    msgAlert.setTitle("Error");
                    msgAlert.setMessage("Please check the SSID!\n");
                    msgAlert.setPositiveButton("OK", null);
                    msgAlert.show();
                    return;
                }

                AP_password = edittxt_new_ap_pwd.getText().toString();

                isConnectNewAP = false;
                isCancel_ctl_newAP = false;
                SendCtlDev_newAP_Packet(SCCtlOps.Flag.NewAPReq, pinCodeText, CurrentControlIP, ssid_name, AP_password);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

								/*Toast toast = Toast.makeText(MainActivity.this, "NEW AP:"+ssid_name+"/"+AP_password,Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 100);
								LinearLayout toastLayout = (LinearLayout) toast.getView();
								TextView toastTV = (TextView) toastLayout.getChildAt(0);
								toastTV.setTextSize(20);
								toast.show();*/

                        pd = new ProgressDialog(mActivity);
                        pd.setTitle("Connecting");
                        pd.setMessage("Please wait...");
                        pd.setIndeterminate(true);
                        pd.setCancelable(false);
                        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isCancel_ctl_newAP = true;
                            }
                        });
                        pd.show();
                    }
                });

                new Thread() {
                    public void run() {
                        int retry = 0;

                        while (!isConnectNewAP && retry++ < 60 && !isCancel_ctl_newAP) {
                            Log.d(TAG, "waiting for new AP connection:" + retry);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        isControlSingleDevice = false;

                        if (isConnectNewAP) {
                            isConnectNewAP = false;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SCCtlOps.rtk_sc_control_reset();
                                    show_discoverDevice();
                                }
                            });

                            DiscovEnable = true;
                            Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                            byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib.rtk_sc_get_default_pin());

                            for (int i = 0; i < 15; i++) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf, "255.255.255.255");
                            }

                            // Update Status
                            Message msg = Message.obtain();
                            msg.obj = null;
                            msg.what = ~SCCtlOps.Flag.DiscoverACK; // timeout
                            SCLib.TreadMsgHandler.sendMessage(msg);

                            mActivity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    handler_pd.sendEmptyMessage(0);
                                    show_discoverDevice();
                                }
                            });

                        } else {
                            if (!isCancel_ctl_newAP) {
                                mActivity.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        handler_pd.sendEmptyMessage(0);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                                mActivity);
                                        alert.setCancelable(false);
                                        // switch password input type
                                        alert.setTitle("Connection Failed");
                                        alert.setCancelable(false);
                                        alert.setPositiveButton("OK", null);
                                        alert.show();
                                        isConnectNewAP = false;
                                    }
                                });
                            }

                        }

                    }
                }.start();
            }

        });

        alert.show();
    }

    public void SendCtlDev_newAP_Packet(final int flag, final String pin,
                                        final String ip, final String SSID, final String pwd) {
        // Log.d(TAG, "ip: " + ip);
        // Log.d(TAG, "pin: " + pin);
        // Log.d(TAG, "name: " + new_name);

        new Thread(new Runnable() {
            int count = 0;

            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                byte[] buf = SCCtlOps.rtk_sc_gen_control_newAP_packet(flag,
                        SCLib.rtk_sc_get_default_pin(), pin, SSID, pwd);

                while (count < 6) {
                    try {
                        Thread.sleep(100);
                        SCLib.rtk_sc_send_control_packet(buf, ip);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count++;
                }
            }
        }).start();
    }


    public void rename_OnClick(String deviceName) {
        LayoutInflater factory = LayoutInflater.from(mActivity);
        final View textEntryView = factory
                .inflate(R.layout.device_rename, null);
        final EditText edit_device_name = (EditText) textEntryView
                .findViewById(R.id.id_device_name);

        edit_device_name.setText(deviceName, TextView.BufferType.EDITABLE);
        edit_device_name.setTextSize(20);

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setTitle("Enter the Device Name:");
        alert.setCancelable(false);
        alert.setView(textEntryView);
        alert.setPositiveButton("Cancel", null);
        alert.setNegativeButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                String new_deviceName = edit_device_name.getText().toString();
                if (new_deviceName.length() > 0) {

                    pd = new ProgressDialog(mActivity);
                    pd.setTitle("Device Renaming ");
                    pd.setMessage("Please wait...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    DiscovEnable = false;
                                    TimesupFlag_rename = false;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
                                    alert.setCancelable(false);
                                    // switch password input type
                                    alert.setTitle("Rename Device Failed");
                                    alert.setCancelable(false);
                                    alert.setPositiveButton("OK", null);
                                    alert.show();
                                }
                            });
                    pd.show();
                    isControlSingleDevice = true;
                    TimesupFlag_rename = false;
                    SendCtlDevPacket(SCCtlOps.Flag.RenameDev, pinCodeText, CurrentControlIP, new_deviceName);

                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(10000);
                                isControlSingleDevice = false;
                                if (!TimesupFlag_rename) {
                                    mActivity.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            handler_pd.sendEmptyMessage(0);

                                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                                    mActivity);
                                            alert.setCancelable(false);
                                            alert.setTitle("Rename Device Failed");
                                            alert.setCancelable(false);
                                            alert.setPositiveButton("OK", null);
                                            alert.show();
                                        }
                                    });
                                } else {

                                    SCCtlOps.rtk_sc_control_reset();
                                    DiscovEnable = true;
                                    Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                                    byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib.rtk_sc_get_default_pin());

                                    for (int i = 0; i < 15; i++) {
                                        Thread.sleep(200);
                                        SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf, "255.255.255.255");
                                    }

                                    // Update Status
                                    Message msg = Message.obtain();
                                    msg.obj = null;
                                    msg.what = ~SCCtlOps.Flag.DiscoverACK; // timeout
                                    SCLib.TreadMsgHandler.sendMessage(msg);

                                    mActivity.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            handler_pd.sendEmptyMessage(0);
                                            show_discoverDevice();
                                        }
                                    });

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } else {
                    showLongToast("Warning: The device name is empty!");
                }
            }

        });
        alert.show();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void setupView() {
    }

    private void setupAp() {
        configuredDevices = new ConfigurationDevice.DeviceInfo[deviceNumber];
        for (int i = 0; i < deviceNumber; i++) {
            configuredDevices[i] = new ConfigurationDevice.DeviceInfo();
            configuredDevices[i].setaliveFlag(1);
            configuredDevices[i].setName("");
            configuredDevices[i].setIP("");
            configuredDevices[i].setmacAdrress("");
            configuredDevices[i].setimg(null);
            configuredDevices[i].setconnectedflag(false);
        }

        APInfo = new ConfigurationDevice.DeviceInfo[APNumber];
        for (int i = 0; i < APNumber; i++) {
            APInfo[i] = new ConfigurationDevice.DeviceInfo();
        }

        SelectedAPInfo = new ConfigurationDevice.DeviceInfo();
        APList_Clear();

        SCCtlOps.ConnectedSSID = null;
        SCCtlOps.ConnectedPasswd = null;
    }


    private void addNetworkPopup() {
        if (APList_alert != null) {
            APList_alert.cancel();
        }

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View addNetworkView = inflater.inflate(R.layout.add_network_content, null);

        final EditText network_name_edit;
        final EditText network_pw_edit;
        final Spinner encrypt_type_spinner;
        final ArrayAdapter<String> encrypt_adapter;
        final String[] encryption_types = {"NONE", "WEP", "WAPI", "WPA-PSK",
                "WPA2-PSK", "WPA_EAP"};

        network_name_edit = (EditText) addNetworkView
                .findViewById(R.id.network_name_edit);
        network_pw_edit = (EditText) addNetworkView
                .findViewById(R.id.id_ap_password);
        encrypt_type_spinner = (Spinner) addNetworkView
                .findViewById(R.id.encrypt_type);
        encrypt_adapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_spinner_item, encryption_types);
        encrypt_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        encrypt_type_spinner.setAdapter(encrypt_adapter);
        encrypt_type_spinner.setOnItemSelectedListener(null);
        encrypt_type_spinner.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("添加网络:")
                .setIcon(R.drawable.ic_dialog_icon)
                .setView(addNetworkView)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ssid_name = network_name_edit.getText().toString();
                        AP_password = network_pw_edit.getText().toString();
                        final String encrypt_type = encrypt_type_spinner
                                .getSelectedItem().toString();
                        SCCtlOps.isHiddenSSID = true;
                        ConnectAPProFlag = true;
                        showConnectingDialog();
                        realConnect(encrypt_type);
                    }
                })
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.show();
    }

    private void realConnect(final String encrypt_type) {
        connectAPThread = new Thread() {
            public void run() {
                String jsonSsidStr = "{" + "\"SSID\":\""
                        + ssid_name + "\"" + ",\"BSSID\":\""
                        + "\"" + ",\"capabilities\":\""
                        + encrypt_type + "[ESS]\""
                        + ",\"level\":" + 0 + ",\"frequency\":"
                        + 0 + "}";

                Log.d(TAG, "jsonSsidStr: " + jsonSsidStr
                        + " pw:" + AP_password);
                Gson gson = new Gson();
                SCCtlOps.reBuiltScanResult = gson.fromJson(
                        jsonSsidStr,
                        new TypeToken<ScanResult>() {
                        }.getType());
                @SuppressWarnings("deprecation")
                int mNumOpenNetworksKept = Settings.Secure.getInt(
                        mActivity.getContentResolver(),
                        Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT,
                        10);
                boolean connResult = false;
                connResult = Wifi.connectToNewNetwork(
                        mActivity, mWifiManager,
                        SCCtlOps.reBuiltScanResult,
                        AP_password, mNumOpenNetworksKept);

                Log.d(TAG, "connResult: " + String.valueOf(connResult));


                String connected_ssid = "";
                int retry = 60;
                do {
                    try {
                        Thread.sleep(500);
                        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                        NetworkInfo mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED
                                || mWifi.isConnected()) {
                            connected_ssid = wifiInfo.getSSID();
                            if (connected_ssid.indexOf("\"") == 0)
                                connected_ssid = connected_ssid.substring(1, connected_ssid.length() - 1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "wifi connect :" + connected_ssid);
                } while (!ssid_name.equals(connected_ssid)
                        && retry-- > 0);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (ConnectAPProFlag) {
                    handler_pd.sendEmptyMessage(0);
                    ConnectAPProFlag = false;
                }

                if (retry > 0) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            configNewDevice_OnClick(null);
                        }
                    });
                } else {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showConnectApErrorDialog();
                        }
                    });
                }
            }
        };
        connectAPThread.start();

    }

    private void showConnectingDialog() {
        pd = new ProgressDialog(mActivity);
        pd.setTitle("连接中");
        pd.setMessage("Please wait...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ConnectAPProFlag = false;
                    }
                });
        pd.show();
    }

    private void showConnectApErrorDialog() {
        AlertDialog.Builder errorAlert = new AlertDialog.Builder(mActivity);
        errorAlert.setTitle("Connect the AP Fail");
        errorAlert.setMessage("Please check the password or other problem.\nYou can go to System Settings/Wi-Fi, select the Wi-Fi network!");
        errorAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });
        errorAlert.show();
    }


    public void configNewDevice_OnClick(View view) {
        int i = 0, j = 0;
        int itmeNum = 0;

        g_SingleChoiceID = -1;

        // check wifi is disable
        if (!mWifiManager.isWifiEnabled()) {
            // OpenWifiPopUp();
            SCLib.WifiOpen();

            do {
                SystemClock.sleep(1000);
                // Log.d(TAG, "Turn on Wi-Fi wait");
            } while (!mWifiManager.isWifiEnabled()); // check again if it is disable

            SCLib.WifiStartScan();
            SystemClock.sleep(2500);
        }

        showLongToast("请选择一个AP");


        try {
            Thread.sleep(800);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        GetAllWifiList();

        fileOps.SetKey(SCLib.WifiGetMacStr());

        APList_builder = new AlertDialog.Builder(mActivity);

        View customTitle = mLayoutInflater.inflate(R.layout.customtitlebar, null);

        APList_builder.setCustomTitle(customTitle);
        APList_builder.setCancelable(false);

        itmeNum = 0;
        for (int num = 0; num < mScanResults.size(); num++) {
            if (num >= APNumber)
                break;
            if (APInfo[num].getaliveFlag() == 1)
                itmeNum++;
        }

        deviceList = new ConfigurationDevice.DeviceInfo[itmeNum];
        // Log.d(TAG, "configNewDevice_OnClick itmeNum:" +
        // String.valueOf(itmeNum));
        for (i = 0; i < itmeNum; i++) {
            if (i < APNumber && APInfo[i] != null) {
                if (APInfo[i].getaliveFlag() == 1) {
                    deviceList[i] = new ConfigurationDevice.DeviceInfo();
                    deviceList[i] = APInfo[i];
                    // Log.d(TAG, itmeNum + " ap ssid: " +
                    // deviceList[i].getName());
                }
            }
        }

        // sort by name
        ConfigurationDevice.DeviceInfo tmpDevice = new ConfigurationDevice.DeviceInfo();
        for (i = 0; i < itmeNum; i++) {
            tmpDevice = deviceList[i];
            // Log.d(TAG, itmeNum + " ap ssid: " + tmpDevice.getName());
            for (j = i - 1; j >= 0
                    && deviceList[j].getName().compareToIgnoreCase(
                    tmpDevice.getName()) > 0; j--) {
                deviceList[j + 1] = deviceList[j];
            }
            deviceList[j + 1] = tmpDevice;

        }

        // set foucs connect item
        int foucsIndex = 0;
        if (WifiConnected) {

//            if (foucsIndex >= 0) {

            for (j = 0; j < itmeNum; j++) {

                if (deviceList[j].getName()
                        .equals(SelectedAPInfo.getName())) {
                    foucsIndex = j;
                    // Log.d(TAG,
                    // "configNewDevice_OnClick() ===foucsIndex"+String.valueOf(foucsIndex));
                    break;
                }
//                }
            }
        } else {
            foucsIndex = 0;
        }

        SCCtlOps.isHiddenSSID = false;

//        final ImageView addNetworkkBtn = (ImageView) customTitle
//                .findViewById(R.id.addNewNetworkBtn);
//        addNetworkkBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                SCCtlOps.addNewNetwork = true;
//
//                addNetworkPopup();
//            }
//
//        });
        SCCtlOps.addNewNetwork = false;
        ListAdapter adapter = new DeviceAdapter(mActivity, deviceList);

        APList_builder.setSingleChoiceItems(adapter, foucsIndex,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // no use "which" due to sort name

                        // convert to origin index
                        g_SingleChoiceID = which;

                        fileOps.ParseSsidPasswdFile(deviceList[which].getName());
                        dialog.dismiss();

                        SCCtlOps.ConnectedSSID = deviceList[which].getName();
                        SCCtlOps.ConnectedBSSID = deviceList[which].getmacAdrress();

                        // check password of the APInfo[g_SingleChoiceID]
                        // boolean isPasswordExist = false;
                        String content = "";
                        byte[] buff = new byte[256]; // input stream buffer
                        try {
                            FileInputStream reader = mActivity.openFileInput(pwfileName);
                            while ((reader.read(buff)) != -1) {
                                content += new String(buff).trim();
                            }
                            reader.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.e("FileNotFoundException", content);
                        } catch (IOException e) {
                            Log.e("deviceinfo IOException", content);
                        }
                        // Log.d(TAG, "APinfo:" + content);
                        SCCtlOps.ConnectedPasswd = "";
                        AP_password = "";
                        if (content.length() > 0) {
                            String[] APitem = content.split(";");
                            for (String splitString : APitem) {
                                String[] array = splitString.split(",");
                                if (deviceList[which].getName().equals(array[0])) {// ssid is the same

                                    if (array.length > 1)
                                        AP_password = array[1];
                                    else
                                        AP_password = "";

                                    SCCtlOps.StoredPasswd = AP_password;
                                    SCCtlOps.ConnectedPasswd = SCCtlOps.StoredPasswd;
                                    break;
                                }
                            }
                        }

                        // check it if it need password
                        if (deviceList[which].getsecurityType() == 0) {// connect
                            // directly
                            // do connect ap action
                            AP_password = "";
                            ConnectAPProFlag = true;
                            showConnectingDialog();

                            connectAPThread = new Thread() {
                                @Override
                                public void run() {

                                    try {
                                        if (connect_action(g_SingleChoiceID)) {

                                            handler_pd.sendEmptyMessage(0);
                                            // Log.d(TAG, "connect AP: "+
                                            // APInfo[g_SingleChoiceID].getName()+
                                            // "success");
                                            mActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if (ConnectAPProFlag) {
                                                        ConnectAPProFlag = false;
                                                        cfg_ready();
                                                    }
                                                }
                                            });
                                        } else {
                                            if (!ConnectAPProFlag) {
                                                Log.e(TAG, "connect AP:" + SelectedAPInfo.getName() + "fail");

                                                mActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        showConnectApErrorDialog();
                                                    }
                                                });
                                            }
                                            handler_pd.sendEmptyMessage(0);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            connectAPThread.start();

                        } else {// ask user to key in password
                            showInputPasswordDialog();
                        }

                    }

                });

        APList_builder.setPositiveButton("取消", null);
        APList_builder.setNeutralButton("手动输入SSID", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SCCtlOps.addNewNetwork = true;
                addNetworkPopup();
            }
        });

        APList_alert = APList_builder.create();
        APList_alert.show();
    }

    private void showInputPasswordDialog() {
        LayoutInflater factory = LayoutInflater.from(mActivity);
        final View wifiPW_EntryView = factory.inflate(R.layout.wifi_password_entry, null);
        final EditText edittxt_apPassword = (EditText) wifiPW_EntryView.findViewById(R.id.id_ap_password);
        if (AP_password.length() > 0)
            edittxt_apPassword.setText(AP_password, TextView.BufferType.EDITABLE);
        else
            edittxt_apPassword.setText("", TextView.BufferType.EDITABLE);

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setCancelable(false);
        // switch password input type
        alert.setTitle("请输入AP密码:");
        alert.setCancelable(false);
        alert.setView(wifiPW_EntryView);
        alert.setPositiveButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {

                    }
                });
        alert.setNegativeButton("连接", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                // Log.d(TAG, "AP Password:" +
                // edittxt_apPassword.getText().toString());

                // AP_password;
                if (edittxt_apPassword.getText()
                        .toString().length() > 0) {
                    AP_password = edittxt_apPassword
                            .getText().toString();
                } else {
                    AlertDialog.Builder msgAlert = new AlertDialog.Builder(
                            mActivity);
                    msgAlert.setTitle("Error");
                    msgAlert.setMessage("Please check the password!\n");
                    msgAlert.setPositiveButton("OK", null);
                    msgAlert.show();
                    return;
                }

                // do connect ap action
                ConnectAPProFlag = true;

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd = new ProgressDialog(
                                mActivity);
                        pd.setTitle("Connecting");
                        pd.setMessage("Please wait...");
                        pd.setIndeterminate(true);
                        pd.setCancelable(false);
                        pd.setButton(DialogInterface.BUTTON_NEGATIVE,
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                        ConnectAPProFlag = false;
                                    }
                                });
                        pd.show();
                    }
                });

                connectAPThread = new Thread() {
                    @Override
                    public void run() {

                        try {
                            if (connect_action(g_SingleChoiceID)) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (ConnectAPProFlag) {
                                            ConnectAPProFlag = false;
                                            cfg_ready();
                                        }
                                    }
                                });

                            } else {
                                if (ConnectAPProFlag) {

                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showConnectApErrorDialog();
                                        }
                                    });
                                }
                                handler_pd.sendEmptyMessage(0);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                connectAPThread.start();
            }

        });

        alert.show();
    }

    private void GetAllWifiList() {
        ConnectivityManager connManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        String tmp = "";
        String connected_ssid = "";
        String connected_bssid = "";
        String connected_ip = "";
        WifiConnected = false;

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED
                || mWifi.isConnected()
                || wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {

            connected_ssid = wifiInfo.getSSID();
            connected_bssid = wifiInfo.getBSSID();
            if (connected_ssid.indexOf("\"") == 0)
                tmp = connected_ssid.substring(1, connected_ssid.length() - 1);
            else
                tmp = connected_ssid;
            int myIp = wifiInfo.getIpAddress();
            int intMyIp3 = myIp / 0x1000000;
            int intMyIp3mod = myIp % 0x1000000;

            int intMyIp2 = intMyIp3mod / 0x10000;
            int intMyIp2mod = intMyIp3mod % 0x10000;

            int intMyIp1 = intMyIp2mod / 0x100;
            int intMyIp0 = intMyIp2mod % 0x100;

            connected_ip = String.valueOf(intMyIp0) + "."
                    + String.valueOf(intMyIp1) + "." + String.valueOf(intMyIp2)
                    + "." + String.valueOf(intMyIp3);

            WifiConnected = true;
            Log.i(TAG, "Connected SSID :" + connected_ssid);
            Log.i(TAG, "Connected BSSID:" + connected_bssid);
        }

        mScanResults = SCLib.WifiGetScanResults();

        APList_Clear();
        wifiArrayList.clear();

        // Log.d(TAG, "ap number: "+ String.valueOf(mScanResults.size()) );
        if (mScanResults != null) {

            boolean checkSameSSID = false;

            int i = 0;

            for (int iScan = 0; iScan < mScanResults.size() && i < APNumber; iScan++) {
                checkSameSSID = false;
                if (iScan < APNumber) {
                    g_ScanResult = mScanResults.get(iScan);
                    //Log.d(TAG, "AP"+String.valueOf(i) +" : " +
                    //		 g_ScanResult.SSID + "("+ g_ScanResult.BSSID+")" +
                    //String.valueOf(g_ScanResult.level));
                } else
                    continue;

                if (g_ScanResult.SSID.length() == 0)
                    continue;

                for (int numAP = 0; numAP < APNumber; numAP++) {
                    if (APInfo[numAP].getaliveFlag() == 1) {
                        if (g_ScanResult.SSID.equals(APInfo[numAP].getName()))
                            checkSameSSID = true;
                    }
                }
                if (checkSameSSID)
                    continue;
                else {

//                    if ((SCCtlOps.ConnectedSSID != null)
//                            && (SCCtlOps.ConnectedSSID.length() > 0)
//                            && (SCCtlOps.ConnectedSSID
//                            .equals(g_ScanResult.SSID))
//                            && SCLib.isWifiConnected(SCCtlOps.ConnectedSSID)) {
//                        // Log.d(TAG, "AP"+String.valueOf(i) +" : " +
//                        // mScanResult.SSID );
//                    }

                    // Log.d(TAG,"=====================");
                    // Log.d(TAG,,mScanResult.SSID);
                    // Log.d(TAG,String.valueOf(getSecurity(mScanResult)));
                    // Log.d(TAG,mScanResult.capabilities);

                    // APInfo[i].setsecurityType(mScanResult.capabilities);
                    APInfo[i].setsecurityType(getSecurity(g_ScanResult));
                    APInfo[i].setaliveFlag(1);
                    APInfo[i].setName(g_ScanResult.SSID);
                    APInfo[i].setmacAdrress(g_ScanResult.BSSID);
                    APInfo[i].setconnectedflag(false);
                    if (WifiConnected && connected_ssid.length() > 0) {
                        if (tmp.indexOf(connected_ssid) >= 0) {
                            APInfo[i].setIP(connected_ip);
                        }
                    } else {
                        APInfo[i].setIP("");
                    }

                    if (g_ScanResult.level > -50)
                        APInfo[i].setimg(getResources().getDrawable(
                                R.drawable.ibd_findimcoband_signal100));
                    else if (g_ScanResult.level > -60)
                        APInfo[i].setimg(getResources().getDrawable(
                                R.drawable.ibd_findimcoband_signal85));
                    else if (g_ScanResult.level > -70)
                        APInfo[i].setimg(getResources().getDrawable(
                                R.drawable.ibd_findimcoband_signal50));
                    else if (g_ScanResult.level > -80)
                        APInfo[i].setimg(getResources().getDrawable(
                                R.drawable.ibd_findimcoband_signal25));
                    else
                        APInfo[i].setimg(getResources().getDrawable(
                                R.drawable.ibd_findimcoband_signal25));

                    // tmp = "\"" + mScanResult.SSID + "\"";
                    if (WifiConnected) {
                        if (connected_bssid.equals(APInfo[i].getmacAdrress())
                                && APInfo[i].getName().length() > 0) {
                            // if( tmp.equals(APInfo[i].getName()) ){
                            APInfo[i].setIP(connected_ip);
                            APInfo[i].setconnectedflag(true);

                            SelectedAPInfo.setconnectedflag(true);
                            SelectedAPInfo.setName(APInfo[i].getName());
                            SelectedAPInfo.setaliveFlag(1);
                            SelectedAPInfo.setimg(APInfo[i].getimg());
                            SelectedAPInfo.setIP(APInfo[i].getIP());
                            SelectedAPInfo.setmacAdrress(APInfo[i]
                                    .getmacAdrress());
                            SelectedAPInfo.setsecurityType(APInfo[i]
                                    .getsecurityType());
                            // }
                            // Log.d(TAG,SelectedAPInfo.getName());
                        }
                    }
                }
                i++;
            }
        }
    }

    private void APList_Clear() {
        for (int i = 0; i < APNumber; i++) {
            APInfo[i].setconnectedflag(false);
            APInfo[i].setaliveFlag(0);
            APInfo[i].setName("");
            APInfo[i].setIP("");
            APInfo[i].setmacAdrress("");
            APInfo[i].setsecurityType(0);
            APInfo[i].setimg(null);
        }

        SelectedAPInfo = new ConfigurationDevice.DeviceInfo();
        SelectedAPInfo.setconnectedflag(false);
        SelectedAPInfo.setaliveFlag(0);
        SelectedAPInfo.setName("");
        SelectedAPInfo.setIP("");
        SelectedAPInfo.setmacAdrress("");
        SelectedAPInfo.setsecurityType(0);
        SelectedAPInfo.setimg(null);
    }

    Handler handler_pd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Log.d(TAG,"handleMessage msg.what: " + String.valueOf(msg.what));
            switch (msg.what) {
                case 0:
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }

                    break;
                case 1:
                    int timeout = 10;
                    int coutDown = timeout;

                    while (coutDown > 0) {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        coutDown--;
                        if (coutDown == 0) {
                            pd.dismiss();
                        }
                    }

                    break;

                default:
                    break;
            }
        }
    };

    private boolean isWiFiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean ret = mWifi.isConnected();
        if (!ret) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                ret = true;
            }
        }

        return ret;
    }


    private static int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return SECURITY_TYPE.SECURITY_WEP.ordinal();
        } else if (result.capabilities.contains("PSK")) {
            return SECURITY_TYPE.SECURITY_PSK.ordinal();
        } else if (result.capabilities.contains("EAP")) {
            return SECURITY_TYPE.SECURITY_EAP.ordinal();
        }
        return SECURITY_TYPE.SECURITY_NONE.ordinal();
    }

    int ieee80211_frequency_to_channel(int freq) {
        if (freq == 2484)
            return 14;

        if (freq < 2484)
            return (freq - 2407) / 5;

        return freq / 5 - 1000;
    }

    private boolean connect_specific_SSID(ScanResult s_result, String pwd,
                                          int retryMax, boolean checkPWD, int retryCount) {
        Log.w(TAG, "connect_specific_SSID " + s_result.frequency + " ("
                + ieee80211_frequency_to_channel(s_result.frequency) + ") "
                + s_result.SSID + " BSSID:" + s_result.BSSID + " retryCount: " + retryCount + " timeout:" + retryMax);

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        List<WifiConfiguration> list = null;
        WifiConfiguration wifiConfig = null;
        WifiConfiguration config_origin = null;
        NetworkInfo mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String s_SSID = "";
        boolean active = false;
        int isConnectChanceOne = 0;
        int retry = 0, getIPretry = 0, probeReqCount = 0, disconnection_count = 0;
        int mNumOpenNetworksKept = 0;
        int spendTime = 0;

        //check that current connection AP if it is already connection
        if (SCLib.getConnectedWifiSSID() != null) {
            s_SSID = SCLib.getConnectedWifiSSID();
            if (s_SSID != null && s_SSID.length() > 0) {
                s_SSID = s_SSID.indexOf("\"") == 0 ? s_SSID.substring(1,
                        s_SSID.length() - 1) : s_SSID;

                if (s_result.SSID.equals(s_SSID) && s_SSID.indexOf("@RSC") != 0 && !checkPWD) {
                    Log.d(TAG, "The " + s_SSID + " is connected.");
                    return true; //reconnect it in order to correct password
                }
            }
        }


        list = mWifiManager.getConfiguredNetworks();
        // 1. getting phone system's WifiConfiguration
        if (list != null) {
            for (int i_conf = 0; i_conf < list.size(); i_conf++) {

                wifiConfig = list.get(i_conf);

                if (wifiConfig.SSID != null)
                    s_SSID = wifiConfig.SSID.indexOf("\"") == 0 ? wifiConfig.SSID
                            .substring(1, wifiConfig.SSID.length() - 1)
                            : wifiConfig.SSID;

                if (wifiConfig.BSSID == null
                        || wifiConfig.BSSID.equalsIgnoreCase("any")) {
                    if (s_SSID.length() > 0 && s_SSID.equals(s_result.SSID)) {
                        // Log.d(TAG,"wifiConfig got 1");
                        break;
                    }
                } else {
                    if (wifiConfig.BSSID.equalsIgnoreCase(s_result.BSSID)) {

                        // Log.d("SC_CONNECTION","the same BSSID: " + s_SSID +
                        // " v.s. " + scanResult.SSID);

                        if (s_SSID.length() > 0) {

                            if (s_SSID.equals(s_result.SSID)) {
                                // Log.d(TAG,"wifiConfig got 2");
                                break;
                            }

                        } else {
                            // Log.d(TAG,"wifiConfig got 3");
                            break;
                        }
                    }
                }
                wifiConfig = null;
            }
        }

        // 3. control wifiManager
        if (wifiConfig != null) {

            String mScanResultSecurity = Wifi.ConfigSec.getScanResultSecurity(s_result);

            config_origin = Wifi.getWifiConfiguration(mWifiManager, s_result,
                    mScanResultSecurity);

            if (config_origin != null) {

                active = Wifi.changePasswordAndConnect(mActivity, mWifiManager, config_origin, pwd, 0);
                if (!active) {
                    Log.d(TAG, "changePasswordAndConnect_directly");
                    final WifiConfiguration configV2 = Wifi.getWifiConfiguration(mWifiManager, s_result, mScanResultSecurity);
                    if (configV2 != null) {
                        active = Wifi.changePasswordAndConnect_directly(mActivity, mWifiManager, configV2, pwd, 0);
                    }

                } else {
                    Log.i(TAG, "Using wifi configuration");
                }
            } else {
                Log.d(TAG, "config_origin = null");
            }
        }

        if (!active) {// using new config

            Log.i(TAG, "To use new wifi configuration");
            mNumOpenNetworksKept = Settings.Secure.getInt(mActivity.getContentResolver(),
                    Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, 10);
            Log.d(TAG, "connectToNewNetwork");
            active = Wifi.connectToNewNetwork(mActivity, mWifiManager,
                    s_result, pwd, mNumOpenNetworksKept);

            if (!active) {

                active = Wifi.connectToNewNetwork_directly(mActivity,
                        mWifiManager, s_result, pwd, mNumOpenNetworksKept);
                if (!active && retryCount % 2 == 0) {
                    Log.w(TAG, "wifiManager.setWifiEnabled(false)");
                    mWifiManager.setWifiEnabled(false);
                }
            }
        }

        if (!active)
            Log.d(TAG, "=======connect_specific_AP======= enableNetwork: "
                    + String.valueOf(active));

        NetworkInfo.DetailedState networkStateTmp = NetworkInfo.DetailedState.IDLE;
        NetworkInfo.DetailedState networkStateNow = NetworkInfo.DetailedState.IDLE;

        SupplicantState splctTmp = SupplicantState.UNINITIALIZED;
        SupplicantState splctNow = SupplicantState.UNINITIALIZED;

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spendTime++;

            if (TimesupFlag_cfg)
                break;

            if (!mWifiManager.isWifiEnabled()) {
                Log.w(TAG, "wifiManager.setWifiEnabled(true)");
                mWifiManager.setWifiEnabled(true);
            }

            wifiInfo = mWifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                break;
            }

            mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            networkStateNow = mWifi.getDetailedState();
            splctNow = mWifiManager.getConnectionInfo().getSupplicantState();

            if ((networkStateTmp != networkStateNow) || (splctTmp != splctNow)) {

                Log.d(TAG, "connect AP=== wait" + String.valueOf(spendTime)
                        + " mWifi State: " + networkStateNow + " " + splctNow
                        + " " + WifiInfo.getDetailedStateOf(splctNow));
                networkStateTmp = networkStateNow;
                splctTmp = splctNow;
            }

            if ((splctNow == SupplicantState.SCANNING)
                    || WifiInfo.getDetailedStateOf(splctNow) == NetworkInfo.DetailedState.SCANNING) {

                if (!mWifiManager.pingSupplicant())
                    Log.e(TAG, "wifiManager.pingSupplicant():" + mWifiManager.pingSupplicant());

                probeReqCount++;
                if (probeReqCount >= 3) {
                    probeReqCount = 0;
                    break;
                }
            }

            if (networkStateNow == NetworkInfo.DetailedState.DISCONNECTED) {
                disconnection_count++;
                if (disconnection_count >= 3)
                    break;
            }

            if (networkStateNow != NetworkInfo.DetailedState.DISCONNECTED
                    || (splctNow != SupplicantState.SCANNING)
                    || WifiInfo.getDetailedStateOf(splctNow) != NetworkInfo.DetailedState.SCANNING) {
                if (++isConnectChanceOne < 3) {
                    retry = 0;
                }
            }

            if (networkStateNow == NetworkInfo.DetailedState.OBTAINING_IPADDR
                    || splctNow == SupplicantState.ASSOCIATING
                    || WifiInfo.getDetailedStateOf(splctNow) == NetworkInfo.DetailedState.OBTAINING_IPADDR
                    || WifiInfo.getDetailedStateOf(splctNow) == NetworkInfo.DetailedState.CONNECTING) {
                Log.d(TAG,
                        "getIPretry:" + getIPretry + " networkStateNow:"
                                + networkStateNow + " "
                                + WifiInfo.getDetailedStateOf(splctNow));
                if (++getIPretry > 20) {
                    break;
                } else {
                    retry = 0;
                }
            }

            retry++;

        } while (!isWiFiConnect() && retry < retryMax);

        // check connect AP correctly
        if (mWifiManager.getConnectionInfo().getSSID() != null) {
            s_SSID = mWifiManager.getConnectionInfo().getSSID();
            s_SSID = s_SSID.indexOf("\"") == 0 ? s_SSID.substring(1,
                    s_SSID.length() - 1) : s_SSID;

            if (!s_result.SSID.equals(s_SSID)) {
                Log.e(TAG, "s_result.SSID:" + s_result.SSID + " v.s. s_SSID:"
                        + s_SSID);
                active = false;
            }
        }

        mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (active && mWifi.isConnected()) {
            Log.d(TAG, "SSID is " + s_SSID + " connection spend(" + (spendTime) + ")s");
            return true;
        } else {
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {

                if (s_result.SSID.equals(s_SSID)) {
                    Log.d(TAG, "SSID is " + s_SSID + " connection spend(" + (spendTime) + ")s !!");
                    return true;
                }
            }
            Log.e(TAG,
                    "active:" + active + " mWifi.isConnected():"
                            + mWifi.isConnected());

            Log.e(TAG, "SSID is " + s_SSID + " connection spend(" + (spendTime)
                    + ")s xxxxxxxxxx");
            return false;
        }
    }


    private boolean connect_action(int choiceID) throws InterruptedException {

        int count_recount = 0, retry_checkConnected = 0;
        Long startTime, nowTime, spentTime;
        startTime = System.currentTimeMillis();

        Log.d(TAG, "connect_action START");
        ScanResult scanResult = null;
        NetworkInfo mWifi = null;
        WifiInfo wifiInfo = null;
        DhcpInfo d = null;
        boolean active = false;
        String s_SSID = "";
        int retry = 0;

        if (!mWifiManager.isWifiEnabled()) {
            if (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                mWifiManager.setWifiEnabled(true);
        }

        // 1. getting the scanResult by user choose
        for (int i = 0; i < APNumber; i++) {
            scanResult = mScanResults.get(i);

            g_ScanResult = scanResult;

            // Log.d("SC_CONNECTION","BSSID: " +
            // deviceList[choiceID].getmacAdrress() + " vs " + scanResult.BSSID
            // + "("+ scanResult.SSID +")");

            if (deviceList[choiceID].getmacAdrress().equals(scanResult.BSSID)
                    && scanResult.SSID.length() > 0) {
                if (deviceList[choiceID].getName().endsWith(scanResult.SSID)) {
                    break;
                }
            }

            scanResult = null;
        }

        if (scanResult == null)
            return false;

        TimesupFlag_cfg = false;

        while (retry++ < 30) {
            if (connect_specific_SSID(scanResult, AP_password, 15, true, retry)) {
                active = true;
                break;
            } else {
                Log.d(TAG, "retry (" + retry + ") to connect remote AP:" + scanResult.SSID);
            }
        }

        // check connection result
        do {
            Thread.sleep(500);
            mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            retry++;

            Log.d(TAG, "=======connect AP======= wait" + String.valueOf(60 - retry) + " mWifi State: " + mWifi.getState());

            if (!ConnectAPProFlag)
                return false;

            if (retry >= 10 && retry <= 15) {
                if (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
                    mWifiManager.setWifiEnabled(true);
                    active = true;
                    Log.w(TAG, "wifiManager.setWifiEnabled(true)");
                }
            }

            wifiInfo = mWifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                break;
            }

        } while (!mWifi.isConnected() && retry <= 60);

        // check connection info
        WifiConnected = false;

        while (count_recount < 100) {
            ++count_recount;
            retry_checkConnected = 0;
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isWiFiConnect()) {
                    break;
                }

            } while (retry_checkConnected++ < 100);
            Log.d(TAG, "connect ation=>checkConnected: " + count_recount + "-" + retry_checkConnected);

            d = mWifiManager.getDhcpInfo();

            if ((d.dns1 != 0) &&
                    (d.gateway != 0) &&
                    (d.serverAddress != 0) &&
                    (d.ipAddress != 0)) {
                break;
            }

            mWifiManager.reconnect();
        }

        Log.i(TAG, "(target AP)DNS 1: " + IntegerLE2IPStr(d.dns1));
        Log.i(TAG, "(target AP)Server IP: " + IntegerLE2IPStr(d.serverAddress));
        Log.i(TAG, "(target AP)Default Gateway: " + IntegerLE2IPStr(d.gateway));
        Log.i(TAG, "(target AP)IP Address: " + IntegerLE2IPStr(d.ipAddress));
        Log.i(TAG, "(target AP)Subnet Mask: " + IntegerLE2IPStr(d.netmask));

        // check connect AP correctly
        if (mWifiManager.getConnectionInfo().getSSID() != null) {
            s_SSID = mWifiManager.getConnectionInfo().getSSID();
            s_SSID = s_SSID.indexOf("\"") == 0 ? s_SSID.substring(1,
                    s_SSID.length() - 1) : s_SSID;

            if (!scanResult.SSID.equals(s_SSID)) {
                active = false;
                // Log.d("SC_CONNECTION","SSID is different!!!! " +
                // scanResult.SSID + " vs " +
                // wifiManager.getConnectionInfo().getSSID());
            }
            Log.d(TAG, "Home AP's SSID is "
                    + mWifiManager.getConnectionInfo().getSSID() + " (" + mWifiManager.getConnectionInfo().getBSSID() + ")");
            // Log.d("SC_CONNECTION","BSSID " + scanResult.BSSID + " vs " +
            // wifiManager.getConnectionInfo().getBSSID());
        }

        if (active) {
            WifiConnected = true;

            SelectedAPInfo.setconnectedflag(true);
            SelectedAPInfo.setaliveFlag(1);
            SelectedAPInfo.setName(deviceList[choiceID].getName());
            SelectedAPInfo.setIP(deviceList[choiceID].getIP());
            SelectedAPInfo.setmacAdrress(deviceList[choiceID].getmacAdrress());
            SelectedAPInfo.setsecurityType(deviceList[choiceID]
                    .getsecurityType());
            SelectedAPInfo.setimg(null);

            SCCtlOps.ConnectedSSID = scanResult.SSID;
            SCCtlOps.ConnectedPasswd = AP_password;

        } else {
            WifiConnected = false;

            SelectedAPInfo.setconnectedflag(false);
            SelectedAPInfo.setaliveFlag(0);
            SelectedAPInfo.setName("");
            SelectedAPInfo.setIP("");
            SelectedAPInfo.setmacAdrress("");
            SelectedAPInfo.setsecurityType(0);
            SelectedAPInfo.setimg(null);
        }

        // check password if it is not exist
        // store the password
        // content: ssid,password;ssid,password;.....
        if (WifiConnected) {

            String content = "";
            byte[] buff = new byte[256]; // input stream buffer
            try {
                FileInputStream reader = mActivity.openFileInput(pwfileName);
                while ((reader.read(buff)) != -1) {
                    content += new String(buff).trim();
                }
                reader.close();
            } catch (FileNotFoundException e) {
                // e.printStackTrace();
                Log.e(TAG, "deviceinfo FileNotFoundException " + content);
            } catch (IOException e) {
                Log.e(TAG, "deviceinfo IOException " + content);
            }
            // Log.d(TAG,"APinfo: "+content);

            if (content.length() > 0) {
                String[] APitem = content.split(";");
                int itemNumber = APitem.length;
                int compareAPNumber = 0;
                String[] array;

                for (int i = 0; i < itemNumber; i++) {
                    array = APitem[i].split(",");
                    if (array.length > 0) {
                        if (!SelectedAPInfo.getName().equals(array[0])) { // ssid is different,so no store
                            compareAPNumber++;
                        }
                    }
                }

                if (itemNumber == compareAPNumber) // add new password
                {
                    // store password into file
                    String spilt = ",";
                    String spiltEnd = ";";
                    FileOutputStream writer;
                    try {
                        writer = mActivity.openFileOutput(pwfileName, Context.MODE_APPEND);
                        writer.write(SelectedAPInfo.getName().getBytes());
                        writer.write(spilt.getBytes());
                        if (SCCtlOps.ConnectedPasswd.length() > 0)
                            writer.write(SCCtlOps.ConnectedPasswd.getBytes());
                        else
                            writer.write("".getBytes());
                        writer.write(spiltEnd.getBytes());
                        writer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // update password
                    FileOutputStream writer;
                    String tmpData = content;
                    String newData = "";
                    String[] tmpAPitem = tmpData.split(";");

                    // refine data
                    for (int i = 0; i < tmpAPitem.length; i++) {
                        array = tmpAPitem[i].split(",");
                        if (!SelectedAPInfo.getName().equals(array[0])) { // ssid is different
                            newData = newData + tmpAPitem[i] + ";";
                        } else {
                            tmpAPitem[i] = array[0] + ","
                                    + SCCtlOps.ConnectedPasswd;
                            newData = newData + tmpAPitem[i] + ";";
                        }
                    }

                    try {
                        writer = mActivity.openFileOutput(pwfileName,
                                Context.MODE_PRIVATE);
                        writer.write(newData.getBytes());
                        writer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            } else {
                // store password into file directly if origin is empty
                String spilt = ",";
                String spiltEnd = ";";
                FileOutputStream writer;
                try {
                    writer = mActivity.openFileOutput(pwfileName, Context.MODE_APPEND);
                    writer.write(SelectedAPInfo.getName().getBytes());
                    writer.write(spilt.getBytes());
                    writer.write(SCCtlOps.ConnectedPasswd.getBytes());
                    writer.write(spiltEnd.getBytes());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        nowTime = System.currentTimeMillis();

        if (nowTime >= startTime)
            spentTime = nowTime - startTime;
        else
            spentTime = Long.MAX_VALUE - startTime + nowTime;

        Log.d(TAG, "connect_action END: " + spentTime + " ms");

        return WifiConnected;
    }

    private static class DeviceAdapter extends ArrayAdapter {

        private static final int RESOURCE = R.layout.wifiap_list;
        private LayoutInflater inflater;

        static class ViewHolder {
            TextView nameTxVw;
            TextView deviceInfo;
            ImageView deviceImg;
            RadioGroup selected;
        }

        @SuppressWarnings("unchecked")
        DeviceAdapter(Context context, ConfigurationDevice.DeviceInfo[] objects) {
            super(context, RESOURCE, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                // inflate a new view and setup the view holder for future use
                convertView = inflater.inflate(RESOURCE, null);

                holder = new ViewHolder();
                holder.nameTxVw = (TextView) convertView
                        .findViewById(R.id.title_aplist);
                holder.deviceInfo = (TextView) convertView
                        .findViewById(R.id.info_aplist);
                holder.deviceImg = (ImageView) convertView
                        .findViewById(R.id.signalImg);
                holder.selected = (RadioGroup) convertView
                        .findViewById(R.id.radioButton1);

                convertView.setTag(holder);
            } else {
                // view already defined, retrieve view holder
                holder = (ViewHolder) convertView.getTag();
            }

            ConfigurationDevice.DeviceInfo cat = (ConfigurationDevice.DeviceInfo) getItem(position);
            if (cat == null) {
                // Log.e( TAG,"error_getView_neil "+ String.valueOf(position) );
            }

            holder.nameTxVw.setText(cat.getName());

            if (cat.getconnectedflag()) {
                holder.deviceInfo.setText("connected");
            } else {
                holder.deviceInfo.setText("");
            }

            holder.deviceImg.setImageDrawable(cat.getimg());

            return convertView;
        }
    }


    public void cfg_ready() {
        handler_pd.sendEmptyMessage(0);

        factory = LayoutInflater.from(mActivity);
        wifiPW_EntryView = factory
                .inflate(R.layout.confirm_pincode_entry, null);
        edittxt_PINcode = (EditText) wifiPW_EntryView.findViewById(R.id.id_ap_password);

        edittxt_PINcode.setText("", TextView.BufferType.EDITABLE);
        edittxt_PINcode.setInputType(InputType.TYPE_CLASS_NUMBER);

        // get last pin code
        pinCodeText = edittxt_PINcode.getText().toString();
        PINGet = edittxt_PINcode.getText().toString();
        PINSet = null;

        pinCodeText = "";
        //CheckConfigDeviceNumber();
        inputPINcode();
    }

    public void inputPINcode() {
        factory = LayoutInflater.from(mActivity);
        wifiPW_EntryView = factory
                .inflate(R.layout.confirm_pincode_entry, null);
        edittxt_PINcode = (EditText) wifiPW_EntryView
                .findViewById(R.id.id_ap_password);

        edittxt_PINcode.setText("", TextView.BufferType.EDITABLE);
        edittxt_PINcode.setInputType(InputType.TYPE_CLASS_NUMBER);

        // get last pin code
        pinCodeText = edittxt_PINcode.getText().toString();
        PINGet = edittxt_PINcode.getText().toString();
        PINSet = null;

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setCancelable(false);
        alert.setTitle("Input Device PIN code:");
        alert.setMessage("The PIN code will be display on device if the PIN code is exist.\nOtherwise, choose the skip option.");
        alert.setCancelable(false);
        alert.setView(wifiPW_EntryView);
        alert.setPositiveButton("Skip", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                pinCodeText = "";

                startToConfigure();
            }
        });
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (edittxt_PINcode.getText().toString().length() == 8) {
                    pinCodeText = edittxt_PINcode.getText().toString();
                    presave_pinCode = pinCodeText;
                    startToConfigure();
                } else if (edittxt_PINcode.getText().toString().length() == 0) {
                    AlertDialog.Builder infoAlert = new AlertDialog.Builder(
                            mActivity);

                    infoAlert.setTitle("Warning!");
                    infoAlert.setMessage("The PIN code is empty.");
                    infoAlert.setPositiveButton("OK", null);
                    infoAlert.show();
                } else {
                    AlertDialog.Builder infoAlert = new AlertDialog.Builder(
                            mActivity);

                    infoAlert.setTitle("Warning!");
                    infoAlert.setMessage("Invalid PIN code. Please input 8 numbers for PIN code.");
                    infoAlert.setPositiveButton("OK", null);
                    infoAlert.show();
                }
                // Log.d(TAG,"pinCode_step1: "+edittxt_PINcode.getText().toString());

            }

        });
        alert.show();
        handler_pd.sendEmptyMessage(0);
    }

    public void startToConfigure() {
        ConfigureAPProFlag = true;

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                pd = new ProgressDialog(mActivity);
                pd.setCancelable(true);
                pd.setTitle("Configure New Device");
                pd.setCancelable(false);
                pd.setMessage("Configuring...");
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setProgress(0);
                pd.setMax(100);
                pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d(TAG, "Cancel !!!!");

                                ConfigureAPProFlag = false;
                                TimesupFlag_cfg = true;
                                SCLib.rtk_sc_stop();
                                SCLib.SoftAP_ss_close();
                                backgroundThread.interrupt();

                                pd = new ProgressDialog(mActivity);
                                pd.setTitle("Warning");
                                pd.setMessage("Connecting to home AP...");
                                pd.setIndeterminate(true);
                                pd.setCancelable(false);
                                pd.show();
                            }
                        });
                pd.show();
            }
        });


        // create a thread for updating the progress bar
        backgroundThread = new Thread(new Runnable() {
            public void run() {
                try {

                    int c = 0;

                    while (pd.getProgress() <= pd.getMax()) {

                        Thread.sleep(1200);
                        Log.i(TAG, "WAIT: " + ++c + "%");
                        progressHandler.sendMessage(progressHandler
                                .obtainMessage());

                        if (c >= 100) {
                            ConfigureAPProFlag = false;
                            TimesupFlag_cfg = true;
                            SCLib.rtk_sc_stop();
                            SCLib.SoftAP_ss_close();
                            handler_pd.sendEmptyMessage(0);
                            break;
                        }

                    }
                } catch (java.lang.InterruptedException e) {
                }
            }
        });
        backgroundThread.start();

        Thread ConfigDeviceThread = new Thread() {
            @Override
            public void run() {

                int retryCount = 0;
                int ret = -1;
                int count_recount = 0, retry_checkConnected = 0;
                String homeAP_bssid = "";

                TimesupFlag_cfg = false;

                //======== soft AP mode ========
                ret = Configure_softAP_action();

                Log.d(TAG, "re-connect to home AP");
                retryCount = 0;
                do {
                    if (connect_specific_SSID(g_ScanResult, AP_password, 15, false, retryCount))
                        break;
                } while (retryCount++ < 10);
                Log.d(TAG, "re-connect to home AP end");

                while (count_recount < 100) {
                    ++count_recount;
                    retry_checkConnected = 0;
                    do {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (isWiFiConnect()) {
                            break;
                        }

                    } while (retry_checkConnected++ < 30);
                    Log.d(TAG, "back to remote AP=>checkConnected: " + count_recount + "-" + retry_checkConnected);

                    DhcpInfo d = mWifiManager.getDhcpInfo();
                    homeAP_bssid = mWifiManager.getConnectionInfo().getBSSID();
                    //Log.i(TAG,"DNS 1: "+IntegerLE2IPStr(d.dns1));
                    //Log.i(TAG,"Server IP: "+IntegerLE2IPStr(d.serverAddress));
                    //Log.i(TAG,"Default Gateway: "+IntegerLE2IPStr(d.gateway));
                    //Log.i(TAG,"IP Address: "+IntegerLE2IPStr(d.ipAddress));
                    //Log.i(TAG,"Subnet Mask: "+IntegerLE2IPStr(d.netmask));
                    //Log.i(TAG,"softAp bssid: "+softAp_bssid);
                    if (homeAP_bssid != null) {
                        if ((d.dns1 != 0) &&
                                (d.gateway != 0) &&
                                (d.serverAddress != 0) &&
                                (d.ipAddress != 0) &&
                                (homeAP_bssid.length() > 0) &&
                                (!homeAP_bssid.equals("00:00:00:00:00:00"))
                                ) {
                            break;
                        } else {
                            Log.w(TAG, "DNS 1: " + IntegerLE2IPStr(d.dns1));
                            Log.w(TAG, "Server IP: " + IntegerLE2IPStr(d.serverAddress));
                            Log.w(TAG, "Default Gateway: " + IntegerLE2IPStr(d.gateway));
                            Log.w(TAG, "IP Address: " + IntegerLE2IPStr(d.ipAddress));
                            Log.w(TAG, "homeAP_bssid: " + homeAP_bssid);
                        }
                    } else {
                        Log.e(TAG, "shomeAP bssid is null");
                    }

                    mWifiManager.reconnect();
                }

                Log.w(TAG, "Connect finish");

                if (ret == 1) { //APP got DUT's response at soft AP mode

                    do {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } while (!TimesupFlag_cfg && pd.getProgress() < 100);

                } else {
                    //======== normal mode ========
                    if (!TimesupFlag_cfg && pd.getProgress() < 100) {
                        ret = Configure_action();
                    }
                }

                if (ret < 0) {// error condition => reset condition
                    DiscovEnable = true;
                    ShowCfgSteptwo = false;
                    ConfigureAPProFlag = false;

                    pd.setProgress(100);
                    backgroundThread.interrupt();
                    handler_pd.sendEmptyMessage(0);

                    if (ret == -99) {// error condition => reset condition
                        SCLib.rtk_sc_stop();

                        ConfigureAPProFlag = false;
                        DiscovEnable = true;
                        ShowCfgSteptwo = false;
                        ConfigureAPProFlag = false;

                        pd.setProgress(100);
                        backgroundThread.interrupt();
                        handler_pd.sendEmptyMessage(0);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder infoAlert = new AlertDialog.Builder(
                                        mActivity);

                                infoAlert.setTitle("Warning!");
                                infoAlert.setMessage("Error");
                                infoAlert.setPositiveButton("OK", null);
                                infoAlert.show();
                            }
                        });

                    } else if (ret == -11) {
                        Log.e(TAG, "Your Speaker is not Found!");
                        /*runOnUiThread(new Runnable() {
                            @Override
							public void run() {
								AlertDialog.Builder infoAlert = new AlertDialog.Builder(
										MainActivity.this);

								infoAlert.setTitle("Warning!");
								infoAlert
										.setMessage("Your Speaker is not Found!");
								infoAlert.setPositiveButton("OK", null);
								infoAlert.show();
							}
						});*/
                    }
                }

                Log.d(TAG, "ReadyCount Finish!!!");

                retryCount = 0;
                while (SCLib.rtk_sc_get_connected_sta_num() == 0
                        && ConfigureAPProFlag && retryCount++ < 40) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (pd.getProgress() == 100)
                        break;
                }

                final int finishPersent = pd.getProgress();
                /*runOnUiThread(new Runnable() {
                    @Override
					public void run() {
						Toast.makeText(getApplication(),
								"Cfg Success Finish: " + finishPersent*1.2, Toast.LENGTH_LONG)
								.show();
					}
				});*/

                Log.d(TAG, "----------------------------------------");
                Log.d(TAG, "finish.getProgress()  " + finishPersent);
                Log.d(TAG, "----------------------------------------");

                // wait dialog cancel
                if (!ConnectAPProFlag) {
                    pd.setProgress(100);
                    backgroundThread.interrupt();
                    handler_pd.sendEmptyMessage(0);
                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (ConfigureAPProFlag) {
                            // show "start to configure"
                            ConfigureAPProFlag = false;

                            showConfiguredList();
                        }
                    }
                });

            }
        };
        ConfigDeviceThread.start();
    }

    @SuppressLint("NewApi")
    protected int Configure_softAP_action() {

        Log.w(TAG, "====================Configure_softAP Start====================");

        ScanResult DUT_softAP = null;
        String sData = "";
        String softSSID = "";
        String softAp_bssid = "";
        String origin_bssid = "";
        int ret = -1;
        int retry = 0, siteSurvey_retry = 300;
        int ip_by_homeAP = 0;
        int softAP_serverIP = 0;
        DhcpInfo d = null;
        NetworkInfo mWifi = null;
        Long startTime, nowTime, spentTime;

        startTime = System.currentTimeMillis();

        allSSID = "";
        Log.d(TAG, "cfgDeviceNumber: " + cfgDeviceNumber);

        SCLib.rtk_sc_reset();

        Log.d(TAG, "!!!!!!!!!!!!!!!0. check if wifi is connected");
        retry = 0;
        while (retry++ < 10) {
            if (TimesupFlag_cfg)
                break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.getState() == NetworkInfo.State.CONNECTED && mWifi.isConnected())
                break;
        }

        ip_by_homeAP = mWifiManager.getConnectionInfo().getIpAddress();
        origin_bssid = mWifiManager.getConnectionInfo().getBSSID();
        SCLib.SoftAPInit(cfgDeviceNumber, ip_by_homeAP);

        Log.d(TAG, "!!!!!!!!!!!!!!!1. search DUT AP");
        do {
            if (TimesupFlag_cfg)
                break;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (allSSID.length() == 0 && --siteSurvey_retry > 0);

        if (allSSID.equals("ERROR") || allSSID.length() == 0) {
            Log.e(TAG, "try to get scanResults fail");
            return -11;
        }

        DUT_softAP = SCLib.getDUT_by_scanResults();

        if (DUT_softAP == null) {
            return -11;
        }

        Log.d(TAG, "!!!!!!!!!!!!!!!2. connect soft AP");
        retry = 0;
        while (retry++ < 10 && !TimesupFlag_cfg && pd.getProgress() < 50) {

            Log.w(TAG, ">>> CONNECT Count: " + retry);

            if (TimesupFlag_cfg || pd.getProgress() >= 100)
                break;

            if (connect_specific_SSID(DUT_softAP, "12345678", 20, true, retry)) {

                int retry_checkConnected = 0, count_reconnect = 0, waitCount = 0;

                while (waitCount < 20) {
                    ++count_reconnect;
                    retry_checkConnected = 0;
                    //Log.d(TAG,"loop start");

                    while (retry_checkConnected++ < 30 && !isWiFiConnect()) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.d(TAG, "===========(soft AP)checkConnected: " + count_reconnect + "-" + retry_checkConnected + " ===========");

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    d = mWifiManager.getDhcpInfo();
                    softAp_bssid = mWifiManager.getConnectionInfo().getBSSID();

                    Log.i(TAG, "softAp bssid: " + softAp_bssid);
                    if (softAp_bssid != null) {
                        if ((d.dns1 != 0) &&
                                (d.gateway != 0) &&
                                (d.serverAddress != 0) &&
                                (d.ipAddress != 0) &&
                                (softAp_bssid.length() > 0) &&
                                (!softAp_bssid.equals("00:00:00:00:00:00"))
                                ) {

                            Log.i(TAG, "DNS 1: " + IntegerLE2IPStr(d.dns1));
                            Log.i(TAG, "Server IP: " + IntegerLE2IPStr(d.serverAddress));
                            Log.i(TAG, "Default Gateway: " + IntegerLE2IPStr(d.gateway));
                            Log.i(TAG, "IP Address: " + IntegerLE2IPStr(d.ipAddress));
                            Log.i(TAG, "Subnet Mask: " + IntegerLE2IPStr(d.netmask));

                            if (!(origin_bssid.equals(softAp_bssid)) && (ip_by_homeAP != d.ipAddress)) {
                                break;
                            } else if (!(origin_bssid.equals(softAp_bssid))) {
                                if (ip_by_homeAP == d.ipAddress) {
                                    //waitCount+=4;//in order to try send wifi profile to soft AP after 40/4  loops
                                    Log.d(TAG, "check renew IP!!!!!");
                                }
                            }

                        } else {
                            Log.w(TAG, "Default Gateway: " + IntegerLE2IPStr(d.gateway));
                            Log.w(TAG, "IP Address: " + IntegerLE2IPStr(d.ipAddress));
                            Log.w(TAG, "softAp_bssid:" + softAp_bssid + "(" + softAp_bssid.length() + ")");
                        }
                    } else {
                        Log.e(TAG, "softAp_bssid is null");
                    }

                    waitCount++;
                    if (waitCount % 2 == 0) {
                        //for DHCP lease IP
                        //wifiManager.disconnect();
                        mWifiManager.reconnect();
                        Log.e(TAG, "reconnect for DHCP lease IP");
                    }
                    //Log.d(TAG,"loop again");
                }

                softSSID = mWifiManager.getConnectionInfo().getSSID();
                if (softSSID.indexOf("\"") == 0)
                    softSSID = softSSID.substring(1, softSSID.length() - 1);

                //check that connected soft AP is not remote AP
                if (SCCtlOps.ConnectedBSSID.equals(softAp_bssid) ||
                        softSSID.indexOf("@RSC") != 0) {
                    Log.e(TAG, "The connected AP is remote AP(" + mWifiManager.getConnectionInfo().getSSID() + ")!!! "
                            + SCCtlOps.ConnectedBSSID + " v.s. " + softAp_bssid);

                    continue;
                }

                if (softAp_bssid == null) {
                    Log.e(TAG, "softAp BSSID is null");
                    continue;
                }

                Log.d(TAG, "Soft AP:" + mWifiManager.getConnectionInfo().getSSID());
                Log.d(TAG, "DNS 1: " + IntegerLE2IPStr(d.dns1));
                Log.d(TAG, "DNS 2: " + IntegerLE2IPStr(d.dns2));
                Log.d(TAG, "Default Gateway: " + IntegerLE2IPStr(d.gateway));
                Log.d(TAG, "IP Address: " + IntegerLE2IPStr(d.ipAddress));
                Log.d(TAG, "Lease Time: " + d.leaseDuration);
                Log.d(TAG, "Subnet Mask: " + IntegerLE2IPStr(d.netmask));
                Log.d(TAG, "Server IP: " + IntegerLE2IPStr(d.serverAddress));
                Log.d(TAG, "softAp bssid: " + softAp_bssid);

                softAP_serverIP = d.serverAddress;
                if (softAP_serverIP == 0) {
                    softAP_serverIP = 267692224;//0XFF4A8C0
                }

                Log.d(TAG, "!!!!!!!!!!!!!!!3. send profile");
                Log.i(TAG, "Send: " + SCCtlOps.ConnectedSSID + " "
                        + SCCtlOps.ConnectedPasswd + " BSSID:"
                        + SCCtlOps.ConnectedBSSID);

                sData = SCLib.softAP_cmd_send(SCCtlOps.ConnectedSSID,
                        SCCtlOps.ConnectedPasswd, SCCtlOps.ConnectedBSSID,
                        pinCodeText,
                        softAP_serverIP,
                        softAp_bssid);


                if (sData.length() > 0) {
                    ret = 1;
                    break;
                } else {
                    Log.e(TAG, "softAP_cmd_send FAIL !!!");
                }

            } else {
                Log.e(TAG, "retry (" + retry + ") to connect soft AP: " + DUT_softAP.SSID);
            }
        }

        nowTime = System.currentTimeMillis();

        if (nowTime >= startTime)
            spentTime = nowTime - startTime;
        else
            spentTime = Long.MAX_VALUE - startTime + nowTime;

        Log.w(TAG, "====================Configure_softAP End================ spend " + (spentTime / 1000));
        return ret;
    }

    private int Configure_action() {
        int watchCount = 0;
        int connectionTimoutCount = 0;

        Log.w(TAG, "====================Configure_action normal method Start====================");
        // check wifi connected
        if (SCCtlOps.ConnectedSSID == null) {
            return -1;
        }
        int connect_count = 200;

        // get wifi ip
        int wifiIP = SCLib.WifiGetIpInt();
        while (connect_count > 0 && wifiIP == 0) {
            wifiIP = SCLib.WifiGetIpInt();
            connect_count--;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (wifiIP == 0) {

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity,
                            "Allocating IP, please wait a moment", Toast.LENGTH_SHORT)
                            .show();
                }
            });

            return -2;
        }

        SCLib.rtk_sc_reset();
        if (PINSet == null) {
            SCLib.rtk_sc_set_default_pin(defaultPINcode);
        } else if (PINSet.length() > 0) {
            SCLib.rtk_sc_set_default_pin(defaultPINcode);
        }

        if (pinCodeText.length() > 0) {
            SCLib.rtk_sc_set_pin(pinCodeText);
        } else
            SCLib.rtk_sc_set_pin(PINSet);

        SCLib.rtk_sc_set_ssid(SCCtlOps.ConnectedSSID);
        SCLib.rtk_sc_set_bssid(SCCtlOps.ConnectedBSSID); // BSSID of connected AP
        if (!SCCtlOps.IsOpenNetwork) {

            SCLib.rtk_sc_set_password(SCCtlOps.ConnectedPasswd);
        }

        TimesupFlag_cfg = false;

        SCLib.rtk_sc_set_deviceNum(cfgDeviceNumber);

        SCLib.rtk_sc_set_ip(wifiIP);
        SCLib.rtk_sc_build_profile();

		/* Profile(SSID+PASSWORD, contain many packets) sending total time(ms). */
        SCLibrary.TotalConfigTimeMs = CONFIGTIMEOUT;

		/*
         * Configuring by using old mode(0~max_time) before new mode(the
		 * remaining time)
		 */
        SCLibrary.OldModeConfigTimeMs = CONFIGTIMEOUT; // 0:R2 CONFIGTIMEOUT:R1

		/* Profile continuous sending rounds. */
        SCLibrary.ProfileSendRounds = 1;

        // exception action

		/* Time interval(ms) between sending two profiles. */
        SCLibrary.ProfileSendTimeIntervalMs = 600; // 600ms
        /* Time interval(ms) between sending two packets. */
        SCLibrary.PacketSendTimeIntervalMs = 6; // 6ms
        /* Each packet sending counts. */
        SCLibrary.EachPacketSendCounts = 1;

        Log.i(TAG, "Android v" + Build.VERSION.RELEASE + " Phone: "
                + Build.MANUFACTURER + " " + Build.MODEL);

        SCLib.rtk_sc_start();
        watchCount = 0;

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            watchCount += 1;

            if (!isWiFiConnect())
                connectionTimoutCount++;
            else
                connectionTimoutCount = 0;

            if (connectionTimoutCount >= 5) {
                connectionTimoutCount = 0;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,
                                "Please check your wifi connection.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

        } while (!TimesupFlag_cfg
                && (watchCount * 1000) < CONFIGTIMEOUT && pd.getProgress() < 100);

        TimesupFlag_cfg = true;

        Log.w(TAG, "====================Configure_action normal End====================");

        return 1;
    }

    public void scanDevice() {
        SCCtlOps.rtk_sc_control_reset();
        NetworkInfo mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            DiscovEnable = true;
            discoveryDevice(discoveryTimeout);
            show_discoverDevice();
        } else {
            showLongToast("Please connect the home AP");
        }
    }

    private void showDiscoveringDeviceDialog() {
        pd = new ProgressDialog(mActivity);
        pd.setTitle("Scan Configured Devices by "
                + SCLib.getConnectedWifiSSID());
        pd.setMessage("Discovering devices ...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DiscovEnable = false;
                    }
                });
        pd.show();
    }

    private void discoveryDevice(final int counts) {
        showDiscoveringDeviceDialog();
        new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib
                        .rtk_sc_get_default_pin());
                long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();
                while (DiscovEnable && (endTime - startTime) < counts) {

                    try {
                        Thread.sleep(200);
                        SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf,
                                "255.255.255.255");
                        // Log.d(TAG,"scan-discovery: 255.255.255.255");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    endTime = System.currentTimeMillis();
                }
                handler_pd.sendEmptyMessage(0);
                Log.i(TAG, "Discover Time Elapsed: " + (endTime - startTime)
                        + "ms");

                // Update Status
                Message msg = Message.obtain();
                msg.obj = null;
                msg.what = ~SCCtlOps.Flag.DiscoverACK; // timeout
                SCLib.TreadMsgHandler.sendMessage(msg);
            }
        }).start();
    }


    private void resetData() {
        for (int i = 0; i < deviceNumber; i++) {
            configuredDevices[i] = new ConfigurationDevice.DeviceInfo();
            configuredDevices[i].setaliveFlag(0);
            configuredDevices[i].setName("");
            configuredDevices[i].setIP("");
            configuredDevices[i].setmacAdrress("");
            configuredDevices[i].setimg(null);
            configuredDevices[i].setconnectedflag(false);
        }
    }

    private void showConfiguredList() {
        ShowCfgSteptwo = false;
        ConfigureAPProFlag = false;
        SCLib.rtk_sc_stop();
        handler_pd.sendEmptyMessage(0);

        final List<HashMap<String, Object>> InfoList = new ArrayList<HashMap<String, Object>>();
        String[] deviceList = null;

        final int itemNum = SCLib.rtk_sc_get_connected_sta_num();

        SCLib.rtk_sc_get_connected_sta_info(InfoList);

        final boolean[] isSelectedArray = new boolean[itemNum];
        Arrays.fill(isSelectedArray, Boolean.TRUE);

        resetData();

        // input data
        if (itemNum > 0) {
            deviceList = new String[itemNum];
            for (int i = 0; i < itemNum; i++) {

                configuredDevices[i].setaliveFlag(1);

                if (InfoList.get(i).get("Name") == null)
                    configuredDevices[i].setName((String) InfoList.get(i).get("MAC"));
                else
                    configuredDevices[i].setName((String) InfoList.get(i).get("Name"));

                configuredDevices[i].setmacAdrress((String) InfoList.get(i).get("MAC"));
                configuredDevices[i].setIP((String) InfoList.get(i).get("IP"));

                deviceList[i] = configuredDevices[i].getName();
            }
        } else {
            if (TimesupFlag_cfg) {
                AlertDialog.Builder alert_timeout = new AlertDialog.Builder(mActivity);
                alert_timeout.setCancelable(false);
                // switch password input type
                alert_timeout.setTitle("Configure Timeout");
                alert_timeout.setCancelable(false);
                alert_timeout.setPositiveButton("OK", null);
                alert_timeout.show();
            }

            handler_pd.sendEmptyMessage(0);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setCancelable(false);

        Toast toast = Toast.makeText(mActivity, aboutMsg,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 100);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(20);
        toast.show();

        builder.setTitle("Configured Device");
        builder.setIcon(android.R.drawable.ic_dialog_info);

        // Log.d(TAG,"show Configured itmeNum:"+itemNum);

        builder.setMultiChoiceItems(deviceList, isSelectedArray,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isSelectedArray[which] = isChecked;
                    }
                });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // String selectedStr = "un-configured state:\n";
                int delDeviceNumber = 0;
                String content = "";
                String name = "";
                String mac = "";
                byte[] buff = new byte[256]; // input stream buffer
                PINGet = pinCodeText;

                byte[] pinget = PINGet.getBytes();
                byte[] pinset;
                if (pinget.length > 0) {
                    if (pinget.length < 8) {
                        pinset = new byte[8];
                        System.arraycopy(pinget, 0, pinset, 0,
                                pinget.length);
                        for (int i = pinget.length; i < 8; i++) {
                            pinset[i] = '0';
                        }
                    } else if (pinget.length >= 8
                            && pinget.length <= 64) {
                        pinset = new byte[pinget.length];
                        System.arraycopy(pinget, 0, pinset, 0,
                                pinget.length);
                    } else {
                        pinset = new byte[64];
                        System.arraycopy(pinget, 0, pinset, 0, 64);
                    }
                    PINSet = new String(pinset);
                } else {
                    PINSet = new String(pinget);
                }
                fileOps.UpdateCfgPinFile((PINSet != null && PINSet
                        .length() > 0) ? PINSet : "null");

                // store the pin code
                // content: bssid,pin;bssid,pin;.....
                if (presave_pinCode.length() > 0) {
                    try {
                        FileInputStream reader = mActivity.openFileInput(pinfileName);
                        while ((reader.read(buff)) != -1) {
                            content += new String(buff).trim();
                        }
                        reader.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "pin code FileNotFoundException "
                                + content);
                    } catch (IOException e) {
                        Log.e(TAG, "pin code IOException " + content);
                    }
                    // Log.d(TAG,"PIN Code info: "+content);

                    // Write pin into file
                    if (content.length() > 0) {
                        String[] DeviceItem = content.split(";");
                        boolean isDiffBSSID = false; // check all
                        // different
                        // SSID
                        int itemNumber = DeviceItem.length;
                        int CompearNumber = 0;

                        for (int i = 0; i < itemNumber; i++) {
                            String[] array = DeviceItem[i].split(",");
                            if (!configuredDevices[0].getmacAdrress()
                                    .equals(array[0])) {// bssid
                                // is
                                // different
                                // ,
                                // no
                                // store
                                CompearNumber++;
                            }
                        }

                        if (itemNumber == CompearNumber)
                            isDiffBSSID = true;

                        if (isDiffBSSID) {// new bssid
                            // store pin into
                            // file
                            String spilt = ",";
                            String spiltEnd = ";";
                            FileOutputStream writer;
                            try {
                                writer = mActivity.openFileOutput(pinfileName,
                                        Context.MODE_APPEND);
                                writer.write(configuredDevices[0]
                                        .getmacAdrress().getBytes());
                                writer.write(spilt.getBytes());
                                writer.write(presave_pinCode.getBytes());
                                writer.write(spiltEnd.getBytes());
                                writer.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else { // update pin
                            String tmpData = content;
                            String[] tmpDeviceItem = tmpData.split(";");
                            String newData = "";
                            String[] array;
                            for (int i = 0; i < itemNumber; i++) {
                                array = tmpDeviceItem[i].split(",");
                                if (configuredDevices[0].getmacAdrress().equals(array[0])) {
                                    newData = array[0] + "," + presave_pinCode + ";";
                                } else {
                                    newData = array[0] + "," + array[1] + ";";
                                }
                            }

                            FileOutputStream writer;
                            try {
                                writer = mActivity.openFileOutput(pinfileName,
                                        Context.MODE_PRIVATE);
                                writer.write(newData.getBytes());
                                writer.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        // store pin into file directly if origin is
                        // empty
                        String spilt = ",";
                        String spiltEnd = ";";
                        FileOutputStream writer;
                        try {
                            writer = mActivity.openFileOutput(pinfileName, Context.MODE_APPEND);
                            writer.write(configuredDevices[0].getmacAdrress().getBytes());
                            writer.write(spilt.getBytes());
                            writer.write(presave_pinCode.getBytes());
                            writer.write(spiltEnd.getBytes());
                            writer.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                for (int _i = 0; _i < itemNum; _i++) {
                    if (!isSelectedArray[_i]) {
                        delDeviceNumber++;
                    }
                }
                if (delDeviceNumber > 0) {
                    delConfirmIP = new String[delDeviceNumber];
                    for (int _i = 0; _i < delDeviceNumber; _i++)
                        delConfirmIP[_i] = "";
                }

                int j = 0;
                boolean isDelDevice = false;

                for (int _i = 0; _i < itemNum; _i++) {
                    if (!isSelectedArray[_i]) {// uncheck device
                        // selectedStr =
                        // selectedStr+"\n"+configuredDevices[_i];

                        configuredDevices[_i].setaliveFlag(0);

                        delConfirmIP[j++] = InfoList.get(_i).get("IP").toString();
                        CurrentControlIP = InfoList.get(_i).get("IP").toString();
                        name = (String) InfoList.get(_i).get("Name").toString();
                        mac = (String) InfoList.get(_i).get("MAC").toString();
                        Log.d(TAG, "uncheck device: " + name);

                        // get PIN code
                        // check device is ready via search service
                        uncheckDevice_SC(name, mac, 60000);

                        isDelDevice = true;
                    }
                }

                if (!isDelDevice) {
                    Log.d(TAG, "search directly");
                    SCCtlOps.rtk_sc_control_reset();

                    DiscovEnable = true;
                }

                adapter_deviceInfo = new SimpleAdapter(
                        mActivity.getApplicationContext(),
                        getData_confirm_Device(),
                        R.layout.device_list_confirm, new String[]{
                        "title", "info"}, new int[]{
                        R.id.title, R.id.info});

                listView.setAdapter(adapter_deviceInfo);
                if (adapter_deviceInfo.getCount() > 0)
                    setListViewHeightBasedOnChildren(listView);
                else if (adapter_deviceInfo.getCount() == 0) {
                    listView.setAdapter(null);
                }

            }

        });
        builder.create();
        builder.show();
    }

    private void setListViewHeightBasedOnChildren(ListView adjuestlistView) {

        ListAdapter listAdapter = adjuestlistView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        View listItem = null;
        int totalHeight = 0;

        listItem = listAdapter.getView(0, null, adjuestlistView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight() * listAdapter.getCount();

        ViewGroup.LayoutParams params = adjuestlistView.getLayoutParams();
        params.height = totalHeight
                + (adjuestlistView.getDividerHeight() * (listAdapter.getCount() - 1));
        adjuestlistView.setLayoutParams(params);
    }


    private void uncheckDevice_SC(final String name, final String mac,
                                  final int scanTimeout) {

        uncheckDevice = true;

        new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);

                int i = 0;
                String initIP = "0.0.0.0";
                List<HashMap<String, Object>> DevInfo;
                DevInfo = new ArrayList<HashMap<String, Object>>();

                byte[] DiscovCmdBuf = SCCtlOps.rtk_sc_gen_discover_packet(SCLib
                        .rtk_sc_get_default_pin());
                long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();

                while (uncheckDevice && (endTime - startTime) < scanTimeout) {

                    SCCtlOps.rtk_sc_control_reset();
                    try {
                        Thread.sleep(500);
                        for (i = 0; i < 10; i++) {
                            SCLib.rtk_sc_send_discover_packet(DiscovCmdBuf,
                                    "255.255.255.255");
                        }
                        // Log.d(TAG,"scan-discovery: " + IP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    endTime = System.currentTimeMillis();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DevInfo = new ArrayList<HashMap<String, Object>>();
                    SCCtlOps.rtk_sc_get_discovered_dev_info(DevInfo);
                    for (i = 0; i < SCCtlOps.rtk_sc_get_discovered_dev_num(); i++) {
                        Log.i(TAG, "check: " + mac + "<->"
                                + (String) DevInfo.get(i).get("MAC") + "  IP:"
                                + (String) DevInfo.get(i).get("IP"));
                        if (mac.equals(DevInfo.get(i).get("MAC"))
                                && !initIP.equals((String) DevInfo.get(i).get(
                                "IP"))) {
                            CurrentControlIP = (String) DevInfo.get(i).get("IP");
                            initIP = CurrentControlIP;
                            uncheckDevice = false;
                            Log.d(TAG, "uncheck :" + CurrentControlIP + " pin:"
                                    + pinCodeText);
                        }
                    }
                }

                if (!initIP.equals("0.0.0.0")) {
                    if (pinCodeText.length() == 0) {
                        pinCodeText = SCLib.rtk_sc_get_default_pin();
                        PINSet = SCLib.rtk_sc_get_default_pin();
                    }
                    Log.i(TAG, "to delete :" + CurrentControlIP + " pin:"
                            + pinCodeText);
                    SendCtlDevPacket(SCCtlOps.Flag.DelProf, pinCodeText, CurrentControlIP, null);
                }

                uncheckDevice = false;
            }
        }).start();

    }

    public void SendCtlDevPacket(final int flag, final String pin,
                                 final String ip, final String new_name) {
        // Log.d(TAG, "ip: " + ip);
        // Log.d(TAG, "pin: " + pin);
        // Log.d(TAG, "name: " + new_name);

        new Thread(new Runnable() {
            int count = 0;

            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                byte[] buf = SCCtlOps.rtk_sc_gen_control_packet(flag,
                        SCLib.rtk_sc_get_default_pin(), pin, new_name);

                while (count < 6) {
                    try {
                        Thread.sleep(1);
                        SCLib.rtk_sc_send_control_packet(buf, ip);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count++;
                }
            }
        }).start();
    }

    private List<? extends Map<String, ?>> getData_confirm_Device() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        int i = 0;

        Log.i(TAG, "========== CONFIRM ==========");
        for (i = 0; i < deviceNumber; i++) {
            if (configuredDevices[i].getaliveFlag() == 1) {
                Log.i(TAG, configuredDevices[i].getName() + " ("
                        + configuredDevices[i].getmacAdrress() + ") "
                        + configuredDevices[i].getIP());

                map = new HashMap<String, Object>();
                map.put("title", configuredDevices[i].getName());
                map.put("info", configuredDevices[i].getmacAdrress()
                        + "   Connected");
                list.add(map);
            }
        }

        isScanFinish = false;

        return list;
    }

}
