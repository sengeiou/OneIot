package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tgc on 17-5-17.
 */

public class UseHelpViewHolder extends BaseViewHolder<String> implements View.OnClickListener {
    String dbl = "CoBand DBL";
    String k3 = "CoBand K3";
    String k4 = "CoBand K4";
    String watch = "CoWatch";


    public UseHelpViewHolder(Context context, ViewGroup root, int layout, int viewType) {
        super(context, root, layout);
    }

    @Override
    public void bindData(String title, int position, boolean isSelected) {
        itemView.setOnClickListener(this);

        TextView bandName = (TextView) getView(R.id.choice_deivce_tv);
        bandName.setText(title);
        final ImageView bandImage = (ImageView) getView(R.id.choice_device_iv);
        if (title.equals(dbl)) {
            bandImage.setImageResource(R.drawable.imco_select_device_imcoband_black);
        } else if (title.equals(k3)) {
            bandImage.setImageResource(R.drawable.imco_select_device_cobandk3_black);
        } else if (title.equals(k4)) {
            bandImage.setImageResource(R.drawable.imco_select_device_cobandk4_silver);
        } else if (title.equals(watch)) {
            bandImage.setImageResource(R.drawable.imco_select_device_cowatch_silver);
        }

    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<UseHelpViewHolder>() {
        @Override
        public UseHelpViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new UseHelpViewHolder(parent.getContext(), parent, R.layout.choice_device_item, viewType);
        }
    };

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new MeItemClickEvent(v, getAdapterPosition()));
    }

}
