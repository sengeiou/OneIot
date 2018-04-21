package com.coband.cocoband.mvp.model.bean.message;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mai on 17-5-15.
 */

public class ConversationBean implements Serializable{
    public boolean unique;
    public Date updatedAt;
    public Lm lm;             //对话中最后一条消息的发送或接收时间
    public String objectId;   //	对话 id（只读），由云端为该对话生成的一个全局唯一的 id。
    public boolean tr;        // 是否为暂态对话
    public Date createdAt;
    public String c;          // 对话创建者的 clientId（只读）
    public List<String> mu;   // 将对话设为静音的参与者，这部分参与者不会收到推送。（仅针对 iOS 以及 Windows Phone 用户有效）
    public List<String> m;    //普通对话的所有参与者（仅针对普通对话，暂态对话和系统对话并不支持持久化的成员列表）
    public String name;       // 对话的名字，可为群组命名。

    public static class Lm{
        public String __type;
        public Date iso;
    }
}
