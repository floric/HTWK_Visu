package org.htwkvisu.gui;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private final MapCanvas parent;

    public Grid(final MapCanvas parent) {
        this.parent = parent;
    }

    /**
     * Returns coordinates for samples where to calculate scoring values.
     * They are positioned in a XY-grid with pixelDensity pixels distance between each sample.
     *
     * @param pixelDensity Distance between samples in pixels
     * @return Sample positions in a grid
     */
    public List<List<Point2D>> calcGridPoints(float pixelDensity) {
        List<List<Point2D>> matrix = new ArrayList<>();

        final double coordsDistance = calcCoordDistanceFromPixelDistance(pixelDensity);

        // create sample coordinates for the currently drawn map area
        // important: to get scoring values for all shown pixels, the code will create samples around the canvas as well

        // longitude
        for (double x = parent.getLeftBottomCorner().getX() - coordsDistance; x < parent.getLeftTopCorner().getX() + coordsDistance; x = x + coordsDistance) {
            List<Point2D> currentLine = new ArrayList<>();

            // latitude
            for (double y = parent.getLeftTopCorner().getY() - coordsDistance; y < parent.getRightTopCorner().getY() + coordsDistance; y = y + coordsDistance) {
                currentLine.add(new Point2D(x, y));
            }

            matrix.add(currentLine);
        }

        return matrix;
    }

    public double calcCoordDistanceFromPixelDistance(float pixelDistance) {
        return parent.transferPixelToCoordinate(pixelDistance, 0).getY() - parent.transferPixelToCoordinate(0, 0).getY();
    }
}
