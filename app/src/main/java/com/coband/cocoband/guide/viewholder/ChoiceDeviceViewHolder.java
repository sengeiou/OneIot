package com.coband.cocoband.guide.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tgc on 17-4-19.
 */

public class ChoiceDeviceViewHolder extends BaseViewHolder<String> implements View.OnClickListener {

    private static final String DBL = "CoBand DBL";
    private static final String K3 = "CoBand K3";
    private static final String K4 = "CoBand K4";
    private static final String WATCH = "CoWatch";
    private static final String K9 = "CoBand K9";

    private String title;
    int viewType;


    public ChoiceDeviceViewHolder(Context context, ViewGroup root, int layout, int viewType) {
        super(context, root, layout);
        this.viewType = viewType;
        Log.d("viewType", "viewType" + viewType);
    }

    @Override
    public void bindData(String title, int position, boolean isSelected) {
        this.title = title;
        itemView.setOnClickListener(this);
        switch (viewType) {
            case FLOOR:

                break;
            default:
                TextView bandName = (TextView) itemView.findViewById(R.id.choice_deivce_tv);
                bandName.setText(title);
                final ImageView bandImage = (ImageView) itemView.findViewById(R.id.choice_device_iv);
                if (title.equals(DBL)) {
                    bandImage.setImageResource(R.drawable.imco_select_device_imcoband_black);
                } else if (title.equals(K3)) {
                    bandImage.setImageResource(R.drawable.imco_select_device_cobandk3_black);
                } else if (title.equals(K4)) {
                    bandImage.setImageResource(R.drawable.imco_select_device_cobandk4_silver);
                } else if (title.equals(WATCH)) {
                    bandImage.setImageResource(R.drawable.imco_select_device_cowatch_silver);
                } else if (title.equals(K9)) {
                    bandImage.setImageResource(R.drawable.imco_device_coband_k9_black);
                }
                break;
        }
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<ChoiceDeviceViewHolder>() {
        @Override
        public ChoiceDeviceViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            int layout_id;
            switch (viewType) {
                case FLOOR:
                    layout_id = R.layout.choice_device_floor_item;
                    break;
                default:
                    layout_id = R.layout.choice_device_item;
                    break;
            }


            return new ChoiceDeviceViewHolder(parent.getContext(), parent, layout_id, viewType);

        }
    };


    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new MeItemClickEvent(v, getAdapterPosition()));
    }
}
