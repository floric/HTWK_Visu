package org.htwkvisu.domain;

import org.htwkvisu.org.pois.ScoreType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class ScoreValueTest {
    @Test
    public void notNullableObjectsExists() {
        Assert.assertTrue(ScoreType.TRAIN.allScoreTypes().stream()
                .flatMap(type -> type.findAll().stream())
                .allMatch(scoreValue -> Objects.nonNull(scoreValue.getName())
                        && Objects.nonNull(scoreValue.getOsmId())
                        && Objects.nonNull(scoreValue.getPoiId())
                        && Objects.nonNull(scoreValue.getPoint())));
    }
}