package com.coband.cocoband.mvp.presenter;

import com.coband.App;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.SetUserInfoView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.bean.LoginInfo;
import com.coband.cocoband.mvp.model.bean.SignUpInfo;
import com.coband.cocoband.mvp.model.bean.UpdateInfoBean;
import com.coband.cocoband.mvp.model.bean.User;
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.entity.response.UpdateAccountResponse;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.GetAppVersionUtil;
import com.coband.common.utils.NetWorkUtil;
import com.coband.common.utils.SaveLoginInfoUtils;
import com.coband.common.utils.UnitUtils;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-4-27.
 */

@Module
public class SetUserInfoPresenter extends BasePresenter {
    public final static String TAG = "SetUserInfoPresenter";
    //作为fragment中单位的临时变量
    public static int unitTemp = 0;
    SetUserInfoView iView;


    @Inject
    public SetUserInfoPresenter() {
    }

    public void updateInfo(UpdateAccountInfo info) {
        String token = DataManager.getInstance().getCurrentAccount().getToken();
        if (token != null) {
            if (NetWorkUtil.isNetConnected()) {
                NetworkOperation.getInstance().updateAccountInfo(token, info,
                        HandleEvent.UPDATE_ACCOUNT_INFO_SUCCESS,
                        HandleEvent.UPDATE_ACCOUNT_INFO_FAILED);
            } else {
                iView.showNetworkUnavailable();
            }
        } else {
            DataManager.getInstance().updateAccountInfo(info);
            iView.showPostAccountInfoSuccess();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.UPDATE_ACCOUNT_INFO_SUCCESS:
                UpdateAccountResponse response = (UpdateAccountResponse) event.getObject();
                if (response.getCode() == 0) {
                    Account account = handleUpdateAccountResponse(response);
                    DataManager.getInstance().updateOrInsertAccount(account);
                    iView.showPostAccountInfoSuccess();
                } else {
                    iView.showPostAccountInfoFailed(response.getCode());
                }
                break;
            case HandleEvent.UPDATE_ACCOUNT_INFO_FAILED:
                String errorMsg = (String) event.getObject();
                iView.showPostAccountInfoFailed(errorMsg);
                break;
            default:
                break;
        }
    }


    private Account handleUpdateAccountResponse(UpdateAccountResponse response) {
        UpdateAccountResponse.PayloadBean bean = response.getPayload();

        Account a = new Account();

        String uid = bean.getUser().getUid();

        String birthday = bean.getUser().getPersonalInfo().getBirthday();
        String bloodType = bean.getUser().getPersonalInfo().getBloodType();
        String city = bean.getUser().getPersonalInfo().getCity();
        String country = bean.getUser().getPersonalInfo().getCountry();
        String gender = bean.getUser().getPersonalInfo().getGender();
        String nickName = bean.getUser().getPersonalInfo().getNickname();
        String province = bean.getUser().getPersonalInfo().getProvince();
        int height = bean.getUser().getPersonalInfo().getHeight();
        double weight = bean.getUser().getPersonalInfo().getWeight();
        String unitSystem = bean.getUser().getPersonalInfo().getUnitSystem();

        double latitude = 0, longitude = 0;
        if (bean.getUser().getPersonalInfo().getLocation() != null) {
            latitude = bean.getUser().getPersonalInfo().getLocation().getLatitude();
            longitude = bean.getUser().getPersonalInfo().getLocation().getLongitude();
        }

        String avatarPath = null, avatarMD5 = null;
        if (bean.getUser().getPersonalInfo().getAvatar() != null) {
            avatarPath = bean.getUser().getPersonalInfo().getAvatar().getPath();
            avatarMD5 = bean.getUser().getPersonalInfo().getAvatar().getMd5();
        }

        String bgPath = null, bgMD5 = null;
        if (bean.getUser().getPersonalInfo().getBackground() != null) {
            bgPath = bean.getUser().getPersonalInfo().getBackground().getPath();
            bgMD5 = bean.getUser().getPersonalInfo().getBackground().getMd5();
        }


        int timezone = -1;
        String language = null, osType = null, osVersion = null, phoneModel = null;
        if (bean.getUser().getPhoneInfo() != null) {
            timezone = bean.getUser().getPhoneInfo().getTimezone();
            language = bean.getUser().getPhoneInfo().getLanguage();
            osType = bean.getUser().getPhoneInfo().getOsType();
            osVersion = bean.getUser().getPhoneInfo().getOsVersion();
            phoneModel = bean.getUser().getPhoneInfo().getPhoneModel();
        }

        int totalExerciseDays = 0;
        long maxDaySteps = 0, totalSteps = 0, startExerciseTime = 0;
        double totalDistance = 0, totalCalories = 0;
        List<String> achievements = null;
        if (bean.getUser().getSportSummary() != null) {
            achievements = bean.getUser().getSportSummary().getAchievements();
            maxDaySteps = bean.getUser().getSportSummary().getMaxDaySteps();
            totalSteps = bean.getUser().getSportSummary().getTotalSteps();
            startExerciseTime = bean.getUser().getSportSummary().getStartExerciseTime();
            totalCalories = bean.getUser().getSportSummary().getTotalCalories();
            totalDistance = bean.getUser().getSportSummary().getTotalDistance();
            totalExerciseDays = bean.getUser().getSportSummary().getTotalExerciseDays();
        }
        int sleepTarget = 0, stepTarget = 0;
        double weightTarget = 0;
        if (bean.getUser().getSportTarget() != null) {
            sleepTarget = bean.getUser().getSportTarget().getSleepTarget();
            stepTarget = bean.getUser().getSportTarget().getStepTarget();
            weightTarget = bean.getUser().getSportTarget().getWeightTarget();
        }

        a.setUid(uid);

        if (avatarPath != null) {
            a.setAvatar(avatarPath);
        }

        if (avatarMD5 != null) {
            a.setAvatarMD5(avatarMD5);
        }

        if (bgPath != null) {
            a.setBackground(bgPath);
        }

        if (bgMD5 != null) {
            a.setBackgroundMD5(bgMD5);
        }

        if (birthday != null) {
            a.setBirthday(birthday);
        }

        if (bloodType != null) {
            a.setBloodType(bloodType);
        }

        if (city != null) {
            a.setCity(city);
        }

        if (country != null) {
            a.setCountry(country);
        }

        if (gender != null) {
            a.setGender(gender);
        }

        if (unitSystem != null) {
            a.setUnitSystem(unitSystem);
        }

        if (nickName != null) {
            a.setNickname(nickName);
        }

        if (province != null) {
            a.setProvince(province);
        }

        if (height != 0) {
            a.setHeight(height);
        }

        if (latitude != 0) {
            a.setLatitude(latitude);
        }

        if (longitude != 0) {
            a.setLongitude(longitude);
        }

        if (weight != 0) {
            a.setWeight(weight);
        }

        if (timezone != -1) {
            a.setTimezone(timezone);
        }

        if (language != null) {
            a.setLanguage(language);
        }

        if (osType != null) {
            a.setOsType(osType);
        }

        if (osVersion != null) {
            a.setOsVersion(osVersion);
        }

        if (phoneModel != null) {
            a.setPhoneModel(phoneModel);
        }

        if (achievements != null && !achievements.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String achievement : achievements) {
                builder.append(achievement + ",");
            }
            a.setAchievements(builder.toString());
        }

