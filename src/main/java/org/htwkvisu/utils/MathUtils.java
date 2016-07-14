package org.htwkvisu.utils;

import java.math.BigDecimal;

/**
 * Created by floric on 7/14/16.
 */
public class MathUtils {

    private MathUtils() {

    }

    public static String roundToDecimalsAsString(double d, int scale) throws IllegalArgumentException {
        if (scale < 0) {
            throw new IllegalArgumentException("Round scale must be higher then 0!");
        }

        return new BigDecimal(String.valueOf(d)).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

}
