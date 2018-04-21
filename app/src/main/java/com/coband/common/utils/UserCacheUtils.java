package com.coband.common.utils;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.coband.cocoband.mvp.model.bean.ChatUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wli on 15/9/30.
 * TODO
 * 1、本地存储
 * 2、避免内存、外存占用过多
 */
public class UserCacheUtils {

    private static Map<String, ChatUser> userMap;

    static {
        userMap = new HashMap<String, ChatUser>();
    }

    public static ChatUser getCachedUser(String objectId) {
        return userMap.get(objectId);
    }

    public static boolean hasCachedUser(String objectId) {
        return userMap.containsKey(objectId);
    }

    public static void cacheUser(ChatUser user) {
        if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
            userMap.put(user.getObjectId(), user);
        }
    }

    public static void cacheUsers(List<ChatUser> users) {
        if (null != users) {
            for (ChatUser user : users) {
                cacheUser(user);
            }
        }
    }

    public static void fetchUsers(List<String> ids) {
        fetchUsers(ids, null);
    }

    public static void fetchUsers(final List<String> ids, final CacheUserCallback cacheUserCallback) {
        Set<String> uncachedIds = new HashSet<String>();
        for (String id : ids) {
            if (!userMap.containsKey(id)) {
                uncachedIds.add(id);
            }
        }

        if (uncachedIds.isEmpty()) {
            if (null != cacheUserCallback) {
                cacheUserCallback.done(getUsersFromCache(ids), null);
                return;
            }
        }

        AVQuery<ChatUser> q = ChatUser.getQuery(ChatUser.class);
        q.whereContainedIn(C.OBJECT_ID, uncachedIds);
        q.setLimit(1000);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(new FindCallback<ChatUser>() {
            @Override
            public void done(List<ChatUser> list, AVException e) {
                if (null == e) {
                    for (ChatUser user : list) {
                        userMap.put(user.getObjectId(), user);
                    }
                }
                if (null != cacheUserCallback) {
                    cacheUserCallback.done(getUsersFromCache(ids), e);
                }

            }
        });
    }

    public static List<ChatUser> getUsersFromCache(List<String> ids) {
        List<ChatUser> userList = new ArrayList<ChatUser>();
        for (String id : ids) {
            if (userMap.containsKey(id)) {
                userList.add(userMap.get(id));
            }
        }
        return userList;
    }

    public static abstract class CacheUserCallback {
        public abstract void done(List<ChatUser> userList, Exception e);
    }
}
