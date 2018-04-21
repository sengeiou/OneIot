package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ivan on 17-6-13.
 */

public class ObtainChildViewPager extends ViewPager {

    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<>();

    public ObtainChildViewPager(Context context) {
        this(context, null);
    }

    public ObtainChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setObjectForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }

    public View findViewFromObject(int position) {
        return mChildrenViews.get(position);
    }
}
