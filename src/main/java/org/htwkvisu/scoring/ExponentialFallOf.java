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
}