        if (maxDaySteps != 0) {
            a.setMaxDaySteps(maxDaySteps);
        }

        if (totalSteps != 0) {
            a.setTotalSteps(totalSteps);
        }

        if (totalCalories != 0) {
            a.setTotalCalories(totalCalories);
        }

        if (totalDistance != 0) {
            a.setTotalDistance(totalDistance);
        }

        if (totalExerciseDays != 0) {
            a.setTotalExerciseDays(totalExerciseDays);
        }

        if (startExerciseTime != 0) {
            a.setStartExerciseTime(startExerciseTime);
        }

        if (sleepTarget != 0) {
            a.setSleepTarget(sleepTarget);
        }

        if (stepTarget != 0) {
            a.setStepTarget(stepTarget);
        }

        if (weightTarget != 0) {
            a.setWeightTarget(weightTarget);
        }

        return a;
    }


    public void updateUnit(int unit) {
        unitTemp = unit;
    }

    public void showWeight(String weight) {
        if (SetUserInfoPresenter.unitTemp == 1) {
            String weightStr = weight.substring(0,
                    weight.lastIndexOf(App.getContext().getString(R.string.kg)));

            float weightFloat = Float.parseFloat(weightStr);
            float inch = weightFloat * 2.2046226f;
            float inchFinal = (Math.round(inch * 10) / 10f);
            iView.showWeightToEditText(inchFinal);
        } else {
            String weightStr = weight.substring(0,
                    weight.lastIndexOf(App.getContext().getString(R.string.lb)));

            float weightFloat = Float.parseFloat(weightStr);
            float metric = weightFloat * 0.4535924f;
            float metricFinal = (Math.round(metric * 10) / 10f);
            iView.showWeightToEditText(metricFinal);
        }
    }

    public void showHeight(String height) {
        if (SetUserInfoPresenter.unitTemp == 1) {
            String heightStr = height.substring(0,
                    height.lastIndexOf(App.getContext().getString(R.string.cm)));

            float heightFloat = Float.parseFloat(heightStr);
            //to inch
            //将厘米转换成英寸，然后转换成xx英尺xx英寸
            List<Integer> list = UnitUtils.cmToInch(heightFloat);
            iView.showHeightInchToEditText(list);
        } else {
            String ftStr = height.substring(0,
                    height.lastIndexOf(App.getContext().getString(R.string.foot)));
            // 英尺
            int ftInteger = Integer.parseInt(ftStr);
            String unitStr = App.getContext().getString(R.string.foot);
            String inchStr = height.substring(height.lastIndexOf(unitStr)
                    + unitStr.length(), height.lastIndexOf(App.getContext()
                    .getString(R.string.inch_unit)));
            //英寸
            int inchInteger = Integer.parseInt(inchStr);

            float heightFloat = (float) (ftInteger * 30.48 + inchInteger * 2.54);
            float heightFinal = (Math.round(heightFloat * 10) / 10f);
            iView.showHeightMetricToEditText(heightFinal);
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (SetUserInfoView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        unitTemp = 0;
        EventBus.getDefault().unregister(this);
    }

    public static double getHeightInGuide(String str) {
        //小数点的索引
        if (SetUserInfoPresenter.unitTemp == 0) {
            int index = str.indexOf(".");
            String height = str.substring(0, index + 2);
            return Double.parseDouble(height);
        } else {
            String ftStr = str.substring(0, str.lastIndexOf(App.getContext().getString(R.string.foot)));
            String temp = App.getContext().getString(R.string.foot);
            String inchStr = str.substring(str.lastIndexOf(temp) + temp.length(), str.lastIndexOf(App.getContext().getString(R.string.inch_unit)));

            double inchDouble = Double.parseDouble(inchStr);
            double ftDouble = Double.parseDouble(ftStr);

            return ftDouble + inchDouble / 12;

        }

    }
}
