package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.domain.ScoreValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.htwkvisu.org.pois.ScoreType.*;

public enum Category {
    HEALTH(PHARMACY, HOSPITAL, DENTIST, VETERINARY, DOCTORS, BLOOD_DONATION)
    , INFRASTRUCTURE(TERMINAL, HELIPAD, AERODROME, BUS, TRAIN, TRAM)
    , EDUCATION(SCHOOL, COLLEGE, LIBRARY, MUSEUM, RESEARCH_INSTITUTION, THEATRE);

    private List<ScoreType> types = new ArrayList<>();

    Category(ScoreType... subcategories) {
        types.addAll(Arrays.asList(subcategories));
    }

    public List<ScoreType> getTypes() {
        return types;
    }

    public Map<ScoreType, List<ScoreValue>> findAllInCategory() {
        return types.stream().collect(Collectors.toMap(Function.identity(), ScoreType::findAll));
    }

    public Map<ScoreType, List<BasicPOI>> generateDrawableInCategory() {
        return types.stream().collect(Collectors.toMap(Function.identity(), ScoreType::generateDrawable));
    }

    public List<ScoreValue> findAll() {
        return types.stream().flatMap(t -> t.findAll().stream()).collect(Collectors.toList());
    }

    public List<BasicPOI> generateDrawable() {
        return types.stream().flatMap(t -> t.generateDrawable().stream()).collect(Collectors.toList());
    }

    public double calculateCategoryValue(Point2D pt) {
        return types.stream().mapToDouble(t -> t.calculateScoreValue(pt)).sum();
    }

    public double calculateCategoryValueForCustom(Point2D pt) {
        return types.stream().mapToDouble(t -> t.calculateScoreValueForCustom(pt)).sum();
    }
}
