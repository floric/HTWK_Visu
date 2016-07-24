package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.scoring.ScoringCalculator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AirportTest {

    private static final String NAME = "Test";
    private static final Point2D POSITION = new Point2D(0.123, -1.234);
    private Airport airport;
    public static final double TINY_DELTA = 0.0001;

    @Before
    public void setUp() throws Exception {
        airport = new Airport(NAME, POSITION);
    }

    @Test
    public void getCategoryFallOfs() throws Exception {
        airport.getCategoryFallOfs().get(ScoringCalculator.CATEGORY_INFRASTRUCTURE);
    }

    @Test
    public void getCoordinates() throws Exception {
        assertEquals(POSITION, airport.getCoordinates());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(NAME, airport.getName());
    }

    @Test
    public void getMinDrawScale() throws Exception {
        assertEquals(0, airport.getMinDrawScale(), TINY_DELTA);
    }

    @Test
    public void draw() throws Exception {
        airport.draw(null, new MapCanvas());
    }

    @Test
    public void showDuringGrab() throws Exception {
        assertEquals(true, airport.showDuringGrab());
    }

}