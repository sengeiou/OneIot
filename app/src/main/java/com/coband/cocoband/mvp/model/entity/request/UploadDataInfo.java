package com.coband.cocoband.mvp.model.entity.request;

import java.util.List;

/**
 * Created by ivan on 4/18/18.
 */

public class UploadDataInfo {

    /**
     * sport : {"date":1520553600,"calories":123,"distance":2800,"stepCount":2890,"finishStepTarget":false}
     * weight : {"date":1520553600,"weight":71.5,"bodyMI":18.1}
     * sleep : {"date":1520553600,"finishSleepTarget":false,"sleepTotalTime":500,"lightSleepTime":130,"deepSleepTime":350,"wakeCount":2}
     * bloodPressureSamples : [{"timestamp":1520581182,"highPressure":129,"lowPressure":89,"heartRate":0},{"timestamp":1520584782,"highPressure":129,"lowPressure":89,"heartRate":0},{"timestamp":1520588382,"highPressure":129,"lowPressure":89,"heartRate":0}]
     * heartRateSamples : [{"timestamp":1520581182,"rate":70},{"timestamp":1520584782,"rate":70},{"timestamp":1520588382,"rate":70}]
     */

    private SportBean sport;
    private WeightBean weight;
    private SleepBean sleep;
    private List<BloodPressureSamplesBean> bloodPressureSamples;
    private List<HeartRateSamplesBean> heartRateSamples;

    public SportBean getSport() {
        return sport;
    }

    public void setSport(SportBean sport) {
        this.sport = sport;
    }

    public WeightBean getWeight() {
        return weight;
    }

    public void setWeight(WeightBean weight) {
        this.weight = weight;
    }

    public SleepBean getSleep() {
        return sleep;
    }

    public void setSleep(SleepBean sleep) {
        this.sleep = sleep;
    }

    public List<BloodPressureSamplesBean> getBloodPressureSamples() {
        return bloodPressureSamples;
    }

    public void setBloodPressureSamples(List<BloodPressureSamplesBean> bloodPressureSamples) {
        this.bloodPressureSamples = bloodPressureSamples;
    }

    public List<HeartRateSamplesBean> getHeartRateSamples() {
        return heartRateSamples;
    }

    public void setHeartRateSamples(List<HeartRateSamplesBean> heartRateSamples) {
        this.heartRateSamples = heartRateSamples;
    }

    public static class SportBean {
        /**
         * date : 1520553600
         * calories : 123.0
         * distance : 2800.0
         * stepCount : 2890
         * finishStepTarget : false
         */

        private int date;
        private double calories;
        private double distance;
        private int stepCount;
        private boolean finishStepTarget;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public double getCalories() {
            return calories;
        }

        public void setCalories(double calories) {
            this.calories = calories;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getStepCount() {
            return stepCount;
        }

        public void setStepCount(int stepCount) {
            this.stepCount = stepCount;
        }

        public boolean isFinishStepTarget() {
            return finishStepTarget;
        }

        public void setFinishStepTarget(boolean finishStepTarget) {
            this.finishStepTarget = finishStepTarget;
        }
    }

    public static class WeightBean {
        /**
         * date : 1520553600
         * weight : 71.5
         * bodyMI : 18.1
         */

        private int date;
        private double weight;
        private double bodyMI;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getBodyMI() {
            return bodyMI;
        }

        public void setBodyMI(double bodyMI) {
            this.bodyMI = bodyMI;
        }
    }

    public static class SleepBean {
        /**
         * date : 1520553600
         * finishSleepTarget : false
         * sleepTotalTime : 500
         * lightSleepTime : 130
         * deepSleepTime : 350
         * wakeCount : 2
         */

        private int date;
        private boolean finishSleepTarget;
        private int sleepTotalTime;
        private int lightSleepTime;
        private int deepSleepTime;
        private int wakeCount;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public boolean isFinishSleepTarget() {
            return finishSleepTarget;
        }

        public void setFinishSleepTarget(boolean finishSleepTarget) {
            this.finishSleepTarget = finishSleepTarget;
        }

        public int getSleepTotalTime() {
            return sleepTotalTime;
        }

        public void setSleepTotalTime(int sleepTotalTime) {
            this.sleepTotalTime = sleepTotalTime;
        }

        public int getLightSleepTime() {
            return lightSleepTime;
        }

        public void setLightSleepTime(int lightSleepTime) {
            this.lightSleepTime = lightSleepTime;
        }

        public int getDeepSleepTime() {
            return deepSleepTime;
        }

        public void setDeepSleepTime(int deepSleepTime) {
            this.deepSleepTime = deepSleepTime;
        }

        public int getWakeCount() {
            return wakeCount;
        }

        public void setWakeCount(int wakeCount) {
            this.wakeCount = wakeCount;
        }
    }

    public static class BloodPressureSamplesBean {
        /**
         * timestamp : 1520581182
         * highPressure : 129
         * lowPressure : 89
         * heartRate : 0
         */

        private int timestamp;
        private int highPressure;
        private int lowPressure;
        private int heartRate;

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getHighPressure() {
            return highPressure;
        }

        public void setHighPressure(int highPressure) {
            this.highPressure = highPressure;
        }

        public int getLowPressure() {
            return lowPressure;
        }

        public void setLowPressure(int lowPressure) {
            this.lowPressure = lowPressure;
        }

        public int getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }
    }

    public static class HeartRateSamplesBean {
        /**
         * timestamp : 1520581182
         * rate : 70
         */

        private int timestamp;
        private int rate;

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }
    }
}
