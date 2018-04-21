package com.coband.cocoband.mvp.model.remote.server;

import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;

import com.coband.cocoband.mvp.model.entity.request.DeviceMessage;
import com.coband.cocoband.mvp.model.entity.request.LogInBody;
import com.coband.cocoband.mvp.model.entity.request.ResetPwdBody;
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.coband.cocoband.mvp.model.entity.request.UpdateTargetRequest;
import com.coband.cocoband.mvp.model.entity.request.UploadDataInfo;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.entity.response.ResetPwdResponse;
import com.coband.cocoband.mvp.model.entity.response.UpdateAccountResponse;
import com.coband.cocoband.mvp.model.entity.response.UpdateTargetResponse;
import com.coband.cocoband.mvp.model.entity.response.UploadDataResponse;
import com.coband.cocoband.mvp.model.entity.response.UploadDeviceResponse;
import com.coband.common.utils.Utils;
import com.google.gson.Gson;
import com.coband.cocoband.event.EventBusEvent;
import com.coband.cocoband.event.NetWorkExceptionEvent;
import com.coband.cocoband.event.me.ActiveUserEvent;
import com.coband.cocoband.event.me.AddFriendEvent;
import com.coband.cocoband.event.me.AddFriendExceptionEvent;
import com.coband.cocoband.event.me.DeleteFriendsEvent;
import com.coband.cocoband.event.me.RankingEvent;
import com.coband.cocoband.event.me.UpdateInfoEvent;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.IMCOCallback;
import com.coband.cocoband.mvp.model.bean.AvatarBean;
import com.coband.cocoband.mvp.model.bean.AvatarFileBean;
import com.coband.cocoband.mvp.model.bean.ChatMessageBean;
import com.coband.cocoband.mvp.model.bean.ConvertUnitBean;
import com.coband.cocoband.mvp.model.bean.DynamicURLBean;
import com.coband.cocoband.mvp.model.bean.ForgotPasswordInfo;
import com.coband.cocoband.mvp.model.bean.GetMedalBean;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.bean.LoginInfo;
import com.coband.cocoband.mvp.model.bean.ResultsList;
import com.coband.cocoband.mvp.model.bean.SignUpInfo;
import com.coband.cocoband.mvp.model.bean.SignUpUserInfo;
import com.coband.cocoband.mvp.model.bean.SingleWeightBean;
import com.coband.cocoband.mvp.model.bean.SurfaceImgBean;
import com.coband.cocoband.mvp.model.bean.SurfaceImgFileBean;
import com.coband.cocoband.mvp.model.bean.TotalWalkCountBean;
import com.coband.cocoband.mvp.model.bean.UnReadBean;
import com.coband.cocoband.mvp.model.bean.UpdatedAtBean;
import com.coband.cocoband.mvp.model.bean.User;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.FollowersAndFolloweesBean;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.MasterUser;
import com.coband.cocoband.mvp.model.bean.message.ConversationBean;
import com.coband.cocoband.mvp.model.bean.message.ConversationCreateResultBean;
import com.coband.cocoband.mvp.model.bean.message.FileBean;
import com.coband.cocoband.mvp.model.bean.message.SendMessageBean;
import com.coband.cocoband.mvp.model.bean.todayrank.CreatedResult;
import com.coband.cocoband.mvp.model.bean.todayrank.RankLikesBean;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankBean;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankStep;
import com.coband.cocoband.mvp.model.bean.todayrank.UpdateResult;
import com.coband.cocoband.mvp.model.bean.todayrank.UserTodayRankBean;
import com.coband.cocoband.mvp.model.entity.BatchUploadData;
import com.coband.cocoband.mvp.model.entity.BatchUploadResult;
import com.coband.cocoband.mvp.model.entity.BloodPressureInfo;
import com.coband.cocoband.mvp.model.entity.EmptyResult;
import com.coband.cocoband.mvp.model.entity.FOTAResult;
import com.coband.cocoband.mvp.model.entity.HeartRateInfo;
import com.coband.cocoband.mvp.model.entity.RandomString;
import com.coband.cocoband.mvp.model.entity.Result;
import com.coband.cocoband.mvp.model.entity.SleepInfo;
import com.coband.cocoband.mvp.model.entity.SportInfo;
import com.coband.cocoband.mvp.model.entity.UploadResult;
import com.coband.cocoband.mvp.model.entity.Weight;
import com.coband.cocoband.mvp.model.entity.filter.PressureUserFilter;
import com.coband.cocoband.mvp.model.entity.filter.SleepUserFilter;
import com.coband.cocoband.mvp.model.entity.filter.SportDateFilter;
import com.coband.cocoband.mvp.model.entity.filter.SportDateLimitFilter;
import com.coband.cocoband.mvp.model.entity.filter.SportUserFilter;
import com.coband.cocoband.mvp.model.entity.request.EmailRegisterContent;
import com.coband.cocoband.mvp.model.entity.request.PhoneRegisterContent;
import com.coband.cocoband.mvp.model.entity.response.RegisterResult;
import com.coband.cocoband.mvp.model.entity.response.VerifyCodeResult;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.common.network.Api;
import com.coband.common.network.FOTAApi;
import com.coband.common.network.NetworkConfig;
import com.coband.common.tools.RxBus;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.FOTAConfig;
import com.coband.common.utils.Logger;
import com.coband.common.utils.NetWorkUtil;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.Conversation;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.HeartRate;
import com.coband.watchassistant.HistoryData;
import com.coband.watchassistant.Sleep;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.coband.common.utils.LeanCloudConfig.APP_ID;

