package com.coband.cocoband.tools;

import com.coband.cocoband.guide.fragment.FindPwdByEmailFragment;
import com.coband.cocoband.guide.fragment.FindPwdByPhoneFragment;
import com.coband.cocoband.guide.fragment.HelloFragment;
import com.coband.cocoband.guide.fragment.SetUserInfoFragment;
import com.coband.cocoband.guide.fragment.GuideFragment;
import com.coband.cocoband.guide.fragment.SignInFragment;
import com.coband.cocoband.guide.fragment.SignUpFragment;
import com.coband.cocoband.main.BandActivity;
import com.coband.cocoband.me.PersonalInfoFragment;
import com.coband.cocoband.me.SettingsFragment;
import com.coband.cocoband.me.UseHelpFragment;
import com.coband.cocoband.me.UserInfoFragment;
import com.coband.cocoband.me.WechatFragment;
import com.coband.cocoband.mvp.presenter.MainPresenter;

import dagger.Component;

/**
 * Created by ivan on 17-4-11.
 */

@Component
public abstract class AppComponent {
    private static AppComponent mComponent;

    public static AppComponent getInstance() {
        if (mComponent == null) {
            mComponent = DaggerAppComponent.builder().build();
        }

        return mComponent;
    }

    public abstract void inject(BandActivity activity);

    public abstract void inject(MainPresenter presenter);

    public abstract void inject(UserInfoFragment fragment);

    public abstract void inject(SettingsFragment fragment);

    public abstract void inject(UseHelpFragment fragment);

    public abstract void inject(GuideFragment fragment);

    public abstract void inject(SignUpFragment fragment);

    public abstract void inject(SetUserInfoFragment fragment);

    public abstract void inject(FindPwdByEmailFragment fragment);

    public abstract void inject(SignInFragment fragment);

    public abstract void inject(HelloFragment fragment);

    public abstract void inject(PersonalInfoFragment fragment);

    public abstract void inject(WechatFragment fragment);

    public abstract void inject(FindPwdByPhoneFragment fragment);
}
