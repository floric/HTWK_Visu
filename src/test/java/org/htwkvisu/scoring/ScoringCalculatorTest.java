package org.htwkvisu.scoring;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ScoringCalculatorTest {

    private ScoringCalculator calculator;
    private static final double TINY_DELTA = 0.0001;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        calculator = new ScoringCalculator();
    }

    @Test
    public void addPOI() throws Exception {
        assertEquals(0, calculator.getPOIs().size());
        calculator.addPOI(new IScorable() {
            @Override
            public Map<String, IFallOf> getCategoryFallOfs() {
                return new HashMap<>();
            }

            @Override
            public Point2D getCoordinates() {
                return new Point2D(0, 0);
            }
        });
        assertEquals(1, calculator.getPOIs().size());
    }

    @Test
    public void addCategory() throws Exception {
        String category = "Test";

        assertEquals(0, calculator.getCategories().size());
        calculator.addCategory(category, 1);
        assertEquals(1, calculator.getCategories().size());
        calculator.addCategory(category, 1);
        assertEquals(1, calculator.getCategories().size());
        assertEquals(category, calculator.getCategories().get(0));
    }

    @Test
    public void setCategoryWeight() throws Exception {
        String category = "Test";
        double oldWeight = 1;
        double newWeight = 2;

        calculator.addCategory(category, oldWeight);
        assertEquals(oldWeight, calculator.getCategoryWeight(category), TINY_DELTA);
        calculator.setCategoryWeight(category, newWeight);
        assertEquals(newWeight, calculator.getCategoryWeight(category), TINY_DELTA);

        exception.expect(IllegalArgumentException.class);
        calculator.setCategoryWeight("123", 1);
    }

    @Test
    public void getCategoryWeight() throws Exception {
        exception.expect(IllegalArgumentException.class);
        calculator.getCategoryWeight("bla");
    }

    @Test
    public void removeCategory() throws Exception {
        String category = "Test";

        calculator.addCategory(category, 1);
        assertEquals(1, calculator.getCategories().size());
        calculator.removeCategory(category);
        assertEquals(0, calculator.getCategories().size());
    }

    @Test
    public void resetCategories() throws Exception {

        calculator.addCategory("Test", 1);
        calculator.addCategory("Bla", 1);
        calculator.addCategory("123", 1);
        assertEquals(true, !calculator.getCategories().isEmpty());
        calculator.resetCategories();
        assertEquals(0, calculator.getCategories().size());
    }

    @Test
    public void calculateValue() throws Exception {
        String category = "Test";
        double constantVal = 10;

        HashMap<String, Double> values = calculator.calculateValue(new Point2D(0, 0));
        assertEquals(0, values.size());

        calculator.addCategory(category, 1);
        values = calculator.calculateValue(new Point2D(0, 0));
        assertEquals(0, values.get(category), TINY_DELTA);
        assertEquals(1, values.size());

        calculator.addPOI(new IScorable() {
            @Override
            public Map<String, IFallOf> getCategoryFallOfs() {
                HashMap<String, IFallOf> fallofs = new HashMap<>();
                fallofs.put(category, new ConstantFallOf(1, constantVal));

                return fallofs;
            }

            @Override
            public Point2D getCoordinates() {
                return new Point2D(0, 0);
            }
        });

        values = calculator.calculateValue(new Point2D(0, 0));
        assertEquals(constantVal, values.get(category), TINY_DELTA);
    }

}