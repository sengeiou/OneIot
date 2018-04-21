package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    private static final String OUTPUT_PATH = "./app/src/main/src-gen";

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(4, "com.coband.watchassistant");

        Entity todayData = schema.addEntity("TodayData");
        todayData.addIdProperty();
        todayData.addLongProperty("date").notNull();
        todayData.addFloatProperty("cal").notNull();
        todayData.addFloatProperty("dis").notNull();
        todayData.addLongProperty("step").notNull();
        todayData.addIntProperty("node").notNull();
        todayData.addLongProperty("tt").notNull();

        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("username").unique();
        user.addDoubleProperty("height").notNull();
        user.addDoubleProperty("weight").notNull();

        user.addIntProperty("gendar").notNull();
        user.addLongProperty("birthday").notNull();
        user.addStringProperty("nick");
        user.addIntProperty("walkTarget").notNull();
        user.addDoubleProperty("weightTarget").notNull();
//        user.addIntProperty("disTarget"); change to float
        user.addIntProperty("caloryTarget").notNull();
        user.addIntProperty("timeTarget").notNull();
        user.addIntProperty("sleepTarget").notNull();
        user.addStringProperty("appVersion");
        user.addStringProperty("email").unique();
        user.addStringProperty("sessionToken");
        user.addStringProperty("updatedAt");
        user.addIntProperty("sportTimeTarget").notNull();
        user.addIntProperty("sex").notNull();
        user.addStringProperty("firmwareSystemVersion");
        user.addIntProperty("unit").notNull();
        user.addStringProperty("userDeviceSystemVersion");
