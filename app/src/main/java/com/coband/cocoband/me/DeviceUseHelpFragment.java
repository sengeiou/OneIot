package com.coband.cocoband.me;

import com.coband.cocoband.me.viewholder.DeviceUserHelpHolder;
import com.coband.cocoband.mvp.model.bean.UserHelpBean;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mai on 17-6-16.
 */

public class DeviceUseHelpFragment extends BaseListFragment {
    public final static String TAG = "DeviceUseHelpFragment";
    @Override
    protected void init() {
        ArrayList<UserHelpBean> dataList = new ArrayList<>();
        List<String> titles = Arrays.asList(getResources().getStringArray(R.array.help_title));
        List<String> contents = Arrays.asList(getResources().getStringArray(R.array.help_content));

        for (int i = 0; i < titles.size(); i++) {
            UserHelpBean userHelpBean = new UserHelpBean(""+(i+1)+". "+titles.get(i), contents.get(i));
            dataList.add(userHelpBean);
        }
        mAdapter.setDataListAndNotify(dataList);
    }

    @Override
    protected MultiItemAdapter setMultiItemAdapter() {
        return new MultiItemAdapter(DeviceUserHelpHolder.class);
    }

    @Override
    protected int setTitleRes() {
        return R.string.fragment_help_title;
    }
}
