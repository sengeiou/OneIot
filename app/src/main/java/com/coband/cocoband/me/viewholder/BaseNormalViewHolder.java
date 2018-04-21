package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coband.App;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;
import com.like.LikeButton;

/**
 * Created by mai on 17-4-27.
 */

public abstract class BaseNormalViewHolder<T> extends BaseViewHolder<T> {
    protected TextView tvTitle;
    protected TextView tvTips;
    protected TextView tvStatus;
    protected ImageView ivArrowIcon;
    protected ImageView civAvatRight;
    protected ImageView civAvatLeft;
    protected ImageView civAvatLeftBig;
    protected ImageView ivLeft;
    protected TextView tvNumLeft;
    protected RelativeLayout rlRoot;
    protected RelativeLayout llLike;
    protected LikeButton lbLike;
    protected TextView tvLikes;
    protected View vDivider;

    public BaseNormalViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        tvTitle = (TextView) getView(R.id.tv_title);
        tvTips = (TextView) getView(R.id.text_tips);
        tvStatus = (TextView) getView(R.id.text_status);
        ivArrowIcon = (ImageView) getView(R.id.iv_arrow_icon);
        civAvatLeft = (ImageView) getView(R.id.civ_avat_left);
        civAvatRight = (ImageView) getView(R.id.civ_avat);
        tvNumLeft = (TextView) getView(R.id.tv_num_left);
        civAvatLeftBig = (ImageView) getView(R.id.civ_avat_left_big);
        lbLike = (LikeButton) getView(R.id.lb_like);
        ivLeft = (ImageView) getView(R.id.iv_left);
        rlRoot = (RelativeLayout) getView(R.id.rl_root);
        llLike = (RelativeLayout) getView(R.id.ll_like);
        tvLikes = (TextView) getView(R.id.tv_likes);
        vDivider = getView(R.id.view_divider);
    }

    protected String getString(@StringRes int sr) {
        return App.getContext().getString(sr);
    }
}
