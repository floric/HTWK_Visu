package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

import java.util.Map;

/**
 * Interface for all POIs which have an influence for the scoring.
 */
public interface IScorable {
    Map<String, IFallOf> getCategoryFallOfs();

    Point2D getCoordinates();
}