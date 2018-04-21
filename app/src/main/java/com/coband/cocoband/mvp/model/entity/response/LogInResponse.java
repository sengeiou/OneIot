package com.coband.cocoband.mvp.model.entity.response;

import java.util.List;

/**
 * Created by ivan on 3/13/18.
 */

public class LogInResponse {
    /**
     * code : 0
     * message : ok
     * payload : {"expire":1516976964,"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY5NDE2MjMsImlkIjoiYWRtaW4iLCJvcmlnX2lhdCI6MTUxNjkzODAyM30.oCb3AylMhK7CnyGk16eX0J0atH5jngrzdEuSL9VBbL4","user":{"uid":101010111,"did":"","accountVerified":true,"personalInfo":{"avatar":"default_icon.png","birthday":"1990-10-20","bloodType":"O","city":"Los angle","country":"USA","gender":"Male","height":180,"location":{"latitude":11.23,"longitude":33.45},"nickName":"jack","province":"New York","weight":72.5},"phoneInfo":{"language":"zh_CN","osType":"iOS","osVersion":"11.2.3","phoneModel":"iphone 6s plus","timezone":28800},"sportSummary":{"achievements":[],"maxDaySteps":0,"startExerciseTime":0,"totalCalories":0,"totalDistance":0,"totalExerciseDays":0,"totalSteps":0},"sportTarget":{"sleepTarget":480,"stepTarget":8000,"weightTarget":70},"createdAt":"2018-02-27T09:45:09.918Z","updatedAt":"2018-02-27T19:47:52.505455829+08:00"}}
     */

