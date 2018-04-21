package com.coband.cocoband.mvp.presenter;

import com.coband.App;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.GuideHelloView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.bean.ConvertUnitBean;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.bean.LoginInfo;
import com.coband.cocoband.mvp.model.bean.UpdatedAtBean;
import com.coband.cocoband.mvp.model.bean.User;
import com.coband.cocoband.mvp.model.entity.Result;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-6-7.
 */

@Module
public class GuideHelloPresenter extends BasePresenter {
    GuideHelloView iView;
    private String userName;
    private String password;

    @Inject
    public GuideHelloPresenter() {
    }

    public void autoLogin() {
        String uid = DataManager.getInstance().getCurrentUid();
        if (uid != null) {
            iView.showLoginSuccess();
        } else {
            iView.showLoginFail();
        }
    }

    @Subscribe
    public void onEvent(HandleEvent event) {
        switch (event.getTag()) {
            case HandleEvent.LOGIN_SUCCESS:
                User user = (User) event.getObject();
                DBHelper.getInstance().insertUserSignIn(user);
                iView.showLoginSuccess();
                break;
            case HandleEvent.LOGIN_FAILED:
                // has no net, but make user enter home page and has a toast
                iView.showLoginSuccess();
                iView.showCustomToast(App.getContext().getString(R.string.network_error));
                break;
            case HandleEvent.GET_UPDATE_DATE_SUCCESS:
                Result<UpdatedAtBean> time = (Result<UpdatedAtBean>) event.getObject();
                long netUpdatedAt = DateUtils.dateStr2MillionSeconds(time.getResults()
                        .get(0).getUpdatedAt());
                long localUpdateAt = DateUtils.dateStr2MillionSeconds(DBHelper
                        .getInstance().getUser().getUpdatedAt());
                Logger.d(this, netUpdatedAt + "---" + localUpdateAt +
                        "---" + (netUpdatedAt - localUpdateAt) + "");
                if (netUpdatedAt <= localUpdateAt) {
                    // 本地数据为新  local data is new
                    Logger.d(this, "Update");
                    String objectId = DBHelper.getInstance().getUser().getObjectId();
                    String session = DBHelper.getInstance().getUser().getSessionToken();
                    NetworkOperation.getInstance().updateAllDB(setDBUser(), objectId, session);
                    iView.showLoginSuccess();
                } else {
                    Logger.d(this, "Login");
                    LoginInfo info = new LoginInfo();
                    info.setUsername(userName);
                    info.setPassword(password);
                }

                Logger.d("GuideHelloPresenter",
                        DBHelper.getInstance().getUser().getTotalWalkCount() + "");
                break;
            case HandleEvent.GET_UPDATE_DATE_FAIL:
                LoginInfo info = new LoginInfo();
                info.setUsername(userName);
                info.setPassword(password);
                break;
            default:
                break;
        }
    }

    private ConvertUnitBean setDBUser() {
        com.coband.watchassistant.User dbUser = DBHelper.getInstance().getUser();
        ConvertUnitBean user = new ConvertUnitBean();
        user.setWeightTarget(dbUser.getWeightTarget());
        user.setDistanceTarget(dbUser.getDistanceTarget());
        user.setTotalDistance(dbUser.getTotalDistance());
        user.setUnit(dbUser.getUnit());
        user.setHeight(dbUser.getHeight());
        user.setWeight(dbUser.getWeight());
        return user;
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (GuideHelloView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }
}