//        user.addStringProperty("objectId").primaryKey();
        user.addStringProperty("createdAt");
        user.addDoubleProperty("totalDistance").notNull();
        user.addIntProperty("copperNumber").notNull();
        user.addIntProperty("silverNumber").notNull();
        user.addBooleanProperty("emailVerified").notNull();
        user.addIntProperty("upvotes").notNull();
        user.addIntProperty("dayHighestSteps").notNull();
        user.addStringProperty("surfaceImg");
        user.addIntProperty("totalExerciceDays").notNull();
        user.addStringProperty("bestSportInfoWithDay");
        user.addStringProperty("objectId");//这是一个object ID,根据这个去查询表
        user.addStringProperty("avatar");
        user.addIntProperty("goldNumber").notNull();
        user.addDoubleProperty("totalCalories").notNull();
        user.addIntProperty("ranking").notNull();
        user.addFloatProperty("distanceTarget").notNull();
        user.addLongProperty("beginSportTimestamp").notNull();
        user.addStringProperty("userDeviceType");
        user.addIntProperty("totalWalkCount").notNull();
        user.addBooleanProperty("mobilePhoneVerified").notNull();
        user.addStringProperty("archivementList");
        user.addStringProperty("deviceTypes");

        /*** below is user setting ***/
        // alarm clock
        user.addIntProperty("whichClock").notNull();
        // not disturb
        user.addBooleanProperty("isMessage").notNull();
        user.addBooleanProperty("isMotorOn").notNull();
        user.addBooleanProperty("isScreenOn").notNull();
        user.addBooleanProperty("disturbTimeSwitch").notNull();
        user.addIntProperty("fromTimeHour").notNull();
        user.addIntProperty("fromTimeMinute").notNull();
        user.addIntProperty("toTimeHour").notNull();
        user.addIntProperty("toTimeMinute").notNull();
        // heart monitor
        user.addBooleanProperty("heartRate").notNull();
        user.addIntProperty("highestHeartRate").notNull();
        user.addBooleanProperty("periodTest").notNull();
        user.addIntProperty("period").notNull();
        // in call remind
        user.addBooleanProperty("inCallRemind").notNull();
        user.addIntProperty("vibrationsTimes").notNull();
        // message remind
        user.addStringProperty("messageRemind");
        user.addBooleanProperty("isOpenMessageRemind").notNull();
        // sedentary
        user.addIntProperty("sedentaryTime").notNull();
        user.addBooleanProperty("isOpenSedentaryRemind").notNull();
        // weather
        user.addBooleanProperty("isOpenWeatherPush").notNull();
        // 24 hour format
        user.addBooleanProperty("is24Format").notNull();
        // light screen time
        user.addIntProperty("lightScreenTime").notNull();
        // smart Anti_lost
        user.addBooleanProperty("smartAnti_lost").notNull();
        // band name
        user.addStringProperty("bandName");


        Entity follower = schema.addEntity("Follower");
        follower.addStringProperty("surfaceImg");
        follower.addStringProperty("updatedAt");
        follower.addStringProperty("avatar");
        follower.addStringProperty("nickName");
        follower.addStringProperty("userName");
        follower.addIntProperty("dayHighestSteps").notNull();
        follower.addStringProperty("archivementList");
        follower.addStringProperty("objectId").primaryKey();
        // addToMany only can use long, fixme 每打开一次APP,userId都会改变一次，原因未知
        Property userId = follower.addLongProperty("userId").getProperty();
        user.addToMany(follower, userId, "followers");
        follower.addToOne(user, userId);

        Entity followee = schema.addEntity("Followee");
        followee.addStringProperty("surfaceImg");
        followee.addStringProperty("updatedAt");
        followee.addStringProperty("avatar");
        followee.addStringProperty("nickName");
        followee.addStringProperty("userName");
        followee.addIntProperty("dayHighestSteps").notNull();
        followee.addStringProperty("archivementList");
        followee.addStringProperty("objectId").primaryKey();
        Property userId2 = followee.addLongProperty("userId").getProperty();
        user.addToMany(followee, userId2, "followees");
        followee.addToOne(user, userId2);


        Entity pushApp = schema.addEntity("PushApp");
        pushApp.addIdProperty();
        pushApp.addStringProperty("packageName");
        pushApp.addStringProperty("appName");

        Entity historyData = schema.addEntity("HistoryData");
        historyData.addIdProperty().primaryKey();
        historyData.addLongProperty("date").notNull(); // seconds
        historyData.addLongProperty("step").notNull();
        historyData.addFloatProperty("distance").notNull();
        historyData.addFloatProperty("calories").notNull();
        historyData.addLongProperty("time").notNull();
        historyData.addBooleanProperty("upload").notNull();
        historyData.addLongProperty("target").notNull();
        historyData.addBooleanProperty("targetFinish").notNull();
        historyData.addStringProperty("uid").notNull();

        Entity card = schema.addEntity("Card");
        card.addIdProperty();
        card.addIntProperty("type").notNull();
        card.addLongProperty("time").notNull();
        card.addStringProperty("message");
        card.addIntProperty("uncompleteStep").notNull();
        card.addIntProperty("completedStep").notNull();
        card.addIntProperty("stepCompletion").notNull();
        card.addIntProperty("sleepTime").notNull(); // minute.
        card.addIntProperty("heartRate").notNull();

        Entity sleep = schema.addEntity("Sleep");
        sleep.addIdProperty();
        sleep.addLongProperty("date").unique().notNull();
        sleep.addIntProperty("deep").notNull(); // deep sleep time. minute
        sleep.addIntProperty("light").notNull(); // light sleep time. minute
        sleep.addIntProperty("wakeCount").notNull(); // wake count.
        sleep.addIntProperty("totalTime").notNull(); // sleep total time. minute
        sleep.addBooleanProperty("upload").notNull(); // upload flag.
        sleep.addStringProperty("uid"); // user object id.
        sleep.addIntProperty("beginTime");
        sleep.addIntProperty("mode");


        Entity heartRate = schema.addEntity("HeartRate");
        heartRate.addIdProperty();
        heartRate.addLongProperty("date").unique().notNull();
        heartRate.addStringProperty("heartRate");
        heartRate.addBooleanProperty("upload").notNull();
        heartRate.addStringProperty("uid").notNull(); // user object id.

        Entity weight = schema.addEntity("DBWeight");
        weight.addIdProperty();
        weight.addFloatProperty("weight").notNull();
        weight.addFloatProperty("bodyMI").notNull();
        weight.addBooleanProperty("upload").notNull();
        weight.addLongProperty("date").unique().notNull();
        weight.addStringProperty("uid").notNull();

        Entity conversation = schema.addEntity("Conversation");
        conversation.addStringProperty("conversationId").primaryKey();
        conversation.addStringProperty("membersId"); //这个对话的所有参与者的ID
        conversation.addStringProperty("createrId"); //这个对话的创建者的ID
        conversation.addDateProperty("last_message_time"); // 对话中最后一条消息的发送或接收时间
        conversation.addStringProperty("last_message"); // 对话中最后一条谈话内容
        conversation.addBooleanProperty("silent").notNull();// 该对话是否为静音
        conversation.addStringProperty("cache_message"); // 最近20条对话的缓存

        Entity stepNodeDetail = schema.addEntity("StepNodeDetail");
        stepNodeDetail.addIdProperty();
        stepNodeDetail.addLongProperty("date").notNull();
        stepNodeDetail.addIntProperty("offset").notNull();
        stepNodeDetail.addLongProperty("step").notNull();
        stepNodeDetail.addIntProperty("calories").notNull();
        stepNodeDetail.addIntProperty("distance").notNull();
        stepNodeDetail.addStringProperty("uid").notNull();

        Entity sleepNodeDetail = schema.addEntity("SleepNodeDetail");
        sleepNodeDetail.addIdProperty();
        sleepNodeDetail.addLongProperty("date").notNull();
        sleepNodeDetail.addIntProperty("begin").notNull();
        sleepNodeDetail.addIntProperty("mode").notNull();
        sleepNodeDetail.addStringProperty("uid").notNull();

        Entity bloodPressure = schema.addEntity("BloodPressure");
        bloodPressure.addIdProperty();
        bloodPressure.addLongProperty("date").notNull();
        bloodPressure.addStringProperty("bloodPressures");
        bloodPressure.addBooleanProperty("upload").notNull();
        bloodPressure.addStringProperty("uid").notNull();


        /***************** CoBand ************************/
        Entity account = schema.addEntity("Account");
        account.addIdProperty();
        account.addStringProperty("account");
        account.addStringProperty("password");
        account.addStringProperty("token");
        account.addStringProperty("unitSystem");
        account.addStringProperty("birthday");
        account.addStringProperty("gender");
        account.addStringProperty("nickname");
        account.addIntProperty("height");
        account.addDoubleProperty("weight");
        account.addIntProperty("stepTarget");
        account.addDoubleProperty("weightTarget");
        account.addIntProperty("sleepTarget");
        account.addStringProperty("uid");
        account.addStringProperty("did");
        account.addBooleanProperty("accountVerified");
        account.addStringProperty("avatar");
        account.addStringProperty("avatarMD5");
        account.addStringProperty("background");
        account.addStringProperty("backgroundMD5");
        account.addStringProperty("bloodType");
        account.addStringProperty("city");
        account.addStringProperty("country");
        account.addDoubleProperty("latitude");
        account.addDoubleProperty("longitude");
        account.addStringProperty("province");
        account.addStringProperty("language");
        account.addStringProperty("osType");
        account.addStringProperty("osVersion");
        account.addStringProperty("phoneModel");
        account.addIntProperty("timezone");
        account.addStringProperty("achievements");
        account.addLongProperty("maxDaySteps");
        account.addLongProperty("startExerciseTime");
        account.addDoubleProperty("totalCalories");
        account.addDoubleProperty("totalDistance");
        account.addIntProperty("totalExerciseDays");
        account.addLongProperty("totalSteps");

        new DaoGenerator().generateAll(schema, OUTPUT_PATH);
    }

}
