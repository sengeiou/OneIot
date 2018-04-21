package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by mai on 17-5-23.
 */

public interface IMessageView extends BaseView{
    void getConversation(ArrayList results);
    // 从网络获取到没有缓存的对话者信息，通知界面刷新
    void refresh();

}
