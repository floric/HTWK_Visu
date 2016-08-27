package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GridTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void calculateGridWorks() {
        ScoringConfig config = new ScoringConfig(30, 0, 10000);
        MapCanvas canvas = new MapCanvas(config);
        canvas.setScale(100000);
        canvas.setWidth(1);
        canvas.setHeight(1);
        Grid gridOne = new Grid(canvas);
        List<Point2D> pts = gridOne.calcGridPoints(1000);
        assertEquals(12, pts.size());

        canvas.setWidth(10);
        canvas.setHeight(10);
        Grid gridTwo = new Grid(canvas);
        pts = gridTwo.calcGridPoints(5);
        assertEquals(20, pts.size());
    }

    @Test
    public void calculateGridFailsOnIllegalPixelDensity() {
        ScoringConfig config = new ScoringConfig(30, 0, 0);
        MapCanvas canvas = new MapCanvas(config);
        Grid gridOne = new Grid(canvas);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("To low pixelDensity, for grid-point-calculation: 0.0");
        gridOne.calcGridPoints(0);
    }

    @Test
    public void calculateGridWorksBorderCase() {
        ScoringConfig config = new ScoringConfig(1, 0, 0);
        MapCanvas canvas = new MapCanvas(config);
        canvas.setWidth(1);
        canvas.setHeight(1);
        Grid gridOne = new Grid(canvas);
        List<Point2D> pts = gridOne.calcGridPoints(1);
        assertEquals(12, pts.size());
    }
}