package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.cocoband.event.me.MedalItemClickEvent;
import com.coband.cocoband.mvp.model.bean.MedalBean;
import com.coband.common.utils.CocoUtils;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgc on 17-5-10.
 */

public class MedalViewHolder extends BaseViewHolder<MedalBean> implements View.OnClickListener{

    private MedalBean bean;
    private ImageView medalIcon;
    private final TextView medalTitle;
    private final TextView medalTip;
    private List<MedalBean> list;

    public MedalViewHolder(Context context, ViewGroup root, int layoutRes, int viewType) {
        super(context, root, layoutRes);
        medalIcon = (ImageView) getView(R.id.medal_icon);
        medalTitle = (TextView) getView(R.id.medal_title);
        medalTip = (TextView) getView(R.id.medal_tip);
    }

    @Override
    public void bindData(MedalBean bean, int position, boolean isSelected) {
        itemView.setOnClickListener(this);
        this.bean = bean;
        list = new ArrayList<>();

        medalIcon.setImageResource(bean.getMedalRes());
        String title = CocoUtils.getAchievementTitleNew(bean.getMedalName());
        medalTitle.setText(title);
        medalTip.setText(CocoUtils.getMedalTipFromTitle(bean.getMedalName()));

        medalTitle.setTextColor(Color.parseColor("#242424"));
        medalTip.setTextColor(Color.parseColor("#a0a0a0"));


        list.add(bean);
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<MedalViewHolder>() {
        @Override
        public MedalViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {

            return new MedalViewHolder(parent.getContext(), parent, R.layout.medal_item, viewType);
        }
    };

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new MedalItemClickEvent(v,getAdapterPosition(),list));
    }

}
