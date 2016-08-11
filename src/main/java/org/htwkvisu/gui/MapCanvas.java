package org.htwkvisu.gui;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.htwkvisu.org.IMapDrawable;
import org.htwkvisu.org.pois.Category;
import org.htwkvisu.org.pois.ScoreType;
import org.htwkvisu.utils.MathUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Canvas for map.
 */
public class MapCanvas extends Canvas {

    public static final double ZOOM_MIN = 150;
    public static final double ZOOM_MAX = 1000000;
    // constants
    private static final double ZOOM_SPEED = 100;
    private static final MouseButton MOUSEBUTTON_DRAG = MouseButton.SECONDARY;
    private static final MouseButton MOUSEBUTTON_SELECT = MouseButton.PRIMARY;
    private static final int SELECTION_MAX_PX_TOLERANCE = 10;

    private static final double MIN_SCORING_VALUE = 0; //TODO GUI-editable
    private static final double MAX_SCORING_VALUE = 130000; //TODO GUI-editable

    // scores
    private Point2D mapCenter = new Point2D(51.343479, 12.387772);
    private LinkedList<IMapDrawable> drawables = new LinkedList<>();
    private int samplingPixelDensity = 25; //TODO GUI-editable

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
    private double scale = 100000;
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
        for (int i = 0; i < 9999; i++) {
            addDrawableElement(
                    new SimplePoint(new Point2D(50.5 + rnd.nextDouble(), 11.5 + rnd.nextDouble()), rnd.nextDouble() * 40000)
            );
        }

        // add test cities
        addDrawableElement(new City(new Point2D(51.340333, 12.37475), "Leipzig", 0));
        addDrawableElement(new City(new Point2D(51.482778, 11.97), "Halle (Saale)", 0));
        addDrawableElement(new City(new Point2D(50.927222, 11.586111), "Jena", 0));
        addDrawableElement(new City(new Point2D(50.983333, 11.033333), "Erfurt", 0));
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

    // The following functions return the coordinate bounds of the Canvas.

    /**
     * Set the maps scale.
     *
     * @param scale of map
     */
    public void setScale(double scale) {
        if (scale < ZOOM_MAX) {
            if (scale > ZOOM_MIN) {
                this.scale = scale;
            } else {
                this.scale = ZOOM_MIN;
            }
        } else {
            this.scale = ZOOM_MAX;
        }
        redraw();
    }

    public Point2D getLeftTopCorner() {
        return new Point2D(coordsBounds.getMaxX(), coordsBounds.getMinY());
    }

    public Point2D getRightTopCorner() {
        return new Point2D(coordsBounds.getMaxX(), coordsBounds.getMaxY());
    }

    public Point2D getLeftBottomCorner() {
        return new Point2D(coordsBounds.getMinX(), coordsBounds.getMinY());
    }

    public Point2D getRightBottomCorner() {
        return new Point2D(coordsBounds.getMinX(), coordsBounds.getMaxY());
    }

    public Point2D getCenter() {
        return mapCenter;
    }

    public int getSamplingPixelDensity() {
        return samplingPixelDensity;
    }

    public void setSamplingPixelDensity(int samplingPixelDensity) {
        if (samplingPixelDensity > 0) {
            this.samplingPixelDensity = samplingPixelDensity;
        }
    }

