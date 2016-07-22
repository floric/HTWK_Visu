package org.htwkvisu.scoring;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ScoringCalculatorTest {

    private ScoringCalculator calculator;
    private static final double TINY_DELTA = 0.0001;

    @Before
    public void setUp() throws Exception {
        calculator = new ScoringCalculator();
    }

    @Test
    public void addPOI() throws Exception {
        assertEquals(0, calculator.getPois().size());
        calculator.addPOI(new IScorable() {
            @Override
            public HashMap<String, IFallOf> getValues() {
                return new HashMap<String, IFallOf>();
            }

            @Override
            public Point2D getPosition() {
                return new Point2D(0, 0);
            }
        });
        assertEquals(1, calculator.getPois().size());
    }

    @Test
    public void addCategory() throws Exception {
        String category = "Test";

        assertEquals(0, calculator.getCategories().size());
        calculator.addCategory(category);
        assertEquals(1, calculator.getCategories().size());
        assertEquals(category, calculator.getCategories().get(0));
    }

    @Test
    public void removeCategory() throws Exception {
        String category = "Test";

        calculator.addCategory(category);
        assertEquals(1, calculator.getCategories().size());
        calculator.removeCategory(category);
        assertEquals(0, calculator.getCategories().size());
    }

    @Test
    public void resetCategories() throws Exception {

        calculator.addCategory("Test");
        calculator.addCategory("Bla");
        calculator.addCategory("123");
        assertEquals(true, !calculator.getCategories().isEmpty());
        calculator.resetCategories();
        assertEquals(0, calculator.getCategories().size());
    }

    @Test
    public void calculateValue() throws Exception {
        String category = "Test";
        double constantVal = 10;

        calculator.addCategory(category);
        HashMap<String, Double> values = calculator.calculateValue(new Point2D(0, 0));
        assertEquals(0, values.get(category), TINY_DELTA);

        calculator.addPOI(new IScorable() {
            @Override
            public HashMap<String, IFallOf> getValues() {
                HashMap<String, IFallOf> fallofs = new HashMap<>();
                fallofs.put(category, new ConstantFallOf(1, constantVal));

                return fallofs;
            }

            @Override
            public Point2D getPosition() {
                return new Point2D(0, 0);
            }
        });
        values = calculator.calculateValue(new Point2D(0, 0));
        assertEquals(constantVal, values.get(category), TINY_DELTA);
    }

}