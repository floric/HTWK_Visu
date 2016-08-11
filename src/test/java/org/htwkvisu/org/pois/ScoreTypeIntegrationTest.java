package org.htwkvisu.org.pois;

import org.junit.Assert;
import org.junit.Before;
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
public class ScoreTypeIntegrationTest {

    private List<Integer> expectedSizes;

    @Before
    public void setUp() {
        expectedSizes = Stream.of(9, 40, 33, 6107, 774, 1582, 198, 860, 459, 96, 941, 3, 1502, 67, 245, 538, 4, 165)
                .collect(Collectors.toList());
    }

    @Test
    public void findAllReturnsExistingScoreValues() {
        List<ScoreType> scoreTypes = ScoreType.HELIPAD.allScoreTypes();
        List<Integer> sizes = scoreTypes.stream().map(t -> t.findAll().size()).collect(Collectors.toList());
        Assert.assertFalse(sizes.stream().anyMatch(s -> s == 0));
        Assert.assertEquals(sizes, expectedSizes);
    }

    @Test
    public void generateDrawableReturnsExistingScoreValues() {
        List<ScoreType> scoreTypes = ScoreType.HELIPAD.allScoreTypes();
        List<Integer> sizes = scoreTypes.stream().map(t -> t.generateDrawable().size()).collect(Collectors.toList());
        Assert.assertFalse(sizes.stream().anyMatch(s -> s == 0));
        Assert.assertEquals(sizes, expectedSizes);
    }
}