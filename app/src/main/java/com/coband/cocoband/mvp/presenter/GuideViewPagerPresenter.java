package com.coband.cocoband.mvp.presenter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.iview.GuideSignInAndSignUpView;
import com.coband.cocoband.widget.adapter.GuidePagerAdapter;
import com.coband.watchassistant.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by tgc on 17-4-11.
 */

@Module
public class GuideViewPagerPresenter extends BasePresenter {

    private GuideSignInAndSignUpView iView;

    @Inject
    public GuideViewPagerPresenter() {
    }

    public void initData(Context context, ViewPager viewPager, CirclePageIndicator indicator) {
        String title1 = context.getString(R.string.guide_vp_title_1);
        String title2 = context.getString(R.string.guide_vp_title_2);
        String title3 = context.getString(R.string.guide_vp_title_3);
        String title4 = context.getString(R.string.guide_vp_title_4);

        String text1 = context.getString(R.string.guide_vp_text_1);
        String text2 = context.getString(R.string.guide_vp_text_2);
        String text3 = context.getString(R.string.guide_vp_text_3);
        String text4 = context.getString(R.string.guide_vp_text_4);

        String[] titles = new String[]{title1, title2, title3, title4};
        String[] texts = new String[]{text1, text2, text3, text4};
        List<LinearLayout> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            LinearLayout linearPage = (LinearLayout) LayoutInflater.from(context).
                    inflate(R.layout.guide_vp_page, null);
            TextView title = (TextView) linearPage.findViewById(R.id.page_title);
            TextView text = (TextView) linearPage.findViewById(R.id.page_text);
            title.setText(titles[i]);
            text.setText(texts[i]);

            data.add(linearPage);
        }
        viewPager.setAdapter(new GuidePagerAdapter(data));
        indicator.setViewPager(viewPager);

    }

    public void playVideo() {
        iView.playVideo();
    }

    public void setViewPager() {
        iView.setViewPager();
    }

    @Override
    public void attachView(BaseView view) {
        this.iView = (GuideSignInAndSignUpView) view;
    }

    @Override
    public void detachView() {

    }
}
