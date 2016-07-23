package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

import java.util.HashMap;

/*
* Interface for all POIs which have an influence for the scoring.
 */
public interface IScorable {
    HashMap<String, IFallOf> getCategoryFallOfs();

    Point2D getPosition();
}