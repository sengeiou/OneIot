package com.coband.cocoband.widget.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by tgc on 17-4-10.
 */

public class GuidePagerAdapter extends PagerAdapter {
    private List<LinearLayout> mData;

    public GuidePagerAdapter(List<LinearLayout> data){
        mData=data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View child=mData.get(position);
        container.addView(child);
        return child;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
