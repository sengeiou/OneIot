package com.coband.cocoband.widget.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.coband.watchassistant.R;


/**
 * Created by mai on 17-3-1.
 */

public class NumPickerDialog {
    private NumberPicker mPicker;
    private MaterialDialog.Builder mBuilder;

    private OnNumSelectedListener mListener;

    public NumPickerDialog(@NonNull Context context, OnNumSelectedListener listener, String title, String[] numbers) {
        this.mListener = listener;

        mPicker = new NumberPicker(context);
        mPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mPicker.setMinValue(0);
        mPicker.setMaxValue(numbers.length - 1);
        mPicker.setDisplayedValues(numbers);
        mPicker.setWrapSelectorWheel(true);

        LinearLayout layout = new LinearLayout(context);
        layout.setGravity(Gravity.CENTER);
        layout.addView(mPicker, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        TextView textView = new TextView(context);
//        textView.setText("BPM");
//        layout.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mBuilder = new MaterialDialog.Builder(context);
        mBuilder.title(title)
                .customView(layout, true)
                .cancelable(true)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .negativeColor(context.getResources().getColor(R.color.color_1E78FF))
                .positiveColor(context.getResources().getColor(R.color.color_1E78FF))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mListener != null) {
                            mPicker.clearFocus();
                            mListener.onMinuteSelected(mPicker, mPicker.getValue());
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                    }
                });
    }

    public void show() {
        if (mBuilder != null) {
            mBuilder.show();
        }
    }

    public interface OnNumSelectedListener {
        void onMinuteSelected(NumberPicker view, int index);
    }

}
