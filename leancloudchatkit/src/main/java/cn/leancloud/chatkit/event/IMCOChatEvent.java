package cn.leancloud.chatkit.event;

/**
 * Created by mai on 17-6-6.
 */

public class IMCOChatEvent {
    public IMCOChatEvent(String conversationId) {
        this.conversationId = conversationId;
    }

    public String conversationId;

}
