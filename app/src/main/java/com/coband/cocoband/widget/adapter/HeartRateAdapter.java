package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coband.cocoband.mvp.model.bean.RateOneDayBean;
import com.coband.cocoband.widget.BaseHolder;
import com.coband.common.utils.DateUtils;
import com.coband.watchassistant.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mqh on 9/20/16.
 */
public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateAdapter.HeartHolder> {
    private List<RateOneDayBean> dataList;
    private int floorViewType = FloorHolder.LAYOUT_TYPE;
    private int normalViewType = HeartHolder.LAYOUT_TYPE;
    public boolean isHasMore = true;


    public HeartRateAdapter(List<RateOneDayBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public HeartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == floorViewType) {
//            View floorView = LayoutInflater.from(parent.getContext()).inflate(floorViewType, parent, false);
//            return new FloorHolder(floorView);
//        } else {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(normalViewType, parent, false);
        return new HeartHolder(contentView);
//        }
    }


    @Override
    public void onBindViewHolder(HeartHolder holder, int position) {
//        ((BaseHolder)holder).onBindViewHolder(holder.itemView,
//                position+1 == getItemCount()?(isHasMore?new Object():null):dataList.get(position), position);
        RateOneDayBean rateBean = dataList.get(position);
        holder.time.setText(DateUtils.getDateBySeconds("yyyy/MM/dd", rateBean.getCalendar()) + " " +
                DateUtils.getTimeByMinute("HH:mm", rateBean.getTime()));
        holder.data.setText("" + rateBean.getRate() + " BPM ");

    }

    @Override
    public int getItemCount() {
//        return dataList.size() + 1;
        return dataList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position + 1 == getItemCount() ? floorViewType : normalViewType;
//    }

    class HeartHolder extends BaseHolder<RateOneDayBean> {

        @BindView(R.id.tv_heart_time)
        public TextView time;
        @BindView(R.id.tv_heart_data)
        public TextView data;

        public static final int LAYOUT_TYPE = R.layout.heart_rate_item;

        public HeartHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getType() {
            return R.layout.heart_rate_item;
        }

        @Override
        public void onBindViewHolder(View view, RateOneDayBean rateBean, int position) {
            if (null != rateBean) {
                time.setText(DateUtils.getDateBySeconds("yyyy/MM/dd", rateBean.getCalendar()) + " " +
                        DateUtils.getTimeByMinute("HH:mm", rateBean.getTime()));
                data.setText("" + rateBean.getRate() + " BPM ");
            }
        }
    }

    class FloorHolder extends BaseHolder<Object> {

        @BindView(R.id.progressbar)
        public ProgressBar progressBar;

        @BindView(R.id.tv_state)
        public TextView tvState;

        public static final int LAYOUT_TYPE = R.layout.list_footer_view;

        public FloorHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getType() {
            return LAYOUT_TYPE;
        }

        @Override
        public void onBindViewHolder(View view, Object obj, int position) {
            boolean isHasMore = (obj != null);
            progressBar.setVisibility(isHasMore ? View.VISIBLE : View.INVISIBLE);
            tvState.setText(isHasMore ? "Loading..." : "No more data");
        }
    }
}
