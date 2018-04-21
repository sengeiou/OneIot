package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coband.App;
import com.coband.cocoband.event.me.FriendsRequestItemClickEvent;
import com.coband.cocoband.mvp.model.bean.FollowerBean;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mai on 17-5-2.
 */

public class FriendsRequestViewHolder extends BaseNormalViewHolder<FollowerBean> implements View.OnClickListener {

    public static final String TAG = "FriendsRequestViewHolder";
    public FriendsRequestViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }
    private FollowerBean mFollowerBean;

    @Override
    public void bindData(FollowerBean o, int position, boolean isSelected) {
        mFollowerBean = o;
        itemView.setOnClickListener(this);
        ImageView civAvatRight2 = (ImageView) itemView.findViewById(R.id.iv_avat_right2);
        civAvatLeft.setVisibility(View.VISIBLE);
        ivArrowIcon.setVisibility(View.GONE);
        civAvatRight2.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.GONE);
        if (o.isFriend) {
            civAvatRight2.setPadding(CocoUtils.dp2px(10), CocoUtils.dp2px(10), CocoUtils.dp2px(10),
                    CocoUtils.dp2px(10));
            civAvatRight2.setBackgroundResource(R.drawable.btn_edit_gray_bkg);
            civAvatRight2.setImageResource(R.drawable.ic_profile_following_gray);
            civAvatRight2.setClickable(false);
        } else {
            civAvatRight2.setPadding(CocoUtils.dp2px(2), CocoUtils.dp2px(2), CocoUtils.dp2px(2),
                    CocoUtils.dp2px(2));
            civAvatRight2.setBackgroundResource(R.drawable.btn_edit_bkg);
            civAvatRight2.setImageResource(R.drawable.ic_add_black_24dp);
            civAvatRight2.setOnClickListener(this);
        }

        Glide.with(App.getContext()).load(o.avatar).error(R.drawable.profile_men).centerCrop()
                .fitCenter().into(civAvatLeft);
        tvTitle.setMaxEms(11);
        tvTitle.setText(o.userName);
        tvTips.setText(o.isFriend?R.string.message_when_agree_request:R.string.request_friend_tips);
    }


    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<FriendsRequestViewHolder>() {
        @Override
        public FriendsRequestViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new FriendsRequestViewHolder(parent.getContext(), parent, R.layout.settings_item);
        }
    };

    @Override
    public void onClick(View v) {
        Logger.d(TAG, "Onclick : " + v.getId() );
        if (v.getId() == R.id.rl_root) {
            Logger.d(TAG, "Onclick 0: " + v.getId());
            EventBus.getDefault().post(new FriendsRequestItemClickEvent(itemView, getAdapterPosition(),
                    mFollowerBean.objId));
        } else if (v.getId() == R.id.iv_avat_right2) {

        }
    }
}
