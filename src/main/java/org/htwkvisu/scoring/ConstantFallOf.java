package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

public class ConstantFallOf implements IFallOf {
    private double maxVal = 0;
    private double radius = 0;

    public ConstantFallOf(double radius, double maxVal) {
        this.maxVal = maxVal;
        this.radius = radius;
    }

    @Override
    public double getValue(Point2D pt, Point2D sample) {
        return maxVal;
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
        return 0;
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
    }

    @Override
    public IFallOf switchToNext() {
        return new LinearFallOf(this.getRadius(), this.getMaximumValue());
    }

    @Override
    public String toString() {
        return "constant";
    }
}