    private int code;
    private String message;
    private PayloadBean payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class PayloadBean {
        /**
         * expire : 1516976964
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY5NDE2MjMsImlkIjoiYWRtaW4iLCJvcmlnX2lhdCI6MTUxNjkzODAyM30.oCb3AylMhK7CnyGk16eX0J0atH5jngrzdEuSL9VBbL4
         * user : {"uid":101010111,"did":"","accountVerified":true,"personalInfo":{"avatar":"default_icon.png","birthday":"1990-10-20","bloodType":"O","city":"Los angle","country":"USA","gender":"Male","height":180,"location":{"latitude":11.23,"longitude":33.45},"nickName":"jack","province":"New York","weight":72.5},"phoneInfo":{"language":"zh_CN","osType":"iOS","osVersion":"11.2.3","phoneModel":"iphone 6s plus","timezone":28800},"sportSummary":{"achievements":[],"maxDaySteps":0,"startExerciseTime":0,"totalCalories":0,"totalDistance":0,"totalExerciseDays":0,"totalSteps":0},"sportTarget":{"sleepTarget":480,"stepTarget":8000,"weightTarget":70},"createdAt":"2018-02-27T09:45:09.918Z","updatedAt":"2018-02-27T19:47:52.505455829+08:00"}
         */

        private int expire;
        private String token;
        private UserBean user;

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * uid : 101010111
             * did :
             * accountVerified : true
             * personalInfo : {"avatar":"default_icon.png","birthday":"1990-10-20","bloodType":"O","city":"Los angle","country":"USA","gender":"Male","height":180,"location":{"latitude":11.23,"longitude":33.45},"nickName":"jack","province":"New York","weight":72.5}
             * phoneInfo : {"language":"zh_CN","osType":"iOS","osVersion":"11.2.3","phoneModel":"iphone 6s plus","timezone":28800}
             * sportSummary : {"achievements":[],"maxDaySteps":0,"startExerciseTime":0,"totalCalories":0,"totalDistance":0,"totalExerciseDays":0,"totalSteps":0}
             * sportTarget : {"sleepTarget":480,"stepTarget":8000,"weightTarget":70}
             * createdAt : 2018-02-27T09:45:09.918Z
             * updatedAt : 2018-02-27T19:47:52.505455829+08:00
             */

            private String uid;
            private String did;
            private boolean accountVerified;
            private PersonalInfoBean personalInfo;
            private PhoneInfoBean phoneInfo;
            private SportSummaryBean sportSummary;
            private SportTargetBean sportTarget;
            private String createdAt;
            private String updatedAt;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getDid() {
                return did;
            }

            public void setDid(String did) {
                this.did = did;
            }

            public boolean isAccountVerified() {
                return accountVerified;
            }

            public void setAccountVerified(boolean accountVerified) {
                this.accountVerified = accountVerified;
            }

            public PersonalInfoBean getPersonalInfo() {
                return personalInfo;
            }

            public void setPersonalInfo(PersonalInfoBean personalInfo) {
                this.personalInfo = personalInfo;
            }

            public PhoneInfoBean getPhoneInfo() {
                return phoneInfo;
            }

            public void setPhoneInfo(PhoneInfoBean phoneInfo) {
                this.phoneInfo = phoneInfo;
            }

            public SportSummaryBean getSportSummary() {
                return sportSummary;
            }

            public void setSportSummary(SportSummaryBean sportSummary) {
                this.sportSummary = sportSummary;
            }

            public SportTargetBean getSportTarget() {
                return sportTarget;
            }

            public void setSportTarget(SportTargetBean sportTarget) {
                this.sportTarget = sportTarget;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public static class PersonalInfoBean {
                /**
                 * avatar : default_icon.png
                 * birthday : 1990-10-20
                 * bloodType : O
                 * city : Los angle
                 * country : USA
                 * gender : Male
                 * height : 180
                 * location : {"latitude":11.23,"longitude":33.45}
                 * nickName : jack
                 * province : New York
                 * weight : 72.5
                 */

                private Avatar avatar;
                private String birthday;
                private String bloodType;
                private String city;
                private String country;
                private String gender;
                private int height;
                private LocationBean location;
                private String nickname;
                private String province;
                private double weight;
                private String unitSystem;
                private Background background;

                public Avatar getAvatar() {
                    return avatar;
                }

                public Background getBackground() {
                    return background;
                }

                public void setBackground(Background background) {
                    this.background = background;
                }

                public void setAvatar(Avatar avatar) {
                    this.avatar = avatar;
                }

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public String getBloodType() {
                    return bloodType;
                }

                public void setBloodType(String bloodType) {
                    this.bloodType = bloodType;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public LocationBean getLocation() {
                    return location;
                }

                public void setLocation(LocationBean location) {
                    this.location = location;
                }

                public String getProvince() {
                    return province;
                }

                public void setProvince(String province) {
                    this.province = province;
                }

                public double getWeight() {
                    return weight;
                }

                public void setWeight(double weight) {
                    this.weight = weight;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getUnitSystem() {
                    return unitSystem;
                }

                public void setUnitSystem(String unitSystem) {
                    this.unitSystem = unitSystem;
                }

                public static class LocationBean {
                    /**
                     * latitude : 11.23
                     * longitude : 33.45
                     */

                    private double latitude;
                    private double longitude;

                    public double getLatitude() {
                        return latitude;
                    }

                    public void setLatitude(double latitude) {
                        this.latitude = latitude;
                    }

                    public double getLongitude() {
                        return longitude;
                    }

                    public void setLongitude(double longitude) {
                        this.longitude = longitude;
                    }


                }

                public static class Avatar {
                    private String path;
                    private String md5;
                    private int size;

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public String getMd5() {
                        return md5;
                    }

                    public void setMd5(String md5) {
                        this.md5 = md5;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }
                }

                public static class Background {
                    private String path;
                    private String md5;
                    private int size;

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public String getMd5() {
                        return md5;
                    }

                    public void setMd5(String md5) {
                        this.md5 = md5;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }
                }
            }

            public static class PhoneInfoBean {
                /**
                 * language : zh_CN
                 * osType : iOS
                 * osVersion : 11.2.3
                 * phoneModel : iphone 6s plus
                 * timezone : 28800
                 */

                private String language;
                private String osType;
                private String osVersion;
                private String phoneModel;
                private int timezone;

                public String getLanguage() {
                    return language;
                }

                public void setLanguage(String language) {
                    this.language = language;
                }

                public String getOsType() {
                    return osType;
                }

                public void setOsType(String osType) {
                    this.osType = osType;
                }

                public String getOsVersion() {
                    return osVersion;
                }

                public void setOsVersion(String osVersion) {
                    this.osVersion = osVersion;
                }

                public String getPhoneModel() {
                    return phoneModel;
                }

                public void setPhoneModel(String phoneModel) {
                    this.phoneModel = phoneModel;
                }

                public int getTimezone() {
                    return timezone;
                }

                public void setTimezone(int timezone) {
                    this.timezone = timezone;
                }
            }

            public static class SportSummaryBean {
                /**
                 * achievements : []
                 * maxDaySteps : 0
                 * startExerciseTime : 0
                 * totalCalories : 0
                 * totalDistance : 0
                 * totalExerciseDays : 0
                 * totalSteps : 0
                 */

                private int maxDaySteps;
                private long startExerciseTime;
                private double totalCalories;
                private double totalDistance;
                private int totalExerciseDays;
                private int totalSteps;
                private List<String> achievements;

                public int getMaxDaySteps() {
                    return maxDaySteps;
                }

                public void setMaxDaySteps(int maxDaySteps) {
                    this.maxDaySteps = maxDaySteps;
                }

                public long getStartExerciseTime() {
                    return startExerciseTime;
                }

                public void setStartExerciseTime(long startExerciseTime) {
                    this.startExerciseTime = startExerciseTime;
                }

                public double getTotalCalories() {
                    return totalCalories;
                }

                public void setTotalCalories(double totalCalories) {
                    this.totalCalories = totalCalories;
                }

                public double getTotalDistance() {
                    return totalDistance;
                }

                public void setTotalDistance(double totalDistance) {
                    this.totalDistance = totalDistance;
                }

                public int getTotalExerciseDays() {
                    return totalExerciseDays;
                }

                public void setTotalExerciseDays(int totalExerciseDays) {
                    this.totalExerciseDays = totalExerciseDays;
                }

                public int getTotalSteps() {
                    return totalSteps;
                }

                public void setTotalSteps(int totalSteps) {
                    this.totalSteps = totalSteps;
                }

                public List<String> getAchievements() {
                    return achievements;
                }

                public void setAchievements(List<String> achievements) {
                    this.achievements = achievements;
                }
            }

            public static class SportTargetBean {
                /**
                 * sleepTarget : 480
                 * stepTarget : 8000
                 * weightTarget : 70
                 */

                private int sleepTarget;
                private int stepTarget;
                private double weightTarget;

                public int getSleepTarget() {
                    return sleepTarget;
                }

                public void setSleepTarget(int sleepTarget) {
                    this.sleepTarget = sleepTarget;
                }

                public int getStepTarget() {
                    return stepTarget;
                }

                public void setStepTarget(int stepTarget) {
                    this.stepTarget = stepTarget;
                }

                public double getWeightTarget() {
                    return weightTarget;
                }

                public void setWeightTarget(double weightTarget) {
                    this.weightTarget = weightTarget;
                }
            }
        }
    }
}
