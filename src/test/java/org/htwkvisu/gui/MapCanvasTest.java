package org.htwkvisu.gui;

import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by floric on 7/14/16.
 */
public class MapCanvasTest {

    private MapCanvas canvas;

    @Before
    public void setUp() {
        canvas = new MapCanvas();
        canvas.setWidth(400);
        canvas.setHeight(400);
    }

    @Test
    public void transferCoordinateToPixel() throws Exception {
        // difficult tests
    }

    @Test
    public void transferPixelToCoordinate() throws Exception {
        // difficult tests
    }

    @Test
    public void centerView() throws Exception {
        Random rnd = new Random();
        Point2D newCenter;

        for (int i = 0; i < 10; i++) {
            newCenter = new Point2D(rnd.nextDouble(), rnd.nextDouble());
            canvas.centerView(newCenter);
            Assert.assertEquals(newCenter, canvas.getMapCenter());
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