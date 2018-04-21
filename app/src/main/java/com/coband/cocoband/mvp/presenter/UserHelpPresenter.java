package com.coband.cocoband.mvp.presenter;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.UserHelpView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-6-6.
 */

@Module
public class UserHelpPresenter extends BasePresenter {
    private UserHelpView iView;

    @Inject
    public UserHelpPresenter() {
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (UserHelpView) view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageOnMainThread(MeItemClickEvent event) {
        int position = event.getPosition();
        iView.showHelp(position);
    }
}
