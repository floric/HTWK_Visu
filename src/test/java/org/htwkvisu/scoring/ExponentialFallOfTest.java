package org.htwkvisu.scoring;

import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class ExponentialFallOfTest {
    private ExponentialFallOf fallOf;
    private static double TINY_DELTA = 0.0001;

    @Before
    public void setUp() throws Exception {
        fallOf = new ExponentialFallOf(10, 5, 2);
    }

    @Test
    public void getValue() throws Exception {
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            double val = rnd.nextDouble();

            double xVal = rnd.nextDouble();
            double yVal = rnd.nextDouble();
            double distance = rnd.nextDouble();

            Point2D pt = new Point2D(xVal, yVal);
            Point2D sample = new Point2D(xVal, yVal + distance);

            fallOf = new ExponentialFallOf(pt.distance(sample), val, rnd.nextDouble());
            Assert.assertEquals(val, fallOf.getValue(pt, pt), TINY_DELTA);
            Assert.assertEquals(0, fallOf.getValue(pt, sample), TINY_DELTA);
        }
    }

    @Test
    public void getMaximumValue() throws Exception {
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            double val = rnd.nextDouble();

            fallOf = new ExponentialFallOf(10, val, 2);
            Assert.assertEquals(val, fallOf.getMaximumValue(), TINY_DELTA);
        }
    }
}