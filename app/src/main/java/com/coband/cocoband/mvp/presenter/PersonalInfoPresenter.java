package com.coband.cocoband.mvp.presenter;

import android.net.Uri;

import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.PersonalInfoView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.response.UpdateAccountResponse;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.Config;
import com.coband.common.utils.FileUtils;
import com.coband.common.utils.NetWorkUtil;
import com.coband.common.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-5-24.
 */

@Module
public class PersonalInfoPresenter extends BasePresenter {
    private String mAvatarPath;
    PersonalInfoView iView;
    private String mNickname;
    private String mGender;
    private String mBirthday;
    private int mHeight;
    private double mWeight;

    @Inject
    public PersonalInfoPresenter() {
    }

    @Subscribe
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.UPDATE_NICKNAME_FAILED:
                String nicknameErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPDATE_NICKNAME_FAILED, nicknameErrorMsg);
                break;
            case HandleEvent.UPDATE_NICKNAME_SUCCESS:
                UpdateAccountResponse nicknameResponse = (UpdateAccountResponse) event.getObject();
                if (nicknameResponse.getCode() == 0) {
                    iView.showUpdateAccountSuccess(HandleEvent.UPDATE_NICKNAME_SUCCESS);
                    DataManager.getInstance().updateNickname(mNickname);
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPDATE_NICKNAME_FAILED, nicknameResponse.getCode());
                }
                break;
            case HandleEvent.UPDATE_GENDER_FAILED:
                String genderErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPDATE_GENDER_FAILED, genderErrorMsg);
                break;
            case HandleEvent.UPDATE_GENDER_SUCCESS:
                UpdateAccountResponse genderResponse = (UpdateAccountResponse) event.getObject();
                if (genderResponse.getCode() == 0) {
                    iView.showUpdateAccountSuccess(HandleEvent.UPDATE_GENDER_SUCCESS);
                    DataManager.getInstance().updateGender(mGender);
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPDATE_GENDER_FAILED, genderResponse.getCode());
                }
                break;
            case HandleEvent.UPDATE_HEIGHT_FAILED:
                String heightErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPDATE_HEIGHT_FAILED, heightErrorMsg);
                break;
            case HandleEvent.UPDATE_HEIGHT_SUCCESS:
                UpdateAccountResponse heightResponse = (UpdateAccountResponse) event.getObject();
                if (heightResponse.getCode() == 0) {
                    iView.showUpdateAccountSuccess(HandleEvent.UPDATE_HEIGHT_SUCCESS);
                    DataManager.getInstance().updateHeight(mHeight);
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPDATE_HEIGHT_FAILED, heightResponse.getCode());
                }
                break;
            case HandleEvent.UPDATE_WEIGHT_FAILED:
                String weightErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPDATE_WEIGHT_FAILED, weightErrorMsg);
                break;
            case HandleEvent.UPDATE_WEIGHT_SUCCESS:
                UpdateAccountResponse weightResponse = (UpdateAccountResponse) event.getObject();
                if (weightResponse.getCode() == 0) {
                    iView.showUpdateAccountSuccess(HandleEvent.UPDATE_WEIGHT_SUCCESS);
                    DataManager.getInstance().updateWeight(mWeight);
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPDATE_WEIGHT_FAILED, weightResponse.getCode());
                }
                break;
            case HandleEvent.UPDATE_BIRTHDAY_FAILED:
                String birthdayErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPDATE_BIRTHDAY_FAILED, birthdayErrorMsg);
                break;
            case HandleEvent.UPDATE_BIRTHDAY_SUCCESS:
                UpdateAccountResponse birthdayResponse = (UpdateAccountResponse) event.getObject();
                if (birthdayResponse.getCode() == 0) {
                    iView.showUpdateAccountSuccess(HandleEvent.UPDATE_BIRTHDAY_SUCCESS);
                    DataManager.getInstance().updateBirthday(mBirthday);
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPDATE_BIRTHDAY_FAILED, birthdayResponse.getCode());
                }
                break;
            case HandleEvent.UPLOAD_AVATAR_SUCCESS:
                UpdateAccountResponse uploadAvatarResponse = (UpdateAccountResponse) event.getObject();
                if (uploadAvatarResponse.getCode() == 0) {
                    String avatarPath = uploadAvatarResponse.getPayload().getUser().getPersonalInfo()
                            .getAvatar().getPath();
                    String avatarMD5 = uploadAvatarResponse.getPayload().getUser().getPersonalInfo()
                            .getAvatar().getMd5();
                    DataManager.getInstance().updateAvatar(avatarPath, avatarMD5);
                    iView.showUpdateAccountSuccess(HandleEvent.UPLOAD_AVATAR_SUCCESS);
                    showLocalHeadPic();
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPLOAD_AVATAR_FAILED, uploadAvatarResponse.getCode());
                }
                break;
            case HandleEvent.UPLOAD_AVATAR_FAILED:
                String uploadAvatarErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPLOAD_AVATAR_FAILED, uploadAvatarErrorMsg);
                break;
            case HandleEvent.UPLOAD_BACKGROUND_SUCCESS:
                UpdateAccountResponse uploadBackgroundResponse = (UpdateAccountResponse) event.getObject();
                if (uploadBackgroundResponse.getCode() == 0) {
                    String bgPath = uploadBackgroundResponse.getPayload().getUser().getPersonalInfo()
                            .getBackground().getPath();
                    String bgMD5 = uploadBackgroundResponse.getPayload().getUser().getPersonalInfo()
                            .getBackground().getMd5();
                    DataManager.getInstance().updateBackground(bgPath, bgMD5);
                    iView.showUpdateAccountSuccess(HandleEvent.UPLOAD_BACKGROUND_SUCCESS);
                } else {
                    iView.showUpdateAccountFailed(HandleEvent.UPLOAD_BACKGROUND_FAILED, uploadBackgroundResponse.getCode());
                }
                break;
            case HandleEvent.UPLOAD_BACKGROUND_FAILED:
                String uploadBackgroundErrorMsg = (String) event.getObject();
                iView.showUpdateAccountFailed(HandleEvent.UPLOAD_BACKGROUND_FAILED, uploadBackgroundErrorMsg);
                break;
            default:
                break;
        }
    }

    public void saveImg(Uri imgUri, int type) {
        String path = imgUri.getPath();
        String modifyPath;
        FileUtils fileUtilsInst = FileUtils.getInst();
        if (type == 1) {
            modifyPath = modifyFileName(path, "avatar.png");
            mAvatarPath = modifyPath;
            fileUtilsInst.renameFile(path, modifyPath);
            updateAccountAvatar(modifyPath);
        } else {
            modifyPath = modifyFileName(path, "coverImg.png");
            fileUtilsInst.renameFile(path, modifyPath);
            updateAccountBackground(modifyPath);
        }
    }

    private String modifyFileName(String path, String replaceString) {
        StringBuffer buffer = new StringBuffer(path);
        int i = path.lastIndexOf("/") + 1;
        buffer.replace(i, path.length(), replaceString);
        return buffer.toString();
    }

    private void showLocalHeadPic() {
        if (null != mAvatarPath) {
            iView.showLocalHeadPic(mAvatarPath);
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (PersonalInfoView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }

    public void updateAccountNickname(String nickname) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            DataManager.getInstance().updateNickname(nickname);
            iView.showUpdateAccountSuccess(HandleEvent.UPDATE_NICKNAME_SUCCESS);
            return;
        }


        if (NetWorkUtil.isNetConnected()) {
            mNickname = nickname;
            iView.showLoading();
            NetworkOperation.getInstance().updateNickname(nickname);
        } else {
            iView.showNetworkUnavailable();
        }
    }

    public void updateAccountGender(String gender) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            DataManager.getInstance().updateGender(gender);
            iView.showUpdateAccountSuccess(HandleEvent.UPDATE_GENDER_SUCCESS);
            return;
        }


        if (NetWorkUtil.isNetConnected()) {
            mGender = gender;
            iView.showLoading();
            NetworkOperation.getInstance().updateGender(gender);
        } else {
            iView.showNetworkUnavailable();
        }
    }

    public void updateAccountHeight(double height) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            if (DataManager.getInstance().getUnitSystem() == Config.INCH) {
                height = Utils.changeFtToCm(height);
            }

            DataManager.getInstance().updateHeight((int) height);
            iView.showUpdateAccountSuccess(HandleEvent.UPDATE_HEIGHT_SUCCESS);
            return;
        }

        if (NetWorkUtil.isNetConnected()) {
            if (DataManager.getInstance().getUnitSystem() == Config.INCH) {
                mHeight = (int) Utils.changeFtToCm(height);
            } else {
                mHeight = (int) height;
            }
            iView.showLoading();
            NetworkOperation.getInstance().updateHeight(mHeight);
        } else {
            iView.showNetworkUnavailable();
        }
    }

    public void updateAccountWeight(double weight) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            if (DataManager.getInstance().getUnitSystem() == Config.INCH) {
                weight = Utils.changeLbToKg(weight);
            }
            DataManager.getInstance().updateWeight(weight);
            iView.showUpdateAccountSuccess(HandleEvent.UPDATE_WEIGHT_SUCCESS);
            return;
        }


        if (NetWorkUtil.isNetConnected()) {
            if (DataManager.getInstance().getUnitSystem() == Config.INCH) {
                mWeight = Utils.changeLbToKg(weight);
            } else {
                mWeight = weight;
            }
            iView.showLoading();
            NetworkOperation.getInstance().updateWeight(mWeight);
        } else {
            iView.showNetworkUnavailable();
        }
    }

    public void updateAccountBirthday(String birthday) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            DataManager.getInstance().updateBirthday(birthday);
            iView.showUpdateAccountSuccess(HandleEvent.UPDATE_BIRTHDAY_SUCCESS);
            return;
        }


        if (NetWorkUtil.isNetConnected()) {
            mBirthday = birthday;
            iView.showLoading();
            NetworkOperation.getInstance().updateBirthday(birthday);
        } else {
            iView.showNetworkUnavailable();
        }
    }

    private void updateAccountAvatar(String path) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            DataManager.getInstance().updateAvatar(path, null);
            iView.showUpdateAccountSuccess(HandleEvent.UPLOAD_AVATAR_SUCCESS);
            showLocalHeadPic();
            return;
        }

        if (NetWorkUtil.isNetConnected()) {
            iView.showLoading();
            NetworkOperation.getInstance().updateAvatar(path);
        } else {
            iView.showNetworkUnavailable();
        }

    }

    private void updateAccountBackground(String path) {
        if (DataManager.getInstance().getCurrentAccount().getToken() == null) {
            DataManager.getInstance().updateBackground(path, null);
            iView.showUpdateAccountSuccess(HandleEvent.UPLOAD_BACKGROUND_SUCCESS);
            return;
        }

        if (NetWorkUtil.isNetConnected()) {
            iView.showLoading();
            NetworkOperation.getInstance().updateBackground(path);
        } else {
            iView.showNetworkUnavailable();
        }

    }
}
