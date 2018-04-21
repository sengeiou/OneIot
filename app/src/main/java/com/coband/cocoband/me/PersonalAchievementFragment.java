package com.coband.cocoband.me;

import android.os.Bundle;

import com.coband.cocoband.me.viewholder.PersonalAchievementViewHolder;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;
import com.coband.watchassistant.User;
import com.imcorecyclerviewlib.MultiItemAdapter;

import java.util.ArrayList;

/**
 * Created by mai on 17-4-27.
 */

public class PersonalAchievementFragment extends BaseListFragment {
    public static final String OBJECT_ID = "object_id";
    public static final String TAG = "PersonalAchievementFragment";

    @Override
    protected MultiItemAdapter setMultiItemAdapter() {
        return new MultiItemAdapter(PersonalAchievementViewHolder.class);
    }

    @Override
    protected int setTitleRes() {
        return R.string.personal_achievement;
    }

    @Override
    protected void init() {
//        List<String> list = Arrays.asList(getResources().getStringArray(R.array.personal_achievement));
        ArrayList<String> strings = new ArrayList<>();
        Bundle arguments = getArguments();

        if (arguments != null && arguments.containsKey(OBJECT_ID)) {
            String objectId = arguments.getString(OBJECT_ID);
            User user;
            if (objectId == null) {
                user = DBHelper.getInstance().getUser();
            } else {
                user = DBHelper.getInstance().getUserByObjectId(objectId);
            }
            if (user == null) Logger.d(TAG, "user == null");
            strings.add(getString(R.string.join_imco) + "," + (user.getCreatedAt().isEmpty() ? DateUtils.getDateBySeconds("yyyy/MM/dd", System.currentTimeMillis()) : user.getCreatedAt().substring(0, 10)));
            int totalExerciceDays = user.getTotalExerciceDays();
            if (user.getTotalExerciceDays() == 0) {
                totalExerciceDays = (int) ((DateUtils.getToday() -
                        DateUtils.getZeroByMilliSeconds(DateUtils.
                                dateStr2MillionSeconds(user.getCreatedAt()))) / (24 * 60 * 60 * 1000));
            }
            if (totalExerciceDays == 0) totalExerciceDays = 1;
            strings.add(getString(R.string.band_day_ave_step) + "," + user.getTotalWalkCount() / totalExerciceDays + " " + getString(R.string.step_unit));
            strings.add(getString(R.string.best_step_single_day) + "," + user.getDayHighestSteps() + " " + getString(R.string.step_unit));
            strings.add(getString(R.string.total_step) + "," + user.getTotalWalkCount() + " " + getString(R.string.step_unit));
        }
        mAdapter.addDataList(strings);
        mAdapter.notifyDataSetChanged();
    }
}
