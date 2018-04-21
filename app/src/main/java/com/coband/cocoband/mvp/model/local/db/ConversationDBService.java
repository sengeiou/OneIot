package com.coband.cocoband.mvp.model.local.db;

import com.coband.App;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.Conversation;
import com.coband.watchassistant.ConversationDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mai on 17-5-23.
 */

public class ConversationDBService {

    public static final String TAG = "ConversationDBService";

    public static void insertConversations(ArrayList<Conversation> list) {
        ConversationDao dao = App.getDaoSession().getConversationDao();
        for (int i = 0; i < list.size(); i++) {
            Conversation conversation = list.get(i);
            conversation.getConversationId();
            Conversation temp = dao.queryBuilder().where(ConversationDao.Properties.ConversationId.eq(conversation.getConversationId())).unique();
            if (temp != null) {
                conversation.setLast_message(temp.getLast_message());
                conversation.setCache_message(temp.getCache_message());
            }
        }
        dao.insertOrReplaceInTx(list);
    }

    /**
     * 用于更新缓存的Conversation
     * 不能设置静音
     *
     * @param conversationId
     * @param conversation
     */
    public static void insertConversation(String conversationId, Conversation conversation) {

        ConversationDao dao = App.getDaoSession().getConversationDao();

        QueryBuilder<Conversation> conversationWhere = dao.queryBuilder()
                .where(ConversationDao.Properties.ConversationId.eq(conversationId));
        Conversation temp = conversationWhere.unique();
        if (temp == null) {
            // temp 为null , 说明这个conversation没有的，但为了防止两个成员重复创建conversation,这里过滤下
            if (conversation.getMembersId() != null) {
                QueryBuilder<Conversation> membersWhere = dao.queryBuilder()
                        .where(ConversationDao.Properties.MembersId.eq(conversation.getMembersId()));
                if (membersWhere.list().size() != 0) return;
            }
            if (conversation.getConversationId() != null) {
                dao.insertOrReplace(conversation);
                return;
            }
        }

        if (conversation.getConversationId() != null)
            temp.setConversationId(conversation.getConversationId());
        if (conversation.getMembersId() != null) temp.setMembersId(conversation.getMembersId());
        if (conversation.getCreaterId() != null) temp.setCreaterId(conversation.getCreaterId());
        if (conversation.getLast_message_time() != null)
            temp.setLast_message_time(conversation.getLast_message_time());
        if (conversation.getLast_message() != null)
            temp.setLast_message(conversation.getLast_message());
        if (conversation.getCache_message() != null)
            temp.setCache_message(conversation.getCache_message());
        dao.insertOrReplace(temp);
    }

    public static List<Conversation> queryConversation() {
        ConversationDao dao = App.getDaoSession().getConversationDao();
        QueryBuilder<Conversation> where = dao.queryBuilder()
                .where(ConversationDao.Properties.MembersId.like("%" + DBHelper.getInstance().getUser().getObjectId() + "%"))
                .orderDesc(ConversationDao.Properties.Last_message_time);
        Logger.d(TAG, " Size :: >>" + where.list().size());
        return where.list();
    }

    public static Conversation queryConversationById(String objectId) {
        ConversationDao dao = App.getDaoSession().getConversationDao();
        QueryBuilder<Conversation> where = dao.queryBuilder()
                .where(ConversationDao.Properties.MembersId.like("%" + DBHelper.getInstance().getUser().getObjectId() + "%"))
                .where(ConversationDao.Properties.MembersId.like("%" + objectId + "%"));
        Logger.d(TAG, " Size :: >>" + where.list().size());
        if (where.list().size() > 1) {
            return where.list().get(0);
        } else {
            return where.unique();
        }
    }
}
