package com.coband.common.utils.chat;

import android.content.Context;

import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

/**
 * Created by mqh on 11/22/16.
 */

public abstract class SimpleNetTask extends NetAsyncTask {
    protected SimpleNetTask(Context cxt) {
        super(cxt);
    }

    protected SimpleNetTask(Context cxt, boolean openDialog) {
        super(cxt, openDialog);
    }


    @Override
    protected void onPost(Exception e) {
        if (e != null) {
            e.printStackTrace();
            Utils.toast(R.string.network_error);
            //Utils.toast(ctx, R.string.pleaseCheckNetwork);
        } else {
            onSucceed();
        }
    }

    protected abstract void doInBack() throws Exception;

    protected abstract void onSucceed();
}
