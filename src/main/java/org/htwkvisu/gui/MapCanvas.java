package org.htwkvisu.gui;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.htwkvisu.gui.interpolate.InterpolateConfig;
import org.htwkvisu.org.IMapDrawable;
import org.htwkvisu.org.pois.NormalizedColorCalculator;
import org.htwkvisu.org.pois.ScoringCalculator;
import org.htwkvisu.utils.ActiveTimer;
import org.htwkvisu.utils.MathUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Canvas for map.
 */
public class MapCanvas extends BasicCanvas {

    public static final Point2D CITY_LEIPZIG = new Point2D(51.340333, 12.37475);
    private Point2D mapCenter = CITY_LEIPZIG;
    private GraphicsContext gc = getGraphicsContext2D();
    private double widthDistance = 0;
    private double heightDistance = 0;
    private CheckBox colorModeCheckBox;
    private Grid grid = new Grid(this);
    private int displayedElements = 0;
    private final ActiveTimer timer;

    /**
     * Construct and init canvas
     */
    public MapCanvas(ScoringConfig config) {
        super(config);
        timer = new ActiveTimer();
    }

    @Override
    protected void drawInfo() {
        if (isColorModeActive()) {
            gc.setFill(Color.GRAY);
        } else {
            gc.setFill(Color.BLACK);
        }

        gc.fillText("Center: " + MathUtils.roundToDecimalsAsString(mapCenter.getX(), 5) + " " +
                MathUtils.roundToDecimalsAsString(mapCenter.getY(), 5), 10, 20);
        gc.fillText("Distance: " + MathUtils.roundToDecimalsAsString(widthDistance, 3) + " km x " + MathUtils.roundToDecimalsAsString(heightDistance, 3) + " km", 10, 40);
        gc.fillText("Elements displayed: " + displayedElements, 10, 60);
        gc.fillText("Scale: " + MathUtils.roundToDecimalsAsString(scale, 2), 10, 80);
        gc.fillText("Bounds: " + getCoordsBoundsAsString(), 10, 100);
    }

