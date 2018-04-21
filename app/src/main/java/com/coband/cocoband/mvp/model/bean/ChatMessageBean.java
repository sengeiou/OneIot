package com.coband.cocoband.mvp.model.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by mai on 17-5-17.
 */

public class ChatMessageBean {
//    AVIMMessageStatusNone(0),
//    AVIMMessageStatusSending(1),
//    AVIMMessageStatusSent(2),
//    AVIMMessageStatusReceipt(3),
//    AVIMMessageStatusFailed(4);


    public String msg_id;
    public String conv_id;
    public int ack_at;
    public String ack_ip;
    public String ack_ua;
    public boolean is_conv;
    public String from;
    public boolean bin;
    public long timestamp;
    public boolean is_room;
    public String from_ip;
    public String to;
    public String data;

    @SendStatus
    public int sendStatus;

    @Retention(SOURCE)
    @IntDef({
            MessageStatusNone,
            MessageStatusSending,
            MessageStatusSent,
            MessageStatusReceipt,
            MessageStatusFailed
    })
    public @interface SendStatus {
    }
    public final static int MessageStatusNone = 0;
    public final static int MessageStatusSending = 1;
    public final static int MessageStatusSent = 2;
    public final static int MessageStatusReceipt = 3;
    public final static int MessageStatusFailed = 4;
}
