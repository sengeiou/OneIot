package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by imco on 5/11/16.
 */
public class EnabledRelativeLayout extends RelativeLayout {
    public EnabledRelativeLayout(Context context) {
        super(context);
    }

    public EnabledRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnabledRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof ImageView) {
                child.setAlpha(enabled ? 1.0f : 0.4f);
            } else {
                child.setEnabled(enabled);
            }
        }

        super.setEnabled(enabled);
    }
}
