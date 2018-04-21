package com.coband.cocoband.event.me;

import android.view.View;

/**
 * Created by mai on 17-4-17.
 */

public class MeMenuClickEvent {
    public View view;
    public int position;
    public String title;

    public MeMenuClickEvent(View view, int position, String title) {
        this(view, position);
        this.title = title;
    }

    public MeMenuClickEvent(View view, int position) {
        this.view = view;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
