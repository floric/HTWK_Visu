package org.htwkvisu.gui;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import org.htwkvisu.org.IMapDrawable;
import org.htwkvisu.utils.MathUtils;
import org.reactfx.util.FxTimer;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class BasicCanvas extends Canvas implements ScoringCanvas {

    static final double ZOOM_MIN = 150;
    static final double ZOOM_MAX = 1000000;
    // constants
    private static final double ZOOM_SPEED = 100;
    private static final MouseButton MOUSE_BUTTON_DRAG = MouseButton.SECONDARY;
    private static final MouseButton MOUSE_BUTTON_SELECT = MouseButton.PRIMARY;
    private static final int SELECTION_MAX_PX_TOLERANCE = 10;
    private static final long REDRAW_DELAY_SECONDS = 1L;

    // cached values for faster drawing
    protected double tmpWidth = 0;
    protected double tmpHeight = 0;
    protected BoundingBox coordsBounds = new BoundingBox(0, 0, tmpWidth, tmpHeight);
    protected LinkedList<IMapDrawable> drawables = new LinkedList<>();

    // canvas dragging
    protected double scale = 350;
    private long lastScrollTime = 0;
    private boolean isDragging = false;
    private double dragX = 0;
    private double dragY = 0;
    protected ScoringConfig config;

    /**
     * Construct and init canvas
     */
    public BasicCanvas(ScoringConfig config) {
        this.config = config;
        config.setCanvas(this);
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());

        addEventHandlers();
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

        setDragMode(true);
        redraw();

        lastScrollTime = System.currentTimeMillis();

        FxTimer.runLater(Duration.ofSeconds(REDRAW_DELAY_SECONDS), () -> {
            if (isInDragMode() && System.currentTimeMillis() - lastScrollTime > REDRAW_DELAY_SECONDS * 500) {
                setDragMode(false);
                redraw();
            }

        });
    }

    public void setDragMode(boolean isDragging) {
        this.isDragging = isDragging;
    }

    public boolean isInDragMode() {
        return isDragging;
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

    public ScoringConfig getConfig() {
        return config;
    }

    public void setConfig(ScoringConfig config) {
        this.config = config;
    }

    /**
     * Add event handlers for mouse and keyboard interactions with map.
     */
    protected void addEventHandlers() {

        // scroll to zoom
        setOnScroll(event -> {
            double addedVal = event.getDeltaY() * (scale / ZOOM_SPEED);
            setScale(scale + addedVal);
        });

        // drag press
        setOnMousePressed(event -> {
            if (event.getButton() == MOUSE_BUTTON_DRAG) {
                dragX = event.getX();
                dragY = event.getY();
                setDragMode(true);
                redraw();
            }
        });

        // drag move
        setOnMouseDragged(event -> {
            if (event.getButton() == MOUSE_BUTTON_DRAG) {
                double xChange = dragX - event.getX();
                double yChange = dragY - event.getY();

                centerView(transferPixelToCoordinate((tmpWidth / 2) + xChange, (tmpHeight / 2) + yChange));

                dragX = event.getX();
                dragY = event.getY();
            }
        });

        // drag release
        setOnMouseReleased(event -> {
            if (event.getButton() == MOUSE_BUTTON_DRAG) {
                setDragMode(false);
                redraw();
            }
        });

        // selection
        setOnMouseClicked(event -> {
            if (event.getButton() == MOUSE_BUTTON_SELECT) {
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

    public BoundingBox getCoordsBounds() {
        return this.coordsBounds;
    }

    public String getCoordsBoundsAsString() {
        DecimalFormat df = new DecimalFormat("####0.00");
        String bounds = "MinX: " + df.format(coordsBounds.getMinX());
        bounds += " MinY: " + df.format(coordsBounds.getMinY());
        bounds += " MaxX: " + df.format(coordsBounds.getMaxX());
        bounds += " MaxY: " + df.format(coordsBounds.getMaxY());
        bounds += " H: " + df.format(coordsBounds.getHeight());
        bounds += " W: " + df.format(coordsBounds.getWidth());
        return bounds;
    }

    protected abstract void drawInfo();

    /**
     * Transfers earth coordinates into map pixel coordinates.
     *
     * @param p Earth coordinates
     * @return Pixel coordinates
     */
    protected abstract Point2D transferCoordinateToPixel(Point2D p);

    /**
     * Transfers pixel coordinates to earth coordinates.
     *
     * @param x Pixel position
     * @param y Pixel position
     * @return Earth coordinates
     */
    protected abstract Point2D transferPixelToCoordinate(double x, double y);

    public Point2D transferPixelToCoordinate(Point2D p) {
        return transferPixelToCoordinate(p.getX(), p.getY());
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
