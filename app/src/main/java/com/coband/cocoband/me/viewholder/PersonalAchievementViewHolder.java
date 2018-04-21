package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

/**
 * Created by mai on 17-4-27.
 */

public class PersonalAchievementViewHolder extends BaseViewHolder<String> {
    public PersonalAchievementViewHolder(Context context, ViewGroup root, int layoutRes, int viewType) {
        super(context, root, layoutRes);
    }

    @Override
    public void bindData(String title, int position, boolean isSelected) {
        itemView.setBackgroundColor(Color.WHITE);
        TextView textTitle = (TextView) getView(R.id.tv_title);
        TextView textTips = (TextView) getView(R.id.text_tips);
        TextView textStatus = (TextView) getView(R.id.text_status);
        ImageView ivArrowIcon = (ImageView) getView(R.id.iv_arrow_icon);
        View vDivider =getView(R.id.view_divider);
        textTitle.setPadding(20, 100,20, 10);
        textStatus.setVisibility(View.GONE);
        ivArrowIcon.setVisibility(View.GONE);
        vDivider.setVisibility(View.GONE);
        textTitle.setText(title.substring(0, title.indexOf(",")));
        textTips.setText(title.substring(title.indexOf(",")+1));
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<PersonalAchievementViewHolder>() {
        @Override
        public PersonalAchievementViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            int layout_id = R.layout.settings_item;
            return new PersonalAchievementViewHolder(parent.getContext(), parent, layout_id, viewType);
        }
    };
}
