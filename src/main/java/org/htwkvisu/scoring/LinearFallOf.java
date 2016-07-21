package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

public class LinearFallOf implements IFallOf {

    protected double maxVal = 0;
    protected double radius = 0;

    public LinearFallOf(double radius, double maxVal) {
        this.maxVal = maxVal;
        this.radius = radius;
    }

    @Override
    public double getValue(Point2D pt, Point2D sample) {
        return (1 - (pt.distance(sample) / radius)) * maxVal;
    }

    @Override
    public double getMaximumValue() {
        return maxVal;
    }
}
