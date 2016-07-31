package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.junit.Before;

public class AirportTest {

    private static final String NAME = "Test";
    private static final Point2D POSITION = new Point2D(0.123, -1.234);
    private Airport airport;
    private static final double TINY_DELTA = 0.0001;

    @Before
    public void setUp() throws Exception {
        airport = new Airport(NAME, POSITION);
    }


}