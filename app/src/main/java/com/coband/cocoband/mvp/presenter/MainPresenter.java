package com.coband.cocoband.mvp.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.MainView;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.bean.LoginInfo;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.tools.AppComponent;
import com.coband.common.utils.Logger;
import com.coband.common.utils.NetWorkUtil;
import com.coband.watchassistant.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import dagger.Module;


/**
 * Created by ivan on 17-4-12.
 */

@Module
public class MainPresenter extends BasePresenter {
    private MainView mView;
    private static final String TAG = "MainPresenter";
    @Inject
    LoginInfo mInfo;

    @Inject
    public MainPresenter() {
        AppComponent.getInstance().inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void attachView(BaseView view) {
        mView = (MainView) view;
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }

    public void login() {
        if (!isNetworkAvaible()) {
            mView.networkUnavailable();
            return;
        }
    }

    // TODO: 17-6-21  will be cause memory leak.
    public void initLCChatKit() {
        if (NetWorkUtil.isNetConnected()) {
            User user = DBHelper.getInstance().getUser();
            if (user != null) {
                LCChatKit.getInstance().open(user.getObjectId(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (null == e) {
                            List<String> muteList = PreferencesHelper.getConversationMute();
                            for (int i = 0; i < muteList.size(); i++) {
                                Logger.d("getConversationMute", muteList.get(i));
                                LCIMNotificationUtils.addTag(muteList.get(i));
                            }
                        } else {
                            Logger.e(TAG, e.toString());
                        }
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.LOGIN_SUCCESS:
                break;
            case HandleEvent.LOGIN_FAILED:
                break;
            case HandleEvent.UPGRADE_FIRMWARE_PROGRESS:
                break;
            case HandleEvent.UPGRADE_FIRMWARE_SERVER_BUSY:
                break;
            case HandleEvent.DOWNLOAD_FIRMWARE_FAIL:
                break;
            case HandleEvent.START_UPGRADE_FIRMWARE:
                break;
            case HandleEvent.UPGRADE_FIRMWARE_SUCCESS:
                break;
            case HandleEvent.UPGRADE_FIRMWARE_FAIL:
                break;
            case HandleEvent.MSG_SHOW_LOST_WARNING:
                Logger.d(TAG, "show lost warning >>>>>");
                mView.startWarning();
                break;
            case HandleEvent.MSG_STOP_LOST_WARNING:
                Logger.d(TAG, "stop lost warning >>>>>");
                mView.stopWarning();
                break;
            case HandleEvent.DEVICE_SYNCING:
                mView.showDeviceSyncing();
                break;
            case HandleEvent.SEND_COMMAND_DISCONNECTED:
                mView.showDeviceDisconnected();
                break;
//            case HandleEvent.CHECKING_FIRMWARE_VERSION_IN_SILENCE:
//            case HandleEvent.NO_NEW_VERSION_IN_SILENCE:
//            case HandleEvent.FOUND_NEW_VERSION_IN_SILENCE:
//            case HandleEvent.CHECK_NEW_VERSION_FAILED_IN_SILENCE:
//            case HandleEvent.DOWNLOAD_FIRMWARE_FAIL_IN_SILENCE:
//            case HandleEvent.DOWNLOAD_FIRMWARE_SUCCESS_IN_SILENCE:
//            case HandleEvent.FOUND_NEW_VERSION_ALREADY_DOWNLOAD_IN_SILENCE:
//                mView.showSilenceCheckFirmware(event.getTag());
//                break;
//            case HandleEvent.DOWNLOAD_FIRMWARE_PROGRESS_IN_SILENCE:
//                Logger.d(TAG, "progress >>>>>> " + (int) event.getObject());
//                mView.showSilenceDownloadFirmwareProgress((int) event.getObject());
//                break;
            case HandleEvent.UPDATE_FIRMWARE_SERVICE_NOT_INIT:
                mView.showFOTAServiceNotInit();
                break;
        }
    }
}
