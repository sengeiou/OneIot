package com.coband.cocoband.mvp.model.bean.todayrank;

import com.coband.cocoband.mvp.model.bean.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mai on 17-5-8.
 */

public class TodayRankBean implements Serializable, Cloneable{
    public long timeStamp;
    public int step;
    public User user;
    public Date createdAt;
    public Date updatedAt;
    public List<String> likes;
    public String objectId;

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Note : this is shadow copy
        return super.clone();
    }
}
