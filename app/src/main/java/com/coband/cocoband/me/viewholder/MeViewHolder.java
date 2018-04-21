package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coband.App;
import com.coband.cocoband.event.me.MeMenuClickEvent;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.R;
import com.coband.watchassistant.User;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by mai on 17-4-14.
 */

public class MeViewHolder extends BaseViewHolder<String> implements View.OnClickListener {
    private int mViewType;
    private ImageView ivProfile;

    public MeViewHolder(Context context, ViewGroup root, int layoutRes, int viewType) {
        super(context, root, layoutRes);
        ButterKnife.bind(this, itemView);
        this.mViewType = viewType;
        itemView.setOnClickListener(this);
    }

    @Override
    public void bindData(String title, int position, boolean isSelected) {
        switch (mViewType) {
            case HEAD:
                // setup avatar
                ivProfile = (ImageView) itemView.findViewById(R.id.iv_avatar);
//                User user = DBHelper.getInstance().getUser();
                Account account = DataManager.getInstance().getCurrentAccount();
                if (account != null && account.getAvatar() != null) {
                    Glide.with(App.getContext())
                            .load(account.getAvatar())
                            .error(R.drawable.profile_men)
                            .fitCenter().centerCrop()
                            .into(ivProfile);
                }

                TextView tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
                tvUsername.setText(account.getNickname() == null ? "CoBand" : account.getNickname());
                break;
            default:
                TextView textTitle = (TextView) itemView.findViewById(R.id.tv_title);
                TextView textTips = (TextView) itemView.findViewById(R.id.text_tips);
                TextView textStatus = (TextView) itemView.findViewById(R.id.text_status);
                textTitle.setText(title);
                textTips.setVisibility(View.GONE);
                textStatus.setVisibility(View.GONE);
                break;
        }
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<MeViewHolder>() {
        @Override
        public MeViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            int layout_id = R.layout.settings_item;
            switch (viewType) {
                case HEAD:
                    layout_id = R.layout.head_item_me;
                    break;
                case FLOOR:
                    break;
                default:
                    layout_id = R.layout.settings_item;
                    break;
            }
            return new MeViewHolder(parent.getContext(), parent, layout_id, viewType);
        }
    };

    @Override
    public void onClick(View v) {
//        RxBus.get().post(new MeItemClickEvent(v, getAdapterPosition()));
        EventBus.getDefault().post(new MeMenuClickEvent(v, getAdapterPosition()));
    }


}
