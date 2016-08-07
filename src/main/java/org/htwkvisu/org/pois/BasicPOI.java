package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.org.IMapDrawable;
import org.htwkvisu.scoring.IFallOf;
import org.htwkvisu.scoring.IScorable;

import java.util.HashMap;
import java.util.Map;

public class BasicPOI implements IScorable, IMapDrawable {
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
        // draw code for airports
    }

    @Override
    public boolean showDuringGrab() {
        return true;
    }

    @Override
    public Map<String, IFallOf> getCategoryFallOfs() {
        //TODO: change interface to need only the score-type, because the score-type hold all needed values
        Map<String, IFallOf> categoryFallOfs = new HashMap<>();
        categoryFallOfs.put(type.name(), type.getFallOf());
        return categoryFallOfs;
    }

    @Override
    public Point2D getCoordinates() {
        return position;
    }
}
