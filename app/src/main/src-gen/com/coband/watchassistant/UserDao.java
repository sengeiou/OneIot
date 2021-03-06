package com.coband.watchassistant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.coband.watchassistant.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Username = new Property(1, String.class, "username", false, "USERNAME");
        public final static Property Height = new Property(2, double.class, "height", false, "HEIGHT");
        public final static Property Weight = new Property(3, double.class, "weight", false, "WEIGHT");
        public final static Property Gendar = new Property(4, int.class, "gendar", false, "GENDAR");
        public final static Property Birthday = new Property(5, long.class, "birthday", false, "BIRTHDAY");
        public final static Property Nick = new Property(6, String.class, "nick", false, "NICK");
        public final static Property WalkTarget = new Property(7, int.class, "walkTarget", false, "WALK_TARGET");
        public final static Property WeightTarget = new Property(8, double.class, "weightTarget", false, "WEIGHT_TARGET");
        public final static Property CaloryTarget = new Property(9, int.class, "caloryTarget", false, "CALORY_TARGET");
        public final static Property TimeTarget = new Property(10, int.class, "timeTarget", false, "TIME_TARGET");
        public final static Property SleepTarget = new Property(11, int.class, "sleepTarget", false, "SLEEP_TARGET");
        public final static Property AppVersion = new Property(12, String.class, "appVersion", false, "APP_VERSION");
        public final static Property Email = new Property(13, String.class, "email", false, "EMAIL");
        public final static Property SessionToken = new Property(14, String.class, "sessionToken", false, "SESSION_TOKEN");
        public final static Property UpdatedAt = new Property(15, String.class, "updatedAt", false, "UPDATED_AT");
        public final static Property SportTimeTarget = new Property(16, int.class, "sportTimeTarget", false, "SPORT_TIME_TARGET");
        public final static Property Sex = new Property(17, int.class, "sex", false, "SEX");
        public final static Property FirmwareSystemVersion = new Property(18, String.class, "firmwareSystemVersion", false, "FIRMWARE_SYSTEM_VERSION");
        public final static Property Unit = new Property(19, int.class, "unit", false, "UNIT");
        public final static Property UserDeviceSystemVersion = new Property(20, String.class, "userDeviceSystemVersion", false, "USER_DEVICE_SYSTEM_VERSION");
        public final static Property CreatedAt = new Property(21, String.class, "createdAt", false, "CREATED_AT");
        public final static Property TotalDistance = new Property(22, double.class, "totalDistance", false, "TOTAL_DISTANCE");
        public final static Property CopperNumber = new Property(23, int.class, "copperNumber", false, "COPPER_NUMBER");
        public final static Property SilverNumber = new Property(24, int.class, "silverNumber", false, "SILVER_NUMBER");
        public final static Property EmailVerified = new Property(25, boolean.class, "emailVerified", false, "EMAIL_VERIFIED");
        public final static Property Upvotes = new Property(26, int.class, "upvotes", false, "UPVOTES");
        public final static Property DayHighestSteps = new Property(27, int.class, "dayHighestSteps", false, "DAY_HIGHEST_STEPS");
        public final static Property SurfaceImg = new Property(28, String.class, "surfaceImg", false, "SURFACE_IMG");
        public final static Property TotalExerciceDays = new Property(29, int.class, "totalExerciceDays", false, "TOTAL_EXERCICE_DAYS");
        public final static Property BestSportInfoWithDay = new Property(30, String.class, "bestSportInfoWithDay", false, "BEST_SPORT_INFO_WITH_DAY");
        public final static Property ObjectId = new Property(31, String.class, "objectId", false, "OBJECT_ID");
        public final static Property Avatar = new Property(32, String.class, "avatar", false, "AVATAR");
        public final static Property GoldNumber = new Property(33, int.class, "goldNumber", false, "GOLD_NUMBER");
        public final static Property TotalCalories = new Property(34, double.class, "totalCalories", false, "TOTAL_CALORIES");
        public final static Property Ranking = new Property(35, int.class, "ranking", false, "RANKING");
        public final static Property DistanceTarget = new Property(36, float.class, "distanceTarget", false, "DISTANCE_TARGET");
        public final static Property BeginSportTimestamp = new Property(37, long.class, "beginSportTimestamp", false, "BEGIN_SPORT_TIMESTAMP");
        public final static Property UserDeviceType = new Property(38, String.class, "userDeviceType", false, "USER_DEVICE_TYPE");
        public final static Property TotalWalkCount = new Property(39, int.class, "totalWalkCount", false, "TOTAL_WALK_COUNT");
        public final static Property MobilePhoneVerified = new Property(40, boolean.class, "mobilePhoneVerified", false, "MOBILE_PHONE_VERIFIED");
        public final static Property ArchivementList = new Property(41, String.class, "archivementList", false, "ARCHIVEMENT_LIST");
        public final static Property DeviceTypes = new Property(42, String.class, "deviceTypes", false, "DEVICE_TYPES");
        public final static Property WhichClock = new Property(43, int.class, "whichClock", false, "WHICH_CLOCK");
        public final static Property IsMessage = new Property(44, boolean.class, "isMessage", false, "IS_MESSAGE");
        public final static Property IsMotorOn = new Property(45, boolean.class, "isMotorOn", false, "IS_MOTOR_ON");
        public final static Property IsScreenOn = new Property(46, boolean.class, "isScreenOn", false, "IS_SCREEN_ON");
        public final static Property DisturbTimeSwitch = new Property(47, boolean.class, "disturbTimeSwitch", false, "DISTURB_TIME_SWITCH");
        public final static Property FromTimeHour = new Property(48, int.class, "fromTimeHour", false, "FROM_TIME_HOUR");
        public final static Property FromTimeMinute = new Property(49, int.class, "fromTimeMinute", false, "FROM_TIME_MINUTE");
        public final static Property ToTimeHour = new Property(50, int.class, "toTimeHour", false, "TO_TIME_HOUR");
        public final static Property ToTimeMinute = new Property(51, int.class, "toTimeMinute", false, "TO_TIME_MINUTE");
        public final static Property HeartRate = new Property(52, boolean.class, "heartRate", false, "HEART_RATE");
        public final static Property HighestHeartRate = new Property(53, int.class, "highestHeartRate", false, "HIGHEST_HEART_RATE");
        public final static Property PeriodTest = new Property(54, boolean.class, "periodTest", false, "PERIOD_TEST");
        public final static Property Period = new Property(55, int.class, "period", false, "PERIOD");
        public final static Property InCallRemind = new Property(56, boolean.class, "inCallRemind", false, "IN_CALL_REMIND");
        public final static Property VibrationsTimes = new Property(57, int.class, "vibrationsTimes", false, "VIBRATIONS_TIMES");
        public final static Property MessageRemind = new Property(58, String.class, "messageRemind", false, "MESSAGE_REMIND");
        public final static Property IsOpenMessageRemind = new Property(59, boolean.class, "isOpenMessageRemind", false, "IS_OPEN_MESSAGE_REMIND");
        public final static Property SedentaryTime = new Property(60, int.class, "sedentaryTime", false, "SEDENTARY_TIME");
        public final static Property IsOpenSedentaryRemind = new Property(61, boolean.class, "isOpenSedentaryRemind", false, "IS_OPEN_SEDENTARY_REMIND");
        public final static Property IsOpenWeatherPush = new Property(62, boolean.class, "isOpenWeatherPush", false, "IS_OPEN_WEATHER_PUSH");
        public final static Property Is24Format = new Property(63, boolean.class, "is24Format", false, "IS24_FORMAT");
        public final static Property LightScreenTime = new Property(64, int.class, "lightScreenTime", false, "LIGHT_SCREEN_TIME");
        public final static Property SmartAnti_lost = new Property(65, boolean.class, "smartAnti_lost", false, "SMART_ANTI_LOST");
        public final static Property BandName = new Property(66, String.class, "bandName", false, "BAND_NAME");
    };

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USERNAME\" TEXT UNIQUE ," + // 1: username
                "\"HEIGHT\" REAL NOT NULL ," + // 2: height
                "\"WEIGHT\" REAL NOT NULL ," + // 3: weight
                "\"GENDAR\" INTEGER NOT NULL ," + // 4: gendar
                "\"BIRTHDAY\" INTEGER NOT NULL ," + // 5: birthday
                "\"NICK\" TEXT," + // 6: nick
                "\"WALK_TARGET\" INTEGER NOT NULL ," + // 7: walkTarget
                "\"WEIGHT_TARGET\" REAL NOT NULL ," + // 8: weightTarget
                "\"CALORY_TARGET\" INTEGER NOT NULL ," + // 9: caloryTarget
                "\"TIME_TARGET\" INTEGER NOT NULL ," + // 10: timeTarget
                "\"SLEEP_TARGET\" INTEGER NOT NULL ," + // 11: sleepTarget
                "\"APP_VERSION\" TEXT," + // 12: appVersion
                "\"EMAIL\" TEXT UNIQUE ," + // 13: email
                "\"SESSION_TOKEN\" TEXT," + // 14: sessionToken
                "\"UPDATED_AT\" TEXT," + // 15: updatedAt
                "\"SPORT_TIME_TARGET\" INTEGER NOT NULL ," + // 16: sportTimeTarget
                "\"SEX\" INTEGER NOT NULL ," + // 17: sex
                "\"FIRMWARE_SYSTEM_VERSION\" TEXT," + // 18: firmwareSystemVersion
                "\"UNIT\" INTEGER NOT NULL ," + // 19: unit
                "\"USER_DEVICE_SYSTEM_VERSION\" TEXT," + // 20: userDeviceSystemVersion
                "\"CREATED_AT\" TEXT," + // 21: createdAt
                "\"TOTAL_DISTANCE\" REAL NOT NULL ," + // 22: totalDistance
                "\"COPPER_NUMBER\" INTEGER NOT NULL ," + // 23: copperNumber
                "\"SILVER_NUMBER\" INTEGER NOT NULL ," + // 24: silverNumber
                "\"EMAIL_VERIFIED\" INTEGER NOT NULL ," + // 25: emailVerified
                "\"UPVOTES\" INTEGER NOT NULL ," + // 26: upvotes
                "\"DAY_HIGHEST_STEPS\" INTEGER NOT NULL ," + // 27: dayHighestSteps
                "\"SURFACE_IMG\" TEXT," + // 28: surfaceImg
                "\"TOTAL_EXERCICE_DAYS\" INTEGER NOT NULL ," + // 29: totalExerciceDays
                "\"BEST_SPORT_INFO_WITH_DAY\" TEXT," + // 30: bestSportInfoWithDay
                "\"OBJECT_ID\" TEXT," + // 31: objectId
                "\"AVATAR\" TEXT," + // 32: avatar
                "\"GOLD_NUMBER\" INTEGER NOT NULL ," + // 33: goldNumber
                "\"TOTAL_CALORIES\" REAL NOT NULL ," + // 34: totalCalories
                "\"RANKING\" INTEGER NOT NULL ," + // 35: ranking
                "\"DISTANCE_TARGET\" REAL NOT NULL ," + // 36: distanceTarget
                "\"BEGIN_SPORT_TIMESTAMP\" INTEGER NOT NULL ," + // 37: beginSportTimestamp
                "\"USER_DEVICE_TYPE\" TEXT," + // 38: userDeviceType
                "\"TOTAL_WALK_COUNT\" INTEGER NOT NULL ," + // 39: totalWalkCount
                "\"MOBILE_PHONE_VERIFIED\" INTEGER NOT NULL ," + // 40: mobilePhoneVerified
                "\"ARCHIVEMENT_LIST\" TEXT," + // 41: archivementList
                "\"DEVICE_TYPES\" TEXT," + // 42: deviceTypes
                "\"WHICH_CLOCK\" INTEGER NOT NULL ," + // 43: whichClock
                "\"IS_MESSAGE\" INTEGER NOT NULL ," + // 44: isMessage
                "\"IS_MOTOR_ON\" INTEGER NOT NULL ," + // 45: isMotorOn
                "\"IS_SCREEN_ON\" INTEGER NOT NULL ," + // 46: isScreenOn
                "\"DISTURB_TIME_SWITCH\" INTEGER NOT NULL ," + // 47: disturbTimeSwitch
                "\"FROM_TIME_HOUR\" INTEGER NOT NULL ," + // 48: fromTimeHour
                "\"FROM_TIME_MINUTE\" INTEGER NOT NULL ," + // 49: fromTimeMinute
                "\"TO_TIME_HOUR\" INTEGER NOT NULL ," + // 50: toTimeHour
                "\"TO_TIME_MINUTE\" INTEGER NOT NULL ," + // 51: toTimeMinute
                "\"HEART_RATE\" INTEGER NOT NULL ," + // 52: heartRate
                "\"HIGHEST_HEART_RATE\" INTEGER NOT NULL ," + // 53: highestHeartRate
                "\"PERIOD_TEST\" INTEGER NOT NULL ," + // 54: periodTest
                "\"PERIOD\" INTEGER NOT NULL ," + // 55: period
                "\"IN_CALL_REMIND\" INTEGER NOT NULL ," + // 56: inCallRemind
                "\"VIBRATIONS_TIMES\" INTEGER NOT NULL ," + // 57: vibrationsTimes
                "\"MESSAGE_REMIND\" TEXT," + // 58: messageRemind
                "\"IS_OPEN_MESSAGE_REMIND\" INTEGER NOT NULL ," + // 59: isOpenMessageRemind
                "\"SEDENTARY_TIME\" INTEGER NOT NULL ," + // 60: sedentaryTime
                "\"IS_OPEN_SEDENTARY_REMIND\" INTEGER NOT NULL ," + // 61: isOpenSedentaryRemind
                "\"IS_OPEN_WEATHER_PUSH\" INTEGER NOT NULL ," + // 62: isOpenWeatherPush
                "\"IS24_FORMAT\" INTEGER NOT NULL ," + // 63: is24Format
                "\"LIGHT_SCREEN_TIME\" INTEGER NOT NULL ," + // 64: lightScreenTime
                "\"SMART_ANTI_LOST\" INTEGER NOT NULL ," + // 65: smartAnti_lost
                "\"BAND_NAME\" TEXT);"); // 66: bandName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
        stmt.bindDouble(3, entity.getHeight());
        stmt.bindDouble(4, entity.getWeight());
        stmt.bindLong(5, entity.getGendar());
        stmt.bindLong(6, entity.getBirthday());
 
        String nick = entity.getNick();
        if (nick != null) {
            stmt.bindString(7, nick);
        }
        stmt.bindLong(8, entity.getWalkTarget());
        stmt.bindDouble(9, entity.getWeightTarget());
        stmt.bindLong(10, entity.getCaloryTarget());
        stmt.bindLong(11, entity.getTimeTarget());
        stmt.bindLong(12, entity.getSleepTarget());
 
        String appVersion = entity.getAppVersion();
        if (appVersion != null) {
            stmt.bindString(13, appVersion);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(14, email);
        }
 
        String sessionToken = entity.getSessionToken();
        if (sessionToken != null) {
            stmt.bindString(15, sessionToken);
        }
 
        String updatedAt = entity.getUpdatedAt();
        if (updatedAt != null) {
            stmt.bindString(16, updatedAt);
        }
        stmt.bindLong(17, entity.getSportTimeTarget());
        stmt.bindLong(18, entity.getSex());
 
        String firmwareSystemVersion = entity.getFirmwareSystemVersion();
        if (firmwareSystemVersion != null) {
            stmt.bindString(19, firmwareSystemVersion);
        }
        stmt.bindLong(20, entity.getUnit());
 
        String userDeviceSystemVersion = entity.getUserDeviceSystemVersion();
        if (userDeviceSystemVersion != null) {
            stmt.bindString(21, userDeviceSystemVersion);
        }
 
        String createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            stmt.bindString(22, createdAt);
        }
        stmt.bindDouble(23, entity.getTotalDistance());
        stmt.bindLong(24, entity.getCopperNumber());
        stmt.bindLong(25, entity.getSilverNumber());
        stmt.bindLong(26, entity.getEmailVerified() ? 1L: 0L);
        stmt.bindLong(27, entity.getUpvotes());
        stmt.bindLong(28, entity.getDayHighestSteps());
 
        String surfaceImg = entity.getSurfaceImg();
        if (surfaceImg != null) {
            stmt.bindString(29, surfaceImg);
        }
        stmt.bindLong(30, entity.getTotalExerciceDays());
 
        String bestSportInfoWithDay = entity.getBestSportInfoWithDay();
        if (bestSportInfoWithDay != null) {
            stmt.bindString(31, bestSportInfoWithDay);
        }
 
        String objectId = entity.getObjectId();
        if (objectId != null) {
            stmt.bindString(32, objectId);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(33, avatar);
        }
        stmt.bindLong(34, entity.getGoldNumber());
        stmt.bindDouble(35, entity.getTotalCalories());
        stmt.bindLong(36, entity.getRanking());
        stmt.bindDouble(37, entity.getDistanceTarget());
        stmt.bindLong(38, entity.getBeginSportTimestamp());
 
        String userDeviceType = entity.getUserDeviceType();
        if (userDeviceType != null) {
            stmt.bindString(39, userDeviceType);
        }
        stmt.bindLong(40, entity.getTotalWalkCount());
        stmt.bindLong(41, entity.getMobilePhoneVerified() ? 1L: 0L);
 
        String archivementList = entity.getArchivementList();
        if (archivementList != null) {
            stmt.bindString(42, archivementList);
        }
 
        String deviceTypes = entity.getDeviceTypes();
        if (deviceTypes != null) {
            stmt.bindString(43, deviceTypes);
        }
        stmt.bindLong(44, entity.getWhichClock());
        stmt.bindLong(45, entity.getIsMessage() ? 1L: 0L);
        stmt.bindLong(46, entity.getIsMotorOn() ? 1L: 0L);
        stmt.bindLong(47, entity.getIsScreenOn() ? 1L: 0L);
        stmt.bindLong(48, entity.getDisturbTimeSwitch() ? 1L: 0L);
        stmt.bindLong(49, entity.getFromTimeHour());
        stmt.bindLong(50, entity.getFromTimeMinute());
        stmt.bindLong(51, entity.getToTimeHour());
        stmt.bindLong(52, entity.getToTimeMinute());
        stmt.bindLong(53, entity.getHeartRate() ? 1L: 0L);
        stmt.bindLong(54, entity.getHighestHeartRate());
        stmt.bindLong(55, entity.getPeriodTest() ? 1L: 0L);
        stmt.bindLong(56, entity.getPeriod());
        stmt.bindLong(57, entity.getInCallRemind() ? 1L: 0L);
        stmt.bindLong(58, entity.getVibrationsTimes());
 
        String messageRemind = entity.getMessageRemind();
        if (messageRemind != null) {
            stmt.bindString(59, messageRemind);
        }
        stmt.bindLong(60, entity.getIsOpenMessageRemind() ? 1L: 0L);
        stmt.bindLong(61, entity.getSedentaryTime());
        stmt.bindLong(62, entity.getIsOpenSedentaryRemind() ? 1L: 0L);
        stmt.bindLong(63, entity.getIsOpenWeatherPush() ? 1L: 0L);
        stmt.bindLong(64, entity.getIs24Format() ? 1L: 0L);
        stmt.bindLong(65, entity.getLightScreenTime());
        stmt.bindLong(66, entity.getSmartAnti_lost() ? 1L: 0L);
 
        String bandName = entity.getBandName();
        if (bandName != null) {
            stmt.bindString(67, bandName);
        }
    }

    @Override
    protected void attachEntity(User entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // username
            cursor.getDouble(offset + 2), // height
            cursor.getDouble(offset + 3), // weight
            cursor.getInt(offset + 4), // gendar
            cursor.getLong(offset + 5), // birthday
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // nick
            cursor.getInt(offset + 7), // walkTarget
            cursor.getDouble(offset + 8), // weightTarget
            cursor.getInt(offset + 9), // caloryTarget
            cursor.getInt(offset + 10), // timeTarget
            cursor.getInt(offset + 11), // sleepTarget
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // appVersion
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // email
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // sessionToken
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // updatedAt
            cursor.getInt(offset + 16), // sportTimeTarget
            cursor.getInt(offset + 17), // sex
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // firmwareSystemVersion
            cursor.getInt(offset + 19), // unit
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // userDeviceSystemVersion
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // createdAt
            cursor.getDouble(offset + 22), // totalDistance
            cursor.getInt(offset + 23), // copperNumber
            cursor.getInt(offset + 24), // silverNumber
            cursor.getShort(offset + 25) != 0, // emailVerified
            cursor.getInt(offset + 26), // upvotes
            cursor.getInt(offset + 27), // dayHighestSteps
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // surfaceImg
            cursor.getInt(offset + 29), // totalExerciceDays
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // bestSportInfoWithDay
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // objectId
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // avatar
            cursor.getInt(offset + 33), // goldNumber
            cursor.getDouble(offset + 34), // totalCalories
            cursor.getInt(offset + 35), // ranking
            cursor.getFloat(offset + 36), // distanceTarget
            cursor.getLong(offset + 37), // beginSportTimestamp
            cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38), // userDeviceType
            cursor.getInt(offset + 39), // totalWalkCount
            cursor.getShort(offset + 40) != 0, // mobilePhoneVerified
            cursor.isNull(offset + 41) ? null : cursor.getString(offset + 41), // archivementList
            cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42), // deviceTypes
            cursor.getInt(offset + 43), // whichClock
            cursor.getShort(offset + 44) != 0, // isMessage
            cursor.getShort(offset + 45) != 0, // isMotorOn
            cursor.getShort(offset + 46) != 0, // isScreenOn
            cursor.getShort(offset + 47) != 0, // disturbTimeSwitch
            cursor.getInt(offset + 48), // fromTimeHour
            cursor.getInt(offset + 49), // fromTimeMinute
            cursor.getInt(offset + 50), // toTimeHour
            cursor.getInt(offset + 51), // toTimeMinute
            cursor.getShort(offset + 52) != 0, // heartRate
            cursor.getInt(offset + 53), // highestHeartRate
            cursor.getShort(offset + 54) != 0, // periodTest
            cursor.getInt(offset + 55), // period
            cursor.getShort(offset + 56) != 0, // inCallRemind
            cursor.getInt(offset + 57), // vibrationsTimes
            cursor.isNull(offset + 58) ? null : cursor.getString(offset + 58), // messageRemind
            cursor.getShort(offset + 59) != 0, // isOpenMessageRemind
            cursor.getInt(offset + 60), // sedentaryTime
            cursor.getShort(offset + 61) != 0, // isOpenSedentaryRemind
            cursor.getShort(offset + 62) != 0, // isOpenWeatherPush
            cursor.getShort(offset + 63) != 0, // is24Format
            cursor.getInt(offset + 64), // lightScreenTime
            cursor.getShort(offset + 65) != 0, // smartAnti_lost
            cursor.isNull(offset + 66) ? null : cursor.getString(offset + 66) // bandName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsername(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHeight(cursor.getDouble(offset + 2));
        entity.setWeight(cursor.getDouble(offset + 3));
        entity.setGendar(cursor.getInt(offset + 4));
        entity.setBirthday(cursor.getLong(offset + 5));
        entity.setNick(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWalkTarget(cursor.getInt(offset + 7));
        entity.setWeightTarget(cursor.getDouble(offset + 8));
        entity.setCaloryTarget(cursor.getInt(offset + 9));
        entity.setTimeTarget(cursor.getInt(offset + 10));
        entity.setSleepTarget(cursor.getInt(offset + 11));
        entity.setAppVersion(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setEmail(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSessionToken(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setUpdatedAt(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setSportTimeTarget(cursor.getInt(offset + 16));
        entity.setSex(cursor.getInt(offset + 17));
        entity.setFirmwareSystemVersion(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setUnit(cursor.getInt(offset + 19));
        entity.setUserDeviceSystemVersion(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCreatedAt(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setTotalDistance(cursor.getDouble(offset + 22));
        entity.setCopperNumber(cursor.getInt(offset + 23));
        entity.setSilverNumber(cursor.getInt(offset + 24));
        entity.setEmailVerified(cursor.getShort(offset + 25) != 0);
        entity.setUpvotes(cursor.getInt(offset + 26));
        entity.setDayHighestSteps(cursor.getInt(offset + 27));
        entity.setSurfaceImg(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setTotalExerciceDays(cursor.getInt(offset + 29));
        entity.setBestSportInfoWithDay(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setObjectId(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setAvatar(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setGoldNumber(cursor.getInt(offset + 33));
        entity.setTotalCalories(cursor.getDouble(offset + 34));
        entity.setRanking(cursor.getInt(offset + 35));
        entity.setDistanceTarget(cursor.getFloat(offset + 36));
        entity.setBeginSportTimestamp(cursor.getLong(offset + 37));
        entity.setUserDeviceType(cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38));
        entity.setTotalWalkCount(cursor.getInt(offset + 39));
        entity.setMobilePhoneVerified(cursor.getShort(offset + 40) != 0);
        entity.setArchivementList(cursor.isNull(offset + 41) ? null : cursor.getString(offset + 41));
        entity.setDeviceTypes(cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42));
        entity.setWhichClock(cursor.getInt(offset + 43));
        entity.setIsMessage(cursor.getShort(offset + 44) != 0);
        entity.setIsMotorOn(cursor.getShort(offset + 45) != 0);
        entity.setIsScreenOn(cursor.getShort(offset + 46) != 0);
        entity.setDisturbTimeSwitch(cursor.getShort(offset + 47) != 0);
        entity.setFromTimeHour(cursor.getInt(offset + 48));
        entity.setFromTimeMinute(cursor.getInt(offset + 49));
        entity.setToTimeHour(cursor.getInt(offset + 50));
        entity.setToTimeMinute(cursor.getInt(offset + 51));
        entity.setHeartRate(cursor.getShort(offset + 52) != 0);
        entity.setHighestHeartRate(cursor.getInt(offset + 53));
        entity.setPeriodTest(cursor.getShort(offset + 54) != 0);
        entity.setPeriod(cursor.getInt(offset + 55));
        entity.setInCallRemind(cursor.getShort(offset + 56) != 0);
        entity.setVibrationsTimes(cursor.getInt(offset + 57));
        entity.setMessageRemind(cursor.isNull(offset + 58) ? null : cursor.getString(offset + 58));
        entity.setIsOpenMessageRemind(cursor.getShort(offset + 59) != 0);
        entity.setSedentaryTime(cursor.getInt(offset + 60));
        entity.setIsOpenSedentaryRemind(cursor.getShort(offset + 61) != 0);
        entity.setIsOpenWeatherPush(cursor.getShort(offset + 62) != 0);
        entity.setIs24Format(cursor.getShort(offset + 63) != 0);
        entity.setLightScreenTime(cursor.getInt(offset + 64));
        entity.setSmartAnti_lost(cursor.getShort(offset + 65) != 0);
        entity.setBandName(cursor.isNull(offset + 66) ? null : cursor.getString(offset + 66));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
