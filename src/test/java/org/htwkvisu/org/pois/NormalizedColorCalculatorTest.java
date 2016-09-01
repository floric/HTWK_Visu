package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.gui.ScoringConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class NormalizedColorCalculatorTest {

    private static final int SIZE_OF_NORM_VALUES = 12;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private MapCanvas canvas;
    private NormalizedColorCalculator calculator;

    /**
     * create example test-environment with standard canvas and enabled education category
     */
    @Before
    public void setUp() {
        canvas = new MapCanvas(new ScoringConfig(30, 0, 10000));
        canvas.setHeight(10);
        canvas.setWidth(10);
        Category.EDUCATION.setEnabledForCategory(true);
        Category.HEALTH.setEnabledForCategory(false);
        Category.INFRASTRUCTURE.setEnabledForCategory(false);
        calculator = new NormalizedColorCalculator(canvas);
    }

    @Test
    public void normedColorForEnabledWithColorModeWorks() throws Exception {
        final List<Point2D> grid = canvas.calculateGrid();

        Assert.assertTrue(grid.stream().map(calculator::normedColorForEnabled).anyMatch(c -> c.getRed() != 0));
        Assert.assertTrue(grid.stream().map(calculator::normedColorForEnabled).allMatch(c -> c.getGreen() == 0));
        Assert.assertTrue(grid.stream().map(calculator::normedColorForEnabled).allMatch(c -> c.getBlue() == 0));


        ScoreType.BUS.setEnabled(true);
        calculator = new NormalizedColorCalculator(canvas);
        Assert.assertTrue(grid.stream().map(calculator::normedColorForEnabled).anyMatch(c -> c.getRed() != 0));
        Assert.assertTrue(grid.stream().map(calculator::normedColorForEnabled).allMatch(c -> c.getGreen() == 0));
        Assert.assertTrue(grid.stream().map(calculator::normedColorForEnabled).anyMatch(c -> c.getBlue() != 0));
    }
}