package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Config;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mai on 17-5-11.
 */

public class SettingsViewHolder extends BaseNormalViewHolder<String> implements View.OnClickListener {
    private static final String TAG = "SettingsViewHolder";

    public SettingsViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bindData(String o, int position, boolean isSelected) {
//        tvTitle.setText(o);
        if (o.equals(getString(R.string.unit)) || o.equals(getString(R.string.sport)) || o.equals(getString(R.string.other))) {
            itemView.setClickable(false);
            tvTitle.setVisibility(View.GONE);
            tvTips.setText(o);
            ivArrowIcon.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            vDivider.setVisibility(View.GONE);
        } else {
            itemView.setClickable(true);
            tvTitle.setVisibility(View.VISIBLE);
            tvTips.setVisibility(View.GONE);
            tvTitle.setText(o);
            ivArrowIcon.setVisibility(View.VISIBLE);
            ivArrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
            vDivider.setVisibility(View.VISIBLE);
            if (o.equals(R.string.clear_cache)) {
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.GONE);
            }

            // unit
            if (o.equals(getString(R.string.inch)) || o.equals(getString(R.string.metric_system))) {
                ivArrowIcon.setVisibility(View.INVISIBLE);
                boolean isMetric = DataManager.getInstance().getUnitSystem() == Config.METRIC;
                if (isMetric && o.equals(getString(R.string.metric_system))) {
                    ivArrowIcon.setVisibility(View.VISIBLE);
                    ivArrowIcon.setImageResource(R.drawable.ic_done_black_24dp);
                } else if (!isMetric && o.equals(getString(R.string.inch))) {
                    ivArrowIcon.setVisibility(View.VISIBLE);
                    ivArrowIcon.setImageResource(R.drawable.ic_done_black_24dp);
                }
            }
        }

    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<SettingsViewHolder>() {
        @Override
        public SettingsViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new SettingsViewHolder(parent.getContext(), parent, R.layout.settings_item);
        }
    };

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick  ");
//        RxBus.get().post(new EventBusEvent<>(EventConstants.GOALS, new MeItemClickEvent(v, getAdapterPosition())));
        EventBus.getDefault().post(new MeItemClickEvent(v, getAdapterPosition()));
    }
}
