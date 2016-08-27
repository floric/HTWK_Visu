package org.htwkvisu.org.pois;

import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.model.ScoreTableModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ScoringCalculatorTest {

    @Test
    public void generateEnabledWorks() throws Exception {
        Category.EDUCATION.setEnabledForCategory(true);
        final List<BasicPOI> pois = ScoringCalculator.generateEnabled();
        Assert.assertEquals(2521, pois.size());

        ScoreType.SCHOOL.setEnabled(false);
        final List<BasicPOI> poisAfterSetDisable = ScoringCalculator.generateEnabled();
        Assert.assertEquals(1019, poisAfterSetDisable.size());
    }

    @Test
    public void calcAllTableModelsWorks() throws Exception {
        final List<ScoreTableModel> models = ScoringCalculator.calcAllTableModels();

        Assert.assertEquals(18, models.size());
        models.stream().allMatch(c -> c.getCategory().equals(Category.EDUCATION.name()));
        models.stream().allMatch(c -> Category.EDUCATION.getTypes().contains(c.getType()));
        models.stream().allMatch(c -> c.getCategory().equals(Category.INFRASTRUCTURE.name()));
        models.stream().allMatch(c -> Category.INFRASTRUCTURE.getTypes().contains(c.getType()));
        models.stream().allMatch(c -> c.getCategory().equals(Category.HEALTH.name()));
        models.stream().allMatch(c -> Category.HEALTH.getTypes().contains(c.getType()));
    }

    @Test
    public void calculateEnabledScoreValue() throws Exception {
        Category.EDUCATION.setEnabledForCategory(true);
        final double value = ScoringCalculator.calculateEnabledScoreValue(MapCanvas.CITY_LEIPZIG);
        Assert.assertEquals(16330.59, value, 0.1);

        ScoreType.SCHOOL.setEnabled(false);
        final double valueAfter = ScoringCalculator.calculateEnabledScoreValue(MapCanvas.CITY_LEIPZIG);
        Assert.assertEquals(10730.59, valueAfter, 0.1);
    }
}