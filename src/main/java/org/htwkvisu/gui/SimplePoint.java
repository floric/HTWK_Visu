package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.htwkvisu.org.IMapDrawable;

/**
 * Sample class for simple points.
 */
public class SimplePoint implements IMapDrawable {

    private Point2D pt;
    private double minScale;

    private static final double POINT_SIZE = 5;

    public SimplePoint(Point2D pt, double minScale) {
        this.pt = pt;
        this.minScale = minScale;
    }

    @Override
    public Point2D getCoordinates() {
        return pt;
    }

    @Override
    public String getName() {
        return "Point";
    }

    @Override
    public double getMinDrawScale() {
        return minScale;
    }

    @Override
    public void draw(GraphicsContext gc, MapCanvas canvas) {
        Point2D lclPt = canvas.transferCoordinateToPixel(pt);
        gc.strokeOval(lclPt.getX() - POINT_SIZE / 2, lclPt.getY() - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
    }

    @Override
    public boolean showDuringGrab() {
        return false;
    }
}
