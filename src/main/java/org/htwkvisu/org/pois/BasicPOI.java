package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.org.IMapDrawable;

public class BasicPOI implements IMapDrawable {
    private final ScoreType type;
    private Point2D position = new Point2D(0, 0);

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

    @Override
    public void draw(GraphicsContext gc, MapCanvas canvas) {
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
