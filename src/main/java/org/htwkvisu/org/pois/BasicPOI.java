package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.org.IMapDrawable;

public class BasicPOI implements IMapDrawable {
    private final ScoreType type;
    private Point2D position = new Point2D(0, 0);
    private static final double POINT_SIZE = 3;

    public BasicPOI(ScoreType type, Point2D position) {
        this.type = type;
        this.position = position;
    }

    @Override
    public String getName() {
        return type.name();
    }

    @Override
    public double getMinDrawScale() {
        return 0;
    }

    /**
     * Test Implementierung für BASIC_POI zeichnen
     */
    @Override
    public void draw(GraphicsContext gc, MapCanvas canvas) {
        //TODO:Discuss how this should be displayed
        Point2D lclPt = canvas.transferCoordinateToPixel(position);
        //gc.setFill(Color.BLACK);
        //gc.fillText(type.toString(), lclPt.getX(), lclPt.getY() - 10);
        gc.setFill(Color.WHEAT);
        gc.fillOval(lclPt.getX() - POINT_SIZE / 2, lclPt.getY() - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
    }

    @Override
    public boolean showDuringGrab() {
        return true;
    }

    @Override
    public Point2D getCoordinates() {
        return position;
    }
}
