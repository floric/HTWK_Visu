package org.htwkvisu.utils;

import javafx.geometry.Point2D;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MathUtilsTest {

    public static final double TINY_DELTA = 0.0001;

    @Test
    public void convertUnitsToKilometres() throws Exception {

    }

    @Test
    public void convertKilometresToUnits() throws Exception {
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            double val = rnd.nextDouble() * 100;
            assertEquals(val, MathUtils.convertKilometresToUnits(MathUtils.convertUnitsToKilometres(val)), TINY_DELTA);
        }

        assertEquals(1, MathUtils.convertKilometresToUnits(MathUtils.KM_PER_COORD), TINY_DELTA);
        assertEquals(MathUtils.KM_PER_COORD, MathUtils.convertUnitsToKilometres(1), TINY_DELTA);

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testFormatCoordinates() throws Exception {
        assertEquals(MathUtils.formatCoordinates(new Point2D(0, 0)), "N0.00000 E0.00000");
        assertEquals(MathUtils.formatCoordinates(new Point2D(1, 1)), "N1.00000 E1.00000");
        assertEquals(MathUtils.formatCoordinates(new Point2D(0.1, 0.1)), "N0.10000 E0.10000");
        assertEquals(MathUtils.formatCoordinates(new Point2D(-1.23, -1.23)), "S1.23000 W1.23000");
    }

    @Test
    public void roundWorksInRountUpOrder() {
        assertEquals(MathUtils.roundToDecimalsAsString(1.5, 0), "2");
        assertEquals(MathUtils.roundToDecimalsAsString(-1.5, 0), "-2");
        assertEquals(MathUtils.roundToDecimalsAsString(-1.49, 0), "-1");
    }

    @Test
    public void roundWithIntsAndZeroScaleSucceeds() {
        assertEquals(MathUtils.roundToDecimalsAsString(1, 0), "1");
        assertEquals(MathUtils.roundToDecimalsAsString(123, 0), "123");
        assertEquals(MathUtils.roundToDecimalsAsString(-123, 0), "-123");
    }

    @Test
    public void roundWithDoubleAndZeroScaleSucceeds() {
        assertEquals(MathUtils.roundToDecimalsAsString(1.1, 0), "1");
        assertEquals(MathUtils.roundToDecimalsAsString(123.0, 0), "123");
        assertEquals(MathUtils.roundToDecimalsAsString(123.1, 0), "123");
        assertEquals(MathUtils.roundToDecimalsAsString(-123.1, 0), "-123");
    }

    @Test
    public void roundWithIntsAndScaleSucceeds() {
        assertEquals(MathUtils.roundToDecimalsAsString(1, 1), "1.0");
        assertEquals(MathUtils.roundToDecimalsAsString(123, 1), "123.0");
        assertEquals(MathUtils.roundToDecimalsAsString(123, 4), "123.0000");
        assertEquals(MathUtils.roundToDecimalsAsString(-123, 2), "-123.00");
    }

    @Test
    public void roundWithDoubleAndScaleSucceeds() {
        assertEquals(MathUtils.roundToDecimalsAsString(1.1, 1), "1.1");
        assertEquals(MathUtils.roundToDecimalsAsString(123.0, 1), "123.0");
        assertEquals(MathUtils.roundToDecimalsAsString(123.1, 1), "123.1");
        assertEquals(MathUtils.roundToDecimalsAsString(-123.1, 1), "-123.1");
        assertEquals(MathUtils.roundToDecimalsAsString(1.1, 5), "1.10000");
    }

    @Test
    public void scaleUnderZeroFails() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Round scale must be higher then 0!");

        assertEquals(MathUtils.roundToDecimalsAsString(1, -1), "1");
    }
}