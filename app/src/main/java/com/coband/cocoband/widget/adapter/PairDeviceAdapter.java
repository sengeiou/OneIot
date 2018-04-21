package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.watchassistant.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 17-5-19.
 */

public class PairDeviceAdapter extends RecyclerView.Adapter<PairDeviceAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemtClickListener = listener;
    }

    private List<PairDevice> mPairDevices;
    private OnItemClickListener mOnItemtClickListener;

    public PairDeviceAdapter(List<PairDevice> pairDevices) {
        this.mPairDevices = pairDevices;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pair_device, parent, false);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.mIvIcon.setImageResource(mPairDevices.get(position).getIconId());
        holder.mTvName.setText(mPairDevices.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemtClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPairDevices.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_device_icon)
        ImageView mIvIcon;

        @BindView(R.id.tv_device_name)
        TextView mTvName;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
