package org.htwkvisu.gui;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Random;

/**
 * Canvas for map.
 *
 * @author floric
 */
public class MapCanvas extends Canvas {

    // constants
    private static final double ZOOM_SPEED = 100;
    private static final double ZOOM_MIN = 50;
    private static final double ZOOM_MAX = 1000000;

    // default: 1 real coordinate unit = 111km => 0.01 unit = 1.11km => 100px => val: 100 * 100
    private double scale = 100000;
    // default: Leipzig
    private Point2D mapCenter = new Point2D(51.343479, 12.387772);

    //TODO Use useful POI class/interface with more information.
    private LinkedList<Point2D> points = new LinkedList<>();

    // cached values for faster drawing
    private GraphicsContext gc = getGraphicsContext2D();
    private double width = 0;
    private double height = 0;
    private double coveredWidth = 0;
    private double coveredHeight = 0;
    private BoundingBox coordsBounds = new BoundingBox(0, 0, width, height);
    private int displayedPoints = 0;

    /**
     * Construct and init canvas
     */
    public MapCanvas() {
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());

        addEventHandlers();

        // test data around coordinates center
        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            addPOI(new Point2D(mapCenter.getX() + (rnd.nextDouble() - 0.5), mapCenter.getY() + (rnd.nextDouble() - 0.5)));
        }
    }

    /**
     * Add event handlers
     */
    private void addEventHandlers() {
        setOnScroll(event -> {
            double addedVal = event.getDeltaY() * (scale / ZOOM_SPEED);

            if ((scale + addedVal) > ZOOM_MIN && (scale + addedVal) < ZOOM_MAX) {
                scale += addedVal;
                redraw();
            }
        });

        //TODO add mouse drag and maybe keyboard input with WASD/directions
    }

    /**
     * Forces a full map redraw
     */
    private void redraw() {
        width = getWidth();
        height = getHeight();
        coveredWidth = width / scale;
        coveredHeight = height / scale;
        coordsBounds = new BoundingBox(mapCenter.getX() - coveredWidth / 2, mapCenter.getY() - coveredHeight / 2,
                coveredWidth, coveredHeight);

        // clear view
        gc.clearRect(0, 0, width, height);

        drawGrid();
        drawPoints();
        drawInfo();
    }

    /**
     * Draw info about canvas in the top left corner of the canvas.
     */
    private void drawInfo() {
        gc.fillText("Center: " + mapCenter.getX() + " " + mapCenter.getY(), 10, 20);
        gc.fillText("Scale: " + scale, 10, 40);
        gc.fillText("Bounds: " + coordsBounds, 10, 60);
        gc.fillText("Points displayed: " + displayedPoints, 10, 80);
    }

    /**
     * Draw coordinates grid of full longitudes/latitude values.
     */
    private void drawGrid() {
        double xPos;
        double yPos;
        gc.setStroke(Color.BLACK);

        double x = coordsBounds.getMinX();
        while (x < coordsBounds.getMaxX()) {
            xPos = transferCoordinateToPixel(new Point2D(Math.ceil(x), 0)).getX();
            gc.strokeLine(xPos, 0, xPos, height);
            x += 1;
        }

        double y = coordsBounds.getMinY();
        while (y < coordsBounds.getMaxY()) {
            yPos = transferCoordinateToPixel(new Point2D(0, Math.ceil(y))).getY();
            gc.strokeLine(0, yPos, width, yPos);
            y += 1;
        }

    }

    /**
     * Draw all points.
     */
    private void drawPoints() {
        gc.setStroke(Color.BLUE);

        displayedPoints = 0;
        for (Point2D p : points) {
            if (coordsBounds.contains(p)) {
                Point2D lclPt = transferCoordinateToPixel(p);
                gc.strokeOval(lclPt.getX() - 2.5, lclPt.getY() - 2.5, 5, 5);
                displayedPoints++;
            }
        }
    }

    /**
     * Transfers earth coordinates into map pixel coordinates.
     *
     * @param p Earth coordinates
     * @return Pixel coordinates
     */
    private Point2D transferCoordinateToPixel(Point2D p) {
        return new Point2D((p.getX() - mapCenter.getX()) * scale + width / 2,
                (p.getY() - mapCenter.getY()) * scale + height / 2);
    }

    /**
     * Center map to specific coordinates.
     *
     * @param center Point of map center.
     */
    public void centerView(Point2D center) {
        mapCenter = center;

        redraw();
    }

    /**
     * Add point. Needs to be refactored for more specific point information.
     *
     * @param pt Point to add
     */
    // TODO Use more information for POIs (like: coordinates, category, influence, name, ...)
    public void addPOI(Point2D pt) {
        points.add(pt);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
