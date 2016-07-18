package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MapCanvasTest {

    private static final int ZERO = 0;
    private static final double ZERO_DOUBLE = 0.0;
    private static final double DELTA = 0.01;
    private static final int MAP_WIDTH = 300;
    private static final int MAP_HEIGHT = 200;
    private static final double TINY_DELTA = 0.001;
    private static final int ONE_HUNDRED = 100;

    private MapCanvas canvas;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        canvas = new MapCanvas();
        canvas.setWidth(MAP_WIDTH);
        canvas.setHeight(MAP_HEIGHT);
    }


    @Test
    public void testScaleChanging() throws Exception {
        Random rnd = new Random();

        // random valid and lower values
        for (int i = 0; i < 5; i++) {
            double newScale = (rnd.nextDouble() - 0.5) * MapCanvas.ZOOM_MIN * 2;
            canvas.setScale(newScale);
            if (newScale < MapCanvas.ZOOM_MIN) {
                assertEquals(MapCanvas.ZOOM_MIN, canvas.getScale(), TINY_DELTA);
            } else {
                assertEquals(newScale, canvas.getScale(), TINY_DELTA);
            }
        }

        // upper zoom border
        canvas.setScale(MapCanvas.ZOOM_MAX + 1);
        assertEquals(MapCanvas.ZOOM_MAX, canvas.getScale(), TINY_DELTA);
        canvas.setScale(MapCanvas.ZOOM_MAX);
        assertEquals(MapCanvas.ZOOM_MAX, canvas.getScale(), TINY_DELTA);
    }

    @Test
    public void transferCoordinateToPixel() throws Exception {
        canvas.centerView(new Point2D(ZERO, ZERO));
        canvas.setScale(MapCanvas.ZOOM_MIN);

        //TODO test scale invariance

        assertBounds(new Point2D(ZERO, ZERO), new Point2D(MAP_WIDTH, ZERO), new Point2D(ZERO, MAP_HEIGHT)
                , new Point2D(MAP_WIDTH, MAP_HEIGHT));

        // test offsets with scale = 1
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            Point2D ptOffset = new Point2D(rnd.nextDouble() * ONE_HUNDRED, rnd.nextDouble() * ONE_HUNDRED);
            canvas.centerView(ptOffset);

            assertBounds(new Point2D(ZERO, ZERO).add(new Point2D(-ptOffset.getY(), ptOffset.getX()))
                    , new Point2D(MAP_WIDTH, ZERO).add(new Point2D(-ptOffset.getY(), ptOffset.getX()))
                    , new Point2D(ZERO, MAP_HEIGHT).add(new Point2D(-ptOffset.getY(), ptOffset.getX()))
                    , new Point2D(MAP_WIDTH, MAP_HEIGHT).add(new Point2D(-ptOffset.getY(), ptOffset.getX())));
        }

        canvas.centerView(new Point2D(ZERO, ZERO));
        for (int i = 0; i < 10; i++) {
            canvas.setScale((double) MapCanvas.ZOOM_MIN + i);

            assertPointToPixel(new Point2D(ZERO, ZERO), canvas.getCenter());
        }
    }

    @Test
    public void transferPixelToCoordinate() throws Exception {
        canvas.setScale(MapCanvas.ZOOM_MIN);
        canvas.centerView(new Point2D(ZERO, ZERO));

        assertPointToPixel(new Point2D(ZERO, ZERO), canvas.getCenter());
        assertBounds(new Point2D(ZERO, ZERO), new Point2D(MAP_WIDTH, ZERO), new Point2D(ZERO, MAP_HEIGHT)
                , new Point2D(MAP_WIDTH, MAP_HEIGHT));
    }

    @Test
    public void testBothTransfersTogether() {
        Random rnd = new Random();

        canvas.setScale(MapCanvas.ZOOM_MIN);
        canvas.centerView(new Point2D(ZERO, ZERO));

        // create random points with random map centers and scales
        // then transfer them to pixel space and back to earth space
        for (int i = 0; i < 50; i++) {
            Point2D p = new Point2D(rnd.nextDouble() * ONE_HUNDRED, rnd.nextDouble() * ONE_HUNDRED);
            canvas.setScale(rnd.nextDouble() + MapCanvas.ZOOM_MIN);
            canvas.centerView(new Point2D(rnd.nextDouble() * 50, rnd.nextDouble() * 50));
            assertEquals(ZERO_DOUBLE, p.distance(canvas.transferCoordinateToPixel(canvas.transferPixelToCoordinate(p)))
                    , DELTA);
            assertEquals(ZERO_DOUBLE, p.distance(canvas.transferPixelToCoordinate(canvas.transferCoordinateToPixel(p)))
                    , DELTA);
        }
    }

    @Test
    public void centerViewMatchRandomCenterPoints() throws Exception {
        Random rnd = new Random();
        Point2D newCenter;

        for (int i = 0; i < 10; i++) {
            newCenter = new Point2D(rnd.nextDouble(), rnd.nextDouble());
            canvas.centerView(newCenter);
            assertEquals(newCenter, canvas.getMapCenter());
        }
    }

    @Test
    public void addDrawableElementSucceedsOnValid() throws Exception {
        canvas.addDrawableElement(new City(new Point2D(ZERO, ZERO), "Test", ZERO));
        canvas.addDrawableElement(new SimplePoint(new Point2D(ZERO, ZERO), ZERO));
    }

    @Test
    public void addDrawableElementFailsOnNull() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No valid element!");

        canvas.addDrawableElement(null);
    }

    private void assertBounds(Point2D leftTop, Point2D rightTop, Point2D leftBottom, Point2D rightBottom) {
        assertPointToPixel(canvas.getLeftTopCorner(), leftTop);
        assertPointToPixel(canvas.getRightTopCorner(), rightTop);
        assertPointToPixel(canvas.getLeftBottomCorner(), leftBottom);
        assertPointToPixel(canvas.getRightBottomCorner(), rightBottom);
    }

    private void assertPointToPixel(Point2D testPt, Point2D expPt) {
        Point2D pt = canvas.transferCoordinateToPixel(testPt);
        assertEquals(ZERO_DOUBLE, pt.distance(expPt), TINY_DELTA);
    }
}