package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coband.App;
import com.coband.common.utils.DateUtils;
import com.coband.interactivelayer.bean.BloodPressurePacket;
import com.coband.watchassistant.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgc on 17-9-18.
 */

public class BloodMeasureAdapter extends BaseRecyclerAdapter<BloodPressurePacket> {
    private List<Long> timeList;
    private String formatDate = "";

    public void setTime(List<Long> timeList) {
        this.timeList = timeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blood_pressure_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition,
                       BloodPressurePacket data) {
        if (viewHolder instanceof MyViewHolder) {

            long timeStamp = data.getTimeStamp();
            int second = data.getSecond();
            long hour = timeStamp * 60 + second;
            if (null != timeList && !timeList.isEmpty() && null != timeList.get(RealPosition)) {
                formatDate = DateUtils.getFormatDate(timeList.get(RealPosition));
            }

            String time = DateUtils.getTime((timeStamp / 60) + ":" + (timeStamp - timeStamp / 60 * 60));
            if (hour < 6 * 60 * 60) {
                ((MyViewHolder) viewHolder).dayTimeTv.setText(formatDate + " " + App.getContext().
                        getString(R.string.beforedawn) + " " + time);

            } else if (hour < 12 * 60 * 60) {
                ((MyViewHolder) viewHolder).dayTimeTv.setText(formatDate + " " + App.getContext().
                        getString(R.string.morning) + " " + time);

            } else if (hour < 18 * 60 * 60) {
                ((MyViewHolder) viewHolder).dayTimeTv.setText(formatDate + " " + App.getContext().
                        getString(R.string.afternoon) + " " + time);
            } else {
                ((MyViewHolder) viewHolder).dayTimeTv.setText(formatDate + " " + App.getContext().
                        getString(R.string.night) + " " + time);
            }

            ((MyViewHolder) viewHolder).bloodValueTv.setText(data.getBpHigh()
                    + "/" + data.getBpLow());
//            ((MyViewHolder) viewHolder).heartValueTv.setText(data.getHeartRate() + "");

            formatDate = "";
        }
    }

    class MyViewHolder extends BaseRecyclerAdapter.Holder {
        @BindView(R.id.heart_value_tv)
        TextView heartValueTv;
        @BindView(R.id.blood_value_tv)
        TextView bloodValueTv;
        @BindView(R.id.day_time_tv)
        TextView dayTimeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
