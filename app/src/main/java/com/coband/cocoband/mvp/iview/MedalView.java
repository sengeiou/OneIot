package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.MedalBean;

import java.util.ArrayList;

/**
 * Created by tgc on 17-11-1.
 */

public interface MedalView extends BaseView {
    void showList(ArrayList<MedalBean> medalBeanList);

    void showFragmentDialog(int position, MedalBean medalBean);
}
