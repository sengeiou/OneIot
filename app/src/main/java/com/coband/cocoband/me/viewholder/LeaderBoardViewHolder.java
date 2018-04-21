package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.coband.App;
import com.coband.cocoband.event.me.LeaderBoardItemEvent;
import com.coband.cocoband.event.me.LeaderBoardLikesEvent;
import com.coband.cocoband.mvp.model.IMCOCallback;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankBean;
import com.coband.cocoband.mvp.model.bean.todayrank.UpdateResult;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.watchassistant.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by mai on 17-5-8.
 */

public class LeaderBoardViewHolder extends BaseNormalViewHolder<TodayRankBean> implements OnLikeListener, View.OnClickListener {

    private TodayRankBean mTodayRand;

    public LeaderBoardViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    public void bindData(TodayRankBean todayRank, int position, boolean isSelected) {
        itemView.setOnClickListener(this);
        if (todayRank.user == null) return;
        this.mTodayRand = todayRank;
        tvStatus.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvStatus.setTextSize(22);
        tvStatus.setTextColor(Color.BLACK);
        ivArrowIcon.setVisibility(View.GONE);
        lbLike.setVisibility(View.VISIBLE);
        llLike.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.GONE);
        tvTitle.setMaxEms(7);
        if (getAdapterPosition() == 0) {
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvTitle.setTextSize(18);
            tvTitle.setTextColor(Color.BLACK);
            tvTips.setVisibility(View.VISIBLE);
            if (todayRank.timeStamp <= 100 && todayRank.timeStamp > 0) {
                tvTips.setText(String.format(App.getContext().getString(R.string.rank), todayRank.timeStamp));// 这里是名次
            } else {
                tvTips.setText(String.format(App.getContext().getString(R.string.rank), 100) + "+");
            }
            tvNumLeft.setVisibility(View.GONE);
            civAvatLeft.setVisibility(View.GONE);
            civAvatLeftBig.setVisibility(View.VISIBLE);
            Glide.with(App.getContext()).load(todayRank.user.getAvatar())
                    .error(R.drawable.profile_men).centerCrop().fitCenter().into(civAvatLeftBig);
        } else {
            tvTips.setVisibility(View.GONE);
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvTitle.setTextSize(16);
            tvTitle.setTextColor(Color.BLACK);
            civAvatLeftBig.setVisibility(View.GONE);
            civAvatLeft.setVisibility(View.VISIBLE);
            tvNumLeft.setVisibility(View.VISIBLE);
            tvNumLeft.setText("" + getAdapterPosition());
            Glide.with(App.getContext()).load(todayRank.user.getAvatar())
                    .error(R.drawable.profile_men).centerCrop().fitCenter().into(civAvatLeft);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) civAvatLeft.getLayoutParams();
            lp.leftMargin = 0;
            civAvatLeft.setLayoutParams(lp);

            if (todayRank.user.getUsername().equals(DBHelper.getInstance().getUser().getUsername())) {
                rlRoot.setBackgroundColor(Color.parseColor("#f8f8f8"));
            } else {
                rlRoot.setBackgroundColor(Color.WHITE);
            }

            if (getAdapterPosition() == 1) {
                tvNumLeft.setVisibility(View.GONE);
                ivLeft.setVisibility(View.VISIBLE);
                ivLeft.setImageResource(R.drawable.leaderboard_ic_gold);
            } else if (getAdapterPosition() == 2) {
                tvNumLeft.setVisibility(View.GONE);
                ivLeft.setVisibility(View.VISIBLE);
                ivLeft.setImageResource(R.drawable.leaderboard_ic_silver);
            } else if (getAdapterPosition() == 3) {
                tvNumLeft.setVisibility(View.GONE);
                ivLeft.setVisibility(View.VISIBLE);
                ivLeft.setImageResource(R.drawable.leaderboard_ic);
            }
        }

        tvStatus.setText("" + todayRank.step);
        tvTitle.setText((todayRank.user.getNickName() != null && !todayRank.user.getNickName().trim().isEmpty())?
                todayRank.user.getNickName() : todayRank.user.getUsername());

        lbLike.setLiked(false);
        tvLikes.setTextColor(getContext().getResources().getColor(R.color.color_e0e0e0));
        if (mTodayRand.likes == null) mTodayRand.likes = new ArrayList<>();
        if (todayRank.likes.contains(DBHelper.getInstance().getUser().getObjectId())) {
            tvLikes.setTextColor(getContext().getResources().getColor(R.color.color_ff6464));
            lbLike.setLiked(true);
        }
        tvLikes.setText("" + todayRank.likes.size());
        lbLike.setOnLikeListener(this);
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<LeaderBoardViewHolder>() {
        @Override
        public LeaderBoardViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new LeaderBoardViewHolder(parent.getContext(), parent, R.layout.settings_item);
        }
    };

    @Override
    public void liked(LikeButton likeButton) {

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }

    @Override
    public void onClick(View v) {
        LeaderBoardItemEvent itemEvent = new LeaderBoardItemEvent();
        itemEvent.position = getAdapterPosition();
        itemEvent.todayRankBean = mTodayRand;
        EventBus.getDefault().post(itemEvent);
    }
}