/**
 * Created by ivan on 17-4-11.
 * all network operation must be access by this class.
 */

public class NetworkOperation {
    private static final String TAG = "NetworkOperation";

    private static String mDynamicURL = "https://lk71houg.api.lncld.net/1.1/";

    private static NetworkOperation instance;

    private boolean isFirstCommand = true;

    private NetworkOperation() {

    }

    public static NetworkOperation getInstance() {
        if (instance == null) {
            instance = new NetworkOperation();
        }
        return instance;
    }

    /**
     * 因为　DNS 污染，动态获取URL
     *
     * @param observableSource
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDynamicURL(final Observable<T> observableSource) {
        if (isFirstCommand) {
            return Api.getInstance().service.getDynamicURL("https://app-router.leancloud.cn/2/route", APP_ID)
                    .flatMap(new Function<DynamicURLBean, ObservableSource<T>>() {
                        @Override
                        public ObservableSource<T> apply(DynamicURLBean dynamicURLBean) throws Exception {
                            mDynamicURL = "https://" + dynamicURLBean.api_server + "/1.1/";
                            isFirstCommand = false;
                            return observableSource;
                        }
                    });
        } else {
            return observableSource;
        }
    }

    /**
     * get Last dynamic url when leancloud return 404
     *
     * @param throwable
     */
    public void getLastDynamicURL(Throwable throwable) {
        String localizedMessage = throwable.getLocalizedMessage();
        if (localizedMessage == null || localizedMessage.contains("HTTP 404")) {
            Api.getInstance().service.getDynamicURL("https://app-router.leancloud.cn/2/route", APP_ID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<DynamicURLBean>() {
                        @Override
                        public void accept(DynamicURLBean dynamicURLBean) throws Exception {
                            mDynamicURL = "https://" + dynamicURLBean.api_server + "/1.1/";
                        }
                    });
        }
    }

