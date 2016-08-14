package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.htwkvisu.org.IMapDrawable;

public interface ScoringCanvas {
    /**
     * Draw scoring values in our canvas.
     */
    void drawScoringValues();

    /**
     * Draw coordinates grid of full longitudes/latitude values.
     */
    void drawGrid();

    /**
     * Add point. Needs to be refactored for more specific point information.
     *
     * @param elem Point to add
     */
    void addDrawableElement(IMapDrawable elem);

    /**
     * Draw all drawables.
     */
    void drawElements();

    /**
     * Center map to specific coordinates.
     *
     * @param center Point of map center.
     */
    void centerView(Point2D center);

    /**
     * Calculats a color for a given scoring value
     *
     * @param value the scoring value
     * @return color for scoring value
     */
    Color getColorForValue(double value);

    Point2D getCenter();

    void redraw();
}
