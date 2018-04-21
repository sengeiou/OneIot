package com.coband.cocoband.mvp.model.bean;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by mqh on 11/22/16.
 */

public class ChatUser extends AVUser {

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String AVATAR = "avatar";
    public static final String LOCATION = "location";
    public static final String INSTALLATION = "installation";

    public final static int NORMAL = 0;
    public final static int REQUEST_SENT = 1;
    public final static int ISFRIEND = 2;
    public int status;

    public static String getCurrentUserId () {
        ChatUser currentUser = getCurrentUser(ChatUser.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }


    public String getAvatarUrl() {
//        AVFile avatar = getAVFile(AVATAR);
//        if (avatar != null) {
//            return avatar.getUrl();
//        } else {
//            return null;
//        }


        return new UserWrapper(this).getAvatar();
    }


    public void saveAvatar(String path, final SaveCallback saveCallback) {
        final AVFile file;
        try {
            file = AVFile.withAbsoluteLocalPath(getUsername(), path);
            put(AVATAR, file);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        saveInBackground(saveCallback);
                    } else {
                        if (null != saveCallback) {
                            saveCallback.done(e);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChatUser getCurrentUser() {
        return getCurrentUser(ChatUser.class);
    }

    public void updateUserInfo() {
        AVInstallation installation = AVInstallation.getCurrentInstallation();
        if (installation != null) {
            put(INSTALLATION, installation);
            saveInBackground();
        }
    }

    public AVGeoPoint getGeoPoint() {
        return getAVGeoPoint(LOCATION);
    }

    public void setGeoPoint(AVGeoPoint point) {
        put(LOCATION, point);
    }

    public static void signUpByNameAndPwd(String name, String password, SignUpCallback callback) {
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground(callback);
    }

    public void removeFriend(String friendId, final SaveCallback saveCallback) {
        unfollowInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<ChatUser>
            findCallback) {
        AVQuery<ChatUser> q = null;
        try {
            q = followeeQuery(ChatUser.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.findInBackground(findCallback);
    }

    public void findFolloweeWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<ChatUser>
            findCallback) {
        AVQuery<ChatUser> q = null;
        try {
            q = followeeQuery(ChatUser.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.findInBackground(findCallback);
    }


    public void findFollowerWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<ChatUser>
            findCallback) {
        AVQuery<ChatUser> q = null;
        try {
            q = followerQuery(ChatUser.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.findInBackground(findCallback);
    }
}
