package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by floric on 7/14/16.
 */
public class MapCanvasTest {

    private MapCanvas canvas;
    private static final int MAP_WIDTH = 300;
    private static final int MAP_HEIGHT = 200;

    @Before
    public void setUp() {
        canvas = new MapCanvas();
        canvas.setWidth(MAP_WIDTH);
        canvas.setHeight(MAP_HEIGHT);
    }

    @Test
    public void transferCoordinateToPixel() throws Exception {
        canvas.centerView(new Point2D(0, 0));
        canvas.setScale(1);

        //TODO test scale invariance

        // top left corner
        assertPointToPixel(new Point2D(MAP_HEIGHT / 2, -MAP_WIDTH / 2), new Point2D(0, 0));
        // top right corner
        assertPointToPixel(new Point2D(MAP_HEIGHT / 2, MAP_WIDTH / 2), new Point2D(MAP_WIDTH, 0));
        // bottom left corner
        assertPointToPixel(new Point2D(-MAP_HEIGHT / 2, -MAP_WIDTH / 2), new Point2D(0, MAP_HEIGHT));
        // bottom right corner
        assertPointToPixel(new Point2D(-MAP_HEIGHT / 2, MAP_WIDTH / 2), new Point2D(MAP_WIDTH, MAP_HEIGHT));

        // test offsets with scale = 1
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            Point2D ptOffset = new Point2D(rnd.nextDouble() * 100, rnd.nextDouble() * 100);
            canvas.centerView(ptOffset);

            // top left corner
            assertPointToPixel(new Point2D(MAP_HEIGHT / 2, -MAP_WIDTH / 2), new Point2D(0, 0).add(new Point2D(-ptOffset.getY(), ptOffset.getX())));
            // top right corner
            assertPointToPixel(new Point2D(MAP_HEIGHT / 2, MAP_WIDTH / 2), new Point2D(MAP_WIDTH, 0).add(new Point2D(-ptOffset.getY(), ptOffset.getX())));
            // bottom left corner
            assertPointToPixel(new Point2D(-MAP_HEIGHT / 2, -MAP_WIDTH / 2), new Point2D(0, MAP_HEIGHT).add(new Point2D(-ptOffset.getY(), ptOffset.getX())));
            // bottom right corner
            assertPointToPixel(new Point2D(-MAP_HEIGHT / 2, MAP_WIDTH / 2), new Point2D(MAP_WIDTH, MAP_HEIGHT).add(new Point2D(-ptOffset.getY(), ptOffset.getX())));
        }

        double scale = 1;
        canvas.centerView(new Point2D(0, 0));
        for (int i = 0; i < 10; i++) {
            scale = i;
            canvas.setScale(scale);

            // center
            assertPointToPixel(new Point2D(0, 0), new Point2D(MAP_WIDTH / 2, MAP_HEIGHT / 2));
        }
    }

    private void assertPointToPixel(Point2D testPt, Point2D expPt) {
        Point2D pt = canvas.transferCoordinateToPixel(testPt);
        assertEquals(0.0, pt.distance(expPt), 0.001);
    }

    @Test
    public void transferPixelToCoordinate() throws Exception {
        Random rnd = new Random();

        canvas.setScale(1);
        canvas.centerView(new Point2D(0, 0));

        // center
        assertPointToCoordinate(new Point2D(MAP_WIDTH / 2, MAP_HEIGHT / 2), new Point2D(0, 0));

        // top left corner
        assertPointToCoordinate(new Point2D(0, 0), new Point2D(MAP_HEIGHT / 2, -MAP_WIDTH / 2));
        // top right corner
        assertPointToCoordinate(new Point2D(MAP_WIDTH, 0), new Point2D(MAP_HEIGHT / 2, MAP_WIDTH / 2));
        // bottom left corner
        assertPointToCoordinate(new Point2D(0, MAP_HEIGHT), new Point2D(-MAP_HEIGHT / 2, -MAP_WIDTH / 2));
        // bottom right corner
        assertPointToCoordinate(new Point2D(MAP_WIDTH, MAP_HEIGHT), new Point2D(-MAP_HEIGHT / 2, MAP_WIDTH / 2));
    }

    @Test
    public void testBothTransfersTogether() {
        Random rnd = new Random();

        canvas.setScale(1);
        canvas.centerView(new Point2D(0, 0));

        // create random points with random map centers and scales
        // then transfer them to pixel space and back to earth space
        for (int i = 0; i < 50; i++) {
            Point2D p = new Point2D(rnd.nextDouble() * 100, rnd.nextDouble() * 100);
            canvas.setScale(rnd.nextDouble());
            canvas.centerView(new Point2D(rnd.nextDouble() * 50, rnd.nextDouble() * 50));
            assertEquals(0.0, p.distance(canvas.transferCoordinateToPixel(canvas.transferPixelToCoordinate(p))), 0.01);
            assertEquals(0.0, p.distance(canvas.transferPixelToCoordinate(canvas.transferCoordinateToPixel(p))), 0.01);
        }
    }

    private void assertPointToCoordinate(Point2D testPt, Point2D expPt) {
        Point2D pt = canvas.transferPixelToCoordinate(testPt);
        assertEquals(0.0, pt.distance(expPt), 0.001);
    }

    @Test
    public void centerView() throws Exception {
        Random rnd = new Random();
        Point2D newCenter;

        for (int i = 0; i < 10; i++) {
            newCenter = new Point2D(rnd.nextDouble(), rnd.nextDouble());
            canvas.centerView(newCenter);
            assertEquals(newCenter, canvas.getMapCenter());
        }
    }

    @Test
    public void addDrawableElement() throws Exception {
        canvas.addDrawableElement(new City(new Point2D(0, 0), "Test", 0));
        canvas.addDrawableElement(new SimplePoint(new Point2D(0, 0), 0));

        try {
            canvas.addDrawableElement(null);
            assert (false);
        } catch (IllegalArgumentException ex) {
        }

    }

}