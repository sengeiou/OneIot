package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-5-31.
 */

public class ConvertUnitBean {
    private int unit;
    private Double totalDistance;
    private Float distanceTarget;
    private Double weightTarget;
    private Double weight;
    private Double height;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Float getDistanceTarget() {
        return distanceTarget;
    }

    public void setDistanceTarget(Float distanceTarget) {
        this.distanceTarget = distanceTarget;
    }

    public Double getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(Double weightTarget) {
        this.weightTarget = weightTarget;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
