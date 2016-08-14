package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;

public interface IScorable {
    double calculateScoreValue(Point2D pt);

    double calculateScoreValueForCustom(Point2D pt);
}
