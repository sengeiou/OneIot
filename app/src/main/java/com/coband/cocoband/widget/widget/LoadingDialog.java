package com.coband.cocoband.widget.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.coband.watchassistant.R;

/**
 * Created by ivan on 3/9/18.
 */

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.ThemeOverlay_AppCompat_Dialog);
        setContentView(R.layout.loading_dialog);
        setCancelable(false);
    }
}
