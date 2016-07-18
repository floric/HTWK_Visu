package org.htwkvisu.utils;

import javafx.geometry.Point2D;

import java.math.BigDecimal;

/**
 * mathematical utils.
 */
public class MathUtils {

    private MathUtils() {
    }

    /**
     * Round double value to String with scale digits after the dot.
     *
     * @param d     Number
     * @param scale Digits count
     * @return Formatted number with scale digits
     * @throws IllegalArgumentException
     */
    public static String roundToDecimalsAsString(double d, int scale) throws IllegalArgumentException {
        if (scale < 0) {
            throw new IllegalArgumentException("Round scale must be higher then 0!");
        }

        return new BigDecimal(String.valueOf(d)).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * Print coordinates to lon/lat coordinates format string.
     *
     * @param pt Point
     * @return String of coordinates.
     */
    public static String formatCoordinates(Point2D pt) {
        StringBuilder strBld = new StringBuilder();
        strBld.append(pt.getX() >= 0 ? "N" : "S");
        strBld.append(roundToDecimalsAsString(Math.abs(pt.getX()), 5));
        strBld.append(" ");
        strBld.append(pt.getY() >= 0 ? "E" : "W");
        strBld.append(roundToDecimalsAsString(Math.abs(pt.getY()), 5));

        return strBld.toString();
    }
}
