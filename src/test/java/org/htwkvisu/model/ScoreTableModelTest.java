package org.htwkvisu.model;

import org.htwkvisu.org.pois.Category;
import org.htwkvisu.org.pois.ScoreType;
import org.htwkvisu.scoring.IFallOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScoreTableModelTest {

    private ScoreTableModel model;

    @Before
    public void setUp() {
        model = new ScoreTableModel(Category.EDUCATION, ScoreType.SCHOOL);
    }

    /**
     * This test shows only that onClick and onEnterCommit return no exception
     */
    @Test
    public void registerListenerWorks() {
        model.onClick();
        model.onEnterCommit();
    }

    @Test
    public void switchAndGetFallOfChangeFallOf() throws Exception {
        final IFallOf fallOf = model.getFallOf();
        final IFallOf otherFallOf = model.switchAndGetFallOf();
        Assert.assertFalse(fallOf.equals(otherFallOf));
    }
}