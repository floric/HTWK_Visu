package org.htwkvisu.gui.interpolate;

import javafx.scene.paint.Color;

public class BiCubicInterpolationStrategy implements InterpolationStrategy {

    private static final int DIMENSION = 4;

    private static double calcCubicValue(double[] line, double val) {
        return line[1] + 0.5 * val * (line[2] - line[0] + val * (2.0 * line[0] - 5.0 * line[1] + 4.0 * line[2] - line[3] + val * (3.0 * (line[1] - line[2]) + line[3] - line[0])));
    }

    private static double calcBicubicValue(double[][] matrix, double x, double y) {
        double[] calcedLines = new double[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            calcedLines[i] = calcCubicValue(matrix[i], y);
        }

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
        final int[][] indices = calcColorIndices(xSize, ySize, x, y - 1);
        final double[][] red = new double[DIMENSION][DIMENSION];
        final double[][] green = new double[DIMENSION][DIMENSION];
        final double[][] blue = new double[DIMENSION][DIMENSION];

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                red[i][j] = cols[indices[i][j]].getRed();
                green[i][j] = cols[indices[i][j]].getGreen();
                blue[i][j] = cols[indices[i][j]].getBlue();
            }
        }

        return Color.color(
                calcBicubicValue(red, xNorm, yNorm),
                calcBicubicValue(green, xNorm, yNorm),
                calcBicubicValue(blue, xNorm, yNorm));
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
