package org.htwkvisu.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by floric on 7/14/16.
 */
public class MathUtilsTest {

    private double eps = 0.0001;

    @Test
    public void testRoundToDecimalsAsString() throws Exception {

        // no elements after dot
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(1, 0), "1");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(123, 0), "123");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(123.1, 0), "123");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(-123, 0), "-123");

        // elements after dot
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(1.1, 1), "1.1");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(123.0, 1), "123.0");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(123.1, 1), "123.1");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(-123.1, 1), "-123.1");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(1.1, 5), "1.10000");

        // rounding
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(1.5, 0), "2");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(-1.5, 0), "-2");
        Assert.assertEquals(MathUtils.roundToDecimalsAsString(-1.49, 0), "-1");

        // exceptions
        try {
            Assert.assertEquals(MathUtils.roundToDecimalsAsString(1, -1), "1");
            Assert.assertEquals(MathUtils.roundToDecimalsAsString(1, -2), "1");
            assert (false);
        } catch (IllegalArgumentException ex) {
            assert (true);
        }
    }

}