package com.coband.cocoband.widget.adapter;

import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.App;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.RemindApp;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 17-5-27.
 */

public class RemindAppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final PackageManager PM = App.getInstance().getPackageManager();

    public static final int ITEM_CONTENT = 1;
    public static final int ITEM_FOOTER = 2;
    private boolean footClick;
    private View mFooterView;

    private List<RemindApp> mApps;
    private boolean mRemind;

    private OnItemSwitchChangedListener mItemSwitchChangedListener;
    private OnFooterClickListener mFooterClickListener;
    private FooterViewHolder footerViewHolder;


    public RemindAppAdapter(List<RemindApp> apps, boolean remind) {
        mApps = apps;
        mRemind = remind;
        this.footClick = true;
    }

    public void setData(List<RemindApp> apps, boolean remind) {
        mApps = apps;
        mRemind = remind;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
//        notifyItemInserted(mApps.size());
    }

    @Override
    public int getItemViewType(int position) {
//        if (mFooterView == null) return ITEM_CONTENT;
//        if (position == mApps.size()) return ITEM_FOOTER;
//        return ITEM_CONTENT;
        if (position < mApps.size()) {
            return ITEM_CONTENT;
        } else {
            return ITEM_FOOTER;
        }
    }

    public void setRemindStatus(boolean remind) {
        mRemind = remind;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == ITEM_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_footer,
                    parent, false);
            return new FooterViewHolder(view);
        }
        View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new ItemViewHolder(headerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_CONTENT) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            RemindApp app = mApps.get(position);
            itemViewHolder.mIvAppIcon.setImageDrawable(app.getResolveInfo().loadIcon(PM));
            itemViewHolder.mTvAppName.setText(app.getResolveInfo().activityInfo.loadLabel(PM));
            itemViewHolder.mSwitchRemind.setChecked(app.isRemind());
            if (mRemind) {
                itemViewHolder.mSwitchRemind.setOnClickListener(new OnSwitchChangeListener(position));
                itemViewHolder.mSwitchRemind.setEnabled(true);
            } else {
                itemViewHolder.mSwitchRemind.setEnabled(false);
            }
        } else {
            footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.mTvAppFoot.setOnClickListener(new OnClickListener());
        }

    }

    public void setFooterText() {
        if (footClick) {
            footerViewHolder.mTvAppFoot.setText(R.string.drop_list);
            footClick = false;
        } else {
            footerViewHolder.mTvAppFoot.setText(R.string.open_list);
            footClick = true;
        }
        EventBus.getDefault().post(new HandleEvent(footClick ?
                HandleEvent.DROP_REMIND_LIST : HandleEvent.OPEN_REMIND_LIST));
    }

    @Override
    public int getItemCount() {
        return mFooterView == null ? mApps.size() : mApps.size() + 1;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_app_icon)
        ImageView mIvAppIcon;
        @BindView(R.id.tv_app_name)
        TextView mTvAppName;
        @BindView(R.id.switch_remind)
        SwitchCompat mSwitchRemind;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_app_foot)
        TextView mTvAppFoot;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mFooterClickListener != null) {
                mFooterClickListener.onFooterClick();
            }
        }
    }

    private class OnSwitchChangeListener implements View.OnClickListener {
        private int position;

        public OnSwitchChangeListener(int position) {
            this.position = position;
        }


        @Override
        public void onClick(View view) {
            SwitchCompat switchCompat = (SwitchCompat) view;
            mApps.get(position).setRemind(switchCompat.isChecked());
            if (mItemSwitchChangedListener != null) {
                mItemSwitchChangedListener.onItemSwitchChanged(position, switchCompat.isChecked());
            }
        }
    }

    public interface OnItemSwitchChangedListener {
        void onItemSwitchChanged(int position, boolean isChecked);
    }

    public interface OnFooterClickListener {
        void onFooterClick();
    }

    public void setOnItemSwitchChangedListener(OnItemSwitchChangedListener listener) {
        mItemSwitchChangedListener = listener;
    }

    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mFooterClickListener = listener;
    }
}
