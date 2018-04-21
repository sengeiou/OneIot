package com.coband.cocoband.mvp.model.bean.message;

import android.support.annotation.StringDef;


import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by mai on 17-5-19.
 */

public class SendMessageBean {

    public String from_peer; // 消息的发件人 client Id
    public String conv_id;   // 发送到对话 id

    @SerializedName("transient")
    public boolean _transient = false; // transient 是关键字，坑爹

    public String message;   // 消息内容
    public boolean no_sync;  // 默认情况下消息会被同步给在线的 from_peer 用户的客户端，设置为 true 禁用此功能。
    public String push_data; // 以消息附件方式设置本条消息的离线推送通知内容。

    @Priority
    public String priority; // 定义消息优先级，可选值为 high、normal、low，分别对应高、中、低三种优先级。该参数大小写不敏感，


    @Retention(SOURCE)
    @StringDef({
            HIGH,
            NORMAL,
            LOW
    })
    public @interface Priority {
    }
    public static final String HIGH = "high";
    public static final String NORMAL = "normal";
    public static final String LOW = "low";

}
