package com.coband.dfu.network;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Environment;
import android.util.Log;

import com.C;
import com.coband.dfu.network.bean.AllFwResultBean;
import com.coband.dfu.network.bean.FwBean;
import com.coband.dfu.network.bean.FwResultBean;
import com.coband.dfu.network.bean.LastFirmwareResultBean;
import com.coband.dfu.network.bean.LastFwBean;
import com.coband.interactivelayer.manager.CommandManager;
import com.coband.protocollayer.gattlayer.GlobalGatt;
import com.coband.utils.FOTAConfig;
import com.coband.utils.LogUtils;
import com.coband.utils.RandomString;
import com.coband.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;

public class OTAService {
    // LOG
    private static final boolean D = true;
    private static final String TAG = "OTAService";

    // Support Service UUID and Characteristic UUID
    private final static UUID OTA_SERVICE_UUID = UUID.fromString("0000d0ff-3c17-d293-8e48-14fe2e4da212");
    private final static UUID OTA_CHARACTERISTIC_UUID = UUID.fromString("0000ffd1-0000-1000-8000-00805f9b34fb");
    private final static UUID OTA_READ_PATCH_CHARACTERISTIC_UUID = UUID.fromString("0000ffd3-0000-1000-8000-00805f9b34fb");
    private final static UUID OTA_READ_APP_CHARACTERISTIC_UUID = UUID.fromString("0000ffd4-0000-1000-8000-00805f9b34fb");

    public final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    public static final UUID DFU_SERVICE_UUID = UUID.fromString("00006287-3c17-d293-8e48-14fe2e4da212");
    // Add for extend OTA upload.
    private final static UUID OTA_EXTEND_FLASH_CHARACTERISTIC_UUID = UUID.fromString("00006587-3c17-d293-8e48-14fe2e4da212");

    // Support Service object and Characteristic object
    private BluetoothGattService mService;
    private BluetoothGattCharacteristic mAppCharac;
    private BluetoothGattCharacteristic mPatchCharac;

    private BluetoothGattService mDfuService;
    private BluetoothGattCharacteristic mExtendCharac;

    // Current firmware version
    private int mAppValue = -1;
    private int mPatchValue = -1;

    private GlobalGatt mGlobalGatt;

    private OnServiceListener mCallback;

    private String mBluetoothAddress;
    //    private String mFWPath;
    private String mFileName;

    private FwResultBean mFwResultBean;
    private LastFirmwareResultBean mLastFirmwareResultBean;
    private AllFwResultBean mAllFwResultBean;

    private static final String DEFAULT_FIRMWARE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fw/";

    public OTAService(String addr, OnServiceListener callback) {
        mCallback = callback;
        mBluetoothAddress = addr;

        mGlobalGatt = GlobalGatt.getInstance();
        initial();
    }

    public void close() {
        mGlobalGatt.unRegisterCallback(mGattCallback);
    }

    private void initial() {
//        mFWPath = Environment.getDownloadCacheDirectory().getAbsolutePath() + "/fw/";
        LogUtils.d(TAG, ">>> OTAService initial");
        // register service discovery callback
        mGlobalGatt.registerCallback(mGattCallback);
    }

    public boolean setService(BluetoothGattService service) {
        if (service.getUuid().equals(OTA_SERVICE_UUID)) {
            mService = service;
            return true;
        }
        return false;
    }

    public List<BluetoothGattCharacteristic> getNotifyCharacteristic() {
        return null;
    }

    public boolean readInfo() {
        if (mAppCharac == null || mPatchCharac == null) {
            if (D) Log.e(TAG, "read Version info error with null charac");
            return false;
        }
        if (D) Log.d(TAG, "read Version info.");
        return readDeviceInfo(mAppCharac);
    }

    public String getServiceUUID() {
        return OTA_SERVICE_UUID.toString();
    }

