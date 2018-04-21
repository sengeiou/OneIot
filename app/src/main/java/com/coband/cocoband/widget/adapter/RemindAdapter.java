package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coband.cocoband.mvp.model.bean.RemindItem;
import com.coband.watchassistant.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imco on 4/5/16.
 */
public class RemindAdapter extends RecyclerView.Adapter<RemindAdapter.ViewHolder> {

    private List<RemindItem> items;

    public RemindAdapter(List<RemindItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = items.get(position).getTitle();
        String status = items.get(position).getStatus();
        holder.title.setText(title);
        if (status != null) {
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText(status);
        } else {
            holder.status.setVisibility(View.GONE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView title;

        @BindView(R.id.text_status)
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