    /***
     *
     * @param objectID
     */
    public void getFollowersAndFollowees(String objectID) {
        if (!CocoUtils.isNetworkAvailable()) return;
        getDynamicURL(Api.getInstance().service.getFollowersAndFollowees(mDynamicURL + ("users/" + objectID + "/followersAndFollowees")))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<FollowersAndFolloweesBean>() {
                    @Override
                    public void accept(FollowersAndFolloweesBean followers) throws Exception {
                        DBHelper.getInstance().insertFollowers(followers.followers);
                        DBHelper.getInstance().insertFollowees(followers.followees);

                        EventBus.getDefault().post(followers);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(TAG, "throwable >>>> " + throwable.getMessage());

                        EventBus.getDefault().post(new HandleEvent(HandleEvent.CLOSE_ALL_DIALOG));
                        getLastDynamicURL(throwable);

                    }
                });
    }

    public void updateAllDB(ConvertUnitBean user, String objID, String session) {
        getDynamicURL(Api.getInstance().service.updateAllDB(mDynamicURL + ("users/" + objID), session, user))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.UPDATE_DB_SUCCESS));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.UPDATE_DB_FAIL));
                        getLastDynamicURL(throwable);

                    }
                });
    }

    /*************************** CoBand ******************************/

    public void phoneRegister(String phone, String verificationCode, String password) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        PhoneRegisterContent content = new PhoneRegisterContent();
        content.setType("phone");
        content.setPassword(password);
        content.setPhone(phone);
        content.setVerifyCode(verificationCode);
        Api.getInstance().service.phoneRegister(String.valueOf(timestamp), sign, nonce, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResult>() {
                    @Override
                    public void accept(RegisterResult registerResult) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.REGISTER_WITH_PHONE_SUCCESS,
                                registerResult));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.REGISTER_WITH_PHONE_FAILED,
                                throwable.getLocalizedMessage()));
                    }
                });
    }

    public void emailRegister(String email, String password) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        EmailRegisterContent content = new EmailRegisterContent();
        content.setType("email");
        content.setPassword(password);
        content.setEmail(email);

        Api.getInstance().service.emailRegister(String.valueOf(timestamp), sign, nonce, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResult>() {
                    @Override
                    public void accept(RegisterResult registerResult) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.REGISTER_WITH_EMAIL_SUCCESS,
                                registerResult));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.REGISTER_WITH_EMAIL_FAILED,
                                throwable.getLocalizedMessage()));
                    }
                });
    }

    public void requestVerifyCode(Map<String, String> map, final int successEvent, final int failedEvent) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        Api.getInstance().service.requestVerifyCode(String.valueOf(timestamp), sign, nonce, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VerifyCodeResult>() {
                    @Override
                    public void accept(VerifyCodeResult verifyCodeResult) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(successEvent, verifyCodeResult));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(failedEvent,
                                throwable.getLocalizedMessage()));
                    }
                });
    }


    public void updateAccountInfo(String token, UpdateAccountInfo info, final int successCode,
                                  final int failCode) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        String authorization = "Bearer " + token;
        Api.getInstance().service.updateAccountInfo(authorization, String.valueOf(timestamp),
                sign, nonce, info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateAccountResponse>() {
                    @Override
                    public void accept(UpdateAccountResponse response) throws Exception {
                        Logger.d(TAG, "code >>>>>>>>> " + response.getCode());
                        EventBus.getDefault().post(new HandleEvent(
                                successCode, response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(TAG, "error msg >>>>>>>>> " + throwable.getLocalizedMessage());
                        EventBus.getDefault().post(new HandleEvent(failCode,
                                throwable.getLocalizedMessage()));
                    }
                });
    }

    public void logIn(LogInBody body) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        Api.getInstance().service.logIn(String.valueOf(timestamp), sign, nonce, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LogInResponse>() {
                    @Override
                    public void accept(LogInResponse response) throws Exception {
                        Logger.d(TAG, "code >>>>>>>>> " + response.getCode());
                        EventBus.getDefault().post(new HandleEvent(
                                HandleEvent.LOGIN_SUCCESS, response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(TAG, "error msg >>>>>>>>> " + throwable.getLocalizedMessage());
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.LOGIN_FAILED,
                                throwable.getLocalizedMessage()));
                    }
                });
    }

    public void updateTarget(String token, final UpdateTargetRequest request) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        String authorization = "Bearer " + token;
        Api.getInstance().service.updateTarget(authorization, String.valueOf(timestamp),
                sign, nonce, request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<UpdateTargetResponse>() {
                    @Override
                    public void accept(UpdateTargetResponse response) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.UPDATE_TARGET_SUCCESS,
                                response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.UPDATE_TARGET_FAILED,
                                throwable.getLocalizedMessage()));

                    }
                });
    }

    public void updateNickname(String nickname) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        UpdateAccountInfo info = new UpdateAccountInfo();
        info.setNickName(nickname);
        updateAccountInfo(token, info, HandleEvent.UPDATE_NICKNAME_SUCCESS,
                HandleEvent.UPDATE_NICKNAME_FAILED);
    }

    public void updateGender(String gender) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        UpdateAccountInfo info = new UpdateAccountInfo();
        info.setGender(gender);
        updateAccountInfo(token, info, HandleEvent.UPDATE_GENDER_SUCCESS,
                HandleEvent.UPDATE_GENDER_FAILED);
    }

    public void updateHeight(int height) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        UpdateAccountInfo info = new UpdateAccountInfo();
        info.setHeight(height);
        updateAccountInfo(token, info, HandleEvent.UPDATE_HEIGHT_SUCCESS,
                HandleEvent.UPDATE_HEIGHT_FAILED);
    }

    public void updateWeight(double weight) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        UpdateAccountInfo info = new UpdateAccountInfo();
        info.setWeight(weight);
        updateAccountInfo(token, info, HandleEvent.UPDATE_WEIGHT_SUCCESS,
                HandleEvent.UPDATE_WEIGHT_FAILED);
    }

    public void updateBirthday(String birthday) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        UpdateAccountInfo info = new UpdateAccountInfo();
        info.setBirthday(birthday);
        updateAccountInfo(token, info, HandleEvent.UPDATE_BIRTHDAY_SUCCESS,
                HandleEvent.UPDATE_BIRTHDAY_FAILED);
    }

    public void updateUnitSystem(String unitSystem) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        UpdateAccountInfo info = new UpdateAccountInfo();
        info.setUnitSystem(unitSystem);
        updateAccountInfo(token, info, HandleEvent.UPDATE_UNIT_SYSTEM_SUCCESS,
                HandleEvent.UPDATE_UNIT_SYSTEM_FAILED);
    }

    // parameter "type",  value in : 0(upload avatar),1(upload background)
    private void updateImage(int type, String path, final int successEvent, final int failedEvent) {
        String uploadType = null;
        Observable<UpdateAccountResponse> observable;


        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        String authorization = "Bearer " + token;
        RequestBody body =
                RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));
