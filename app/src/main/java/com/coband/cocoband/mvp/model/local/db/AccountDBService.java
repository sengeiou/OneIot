package com.coband.cocoband.mvp.model.local.db;

import com.coband.App;
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.watchassistant.Account;
import com.coband.watchassistant.AccountDao;

import java.util.List;

/**
 * Created by ivan on 3/11/18.
 */

public class AccountDBService {
    static void insertAccount(Account account) {
        List<Account> accounts = App.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.Uid.eq(account.getUid()))
                .list();
        if (!accounts.isEmpty()) {
            for (Account existAccount : accounts) {
                App.getAccountDao().delete(existAccount);
            }
        }

        App.getAccountDao().insert(account);
    }

    static void updateToken(String token) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setToken(token);
            App.getAccountDao().update(account);
        }
    }

    static String queryTokenByUid(String uid) {
        Account account = getAccount(uid);
        if (account == null) {
            return null;
        } else {
            return account.getToken();
        }
    }

    static Account getAccount(String uid) {
        List<Account> accounts = App.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.Uid.eq(uid))
                .list();
        if (accounts.isEmpty()) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    static void updateOrInsertAccount(Account account) {
        Account existsAccount = getCurrentAccount();
        if (existsAccount == null) {
            App.getAccountDao().insert(account);
        } else {
            String token = null;
            if (account.getToken() != null) {
                token = account.getToken();
            }

            existsAccount.setToken(token);
            String did = null;
            if (account.getDid() != null) {
                did = account.getDid();
            }

            String avatar = account.getAvatar();
            String birthday = account.getBirthday();
            String bloodType = account.getBloodType();
            String city = account.getCity();
            String country = account.getCountry();
            String gender = account.getGender();
            String nickName = account.getNickname();
            String province = account.getProvince();

            int height = 0;
            if (account.getHeight() != null) {
                height = account.getHeight();
            }

            double weight = 0;
            if (account.getWeight() != null) {
                weight = account.getWeight();
            }

            double latitude = 0, longitude = 0;
            if (account.getLatitude() != null) {
                latitude = account.getLatitude();
            }

            if (account.getLongitude() != null) {
                longitude = account.getLongitude();
            }

            int timezone = -1;
            if (account.getTimezone() != null) {
                timezone = account.getTimezone();
            }

            String language = account.getLanguage();
            String osType = account.getOsType();
            String osVersion = account.getOsVersion();
            String phoneModel = account.getPhoneModel();

            String achievements = account.getAchievements();

            long maxDaySteps = 0, totalSteps = 0, startExerciseTime = 0;
            double totalDistance = 0, totalCalories = 0;
            int totalExerciseDays = 0;
            if (account.getMaxDaySteps() != null)
                maxDaySteps = account.getMaxDaySteps();

            if (account.getTotalSteps() != null) {
                totalSteps = account.getTotalSteps();
            }
            if (account.getStartExerciseTime() != null) {
                startExerciseTime = account.getStartExerciseTime();
            }
            if (account.getTotalCalories() != null) {
                totalCalories = account.getTotalCalories();
            }
            if (account.getTotalDistance() != null) {
                totalDistance = account.getTotalDistance();
            }
            if (account.getTotalExerciseDays() != null) {
                totalExerciseDays = account.getTotalExerciseDays();
            }


            int sleepTarget = 0, stepTarget = 0;
            double weightTarget = 0;
            if (account.getSleepTarget() != null) {
                sleepTarget = account.getSleepTarget();
            }
            if (account.getStepTarget() != null) {
                stepTarget = account.getStepTarget();
            }
            if (account.getWeightTarget() != null) {
                weightTarget = account.getWeightTarget();
            }


            String acc = account.getAccount();
            String pwd = account.getPassword();

            if (acc != null) {
                existsAccount.setAccount(acc);
            }

            if (pwd != null) {
                account.setPassword(pwd);
            }

            if (did != null) {
                existsAccount.setDid(did);
            }

            if (avatar != null) {
                existsAccount.setAvatar(avatar);
            }

            if (birthday != null) {
                existsAccount.setBirthday(birthday);
            }

            if (bloodType != null) {
                existsAccount.setBloodType(bloodType);
            }

            if (city != null) {
                existsAccount.setCity(city);
            }

            if (country != null) {
                existsAccount.setCountry(country);
            }

            if (gender != null) {
                existsAccount.setGender(gender);
            }

            if (nickName != null) {
                existsAccount.setNickname(nickName);
            }

            if (province != null) {
                existsAccount.setProvince(province);
            }

            if (height != 0) {
                existsAccount.setHeight(height);
            }

            if (latitude != 0) {
                existsAccount.setLatitude(latitude);
            }

            if (longitude != 0) {
                existsAccount.setLongitude(longitude);
            }

            if (weight != 0) {
                existsAccount.setWeight(weight);
            }

            if (timezone != -1) {
                existsAccount.setTimezone(timezone);
            }

            if (language != null) {
                existsAccount.setLanguage(language);
            }

            if (osType != null) {
                existsAccount.setOsType(osType);
            }

            if (osVersion != null) {
                existsAccount.setOsVersion(osVersion);
            }

            if (phoneModel != null) {
                existsAccount.setPhoneModel(phoneModel);
            }

            if (achievements != null) {
                existsAccount.setAchievements(achievements);
            }

            if (maxDaySteps != 0) {
                existsAccount.setMaxDaySteps(maxDaySteps);
            }

            if (totalSteps != 0) {
                existsAccount.setTotalSteps(totalSteps);
            }

            if (totalCalories != 0) {
                existsAccount.setTotalCalories(totalCalories);
            }

            if (totalDistance != 0) {
                existsAccount.setTotalDistance(totalDistance);
            }

            if (totalExerciseDays != 0) {
                existsAccount.setTotalExerciseDays(totalExerciseDays);
            }

            if (startExerciseTime != 0) {
                existsAccount.setStartExerciseTime(startExerciseTime);
            }

            if (sleepTarget != 0) {
                existsAccount.setSleepTarget(sleepTarget);
            }

            if (stepTarget != 0) {
                existsAccount.setStepTarget(stepTarget);
            }

            if (stepTarget != 0) {
                existsAccount.setStepTarget(stepTarget);
            }

            if (weightTarget != 0) {
                existsAccount.setWeightTarget(weightTarget);
            }

            App.getAccountDao().update(existsAccount);

        }
    }

    static void updateAccountTarget(int stepTarget, int sleepTarget, double weightTarget) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setStepTarget(stepTarget);
            account.setSleepTarget(sleepTarget);
            account.setWeightTarget(weightTarget);
            App.getAccountDao().update(account);
        }
    }

    static void updateWeight(double weight) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setWeight(weight);
            App.getAccountDao().update(account);
        }
    }

    public static void updateNickname(String nickname) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setNickname(nickname);
            App.getAccountDao().update(account);
        }
    }

    static Account getCurrentAccount() {
        List<Account> accounts = App.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.Uid.eq(PreferencesHelper.getCurrentId()))
                .list();
        if (accounts.isEmpty()) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    static void updateGender(String gender) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setGender(gender);
            App.getAccountDao().update(account);
        }
    }

    static void updateHeight(int height) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setHeight(height);
            App.getAccountDao().update(account);
        }
    }

    static void updateBirthday(String birthday) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setBirthday(birthday);
            App.getAccountDao().update(account);
        }
    }

    static void updateAvatar(String avatarPath, String avatarMD5) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setAvatar(avatarPath);
            account.setAvatarMD5(avatarMD5);
            App.getAccountDao().update(account);
        }
    }

    static void updateBackground(String bgPath, String bgMD5) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setBackground(bgPath);
            account.setBackgroundMD5(bgMD5);
            App.getAccountDao().update(account);
        }
    }

    static void updateUnitSystem(String unitSystem) {
        Account account = getCurrentAccount();
        if (account != null) {
            account.setUnitSystem(unitSystem);
            App.getAccountDao().update(account);
        }
    }

    static void updateAccountInfo(UpdateAccountInfo info) {
        Account account = getCurrentAccount();
        if (account != null) {
            if (info.getNickName() != null) {
                account.setNickname(info.getNickName());
            }

            if (info.getUnitSystem() != null) {
                account.setUnitSystem(info.getUnitSystem());
            }

            if (info.getBirthday() != null) {
                account.setBirthday(info.getBirthday());
            }

            if (info.getBloodType() != null) {
                account.setBloodType(info.getBloodType());
            }

            if (info.getGender() != null) {
                account.setGender(info.getGender());
            }

            if (info.getWeight() != 0) {
                account.setWeight(info.getWeight());
            }

            if (info.getHeight() != 0) {
                account.setHeight(info.getHeight());
            }

            App.getAccountDao().update(account);
        }
    }
}
