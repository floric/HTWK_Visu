package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@Ignore
public class MapCanvasTest {

    private static final int ZERO = 0;
    private static final double ZERO_DOUBLE = 0.0;
    private static final double DELTA = 0.01;
    private static final int MAP_WIDTH = 300;
    private static final int MAP_HEIGHT = 200;
    private static final double TINY_DELTA = 0.001;
    private static final int ONE_HUNDRED = 100;
    private static final int DEFAULT_PIXEL_DENSITY = 30;
    private static final int DEFAULT_MIN_SCORING_VALUE = 0;
    private static final int DEFAULT_MAX_SCORING_VALUE = 100000;
    private MapCanvas canvas;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        canvas = new MapCanvas(DEFAULT_PIXEL_DENSITY,DEFAULT_MIN_SCORING_VALUE,DEFAULT_MAX_SCORING_VALUE);
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
        // test center
        canvas.centerView(new Point2D(ZERO, ZERO));
        for (int i = 0; i < 10; i++) {
            canvas.setScale(MapCanvas.ZOOM_MIN + i);
            assertCoordToPixel(canvas.getCenter(), new Point2D(MAP_WIDTH / 2, MAP_HEIGHT / 2));
        }

        // other cases automatically catched with testBothTransersTogether
    }

    @Test
    public void transferPixelToCoordinate() throws Exception {
        canvas.setScale(MapCanvas.ZOOM_MIN);
        canvas.centerView(new Point2D(ZERO, ZERO));

        assertPixelToCoord(new Point2D(MAP_WIDTH / 2, MAP_HEIGHT / 2), canvas.getCenter());
        assertPixelToCoord(new Point2D(ZERO, ZERO), canvas.getLeftTopCorner());
        assertPixelToCoord(new Point2D(MAP_WIDTH, ZERO), canvas.getRightTopCorner());
        assertPixelToCoord(new Point2D(ZERO, MAP_HEIGHT), canvas.getLeftBottomCorner());
        assertPixelToCoord(new Point2D(MAP_WIDTH, MAP_HEIGHT), canvas.getRightBottomCorner());
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
            assertEquals(newCenter, canvas.getCenter());
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

    private void assertCoordToPixel(Point2D testPt, Point2D expPt) {
        Point2D pt = canvas.transferCoordinateToPixel(testPt);
        assertEquals(ZERO_DOUBLE, pt.distance(expPt), TINY_DELTA);
    }

    private void assertPixelToCoord(Point2D testPt, Point2D expPt) {
        Point2D pt = canvas.transferPixelToCoordinate(testPt);
        assertEquals(ZERO_DOUBLE, pt.distance(expPt), TINY_DELTA);
    }

    @Test
    public void getSampleCoordPoints() throws Exception {
        // sample just one point and border samples
        canvas.setWidth(1);
        canvas.setHeight(1);
        Grid gridOne = new Grid(canvas);
        List<List<Point2D>> pts = gridOne.calcGridPoints(1000);
        assertEquals(3, pts.size());
        assertEquals(3, pts.get(0).size());

        // sample two points and border samples
        canvas.setWidth(10);
        canvas.setHeight(10);
        Grid gridTwo = new Grid(canvas);
        pts = gridTwo.calcGridPoints(5);
        assertEquals(4, pts.size());
        assertEquals(4, pts.get(0).size());

        // distance between samples is equal
        assertEquals(0, Math.abs(pts.get(1).get(0).distance(pts.get(0).get(0)) - (pts.get(2).get(0).distance(pts.get(1).get(0)))), TINY_DELTA);
        assertEquals(canvas.transferPixelToCoordinate(5, 0).getX() / canvas.getScale(), pts.get(1).get(0).distance(pts.get(0).get(0)), TINY_DELTA);
        assertEquals(canvas.transferPixelToCoordinate(0, 5).getY() / canvas.getScale(), pts.get(1).get(1).distance(pts.get(1).get(0)), TINY_DELTA);
    }
}