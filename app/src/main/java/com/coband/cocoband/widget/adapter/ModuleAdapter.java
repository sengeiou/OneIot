package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.coband.cocoband.mvp.model.entity.Module;
import com.coband.cocoband.widget.ItemTouchHelperAdapter;
import com.coband.watchassistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 17-5-15.
 */

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleVH> implements ItemTouchHelperAdapter {
    private List<Module> mModules = new ArrayList<>();
    private OnSwitchCheckListener mListener;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mModules, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public interface OnSwitchCheckListener {
        void onSwitchCheck(int position, boolean checked);
    }

    public void setOnSwitchCheckListener(OnSwitchCheckListener listener) {
        this.mListener = listener;
    }

    public ModuleAdapter(List<Module> modules) {
        this.mModules = modules;
    }

    @Override
    public ModuleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ModuleVH(itemView);
    }

    @Override
    public void onBindViewHolder(ModuleVH holder, final int position) {
        holder.mTvTitle.setText(mModules.get(position).getTitle());
        holder.mSwitchCompat.setChecked(mModules.get(position).isAvaviable());
        holder.mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    mListener.onSwitchCheck(position, isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModules.size();
    }


    static class ModuleVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView mTvTitle;

        @BindView(R.id.switch_module)
        SwitchCompat mSwitchCompat;

        public ModuleVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