    @Override
    public void drawScoringValues() {
        // get sample points for canvas
        List<Point2D> gridPoints = calculateGrid();
        Color[] cols = new Color[gridPoints.size()];

        NormalizedColorCalculator norm = new NormalizedColorCalculator(this, isColorModeActive());

        final int pixelDensity = config.getSamplingPixelDensity();
        final int xSize = grid.getxSize();
        final int ySize = grid.getySize();

        // calculate values, parallel
        IntStream.range(0, ySize).parallel().forEach(y -> {
            for (int x = 0; x < xSize; x++) {
                final int index = y * xSize + x;
                Point2D pt = gridPoints.get(index);
                cols[index] = norm.calculateColor(pt);
            }
        });

        PixelWriter pxWriter = gc.getPixelWriter();

        // go through grid cells
        for (int y = 0; y < (ySize - 1); y++) {
            for (int x = 0; x < xSize - 1; x++) {
                final Point2D pxPos = transferCoordinateToPixel(gridPoints.get(y * xSize + x));
                final int posX = (int) pxPos.getX();
                final int posY = (int) pxPos.getY();

                // calculate pixels in grid cells
                for (int xStep = 0; xStep < pixelDensity; xStep++) {
                    for (int yStep = 0; yStep < pixelDensity; yStep++) {
                        final float xNorm = (float) xStep / pixelDensity;
                        final float yNorm = (float) yStep / pixelDensity;

                        // pixel positions in screenspace
                        final int pixX = posX + xStep;
                        final int pixY = posY + yStep;

                        // skip pixels out of screen
                        if (pixX >= 0 && pixY >= 0 && pixX < getWidth() && pixY < getHeight()) {
                            pxWriter.setColor(pixX, pixY, config.getInterpolationMode().interpolateColor(
                                    new InterpolateConfig(cols, xSize, ySize, x, y, xNorm, yNorm)));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void drawGrid() {
        double northPos;
        double eastPos;
        if (isColorModeActive()) {
            gc.setStroke(Color.GRAY);
        } else {
            gc.setStroke(Color.BLACK);
        }

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

    @Override
    public void addDrawableElement(IMapDrawable elem) {
        if (elem == null) {
            throw new IllegalArgumentException("No valid element!");
        }
        drawables.add(elem);
    }

    private void addTestCities() {
        addDrawableElement(new City(CITY_LEIPZIG, "Leipzig", 0));
        addDrawableElement(new City(new Point2D(51.049259, 13.73836112), "Dresden", 0));
        addDrawableElement(new City(new Point2D(50.832222, 12.92416666), "Chemnitz", 0));
        addDrawableElement(new City(new Point2D(50.718888, 12.492222), "Zwickau", 0));
        addDrawableElement(new City(new Point2D(50.495, 12.138333), "Plauen", 0));
        addDrawableElement(new City(new Point2D(50.911944, 13.342778), "Freiberg", 0));
        addDrawableElement(new City(new Point2D(51.238333, 12.725), "Grimma", 0));
        addDrawableElement(new City(new Point2D(51.558333, 13.004167), "Torgau", 0));
        addDrawableElement(new City(new Point2D(51.181389, 14.423889), "Bautzen", 0));
        addDrawableElement(new City(new Point2D(51.163611, 13.4775), "Meißen", 0));
        addDrawableElement(new City(new Point2D(50.585278, 12.700833), "Aue", 0));
        addDrawableElement(new City(new Point2D(51.308056, 13.293889), "Riesa", 0));
        addDrawableElement(new City(new Point2D(51.152778, 14.987222), "Görlitz", 0));
    }

    @Override
    public void drawElements() {
        drawables.clear();
        ScoringCalculator.generateEnabled().forEach(this::addDrawableElement);
        addTestCities();
        List<IMapDrawable> toDraw = drawables.parallelStream()
                .filter(p -> !isInDragMode() || p.showDuringGrab())
                .filter(p -> p.getMinDrawScale() < scale)
                .filter(p -> coordsBounds.contains(p.getCoordinates()))
                .collect(Collectors.toList());

        displayedElements = toDraw.size();
        for (IMapDrawable elem : toDraw) {
            elem.draw(this.gc, this);
        }
    }

    @Override
    public void centerView(Point2D center) {
        if (center != null) {
            mapCenter = center;
        } else {
            mapCenter = CITY_LEIPZIG;
        }
        redraw();
    }

    @Override
    public Point2D getCenter() {
        return mapCenter;
    }

    @Override
    public void redraw() {
        timer.reset();
        tmpWidth = getWidth();
        tmpHeight = getHeight();
        double coveredWidth = tmpWidth / scale;
        double coveredHeight = tmpHeight / scale;
        coordsBounds = new BoundingBox(mapCenter.getX() - coveredHeight / 2, mapCenter.getY() - coveredWidth / 2,
                coveredHeight, coveredWidth);
        heightDistance = MathUtils.convertUnitsToKilometres(coordsBounds.getWidth());
        widthDistance = MathUtils.convertUnitsToKilometres(coordsBounds.getHeight());

        // clear view
        gc.clearRect(0, 0, tmpWidth, tmpHeight);

        // draw map content
        if (!isInDragMode()) {
            drawScoringValues();
        }
        drawGrid();
        drawElements();
        drawInfo();
        timer.logInfo();
    }

    @Override
    public Point2D transferCoordinateToPixel(Point2D p) {
        return new Point2D((p.getY() - mapCenter.getY()) * scale + tmpWidth / 2,
                ((mapCenter.getX() - p.getX()) * scale + tmpHeight / 2));
    }

    @Override
    public Point2D transferPixelToCoordinate(double x, double y) {
        return new Point2D(coordsBounds.getMaxX() - (y / tmpHeight) * coordsBounds.getWidth(),
                coordsBounds.getMinY() + (x / tmpWidth) * coordsBounds.getHeight());
    }

    public int calculateMaxScore() {
        List<Point2D> gridPoints = calculateGrid();
        double score = 0.0;
        double tmp = score;
        for (Point2D point : gridPoints) {
            tmp = ScoringCalculator.calculateEnabledScoreValue(point);
            if (score < tmp) {
                score = tmp;
            }
        }
        return (int) score;
    }

    public List<Point2D> calculateGrid() {
        return grid.calcGridPoints(config.getSamplingPixelDensity());
    }

    public boolean isColorModeActive() {
        return colorModeCheckBox != null && colorModeCheckBox.isSelected();
    }

    public ActiveTimer getTimer() {
        return timer;
    }

    public void setColorModeCheckBox(CheckBox colorModeCheckBox) {
        this.colorModeCheckBox = colorModeCheckBox;
    }
}
