package com.coband.cocoband.event.me;

import android.view.View;

import com.coband.cocoband.mvp.model.bean.MedalBean;

import java.util.List;

/**
 * Created by tgc on 17-5-11.
 */

public class MedalItemClickEvent {
    public View view;
    public int position;
    public List<MedalBean> list;
    public MedalItemClickEvent(View view, int position, List<MedalBean> list) {
        this.view = view;
        this.position = position;
        this.list=list;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<MedalBean> getList() {
        return list;
    }

    public void setList(List<MedalBean> list) {
        this.list = list;
    }
}
