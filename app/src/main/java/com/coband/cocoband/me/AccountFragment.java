package com.coband.cocoband.me;


import android.support.v4.app.Fragment;

import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.me.viewholder.AccountViewHolder;
import com.coband.cocoband.mvp.model.bean.AccountBean;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.common.utils.HideMailAddressUtil;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseListFragment {

    public static final String TAG = "AccountFragment";

    @Override
    protected MultiItemAdapter setMultiItemAdapter() {
        return new MultiItemAdapter(AccountViewHolder.class);
    }

    @Override
    protected int setTitleRes() {
        return R.string.account_and_security;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageOnMainThread(MeItemClickEvent event) {

        int position = event.getPosition();
        switch (position) {
//            case 2:
//                addFragment(new ForgotPasswordFragment(),
//                        ForgotPasswordFragment.TAG, true);
//                break;
            default:
                break;
        }

    }

    @Override
    protected void init() {
        List<AccountBean> list = new ArrayList<>();
        AccountBean mailBean = new AccountBean();
        mailBean.title = "title";
        mailBean.setTitleText(getString(R.string.reg_mail));
        String email = DBHelper.getInstance().getUser().getEmail();
        String hideMail = HideMailAddressUtil.hideAddress(email);
        mailBean.setTip(hideMail);
        list.add(mailBean);

        AccountBean usernameBean = new AccountBean();
        usernameBean.title = "title";
        usernameBean.setTitleText(getString(R.string.hint_username));
        // add by ivan. the logic operation not allow on the V layer.
        usernameBean.setTip(DBHelper.getInstance().getUser().getUsername());
        list.add(usernameBean);

//        AccountBean modifyBean = new AccountBean();
//        modifyBean.title = "foot";
//        modifyBean.setTitleText(getString(R.string.modify_password));
//        list.add(modifyBean);
        mAdapter.setDataListAndNotify(list);
    }
}
