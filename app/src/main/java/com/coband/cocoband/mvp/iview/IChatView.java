package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.bean.ChatMessageBean;
import com.coband.cocoband.mvp.model.bean.message.ConversationCreateResultBean;
import com.coband.watchassistant.Conversation;

import java.util.List;

/**
 * Created by mai on 17-5-24.
 */

public interface IChatView extends BaseView{
    void getConversation(Conversation conversationBean);
    void netWorkException();
    void createConversation(ConversationCreateResultBean bean);
    void getMessageList(List<ChatMessageBean> messages);
}
