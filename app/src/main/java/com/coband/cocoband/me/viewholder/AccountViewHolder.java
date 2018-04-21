package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.mvp.model.bean.AccountBean;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tgc on 17-5-17.
 */

public class AccountViewHolder extends BaseViewHolder<AccountBean> implements View.OnClickListener{

    public AccountViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    public void bindData(AccountBean s, int position, boolean isSelected) {
        itemView.setOnClickListener(this);
        if (s.title!=null && s.title.equals("head")){

        }else if (s.title!=null && s.title.equals("title")){
            TextView tvTitle = (TextView) getView(R.id.tv_title);
            TextView tvTips = (TextView) getView(R.id.text_tips);
            TextView tvStatus = (TextView) getView(R.id.text_status);
            ImageView ivArrowIcon = (ImageView) getView(R.id.iv_arrow_icon);

            tvStatus.setVisibility(View.GONE);
            ivArrowIcon.setVisibility(View.GONE);
            tvTitle.setText(s.titleText);
            tvTips.setText(s.tip);
        }else {
            TextView tvTitle = (TextView) getView(R.id.tv_title);
            TextView tvTips = (TextView) getView(R.id.text_tips);
            TextView tvStatus = (TextView) getView(R.id.text_status);
            ImageView ivArrowIcon = (ImageView) getView(R.id.iv_arrow_icon);

            tvStatus.setVisibility(View.GONE);
            tvTips.setVisibility(View.GONE);
            tvTitle.setText(s.titleText);
            ivArrowIcon.setVisibility(View.VISIBLE);
        }
    }

    public static ViewHolderCreator HOLDER_CREATOR=new ViewHolderCreator<AccountViewHolder>() {
        @Override
        public AccountViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            int layoutRes = R.layout.settings_item;

            return new AccountViewHolder(parent.getContext(), parent, layoutRes);
        }
    };

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new MeItemClickEvent(v, getAdapterPosition()));
    }
}
