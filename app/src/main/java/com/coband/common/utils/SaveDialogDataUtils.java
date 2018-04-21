package com.coband.common.utils;

import com.coband.App;
import com.coband.cocoband.mvp.model.bean.SignUpUserInfo;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.watchassistant.R;
import com.coband.watchassistant.User;

/**
 * Created by tgc on 17-5-22.
 */

public class SaveDialogDataUtils {

    public static SignUpUserInfo saveNick(String nickName){
        User userDB = DBHelper.getInstance().getUser();
        Integer unitDB = userDB.getUnit();
        Integer sex = userDB.getSex();

        SignUpUserInfo info=new SignUpUserInfo();
        info.setWalkTarget(userDB.getWalkTarget());
        info.setSleepTarget(userDB.getSleepTarget());
        info.setWeightTarget(userDB.getWeightTarget());

        info.setUnit(unitDB);
        info.setSex(sex);
        info.setNickName(nickName);
        info.setWeight(userDB.getWeight());
        info.setHeight(userDB.getHeight());

        String format="yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
        String dateByMilliseconds = DateUtils.getDateBySeconds(format, userDB.getBirthday());

        SignUpUserInfo.Birthday birthdayInfo=new SignUpUserInfo.Birthday();
        birthdayInfo.setIso(dateByMilliseconds);
        info.setBirthday(birthdayInfo);

        return info;
    }

    public static SignUpUserInfo saveSex(String sex){
        User userDB = DBHelper.getInstance().getUser();
        Integer unitDB = userDB.getUnit();

        SignUpUserInfo info=new SignUpUserInfo();
        info.setUnit(unitDB);
        if (sex.equals(App.getContext().getString(R.string.male))){
            info.setSex(0);
        }else {
            info.setSex(1);
        }

        info.setWalkTarget(userDB.getWalkTarget());
        info.setSleepTarget(userDB.getSleepTarget());
        info.setWeightTarget(userDB.getWeightTarget());

        info.setNickName(userDB.getNick());
        info.setWeight(userDB.getWeight());
        info.setHeight(userDB.getHeight());

        String format="yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
        String dateByMilliseconds = DateUtils.getDateBySeconds(format, userDB.getBirthday());

        SignUpUserInfo.Birthday birthdayInfo=new SignUpUserInfo.Birthday();
        birthdayInfo.setIso(dateByMilliseconds);
        info.setBirthday(birthdayInfo);

        return info;
    }

    public static SignUpUserInfo saveHeight(double height,int unit){
        User userDB = DBHelper.getInstance().getUser();
        Integer sexDB = userDB.getSex();
        //保存时会抹去weight的值，此处要重新从DB中取出
        Double weight = userDB.getWeight();

        SignUpUserInfo info=new SignUpUserInfo();

        info.setWalkTarget(userDB.getWalkTarget());
        info.setSleepTarget(userDB.getSleepTarget());
        info.setWeightTarget(userDB.getWeightTarget());

        info.setHeight(height);
        info.setUnit(unit);
        info.setSex(sexDB);
        info.setWeight(weight);
        info.setNickName(userDB.getNick());

        String format="yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
        String dateByMilliseconds = DateUtils.getDateBySeconds(format, userDB.getBirthday());

        SignUpUserInfo.Birthday birthdayInfo=new SignUpUserInfo.Birthday();
        birthdayInfo.setIso(dateByMilliseconds);
        info.setBirthday(birthdayInfo);

        return info;
    }

    public static SignUpUserInfo saveWeight(double weight,int unit){
        User userDB = DBHelper.getInstance().getUser();
        Integer sexDB = userDB.getSex();
        //保存时会抹去height的值，此处要重新从DB中取出
        Double height = userDB.getHeight();

        SignUpUserInfo info=new SignUpUserInfo();

        info.setWalkTarget(userDB.getWalkTarget());
        info.setSleepTarget(userDB.getSleepTarget());
        info.setWeightTarget(userDB.getWeightTarget());

        info.setWeight(weight);
        info.setUnit(unit);
        info.setSex(sexDB);
        info.setHeight(height);
        info.setNickName(userDB.getNick());

        String format="yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
        String dateByMilliseconds = DateUtils.getDateBySeconds(format, userDB.getBirthday());

        SignUpUserInfo.Birthday birthdayInfo=new SignUpUserInfo.Birthday();
        birthdayInfo.setIso(dateByMilliseconds);
        info.setBirthday(birthdayInfo);
        return info;
    }

//    public static SignUpSettingUserInfo saveBirthday(long birthdayLong){
//        User userDB = DBHelper.getInstance().getUser();
//        Integer sexDB = userDB.getSex();
//        Integer unitDB = userDB.getUnit();
//
//        SignUpSettingUserInfo settingInfo=new SignUpSettingUserInfo();
//        settingInfo.setBirthdayData(String.valueOf(birthdayLong));
//
//        settingInfo.setSex(sexDB);
//        settingInfo.setUnit(unitDB);
//        settingInfo.setNickName(userDB.getNick());
//        settingInfo.setWeight(userDB.getWeight());
//        settingInfo.setHeight(userDB.getHeight());
//        return settingInfo;
//    }

    public static SignUpUserInfo saveBirthdayWithInfo(long birthdayLong){
        User userDB = DBHelper.getInstance().getUser();
        Integer sexDB = userDB.getSex();
        Integer unitDB = userDB.getUnit();

        //将long类型的时间转换为如下格式的String类型
        String format="yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
        String dateByMilliseconds = DateUtils.getDateBySeconds(format, birthdayLong);
        SignUpUserInfo info=new SignUpUserInfo();

        SignUpUserInfo.Birthday birthdayInfo=new SignUpUserInfo.Birthday();
        birthdayInfo.setIso(dateByMilliseconds);
//        info.setBirthdayStr(String.valueOf(birthdayLong));

        info.setWalkTarget(userDB.getWalkTarget());
        info.setSleepTarget(userDB.getSleepTarget());
        info.setWeightTarget(userDB.getWeightTarget());

        info.setBirthday(birthdayInfo);
        info.setSex(sexDB);
        info.setUnit(unitDB);

        info.setNickName(userDB.getNick());
        info.setWeight(userDB.getWeight());
        info.setHeight(userDB.getHeight());
        return info;
    }
}
