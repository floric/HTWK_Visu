package org.htwkvisu.org;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.htwkvisu.gui.MapCanvas;

/**
 * Created by floric on 7/14/16.
 */
public interface IMapDrawable {
    Point2D getCoordinates();

    String getName();

    double getMinDrawScale();

    void draw(GraphicsContext gc, MapCanvas canvas);
}
