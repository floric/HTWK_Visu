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
        calculator = new NormalizedColorCalculator(canvas, true);
    }

    @Test
    public void normEnabledWorks() throws Exception {
        final List<Double> normedValues = calculator.normEnabled();

        Assert.assertEquals(SIZE_OF_NORM_VALUES, normedValues.size());
        normedValues.stream().allMatch(x -> x < 256 && x >= 0);

        Category.INFRASTRUCTURE.setEnabledForCategory(true);
        final List<Double> normedValuesAfter = calculator.normEnabled();
        Assert.assertEquals(SIZE_OF_NORM_VALUES, normedValuesAfter.size());
        Assert.assertNotEquals(normedValues, normedValuesAfter);
        Assert.assertNotEquals(normedValues.get(0), normedValuesAfter.get(0));
    }

    @Test
    public void calculateColorWithColorModeWorks() throws Exception {
        final List<Point2D> grid = canvas.calculateGrid();

        Assert.assertTrue(grid.stream().map(calculator::calculateColor).anyMatch(c -> c.getRed() != 0));
        Assert.assertTrue(grid.stream().map(calculator::calculateColor).allMatch(c -> c.getGreen() == 0));
        Assert.assertTrue(grid.stream().map(calculator::calculateColor).allMatch(c -> c.getBlue() == 0));

        ScoreType.BUS.setEnabled(true);
        Assert.assertTrue(grid.stream().map(calculator::calculateColor).anyMatch(c -> c.getRed() != 0));
        Assert.assertTrue(grid.stream().map(calculator::calculateColor).allMatch(c -> c.getGreen() == 0));
        Assert.assertTrue(grid.stream().map(calculator::calculateColor).anyMatch(c -> c.getBlue() != 0));
    }
}