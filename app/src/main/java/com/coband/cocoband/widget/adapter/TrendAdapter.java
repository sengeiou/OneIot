package com.coband.cocoband.widget.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mqh on 9/24/16.
 */
public abstract class TrendAdapter extends PagerAdapter {

    private List<View> viewList;

    public TrendAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position % viewList.size()));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position % viewList.size());
        initView(view, position);
        container.addView(view);
        return view;
    }


    protected abstract void initView(View view, int position);


}
