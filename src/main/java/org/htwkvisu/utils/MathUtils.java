package org.htwkvisu.utils;

import javafx.geometry.Point2D;

import java.math.BigDecimal;

/**
 * mathematical utils.
 */
public class MathUtils {

    // default: 1 real coordinate unit = 111km => 0.01 unit = 1.11km => 100px => val: 100 * 100
    public static final double KM_PER_COORD = 111;

    private MathUtils() {
    }

    /**
     * Round double value to String with scale digits after the dot.
     *
     * @param d     Number
     * @param scale Digits count
     * @return Formatted number with scale digits
     * @throws IllegalArgumentException Exception
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

    /**
     * Return distance from units converted in kilometres.
     *
     * @param distInUnits Distance in units
     * @return Distance in Kilometres
     */
    public static double convertUnitsToKilometres(double distInUnits) {
        return distInUnits * KM_PER_COORD;
    }

    /**
     * Return distance from kilometres converted in units.
     *
     * @param distInKMs Distance in kilometres
     * @return Distance in Units
     */
    public static double convertKilometresToUnits(double distInKMs) {
        return distInKMs / KM_PER_COORD;
    }

    public static double getCubicValue(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    public static double getBicubicValue(double[][] p, double x, double y) {
        double[] arr = new double[4];

        arr[0] = getCubicValue(p[0], y);
        arr[1] = getCubicValue(p[1], y);
        arr[2] = getCubicValue(p[2], y);
        arr[3] = getCubicValue(p[3], y);

        double val = getCubicValue(arr, x);

        if (val <= 0) {
            return 0;
        }

        if (val >= 1) {
            return 1;
        }

        return val;
    }
}
