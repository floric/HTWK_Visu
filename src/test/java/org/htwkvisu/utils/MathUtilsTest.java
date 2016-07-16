package org.htwkvisu.utils;

import javafx.geometry.Point2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class for Utils.
 * Maybe refactored to general Utils, not just Math specific?!
 *
 * Created by floric on 7/14/16.
 */
public class MathUtilsTest {
    @Test
    public void testFormatCoordinates() throws Exception {
        assertEquals(MathUtils.formatCoordinates(new Point2D(0, 0)), "N0.00000 E0.00000");
        assertEquals(MathUtils.formatCoordinates(new Point2D(1, 1)), "N1.00000 E1.00000");
        assertEquals(MathUtils.formatCoordinates(new Point2D(0.1, 0.1)), "N0.10000 E0.10000");
        assertEquals(MathUtils.formatCoordinates(new Point2D(-1.23, -1.23)), "S1.23000 W1.23000");
    }

    @Test
    public void testRoundToDecimalsAsString() throws Exception {

        // no elements after dot
        assertEquals(MathUtils.roundToDecimalsAsString(1, 0), "1");
        assertEquals(MathUtils.roundToDecimalsAsString(123, 0), "123");
        assertEquals(MathUtils.roundToDecimalsAsString(123.1, 0), "123");
        assertEquals(MathUtils.roundToDecimalsAsString(-123, 0), "-123");

        // elements after dot
        assertEquals(MathUtils.roundToDecimalsAsString(1.1, 1), "1.1");
        assertEquals(MathUtils.roundToDecimalsAsString(123.0, 1), "123.0");
        assertEquals(MathUtils.roundToDecimalsAsString(123.1, 1), "123.1");
        assertEquals(MathUtils.roundToDecimalsAsString(-123.1, 1), "-123.1");
        assertEquals(MathUtils.roundToDecimalsAsString(1.1, 5), "1.10000");

        // rounding
        assertEquals(MathUtils.roundToDecimalsAsString(1.5, 0), "2");
        assertEquals(MathUtils.roundToDecimalsAsString(-1.5, 0), "-2");
        assertEquals(MathUtils.roundToDecimalsAsString(-1.49, 0), "-1");

        // exceptions
        try {
            assertEquals(MathUtils.roundToDecimalsAsString(1, -1), "1");
            assertEquals(MathUtils.roundToDecimalsAsString(1, -2), "1");
            assert (false);
        } catch (IllegalArgumentException ex) {
            assert (true);
        }
    }

}