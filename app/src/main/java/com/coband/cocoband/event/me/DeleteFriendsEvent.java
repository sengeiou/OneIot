package com.coband.cocoband.event.me;

/**
 * Created by mai on 17-5-26.
 */

public class DeleteFriendsEvent {
    public DeleteFriendsEvent(boolean success) {
        this.success = success;
    }

    public boolean success;
    public int position;
}
