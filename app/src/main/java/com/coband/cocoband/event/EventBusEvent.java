package com.coband.cocoband.event;

import com.coband.cocoband.mvp.model.bean.User;

/**
 * Created by ivan on 17-4-12.
 */

public class EventBusEvent<T> {
    public static final int BLE_DEVICE = 0;
    public static final int CONNECTING = 1;
    public static final int LOG_IN_SUCCESS = 2;
    public static final int LOG_IN_FAILED = 3;
    public static final int SPORT_SUCCESS = 4;
    public static final int SLEEP_SUCCESS = 5;
    public static final int HREATRATE_SUCCESS = 6;
    public static final int ERROR = 7;
    public static final int BLOOD_PRESSURFE_SUCCESS = 8;
    public static final int SPORT_ERROR = 9;
    public static final int SLEEP_ERROR = 10;
    public static final int HREATRATE_ERROR = 11;
    public static final int BLOOD_PRESSURFE_ERROR = 12;

    private int mEvent;
    private T mMessage;
    private User mUser;

    public EventBusEvent(int event) {
        this.mEvent = event;
    }

    public EventBusEvent(int event, T message) {
        this.mEvent = event;
        this.mMessage = message;
    }

    public EventBusEvent(int event, User user) {
        this.mEvent = event;
        this.mUser = user;
    }

    public T getMessage() {
        return mMessage;
    }

    public void setMessage(T message) {
        this.mMessage = message;
    }

    public int getEvent() {
        return mEvent;
    }

    public void setEvent(int event) {
        this.mEvent = event;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }
}
