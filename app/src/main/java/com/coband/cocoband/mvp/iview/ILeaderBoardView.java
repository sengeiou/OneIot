package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankBean;

import java.util.ArrayList;

/**
 * Created by mai on 31/05/17.
 */

public interface ILeaderBoardView extends BaseView {
    void getLeaderBoardData(ArrayList<TodayRankBean> list);
    void cacheIsNull();
}
