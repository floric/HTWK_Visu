package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

public class ExponentialFallOf implements IFallOf {

    protected double maxVal = 0;
    protected double radius = 0;
    protected double exp = 0;

    public ExponentialFallOf(double radius, double maxVal, double exp) {
        this.maxVal = maxVal;
        this.radius = radius;
        this.exp = exp;
    }

    @Override
    public double getValue(Point2D pt, Point2D sample) {
        return Math.pow(1 - (pt.distance(sample) / radius), exp) * maxVal;
    }

    @Override
    public double getMaximumValue() {
        return maxVal;
    }
}
