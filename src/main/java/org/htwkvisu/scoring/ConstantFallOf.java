package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

public class ConstantFallOf implements IFallOf {
    protected double val = 0;
    protected double radius = 0;

    public ConstantFallOf(double radius, double val) {
        this.val = val;
        this.radius = radius;
    }

    @Override
    public double getValue(Point2D pt, Point2D sample) {
        return val;
    }

    @Override
    public double getMaximumValue() {
        return val;
    }

    @Override
    public double getRadius() {
        return radius;
    }
}
