package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.org.IMapDrawable;
import org.htwkvisu.scoring.ExponentialFallOf;
import org.htwkvisu.scoring.IFallOf;
import org.htwkvisu.scoring.IScorable;
import org.htwkvisu.scoring.ScoringCalculator;
import org.htwkvisu.utils.MathUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Airport: drawable and scorable
 */
public class Airport implements IScorable, IMapDrawable {

    private static final double INFLUENCE_RADIUS = MathUtils.convertKilometresToUnits(100);
    private static final double INFLUENCE_MAX_VAL = 100;
    private static final double INFLUENCE_EXP = 3;

    private String name = "Airport";
    private Point2D position = new Point2D(0, 0);

    public Airport(String name, Point2D position) {
        this.name = name;
        this.position = position;
    }

    @Override
    public Map<String, IFallOf> getCategoryFallOfs() {
        Map<String, IFallOf> fallOfs = new HashMap<>();
        fallOfs.put(ScoringCalculator.CATEGORY_INFRASTRUCTURE, new ExponentialFallOf(INFLUENCE_RADIUS, INFLUENCE_MAX_VAL, INFLUENCE_EXP));

        return fallOfs;
    }

    @Override
    public Point2D getCoordinates() {
        return position;
    }

    @Override
    public String getName() {
        return name;
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
}
