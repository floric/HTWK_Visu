package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.scoring.ConstantFallOf;
import org.htwkvisu.scoring.ExponentialFallOf;
import org.htwkvisu.scoring.LinearFallOf;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.htwkvisu.org.pois.Category.*;
import static org.htwkvisu.org.pois.ScoreType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ScoreTypeTest {

    private static final double TINY_DELTA = 0.0001;
    private static final int TEN = 10;
    private static final Point2D SAMPLE_POINT = new Point2D(0, 0);
    private static final Point2D POINT_WITH_TWOS = new Point2D(2, 2);
    private static final double EXPECTED_LINEAR_VALUE = 0.5719095841793653;
    private static final Point2D POINT_FOR_EXPONENTIAL = new Point2D(4, 4);
    private static final double EXPECTED_EXPONENTIAL_VALUE = 0.9999999999999998;
    private static final Point2D POINT_THREE = new Point2D(3, 3);
    private static final double VALUE_FROM_SCORING_WITHOUT_WEIGHT = 5.0;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp(){
        // Reset because of parallelism of Tests
        TRAM.setWeight(1.0);
    }

    @Test
    public void constantFallOfWorks() {
        AERODROME.setFallOf(new ConstantFallOf(1, TEN));
        AERODROME.setCustomPOIs(SAMPLE_POINT);
        double values = AERODROME.calculateScoreValueForCustom(SAMPLE_POINT);

        assertEquals(TEN, values, TINY_DELTA);

        assertTrue(INFRASTRUCTURE.calculateCategoryValueForCustom(SAMPLE_POINT) > 0);
    }

    @Test
    public void linearFallOfWorks() {
        SCHOOL.setFallOf(new LinearFallOf(3, TEN));
        SCHOOL.setCustomPOIs(SAMPLE_POINT);
        double values = SCHOOL.calculateScoreValueForCustom(POINT_WITH_TWOS);

        assertEquals(EXPECTED_LINEAR_VALUE, values, TINY_DELTA);
        assertTrue(EDUCATION.calculateCategoryValueForCustom(POINT_WITH_TWOS) == EXPECTED_LINEAR_VALUE);
    }

    @Test
    public void exponentialFallOfWorks() {
        SCHOOL.setFallOf(new ExponentialFallOf(4,2,2));
        SCHOOL.setCustomPOIs(POINT_WITH_TWOS);
        double values = SCHOOL.calculateScoreValueForCustom(POINT_FOR_EXPONENTIAL);
        assertEquals(EXPECTED_EXPONENTIAL_VALUE, values, TINY_DELTA);
        assertTrue(EDUCATION.calculateCategoryValueForCustom(POINT_FOR_EXPONENTIAL) == EXPECTED_EXPONENTIAL_VALUE);
    }

    @Test
    public void mixedFallOfsWork() {
        TRAM.setFallOf(new ExponentialFallOf(3,3,2));
        TRAM.setCustomPOIs(POINT_THREE);
        double value = TRAM.calculateScoreValueForCustom(POINT_THREE);

        TRAIN.setFallOf(new LinearFallOf(3,1));
        TRAIN.setCustomPOIs(POINT_THREE);
        value += TRAIN.calculateScoreValueForCustom(POINT_THREE);

        BUS.setFallOf(new ConstantFallOf(3,1));
        BUS.setCustomPOIs(POINT_THREE);
        value += BUS.calculateScoreValueForCustom(POINT_THREE);

        assertTrue(INFRASTRUCTURE.calculateCategoryValueForCustom(POINT_THREE) == value);
        assertEquals(VALUE_FROM_SCORING_WITHOUT_WEIGHT,value,TINY_DELTA);
    }

    @Test
    public void weightWork(){
        TRAM.setFallOf(new ExponentialFallOf(3,3,2));
        TRAM.setCustomPOIs(POINT_THREE);
        double value = TRAM.calculateScoreValueForCustom(POINT_THREE);
        TRAM.setWeight(TEN);
        double weightedValue = TRAM.calculateScoreValueForCustom(POINT_THREE);
        double expectedWeightedValue = value * TEN/(TEN+17);

        assertEquals(expectedWeightedValue,weightedValue,TINY_DELTA);

        TRAIN.setFallOf(new LinearFallOf(3,1));
        TRAIN.setCustomPOIs(POINT_THREE);
        value += TRAIN.calculateScoreValueForCustom(POINT_THREE);
        weightedValue += TRAIN.calculateScoreValueForCustom(POINT_THREE);

        BUS.setFallOf(new ConstantFallOf(3,1));
        BUS.setCustomPOIs(POINT_THREE);
        value += BUS.calculateScoreValueForCustom(POINT_THREE);
        weightedValue += BUS.calculateScoreValueForCustom(POINT_THREE);

        assertTrue(INFRASTRUCTURE.calculateCategoryValueForCustom(POINT_THREE) == weightedValue);
        assertNotEquals(VALUE_FROM_SCORING_WITHOUT_WEIGHT,value,TINY_DELTA);
    }

    @Test
    public void setWeightShouldFail(){
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Weight shouldn't be 0.0 .");
        TRAM.setWeight(0.0);
    }

}