//        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", path, requestFile);

        File file = new File(path);
        String md5 = null;
        try {
            md5 = Utils.getMd5ByFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.d(TAG, "file not found ------------------>");
        }

        if (type == 0) {
            uploadType = "avatar";
        } else {
            uploadType = "background";
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("md5", md5)
                .addFormDataPart("ext", "png")
                .addFormDataPart(uploadType, file.getName(), body);
        List<MultipartBody.Part> parts = builder.build().parts();

        if (type == 0) {
            observable = Api.getInstance().service.uploadAvatar(authorization, String.valueOf(timestamp),
                    sign, nonce, parts);
        } else {
            observable = Api.getInstance().service.uploadBackground(authorization, String.valueOf(timestamp),
                    sign, nonce, parts);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateAccountResponse>() {
                    @Override
                    public void accept(UpdateAccountResponse uploadAvatarResponse) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(successEvent,
                                uploadAvatarResponse));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(failedEvent,
                                throwable.getLocalizedMessage()));

                    }
                });
    }

    public void updateAvatar(String path) {
        updateImage(0, path, HandleEvent.UPLOAD_AVATAR_SUCCESS, HandleEvent.UPLOAD_AVATAR_FAILED);
    }

    public void updateBackground(String path) {
        updateImage(1, path, HandleEvent.UPLOAD_BACKGROUND_SUCCESS, HandleEvent.UPLOAD_BACKGROUND_FAILED);
    }

    public void uploadDeviceMessage(String token, String model, String macAddress) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        String authorization = "Bearer " + token;
        DeviceMessage body = new DeviceMessage();
        body.setMac(macAddress);
        body.setName("CoBand");
        body.setModel(model);
        Api.getInstance().service.uploadDeviceMessage(authorization, String.valueOf(timestamp),
                sign, nonce, body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<UploadDeviceResponse>() {
                    @Override
                    public void accept(UploadDeviceResponse response) throws Exception {
                        Logger.d(TAG, "code >>>>> " + response.getCode());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(TAG, "error msg >>>>> " + throwable.getLocalizedMessage());
                    }
                });

    }

    public void requestResetPwdByPhone(ResetPwdBody body) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        Api.getInstance().service.requestResetPwdByPhone(String.valueOf(timestamp), sign, nonce, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResetPwdResponse>() {
                    @Override
                    public void accept(ResetPwdResponse response) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.RESET_PWD_SUCCESS, response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.RESET_PWD_FAILED,
                                throwable.getLocalizedMessage()));
                    }
                });
    }

    public void requestResetPwdByEmail(String emailAddress) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = new RandomString(12).nextString();
        String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                NetworkConfig.HMAC_AppSecret);
        Api.getInstance().service.requestResetPwdByEmail(String.valueOf(timestamp), sign, nonce, emailAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResetPwdResponse>() {
                    @Override
                    public void accept(ResetPwdResponse response) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.RESET_PWD_SUCCESS_BY_EMAIL, response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new HandleEvent(HandleEvent.RESET_PWD_FAILED_BY_EMAIL,
                                throwable.getLocalizedMessage()));
                    }
                });
    }

    public void uploadDayData(final UploadDataInfo info) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        if (token != null) {
            long timestamp = System.currentTimeMillis() / 1000;
            String nonce = new RandomString(12).nextString();
            String sign = Utils.hmacSHA1Encrypt(NetworkConfig.HMAC_AppKEY + timestamp + nonce,
                    NetworkConfig.HMAC_AppSecret);
            String authorization = "Bearer " + token;
            Api.getInstance().service.uploadDayData(authorization, String.valueOf(timestamp), sign, nonce, info)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UploadDataResponse>() {
                        @Override
                        public void accept(UploadDataResponse response) throws Exception {
                            if (response.getCode() == 0) {
                                EventBus.getDefault().post(new HandleEvent(HandleEvent.UPLOAD_DAY_DATA_SUCCESS, info.getSport().getDate()));
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Logger.d(TAG, "upload day data failed >>>>>>> " + info.getSport().getDate());
                        }
                    });

        }
    }
}

