package com.coband.cocoband.mvp.model.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
/**
 * Created by mqh on 11/22/16.
 *
 * todo 这是chatkit 的后台类名，我们的不一样，记得改
 */
@AVClassName("AddRequest")
public class AddRequest extends AVObject {
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_DONE = 1;

    public static final String FROM_USER = "fromUser";
    public static final String TO_USER = "toUser";
    public static final String STATUS = "status";

    /**
     * 标记接收方是否已读该消息
     */
    public static final String IS_READ = "isRead";

    public AddRequest() {
    }

    public ChatUser getFromUser() {
        return getAVUser(FROM_USER, ChatUser.class);
    }

    public void setFromUser(ChatUser fromUser) {
        put(FROM_USER, fromUser);
    }

    public ChatUser getToUser() {
        return getAVUser(TO_USER, ChatUser.class);
    }

    public void setToUser(ChatUser toUser) {
        put(TO_USER, toUser);
    }

    public int getStatus() {
        return getInt(STATUS);
    }

    public void setStatus(int status) {
        put(STATUS, status);
    }

    public boolean isRead() {
        return getBoolean(IS_READ);
    }

    public void setIsRead(boolean isRead) {
        put(IS_READ, isRead);
    }
}
