package com.coband.cocoband.event.me;

import com.coband.cocoband.mvp.model.bean.ResultsList;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankBean;

/**
 * Created by mai on 17-6-9.
 */

public class RankingEvent {
    public RankingEvent(ResultsList<TodayRankBean> ranklist) {
        this.ranklist = ranklist;
    }

    public ResultsList<TodayRankBean> ranklist;

}
