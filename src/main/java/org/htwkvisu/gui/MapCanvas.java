package org.htwkvisu.gui;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.htwkvisu.org.IMapDrawable;
import org.htwkvisu.utils.MathUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Canvas for map.
 *
 * @author floric
 */
public class MapCanvas extends Canvas {

    // constants
    private static final double ZOOM_SPEED = 100;
    private static final double ZOOM_MIN = 150;
    private static final double ZOOM_MAX = 1000000;
    private static final MouseButton DRAG_BUTTON = MouseButton.MIDDLE;
    private static final double KM_PER_COORD = 111;

    // default: 1 real coordinate unit = 111km => 0.01 unit = 1.11km => 100px => val: 100 * 100
    private double scale = 100000;

    // default: Leipzig
    private Point2D mapCenter = new Point2D(51.343479, 12.387772);

    private LinkedList<IMapDrawable> drawables = new LinkedList<>();

    // cached values for faster drawing
    private GraphicsContext gc = getGraphicsContext2D();
    private double tmpWidth = 0;
    private double tmpHeight = 0;
    private double coveredWidth = 0;
    private double coveredHeight = 0;
    private double widthDistance = 0;
    private double heightDistance = 0;
    private BoundingBox coordsBounds = new BoundingBox(0, 0, tmpWidth, tmpHeight);
    private int displayedElems = 0;

    // canvas dragging
    private boolean isDragging = false;
    private double dragX = 0;
    private double dragY = 0;

    /**
     * Construct and init canvas
     */
    public MapCanvas() {
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());

        addEventHandlers();

        // test data around coordinates center
        Random rnd = new Random();
        for (int i = 0; i < 999999; i++) {
            addDrawableElement(
                    new SimplePoint(new Point2D(51 + rnd.nextDouble(), 13 + rnd.nextDouble()), rnd.nextDouble() * 40000)
            );
        }

