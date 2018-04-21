package com.coband.cocoband.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.coband.App;
import com.coband.cocoband.BaseActivity;
import com.coband.cocoband.mvp.iview.MainView;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.presenter.MainPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by imco on 3/26/16.
 */
public class BandActivity extends BaseActivity implements MainView {

    private static final String TAG = "BandActivity ---> ";

    private static final int REQUEST_ENABLE_BT = 1;


    private TimerTask task;
    private Timer timer;

    //smart lost warn
    private Dialog mLostWarningDialog;
    private Uri mRingUri;
    private MediaPlayer mMediaPlayer;

    private boolean mBackPressedEnabled = true;

    private boolean isShowLostDialog = true;
//    private RelativeLayout mRelativeDrawerHeader;
//
//
//    private TextView mTextUserName;
//    private CircleImageView mImageAvatar;
//    private Fragment currenMainFragment;


    @Inject
    MainPresenter mMainPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_band;
    }

    @Override
    protected void init() {
        DBHelper.getInstance().getWeekSleep(DateUtils.getToday());

        AppComponent.getInstance().inject(this);
        mMainPresenter.attachView(this);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressedEnabled) {
            super.onBackPressed();
        }
    }


    public void setBackPressedEanbled(boolean enabled) {
        this.mBackPressedEnabled = enabled;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // don't modify this!!! we would save the fragments state while the activity recycle by system.
//        super.onSaveInstanceState(outState);

    }

    @Override
    protected void setupView() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        boolean isFirstReg = getIntent().getBooleanExtra("isFirstReg", false);
        MainFragment mainFragment = MainFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFirstReg", isFirstReg);
        mainFragment.setArguments(bundle);
        transaction.replace(R.id.content, mainFragment);
        transaction.commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void showDeviceSyncing() {
        showToast(R.string.syncing);
    }

    public void enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Logger.d(TAG, "no support bluetooth adapter ");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //MasterUser choose not to enable Bluetooth
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void networkUnavailable() {
        showToast(R.string.network_error);
    }

    @Override
    public void loginSuccess() {
        //TODO.
        showToast(R.string.sign_in);
    }

    @Override
    public void loginFailed() {
        //TODO
    }

    @Override
    public void bleNotSupport() {
        //TODO
        showToast(R.string.not_support_ble);
        finish();
    }

    @Override
    public void bluetoothUnavailable() {
        enableBluetooth();
    }

    @Override
    public void startWarning() {
        playLostWarningMusic();

        if (mLostWarningDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BandActivity.this, R.style.DialogTheme);
            builder.setTitle(getResources().getString(R.string.note_lost));
            builder.setMessage(getResources().getString(R.string.message_note_lost));
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }
                    isShowLostDialog = false;
                    CocoUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isShowLostDialog = true;
                        }
                    }, 40000);
                }
            });

            mLostWarningDialog = builder.create();
            mLostWarningDialog.show();
        } else if (!mLostWarningDialog.isShowing()) {
            mLostWarningDialog.show();
        }
    }

    private void playLostWarningMusic() {
        if (mRingUri == null) {
            mRingUri = RingtoneManager.getActualDefaultRingtoneUri(App.getContext(), RingtoneManager.TYPE_RINGTONE);
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(App.getContext(), mRingUri);
            mMediaPlayer.setLooping(true);
        }
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void stopWarning() {
        if (mLostWarningDialog != null && mLostWarningDialog.isShowing()) {
            mLostWarningDialog.dismiss();
        }

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(0);
        }
    }

    @Override
    public void showDeviceDisconnected() {
        showToast(R.string.device_disconnected);
    }

    @Override
    public void showSilenceCheckFirmware(int event) {
        switch (event) {
            case HandleEvent.CHECKING_FIRMWARE_VERSION_IN_SILENCE:
                showToast("正在后台检查固件版本...");
                break;
            case HandleEvent.NO_NEW_VERSION_IN_SILENCE:
                showToast("没有发现新的固件版本...");
                break;
            case HandleEvent.FOUND_NEW_VERSION_IN_SILENCE:
                showToast("发现新的固件版本...");
                break;
            case HandleEvent.CHECK_NEW_VERSION_FAILED_IN_SILENCE:
                showToast("检查新版本失败，请确认后台服务是否出现问题");
                break;
            case HandleEvent.DOWNLOAD_FIRMWARE_FAIL_IN_SILENCE:
                showToast("下载新版本固件失败，请确认后台服务是否出现问题");
                break;
            case HandleEvent.DOWNLOAD_FIRMWARE_SUCCESS_IN_SILENCE:
                showToast("新版本固件下载完毕");
                break;
            case HandleEvent.FOUND_NEW_VERSION_ALREADY_DOWNLOAD_IN_SILENCE:
                showToast("检查到新固件并且新固件已存在本地");
                break;
        }
    }

    @Override
    public void showSilenceDownloadFirmwareProgress(int progress) {
        showToast("下载进度......" + String.valueOf(progress));
    }

    @Override
    public void showFOTAServiceNotInit() {
        showToast(R.string.fota_service_not_init);
    }
}
