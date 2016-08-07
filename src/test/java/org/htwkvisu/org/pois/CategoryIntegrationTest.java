package org.htwkvisu.org.pois;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This test class is ignored, because of the ci-server.
 * TODO: please fix the integration of the score_values with the import-script
 */
@Ignore
public class CategoryIntegrationTest {

    private static final int EDUCATION_SIZE = 2521;
    private static final int HEALTH_SIZE = 2557;
    private static final int INFRASTRUCTURE_SIZE = 8545;

    @Test
    public void infrastructureContainsScoreTypes() {
        List<Integer> sizes = convertToSizes(Category.INFRASTRUCTURE);
        Assert.assertFalse(sizes.stream().anyMatch(s -> s == 0));
        Assert.assertEquals(sizes,
                Stream.of(9, 40, 33, 6107, 774, 1582)
                        .collect(Collectors.toList()));
    }

    @Test
    public void healthContainsScoreTypes() {
        List<Integer> sizes = convertToSizes(Category.HEALTH);
        Assert.assertFalse(sizes.stream().anyMatch(s -> s == 0));
        Assert.assertEquals(sizes,
                Stream.of(860, 198, 459, 96, 941, 3)
                        .collect(Collectors.toList()));
    }

    @Test
    public void educationContainsScoreTypes() {
        List<Integer> sizes = convertToSizes(Category.EDUCATION);
        Assert.assertFalse(sizes.stream().anyMatch(s -> s == 0));
        Assert.assertEquals(sizes,
                Stream.of(1502, 67, 245, 538, 4, 165)
                        .collect(Collectors.toList()));
    }

    @Test
    public void findAllReturnsExistingValues() {
        Assert.assertEquals(Category.EDUCATION.findAll().size(),  EDUCATION_SIZE);
        Assert.assertEquals(Category.HEALTH.findAll().size(), HEALTH_SIZE);
        Assert.assertEquals(Category.INFRASTRUCTURE.findAll().size(), INFRASTRUCTURE_SIZE);
    }

    @Test
    public void generateDrawableReturnsExistingValues() {
        Assert.assertEquals(Category.EDUCATION.generateDrawable().size(), EDUCATION_SIZE);
        Assert.assertEquals(Category.HEALTH.generateDrawable().size(), HEALTH_SIZE);
        Assert.assertEquals(Category.INFRASTRUCTURE.generateDrawable().size(), INFRASTRUCTURE_SIZE);
    }

    private static List<Integer> convertToSizes(Category category) {
        List<ScoreType> scoreTypes = category.getTypes();
        return scoreTypes.stream().map(t -> t.findAll().size()).collect(Collectors.toList());
    }
}