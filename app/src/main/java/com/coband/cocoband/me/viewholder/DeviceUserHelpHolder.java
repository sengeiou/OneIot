package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coband.App;
import com.coband.cocoband.mvp.model.bean.UserHelpBean;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

/**
 * Created by mai on 17-6-16.
 */

public class DeviceUserHelpHolder extends BaseViewHolder<UserHelpBean> implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvContent;

    public DeviceUserHelpHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);

    }

    @Override
    public void bindData(UserHelpBean bean, int position, boolean isSelected) {
        tvTitle.setText(bean.title);
        tvContent.setText(bean.content);
        tvTitle.setOnClickListener(this);
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<DeviceUserHelpHolder>() {
        @Override
        public DeviceUserHelpHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new DeviceUserHelpHolder(parent.getContext(), parent, R.layout.item_device_help);
        }
    };

    @Override
    public void onClick(View v) {

        if (tvContent.getVisibility() == View.VISIBLE) {
            tvContent.setVisibility(View.GONE);
            Drawable nav_down= App.getContext().getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_18dp);
            nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
            tvTitle.setCompoundDrawables(null, null, nav_down, null);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            Drawable nav_up= App.getContext().getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_18dp);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            tvTitle.setCompoundDrawables(null, null, nav_up, null);
        }
    }
}
