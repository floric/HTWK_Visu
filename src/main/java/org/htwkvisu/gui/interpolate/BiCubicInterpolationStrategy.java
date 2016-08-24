package org.htwkvisu.gui.interpolate;

import javafx.scene.paint.Color;

public class BiCubicInterpolationStrategy implements InterpolationStrategy {

    private static double getCubicValue(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    private static double getBicubicValue(double[][] p, double x, double y) {
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

    private Color interpolateCubic(Color[] cols, int xSize, int y, int x, float xNorm, float yNorm) {

        final int[][] indices = {
                {(y + 1) * xSize + x + 1, y * xSize + x + 1, (y - 1) * xSize + x + 1, (y - 2) * xSize + x + 1},
                {(y + 1) * xSize + x, y * xSize + x, (y - 1) * xSize + x, (y - 2) * xSize + x},
                {(y + 1) * xSize + x - 1, y * xSize + x - 1, (y - 1) * xSize + x - 1, (y - 2) * xSize + x - 1},
                {(y + 1) * xSize + x - 2, y * xSize + x - 2, (y - 1) * xSize + x - 2, (y - 2) * xSize + x - 2}
        };
        return Color.color(
                getBicubicValue(new double[][]{
                        {cols[indices[0][0]].getRed(), cols[indices[0][1]].getRed(), cols[indices[0][2]].getRed(), cols[indices[0][3]].getRed()},
                        {cols[indices[1][0]].getRed(), cols[indices[1][1]].getRed(), cols[indices[1][2]].getRed(), cols[indices[1][3]].getRed()},
                        {cols[indices[2][0]].getRed(), cols[indices[2][1]].getRed(), cols[indices[2][2]].getRed(), cols[indices[2][3]].getRed()},
                        {cols[indices[3][0]].getRed(), cols[indices[3][1]].getRed(), cols[indices[3][2]].getRed(), cols[indices[3][3]].getRed()}
                }, xNorm, yNorm),
                getBicubicValue(new double[][]{
                        {cols[indices[0][0]].getGreen(), cols[indices[0][1]].getGreen(), cols[indices[0][2]].getGreen(), cols[indices[0][3]].getGreen()},
                        {cols[indices[1][0]].getGreen(), cols[indices[1][1]].getGreen(), cols[indices[1][2]].getGreen(), cols[indices[1][3]].getGreen()},
                        {cols[indices[2][0]].getGreen(), cols[indices[2][1]].getGreen(), cols[indices[2][2]].getGreen(), cols[indices[2][3]].getGreen()},
                        {cols[indices[3][0]].getGreen(), cols[indices[3][1]].getGreen(), cols[indices[3][2]].getGreen(), cols[indices[3][3]].getGreen()}
                }, xNorm, yNorm),
                getBicubicValue(new double[][]{
                        {cols[indices[0][0]].getBlue(), cols[indices[0][1]].getBlue(), cols[indices[0][2]].getBlue(), cols[indices[0][3]].getBlue()},
                        {cols[indices[1][0]].getBlue(), cols[indices[1][1]].getBlue(), cols[indices[1][2]].getBlue(), cols[indices[1][3]].getBlue()},
                        {cols[indices[2][0]].getBlue(), cols[indices[2][1]].getBlue(), cols[indices[2][2]].getBlue(), cols[indices[2][3]].getBlue()},
                        {cols[indices[3][0]].getBlue(), cols[indices[3][1]].getBlue(), cols[indices[3][2]].getBlue(), cols[indices[3][3]].getBlue()},
                }, xNorm, yNorm));
    }

    public static double interpolateCubicFloat(double l, double lB, double r, double rA, double x) {
        double val = l + 0.5 * x * (r - lB + x * (2.0 * lB - 5.0 * l + 4.0 * r - rA + x * (3.0 * (l - r) + rA - lB)));
        if (val > 0 && val < 1) {
            return val;
        } else if (val <= 0) {
            return 0;
        } else {
            return 1;
        }

    }

    /*private float interpolateCubicFloat(float l, float lm, float r, float rm, float t) {
        float t2 = t * t;
        float t3 = t2 * t;

        return (2 * t3 - 3 * t2) * l + (t3 - 2 * t2 + t) * lm + (-2 * t3 + 3 * t2) * r + (t3 - t2) * rm;
    }*/

    @Override
    public Color interpolate(InterpolateConfig config) {
        return interpolateCubic(config.getCols(), config.getxSize(), config.getY(), config.getX(), 1 - config.getxNorm()
                , config.getyNorm());
    }
}