        // add test cities
        addDrawableElement(new City(new Point2D(51.340333, 12.37475), "Leipzig", 0));
        addDrawableElement(new City(new Point2D(51.482778, 11.97), "Halle (Saale)", 0));
        addDrawableElement(new City(new Point2D(50.927222, 11.586111), "Jena", 0));
        addDrawableElement(new City(new Point2D(50.983333, 11.033333), "Erfurt", 0));
    }

    /**
     * Get center of map.
     *
     * @return Point2D Center of map
     */
    public Point2D getMapCenter() {
        return mapCenter;
    }

    /**
     * Get coordinates bounds currently drawn on map.
     *
     * @return BoundingBox Coordinates Boundingbox
     */
    public BoundingBox getCoordsBounds() {
        return coordsBounds;
    }


    /**
     * Get the map scale.
     *
     * @return scale of map
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the maps scale.
     *
     * @param scale of map
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Add event handlers for mouse and keyboard interactions with map.
     */
    private void addEventHandlers() {
        setOnScroll(event -> {
            double addedVal = event.getDeltaY() * (scale / ZOOM_SPEED);

            if ((scale + addedVal) > ZOOM_MIN && (scale + addedVal) < ZOOM_MAX) {
                scale += addedVal;
                redraw();
            }
        });

        setOnMousePressed(event -> {
            if (event.getButton() == DRAG_BUTTON) {
                dragX = event.getX();
                dragY = event.getY();
                isDragging = true;
                redraw();
            }
        });

        setOnMouseDragged(event -> {
            if (event.getButton() == DRAG_BUTTON) {
                double xChange = dragX - event.getX();
                double yChange = dragY - event.getY();

                centerView(transferPixelToCoordinate((tmpWidth / 2) + xChange, (tmpHeight / 2) - yChange));

                dragX = event.getX();
                dragY = event.getY();
            }
        });

        setOnMouseReleased(event -> {
            if (event.getButton() == DRAG_BUTTON) {
                isDragging = false;
                redraw();
            }
        });

        setOnContextMenuRequested(event -> {
            System.out.println("Click: " + event.getX() + "; " + event.getY());
            Point2D coords = transferPixelToCoordinate(event.getX(), event.getY());
            System.out.println("Coords: N" + coords.getX() + "; E" + coords.getY());
            gc.setFill(Color.GREEN);
            gc.setStroke(Color.GREEN);
            Point2D pixelPos = transferCoordinateToPixel(coords);
            gc.fillRect(pixelPos.getX(), pixelPos.getY(), 2, 2);
            System.out.println("Pos: " + pixelPos);
        });
    }

    /**
     * Forces a full map redraw
     */
    private void redraw() {
        tmpWidth = getWidth();
        tmpHeight = getHeight();
        coveredWidth = tmpWidth / scale;
        coveredHeight = tmpHeight / scale;
        coordsBounds = new BoundingBox(mapCenter.getX() - coveredHeight / 2, mapCenter.getY() - coveredWidth / 2,
                coveredHeight, coveredWidth);
        heightDistance = coordsBounds.getWidth() * KM_PER_COORD;
        widthDistance = coordsBounds.getHeight() * KM_PER_COORD;

        // clear view
        gc.clearRect(0, 0, tmpWidth, tmpHeight);

        drawGrid();

        // is dragging value can be used for faster redraw during map interaction
        drawElements();

        drawInfo();
    }

    /**
     * Draw info about canvas in the top left corner of the canvas.
     */
    private void drawInfo() {
        gc.setFill(Color.BLACK);
        gc.fillText("Center: " + MathUtils.roundToDecimalsAsString(mapCenter.getX(), 5) + " " +
                MathUtils.roundToDecimalsAsString(mapCenter.getY(), 5), 10, 20);
        gc.fillText("Distance: " + MathUtils.roundToDecimalsAsString(widthDistance, 3) + " km x " + MathUtils.roundToDecimalsAsString(heightDistance, 3) + " km", 10, 40);
        gc.fillText("Elements displayed: " + displayedElems, 10, 60);
        gc.fillText("Scale: " + MathUtils.roundToDecimalsAsString(scale, 2), 10, 80);
        gc.fillText("Bounds: " + coordsBounds, 10, 100);
    }

    /**
     * Draw coordinates grid of full longitudes/latitude values.
     */
    private void drawGrid() {
        double northPos;
        double eastPos;
        gc.setStroke(Color.BLACK);

        double x = coordsBounds.getMinX();
        while (x < coordsBounds.getMaxX()) {
            northPos = transferCoordinateToPixel(new Point2D(Math.ceil(x), Math.ceil(x))).getY();
            gc.strokeLine(0, northPos, tmpWidth, northPos);
            x += 1;
        }

        double y = coordsBounds.getMinY();
        while (y < coordsBounds.getMaxY()) {
            eastPos = transferCoordinateToPixel(new Point2D(Math.ceil(y), Math.ceil(y))).getX();
            gc.strokeLine(eastPos, 0, eastPos, tmpHeight);
            y += 1;
        }
    }

    /**
     * Draw all drawables.
     */
    private void drawElements() {
        List<IMapDrawable> toDraw = drawables.parallelStream()
                .filter(p -> !isDragging || p.showDuringGrab())
                .filter(p -> p.getMinDrawScale() < scale)
                .filter(p -> coordsBounds.contains(p.getCoordinates()))
                .collect(Collectors.toList());

        displayedElems = toDraw.size();
        for (IMapDrawable elem : toDraw) {
            elem.draw(gc, this);
        }
    }

    /**
     * Transfers earth coordinates into map pixel coordinates.
     *
     * @param p Earth coordinates
     * @return Pixel coordinates
     */
    public Point2D transferCoordinateToPixel(Point2D p) {
        /*return new Point2D((p.getX() - mapCenter.getX()) * scale + tmpWidth / 2,
                (p.getY() - mapCenter.getY()) * scale + tmpHeight / 2);*/
        return new Point2D((p.getY() - mapCenter.getY()) * scale + tmpWidth / 2,
                (p.getX() - mapCenter.getX()) * scale + tmpHeight / 2);
    }

    /**
     * Transfers pixel coordinates to earth coordinates.
     *
     * @param x Pixel position
     * @param y Pixel position
     * @return Earth coordinates
     */
    public Point2D transferPixelToCoordinate(double x, double y) {
        /*return new Point2D(coordsBounds.getMinY() + (y / tmpHeight) * coordsBounds.getHeight(),
                coordsBounds.getMinX() + (x / tmpWidth) * coordsBounds.getWidth());*/
        return new Point2D(coordsBounds.getMinX() + (y / tmpHeight) * coordsBounds.getWidth(),
                coordsBounds.getMinY() + (x / tmpWidth) * coordsBounds.getHeight());
    }

    public Point2D transferPixelToCoordinate(Point2D p) {
        return transferPixelToCoordinate(p.getX(), p.getY());
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
     * @param elem Point to add
     */
    public void addDrawableElement(IMapDrawable elem) {
        if (elem == null) {
            throw new IllegalArgumentException("No valid element!");
        }

        drawables.add(elem);
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
