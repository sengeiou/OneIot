package com.coband.cocoband.me.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coband.App;
import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.DecimalUtils;
import com.coband.common.utils.UnitUtils;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.R;
import com.coband.watchassistant.User;
import com.imcorecyclerviewlib.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by mai on 17-4-14.
 */

public class PersonalInfoViewHolder extends BaseViewHolder<String> implements View.OnClickListener {
    private Context mContext;

    public PersonalInfoViewHolder(Context context, ViewGroup root, int layoutRes, int viewType) {
        super(context, root, layoutRes);
        mContext = context;
        itemView.setOnClickListener(this);
    }

    @Override
    public void bindData(String title, int position, boolean isSelected) {
        Account account = DataManager.getInstance().getCurrentAccount();
        TextView textTitle = (TextView) getView(R.id.tv_title);
        TextView textTips = (TextView) getView(R.id.text_tips);
        TextView textStatus = (TextView) getView(R.id.text_status);
        textTitle.setText(title);
        textTips.setVisibility(View.GONE);

        if (equals(title, R.string.modify_avatar)) {
            textStatus.setVisibility(View.GONE);
            ImageView civAvat = (ImageView) getView(R.id.civ_avat);
            civAvat.setVisibility(View.VISIBLE);
            Glide.with(App.getContext()).load(account.getAvatar()).error(R.drawable.profile_men)
                    .fitCenter().centerCrop().into(civAvat);
        } else if (equals(title, R.string.sufaceImage)) {
            textStatus.setText(R.string.modify);
        } else if (equals(title, R.string.nickname)) {
            textStatus.setText(account.getNickname());
        } else if (equals(title, R.string.gender)) {
            textStatus.setText(account.getGender() == null ? Config.MALE :
                    account.getGender().equals(Config.MALE) ?
                            mContext.getString(R.string.male) : mContext.getString(R.string.female));
        } else if (equals(title, R.string.birthday) && account.getBirthday() != null) {
            textStatus.setText(account.getBirthday());
        } else if (equals(title, R.string.height)) {
            if (DataManager.getInstance().getUnitSystem() == Config.METRIC) {
                textStatus.setText("" + DecimalUtils.retainDecimal(1, account.getHeight()) + getContext().getString(R.string.cm));
            } else {
                double height = Utils.changeCmToFt(account.getHeight());
                List<Integer> heightList = UnitUtils.inchToFtInch(height);
                textStatus.setText("" + heightList.get(0) + getContext().getString(R.string.foot) +
                        heightList.get(1) + getContext().getString(R.string.inch_unit));
            }
        } else if (equals(title, R.string.weight)) {
            if (DataManager.getInstance().getUnitSystem() == Config.METRIC) {
                textStatus.setText("" + DecimalUtils.retainDecimal(1, account.getWeight()) +
                        getContext().getString(R.string.kg));
            } else {
                double weight = Utils.changeKgToLb(account.getWeight());
                textStatus.setText(Utils.format2OneDecimalPlaces(weight) + getContext().getString(R.string.lb));
            }
        }
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<PersonalInfoViewHolder>() {
        @Override
        public PersonalInfoViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            int layout_id = R.layout.settings_item;
            return new PersonalInfoViewHolder(parent.getContext(), parent, layout_id, viewType);
        }
    };

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new MeItemClickEvent(v, getAdapterPosition()));
    }
}
