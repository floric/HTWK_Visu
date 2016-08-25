package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.hibernate.mapping.Array;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.org.IMapDrawable;

public class BasicPOI implements IMapDrawable {
    private final ScoreType type;
    private final Point2D position;
    private static final double POINT_SIZE = 2;
    private final Color color;

    public BasicPOI(ScoreType type, Point2D position) {
        this.type = type;
        this.position = position;
        this.color = calcTypeColor();
    }

    private Color calcTypeColor() {
        Color tmpColor = Color.BLACK;
        for (Category cat : Category.values()) {
            for (ScoreType scoreType : cat.getTypes()) {
                if (type == scoreType) {
                    tmpColor = getColorForCategory(cat);
                }
            }
        }
        return tmpColor;
    }

    private Color getColorForCategory(Category cat) {
        Color tmpColor;
        if (cat == Category.EDUCATION) {
            tmpColor = Color.RED;
        } else if (cat == Category.HEALTH) {
            tmpColor = Color.GREEN;
        } else {
            tmpColor = Color.BLUE;
        }
        return tmpColor;
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
        Point2D lclPt = canvas.transferCoordinateToPixel(position);
        gc.setFill(color);
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
