package com.coband.cocoband.event.me;

import android.view.View;

/**
 * Created by mai on 17-4-17.
 */

public class FriendsRequestItemClickEvent {
    public View view;
    public int position;
    public String objId;

    public FriendsRequestItemClickEvent(View view, int position, String objId) {
        this(view, position);
        this.objId = objId;
    }

    public FriendsRequestItemClickEvent(View view, int position) {
        this.view = view;
        this.position = position;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
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
}