    /**
     * Add event handlers for mouse and keyboard interactions with map.
     */
    private void addEventHandlers() {

        // scroll to zoom
        setOnScroll(event -> {
            double addedVal = event.getDeltaY() * (scale / ZOOM_SPEED);
            setScale(scale + addedVal);
            redraw();
        });

        // drag press
        setOnMousePressed(event -> {
            if (event.getButton() == MOUSEBUTTON_DRAG) {
                dragX = event.getX();
                dragY = event.getY();
                isDragging = true;
                redraw();
            }
        });

        // drag move
        setOnMouseDragged(event -> {
            if (event.getButton() == MOUSEBUTTON_DRAG) {
                double xChange = dragX - event.getX();
                double yChange = dragY - event.getY();

                centerView(transferPixelToCoordinate((tmpWidth / 2) + xChange, (tmpHeight / 2) + yChange));

                dragX = event.getX();
                dragY = event.getY();
            }
        });

        // drag release
        setOnMouseReleased(event -> {
            if (event.getButton() == MOUSEBUTTON_DRAG) {
                isDragging = false;
                redraw();
            }
        });

        // selection
        setOnMouseClicked(event -> {
            if (event.getButton() == MOUSEBUTTON_SELECT) {
                List<IMapDrawable> drawablesToSelect = drawables.parallelStream()
                        .filter(elem -> elem.getMinDrawScale() < scale)
                        .filter(elem -> coordsBounds.contains(elem.getCoordinates())).collect(Collectors.toList());

                Point2D clickPos = new Point2D(event.getX(), event.getY());

                for (IMapDrawable elem : drawablesToSelect) {
                    Point2D lclPos = transferCoordinateToPixel(elem.getCoordinates());
                    if (lclPos.distance(clickPos) < SELECTION_MAX_PX_TOLERANCE) {
                        Logger.getGlobal().info("Selected: " + elem.getName() + " at " + MathUtils.formatCoordinates(elem.getCoordinates()));
                        break;
                    }
                }
            }
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
        heightDistance = MathUtils.convertUnitsToKilometres(coordsBounds.getWidth());
        widthDistance = MathUtils.convertUnitsToKilometres(coordsBounds.getHeight());

        // clear view
        gc.clearRect(0, 0, tmpWidth, tmpHeight);

        // draw map content
        drawScoringValues();
        drawGrid();
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
     * Draw scoring values in our canvas.
     */
    private void drawScoringValues() {
        // get sample points for canvas
        // sample points will be drawn every "samplingPixelDensity" pixels in x and y direction
        Grid grid = new Grid(this);
        List<List<Point2D>> gridPoints = grid.calcGridPoints(samplingPixelDensity);

        // save previous colors
        final Paint curFillPaint = gc.getFill();
        final Paint curStrokePaint = gc.getStroke();

        // now calculate the values
        for (List<Point2D> line : gridPoints) {
            for (Point2D coord : line) {
                final double scoreForCoord = Category.INFRASTRUCTURE.calculateScoreValue(coord);
                gc.setFill(getColorForValue(scoreForCoord));

                Point2D pixelPos = transferCoordinateToPixel(coord);
                //Current as oval
                gc.fillRect(pixelPos.getX(), pixelPos.getY(), samplingPixelDensity, samplingPixelDensity);
            }
        }

        // draw score values

        // now map the score value to a color function for visualization
        // and use the GraphicsContext to draw the final colors to the canvas
        // interpolate between the samples points with simple linear interpolation in our matrix/grid
        // Using PixelWriter is recommended (faster)


        //Restore previous colors
        gc.setFill(curFillPaint);
        gc.setStroke(curStrokePaint);
    }

    /**
     * Draw coordinates grid of full longitudes/latitude values.
     */
    private void drawGrid() {
        double northPos;
        double eastPos;
        gc.setStroke(Color.BLACK);

        // Latitude
        double x = coordsBounds.getMinX();
        while (x < coordsBounds.getMaxX()) {
            double fullVal = Math.ceil(x);
            northPos = transferCoordinateToPixel(new Point2D(fullVal, Math.ceil(x))).getY();
            gc.strokeLine(0, northPos, tmpWidth, northPos);
            gc.fillText(MathUtils.roundToDecimalsAsString(fullVal, 0), 10, northPos - 10);
            x++;
        }

        // Longitude
        double y = coordsBounds.getMinY();
        while (y < coordsBounds.getMaxY()) {
            double fullVal = Math.ceil(y);
            eastPos = transferCoordinateToPixel(new Point2D(Math.ceil(y), fullVal)).getX();
            gc.strokeLine(eastPos, 0, eastPos, tmpHeight);
            gc.fillText(MathUtils.roundToDecimalsAsString(fullVal, 0), eastPos + 10, tmpHeight - 10);
            y++;
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
            elem.draw(this.gc, this);
        }
    }

    /**
     * Transfers earth coordinates into map pixel coordinates.
     *
     * @param p Earth coordinates
     * @return Pixel coordinates
     */
    public Point2D transferCoordinateToPixel(Point2D p) {
        return new Point2D((p.getY() - mapCenter.getY()) * scale + tmpWidth / 2,
                ((mapCenter.getX() - p.getX()) * scale + tmpHeight / 2));
    }

    /**
     * Transfers pixel coordinates to earth coordinates.
     *
     * @param x Pixel position
     * @param y Pixel position
     * @return Earth coordinates
     */
    public Point2D transferPixelToCoordinate(double x, double y) {
        return new Point2D(coordsBounds.getMaxX() - (y / tmpHeight) * coordsBounds.getWidth(),
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

    /**
     * Calculats a color for a given scoring value
     *
     * @param value the scoring value
     * @return color for scoring value
     */
    private static Color getColorForValue(double value) {
        double hue;
        if (value < MIN_SCORING_VALUE) {
            hue = Color.BLUE.getHue();
        } else if (value > MAX_SCORING_VALUE) {
            hue = Color.RED.getHue();
        } else {
            hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * (value - MIN_SCORING_VALUE) / (MAX_SCORING_VALUE - MIN_SCORING_VALUE);
        }

        return Color.hsb(hue, 1.0, 1.0);
    }
}
