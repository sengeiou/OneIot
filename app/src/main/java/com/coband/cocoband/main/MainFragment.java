package com.coband.cocoband.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.dashboard.DeviceFragment;
import com.coband.cocoband.me.MeFragment;
import com.coband.cocoband.widget.widget.NoSwipeViewPager;
import com.coband.watchassistant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by ivan on 17-5-15.
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.vp_main)
    NoSwipeViewPager mViewPager;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    @BindString(R.string.bottom_menu_main)
    String mBottomHome;

    @BindString(R.string.bottom_menu_message)
    String mBottomMessage;

    @BindString(R.string.bottom_menu_device)
    String mBottomDevice;

    @BindString(R.string.bottom_menu_me)
    String mBottomMe;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setupView() {
        initViewPager();
        initBottomNavigationView();
        Bundle arguments = getArguments();
        if (arguments.getBoolean("isFirstReg")) {
            mViewPager.setCurrentItem(2);
            mBottomNavigationBar.selectTab(2);
        } else {
            mViewPager.setCurrentItem(0);
        }
    }


    private void initViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(DeviceFragment.newInstance());
//        fragments.add(MessageFragment.newInstance());
        fragments.add(MeFragment.newInstance());
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // remove this to keep fragment alive.
//                super.destroyItem(container, position, object);
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewPager.setOffscreenPageLimit(3);
    }

    private void initBottomNavigationView() {
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_bottomtabbar_home, mBottomHome))
                .addItem(new BottomNavigationItem(R.drawable.ic_bottomtabbar_me, mBottomMe))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
}
