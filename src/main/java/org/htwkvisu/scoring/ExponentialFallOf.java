package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

public class ExponentialFallOf implements IFallOf {

    private double maxVal = 0;
    private double radius = 0;
    private double exp = 0;

    public ExponentialFallOf(double radius, double maxVal, double exp) {
        this.maxVal = maxVal;
        this.radius = radius;
        this.exp = exp;
    }

    @Override
    public double getValue(Point2D pt, Point2D sample) {
        return (1 - Math.pow(pt.distance(sample) / radius, exp)) * maxVal;
    }

    @Override
    public double getMaximumValue() {
        return maxVal;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getExp() {
        return exp;
    }

    @Override
    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void setExp(double exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "ExponentialFallOf";
    }
}