    public String getServiceSimpleName() {
        return "Dfu";
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mService = gatt.getService(OTA_SERVICE_UUID);
                if (mService == null) {
                    Log.e(TAG, "OTA service not found");
                    return;
                } else {
                    mPatchCharac = mService.getCharacteristic(OTA_READ_PATCH_CHARACTERISTIC_UUID);
                    if (mPatchCharac == null) {
                        if (D) Log.e(TAG, "OTA Patch characteristic not found");
                        return;
                    } else {
                        if (D)
                            Log.d(TAG, "OTA Patch characteristic is found, mPatchCharac: " + mPatchCharac.getUuid());
                    }
                    mAppCharac = mService.getCharacteristic(OTA_READ_APP_CHARACTERISTIC_UUID);
                    if (mAppCharac == null) {
                        if (D) Log.e(TAG, "OTA App characteristic not found");
                        return;
                    } else {
                        if (D)
                            Log.d(TAG, "OTA App characteristic is found, mAppCharac: " + mAppCharac.getUuid());
                    }
                }

                mDfuService = gatt.getService(DFU_SERVICE_UUID);
                if (mDfuService == null) {
                    if (D) Log.e(TAG, "Dfu Service not found");
                    return;
                } else {
                    if (D)
                        Log.d(TAG, "Dfu Service is found, mDfuService: " + mDfuService.getUuid());
                    mExtendCharac = mDfuService.getCharacteristic(OTA_EXTEND_FLASH_CHARACTERISTIC_UUID);
                    if (mExtendCharac == null) {
                        if (D) Log.e(TAG, "Dfu extend characteristic not found");
                        return;
                    } else {
                        if (D)
                            Log.d(TAG, "Dfu extend characteristic is found, mExtendCharac: " + mExtendCharac.getUuid());
                    }
                }
                //isConnected = true;
            } else {
                if (D) Log.e(TAG, "Discovery service error: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //if(D) Log.d(TAG, "onCharacteristicRead UUID is: " + characteristic.getUuid() + ", addr: " +mBluetoothAddress);
            //if(D) Log.d(TAG, "onCharacteristicRead data value:"+ Arrays.toString(characteristic.getValue()) + ", addr: " +mBluetoothAddress);
            byte[] data = characteristic.getValue();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().equals(OTA_READ_APP_CHARACTERISTIC_UUID)) {
                    if (D) Log.d(TAG, "data = " + Arrays.toString(characteristic.getValue()));
                    byte[] appVersionValue = characteristic.getValue();
                    ByteBuffer wrapped = ByteBuffer.wrap(appVersionValue);
                    wrapped.order(ByteOrder.LITTLE_ENDIAN);
                    mAppValue = wrapped.getShort(0);

                    //mTargetVersionView.setText(String.valueOf(oldFwVersion));
                    if (D)
                        Log.d(TAG, "old firmware version: " + mAppValue + " .getValue=" + Arrays.toString(characteristic.getValue()));
                    if (mPatchCharac != null) {
                        readDeviceInfo(mPatchCharac);
                    }
                } else if (characteristic.getUuid().equals(OTA_READ_PATCH_CHARACTERISTIC_UUID)) {
                    byte[] patchVersionValue = characteristic.getValue();
                    ByteBuffer wrapped = ByteBuffer.wrap(patchVersionValue);
                    wrapped.order(ByteOrder.LITTLE_ENDIAN);
                    mPatchValue = wrapped.getShort(0);
                    if (D)
                        Log.d(TAG, "old patch version: " + mPatchValue + " .getValue=" + Arrays.toString(characteristic.getValue()));
                    //here can add read other characteristic
                    mCallback.onVersionRead(mAppValue, mPatchValue);
                }
            }

        }
    };

    public boolean checkSupportedExtendFlash() {
        return mExtendCharac != null;
    }

    private boolean readDeviceInfo(BluetoothGattCharacteristic characteristic) {
        if (D) Log.d(TAG, "read readDeviceinfo:" + characteristic.getUuid().toString());
        if (characteristic != null) {
            return mGlobalGatt.readCharacteristic(characteristic);
        } else {
            if (D) Log.e(TAG, "readDeviceinfo Characteristic is null");
        }
        return false;
    }

    public int getAppValue() {
        return mAppValue;
    }

    public int getPatchValue() {
        return mPatchValue;
    }

    /**
     * Interface required to be implemented by activity
     */
    public static interface OnServiceListener {
        /**
         * Fired when value come.
         *
         * @param appVersion   app Version value
         * @param patchVersion patch Version value
         */
        public void onVersionRead(int appVersion, int patchVersion);

        /**
         * @param code code == 0，found the new firmware。
         *             code != 0，Did not find a new firmware, or the server side of the error,
         *             or parameters wrong, the specific reference to the following error code:
         *             CheckInNoNewVersion = 40200
         *             ErrNumCheckInFailed = 40201
         *             ErrNumCheckInWrongParameter = 40202
         *             ErrNumCheckInResourceNofound = 40203
         */
        void noNewVersion(int code, String message);

        void hasNewVersion(String description, String version, String path);


        void allFirmware(ArrayList<AllFwResultBean.PayloadBean> firmwareList);

        /**
         * Download progress
         *
         * @param progressRate Download the progress of the firmware , Ranges from 0 to 100
         */
        void downloadProgress(int progressRate);

        /**
         * Download error
         */
        void error(Throwable e,
                   @CommandManager.CheckFirmwareUpdatesError int errorCode);

        /**
         * Download complete
         *
         * @param fwPath Firmware path
         */
        void downloadComplete(String fwPath);
    }

    private PublishSubject<Integer> mDownloadProgress = PublishSubject.create();

    public void checkNewFWVersion(final String appVersion, final String userId, final String vendor,
                                  final String deviceType) {

        String nonce = new RandomString(12).nextString();
        String timestamp = "" + (System.currentTimeMillis() / 1000);


        Api.getInstance().service
                .checkFw(timestamp, appVersion, nonce,
                        Utils.hmacSHA1Encrypt(FOTAConfig.K9_APP_KEY + timestamp + nonce,
                                FOTAConfig.K9_APP_SECRET),
                        new FwBean(userId, vendor, deviceType, C.FW_TYPE_APP, mBluetoothAddress,
                                "" + mAppValue))
                .filter(new Predicate<FwResultBean>() {
                    @Override
                    public boolean test(FwResultBean fwResultBean) throws Exception {
                        mFwResultBean = fwResultBean;
                        return fwResultBean.code == 0;
                    }
                })
                .switchIfEmpty(new ObservableSource<FwResultBean>() {
                    @Override
                    public void subscribe(Observer<? super FwResultBean> observer) {
                        Observable.just(new Object()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object object) throws Exception {
                                        mCallback.noNewVersion(mFwResultBean.code, mFwResultBean.errorStr);
                                    }
                                });
                        observer.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FwResultBean>() {
                    @Override
                    public void accept(FwResultBean bean) throws Exception {
                        verifyLocalFirmware(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.d(TAG, "check fw throwable >>>>>>> " + throwable.getMessage());
                        mCallback.error(throwable, CommandManager.CheckFirmwareUpdatesError.CHECK_VERSION_ERROR);
                    }
                });


//        flatMap(Api.getInstance().service
//                .checkFw(timestamp, appVersion, nonce,
//                        Utils.hmacSHA1Encrypt(FOTAConfig.K9_APP_KEY + timestamp + nonce,
//                                FOTAConfig.K9_APP_SECRET),
//                        new FwBean(userId, vendor, deviceType, C.FW_TYPE_APP, mBluetoothAddress,
//                                "" + mAppValue))
//                .filter(new Predicate<FwResultBean>() {
//                    @Override
//                    public boolean test(FwResultBean fwResultBean) throws Exception {
//                        mFwResultBean = fwResultBean;
//                        LogUtils.d(TAG, "fwResultBean >>>>>>>> " + fwResultBean);
//                        return fwResultBean.code == 0;
//                    }
//                })
//                .switchIfEmpty(new ObservableSource<FwResultBean>() {
//                    @Override
//                    public void subscribe(Observer<? super FwResultBean> observer) {
//                        observer.onComplete();
//                        checkNewPatchVersion(appVersion, userId, vendor, deviceType);
//                    }
//                }))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String filePath) throws Exception {
//                        Log.d(TAG, "filePath: " + filePath);
//                        mCallback.hasNewVersion(mFwResultBean.payload.description,
//                                mFwResultBean.payload.version, filePath);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        mCallback.error(throwable, CommandManager.CheckFirmwareUpdatesError.CHECK_PATCH_ERROR);
//                        Log.e(TAG, ">>>>>>>>>>>>" + throwable.getMessage());
//                    }
//                });
    }


    private void verifyLocalFirmware(final FwResultBean bean) {
        File dir = new File(DEFAULT_FIRMWARE_DIR);
        if (!dir.exists()) {
            mCallback.hasNewVersion(bean.payload.description,
                    bean.payload.version, "");
            return;
        }

        File[] files = dir.listFiles();

        Observable.fromArray(files)
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) throws Exception {
                        return bean.payload.md5sum.equals(Utils.getMd5ByFile(file));
                    }
                })
                .switchIfEmpty(new Observable<File>() {
                    @Override
                    protected void subscribeActual(Observer<? super File> observer) {
                        Observable.just(new Object()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object object) throws Exception {
                                        mCallback.hasNewVersion(bean.payload.description,
                                                bean.payload.version, "");
                                    }
                                });
                        observer.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        mCallback.hasNewVersion(bean.payload.description,
                                bean.payload.version, file.getAbsolutePath());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, throwable.getLocalizedMessage());
                    }
                });
    }

    public void checkNewPatchVersion(String appVersion, String userId, String vendor, String deviceType) {
        String nonce = new RandomString(12).nextString();
        String timestamp = "" + (System.currentTimeMillis() / 1000);

        flatMap(Api.getInstance().service
                .checkFw(timestamp, appVersion, nonce,
                        Utils.hmacSHA1Encrypt(FOTAConfig.K9_APP_KEY + timestamp + nonce, FOTAConfig.K9_APP_SECRET),
                        new FwBean(userId, vendor, deviceType, C.FW_TYPE_PATCH, mBluetoothAddress, "" + mPatchValue))
                .filter(new Predicate<FwResultBean>() {
                    @Override
                    public boolean test(FwResultBean fwResultBean) throws Exception {
                        mFwResultBean = fwResultBean;
                        return fwResultBean.code == 0;
                    }
                })
                .switchIfEmpty(new ObservableSource<FwResultBean>() {
                    @Override
                    public void subscribe(Observer<? super FwResultBean> observer) {
                        Observable.just(new Object()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object object) throws Exception {
                                        mCallback.noNewVersion(mFwResultBean.code, mFwResultBean.errorStr);
                                    }
                                });
                        observer.onComplete();
                    }
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String filePath) throws Exception {
                        Log.d(TAG, "filePath: " + filePath);
                        mCallback.hasNewVersion(mFwResultBean.payload.description,
                                mFwResultBean.payload.version, filePath);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mCallback.error(throwable, CommandManager.CheckFirmwareUpdatesError.CHECK_PATCH_ERROR);
                        Log.e(TAG, ">>>>>>>>>>>>" + throwable.getMessage());
                    }
                });
    }


    private Observable<String> flatMap(Observable<FwResultBean> observable) {
        return observable.flatMap(new Function<FwResultBean, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(final FwResultBean fwResultBean) throws Exception {
                return Observable.fromArray(new File(DEFAULT_FIRMWARE_DIR).listFiles())
                        .map(new Function<File, String>() {
                            @Override
                            public String apply(File file) throws Exception {
                                return file.getAbsolutePath();
                            }
                        })
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String file) throws Exception {
                                return fwResultBean.payload.md5sum.equals(Utils.getMd5ByFile(file));
                            }
                        })
                        .switchIfEmpty(new ObservableSource<String>() {
                            @Override
                            public void subscribe(Observer<? super String> observer) {
                                observer.onNext("");
                                observer.onComplete();
                            }
                        });
            }
        });
    }


    /**
     * get all able Firmware , just for test
     *
     * @param userId
     * @param vendor
     * @param deviceType
     */
    public void checkAllNewFWVersion(final String userId, final String vendor, final String deviceType) {
        Api.getInstance().service
                .checkAllFw(new FwBean(userId, vendor, deviceType, C.FW_TYPE_APP, mBluetoothAddress, "" + mAppValue))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllFwResultBean>() {
                    @Override
                    public void accept(AllFwResultBean allFwResultBean) throws Exception {
                        mAllFwResultBean = allFwResultBean;
                        mCallback.allFirmware(allFwResultBean.payload);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mCallback.error(throwable, CommandManager.CheckFirmwareUpdatesError.CHECK_VERSION_ERROR);
                        Log.e(TAG, "checkAllNewFWVersion>>>>>>>>>>>>" + throwable.getMessage());
                    }
                });
    }

    public void checkLastFirmwares(String userId, String appVersion, String patchVersion) {
        mLastFirmwareResultBean = null;
        String nonce = new RandomString(12).nextString();
        String timestamp = "" + (System.currentTimeMillis() / 1000);
        Api.getInstance().service.checkLastFirmwares(timestamp, nonce,
                Utils.hmacSHA1Encrypt(FOTAConfig.K9_APP_KEY + timestamp + nonce, FOTAConfig.K9_APP_SECRET),
                new LastFwBean(userId, mBluetoothAddress, appVersion, patchVersion))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LastFirmwareResultBean>() {
                    @Override
                    public void accept(LastFirmwareResultBean lastFirmwareResultBean) throws Exception {
                        if (lastFirmwareResultBean.code != 0) {
                            mCallback.noNewVersion(lastFirmwareResultBean.code, "");
                            return;
                        }
                        File file = new File(DEFAULT_FIRMWARE_DIR);
                        if (file.exists() && file.isDirectory()) {
                            String[] fileList = file.list();
                            for (int i = 0; i < fileList.length; i++) {
                                String filePath = fileList[i];
                                String md5ByFile = Utils.getMd5ByFile(new File(DEFAULT_FIRMWARE_DIR + filePath));
                                String md5sum = lastFirmwareResultBean.payload.get(0).md5sum;
                                if (md5ByFile.equals(md5sum)) {
                                    mCallback.hasNewVersion("", "", DEFAULT_FIRMWARE_DIR + filePath);
                                    return;
                                }
                            }
                        }
                        mLastFirmwareResultBean = lastFirmwareResultBean;
                        download(lastFirmwareResultBean.payload.get(0).resourceUrl);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mCallback.error(throwable, CommandManager.CheckFirmwareUpdatesError.CHECK_VERSION_ERROR);
                        Log.e(TAG, "checkLastFirmwares>>>>>>>>>>>>" + throwable.getMessage());
                    }
                });
    }

    public void downloadFirmware() {
        if (mFwResultBean == null) {
            mCallback.error(new Throwable("upgrade message is null"),
                    CommandManager.CheckFirmwareUpdatesError.DOWNLOAD_ERROR);
            return;
        }

        if (mFwResultBean.code == 0) {
            Log.d(TAG, "(fwResultBean.code == 0)1");

            File file = new File(DEFAULT_FIRMWARE_DIR);
            if (!file.exists()) {
                boolean create = file.mkdirs();
                if (!create) {
                    LogUtils.d(TAG, "create dir error >>>>>>>>");
                    mCallback.error(new Throwable("create directory failed"),
                            CommandManager.CheckFirmwareUpdatesError.DOWNLOAD_ERROR);
                    return;
                }
            }

            mFileName = mFwResultBean.payload.resourceUrl.substring(mFwResultBean.payload.resourceUrl.lastIndexOf("/") + 1);
            Log.d(TAG, "(fwResultBean.code == 0)" + mFileName);

            mDownloadProgress = PublishSubject.create();
            mDownloadProgress.distinct()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            LogUtils.d(TAG, "onSubscribe ");
                        }

                        @Override
                        public void onNext(Integer value) {
                            LogUtils.d(TAG, "downloadProgress " + value);
                            mCallback.downloadProgress(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.d(TAG, "onError ");

                            mCallback.error(e, CommandManager.CheckFirmwareUpdatesError.DOWNLOAD_ERROR);
                        }

                        @Override
                        public void onComplete() {
                            LogUtils.d(TAG, "onComplete ");
                            mCallback.downloadComplete(DEFAULT_FIRMWARE_DIR + mFileName);
                        }
                    });

            Api.getInstance().service
                    .downloadFile(mFwResultBean.payload.resourceUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            Log.d(TAG, "subscribe");
                            save(responseBody.byteStream(), responseBody.contentLength(), DEFAULT_FIRMWARE_DIR + mFileName);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, throwable.getMessage());
                            mDownloadProgress.onError(throwable);
                        }
                    });
        } else {
            mCallback.noNewVersion(mFwResultBean.code, mFwResultBean.errorStr);
        }
    }


    public void download(String url) {

        File file = new File(DEFAULT_FIRMWARE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        mFileName = url.substring(url.lastIndexOf("/") + 1);
        Log.d(TAG, "(fwResultBean.code == 0)" + mFileName);

        mDownloadProgress = PublishSubject.create();
        mDownloadProgress.distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d(TAG, "onSubscribe ");
                    }

                    @Override
                    public void onNext(Integer value) {
                        LogUtils.d(TAG, "downloadProgress " + value);
                        mCallback.downloadProgress(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "onError ");

                        mCallback.error(e, CommandManager.CheckFirmwareUpdatesError.DOWNLOAD_ERROR);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d(TAG, "onComplete ");
                        mCallback.downloadComplete(DEFAULT_FIRMWARE_DIR + mFileName);
                        if (null != mLastFirmwareResultBean) {
                            mCallback.hasNewVersion(mLastFirmwareResultBean.payload.get(0).description,
                                    mLastFirmwareResultBean.payload.get(0).version, DEFAULT_FIRMWARE_DIR);
                        }

                    }
                });

        Api.getInstance().service
                .downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        Log.d(TAG, "subscribe");
                        save(responseBody.byteStream(), responseBody.contentLength(), DEFAULT_FIRMWARE_DIR + mFileName);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mDownloadProgress.onError(throwable);
                        Log.e(TAG, throwable.getMessage());
                    }
                });

    }

    private void save(InputStream inputStream, long fileLength, String localPath) {
        File file = new File(localPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        Log.d(TAG, "save");

        try {
            outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            long total = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                total += len;
                LogUtils.d(TAG, "total += len ");
                if (fileLength > 0) {
                    LogUtils.d(TAG, "fileLength > 0 ");

                    int percentage = (int) (total * 100 / fileLength);
                    mDownloadProgress.onNext(percentage);
                }
                outputStream.write(bytes, 0, len);
            }
        } catch (Exception e) {
            mDownloadProgress.onError(e);
            Log.e(TAG, ">>>>>>>>>>" + e.getMessage());

            if (file.exists()) {
                file.delete();
            }
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception e) {
                Log.e(TAG, ">>>>>>>>>>" + e.getMessage());
                mDownloadProgress.onError(e);
                e.printStackTrace();
            }
            Log.d(TAG, "finally");
            mDownloadProgress.onComplete();
        }
    }
}
