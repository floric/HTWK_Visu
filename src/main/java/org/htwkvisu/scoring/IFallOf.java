package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

/**
 * Interface for fallof functions.
 */
public interface IFallOf {
    double getValue(Point2D pt, Point2D sample);

    double getMaximumValue();

    double getRadius();
}
