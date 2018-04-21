package com.coband.cocoband.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mai on 17-3-2.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    private Unbinder mUnbinder;

    public BaseHolder(View itemView) {
        super(itemView);
        if (((ViewGroup) itemView).getChildCount() > 0)
            mUnbinder=ButterKnife.bind(this, itemView);
    }

    public abstract int getType();

    public abstract void onBindViewHolder(View view, T obj, int position);
}
