package org.htwkvisu.scoring;

import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class ConstantFallOfTest {
    private ConstantFallOf fallOf;
    private static double TINY_DELTA = 0.0001;

    @Before
    public void setUp() throws Exception {
        fallOf = new ConstantFallOf(1.0);
    }

    @Test
    public void getValue() throws Exception {
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            double val = rnd.nextDouble();

            Point2D pt = new Point2D(rnd.nextDouble(), rnd.nextDouble());
            Point2D sample = new Point2D(rnd.nextDouble(), rnd.nextDouble());

            fallOf = new ConstantFallOf(val);
            Assert.assertEquals(val, fallOf.getValue(pt, sample), TINY_DELTA);
        }
    }

    @Test
    public void getMaximumValue() throws Exception {
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            double val = rnd.nextDouble();

            fallOf = new ConstantFallOf(val);
            Assert.assertEquals(val, fallOf.getMaximumValue(), TINY_DELTA);
        }
    }

}