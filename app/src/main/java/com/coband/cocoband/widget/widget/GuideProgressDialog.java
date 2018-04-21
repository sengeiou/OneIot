package com.coband.cocoband.widget.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.coband.watchassistant.R;

/**
 * Created by tgc on 17-5-8.
 */

public class GuideProgressDialog extends Dialog {
    public GuideProgressDialog(Context context, String message) {
        super(context, R.style.ThemeOverlay_AppCompat_Dialog);
        setContentView(R.layout.progress_dialog);
//        getWindow().getAttributes().gravity= Gravity.CENTER;

//        Window window=this.getWindow();
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.gravity= Gravity.CENTER;
//        attributes.width= WindowManager.LayoutParams.WRAP_CONTENT;
//        attributes.height= WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(attributes);

        TextView dialogTip = (TextView) findViewById(R.id.bar_text);
        if (null!=message){
            dialogTip.setText(message);
        }
    }
}
