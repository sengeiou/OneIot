package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

/**
 * Created by tgc on 17-5-10.
 */

public abstract class BaseMedalViewHolder<T> extends BaseViewHolder<T> {

    protected ImageView medalIcon;
    protected TextView medalTitle;
    protected TextView medalTip;

    public BaseMedalViewHolder(Context context, ViewGroup root, int layoutRes){
        super(context, root, layoutRes);
        medalIcon= (ImageView) getView(R.id.medal_icon);
        medalTip= (TextView) getView(R.id.medal_tip);
        medalTitle= (TextView) getView(R.id.medal_title);
    }
}
