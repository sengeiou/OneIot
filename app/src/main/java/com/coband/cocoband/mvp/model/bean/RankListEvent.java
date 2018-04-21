package com.coband.cocoband.mvp.model.bean;

/**
 * Created by mai on 17-2-4.
 */

public class RankListEvent {

    public static final int START_UPDATE_DATA = 0;
    public static final int STOP_UPDATE_DATA = 1;
    private int tag;
    public int rankNum;
    public RankListEvent(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }
}
