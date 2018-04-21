package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-6-17.
 */

public class DNDSettings {
    private boolean mMessageRemindStatus;
    private boolean mScreenRemindStatus;
    private boolean mVibrationRemindStatus;
    private boolean mScheduledStatus;
    private int mScheduledStartTime;
    private int mScheduledEndTime;

    public boolean getMessageRemindStatus() {
        return mMessageRemindStatus;
    }

    public void setMessageRemindStatus(boolean messageRemindStatus) {
        mMessageRemindStatus = messageRemindStatus;
    }

    public boolean getScreenRemindStatus() {
        return mScreenRemindStatus;
    }

    public void setScreenRemindStatus(boolean screenRemindStatus) {
        mScreenRemindStatus = screenRemindStatus;
    }

    public boolean getVibrationRemindStatus() {
        return mVibrationRemindStatus;
    }

    public void setVibrationRemindStatus(boolean vibrationRemindStatus) {
        mVibrationRemindStatus = vibrationRemindStatus;
    }

    public boolean getScheduledStatus() {
        return mScheduledStatus;
    }

    public void setScheduledStatus(boolean scheduledStatus) {
        mScheduledStatus = scheduledStatus;
    }

    public int getScheduledStartTime() {
        return mScheduledStartTime;
    }

    public void setScheduledStartTime(int scheduledStartTime) {
        mScheduledStartTime = scheduledStartTime;
    }

    public int getScheduledEndTime() {
        return mScheduledEndTime;
    }

    public void setScheduledEndTime(int scheduledEndTime) {
        mScheduledEndTime = scheduledEndTime;
    }
}
