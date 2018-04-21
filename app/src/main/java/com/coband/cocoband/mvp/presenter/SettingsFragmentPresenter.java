package com.coband.cocoband.mvp.presenter;

import com.coband.App;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.SettingsView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.response.UpdateAccountResponse;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.Config;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-6-2.
 */

@Module
public class SettingsFragmentPresenter extends BasePresenter {
    SettingsView iView;

    @Inject
    public SettingsFragmentPresenter() {
    }

    private void updateUnit(int unit) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            String unitSystem = unit == Config.METRIC ? Config.METRIC_STRING : Config.BRITISH_STRING;
            DataManager.getInstance().updateUnitSystem(unitSystem);
            iView.showUpdateAccountInfoSuccess(HandleEvent.UPDATE_UNIT_SYSTEM_SUCCESS);
            return;
        }


        iView.showLoading();
        String unitSystem = unit == Config.METRIC ? Config.METRIC_STRING : Config.BRITISH_STRING;
        NetworkOperation.getInstance().updateUnitSystem(unitSystem);
    }


    public void exit() {

        PreferencesHelper.setTodayData(0, 0, 0);
        DataManager.getInstance().setCurrentUid(null);
        iView.fragmentExit();
    }

    public void setUnitSystem(int unitSystem) {
        updateUnit(unitSystem);
//        iView.notifyItem();
    }

    public void clearCache() {
        deleteAll(App.getContext().getExternalCacheDir());
        deleteAll(App.getContext().getCacheDir());
    }

    private void deleteAll(File file) {
        if (null != file) {
            if (file.isFile() || file.list().length == 0) {
                file.delete();
            } else {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteAll(files[i]);
                    files[i].delete();
                }
                if (file.exists())         //如果文件本身就是目录 ，就要删除目录
                    file.delete();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.UPDATE_UNIT_SYSTEM_SUCCESS:
                UpdateAccountResponse response = (UpdateAccountResponse) event.getObject();
                if (response.getCode() == 0) {
                    String unitSystem = response.getPayload().getUser().getPersonalInfo().getUnitSystem();
                    DataManager.getInstance().updateUnitSystem(unitSystem);
                    iView.showUpdateAccountInfoSuccess(HandleEvent.UPDATE_UNIT_SYSTEM_SUCCESS);
                } else {
                    iView.showUpdateAccountInfoFailed(HandleEvent.UPDATE_UNIT_SYSTEM_FAILED,
                            response.getCode());
                }
                break;
            case HandleEvent.UPDATE_UNIT_SYSTEM_FAILED:
                String errorMsg = (String) event.getObject();
                iView.showUpdateAccountInfoFailed(HandleEvent.UPDATE_UNIT_SYSTEM_FAILED, errorMsg);
                break;
            case HandleEvent.MSG_DISCONNECTED:
                iView.showDeviceDisconnected();
                break;
            default:
                break;
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (SettingsView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }
}
