package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coband.watchassistant.R;

/**
 * Created by imco on 5/11/16.
 */
public class BatteryView extends LinearLayout {

    private TextView mTextBattery;
    private ImageView mImageBattery;

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setOrientation(HORIZONTAL);
        mImageBattery = new ImageView(context);
        mTextBattery = new TextView(context);

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        iconParams.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        textParams.setMargins(20, 0, 0, 0);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        mImageBattery.setImageResource(R.drawable.ibd_power_60);
        mTextBattery.setText("100%");
        addView(mImageBattery, iconParams);
        addView(mTextBattery, textParams);
    }

    public void setBattery(int battery) {
        if (battery == 100) {
            mImageBattery.setImageResource(R.drawable.ibd_power_100);
        } else if (battery >= 80) {
            mImageBattery.setImageResource(R.drawable.ibd_power_80);
        } else if (battery >= 60) {
            mImageBattery.setImageResource(R.drawable.ibd_power_60);
        } else if (battery >= 40) {
            mImageBattery.setImageResource(R.drawable.ibd_power_40);
        } else if (battery >= 20) {
            mImageBattery.setImageResource(R.drawable.ibd_power_20);
        } else {
            mImageBattery.setImageResource(R.drawable.ibd_power_0);
        }
        mTextBattery.setText(battery + "%");
    }
}
