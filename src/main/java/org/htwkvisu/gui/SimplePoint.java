package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.htwkvisu.org.IMapDrawable;

/**
 * Created by floric on 7/14/16.
 */
public class SimplePoint implements IMapDrawable {

    private Point2D pt;
    private String name;
    private double minScale;

    private static final double POINT_SIZE = 5;

    public SimplePoint(Point2D pt, String name, double minScale) {
        this.pt = pt;
        this.name = name;
        this.minScale = minScale;
    }

    @Override
    public Point2D getCoordinates() {
        return pt;
    }

    @Override
    public String getName() {
        return name;
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
