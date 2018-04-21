package com.coband.cocoband.mvp.model.entity.response;

import java.util.List;

/**
 * Created by ivan on 3/12/18.
 */

public class UpdateAccountResponse {
    /**
     * code : 0
     * message : ok
     * payload : {"user":{"uid":101010122,"accountType":"email","deviceID":"","email":"23767251@qq.com","accountVerified":false,"personalInfo":{"avatar":{"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/3998e9da-2424-11e8-b16a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:31:03.856Z"},"background":{"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/f8775d1e-2424-11e8-b55a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:36:24.082Z"},"birthday":"1990-10-20","bloodType":"O","city":"Los angle","country":"USA","gender":"Male","height":180,"location":{"latitude":11.23,"longitude":33.45},"nickname":"jack","province":"New York","weight":72.5},"phoneInfo":{"language":"zh_CN","osType":"iOS","osVersion":"11.2.3","phoneModel":"iphone 6s plus","timezone":28800},"sportSummary":{"achievements":[],"maxDaySteps":0,"startExerciseTime":0,"totalCalories":0,"totalDistance":0,"totalExerciseDays":0,"totalSteps":0},"sportTarget":{"sleepTarget":480,"stepTarget":8000,"weightTarget":70},"createdAt":"2018-03-05T12:01:10.988Z","updatedAt":"2018-03-05T12:02:45.953Z"}}
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
         * user : {"uid":101010122,"accountType":"email","deviceID":"","email":"23767251@qq.com","accountVerified":false,"personalInfo":{"avatar":{"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/3998e9da-2424-11e8-b16a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:31:03.856Z"},"background":{"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/f8775d1e-2424-11e8-b55a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:36:24.082Z"},"birthday":"1990-10-20","bloodType":"O","city":"Los angle","country":"USA","gender":"Male","height":180,"location":{"latitude":11.23,"longitude":33.45},"nickname":"jack","province":"New York","weight":72.5},"phoneInfo":{"language":"zh_CN","osType":"iOS","osVersion":"11.2.3","phoneModel":"iphone 6s plus","timezone":28800},"sportSummary":{"achievements":[],"maxDaySteps":0,"startExerciseTime":0,"totalCalories":0,"totalDistance":0,"totalExerciseDays":0,"totalSteps":0},"sportTarget":{"sleepTarget":480,"stepTarget":8000,"weightTarget":70},"createdAt":"2018-03-05T12:01:10.988Z","updatedAt":"2018-03-05T12:02:45.953Z"}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * uid : 101010122
             * accountType : email
             * deviceID :
             * email : 23767251@qq.com
             * accountVerified : false
             * personalInfo : {"avatar":{"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/3998e9da-2424-11e8-b16a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:31:03.856Z"},"background":{"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/f8775d1e-2424-11e8-b55a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:36:24.082Z"},"birthday":"1990-10-20","bloodType":"O","city":"Los angle","country":"USA","gender":"Male","height":180,"location":{"latitude":11.23,"longitude":33.45},"nickname":"jack","province":"New York","weight":72.5}
             * phoneInfo : {"language":"zh_CN","osType":"iOS","osVersion":"11.2.3","phoneModel":"iphone 6s plus","timezone":28800}
             * sportSummary : {"achievements":[],"maxDaySteps":0,"startExerciseTime":0,"totalCalories":0,"totalDistance":0,"totalExerciseDays":0,"totalSteps":0}
             * sportTarget : {"sleepTarget":480,"stepTarget":8000,"weightTarget":70}
             * createdAt : 2018-03-05T12:01:10.988Z
             * updatedAt : 2018-03-05T12:02:45.953Z
             */

            private String uid;
            private String accountType;
            private String deviceID;
            private String email;
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

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public String getDeviceID() {
                return deviceID;
            }

            public void setDeviceID(String deviceID) {
                this.deviceID = deviceID;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
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
                 * avatar : {"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/3998e9da-2424-11e8-b16a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:31:03.856Z"}
                 * background : {"md5":"9bba896ae53a3c090ecb34eab41264f9","path":"http://localhost:10080/cdn/image/f8775d1e-2424-11e8-b55a-34363bc5d3b2.png","size":161029,"updatedAt":"2018-03-10T05:36:24.082Z"}
                 * birthday : 1990-10-20
                 * bloodType : O
                 * city : Los angle
                 * country : USA
                 * gender : Male
                 * height : 180
                 * location : {"latitude":11.23,"longitude":33.45}
                 * nickname : jack
                 * province : New York
                 * weight : 72.5
                 */

                private AvatarBean avatar;
                private BackgroundBean background;
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

                public String getUnitSystem() {
                    return unitSystem;
                }

                public void setUnitSystem(String unitSystem) {
                    this.unitSystem = unitSystem;
                }

                public AvatarBean getAvatar() {
                    return avatar;
                }

                public void setAvatar(AvatarBean avatar) {
                    this.avatar = avatar;
                }

                public BackgroundBean getBackground() {
                    return background;
                }

                public void setBackground(BackgroundBean background) {
                    this.background = background;
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

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
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

                public static class AvatarBean {
                    /**
                     * md5 : 9bba896ae53a3c090ecb34eab41264f9
                     * path : http://localhost:10080/cdn/image/3998e9da-2424-11e8-b16a-34363bc5d3b2.png
                     * size : 161029
                     * updatedAt : 2018-03-10T05:31:03.856Z
                     */

                    private String md5;
                    private String path;
                    private int size;
                    private String updatedAt;

                    public String getMd5() {
                        return md5;
                    }

                    public void setMd5(String md5) {
                        this.md5 = md5;
                    }

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }

                    public String getUpdatedAt() {
                        return updatedAt;
                    }

                    public void setUpdatedAt(String updatedAt) {
                        this.updatedAt = updatedAt;
                    }
                }

                public static class BackgroundBean {
                    /**
                     * md5 : 9bba896ae53a3c090ecb34eab41264f9
                     * path : http://localhost:10080/cdn/image/f8775d1e-2424-11e8-b55a-34363bc5d3b2.png
                     * size : 161029
                     * updatedAt : 2018-03-10T05:36:24.082Z
                     */

                    private String md5;
                    private String path;
                    private int size;
                    private String updatedAt;

                    public String getMd5() {
                        return md5;
                    }

                    public void setMd5(String md5) {
                        this.md5 = md5;
                    }

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }

                    public String getUpdatedAt() {
                        return updatedAt;
                    }

                    public void setUpdatedAt(String updatedAt) {
                        this.updatedAt = updatedAt;
                    }
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
                private int startExerciseTime;
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

                public int getStartExerciseTime() {
                    return startExerciseTime;
                }

                public void setStartExerciseTime(int startExerciseTime) {
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
