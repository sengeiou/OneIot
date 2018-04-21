package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.common.utils.Config;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mai on 17-5-11.
 */

public class GoalsViewHolder extends BaseNormalViewHolder<String> implements View.OnClickListener {
    private static final String TAG = "GoalsViewHolder";

    public GoalsViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bindData(String o, int position, boolean isSelected) {
        tvTips.setVisibility(View.GONE);
        tvTitle.setText(o);

        if (o.equals(getString(R.string.walk))) {
            Integer stepTarget = DataManager.getInstance().getCurrentAccount().getStepTarget();
            if (stepTarget != null) {
                tvStatus.setText("" + stepTarget + " " + getString(R.string.step_unit));
            } else {
                tvStatus.setText("" + Config.DEFAULT_STEP_TARGET + " " + getString(R.string.step_unit));
            }
        } else if (o.equals(getString(R.string.sleep))) {
            Integer sleepTarget = DataManager.getInstance().getCurrentAccount().getSleepTarget();
            if (sleepTarget == null) {
                tvStatus.setText("" + Config.DEFAULT_SLEEP_TARGET / 60 + " " + getString(R.string.hour));
            } else {
                float sleepTargetFloat = ((float) (sleepTarget)) / 60f;
                sleepTargetFloat = (float) (Math.round(sleepTargetFloat * 10)) / 10;
                tvStatus.setText("" + sleepTargetFloat + " " + getString(R.string.hour));
            }
        } else if (o.equals(getString(R.string.weight))) {
            int unit = DataManager.getInstance().getUnitSystem();
            Double weightTarget = DBHelper.getInstance().getCurrentAccount().getWeightTarget();

            if (weightTarget == null) {
                if(unit == Config.METRIC) {
                    tvStatus.setText("" + Config.DEFAULT_WEIGHT_TARGET_METRIC + " " + getString(R.string.kg));
                } else {
                    tvStatus.setText("" + Config.DEFAULT_WEIGHT_TARGET_INCH + getString(R.string.lb));
                }
            } else {
                if (unit == Config.METRIC) {
                    tvStatus.setText("" + Utils.format2OneDecimalPlaces(weightTarget) + " " + getString(R.string.kg));
                } else {
                    weightTarget = Utils.changeKgToLb(weightTarget);
                    tvStatus.setText("" + Utils.format2OneDecimalPlaces(weightTarget) + " " + getString(R.string.lb));
                }
            }
        }
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<GoalsViewHolder>() {
        @Override
        public GoalsViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new GoalsViewHolder(parent.getContext(), parent, R.layout.settings_item);
        }
    };

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick  ");
        EventBus.getDefault().post(new MeItemClickEvent(v, getAdapterPosition()));
    }
}
