package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.cocoband.mvp.model.entity.BleDevice;
import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.watchassistant.R;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 17-5-19.
 */

public class BleDeviceAdapter extends RecyclerView.Adapter<BleDeviceAdapter.VH> {

    private List<BleDevice> mDeviceList;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public BleDeviceAdapter(List<BleDevice> deviceList) {
        this.mDeviceList = deviceList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        BleDevice device = mDeviceList.get(position);
        if (device.getRssi() > C.STONG) {
            holder.mIvRssi.setImageResource(R.drawable.ibd_findimcoband_signal100);
        } else if (device.getRssi() > C.NORMAL) {
            holder.mIvRssi.setImageResource(R.drawable.ibd_findimcoband_signal85);
        } else if (device.getRssi() > C.WEAK) {
            holder.mIvRssi.setImageResource(R.drawable.ibd_findimcoband_signal50);
        } else {
            holder.mIvRssi.setImageResource(R.drawable.ibd_findimcoband_signal25);
        }

//        String deviceName = device.getDevice().getName();
//        if (deviceName != null) {
//            if (PreferencesHelper.getDeviceName(device.getDevice().getAddress()) != null) {
//                holder.mTvDeviceName.setText(PreferencesHelper.getDeviceName(device.getDevice().getAddress()));
//            } else if (deviceName.contains(C.K4_ORIGIN_NAME) || deviceName.contains(C.K4_WITH_BP_ORIGIN_NAME)) {
//                holder.mTvDeviceName.setText("CoBand K4");
//            } else if (deviceName.contains(C.DBL_ORIGIN_NAME) ||
//                    device.getDevice().getName().contains(C.DBL_V1_ORIGIN_NAME)) {
//                holder.mTvDeviceName.setText("CoBand DBL");
//            } else if (deviceName.contains(C.K3_ORIGIN_NAME)) {
//                holder.mTvDeviceName.setText("CoBand K3");
//            } else if (deviceName.contains(C.X1_ORIGIN_NAME)) {
//                holder.mTvDeviceName.setText("CoBand Xone");
//            } else {
//                holder.mTvDeviceName.setText(device.getDevice().getName());
//            }
//        } else {
//            holder.mTvDeviceName.setText("Unknown device");
//        }

        holder.mTvDeviceName.setText(Config.DEFAULT_DEVICE_NAME);
        if (device.getConnectionStatus() == BleDevice.CONNECT_FAILED) {
            holder.mTvConnectionStatus.setVisibility(View.VISIBLE);
            holder.mTvConnectionStatus.setText(holder.connectFailed);
        } else if (device.getConnectionStatus() == BleDevice.CONNECTING) {
            holder.mTvConnectionStatus.setVisibility(View.VISIBLE);
            holder.mTvConnectionStatus.setText(holder.connecting);
        } else {
            holder.mTvConnectionStatus.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device_name)
        TextView mTvDeviceName;
        @BindView(R.id.tv_connecting_status)
        TextView mTvConnectionStatus;
        @BindView(R.id.iv_rssi)
        ImageView mIvRssi;

        @BindString(R.string.signal)
        String signal;

        @BindString(R.string.connect_failed)
        String connectFailed;

        @BindString(R.string.connecting)
        String connecting;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
