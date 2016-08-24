package org.htwkvisu.gui.interpolate;

import javafx.scene.paint.Color;

public class BiCubicInterpolationStrategy implements InterpolationStrategy {

    private static double calcCubicValue(double[] line, double val) {
        return line[1] + 0.5 * val * (line[2] - line[0] + val * (2.0 * line[0] - 5.0 * line[1] + 4.0 * line[2] - line[3] + val * (3.0 * (line[1] - line[2]) + line[3] - line[0])));
    }

    private static double calcBicubicValue(double[][] matrix, double x, double y) {
        double[] calcedLines = new double[4];

        calcedLines[0] = calcCubicValue(matrix[0], y);
        calcedLines[1] = calcCubicValue(matrix[1], y);
        calcedLines[2] = calcCubicValue(matrix[2], y);
        calcedLines[3] = calcCubicValue(matrix[3], y);

        double val = calcCubicValue(calcedLines, x);

        if (val <= 0) {
            return 0;
        }

        if (val >= 1) {
            return 1;
        }

        return val;
    }

    private Color interpolateCubic(Color[] cols, int xSize, int ySize, int x, int y, float xNorm, float yNorm) {

        final int[][] indices = calcColorIndices(xSize, ySize, x, y);
        return Color.color(
                calcBicubicValue(new double[][]{
                        {cols[indices[0][0]].getRed(), cols[indices[0][1]].getRed(), cols[indices[0][2]].getRed(), cols[indices[0][3]].getRed()},
                        {cols[indices[1][0]].getRed(), cols[indices[1][1]].getRed(), cols[indices[1][2]].getRed(), cols[indices[1][3]].getRed()},
                        {cols[indices[2][0]].getRed(), cols[indices[2][1]].getRed(), cols[indices[2][2]].getRed(), cols[indices[2][3]].getRed()},
                        {cols[indices[3][0]].getRed(), cols[indices[3][1]].getRed(), cols[indices[3][2]].getRed(), cols[indices[3][3]].getRed()}
                }, xNorm, yNorm),
                calcBicubicValue(new double[][]{
                        {cols[indices[0][0]].getGreen(), cols[indices[0][1]].getGreen(), cols[indices[0][2]].getGreen(), cols[indices[0][3]].getGreen()},
                        {cols[indices[1][0]].getGreen(), cols[indices[1][1]].getGreen(), cols[indices[1][2]].getGreen(), cols[indices[1][3]].getGreen()},
                        {cols[indices[2][0]].getGreen(), cols[indices[2][1]].getGreen(), cols[indices[2][2]].getGreen(), cols[indices[2][3]].getGreen()},
                        {cols[indices[3][0]].getGreen(), cols[indices[3][1]].getGreen(), cols[indices[3][2]].getGreen(), cols[indices[3][3]].getGreen()}
                }, xNorm, yNorm),
                calcBicubicValue(new double[][]{
                        {cols[indices[0][0]].getBlue(), cols[indices[0][1]].getBlue(), cols[indices[0][2]].getBlue(), cols[indices[0][3]].getBlue()},
                        {cols[indices[1][0]].getBlue(), cols[indices[1][1]].getBlue(), cols[indices[1][2]].getBlue(), cols[indices[1][3]].getBlue()},
                        {cols[indices[2][0]].getBlue(), cols[indices[2][1]].getBlue(), cols[indices[2][2]].getBlue(), cols[indices[2][3]].getBlue()},
                        {cols[indices[3][0]].getBlue(), cols[indices[3][1]].getBlue(), cols[indices[3][2]].getBlue(), cols[indices[3][3]].getBlue()},
                }, xNorm, yNorm));
    }

    private int[][] calcColorIndices(int xSize, int ySize, int x, int y) {
        int lowerXOffset = 0;
        int lowerYOffset = 0;
        int upperXOffset = 0;
        int upperYOffset = 0;

        if (x == 0) {
            lowerXOffset = 1;
        } else if (x == xSize - 2) {
            upperXOffset = -1;
        }

        if (y == 0) {
            lowerYOffset = 1;
        } else if (y == ySize - 2) {
            upperYOffset = -1;
        }

        final int rowOne = (y - 1 + lowerYOffset) * xSize;
        final int rowTwo = y * xSize;
        final int rowThree = (y + 1) * xSize;
        final int rowFour = (y + 2 + upperYOffset) * xSize;
        final int colOne = x - 1 + lowerXOffset;
        final int colTwo = x;
        final int colThree = x + 1;
        final int colFour = x + 2 + upperXOffset;

        return new int[][]{
                {rowOne + colOne, rowTwo + colOne, rowThree + colOne, rowFour + colOne},
                {rowOne + colTwo, rowTwo + colTwo, rowThree + colTwo, rowFour + colTwo},
                {rowOne + colThree, rowTwo + colThree, rowThree + colThree, rowFour + colThree},
                {rowOne + colFour, rowTwo + colFour, rowThree + colFour, rowFour + colFour}
        };
    }

    @Override
    public Color interpolate(InterpolateConfig config) {
        return interpolateCubic(config.getCols(), config.getxSize(), config.getySize(), config.getX(), config.getY(), config.getxNorm()
                , 1 - config.getyNorm());
    }
}
