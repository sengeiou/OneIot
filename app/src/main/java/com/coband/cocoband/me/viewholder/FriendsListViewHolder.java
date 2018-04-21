package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.coband.App;
import com.coband.cocoband.event.me.FriendsListItemClickEvent;
import com.coband.cocoband.mvp.model.bean.FollowerBean;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.common.utils.CocoUtils;
import com.coband.watchassistant.R;
import com.coband.watchassistant.User;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mai on 17-4-25.
 */

public class FriendsListViewHolder extends BaseViewHolder<FollowerBean> implements View.OnClickListener {
    public FriendsListViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    private FollowerBean mFollower;

    @Override
    public void bindData(FollowerBean s, int position, boolean isSelected) {
        this.mFollower = s;
        User user = DBHelper.getInstance().getUser();
        itemView.setOnClickListener(this);
        if (s.title != null && s.title.equals("head")) {
            TextView tvTitle = (TextView) getView(R.id.tv_title);
            TextView tvTips = (TextView) getView(R.id.text_tips);
            TextView tvStatus = (TextView) getView(R.id.text_status);
            ImageView ivArrowIcon = (ImageView) getView(R.id.iv_arrow_icon);
            ImageView civAvatLeft = (ImageView) getView(R.id.civ_avat_left);
            ImageView civAvatRight = (ImageView) getView(R.id.civ_avat);

            tvStatus.setVisibility(View.GONE);
            ivArrowIcon.setVisibility(View.GONE);
            tvTips.setVisibility(View.GONE);

            civAvatRight.setVisibility(View.VISIBLE);
            civAvatLeft.setVisibility(View.VISIBLE);
            ivArrowIcon.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = civAvatRight.getLayoutParams();
            lp.height = CocoUtils.dp2px(25);
            lp.width = CocoUtils.dp2px(25);
            civAvatRight.setLayoutParams(lp);
            civAvatRight.setImageResource(R.drawable.red_bkg);
            tvStatus.setTextColor(Color.WHITE);
            tvStatus.setText("" + s.requestCount);
            civAvatLeft.setImageResource(R.drawable.me_friends_friend_request);
            tvTitle.setText(R.string.request_friend);
        } else if (s.title != null && s.title.equals("title")) {
            TextView tvTitle = (TextView) getView(R.id.tv_title);
            // 这里设置好友人数
            tvTitle.setText(String.format(App.getContext().getResources().getString(R.string.friends_count), s.requestCount>=0?s.requestCount:0));
        } else {
            TextView tvTitle = (TextView) getView(R.id.tv_title);
            TextView tvTips = (TextView) getView(R.id.text_tips);
            TextView tvStatus = (TextView) getView(R.id.text_status);
            ImageView ivArrowIcon = (ImageView) getView(R.id.iv_arrow_icon);
            ImageView civAvatLeft = (ImageView) getView(R.id.civ_avat_left);

            tvStatus.setVisibility(View.GONE);
            tvTips.setVisibility(View.GONE);

            civAvatLeft.setVisibility(View.GONE);
            ivArrowIcon.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);

            tvTitle.setText(s.nickName.isEmpty()?s.userName:s.nickName);
            civAvatLeft.setVisibility(View.VISIBLE);
            Glide.with(App.getContext()).load(s.avatar).error(R.drawable.profile_men)
                    .signature(new StringSignature(user.getCreatedAt())).into(civAvatLeft);
        }
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<FriendsListViewHolder>() {
        @Override
        public FriendsListViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            int layoutRes;
            switch (viewType) {
                case BaseNormalViewHolder.HEAD:
                    layoutRes = R.layout.settings_item;
                    break;
                case BaseNormalViewHolder.TITLE:
                    layoutRes = R.layout.title_item;
                    break;
                default:
                    layoutRes = R.layout.settings_item;
                    break;
            }
            return new FriendsListViewHolder(parent.getContext(), parent, layoutRes);
        }
    };

    @Override
    public void onClick(View v) {

        EventBus.getDefault().post(new FriendsListItemClickEvent(v, getAdapterPosition(), mFollower.objId));
    }
}
